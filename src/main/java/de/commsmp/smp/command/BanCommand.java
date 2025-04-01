package de.commsmp.smp.command;

import java.time.Instant;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import de.commsmp.smp.SMP;
import de.commsmp.smp.command.api.SimpleCommand;
import de.commsmp.smp.config.BanConfig;
import de.commsmp.smp.config.data.BannedPlayer;
import de.eztxm.ezlib.config.reflect.JsonProcessor;

public class BanCommand implements SimpleCommand {

    @Override
    public void execute(String label, CommandSender sender, String[] args) {
        JsonProcessor<BanConfig> processor = SMP.getInstance().getBanProcessor();
        BanConfig config = processor.getInstance();
        switch (label.toLowerCase()) {
            case "ban" -> {
                if (args.length < 2) {
                    sender.sendMessage("/ban <player> <duration>");
                    return;
                }
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                if (target == null) {
                    sender.sendMessage("Target null");
                    return;
                }
                if (isBanned(config, target.getUniqueId())) {
                    sender.sendMessage("Target already banned");
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
                BannedPlayer bannedPlayer = new BannedPlayer();
                bannedPlayer.setUniqueId(target.getUniqueId().toString());
                bannedPlayer.setDuration(Integer.parseInt(args[1]));
                bannedPlayer.setTimestamp(Instant.now().toString());
                bannedPlayer.setReason(builder.toString());
                config.getBannedPlayers().add(bannedPlayer);
                processor.saveConfiguration();
                sender.sendMessage("Banned target");
            }
            case "unban" -> {
                if (args.length < 0) {
                    sender.sendMessage("/unban <player>");
                    return;
                }
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                if (target == null) {
                    sender.sendMessage("Target null");
                    return;
                }
                if (!isBanned(config, target.getUniqueId())) {
                    sender.sendMessage("Target is not banned");
                    return;
                }
                int index = 0;
                for (BannedPlayer bannedPlayer : config.getBannedPlayers()) {
                    if (!bannedPlayer.getUniqueId().equalsIgnoreCase(target.getUniqueId().toString())) {
                        index++;
                        continue;
                    }
                    break;
                }
                config.getBannedPlayers().remove(index);
                processor.saveConfiguration();
                sender.sendMessage("Unbanned target");
            }
        }
    }

    private boolean isBanned(BanConfig config, UUID uniqueId) {
        boolean banned = false;
        for (BannedPlayer bannedPlayer : config.getBannedPlayers()) {
            if (!bannedPlayer.getUniqueId().equalsIgnoreCase(uniqueId.toString())) {
                continue;
            }
            banned = true;
        }
        return banned;
    }
}
