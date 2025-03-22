package de.commsmp.smp.util;

import de.commsmp.smp.SMP;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

@Setter
@Getter
public class GraveStone {
    private final Player player;
    private final InventoryHolder holder;
    private final int id;
    private final Location location;

    public GraveStone(Player player, InventoryHolder holder, int id, Location location) {
        this.player = player;
        this.holder = holder;
        this.id = id;
        this.location = location;
    }

    public void spawn() {
        Block block = this.location.getWorld().getBlockAt(this.location.getBlockX(), this.location.getBlockY(), this.location.getBlockZ());
        block.setType(Material.CHEST);
        GraveStoneHandler graveStoneHandler = SMP.getInstance().getGraveStoneHandler();
        graveStoneHandler.getGraveStoneInteractables().get(this.player.getUniqueId()).put(this.id, block.getLocation());
    }
}
