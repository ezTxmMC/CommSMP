package de.commsmp.smp.listener;

import de.commsmp.smp.SMP;
import de.commsmp.smp.scoreboard.SpawnScoreboard;
import de.commsmp.smp.util.AdventureColor;
import de.commsmp.smp.util.GraveStoneHandler;
import de.commsmp.smp.util.Mode;
import de.commsmp.smp.util.PlayerManager;
import de.commsmp.smp.util.Status;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class JoinListener implements Listener {

    private final SMP smp;

    public JoinListener(SMP smp) {
        this.smp = smp;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPlayedBefore()) {
            if (!smp.getMainConfig().getSpawnLocation().isEmpty()) {
                String[] spawnLocation = smp.getMainConfig().getSpawnLocation().split(";");
                Location location = new Location(Bukkit.getWorld(spawnLocation[0]), Integer.parseInt(spawnLocation[1]),
                        Integer.parseInt(spawnLocation[2]), Integer.parseInt(spawnLocation[3]),
                        Integer.parseInt(spawnLocation[4]), Integer.parseInt(spawnLocation[5]));
                player.teleport(location);
            }
        }

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

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        UUID uniqueId = event.getUniqueId();
        if (smp.getBanConfig().isBanned(uniqueId)) {
            event.disallow(Result.KICK_BANNED, AdventureColor.apply("Du wurdest von dem Server gebannt!"));
            return;
        }
    }
}
