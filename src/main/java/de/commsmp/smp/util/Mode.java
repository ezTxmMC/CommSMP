package de.commsmp.smp.util;

import lombok.Getter;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

public class Mode {
    private final Player player;
    private final ArmorStand armorStand;
    @Getter
    private final ModeType modeType;

    public Mode(Player player) {
        this.player = player;
        this.armorStand = this.createArmorStand(ModeType.NONE);
        this.modeType = ModeType.NONE;
    }

    public void setListName(ModeType modeType) {
        String suffix = "";
        switch (modeType) {
            case ROLEPLAY -> {
                suffix = " <dark_gray>▪ <#33ffcc>RP";
            }
            case PASSIVE -> {
                suffix = " <dark_gray>▪ <#cccccc>PASSIVE";
            }
            case NONE -> suffix = "";
        }
        player.playerListName(AdventureColor.apply("<gray>" + player.getName() + suffix));
    }

    private ArmorStand createArmorStand(ModeType modeType) {
        String head = "";
        switch (modeType) {
            case ROLEPLAY -> {
                head = "<#33ffcc>RP";
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
        armorStand.setSmall(true);
        armorStand.customName(AdventureColor.apply(head));
        player.addPassenger(armorStand);
        return armorStand;
    }

    public void updateHead(ModeType modeType) {
        String head = "";
        switch (modeType) {
            case ROLEPLAY -> {
                head = "<#33ffcc>RP";
            }
            case PASSIVE -> {
                head = "<#cccccc>PASSIVE";
            }
            case NONE -> head = "";
        }
        armorStand.customName(AdventureColor.apply(head));
    }
}
