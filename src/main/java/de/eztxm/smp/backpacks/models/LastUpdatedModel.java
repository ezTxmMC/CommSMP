package de.eztxm.smp.backpacks.models;

import de.eztxm.smp.backpacks.UpdateType;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.time.OffsetDateTime;
import java.util.UUID;

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


    public UUID getUser() {
        return user;
    }

    public void setUser(UUID user) {
        this.user = user;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public UpdateType getType() {
        return type;
    }

    public void setType(UpdateType type) {
        this.type = type;
    }

    public Material getItem() {
        return item;
    }

    public void setItem(Material item) {
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public Inventory getMovedTo() {
        return movedTo;
    }

    public void setMovedTo(Inventory movedTo) {
        this.movedTo = movedTo;
    }
}
