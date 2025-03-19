package de.eztxm.smp.config.types;

import de.eztxm.ezlib.config.object.JsonObject;
import de.eztxm.smp.lock.types.InteractionType;

import java.util.UUID;

public class InteractionObject {

    private final UUID playerUniqueId;
    private final InteractionType interactionType;
    private final int range;
    private final boolean added;

    public InteractionObject(UUID playerUniqueId, InteractionType interactionType) {
        this(playerUniqueId, interactionType, 0, false);
    }

    public InteractionObject(UUID playerUniqueId, InteractionType interactionType, int range, boolean added) {
        this.playerUniqueId = playerUniqueId;
        this.interactionType = interactionType;
        this.range = range;
        this.added = added;
    }

    public UUID getPlayerUniqueId() {
        return playerUniqueId;
    }

    public InteractionType getInteractionType() {
        return interactionType;
    }

    public int getRange() {
        return range;
    }

    public boolean hasBeenAdded() {
        return added;
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.set("playerUniqueId", playerUniqueId.toString());
        json.set("interactionType", interactionType.name());
        json.set("range", range);
        json.set("added", added);
        return json;
    }

    public static InteractionObject fromJson(JsonObject json) {
        UUID playerUniqueId = UUID.fromString(json.get("playerUniqueId").toString());
        InteractionType interactionType = InteractionType.valueOf(json.get("interactionType").toString());
        int range = Integer.parseInt(json.get("range").toString());
        boolean added = Boolean.parseBoolean(json.get("added").toString());

        return new InteractionObject(playerUniqueId, interactionType, range, added);
    }
}
