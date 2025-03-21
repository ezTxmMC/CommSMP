package de.eztxm.smp.util;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemBuilder {
    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    /***
     * Erstelle mit dem Constructor ein ItemStack mit einem Material.
     * @param material - Material
     */
    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
    }

    /***
     * Nutze einen ItemStack im ItemBuilder.
     * @param itemStack - ItemStack
     */
    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    /***
     * Setze den Namen des ItemStacks.
     * @param name - Displayname
     * @return - Gibt die Klasse für Oneliner zurück.
     */
    public ItemBuilder name(Component name) {
        this.itemMeta.displayName(name);
        return this;
    }

    /***
     * Setze die Anzahl des ItemStacks.
     * @param amount - Amount
     * @return - Gibt die Klasse für Oneliner zurück.
     */
    public ItemBuilder amount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    /***
     * Gebe dem ItemStack ein Enchantment und ein Level.
     * @param enchantment - Enchantment
     * @param level - Level des Enchantments
     * @return - Gibt die Klasse für Oneliner zurück.
     */
    public ItemBuilder enchant(Enchantment enchantment, int level) {
        this.itemStack.addEnchantment(enchantment, level);
        return this;
    }

    /***
     * Entferne dem ItemStack ein Enchantment.
     * @param enchantment - Enchantment.
     * @return - Gibt die Klasse für Oneliner zurück.
     */
    public ItemBuilder disenchant(Enchantment enchantment) {
        this.itemStack.removeEnchantment(enchantment);
        return this;
    }

    /***
     * Setze die CustomModelData-ID des ItemStacks.
     * @param id - CustomModelData-ID
     * @return - Gibt die Klasse für Oneliner zurück.
     */
    public ItemBuilder customModelData(int id) {
        this.itemMeta.setCustomModelData(id);
        return this;
    }

    /***
     * Setze die maximale Anzahl eines Stacks des ItemStacks.
     * @param stackSize - Stack size
     * @return - Gibt die Klasse für Oneliner zurück.
     */
    public ItemBuilder maxStackSize(int stackSize) {
        this.itemMeta.setMaxStackSize(stackSize);
        return this;
    }

    /***
     * Setze die Lore des ItemStacks.
     * @param lines - Lore lines
     * @return - Gibt die Klasse für Oneliner zurück.
     */
    public ItemBuilder lore(Component... lines) {
        this.itemMeta.lore(List.of(lines));
        return this;
    }

    /***
     * Editiere die Lore des ItemStacks
     * @param line - Line
     * @param content - Content
     * @return - Gibt die Klasse für Oneliner zurück.
     */
    public ItemBuilder editLoreLine(int line, Component content) {
        List<Component> lore = this.itemMeta.lore();
        assert lore != null;
        lore.set(line, content);
        this.itemMeta.lore(lore);
        return this;
    }

    /***
     * Setze ItemFlags für des ItemStacks
     * @param itemFlags - ItemFlags
     * @return - Gibt die Klasse für Onliner zurück.
     */
    public ItemBuilder itemFlags(ItemFlag... itemFlags) {
        this.itemMeta.addItemFlags(itemFlags);
        return this;
    }

    /***
     * Setze den ItemStack unzerstörbar.
     * @return - Gibt die Klasse für Onliner zurück.
     */
    public ItemBuilder unbreakable() {
        this.itemMeta.setUnbreakable(true);
        return this;
    }

    /***
     * Verstecke die ToolTips.
     * @return - Gibt die Klasse für Onliner zurück.
     */
    public ItemBuilder hideToolTip() {
        this.itemMeta.setHideTooltip(true);
        return this;
    }

    public ItemBuilder glint() {
        this.itemMeta.setEnchantmentGlintOverride(true);
        return this;
    }

    /***
     * Hole dir den fertigen ItemStack.
     * @return - ItemStack
     */
    public ItemStack toItemStack() {
        this.itemStack.setItemMeta(itemMeta);
        return this.itemStack;
    }
}