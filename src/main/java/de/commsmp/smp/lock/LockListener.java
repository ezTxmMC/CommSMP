package de.commsmp.smp.lock;

import de.commsmp.smp.SMP;
import de.commsmp.smp.config.LockConfig;
import de.commsmp.smp.util.AdventureColor;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.block.data.Rail;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Iterator;
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
    public void onEntityExplode(EntityExplodeEvent event) {
        Iterator<Block> iterator = event.blockList().iterator();
        while (iterator.hasNext()) {
            Block block = iterator.next();
            if (lockConfig.isLocked(block.getLocation())) {
                iterator.remove();
            }
        }
    }

    @EventHandler
    public void onMinecartDamage(PrePlayerAttackEntityEvent event) {
        Entity entity = event.getAttacked();
        if (!(entity instanceof HopperMinecart || entity instanceof StorageMinecart)) {
            return;
        }
        Minecart minecart = (Minecart) entity;
        PersistentDataContainer container = minecart.getPersistentDataContainer();
        NamespacedKey lockKey = new NamespacedKey(SMP.getInstance(), "lock");
        if (!container.has(lockKey, PersistentDataType.STRING)) {
            return;
        }
        Player player = event.getPlayer();
        String ownerUUID = container.get(lockKey, PersistentDataType.STRING);
        if (ownerUUID != null && ownerUUID.equals(player.getUniqueId().toString())) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onVehicleDamage(VehicleDamageEvent event) {
        Vehicle vehicle = event.getVehicle();
        if (!(vehicle instanceof HopperMinecart || vehicle instanceof StorageMinecart)) {
            return;
        }
        if (event.getAttacker() == null) {
            NamespacedKey lockKey = new NamespacedKey(SMP.getInstance(), "lock");
            if (vehicle.getPersistentDataContainer().has(lockKey, PersistentDataType.STRING)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerMinecartInteract(final PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null)
            return;

        if (!(clickedBlock.getBlockData() instanceof Rail))
            return;

        if (event.getItem() == null)
            return;
        Material itemType = event.getItem().getType();
        if (!(itemType == Material.HOPPER_MINECART || itemType == Material.CHEST_MINECART))
            return;

        Location spawnLocation = clickedBlock.getLocation().clone().add(0, 1, 0);
        UUID playerUUID = event.getPlayer().getUniqueId();

        Bukkit.getScheduler().runTaskLater(SMP.getInstance(), () -> {
            double radiusX = 1.0, radiusY = 1.0, radiusZ = 1.0;
            boolean found = false;
            for (Entity entity : clickedBlock.getWorld().getNearbyEntities(spawnLocation, radiusX, radiusY, radiusZ)) {
                if (entity instanceof Minecart) {
                    Minecart minecart = (Minecart) entity;
                    if (!(minecart instanceof HopperMinecart || minecart instanceof StorageMinecart)) {
                        continue;
                    }
                    if (minecart.getTicksLived() > 40) {
                        continue;
                    }

                    PersistentDataContainer container = minecart.getPersistentDataContainer();
                    NamespacedKey lockKey = new NamespacedKey(SMP.getInstance(), "lock");
                    if (container.has(lockKey, PersistentDataType.STRING)) {
                        continue;
                    }
                    container.set(lockKey, PersistentDataType.STRING, playerUUID.toString());
                    event.getPlayer().sendMessage(AdventureColor
                            .apply(SMP.getInstance().getPrefix() + "Das Minecart wurde automatisch gesperrt!"));
                    found = true;
                    break;
                }
            }
            if (!found) {
                event.getPlayer().sendMessage(
                        AdventureColor.apply(SMP.getInstance().getPrefix() + "Kein Minecart in der Nähe gefunden."));
            }
        }, 2L);
    }

    @EventHandler
    public void onPlayerInteractWithMinecart(final PlayerInteractEntityEvent event) {
        Entity rightClicked = event.getRightClicked();
        if (!(rightClicked instanceof HopperMinecart || rightClicked instanceof StorageMinecart))
            return;

        PersistentDataContainer container = rightClicked.getPersistentDataContainer();
        NamespacedKey lockKey = new NamespacedKey(SMP.getInstance(), "lock");
        if (!container.has(lockKey, PersistentDataType.STRING)) {
            return;
        }

        String lockOwner = container.get(lockKey, PersistentDataType.STRING);
        if (lockOwner != null && lockOwner.equals(event.getPlayer().getUniqueId().toString())) {
            return;
        }

        event.setCancelled(true);
        event.getPlayer()
                .sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix() + "Dieses Minecart ist gesperrt!"));
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        Block block = event.getBlock();
        if (lockConfig.isLocked(block.getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        Block block = event.getBlock();
        if (lockConfig.isLocked(block.getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(final BlockPlaceEvent event) {
        final Block placedBlock = event.getBlock();
        final Player player = event.getPlayer();

        for (final Block adjacentBlock : getAdjacentBlocks(placedBlock)) {
            if (placedBlock.getType() != Material.CHEST)
                continue;
            if (adjacentBlock.getType() != Material.CHEST)
                continue;

            final Location adjacentLocation = adjacentBlock.getLocation();

            if (lockConfig.isLocked(adjacentLocation)) {
                final UUID ownerUUID = lockConfig.getOwner(adjacentLocation);

                if (ownerUUID == null || !player.getUniqueId().equals(ownerUUID)) {
                    player.sendMessage(
                            AdventureColor.apply(smp.getPrefix()
                                    + "Du kannst diese Kiste nicht mit einer gesperrten Kiste verbinden."));
                    event.setCancelled(true);
                    return;
                }

                lockConfig.lock(placedBlock.getLocation(), ownerUUID);
                player.sendMessage(
                        AdventureColor.apply(smp.getPrefix() + "Die Kiste wurde erfolgreich verbunden und gesperrt."));
                return;
            }
        }

        if (LockUtil.isLockable(placedBlock)) {
            lockConfig.lock(placedBlock.getLocation(), player.getUniqueId());
            player.sendMessage(AdventureColor.apply(smp.getPrefix() + "Der Block wurde automatisch gesperrt!"));
        }
    }

    @EventHandler
    public void onViewerAttemptModifyInventory(final InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof final Player player) || player.hasPermission("leben.lock.*")) {
            return;
        }

        final Block block = event.getInventory().getLocation() != null ? event.getInventory().getLocation().getBlock()
                : null;
        if (block == null) {
            return;
        }
        Location location = block.getLocation();
        if (!lockConfig.isLocked(location)) {
            return;
        }
        UUID ownerUUID = lockConfig.getOwner(location);
        List<UUID> trusted = lockConfig.getTrusted(location);

        if ((ownerUUID != null && ownerUUID.equals(player.getUniqueId())) || trusted.contains(player.getUniqueId())) {
            return;
        }

        if (lockConfig.isDonatable(location)) {
            if ((event.getClick().isShiftClick() && event.getClickedInventory() == event.getView().getTopInventory()
                    && event.getCurrentItem() != null)
                    || (event.getClickedInventory() == event.getView().getTopInventory()
                            && event.getCurrentItem() != null)) {
                player.sendMessage(
                        AdventureColor.apply(smp.getPrefix() + "Du kannst nur Items hinzufügen, nicht entfernen!"));
                event.setCancelled(true);
                return;
            }
            if (event.getCursor() != null && event.getCursor().getType() != Material.AIR
                    && event.getClickedInventory() == event.getView().getTopInventory()) {
                return;
            }
            if (event.getClick().isShiftClick()
                    && event.getClickedInventory() == event.getView().getBottomInventory()) {
                return;
            }
            event.setCancelled(true);
            player.sendMessage(AdventureColor.apply(smp.getPrefix() + "Nur das Hinzufügen von Items ist erlaubt!"));
        }

        if (lockConfig.isViewable(location)) {
            if (ownerUUID != null && ownerUUID.equals(player.getUniqueId()) || trusted.contains(player.getUniqueId())) {
                return;
            }
            player.sendMessage(AdventureColor.apply(smp.getPrefix() + "Du kannst den Inhalt nicht verändern!"));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHopperExtractFromLockedChest(final InventoryMoveItemEvent event) {
        if (event.getSource().getLocation() == null)
            return;
        Block chestBlock = event.getSource().getLocation().getBlock();

        if (!lockConfig.isLocked(chestBlock.getLocation()))
            return;

        UUID chestOwner = lockConfig.getOwner(chestBlock.getLocation());
        if (chestOwner == null) {
            event.setCancelled(true);
            return;
        }

        if (event.getDestination().getHolder() instanceof HopperMinecart) {
            HopperMinecart hopper = (HopperMinecart) event.getDestination().getHolder();
            PersistentDataContainer container = hopper.getPersistentDataContainer();
            NamespacedKey lockKey = new NamespacedKey(SMP.getInstance(), "lock");
            if (container.has(lockKey, PersistentDataType.STRING)) {
                String hopperOwnerStr = container.get(lockKey, PersistentDataType.STRING);
                if (hopperOwnerStr != null) {
                    UUID hopperOwner = UUID.fromString(hopperOwnerStr);
                    if (chestOwner.equals(hopperOwner)) {
                        return;
                    }
                }
            }
        }
        if (event.getDestination().getHolder() instanceof Hopper) {
            Block hopperBlock = event.getDestination().getLocation().getBlock();
            if (!lockConfig.isLocked(hopperBlock.getLocation())) {
                event.setCancelled(true);
                return;
            }

            UUID hopperOwner = lockConfig.getOwner(hopperBlock.getLocation());
            if (hopperOwner != null && hopperOwner.equals(chestOwner)) {
                return;
            }
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
            player.sendMessage(
                    AdventureColor.apply(smp.getPrefix() + "Dieser Block ist &cgesperrt&7 und gehört jemand anderem."));
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
                    AdventureColor.apply(
                            smp.getPrefix() + "Du kannst diesen Block nicht &czerstören&7, da er &cgesperrt &7ist."));
            event.setCancelled(true);
            return;
        }
        lockConfig.unlock(location);
        player.sendMessage(AdventureColor.apply(smp.getPrefix() + "Die Sperre wurde &aerfolgreich &7entfernt."));
    }

    private Block[] getAdjacentBlocks(final Block block) {
        return new Block[] {
                block.getRelative(1, 0, 0),
                block.getRelative(-1, 0, 0),
                block.getRelative(0, 0, 1),
                block.getRelative(0, 0, -1)
        };
    }

}
