package de.commsmp.smp.listener;

import de.commsmp.smp.SMP;
import de.commsmp.smp.listener.filter.Moderation;
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

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener implements Listener {

    private final ConcurrentHashMap<String, Boolean> moderationCache = new ConcurrentHashMap<>();

    //private final OpenAIClientAsync client;
    private final Moderation moderation;
    private final AtomicBoolean filtered = new AtomicBoolean();

    public ChatListener() {
        //client = OpenAIOkHttpClientAsync.builder().apiKey(SMP.getInstance().getMainConfig().getOpenAIKey()).build();
        moderation = new Moderation(SMP.getInstance().getMainConfig().getOpenAIKey());
    }

    @EventHandler
    public void onChat(PlayerChatEvent event) {
        Player sender = event.getPlayer();
        String message = event.getMessage();

        if(moderation.filter(message)) {
            sender.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix() + "Bitte achte auf deine Wortwahl!"));
            event.setCancelled(true);
            Bukkit.getScheduler().runTaskLaterAsynchronously(SMP.getInstance(), () -> {
                String aiOutput = moderation.answer(sender.getName(), event.getMessage());
                sender.sendMessage(AdventureColor.apply("<gradient:#FB0808:#EEA400><bold>Aurora</gradient> <dark_gray><bold>|</bold> <gray>" + aiOutput));
            }, 1L);
            return;
        }
        /*filter(event.getMessage()).thenAccept(result -> {
            if (result.flagged()) {
                sender.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix() + "Bitte achte auf deine Wortwahl!"));
                event.setCancelled(true);
                filtered.set(true);
            }
        });

        if (filtered.get()) {
            filtered.set(false);
            return;
        }*/

        boolean global = false;
        boolean scream = false;
        int radius = 15;

        if (message.startsWith("@g")) {
            global = true;
            message = message.substring(2).trim();
        }

        if (message.startsWith("!")) {
            scream = true;
            radius = 30;
            message = message.substring(1).trim();
        }

        boolean reachedSomeone = sendMessage(sender, message, global, scream, radius);
        if (!reachedSomeone) {
            String hint = global ? "Du konntest niemanden mit deiner Nachricht erreichen. Nutze '@' vor deiner Nachricht, um mit allen zu schreiben." : "Du konntest niemanden mit deiner Nachricht erreichen. Nutze '!' vor deiner Nachricht, um weitere Distanz zu schreiben und '@', um mit allen zu schreiben.";
            sender.sendMessage(Component.text(hint, NamedTextColor.RED));
        }

        event.setCancelled(true);
    }

    private boolean sendMessage(Player sender, String message, boolean global, boolean scream, int radius) {
        boolean reachedSomeone = false;

        String mentionedName = extractMention(message);
        if(mentionedName != null) {
            Player mentionedPlayer = Bukkit.getPlayerExact(mentionedName);
            if(mentionedPlayer == null || !mentionedPlayer.isOnline()) {
                mentionedName = null;
            }
            if(mentionedPlayer != null || mentionedPlayer.isOnline()) {
                mentionedName = mentionedPlayer.getName();
            }
        }

        if(global) {
            for(Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(formatMessageForReceiver(message, true, scream, sender, player, mentionedName));
            }
            return true;
        }

        for(Entity entity : sender.getLocation().getWorld().getNearbyEntities(sender.getLocation(), radius, radius, radius)) {
            if(entity instanceof Player receiver) {
                Component formattedMessage = formatMessageForReceiver(message, false, scream, sender, receiver, mentionedName);
                receiver.sendMessage(formattedMessage);
                reachedSomeone = true;
            }
        }
        return reachedSomeone;
    }

    private Component formatMessageForReceiver(String message, boolean global, boolean scream, Player sender, Player receiver, String mentionedName) {
        Component globalComponent = global
                ? AdventureColor.apply("&8[&eG&8] ")
                : scream
                ? AdventureColor.apply("&8[&cRuft&8] ")
                : Component.empty();
        Component base = AdventureColor.apply("<yellow>" + sender.getName() + " <dark_gray>» ");

        if (mentionedName != null) {
            String mention = "@" + mentionedName;
            Pattern pattern = Pattern.compile("(?i)" + Pattern.quote(mention));
            Matcher matcher = pattern.matcher(message);
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
                parts.add(AdventureColor.apply("&7" + message.substring(lastEnd)));
            }
            Component messageComponent = Component.join(JoinConfiguration.noSeparators(), parts);

            if (receiver.getName().equalsIgnoreCase(mentionedName)) {
                receiver.playSound(receiver.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1f, 1f);
            }
            return globalComponent.append(base).append(messageComponent);
        }
        return globalComponent.append(base).append(AdventureColor.apply(message));
    }

    public String extractMention(String message) {
        Pattern pattern = Pattern.compile("(?i)@([a-zA-Z0-9_]+)");
        Matcher matcher = pattern.matcher(message);
        if(matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    /*private CompletableFuture<FilterResult> filter(String message) {
        return CompletableFuture.supplyAsync(() -> {
            String cleanedMessage = message.replaceAll("@", "").replaceAll("[*+]", "").toLowerCase();

            if (moderationCache.containsKey(cleanedMessage)) {
                return new FilterResult(moderationCache.get(cleanedMessage), EnumSet.noneOf(FilterCategory.class));
            }

            EnumSet<FilterCategory> blacklist = Blacklist.checkMessage(message);
            if (!blacklist.isEmpty()) {
                return new FilterResult(true, blacklist);
            }

            ModerationCreateParams params = ModerationCreateParams.builder()
                    .input(cleanedMessage)
                    .model(ModerationModel.TEXT_MODERATION_LATEST)
                    .build();

            ModerationCreateResponse response = client.moderations().create(params).join();
            Moderation result = response.results().getFirst();


            if (!result.flagged()) {

                params = ModerationCreateParams.builder()
                        .input(message)
                        .model(ModerationModel.OMNI_MODERATION_LATEST)
                        .build();

                response = client.moderations().create(params).join();
                result = response.results().getFirst();
            }

            boolean flagged = result.flagged();
            EnumSet<FilterCategory> categories = EnumSet.noneOf(FilterCategory.class);

            if (result.categories().sexual()) categories.add(FilterCategory.SEXUAL);
            if (result.categories().hate()) categories.add(FilterCategory.HATE);
            if (result.categories().harassment()) categories.add(FilterCategory.HARASSMENT);
            if (result.categories().violence()) categories.add(FilterCategory.VIOLENCE);
            if (result.categories().selfHarm()) categories.add(FilterCategory.SELF_HARM);

            if (cleanedMessage.contains("hitler") || cleanedMessage.contains("nazi")) {
                flagged = true;
                categories.add(FilterCategory.EXTREME_RIGHTWING);
            }

            moderationCache.put(cleanedMessage, flagged);
            return new FilterResult(flagged, categories);
        }, Executors.newCachedThreadPool());
    }*/
}
