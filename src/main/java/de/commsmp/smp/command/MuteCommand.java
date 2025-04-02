package de.commsmp.smp.command;

import de.commsmp.smp.SMP;
import de.commsmp.smp.command.api.SimpleCommand;
import de.commsmp.smp.config.MuteConfig;
import de.commsmp.smp.config.data.MutedPlayer;
import de.eztxm.ezlib.config.reflect.JsonProcessor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.time.Instant;

public class MuteCommand implements SimpleCommand {

    @Override
    public void execute(String label, CommandSender sender, String[] args) {
        JsonProcessor<MuteConfig> processor = SMP.getInstance().getConfigProvider().getProcessor(MuteConfig.class);
        MuteConfig config = processor.getInstance();
        switch (label.toLowerCase()) {
            case "mute" -> {
                if (args.length < 2) {
                    sender.sendMessage("/mute <player> <duration>");
                    return;
                }
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                if (target == null) {
                    sender.sendMessage("Target null");
                    return;
                }
                if (config.isMuted(target.getUniqueId())) {
                    sender.sendMessage("Target already muted");
                    return;
                }
                StringBuilder builder = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    builder.append(args[i]);
                    if (i == args.length - 1) {
                        continue;
                    }
                    builder.append(" ");
                }
                MutedPlayer mutedPlayer = new MutedPlayer();
                mutedPlayer.setUniqueId(target.getUniqueId().toString());
                mutedPlayer.setDuration(Integer.parseInt(args[1]));
                mutedPlayer.setTimestamp(Instant.now().toString());
                mutedPlayer.setReason(builder.toString());
                config.getMutedPlayers().add(mutedPlayer);
                processor.saveConfiguration();
                sender.sendMessage("Muted target");
            }
            case "unmute" -> {
                if (args.length < 0) {
                    sender.sendMessage("/unmute <player>");
                    return;
                }
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                if (target == null) {
                    sender.sendMessage("Target null");
                    return;
                }
                if (!config.isMuted(target.getUniqueId())) {
                    sender.sendMessage("Target is not muted");
                    return;
                }
                int index = 0;
                for (MutedPlayer mutedPlayer : config.getMutedPlayers()) {
                    if (!mutedPlayer.getUniqueId().equalsIgnoreCase(target.getUniqueId().toString())) {
                        index++;
                        continue;
                    }
                    break;
                }
                config.getMutedPlayers().remove(index);
                processor.saveConfiguration();
                sender.sendMessage("Unmuted target");
            }
        }
    }
}
