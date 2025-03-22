package de.eztxm.smp.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GraveStoneInventory implements InventoryHolder {
    private final Inventory inventory;

    public GraveStoneInventory(Player player) {
        this.inventory = Bukkit.createInventory(this, this.calculateChestSize(player.getInventory().getContents().length), "Grave Stone");
        for (@Nullable ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack == null || itemStack.getType() == Material.AIR) {
                continue;
            }
            this.inventory.addItem(itemStack);
        }
    }

    private int calculateChestSize(int itemCount) {
        if (itemCount < 10 && itemCount > 0) {
            return 9;
        }
        if (itemCount < 19 && itemCount > 0) {
            return 18;
        }
        if (itemCount < 28 && itemCount > 0) {
            return 27;
        }
        if (itemCount < 37 && itemCount > 27) {
            return 36;
        }
        if (itemCount < 46 && itemCount > 36) {
            return 45;
        }
        return 54;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }
}
