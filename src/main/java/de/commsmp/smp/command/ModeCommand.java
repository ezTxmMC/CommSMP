package de.commsmp.smp.command;

import de.commsmp.smp.SMP;
import de.commsmp.smp.command.api.SimpleCommand;
import de.commsmp.smp.util.Mode;
import de.commsmp.smp.util.ModeType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModeCommand implements SimpleCommand {

    @Override
    public void execute(String label, CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            return;
        }
        if (label.equalsIgnoreCase("mode")) {
            if (args.length == 0) {
                player.sendMessage(
                        SMP.getInstance().getPrefix() + "/mode <#33ffff><mode>");
                return;
            }
            label = args[0];
        }
        Mode mode = SMP.getInstance().getPlayerManager().getModes().get(player.getUniqueId());
        ModeType modeType = ModeType.valueOf(label.toUpperCase());
        if (modeType.equals(ModeType.PASSIVE)) {
            // Check if the player is not in combat
        }
        mode.setListName(modeType);
        mode.updateHead(modeType);
        player.sendMessage(
                SMP.getInstance().getPrefix() + "Dein Modus wurde zu <#33ffff>" + modeType.name()
                        + " <gray>geändert.");
    }

    @Override
    public List<String> suggest(String label, CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> types = new ArrayList<>();
            for (ModeType modeType : ModeType.values()) {
                types.add(modeType.name());
            }
            types.removeIf(type -> !args[0].startsWith(type));
            return types;
        }
        return Collections.emptyList();
    }
}
