package de.eztxm.smp.config.types;

import de.eztxm.ezlib.config.object.JsonObject;
import de.eztxm.smp.lock.types.InteractionType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LockObject {

    private final UUID lockId;
    private final UUID owner;
    private final ArrayList<UUID> members;
    private final Material type;
    private final Location location;
    private OffsetDateTime lastInteraction;
    private final OffsetDateTime created;
    private final ArrayList<InteractionObject> interactions;

    public LockObject(UUID lockId, UUID owner, ArrayList<UUID> members, Material type, Location location) {
        this(lockId, owner, members, type, location, OffsetDateTime.now(), OffsetDateTime.now(), (ArrayList<InteractionObject>) List.of(new InteractionObject(owner, InteractionType.CREATED)));
    }

    public LockObject(UUID lockId, UUID owner, ArrayList<UUID> members, Material type, Location location, OffsetDateTime lastInteraction, OffsetDateTime created, ArrayList<InteractionObject> interactions) {
        this.lockId = lockId;
        this.owner = owner;
        this.members = members;
        this.type = type;
        this.location = location;
        this.lastInteraction = lastInteraction;
        this.created = created;
        this.interactions = interactions;
    }

    @Override
    public String toString() {
        JsonObject json = new JsonObject();
        json.set("lockId", lockId.toString());
        json.set("owner", owner.toString());

        List<String> memberList = new ArrayList<>();
        for (UUID member : members) {
            memberList.add(member.toString());
        }
        json.set("members", memberList);

        json.set("type", type.name());

        JsonObject locationJson = new JsonObject();
        locationJson.set("world", location.getWorld().getName());
        locationJson.set("x", location.getBlockX());
        locationJson.set("y", location.getBlockY());
        locationJson.set("z", location.getBlockZ());
        json.set("location", locationJson);

        json.set("lastInteraction", lastInteraction.toString());
        json.set("created", created.toString());

        List<JsonObject> interactionList = new ArrayList<>();
        for (InteractionObject interaction : interactions) {
            interactionList.add(interaction.toJson());
        }
        json.set("interactions", interactionList);

        return json.toJsonString();
    }

    public static LockObject fromString(String jsonString) {
        JsonObject json = JsonObject.parse(jsonString);

        UUID lockId = UUID.fromString(json.get("lockId").toString());
        UUID owner = UUID.fromString(json.get("owner").toString());

        List<Object> memberObjects = (List<Object>) json.get("members");
        ArrayList<UUID> members = new ArrayList<>();
        for (Object member : memberObjects) {
            members.add(UUID.fromString(member.toString()));
        }

        Material type = Material.valueOf(json.get("type").toString());

        JsonObject locationJson = (JsonObject) json.get("location");
        Location location = new Location(
                Bukkit.getWorld(locationJson.get("world").toString()),
                Integer.parseInt(locationJson.get("x").toString()),
                Integer.parseInt(locationJson.get("y").toString()),
                Integer.parseInt(locationJson.get("z").toString())
        );

        OffsetDateTime lastInteraction = OffsetDateTime.parse(json.get("lastInteraction").toString());
        OffsetDateTime created = OffsetDateTime.parse(json.get("created").toString());

        List<Object> interactionObjects = (List<Object>) json.get("interactions");
        ArrayList<InteractionObject> interactions = new ArrayList<>();
        for (Object interaction : interactionObjects) {
            interactions.add(InteractionObject.fromJson((JsonObject) interaction));
        }

        LockObject lockObject = new LockObject(lockId, owner, members, type, location);
        lockObject.lastInteraction = lastInteraction;
        lockObject.interactions.addAll(interactions);
        return lockObject;
    }
}
