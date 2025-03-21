package de.eztxm.smp.listener;

import de.eztxm.smp.util.ItemHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (player.getKiller() != null) {
            player.getLocation().getWorld().dropItemNaturally(player.getLocation(), new ItemHelper(Material.PLAYER_HEAD)
                    .setSkullProfile(player.getPlayerProfile()).build());
        }
    }
}
