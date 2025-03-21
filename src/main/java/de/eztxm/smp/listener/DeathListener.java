package de.eztxm.smp.listener;

import de.eztxm.smp.util.ItemBuilder;
import de.eztxm.smp.util.ItemHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (player.getKiller() != null) {
            ItemStack stack = new ItemBuilder(Material.PLAYER_HEAD).toItemStack();
            SkullMeta meta = (SkullMeta) stack.getItemMeta();
            meta.setOwningPlayer(player);
            stack.setItemMeta(meta);
            player.getLocation().getWorld().dropItemNaturally(player.getLocation(), stack);
        }
    }
}
