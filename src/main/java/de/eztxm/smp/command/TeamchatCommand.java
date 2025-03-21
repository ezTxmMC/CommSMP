package de.eztxm.smp.command;

import de.eztxm.smp.SMP;
import de.eztxm.smp.util.PlayerManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TeamchatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) return false;
        if (args.length == 0) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("""
                    /teamchat <message>
                    /teamchat enable
                    /teamchat disable"""));
            return false;
        }
        PlayerManager playerManager = SMP.getInstance().getPlayerManager();
        switch (args[0].toLowerCase()) {
            case "enable" -> {
                if (playerManager.getTeamchat().get(player.getUniqueId())) {
                    player.sendMessage(Component.text("Teamchat ist bereits aktiviert"));
                    return false;
                }
                playerManager.getTeamchat().put(player.getUniqueId(), true);
                player.sendMessage(Component.text("Teamchat aktiviert"));
                return true;
            }
            case "disable" -> {
                if (playerManager.getTeamchat().get(player.getUniqueId()) == null || !playerManager.getTeamchat().get(player.getUniqueId())) {
                    player.sendMessage(Component.text("Teamchat ist bereits deaktiviert"));
                    return false;
                }
                playerManager.getTeamchat().put(player.getUniqueId(), false);
                player.sendMessage(Component.text("Teamchat deaktiviert"));
                return true;
            }
            default -> {
                StringBuilder message = new StringBuilder();
                for (int i = 0; i < args.length; i++) {
                    if (i != 0) {
                        message.append(" ");
                    }
                    message.append(args[i]);
                }
                Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                    if (!onlinePlayer.hasPermission("commsmp.command.teamchat")) {
                        return;
                    }
                    if (playerManager.getTeamchat().get(onlinePlayer.getUniqueId()) == null || !playerManager.getTeamchat().get(onlinePlayer.getUniqueId())) {
                        return;
                    }
                    player.sendMessage(MiniMessage.miniMessage().deserialize("@Team - " + player.getName() + " >> " + message));
                });
            }
        }
        return true;
    }
}
