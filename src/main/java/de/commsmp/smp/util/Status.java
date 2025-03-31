package de.commsmp.smp.util;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

public class Status {
    private final Player player;
    private final ArmorStand armorStand;

    public Status(Player player) {
        this.player = player;
        this.armorStand = this.createArmorStand(StatusType.NONE);
    }

    public void setListName(StatusType nameMode) {
        String suffix = "";
        switch (nameMode) {
            case REC -> {
                suffix = " <dark_gray>▪ <#ff3333>REC";
            }
            case LIVE -> {
                suffix = " <dark_gray>▪ <#9933ff>LIVE";
            }
            case ROLEPLAY -> {
                suffix = " <dark_gray>▪ <#33ffcc>RP";
            }
            case AFK -> {
                suffix = " <dark_gray>▪ <#888888>AFK";
            }
            case PASSIVE -> {
                suffix = " <dark_gray>▪ <#cccccc>PASSIVE";
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
            case ROLEPLAY -> {
                head = "<#33ffcc>RP";
            }
            case AFK -> {
                head = "<#888888>AFK";
            }
            case PASSIVE -> {
                head = "<#cccccc>PASSIVE";
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
            case ROLEPLAY -> {
                head = "<#33ffcc>RP";
            }
            case AFK -> {
                head = "<#888888>AFK";
            }
            case PASSIVE -> {
                head = "<#cccccc>PASSIVE";
            }
            case NONE -> head = "";
        }
        armorStand.customName(AdventureColor.apply(head));
    }
}
