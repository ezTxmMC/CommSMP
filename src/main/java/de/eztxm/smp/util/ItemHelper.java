package de.eztxm.smp.util;

import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemHelper {

    private final ItemStack itemStack;
    private ItemMeta itemMeta;

    public ItemHelper(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemHelper(Material material, int amount) {
        this.itemStack = new ItemStack(material, amount);
    }

    public ItemHelper(@NotNull Material type, int amount, short damage) {
        this.itemStack = new ItemStack(type, amount, damage);
    }

    public ItemHelper(Material material) {
        this.itemStack = new ItemStack(material);
    }

    public ItemHelper setMaterial(Material material) {
        this.itemStack.setType(material);
        return this;
    }

    public ItemHelper setUnbreakable(boolean unbreakable) {
        checkForMeta();
        if (unbreakable) {
            return unbreakable();
        }
        return breakable();
    }

    public ItemHelper unbreakable() {
        checkForMeta();
        itemMeta.setUnbreakable(true);
        return this;
    }

    public ItemHelper breakable() {
        checkForMeta();
        itemMeta.setUnbreakable(false);
        return this;
    }

    public ItemHelper setDisplayName(String displayName) {
        checkForMeta();
        this.itemMeta.displayName(AdventureColor.apply(displayName));
        return this;
    }

    public ItemHelper addItemFlag(ItemFlag... flag) {
        checkForMeta();
        this.itemMeta.addItemFlags(flag);
        return this;
    }

    public ItemHelper removeItemFlag(ItemFlag... flag) {
        checkForMeta();
        this.itemMeta.removeItemFlags(flag);
        return this;
    }

    public ItemHelper removeLore() {
        checkForMeta();
        this.itemMeta.lore(new ArrayList<>());
        return this;
    }

    public ItemHelper setLore(List<Component> lore) {
        checkForMeta();
        this.itemMeta.lore(lore);
        return this;
    }

    public ItemHelper setAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemHelper setDurability(short durability) {
        this.itemStack.setDurability(durability);
        return this;
    }

    public ItemHelper addEnchantment(Enchantment enchantment, int level, boolean unsafe) {
        if (unsafe) {
            this.itemStack.addUnsafeEnchantment(enchantment, level);
            return this;
        }
        this.itemStack.addEnchantment(enchantment, level);
        return this;
    }

    public ItemHelper addEnchantments(Map<Enchantment, Integer> enchantments, boolean unsafe) {
        if (unsafe) {
            this.itemStack.addUnsafeEnchantments(enchantments);
            return this;
        }
        this.itemStack.addEnchantments(enchantments);
        return this;
    }

    public ItemHelper addEnchantment(Enchantment enchantment, int level) {
        return addEnchantment(enchantment, level, false);
    }

    public ItemHelper addEnchantments(Map<Enchantment, Integer> enchantments) {
        return addEnchantments(enchantments, false);
    }

    public ItemHelper addUnsafeEnchantments(Map<Enchantment, Integer> enchantments) {
        return addEnchantments(enchantments, true);
    }

    public ItemHelper addUnsafeEnchantment(Enchantment enchantment, int level) {
        return addEnchantment(enchantment, level, true);
    }


    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private void checkForMeta() {
        if (this.itemMeta == null) {
            this.itemMeta = itemStack.getItemMeta();
        }
    }
}
