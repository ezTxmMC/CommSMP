package de.commsmp.smp.backpack;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.commsmp.smp.backpack.models.BackpackModel;
import de.commsmp.smp.backpack.models.LastUpdatedModel;
import de.commsmp.smp.util.InventoryParser;
import de.commsmp.smp.util.NamespacedKeys;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Type;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BackpackStorage {

    private static final ConcurrentHashMap<UUID, BackpackModel> CACHE = new ConcurrentHashMap<>();
    private static final Gson gson = new Gson();
    private final PersistentDataContainer container;
    private final UUID backpackId;

    public BackpackStorage(ItemStack itemStack) {
        if (itemStack == null) throw new IllegalArgumentException("ItemStack darf nicht null sein.");
        this.container = itemStack.getItemMeta().getPersistentDataContainer();
        String idRaw = container.getOrDefault(NamespacedKeys.BACKPACK_ID, PersistentDataType.STRING, UUID.randomUUID().toString());
        this.backpackId = UUID.fromString(idRaw);
        container.set(NamespacedKeys.BACKPACK_ID, PersistentDataType.STRING, backpackId.toString());
        Bukkit.getLogger().info("BackpackStorage init: ID = " + backpackId);
    }

    public BackpackModel load() {
        if (CACHE.containsKey(backpackId)) {
            Bukkit.getLogger().info("Backpack " + backpackId + " loaded from cache");
            return CACHE.get(backpackId);
        }

        ItemStack[] items = new ItemStack[0];
        UUID creator = null;
        OffsetDateTime creation = OffsetDateTime.now();
        List<LastUpdatedModel> lastUpdated = new ArrayList<>();

        try {
            String base64 = container.get(NamespacedKeys.BACKPACK_DATA, PersistentDataType.STRING);
            if (base64 != null) items = InventoryParser.itemStackArrayFromBase64(base64);

            String creatorStr = container.get(NamespacedKeys.CREATOR, PersistentDataType.STRING);
            if (creatorStr != null) creator = UUID.fromString(creatorStr);

            String creationStr = container.get(NamespacedKeys.CREATION_DATE, PersistentDataType.STRING);
            if (creationStr != null) creation = OffsetDateTime.parse(creationStr);

            String updatedJson = container.get(NamespacedKeys.LAST_UPDATED, PersistentDataType.STRING);
            if (updatedJson != null) {
                Type listType = new TypeToken<List<LastUpdatedModel>>() {
                }.getType();
                lastUpdated = gson.fromJson(updatedJson, listType);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        BackpackModel model = new BackpackModel(backpackId, creator, creation, items, lastUpdated);
        CACHE.put(backpackId, model);
        return model;
    }

    public void save(BackpackModel model) {
        if (model == null) return;

        try {
            String base64 = InventoryParser.itemStackArrayToBase64(model.getItems());
            container.set(NamespacedKeys.BACKPACK_DATA, PersistentDataType.STRING, base64);
            container.set(NamespacedKeys.CREATOR, PersistentDataType.STRING, model.getCreator().toString());
            container.set(NamespacedKeys.CREATION_DATE, PersistentDataType.STRING, model.getCreationDate().toString());
            container.set(NamespacedKeys.LAST_UPDATED, PersistentDataType.STRING, gson.toJson(model.getLastUpdated()));
            if (CACHE.get(backpackId) != null) {
                CACHE.remove(backpackId);
            }
            CACHE.put(backpackId, model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearCache(UUID backpackId) {
        CACHE.remove(backpackId);
    }

    public static void clearAll() {
        CACHE.clear();
    }
}
