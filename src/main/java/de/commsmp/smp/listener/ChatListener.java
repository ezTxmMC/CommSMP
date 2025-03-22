package de.commsmp.smp.listener;

import de.commsmp.smp.SMP;
import de.commsmp.smp.listener.filter.Blacklist;
import de.commsmp.smp.listener.filter.FilterCategory;
import de.commsmp.smp.listener.filter.ModerationModel;
import de.commsmp.smp.util.AdventureColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener implements Listener {

    private record FilterResult(boolean flagged, EnumSet<FilterCategory> categories) {
    }

    private final ConcurrentHashMap<String, Boolean> moderationCache = new ConcurrentHashMap<>();
    private final ModerationModel moderationModel;

    public ChatListener() {
        try {
            File modelFile = new File(SMP.getInstance().getDataFolder().getAbsolutePath() + "/ai/chatModel.bin");
            moderationModel = new ModerationModel();
            if (modelFile.exists()) {
                moderationModel.loadModel(SMP.getInstance().getDataFolder().getAbsolutePath() + "/ai/chatModel.bin");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Fehler beim Initialisieren des ChatModerationModel", e);
        }
    }

    @EventHandler
    public void onChat(PlayerChatEvent event) {
        Player sender = event.getPlayer();
        String plainText = event.getMessage();

        if (plainText.toLowerCase().startsWith("!train ")) {
            handleTrainCommand(sender, plainText);
            event.setCancelled(true);
            return;
        }

        /*FilterResult result = filter(plainText);

        if (result.flagged()) {
            sender.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix() + "Bitte achte auf deine Wortwahl!"));
            event.setCancelled(true);
            return;
        }*/

        boolean global = false;
        boolean scream = false;
        int radius = 15;

        if (plainText.startsWith("@g")) {
            global = true;
            plainText = plainText.substring(1).trim();
        }

        if (plainText.startsWith("!")) {
            scream = true;
            radius = 30;
            plainText = plainText.substring(1).trim();
        }

        boolean reachedSomeone = sendMessage(sender, plainText, global, scream, radius);
        if (!reachedSomeone) {
            String hint = global
                    ? "Du konntest niemanden mit deiner Nachricht erreichen. Nutze '@' vor deiner Nachricht, um mit allen zu schreiben."
                    : "Du konntest niemanden mit deiner Nachricht erreichen. Nutze '!' vor deiner Nachricht, um weitere Distanz zu schreiben und '@', um mit allen zu schreiben.";
            sender.sendMessage(Component.text(hint, NamedTextColor.RED));
        }

        event.setCancelled(true);
    }

    private void handleTrainCommand(Player sender, String plainText) {
        if (!sender.hasPermission("smp.ai.train")) {
            sender.sendMessage(AdventureColor.apply("Du hast keine Berechtigung, Trainingsbefehle zu nutzen."));
            return;
        }

        String[] parts = plainText.split("\\s+", 3);
        if (parts.length < 3) {
            sender.sendMessage(AdventureColor.apply("Falsches Format. Verwende: !train <ok|flagged> <Nachricht>"));
            return;
        }

        String label = parts[1].toLowerCase();
        if (!("ok".equals(label) || "flagged".equals(label))) {
            sender.sendMessage(AdventureColor.apply("Ungültiges Label. Erlaubt sind 'ok' oder 'flagged'."));
            return;
        }

        String trainingMessage = parts[2];

        try {
            moderationModel.updateModel(trainingMessage, label);
            moderationModel.saveModel(SMP.getInstance().getDataFolder().getAbsolutePath() + "/ai/chatModel.bin");
            sender.sendMessage(AdventureColor.apply("Trainingsbeispiel hinzugefügt: " + trainingMessage));
        } catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage(AdventureColor.apply("Fehler beim Aktualisieren des Modells."));
        }
    }

    private boolean sendMessage(Player sender, String message, boolean global, boolean scream, int radius) {
        boolean reachedSomeone = false;

        if (global) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(formatMessageForReceiver(message, true, false, sender, player));
            }
            return true;
        }

        for (Entity entity : sender.getLocation().getWorld().getNearbyEntities(sender.getLocation(), radius, radius, radius)) {
            if (entity instanceof Player receiver) {
                Component formattedMessage = formatMessageForReceiver(message, global, scream, sender, receiver);
                receiver.sendMessage(formattedMessage);
                reachedSomeone = true;
            }
        }
        return reachedSomeone;
    }

    private Component formatMessageForReceiver(String message, boolean global, boolean scream, Player sender, Player receiver) {
        String mention = "@" + receiver.getName();
        String regex = "(?i)" + Pattern.quote(mention);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);

        Component globalComponent = global ? AdventureColor.apply("&8[&eG&8] ") : scream ? AdventureColor.apply("&8[&cRuft&8]") : Component.empty();
        Component base = AdventureColor.apply("<yellow>" + sender.getName() + " <dark_gray>» ");

        if (matcher.find()) {
            matcher.reset();
            List<Component> parts = new ArrayList<>();
            int lastEnd = 0;

            while (matcher.find()) {
                if (matcher.start() > lastEnd) {
                    String before = message.substring(lastEnd, matcher.start());
                    parts.add(AdventureColor.apply(before));
                }
                String hit = message.substring(matcher.start(), matcher.end());
                parts.add(AdventureColor.apply("&b" + hit));
                lastEnd = matcher.end();
            }
            if (lastEnd < message.length()) {
                parts.add(AdventureColor.apply(message.substring(lastEnd)));
            }

            Component messageComponent = Component.join(JoinConfiguration.noSeparators(), parts).color(NamedTextColor.GRAY);
            receiver.playSound(receiver.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1f, 1f);
            return globalComponent.append(base).append(messageComponent);
        }

        return globalComponent.append(base).append(AdventureColor.apply(message));
    }

    private FilterResult filter(String message) {
        String cleanedMessage = message.replaceAll("@", "").replaceAll("[*+!_-]", "").toLowerCase();

        if (moderationCache.containsKey(cleanedMessage)) {
            return new FilterResult(moderationCache.get(cleanedMessage), EnumSet.noneOf(FilterCategory.class));
        }

        EnumSet<FilterCategory> blacklist = Blacklist.checkMessage(message);
        if (!blacklist.isEmpty()) {
            moderationCache.put(cleanedMessage, true);
            return new FilterResult(true, blacklist);
        }

        boolean flagged = false;
        EnumSet<FilterCategory> categories = EnumSet.noneOf(FilterCategory.class);

        try {
            String classification = moderationModel.classify(message);
            if ("flagged".equalsIgnoreCase(classification)) {
                flagged = true;
                categories.add(FilterCategory.HARASSMENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cleanedMessage.contains("hitler") || cleanedMessage.contains("nazi")) {
            flagged = true;
            categories.add(FilterCategory.EXTREME_RIGHTWING);
        }

        moderationCache.put(cleanedMessage, flagged);
        return new FilterResult(flagged, categories);
    }

}