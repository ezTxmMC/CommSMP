package de.eztxm.smp.listener;

import de.eztxm.smp.SMP;
import de.eztxm.smp.util.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    private final SMP smp;

    public QuitListener(SMP smp) {
        this.smp = smp;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerManager playerManager = smp.getPlayerManager();
        if (playerManager.getScoreboardTasks().containsKey(player.getUniqueId())) {
            playerManager.getScoreboardTasks().get(player.getUniqueId()).cancel();
            playerManager.getScoreboardTasks().remove(player.getUniqueId());
        }
    }
}
