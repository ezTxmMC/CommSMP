package de.commsmp.smp.command;

import de.commsmp.smp.SMP;
import de.commsmp.smp.command.api.SimpleCommand;
import de.commsmp.smp.config.Config;
import de.commsmp.smp.util.AdventureColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SetSpawnCommand implements SimpleCommand {

    @Override
    public String permission() {
        return "commsmp.smp.setspawn";
    }

    @Override
    public void execute(String label, CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix()
                    + "&cDieser Command ist nur für Spieler verfügbar!"));
            return;
        }

        Location loc = player.getLocation();
        String spawnLocation = loc.getWorld().getName() + ";"
                + loc.getBlockX() + ";"
                + loc.getBlockY() + ";"
                + loc.getBlockZ() + ";"
                + loc.getYaw() + ";"
                + loc.getPitch();

        Config config = SMP.getInstance().getMainConfig();
        config.setSpawnLocation(spawnLocation);

        player.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix()
                + "&aSpawn location gesetzt auf: " + spawnLocation));
    }

    @Override
    public List<String> suggest(String label, CommandSender sender, String[] args) {
        return SimpleCommand.super.suggest(label, sender, args);
    }
}
