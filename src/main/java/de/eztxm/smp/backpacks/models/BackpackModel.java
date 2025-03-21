package de.eztxm.smp.backpacks.models;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class BackpackModel {

    private final UUID uniqueId;
    private UUID creator;
    private OffsetDateTime creationDate;
    private ItemStack[] items;
    private List<LastUpdatedModel> lastUpdated;

    public BackpackModel(UUID uniqueId, UUID creator, OffsetDateTime creationDate, ItemStack[] items, List<LastUpdatedModel> lastUpdated) {
        this.uniqueId = uniqueId;
        this.creator = creator;
        this.creationDate = creationDate;
        this.items = items;
        this.lastUpdated = lastUpdated;
    }

    public BackpackModel(UUID uniqueId, UUID creator, OffsetDateTime creationDate, ItemStack[] items) {
        this(uniqueId, creator, creationDate, items, new ArrayList<>());
    }

    public BackpackModel(UUID uniqueId, Player creator, OffsetDateTime creationDate, ItemStack[] items, List<LastUpdatedModel> lastUpdated) {
        this(uniqueId, creator != null ? creator.getUniqueId() : null, creationDate, items, lastUpdated);
    }

    public BackpackModel(UUID uniqueId, Player creator, OffsetDateTime creationDate, ItemStack[] items) {
        this(uniqueId, creator, creationDate, items, new ArrayList<>());
    }

    public BackpackModel(UUID uniqueId, UUID creator, ItemStack[] items, List<LastUpdatedModel> lastUpdated) {
        this(uniqueId, creator, OffsetDateTime.now(), items, lastUpdated);
    }

    public BackpackModel(UUID uniqueId, UUID creator, ItemStack[] items) {
        this(uniqueId, creator, OffsetDateTime.now(), items, new ArrayList<>());
    }

    public BackpackModel(UUID uniqueId, Player creator, ItemStack[] items, List<LastUpdatedModel> lastUpdated) {
        this(uniqueId, creator != null ? creator.getUniqueId() : null, OffsetDateTime.now(), items, lastUpdated);
    }

    public BackpackModel(UUID uniqueId, Player creator, ItemStack[] items) {
        this(uniqueId, creator, OffsetDateTime.now(), items, new ArrayList<>());
    }

    public BackpackModel(Player creator, ItemStack[] items, List<LastUpdatedModel> lastUpdated) {
        this(UUID.randomUUID(), creator, OffsetDateTime.now(), items, lastUpdated);
    }

    public BackpackModel(Player creator, ItemStack[] items) {
        this(UUID.randomUUID(), creator, OffsetDateTime.now(), items, new ArrayList<>());
    }

    public BackpackModel(Player creator, OffsetDateTime creationDate, List<LastUpdatedModel> lastUpdated) {
        this(UUID.randomUUID(), creator, creationDate, new ItemStack[]{}, lastUpdated);
    }

    public BackpackModel(Player creator, OffsetDateTime creationDate) {
        this(UUID.randomUUID(), creator, creationDate, new ItemStack[]{}, new ArrayList<>());
    }

    public BackpackModel(Player creator, List<LastUpdatedModel> lastUpdated) {
        this(UUID.randomUUID(), creator, OffsetDateTime.now(), new ItemStack[]{}, lastUpdated);
    }

    public BackpackModel(Player creator) {
        this(UUID.randomUUID(), creator, OffsetDateTime.now(), new ItemStack[]{}, new ArrayList<>());
    }

    public BackpackModel(UUID uniqueId, Player creator, List<LastUpdatedModel> lastUpdated) {
        this(uniqueId, creator, OffsetDateTime.now(), new ItemStack[]{}, lastUpdated);
    }

    public BackpackModel(UUID uniqueId, Player creator) {
        this(uniqueId, creator, OffsetDateTime.now(), new ItemStack[]{}, new ArrayList<>());
    }

    public BackpackModel(UUID uniqueId, UUID creator, List<LastUpdatedModel> lastUpdated) {
        this(uniqueId, creator, OffsetDateTime.now(), new ItemStack[]{}, lastUpdated);
    }
}
