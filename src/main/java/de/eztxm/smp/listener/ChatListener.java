package de.eztxm.smp.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(PlayerChatEvent event) {
        Player sender = event.getPlayer();
        String message = event.getMessage();

        boolean global = false;
        int radius = 15;

        if (message.startsWith("!")) {
            global = true;
            radius = 30;
            message = message.substring(1).trim();
        }

        if (message.startsWith("@") && global) {
            message = message.substring(1).trim();
        }

        boolean reachedSomeone = sendMessage(sender, message, global, radius);
        if (!reachedSomeone) {
            String hint = global ? "Du konntest niemanden mit deiner Nachricht erreichen. Nutze '@' vor deiner Nachricht, um mit allen zu schreiben." : "Du konntest niemanden mit deiner Nachricht erreichen. Nutze '!' vor deiner Nachricht, um weitere Distanz zu schreiben und '@', um mit allen zu schreiben.";
            sender.sendMessage(Component.text(hint, NamedTextColor.RED));
        }

        event.setCancelled(true);
    }

    private boolean sendMessage(Player sender, String message, boolean global, int radius) {
        boolean reachedSomeone = false;

        for (Entity entity : sender.getLocation().getWorld().getNearbyEntities(sender.getLocation(), radius, radius, radius)) {
            if (entity instanceof Player receiver) {
                Component formattedMessage = formatMessageForReceiver(message, global, receiver);
                receiver.sendMessage(formattedMessage);
                reachedSomeone = true;
            }
        }
        return reachedSomeone;
    }

    private Component formatMessageForReceiver(String message, boolean global, Player receiver) {
        String receiverName = receiver.getName();
        String lowerMessage = message.toLowerCase();
        String lowerName = receiverName.toLowerCase();

        if (lowerMessage.contains(lowerName)) {
            int index = lowerMessage.indexOf(lowerName);
            String before = message.substring(0, index);
            String namePart = message.substring(index, index + receiverName.length());
            String after = message.substring(index + receiverName.length());

            String prefix = global ? "" : "@";

            receiver.playSound(receiver.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1f, 1f);

            return Component.text(before)
                    .append(Component.text(prefix + namePart, NamedTextColor.AQUA))
                    .append(Component.text(after));
        }
        return Component.text(message);
    }
}
