package de.eztxm.smp.listener;

import de.eztxm.smp.util.ItemBuilder;
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
            ItemStack itemStack = new ItemBuilder(Material.PLAYER_HEAD).toItemStack();
            SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
            itemMeta.setPlayerProfile(player.getPlayerProfile());
            itemStack.setItemMeta(itemMeta);
            player.getLocation().getWorld().dropItemNaturally(player.getLocation(), itemStack);
        }
    }
}
