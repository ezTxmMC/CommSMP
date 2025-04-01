package de.commsmp.smp.command;

import de.commsmp.smp.SMP;
import de.commsmp.smp.command.api.SimpleCommand;
import de.commsmp.smp.config.LockConfig;
import de.commsmp.smp.util.AdventureColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LockCommand implements SimpleCommand {

    LockConfig config = SMP.getInstance().getLockConfig();

    @Override
    public void execute(String label, CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix() + "&cDieser Command ist nur für Spieler verfügbar!"));
            return;
        }

        Block targetBlock = player.getTargetBlockExact(5);
        if (targetBlock == null) {
            player.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix()
                    + "&cKein Zielblock gefunden. Bitte schaue auf einen Block, um ihn zu locken oder unlocken."));
            return;
        }
        Location location = targetBlock.getLocation();

        switch (label.toLowerCase()) {
            case "lock" -> handleLock(player, location);
            case "unlock" -> handleUnlock(player, location);
            case "trust" -> handleTrust(player, location, args);
            case "untrust" -> handleUntrust(player, location, args);
            default -> player.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix()
                    + "&cUnbekannter Command! Nutze: lock, unlock, trust oder untrust."));
        }
    }

    private void handleLock(Player player, Location location) {
        if (config.isLocked(location)) {
            player.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix()
                    + "&cDieser Block ist bereits gelockt!"));
            return;
        }
        config.lock(location, player.getUniqueId());
        player.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix()
                + "&aBlock erfolgreich gelockt. Enjoy your private zone!"));
    }

    private void handleUnlock(Player player, Location location) {
        if (!config.isLocked(location)) {
            player.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix()
                    + "&cDieser Block ist gar nicht gelockt!"));
            return;
        }
        UUID owner = config.getOwner(location);
        if (!player.getUniqueId().equals(owner)) {
            player.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix()
                    + "&cDu bist nicht der Owner dieses Blocks!"));
            return;
        }
        config.unlock(location);
        player.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix()
                + "&aBlock erfolgreich unlocked. Jetzt hast du wieder Zugriff!"));
    }

    private void handleTrust(Player player, Location location, String[] args) {
        if (!config.isLocked(location)) {
            player.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix()
                    + "&cDieser Block ist nicht gelockt!"));
            return;
        }
        UUID owner = config.getOwner(location);
        if (!player.getUniqueId().equals(owner)) {
            player.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix()
                    + "&cDu bist nicht der Owner dieses Blocks!"));
            return;
        }
        if (args.length < 1) {
            player.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix()
                    + "&7Bitte gib den Namen des Spielers an, den du trusten möchtest. Beispiel: /trust <Spielername>"));
            return;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix()
                    + "&cSpieler nicht gefunden!"));
            return;
        }
        List<UUID> trusted = config.getTrusted(location);
        if (trusted.contains(target.getUniqueId())) {
            player.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix()
                    + "&cDieser Spieler ist bereits vertrauenswürdig!"));
            return;
        }
        config.addTrusted(location, target.getUniqueId());
        player.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix()
                + "&aSpieler " + target.getName() + " wurde erfolgreich getrusted."));
    }

    private void handleUntrust(Player player, Location location, String[] args) {
        if (!config.isLocked(location)) {
            player.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix()
                    + "&cDieser Block ist nicht gelockt!"));
            return;
        }
        UUID owner = config.getOwner(location);
        if (!player.getUniqueId().equals(owner)) {
            player.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix()
                    + "&cDu bist nicht der Owner dieses Blocks!"));
            return;
        }
        if (args.length < 1) {
            player.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix()
                    + "&7Bitte gib den Namen des Spielers an, den du untrusten möchtest. Beispiel: /untrust <Spielername>"));
            return;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix()
                    + "&cSpieler nicht gefunden!"));
            return;
        }
        List<UUID> trusted = config.getTrusted(location);
        if (!trusted.contains(target.getUniqueId())) {
            player.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix()
                    + "&cDieser Spieler ist nicht vertrauenswürdig!"));
            return;
        }
        config.removeTrusted(location, target.getUniqueId());
        player.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix()
                + "&aSpieler " + target.getName() + " wurde aus der Trusted-Liste entfernt."));
    }

    @Override
    public List<String> suggest(String label, CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            return List.of();
        }
        switch (label.toLowerCase()) {
            case "trust" -> {
                if (args.length == 1) {
                    String input = args[0].toLowerCase();
                    List<String> suggestions = new ArrayList<>();
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (!p.getName().equalsIgnoreCase(player.getName()) && p.getName().toLowerCase().startsWith(input)) {
                            suggestions.add(p.getName());
                        }
                    }
                    return suggestions;
                }
            }
            case "untrust" -> {
                if (args.length == 1) {
                    String input = args[0].toLowerCase();
                    List<String> suggestions = new ArrayList<>();
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getName().toLowerCase().startsWith(input)) {
                            suggestions.add(p.getName());
                        }
                    }
                    return suggestions;
                }
            }
            default -> {
                return SimpleCommand.super.suggest(label, sender, args);
            }
        }
        return SimpleCommand.super.suggest(label, sender, args);
    }
}
