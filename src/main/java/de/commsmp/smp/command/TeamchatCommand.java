package de.commsmp.smp.command;

import de.commsmp.smp.SMP;
import de.commsmp.smp.command.api.SimpleCommand;
import de.commsmp.smp.util.PlayerManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamchatCommand implements SimpleCommand {

    @Override
    public void execute(String label, CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return;
        if (args.length == 0) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("""
                    /teamchat <message>
                    /teamchat enable
                    /teamchat disable"""));
            return;
        }
        PlayerManager playerManager = SMP.getInstance().getPlayerManager();
        switch (args[0].toLowerCase()) {
            case "enable" -> {
                if (playerManager.getTeamchat().get(player.getUniqueId())) {
                    player.sendMessage(Component.text("Teamchat ist bereits aktiviert"));
                    return;
                }
                playerManager.getTeamchat().put(player.getUniqueId(), true);
                player.sendMessage(Component.text("Teamchat aktiviert"));
            }
            case "disable" -> {
                if (playerManager.getTeamchat().get(player.getUniqueId()) == null || !playerManager.getTeamchat().get(player.getUniqueId())) {
                    player.sendMessage(Component.text("Teamchat ist bereits deaktiviert"));
                    return;
                }
                playerManager.getTeamchat().put(player.getUniqueId(), false);
                player.sendMessage(Component.text("Teamchat deaktiviert"));
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
    }
}
