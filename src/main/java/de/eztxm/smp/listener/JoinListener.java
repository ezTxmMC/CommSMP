package de.eztxm.smp.listener;

import de.eztxm.smp.SMP;
import de.eztxm.smp.scoreboard.SpawnScoreboard;
import de.eztxm.smp.util.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitTask;

public class JoinListener implements Listener {

    private final SMP smp;

    public JoinListener(SMP smp) {
        this.smp = smp;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        SpawnScoreboard scoreboard = new SpawnScoreboard(player);
        BukkitTask scoreboardTask = Bukkit.getScheduler().runTaskTimerAsynchronously(smp, scoreboard::update, 5L, 20 * 3L);
        PlayerManager playerManager = smp.getPlayerManager();
        playerManager.getScoreboardTasks().put(player.getUniqueId(), scoreboardTask);
    }
}
