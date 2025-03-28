package de.commsmp.smp.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class SitListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }

        if (!sittable(block.getType())) {
            return;
        }

        Player player = event.getPlayer();
        if (player.getVehicle() != null) {
            return;
        }

        double[] offsets = getOffsets(block);
        double offsetX = offsets[0];
        double offsetY = offsets[1];
        double offsetZ = offsets[2];

        Location seatLocation = block.getLocation().add(0.5 + offsetX, offsetY, 0.5 + offsetZ);

        ArmorStand seat = block.getWorld().spawn(seatLocation, ArmorStand.class, armorStand -> {
            armorStand.setVisible(false);
            armorStand.setGravity(false);
            armorStand.setMarker(true);
            armorStand.setInvulnerable(true);
        });

        seat.addPassenger(player);

        event.setCancelled(true);
    }

    @EventHandler
    public void onVehicleExit(VehicleExitEvent event) {
        if (event.getExited() instanceof Player && event.getVehicle() instanceof ArmorStand) {
            ArmorStand seat = (ArmorStand) event.getVehicle();
            seat.remove();
        }
    }

    private boolean sittable(Material material) {
        String name = material.name().toLowerCase();
        return name.contains("slab") || name.contains("stair") || material == Material.HAY_BLOCK;
    }

    private double[] getOffsets(Block block) {
        double offsetX = 0.0;
        double offsetY = 1.0;
        double offsetZ = 0.0;

        BlockData data = block.getBlockData();

        if (data instanceof Slab slab) {
            if (slab.getType() == Slab.Type.BOTTOM) {
                offsetY = 0.5;
            }
            if (slab.getType() == Slab.Type.TOP) {
                offsetY = 1.0;
            }
        }

        if (data instanceof Stairs stairs) {
            if (stairs.getHalf() == Bisected.Half.BOTTOM) {
                offsetY = 0.6;
            }
            if (stairs.getHalf() == Bisected.Half.TOP) {
                offsetY = 1.0;
            }

            if (stairs.getFacing() == BlockFace.NORTH) {
                offsetZ = 0.2;
            }
            if (stairs.getFacing() == BlockFace.SOUTH) {
                offsetZ = -0.2;
            }
            if (stairs.getFacing() == BlockFace.EAST) {
                offsetX = -0.2;
            }
            if (stairs.getFacing() == BlockFace.WEST) {
                offsetX = 0.2;
            }
        }

        if (block.getType() == Material.HAY_BLOCK) {
            offsetY = 1.0;
        }

        return new double[] {offsetX, offsetY, offsetZ};
    }

}
