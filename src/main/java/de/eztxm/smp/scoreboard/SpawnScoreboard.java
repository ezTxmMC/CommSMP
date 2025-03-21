package de.eztxm.smp.scoreboard;

import de.eztxm.smp.SMP;
import de.eztxm.smp.util.AdventureColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class SpawnScoreboard {
    private final Scoreboard scoreboard;
    private final Player player;

    public SpawnScoreboard(Player player) {
        ScoreboardBuilder builder = new ScoreboardBuilder()
                .create()
                .objective(
                        "display",
                        Criteria.DUMMY,
                        AdventureColor.apply("<#005fff><bold> CommSMP </bold>"))
                .displaySlot(DisplaySlot.SIDEBAR)
                .score(15, "§0                    §0")
                .score(14, "§7Rang:")
                .score(13, ChatColor.BLACK.toString())
                .score(12, "§0                    §1")
                .score(11, "§7Diamanten:")
                .score(10, ChatColor.DARK_GRAY.toString())
                .score(9, "§1                    §0")
                .score(8, "§1                    §1")
                .score(7, "§x§0§0§5§f§f§f§o          smp.eztxm.de");
        this.scoreboard = builder.getScoreboard();
        this.player = player;
        Team rank = this.scoreboard.registerNewTeam("rank");
        rank.setPrefix("§8> §cLoading");
        rank.addEntry(ChatColor.BLACK.toString());
        Team diamonds = this.scoreboard.registerNewTeam("diamonds");
        diamonds.setPrefix("§8> §cLoading");
        diamonds.addEntry(ChatColor.DARK_GRAY.toString());
        this.player.setScoreboard(this.scoreboard);
    }

    public void update() {
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
        rank.setPrefix("§8> " + (group.getDisplayName() == null ? "" : ChatColor.translateAlternateColorCodes('&', LegacyComponentSerializer.legacySection().serialize(AdventureColor.apply(group.getDisplayName())))));
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
        diamonds.setPrefix("§8> §b" + diamondCount);
    }
}
