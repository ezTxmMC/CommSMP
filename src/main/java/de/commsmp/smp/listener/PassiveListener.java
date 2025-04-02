package de.commsmp.smp.listener;

import de.commsmp.smp.SMP;
import de.commsmp.smp.util.Mode;
import de.commsmp.smp.util.ModeType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerPortalEvent;

public class PassiveListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        Entity target = event.getEntity();
        Entity damager = event.getDamager();

        if (!event.getCause().equals(DamageCause.ENTITY_ATTACK)
                && !event.getCause().equals(DamageCause.ENTITY_SWEEP_ATTACK)) {
            event.setCancelled(false);
            return;
        }

        if (target instanceof Player targetPlayer && damager instanceof Player attackerPlayer) {
            Mode targetMode = SMP.getInstance().getPlayerManager().getModes().get(targetPlayer.getUniqueId());
            Mode attackerMode = SMP.getInstance().getPlayerManager().getModes().get(attackerPlayer.getUniqueId());
            if (targetMode.getModeType().equals(ModeType.PASSIVE)
                    || attackerMode.getModeType().equals(ModeType.PASSIVE)) {
                event.setCancelled(true);
            }
            return;
        }

        if (target instanceof Player targetPlayer && !(damager instanceof Player)) {
            Mode targetMode = SMP.getInstance().getPlayerManager().getModes().get(targetPlayer.getUniqueId());
            if (targetMode.getModeType().equals(ModeType.PASSIVE)) {
                event.setCancelled(true);
            }
            return;
        }

        if (damager instanceof Player attackerPlayer && !(target instanceof Player)) {
            Mode attackerMode = SMP.getInstance().getPlayerManager().getModes().get(attackerPlayer.getUniqueId());
            if (attackerMode.getModeType().equals(ModeType.PASSIVE)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Mode mode = SMP.getInstance().getPlayerManager().getModes().get(player.getUniqueId());
        if (!mode.getModeType().equals(ModeType.PASSIVE)) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Mode mode = SMP.getInstance().getPlayerManager().getModes().get(player.getUniqueId());
        if (!mode.getModeType().equals(ModeType.PASSIVE)) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChangedWorld(PlayerPortalEvent event) {
        Player player = event.getPlayer();
        Mode mode = SMP.getInstance().getPlayerManager().getModes().get(player.getUniqueId());
        if (!mode.getModeType().equals(ModeType.PASSIVE)) {
            return;
        }
        event.setCancelled(true);
    }
}
