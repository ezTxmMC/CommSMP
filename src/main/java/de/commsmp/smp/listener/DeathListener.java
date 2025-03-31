package de.commsmp.smp.listener;

import de.commsmp.smp.SMP;
import de.commsmp.smp.inventory.GraveStoneInventory;
import de.commsmp.smp.util.*;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Random;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = event.getEntity().getKiller();
        EntityDamageEvent cause = player.getLastDamageCause();
        event.deathMessage(AdventureColor
                .apply(processNotification(player.getName(), (killer == null ? null : killer.getName()), cause)));
        PlayerManager playerManager = SMP.getInstance().getPlayerManager();
        GraveStone graveStone = new GraveStone(
                player,
                new GraveStoneInventory(player),
                playerManager.getGraveStones().get(player.getUniqueId()).size() + 1,
                player.getLocation());
        playerManager.getGraveStones().get(player.getUniqueId()).put(
                playerManager.getGraveStones().get(player.getUniqueId()).size() + 1,
                graveStone);
        graveStone.spawn();
        player.getInventory().clear();
        if (player.getKiller() != null) {
            player.getLocation().getWorld().dropItemNaturally(player.getLocation(),
                    new ItemBuilder(Material.PLAYER_HEAD).setSkullOwner(player).build());
        }
    }

    private String processNotification(String killedName, String killerName, EntityDamageEvent cause) {
        Random random = new Random();
        if (killerName != null) {
            int length = DeathCause.PLAYER.messages().length;
            int index = (length <= 1) ? 0 : random.nextInt(length);
            return DeathCause.PLAYER.message(index)
                    .replace("$killed", killedName)
                    .replace("$killer", killerName);
        }
        if (cause != null) {
            EntityDamageEvent.DamageCause damage = cause.getCause();
            DeathCause death = DeathCause.fromBukkitCause(damage);
            if (damage == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION
                    || damage == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK
                    || damage == EntityDamageEvent.DamageCause.ENTITY_ATTACK
                    || damage == EntityDamageEvent.DamageCause.PROJECTILE) {
                EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) cause;
                EntityType entityType = event.getDamager().getType();
                if (damage == EntityDamageEvent.DamageCause.PROJECTILE) {
                    death = DeathCause.fromProjectileType(entityType);
                }
                int length = death.messages().length;
                int index = (length <= 1) ? 0 : random.nextInt(length);
                return death.message(index)
                        .replace("$killed", killedName)
                        .replace("$entity", StringManipulator.manipulateString(entityType.name().toLowerCase()));
            }
            int length = death.messages().length;
            int index = (length <= 1) ? 0 : random.nextInt(length);
            return death.message(index)
                    .replace("$killed", killedName);
        }
        return DeathCause.NONE.message(random.nextInt(DeathCause.NONE.messages().length))
                .replace("$killed", killedName);
    }
}
