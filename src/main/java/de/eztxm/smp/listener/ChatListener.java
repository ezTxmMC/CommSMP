package de.eztxm.smp.listener;

import com.openai.client.OpenAIClient;
import com.openai.client.OpenAIClientAsync;
import com.openai.client.okhttp.OpenAIOkHttpClientAsync;
import com.openai.models.Moderation;
import com.openai.models.ModerationCreateParams;
import com.openai.models.ModerationCreateResponse;
import com.openai.models.ModerationModel;
import de.eztxm.smp.SMP;
import de.eztxm.smp.util.AdventureColor;
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener implements Listener {

    private final OpenAIClientAsync client;

    public ChatListener() {
        client = OpenAIOkHttpClientAsync.builder().apiKey(SMP.getInstance().getMainConfig().getOpenAIKey()).build();
    }

    @EventHandler
    public void onChat(PlayerChatEvent event) {
        Player sender = event.getPlayer();
        String message = event.getMessage();

        if(filter(event.getMessage())) {
            sender.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix() + "Bitte achte auf deine Wortwahl!"));
            event.setCancelled(true);
            return;
        }

        boolean global = false;
        int radius = 15;

        if (message.startsWith("@g")) {
            global = true;
            message = message.substring(2).trim();
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

        if(global) {
            for(Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(formatMessageForReceiver(message, true, sender, player));
            }
            return true;
        }

        for (Entity entity : sender.getLocation().getWorld().getNearbyEntities(sender.getLocation(), radius, radius, radius)) {
            if (entity instanceof Player receiver) {
                Component formattedMessage = formatMessageForReceiver(message, global, sender, receiver);
                receiver.sendMessage(formattedMessage);
                reachedSomeone = true;
            }
        }
        return reachedSomeone;
    }

    private Component formatMessageForReceiver(String message, boolean global, Player sender, Player receiver) {
        String mentionPattern = "(?i)" + Pattern.quote("@" + receiver.getName());
        Pattern pattern = Pattern.compile(mentionPattern);
        Matcher matcher = pattern.matcher(message);

        Component globalComponent = global
                ? Component.text("[G] ", NamedTextColor.DARK_GRAY)
                : Component.empty();

        Component base = Component.text(sender.getName() + " » ", NamedTextColor.YELLOW);

        if (matcher.find()) {
            matcher.reset();
            List<Component> parts = new ArrayList<>();
            int lastEnd = 0;

            while(matcher.find()) {
                if(matcher.start() > lastEnd) {
                    parts.add(Component.text(message.substring(lastEnd, matcher.start()), NamedTextColor.WHITE));
                }
                parts.add(Component.text(message.substring(matcher.start(), matcher.end()), NamedTextColor.AQUA));
                lastEnd = matcher.end();
            }
            if(lastEnd < message.length()){
                parts.add(Component.text(message.substring(lastEnd), NamedTextColor.WHITE));
            }

            Component messageComponent = Component.join(JoinConfiguration.noSeparators(), parts);
            receiver.playSound(receiver.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1f, 1f);

            return globalComponent.append(base).append(messageComponent);
        }

        return globalComponent.append(base).append(Component.text(message, NamedTextColor.WHITE));
    }

    private boolean filter(String message) {
        ModerationCreateParams params = ModerationCreateParams.builder().input(message.replaceAll("@", "").replaceAll("\\*", "").replaceAll("\\+", "")).model(ModerationModel.TEXT_MODERATION_STABLE).build();
        ModerationCreateResponse response = client.moderations().create(params).join();
        return response.results().stream().anyMatch(Moderation::flagged);
    }
}
