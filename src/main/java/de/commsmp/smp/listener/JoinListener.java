package de.commsmp.smp.listener;

import de.commsmp.smp.SMP;
import de.commsmp.smp.scoreboard.SpawnScoreboard;
import de.commsmp.smp.util.GraveStoneHandler;
import de.commsmp.smp.util.Mode;
import de.commsmp.smp.util.PlayerManager;
import de.commsmp.smp.util.Status;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class JoinListener implements Listener {

    private final SMP smp;

    public JoinListener(SMP smp) {
        this.smp = smp;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        SpawnScoreboard scoreboard = new SpawnScoreboard(player);
        BukkitTask scoreboardTask = Bukkit.getScheduler().runTaskTimerAsynchronously(smp, scoreboard::update, 5L,
                20 * 3L);
        PlayerManager playerManager = smp.getPlayerManager();
        playerManager.getScoreboardTasks().put(player.getUniqueId(), scoreboardTask);
        playerManager.getTeamchat().put(player.getUniqueId(), true);
        Status status = new Status(player);
        playerManager.getStatus().put(player.getUniqueId(), status);
        Mode mode = new Mode(player);
        playerManager.getModes().put(player.getUniqueId(), mode);
        playerManager.getGraveStones().put(player.getUniqueId(), new HashMap<>());
        GraveStoneHandler graveStoneHandler = smp.getGraveStoneHandler();
        graveStoneHandler.getGraveStoneInteractables().put(player.getUniqueId(), new HashMap<>());
    }
}
