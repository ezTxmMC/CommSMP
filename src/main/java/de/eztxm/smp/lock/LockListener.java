package de.eztxm.smp.lock;

import de.eztxm.smp.SMP;
import de.eztxm.smp.config.LockConfig;
import de.eztxm.smp.util.BukkitColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;
import java.util.UUID;

public class LockListener implements Listener {

    private final SMP smp;
    private final LockConfig lockConfig;

    public LockListener(final SMP smp) {
        this.smp = smp;
        this.lockConfig = smp.getLockConfig();
    }

    @EventHandler
    public void onPlace(final BlockPlaceEvent event) {
        final Block placedBlock = event.getBlock();
        final Player player = event.getPlayer();

        for (final Block adjacentBlock : getAdjacentBlocks(placedBlock)) {
            if (adjacentBlock.getType() != Material.CHEST) {
                continue;
            }

            final Location adjacentLocation = adjacentBlock.getLocation();

            if (lockConfig.isLocked(adjacentLocation)) {
                final UUID ownerUUID = lockConfig.getOwner(adjacentLocation);

                if (ownerUUID == null || !player.getUniqueId().equals(ownerUUID)) {
                    player.sendMessage(
                            BukkitColor.apply(smp.getPrefix() + "Du kannst diese Kiste nicht mit einer gesperrten Kiste verbinden."));
                    event.setCancelled(true);
                    return;
                }

                lockConfig.lock(placedBlock.getLocation(), ownerUUID);
                player.sendMessage(BukkitColor.apply(smp.getPrefix() + "Die Kiste wurde erfolgreich verbunden und gesperrt."));
                return;
            }
        }

        if(LockUtil.isLockable(placedBlock)) {
            lockConfig.lock(placedBlock.getLocation(), player.getUniqueId());
            player.sendMessage(smp.getPrefix() + "Der Block wurde automatisch gesperrt!");
        }
    }

    @EventHandler
    public void onViewerAttemptModifyInventory(final InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof final Player player) || player.hasPermission("leben.lock.*")) {
            return;
        }

        final Block block = event.getInventory().getLocation() != null ? event.getInventory().getLocation().getBlock() : null;
        if(block == null) {
            return;
        }
        Location location = block.getLocation();
        if(!lockConfig.isLocked(location)) {
            return;
        }
        UUID ownerUUID = lockConfig.getOwner(location);
        List<UUID> trusted = lockConfig.getTrusted(location);

        if((ownerUUID != null && ownerUUID.equals(player.getUniqueId())) || trusted.contains(player.getUniqueId())) {
            return;
        }

        if(lockConfig.isDonatable(location)) {
            if((event.getClick().isShiftClick() && event.getClickedInventory() == event.getView().getTopInventory() && event.getCurrentItem() != null)
                    || (event.getClickedInventory() == event.getView().getTopInventory() && event.getCurrentItem() != null)) {
                player.sendMessage(BukkitColor.apply(smp.getPrefix() + "Du kannst nur Items hinzufügen, nicht entfernen!"));
                event.setCancelled(true);
                return;
            }
            if(event.getCursor() != null && event.getCursor().getType() != Material.AIR && event.getClickedInventory() == event.getView().getTopInventory()) {
                return;
            }
            if(event.getClick().isShiftClick() && event.getClickedInventory() == event.getView().getBottomInventory()) {
                return;
            }
            event.setCancelled(true);
            player.sendMessage(BukkitColor.apply(smp.getPrefix() + "Nur das Hinzufügen von Items ist erlaubt!"));
        }

        if(lockConfig.isViewable(location)) {
            if(ownerUUID != null && ownerUUID.equals(player.getUniqueId()) || trusted.contains(player.getUniqueId())) {
                return;
            }
            player.sendMessage(BukkitColor.apply(smp.getPrefix() + "Du kannst den Inhalt nicht verändern!"));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHopperExtractFromLockedChest(final InventoryMoveItemEvent event) {
        final Block block = event.getSource().getLocation() != null ? event.getSource().getLocation().getBlock() : null;
        if (block == null || !lockConfig.isLocked(block.getLocation())) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onHopperPickupFromLockedChest(final InventoryPickupItemEvent event) {
        final Block block = event.getInventory().getLocation() != null ? event.getInventory().getLocation().getBlock() : null;
        if (block == null || !lockConfig.isLocked(block.getLocation())) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerUseLockedBlock(final PlayerInteractEvent event) {
        final Block block = event.getClickedBlock();
        final Player player = event.getPlayer();

        if (block == null) {
            return;
        }

        final Location location = block.getLocation();
        if (lockConfig.isViewable(location) || !lockConfig.isLocked(block.getLocation())) {
            return;
        }

        final UUID ownerUUID = lockConfig.getOwner(location);
        if (!player.getUniqueId().equals(ownerUUID) && !lockConfig.getTrusted(location).contains(player.getUniqueId())
                && !player.hasPermission("lock.bypass")) {
            player.sendMessage(BukkitColor.apply(smp.getPrefix() + "Dieser Block ist &cgesperrt&7 und gehört jemand anderem."));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBreakLockedBlock(final BlockBreakEvent event) {
        final Block block = event.getBlock();
        final Player player = event.getPlayer();

        if (!lockConfig.isLocked(block.getLocation())) {
            return;
        }

        final Location location = block.getLocation();
        final UUID ownerUUID = lockConfig.getOwner(location);
        if (!player.getUniqueId().equals(ownerUUID) && !player.hasPermission("lock.bypass")) {
            player.sendMessage(
                    BukkitColor.apply(smp.getPrefix() + "Du kannst diesen Block nicht &czerstören&7, da er &cgesperrt &7ist."));
            event.setCancelled(true);
            return;
        }
        lockConfig.unlock(location);
        player.sendMessage(BukkitColor.apply(smp.getPrefix() + "Die Sperre wurde &aerfolgreich &7entfernt."));
    }

    private Block[] getAdjacentBlocks(final Block block) {
        return new Block[]{
                block.getRelative(1, 0, 0),
                block.getRelative(-1, 0, 0),
                block.getRelative(0, 0, 1),
                block.getRelative(0, 0, -1)
        };
    }

}
