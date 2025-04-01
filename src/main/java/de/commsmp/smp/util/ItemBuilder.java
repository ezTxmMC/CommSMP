package de.commsmp.smp.util;

import de.commsmp.smp.SMP;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
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

    public ItemBuilder setItemModel(String resourceName) {
        checkForMeta();
        this.itemMeta.setItemModel(new NamespacedKey("commsmp", resourceName));
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
            return null;
        }
        checkForMeta();
        if (!(this.itemMeta instanceof SkullMeta)) {
            this.itemMeta = this.itemStack.getItemMeta();
        }
        SkullMeta skullMeta = (SkullMeta) this.itemMeta;
        skullMeta.setOwningPlayer(player);
        this.itemStack.setItemMeta(skullMeta);
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
        checkForMeta();
        if (!(this.itemMeta instanceof Damageable)) {
            this.itemMeta = this.itemStack.getItemMeta();
        }
        Damageable damageableMeta = (Damageable) this.itemMeta;
        damageableMeta.setDamage(durability);
        this.itemStack.setItemMeta(damageableMeta);
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

    public ItemBuilder addData(String key, String value) {
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        container.set(new NamespacedKey(SMP.getInstance(), key), PersistentDataType.STRING, value);
        return this;
    }

    public ItemBuilder removeData(String key) {
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        container.remove(new NamespacedKey(SMP.getInstance(), key));
        return this;
    }

    public String getData(String key) {
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        return container.get(new NamespacedKey(SMP.getInstance(), key), PersistentDataType.STRING);
    }

    public ItemStack build() {
        this.itemStack.setItemMeta(itemMeta);
        return this.itemStack;
    }
}
