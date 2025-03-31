package de.commsmp.smp.util;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import lombok.Getter;

public class Status {
    private final Player player;
    private final ArmorStand armorStand;
    @Getter
    private final StatusType statusType;

    public Status(Player player) {
        this.player = player;
        this.armorStand = this.createArmorStand(StatusType.NONE);
        this.statusType = StatusType.NONE;
    }

    public void setListName(StatusType statusType) {
        String suffix = "";
        switch (statusType) {
            case REC -> {
                suffix = " <dark_gray>▪ <#ff3333>REC";
            }
            case LIVE -> {
                suffix = " <dark_gray>▪ <#9933ff>LIVE";
            }
            case AFK -> {
                suffix = " <dark_gray>▪ <#888888>AFK";
            }
            case NONE -> suffix = "";
        }
        player.playerListName(AdventureColor.apply("<gray>" + player.getName() + suffix));
    }

    private ArmorStand createArmorStand(StatusType statusType) {
        String head = "";
        switch (statusType) {
            case REC -> {
                head = "<#ff3333>REC";
            }
            case LIVE -> {
                head = "<#9933ff>LIVE";
            }
            case AFK -> {
                head = "<#888888>AFK";
            }
            case NONE -> head = "";
        }
        ArmorStand armorStand = player.getWorld().spawn(player.getLocation(), ArmorStand.class);
        armorStand.setInvisible(true);
        armorStand.setInvulnerable(true);
        armorStand.setPersistent(true);
        armorStand.setCollidable(false);
        armorStand.customName(AdventureColor.apply(head));
        player.addPassenger(armorStand);
        return armorStand;
    }

    public void updateHead(StatusType statusType) {
        String head = "";
        switch (statusType) {
            case REC -> {
                head = "<#ff3333>REC";
            }
            case LIVE -> {
                head = "<#9933ff>LIVE";
            }
            case AFK -> {
                head = "<#888888>AFK";
            }
            case NONE -> head = "";
        }
        armorStand.customName(AdventureColor.apply(head));
    }
}
