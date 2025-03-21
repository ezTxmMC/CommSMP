package de.eztxm.smp.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        if (message.startsWith("@")) {
            event.setCancelled(false);
            return;
        }

        int radius = message.startsWith("!") ? 30 : 15;
        boolean global = message.startsWith("!");
        String trimmedMessage = global ? message.substring(1).trim() : message;

        boolean reachedSomeone = sendMessage(player, trimmedMessage, radius);
        if (!reachedSomeone) {
            player.sendMessage(Component.text(global ? "Du konntest niemanden mit deiner Nachricht erreichen. Nutze '@' vor deiner Nachricht, um mit allen zu schreiben." : "Du konntest niemanden mit deiner Nachricht erreichen. Nutze '!' vor deiner Nachricht, um weitere Distanz zu schreiben und '@', um mit allen zu schreiben.", NamedTextColor.RED));
        }

        event.setCancelled(true);
    }

    private boolean sendMessage(Player sender, String message, int radius) {
        boolean reachedSomeone = false;

        for (Entity entity : sender.getLocation().getWorld().getNearbyEntities(sender.getLocation(), radius, radius, radius)) {
            if (entity instanceof Player target) {
                Component formattedMessage = formatMentionMessage(target, message);
                target.sendMessage(formattedMessage);
                reachedSomeone = true;
            }
        }
        return reachedSomeone;
    }

    private Component formatMentionMessage(Player receiver, String message) {
        Component finalMessage = Component.empty();
        String[] words = message.split(" ");

        for (String word : words) {
            Player mentionedPlayer = Bukkit.getPlayerExact(word);
            if (mentionedPlayer != null && mentionedPlayer.isOnline()) {
                if (mentionedPlayer.getUniqueId().equals(receiver.getUniqueId())) {
                    receiver.playSound(receiver.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1f, 1f);
                }
                finalMessage = finalMessage.append(Component.text("@", NamedTextColor.AQUA)).append(Component.text(mentionedPlayer.getName(), NamedTextColor.AQUA)).append(Component.space());
                continue;
            }
            finalMessage = finalMessage.append(Component.text(word)).append(Component.space());
        }
        return finalMessage;
    }
}
