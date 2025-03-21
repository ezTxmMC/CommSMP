package de.eztxm.smp.util;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemBuilder {
    private final ItemStack itemStack;
    private ItemMeta itemMeta;

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    private void checkForMeta() {
        if (this.itemMeta == null) {
            this.itemMeta = this.itemStack.getItemMeta();
        }
    }

    public ItemBuilder name(Component name) {
        checkForMeta();
        this.itemMeta.displayName(name);
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        this.itemStack.addEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder disenchant(Enchantment enchantment) {
        this.itemStack.removeEnchantment(enchantment);
        return this;
    }

    public ItemBuilder customModelData(int id) {
        checkForMeta();
        this.itemMeta.setCustomModelData(id);
        return this;
    }

    public ItemBuilder maxStackSize(int stackSize) {
        checkForMeta();
        this.itemMeta.setMaxStackSize(stackSize);
        return this;
    }

    public ItemBuilder lore(Component... lines) {
        checkForMeta();
        this.itemMeta.lore(List.of(lines));
        return this;
    }

    public ItemBuilder editLoreLine(int line, Component content) {
        checkForMeta();
        List<Component> lore = this.itemMeta.lore();
        if (lore != null && line >= 0 && line < lore.size()) {
            lore.set(line, content);
            this.itemMeta.lore(lore);
        }
        return this;
    }

    public ItemBuilder itemFlags(ItemFlag... flags) {
        checkForMeta();
        this.itemMeta.addItemFlags(flags);
        return this;
    }

    public ItemBuilder unbreakable() {
        checkForMeta();
        this.itemMeta.setUnbreakable(true);
        return this;
    }

    public ItemBuilder breakable() {
        checkForMeta();
        this.itemMeta.setUnbreakable(false);
        return this;
    }

    public ItemBuilder hideToolTip() {
        checkForMeta();
        this.itemMeta.setHideTooltip(true);
        return this;
    }

    public ItemBuilder glint() {
        checkForMeta();
        this.itemMeta.setEnchantmentGlintOverride(true);
        return this;
    }

    public ItemBuilder setSkullOwner(@Nullable Player player) {
        if (this.itemStack.getType() != Material.PLAYER_HEAD) {
            this.itemStack.setType(Material.PLAYER_HEAD);
        }
        checkForMeta();
        if (!(this.itemMeta instanceof SkullMeta)) {
            this.itemMeta = this.itemStack.getItemMeta();
        }
        SkullMeta skullMeta = (SkullMeta) this.itemMeta;
        skullMeta.setPlayerProfile(player.getPlayerProfile());
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(player.getName()));
        this.itemMeta = skullMeta;
        return this;
    }

    public ItemBuilder setDisplayName(String displayName) {
        checkForMeta();
        this.itemMeta.displayName(AdventureColor.apply(displayName));
        return this;
    }

    public ItemBuilder removeLore() {
        checkForMeta();
        this.itemMeta.lore(new ArrayList<>());
        return this;
    }

    public ItemBuilder setLore(List<Component> lore) {
        checkForMeta();
        this.itemMeta.lore(lore);
        return this;
    }

    public ItemBuilder setDurability(short durability) {
        this.itemStack.setDurability(durability);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level, boolean unsafe) {
        if (unsafe) {
            this.itemStack.addUnsafeEnchantment(enchantment, level);
        } else {
            this.itemStack.addEnchantment(enchantment, level);
        }
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        return addEnchantment(enchantment, level, false);
    }

    public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments, boolean unsafe) {
        if (unsafe) {
            this.itemStack.addUnsafeEnchantments(enchantments);
        } else {
            this.itemStack.addEnchantments(enchantments);
        }
        return this;
    }

    public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {
        return addEnchantments(enchantments, false);
    }

    public ItemStack build() {
        this.itemStack.setItemMeta(itemMeta);
        return this.itemStack;
    }
}
