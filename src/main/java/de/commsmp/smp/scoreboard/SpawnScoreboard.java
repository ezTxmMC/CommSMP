package de.commsmp.smp.scoreboard;

import de.commsmp.smp.SMP;
import de.commsmp.smp.util.AdventureColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.*;

import java.util.Objects;

public class SpawnScoreboard {
    private final Scoreboard scoreboard;
    private final Objective objective;
    private final Player player;

    public SpawnScoreboard(Player player) {
        ScoreboardBuilder builder = new ScoreboardBuilder()
                .create()
                .objective(
                        "display",
                        Criteria.DUMMY,
                        AdventureColor.apply("<#005fff><bold> CommSMP </bold>"))
                .score(15, "§0                    §0")
                .score(14, "§7Rang:")
                .score(13, ChatColor.BLACK.toString())
                .score(12, "§0                    §1")
                .score(11, "§7Diamanten:")
                .score(10, ChatColor.DARK_GRAY.toString())
                .score(9, "§1                    §0")
                .score(8, "§1                    §1")
                .score(7, LegacyComponentSerializer.legacySection()
                        .serialize(AdventureColor.apply("&1     <#005fff><italic>commsmp.de</italic>     &1")));
        this.scoreboard = builder.getScoreboard();
        this.objective = builder.getObjective();
        this.player = player;
        if (this.isPlayerInRadius(player, new Location(Bukkit.getWorld("world"), 0, 64, 0), 50)) {
            builder.displaySlot(DisplaySlot.SIDEBAR);
        }
        Team rank = this.scoreboard.registerNewTeam("rank");
        rank.prefix(AdventureColor.apply("<dark_gray>» <red>Loading"));
        rank.addEntry(ChatColor.BLACK.toString());
        Team diamonds = this.scoreboard.registerNewTeam("diamonds");
        diamonds.prefix(AdventureColor.apply("<dark_gray>» <red>Loading"));
        diamonds.addEntry(ChatColor.DARK_GRAY.toString());
        this.player.setScoreboard(this.scoreboard);
    }

    public void update() {
        if (this.isPlayerInRadius(player, new Location(Bukkit.getWorld("world"), 0, 64, 0), 50)
                && !Objects.equals(objective.getDisplaySlot(), DisplaySlot.SIDEBAR)) {
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        } else {
            objective.setDisplaySlot(null);
        }
        LuckPerms luckPerms = SMP.getInstance().getLuckPerms();
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        assert user != null;
        Group group = luckPerms.getGroupManager().getGroup(user.getPrimaryGroup());
        Team rank = this.scoreboard.getTeam("rank");
        if (rank == null) {
            rank = this.scoreboard.registerNewTeam("rank");
            rank.addEntry(ChatColor.BLACK.toString());
        }
        assert group != null;
        rank.prefix(AdventureColor.apply("<dark_gray>» <gray>" + (group.getDisplayName() == null ? ""
                : group.getDisplayName())));
        Team diamonds = this.scoreboard.getTeam("diamonds");
        if (diamonds == null) {
            diamonds = this.scoreboard.registerNewTeam("diamonds");
            diamonds.addEntry(ChatColor.DARK_GRAY.toString());
        }
        int diamondCount = 0;
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack == null || itemStack.getType() == Material.AIR) {
                continue;
            }
            if (itemStack.getType() == Material.DIAMOND) {
                diamondCount += itemStack.getAmount();
            }
        }
        diamonds.prefix(AdventureColor.apply("<dark_gray>» <aqua>" + diamondCount));
    }

    private boolean isPlayerInRadius(Player player, Location center, double radius) {
        if (!player.getWorld().equals(center.getWorld()))
            return false;
        return player.getLocation().distanceSquared(center) <= radius * radius;
    }
}
