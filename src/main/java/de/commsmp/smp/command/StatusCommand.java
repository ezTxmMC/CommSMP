package de.commsmp.smp.command;

import de.commsmp.smp.SMP;
import de.commsmp.smp.command.api.SimpleCommand;
import de.commsmp.smp.util.Status;
import de.commsmp.smp.util.StatusType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatusCommand implements SimpleCommand {

    @Override
    public void execute(String label, CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            return;
        }
        if (label.equalsIgnoreCase("status")) {
            if (args.length == 0) {
                player.sendMessage(
                        SMP.getInstance().getPrefix() + "/status <#33ffff><status>");
                return;
            }
            label = args[0];
        }
        Status status = SMP.getInstance().getPlayerManager().getStatus().get(player.getUniqueId());
        StatusType statusType = StatusType.valueOf(label.toUpperCase());
        status.setListName(statusType);
        status.updateHead(statusType);
        player.sendMessage(
                SMP.getInstance().getPrefix() + "Dein Status wurde zu <#33ffff>" + statusType.name()
                        + " <gray>geändert.");
    }

    @Override
    public List<String> suggest(String label, CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> types = new ArrayList<>();
            for (StatusType statusType : StatusType.values()) {
                types.add(statusType.name());
            }
            types.removeIf(type -> !args[0].startsWith(type));
            return types;
        }
        return Collections.emptyList();
    }
}
