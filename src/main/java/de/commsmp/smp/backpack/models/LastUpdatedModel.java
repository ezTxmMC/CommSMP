package de.commsmp.smp.backpack.models;

import de.commsmp.smp.backpack.UpdateType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.time.OffsetDateTime;
import java.util.UUID;

@Setter
@Getter
public class LastUpdatedModel {

    private UUID user;
    private OffsetDateTime timestamp;
    private UpdateType type;
    private Material item;
    private int amount;
    private int slot;
    private Inventory movedTo;

    public LastUpdatedModel(UUID user, OffsetDateTime timestamp, UpdateType type, Material item, int amount, int slot, Inventory movedTo) {
        this.user = user;
        this.timestamp = timestamp;
        this.type = type;
        this.item = item;
        this.amount = amount;
        this.slot = slot;
        this.movedTo = movedTo;
    }

    public LastUpdatedModel(UUID user, UpdateType type, Material item, int amount, int slot, Inventory movedTo) {
        this(user, OffsetDateTime.now(), type, item, amount, slot, movedTo);
    }


}
