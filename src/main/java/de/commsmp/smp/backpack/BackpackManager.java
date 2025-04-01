package de.commsmp.smp.backpack;

import de.commsmp.smp.SMP;
import de.commsmp.smp.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

public class BackpackManager {

    public void registerCrafting() {
        ItemStack leatherBackpack = new ItemBuilder(Material.BARRIER).customModelData(13001)
                .setDisplayName("Leder Rucksack")
                .setItemModel("backpack_leather").addData("backpack", "leather").build();
        ItemStack copperBackpack = new ItemBuilder(Material.BARRIER).customModelData(13002)
                .setDisplayName("Kupfer Rucksack")
                .setItemModel("backpack_leather").addData("backpack", "copper").build();
        ItemStack ironBackpack = new ItemBuilder(Material.BARRIER).customModelData(13003)
                .setDisplayName("Eisen Rucksack")
                .setItemModel("backpack_leather").addData("backpack", "iron").build();
        ItemStack goldBackpack = new ItemBuilder(Material.BARRIER).customModelData(13004)
                .setDisplayName("Gold Rucksack")
                .setItemModel("backpack_leather").addData("backpack", "gold").build();
        ItemStack diamondBackpack = new ItemBuilder(Material.BARRIER).customModelData(13005)
                .setDisplayName("Diamant Rucksack")
                .setItemModel("backpack_leather").addData("backpack", "diamond").build();
        ItemStack netheriteBackpack = new ItemBuilder(Material.BARRIER).customModelData(13006)
                .setDisplayName("Netherite Rucksack")
                .setItemModel("backpack_leather").addData("backpack", "netherite").build();

        ShapedRecipe leather = new ShapedRecipe(new NamespacedKey(SMP.getInstance(), "backpack_leather"),
                leatherBackpack);
        ShapedRecipe copper = new ShapedRecipe(new NamespacedKey(SMP.getInstance(), "backpack_copper"),
                copperBackpack);
        ShapedRecipe iron = new ShapedRecipe(new NamespacedKey(SMP.getInstance(), "backpack_iron"),
                ironBackpack);
        ShapedRecipe gold = new ShapedRecipe(new NamespacedKey(SMP.getInstance(), "backpack_gold"),
                goldBackpack);
        ShapedRecipe diamond = new ShapedRecipe(new NamespacedKey(SMP.getInstance(), "backpack_diamond"),
                diamondBackpack);
        ShapedRecipe netherite = new ShapedRecipe(new NamespacedKey(SMP.getInstance(), "backpack_netherite"),
                netheriteBackpack);

        leather.shape("xxx", "xzx", "xxx");
        leather.setIngredient('x', Material.LEATHER);
        leather.setIngredient('z', Material.CHEST);

        copper.shape("xyx", "xzx", "xyx");
        copper.setIngredient('x', Material.LEATHER);
        copper.setIngredient('z', new RecipeChoice.ExactChoice(leatherBackpack));
        copper.setIngredient('y', Material.COPPER_INGOT);

        iron.shape("xyx", "xzx", "xyx");
        iron.setIngredient('x', Material.LEATHER);
        iron.setIngredient('z', new RecipeChoice.ExactChoice(copperBackpack));
        iron.setIngredient('y', Material.IRON_INGOT);

        gold.shape("xyx", "xzx", "xyx");
        gold.setIngredient('x', Material.LEATHER);
        gold.setIngredient('z', new RecipeChoice.ExactChoice(ironBackpack));
        gold.setIngredient('y', Material.GOLD_INGOT);

        diamond.shape("xyx", "xzx", "xyx");
        diamond.setIngredient('x', Material.LEATHER);
        diamond.setIngredient('z', new RecipeChoice.ExactChoice(goldBackpack));
        diamond.setIngredient('y', Material.DIAMOND);

        netherite.shape("xyx", "xzx", "xyx");
        netherite.setIngredient('x', Material.NETHERITE_SCRAP);
        netherite.setIngredient('z', new RecipeChoice.ExactChoice(diamondBackpack));
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
