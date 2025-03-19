package de.eztxm.smp.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
        if (message.startsWith("!")) {
            event.setCancelled(true);
            if (this.emptyArea(player, 30)) {
                player.sendMessage(Component.text(
                        "Du konntest niemanden mit deiner Nachricht erreichen. Nutze '@' vor deiner Nachricht, um mit allen zu schreiben.",
                        NamedTextColor.RED));
                return;
            }
            this.sendMessage(player, message, 30);
            return;
        }
        event.setCancelled(true);
        if (this.emptyArea(player, 15)) {
            player.sendMessage(Component.text(
                    "Du konntest niemanden mit deiner Nachricht erreichen. Nutze '!' vor deiner Nachricht, um weitere Distanz zu schreiben und '@', um mit allen zu schreiben.",
                    NamedTextColor.RED));
            return;
        }
        this.sendMessage(player, message, 15);
    }

    private void sendMessage(Player player, String message, int radius) {
        player.getLocation().getWorld().getNearbyEntities(player.getLocation(), radius, radius, radius).forEach(entity -> {
            if (entity instanceof Player target) {
                target.sendMessage(Component.text(message));
            }
        });
    }

    private boolean emptyArea(Player player, int radius) {
        return player.getLocation().getWorld().getNearbyEntities(player.getLocation(), radius, radius, radius).isEmpty();
    }
}
