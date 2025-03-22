package de.eztxm.smp.listener;

import de.eztxm.smp.SMP;
import de.eztxm.smp.inventory.GraveStoneInventory;
import de.eztxm.smp.util.GraveStone;
import de.eztxm.smp.util.ItemBuilder;
import de.eztxm.smp.util.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerManager playerManager = SMP.getInstance().getPlayerManager();
        GraveStone graveStone = new GraveStone(
                player,
                new GraveStoneInventory(player),
                playerManager.getGraveStones().get(player.getUniqueId()).size() + 1,
                player.getLocation());
        playerManager.getGraveStones().get(player.getUniqueId()).put(
                playerManager.getGraveStones().get(player.getUniqueId()).size() + 1,
                graveStone
        );
        graveStone.spawn();
        player.getInventory().clear();
        if (player.getKiller() != null) {
            player.getLocation().getWorld().dropItemNaturally(player.getLocation(), new ItemBuilder(Material.PLAYER_HEAD).setSkullOwner(player).build());
        }
    }
}
