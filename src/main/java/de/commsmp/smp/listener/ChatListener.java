package de.commsmp.smp.listener;

import de.commsmp.smp.util.AdventureColor;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player sender = event.getPlayer();
        Component message = event.message();

        boolean global = false;
        boolean scream = false;
        int radius = 15;

        if (message.insertion().startsWith("@g")) {
            global = true;
            message.insertion(message.insertion().substring(2).trim());
        }

        if (message.insertion().startsWith("!")) {
            scream = true;
            radius = 30;
            message.insertion(message.insertion().substring(1).trim());
        }

        boolean reachedSomeone = sendMessage(sender, message, global, scream, radius);
        if (!reachedSomeone) {
            String hint = global
                    ? "Du konntest niemanden mit deiner Nachricht erreichen. Nutze '@' vor deiner Nachricht, um mit allen zu schreiben."
                    : "Du konntest niemanden mit deiner Nachricht erreichen. Nutze '!' vor deiner Nachricht, um weitere Distanz zu schreiben und '@', um mit allen zu schreiben.";
            sender.sendMessage(Component.text(hint, NamedTextColor.RED));
        }

        event.setCancelled(true);
    }

    private boolean sendMessage(Player sender, Component message, boolean global, boolean scream, int radius) {
        boolean reachedSomeone = false;

        String mentionedName = extractMention(message.insertion());
        if (mentionedName != null) {
            Player mentionedPlayer = Bukkit.getPlayerExact(mentionedName);
            if (mentionedPlayer == null || !mentionedPlayer.isOnline()) {
                mentionedName = null;
            }
            if (mentionedPlayer != null && mentionedPlayer.isOnline()) {
                mentionedName = mentionedPlayer.getName();
            }
        }

        if (global) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(formatMessageForReceiver(message, true, scream, sender, player, mentionedName));
            }
            return true;
        }

        for (Entity entity : sender.getLocation().getWorld().getNearbyEntities(sender.getLocation(), radius, radius,
                radius)) {
            if (entity instanceof Player receiver) {
                Component formattedMessage = formatMessageForReceiver(message, false, scream, sender, receiver,
                        mentionedName);
                receiver.sendMessage(formattedMessage);
                reachedSomeone = true;
            }
        }
        return reachedSomeone;
    }

    private Component formatMessageForReceiver(Component message, boolean global, boolean scream, Player sender,
            Player receiver, String mentionedName) {
        Component globalComponent = global
                ? AdventureColor.apply("&8[&eG&8] ")
                : scream
                        ? AdventureColor.apply("&8[&cRuft&8] ")
                        : Component.empty();
        Component base = AdventureColor.apply("<yellow>" + sender.getName() + " <dark_gray>» ");

        if (mentionedName != null) {
            String mention = "@" + mentionedName;
            Pattern pattern = Pattern.compile("(?i)" + Pattern.quote(mention));
            Matcher matcher = pattern.matcher(message.insertion());
            List<Component> parts = new ArrayList<>();
            int lastEnd = 0;
            while (matcher.find()) {
                if (matcher.start() > lastEnd) {
                    String before = message.insertion().substring(lastEnd, matcher.start());
                    parts.add(AdventureColor.apply(before));
                }
                String hit = message.insertion().substring(matcher.start(), matcher.end());
                parts.add(AdventureColor.apply("&b" + hit));
                lastEnd = matcher.end();
            }
            if (lastEnd < message.insertion().length()) {
                parts.add(AdventureColor.apply("&7" + message.insertion().substring(lastEnd)));
            }
            Component messageComponent = Component.join(JoinConfiguration.noSeparators(), parts);

            if (receiver.getName().equalsIgnoreCase(mentionedName)) {
                receiver.playSound(receiver.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1f, 1f);
            }
            return globalComponent.append(base).append(messageComponent);
        }
        return globalComponent.append(base).append(message);
    }

    public String extractMention(String message) {
        Pattern pattern = Pattern.compile("(?i)@([a-zA-Z0-9_]+)");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    /*
     * private CompletableFuture<FilterResult> filter(String message) {
     * return CompletableFuture.supplyAsync(() -> {
     * String cleanedMessage = message.replaceAll("@", "").replaceAll("[*+]",
     * "").toLowerCase();
     * 
     * if (moderationCache.containsKey(cleanedMessage)) {
     * return new FilterResult(moderationCache.get(cleanedMessage),
     * EnumSet.noneOf(FilterCategory.class));
     * }
     * 
     * EnumSet<FilterCategory> blacklist = Blacklist.checkMessage(message);
     * if (!blacklist.isEmpty()) {
     * return new FilterResult(true, blacklist);
     * }
     * 
     * ModerationCreateParams params = ModerationCreateParams.builder()
     * .input(cleanedMessage)
     * .model(ModerationModel.TEXT_MODERATION_LATEST)
     * .build();
     * 
     * ModerationCreateResponse response =
     * client.moderations().create(params).join();
     * Moderation result = response.results().getFirst();
     * 
     * 
     * if (!result.flagged()) {
     * 
     * params = ModerationCreateParams.builder()
     * .input(message)
     * .model(ModerationModel.OMNI_MODERATION_LATEST)
     * .build();
     * 
     * response = client.moderations().create(params).join();
     * result = response.results().getFirst();
     * }
     * 
     * boolean flagged = result.flagged();
     * EnumSet<FilterCategory> categories = EnumSet.noneOf(FilterCategory.class);
     * 
     * if (result.categories().sexual()) categories.add(FilterCategory.SEXUAL);
     * if (result.categories().hate()) categories.add(FilterCategory.HATE);
     * if (result.categories().harassment())
     * categories.add(FilterCategory.HARASSMENT);
     * if (result.categories().violence()) categories.add(FilterCategory.VIOLENCE);
     * if (result.categories().selfHarm()) categories.add(FilterCategory.SELF_HARM);
     * 
     * if (cleanedMessage.contains("hitler") || cleanedMessage.contains("nazi")) {
     * flagged = true;
     * categories.add(FilterCategory.EXTREME_RIGHTWING);
     * }
     * 
     * moderationCache.put(cleanedMessage, flagged);
     * return new FilterResult(flagged, categories);
     * }, Executors.newCachedThreadPool());
     * }
     */
}
