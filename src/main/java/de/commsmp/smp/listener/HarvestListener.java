package de.commsmp.smp.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class HarvestListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }

        if (block.getBlockData() instanceof Ageable) {
            Ageable ageable = (Ageable) block.getBlockData();

            if (ageable.getAge() == ageable.getMaximumAge()) {
                event.setCancelled(true);
                Material dropMaterial = getDropMaterial(block.getType());
                if (dropMaterial != null) {
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(dropMaterial));
                }
                ageable.setAge(0);
                block.setBlockData(ageable);
            }
        }
    }

    private Material getDropMaterial(Material blockType) {
        switch (blockType) {
            case CARROTS:
                return Material.CARROT;
            case POTATOES:
                return Material.POTATO;
            case BEETROOTS:
                return Material.BEETROOT;
            default:
                return blockType;
        }
    }

}
