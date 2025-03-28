package de.commsmp.smp.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

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
        if (block.getBlockData() instanceof Ageable ageable) {
            if (ageable.getAge() != ageable.getMaximumAge()) {
                return;
            }
            event.setCancelled(true);
            Material dropMaterial = getDropMaterial(block.getType());
            if (dropMaterial != null) {
                block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(
                        dropMaterial,
                        new Random().nextInt(3)
                ));
            }
            ageable.setAge(0);
            block.setBlockData(ageable);
        }
    }

    private Material getDropMaterial(Material blockType) {
        return switch (blockType) {
            case CARROTS -> Material.CARROT;
            case POTATOES -> Material.POTATO;
            case BEETROOTS -> Material.BEETROOT;
            case WHEAT_SEEDS -> Material.WHEAT;
            default -> blockType;
        };
    }
}
