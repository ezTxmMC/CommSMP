package de.eztxm.smp.lock;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class LockUtil {

    public static boolean isLockable(final Block block) {
        final Material type = block.getType();

        return type == Material.CHEST || type == Material.TRAPPED_CHEST || type == Material.FURNACE || type == Material.BLAST_FURNACE
                || type == Material.SMOKER || type == Material.BARREL || type == Material.SHULKER_BOX || type == Material.WHITE_SHULKER_BOX
                || type == Material.ORANGE_SHULKER_BOX || type == Material.MAGENTA_SHULKER_BOX || type == Material.LIGHT_BLUE_SHULKER_BOX
                || type == Material.YELLOW_SHULKER_BOX || type == Material.LIME_SHULKER_BOX || type == Material.PINK_SHULKER_BOX
                || type == Material.GRAY_SHULKER_BOX || type == Material.LIGHT_GRAY_SHULKER_BOX || type == Material.CYAN_SHULKER_BOX
                || type == Material.PURPLE_SHULKER_BOX || type == Material.BLUE_SHULKER_BOX || type == Material.BROWN_SHULKER_BOX
                || type == Material.GREEN_SHULKER_BOX || type == Material.RED_SHULKER_BOX || type == Material.BLACK_SHULKER_BOX
                || type == Material.IRON_DOOR || isWoodenDoor(type) || type == Material.IRON_TRAPDOOR || isWoodenTrapdoor(type)
                || type == Material.ENDER_CHEST || type == Material.DISPENSER || type == Material.DROPPER;
    }

    private static boolean isWoodenDoor(final Material type) {
        return type == Material.OAK_DOOR || type == Material.SPRUCE_DOOR || type == Material.BIRCH_DOOR || type == Material.JUNGLE_DOOR
                || type == Material.ACACIA_DOOR || type == Material.DARK_OAK_DOOR || type == Material.CRIMSON_DOOR
                || type == Material.WARPED_DOOR || type == Material.MANGROVE_DOOR || type == Material.BAMBOO_DOOR
                || type == Material.CHERRY_DOOR;
    }

    private static boolean isWoodenTrapdoor(final Material type) {
        return type == Material.OAK_TRAPDOOR || type == Material.SPRUCE_TRAPDOOR || type == Material.BIRCH_TRAPDOOR
                || type == Material.JUNGLE_TRAPDOOR || type == Material.ACACIA_TRAPDOOR || type == Material.DARK_OAK_TRAPDOOR
                || type == Material.CRIMSON_TRAPDOOR || type == Material.WARPED_TRAPDOOR || type == Material.MANGROVE_TRAPDOOR
                || type == Material.BAMBOO_TRAPDOOR || type == Material.CHERRY_TRAPDOOR;
    }

}
