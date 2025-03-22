package de.commsmp.smp.backpack;

import de.commsmp.smp.SMP;
import de.commsmp.smp.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;

public class BackpackManager {

    public BackpackManager() {

    }

    public void registerCrafting() {
        ShapedRecipe leather = new ShapedRecipe(new NamespacedKey(SMP.getInstance(), "leather"), new ItemBuilder(Material.BROWN_SHULKER_BOX).build());
        ShapedRecipe copper = new ShapedRecipe(new NamespacedKey(SMP.getInstance(), "copper"), new ItemBuilder(Material.RED_SHULKER_BOX).build());
        ShapedRecipe iron = new ShapedRecipe(new NamespacedKey(SMP.getInstance(), "iron"), new ItemBuilder(Material.LIGHT_GRAY_SHULKER_BOX).build());
        ShapedRecipe gold = new ShapedRecipe(new NamespacedKey(SMP.getInstance(), "gold"), new ItemBuilder(Material.ORANGE_SHULKER_BOX).build());
        ShapedRecipe diamond = new ShapedRecipe(new NamespacedKey(SMP.getInstance(), "diamond"), new ItemBuilder(Material.LIGHT_BLUE_SHULKER_BOX).build());
        ShapedRecipe netherite = new ShapedRecipe(new NamespacedKey(SMP.getInstance(), "netherite"), new ItemBuilder(Material.BLACK_SHULKER_BOX).build());

        leather.shape("xxx", "xzx", "xxx");
        leather.setIngredient('x', Material.LEATHER);
        leather.setIngredient('z', Material.CHEST);

        copper.shape("xyx", "xzx", "xyx");
        copper.setIngredient('x', Material.LEATHER);
        copper.setIngredient('z', Material.BROWN_SHULKER_BOX); // Leather Backpack
        copper.setIngredient('y', Material.COPPER_INGOT);

        iron.shape("xyx", "xzx", "xyx");
        iron.setIngredient('x', Material.LEATHER);
        iron.setIngredient('z', Material.RED_SHULKER_BOX); // Copper Backpack
        iron.setIngredient('y', Material.IRON_INGOT);

        gold.shape("xyx", "xzx", "xyx");
        gold.setIngredient('x', Material.LEATHER);
        gold.setIngredient('z', Material.LIGHT_GRAY_SHULKER_BOX); // Iron Backpack
        gold.setIngredient('y', Material.GOLD_INGOT);

        diamond.shape("xyx", "xzx", "xyx");
        diamond.setIngredient('x', Material.LEATHER);
        diamond.setIngredient('z', Material.ORANGE_SHULKER_BOX); // Gold Backpack
        diamond.setIngredient('y', Material.DIAMOND);

        netherite.shape("xyx", "xzx", "xyx");
        netherite.setIngredient('x', Material.NETHERITE_SCRAP);
        netherite.setIngredient('z', Material.LIGHT_BLUE_SHULKER_BOX); // Diamond Backpack
        netherite.setIngredient('y', Material.GOLD_INGOT);

        Bukkit.addRecipe(leather);
        Bukkit.addRecipe(copper);
        Bukkit.addRecipe(iron);
        Bukkit.addRecipe(gold);
        Bukkit.addRecipe(diamond);
        if (SMP.getInstance().getMainConfig().isNetheriteEnabled()) {
            Bukkit.addRecipe(netherite); // Disabled by now
        }

    }
}
