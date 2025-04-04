package de.commsmp.smp.config;

import de.commsmp.smp.SMP;
import de.commsmp.smp.config.data.LockInfo;
import de.eztxm.ezlib.config.JsonConfig;
import de.eztxm.ezlib.config.object.JsonObject;
import de.eztxm.ezlib.config.object.ObjectConverter;
import org.bukkit.Location;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class LockConfig extends JsonConfig {
    private final Map<String, LockInfo> locks;

    public LockConfig() {
        super(SMP.getInstance().getDataFolder().getPath(), "locks.json", false);
        this.locks = new HashMap<>();
        loadLocks();
    }

    private String getLocationKey(final Location location) {
        return location.getWorld().getName() + ";" +
                location.getBlockX() + ";" +
                location.getBlockY() + ";" +
                location.getBlockZ();
    }

    public boolean isLocked(final Location location) {
        return locks.containsKey(getLocationKey(location));
    }

    public UUID getOwner(final Location location) {
        LockInfo info = locks.get(getLocationKey(location));
        return (info != null && info.getOwner() != null) ? UUID.fromString(info.getOwner()) : null;
    }

    public void lock(final Location location, final UUID owner) {
        String key = getLocationKey(location);
        LockInfo info = new LockInfo(owner.toString(), new ArrayList<>(), false, false);
        locks.put(key, info);
        save();
    }

    public void unlock(final Location location) {
        locks.remove(getLocationKey(location));
        save();
    }

    public boolean donate(final Location location) {
        String key = getLocationKey(location);
        LockInfo oldInfo = locks.get(key);
        if (oldInfo == null)
            return false;
        boolean newDonate = !oldInfo.isDonate();
        LockInfo newInfo = new LockInfo(oldInfo.getOwner(), oldInfo.getTrusted(), oldInfo.isViewable(), newDonate);
        locks.put(key, newInfo);
        save();
        return newDonate;
    }

    public List<UUID> getTrusted(final Location location) {
        LockInfo info = locks.get(getLocationKey(location));
        List<UUID> result = new ArrayList<>();
        if (info != null && info.getTrusted() != null) {
            for (String uuidStr : info.getTrusted()) {
                try {
                    result.add(UUID.fromString(uuidStr));
                } catch (IllegalArgumentException ignored) {
                }
            }
        }
        return result;
    }

    public void addTrusted(final Location location, final UUID playerUUID) {
        String key = getLocationKey(location);
        LockInfo oldInfo = locks.get(key);
        if (oldInfo == null)
            return;
        List<String> newTrusted = new ArrayList<>(oldInfo.getTrusted());
        newTrusted.add(playerUUID.toString());
        LockInfo newInfo = new LockInfo(oldInfo.getOwner(), newTrusted, oldInfo.isViewable(), oldInfo.isDonate());
        locks.put(key, newInfo);
        save();
    }

    public void removeTrusted(final Location location, final UUID playerUUID) {
        String key = getLocationKey(location);
        LockInfo oldInfo = locks.get(key);
        if (oldInfo == null)
            return;
        List<String> newTrusted = new ArrayList<>(oldInfo.getTrusted());
        newTrusted.remove(playerUUID.toString());
        LockInfo newInfo = new LockInfo(oldInfo.getOwner(), newTrusted, oldInfo.isViewable(), oldInfo.isDonate());
        locks.put(key, newInfo);
        save();
    }

    public void setTrusted(final Location location, final List<UUID> trustedPlayers) {
        String key = getLocationKey(location);
        LockInfo oldInfo = locks.get(key);
        if (oldInfo == null)
            return;
        List<String> newTrusted = new ArrayList<>();
        for (UUID uuid : trustedPlayers) {
            newTrusted.add(uuid.toString());
        }
        LockInfo newInfo = new LockInfo(oldInfo.getOwner(), newTrusted, oldInfo.isViewable(), oldInfo.isDonate());
        locks.put(key, newInfo);
        save();
    }

    public boolean isDonatable(final Location location) {
        LockInfo info = locks.get(getLocationKey(location));
        return info != null && info.isDonate();
    }

    public boolean isViewable(final Location location) {
        LockInfo info = locks.get(getLocationKey(location));
        return info != null && (info.isViewable() || info.isDonate());
    }

    public void toggleViewable(final Location location) {
        String key = getLocationKey(location);
        LockInfo oldInfo = locks.get(key);
        if (oldInfo == null)
            return;
        LockInfo newInfo = new LockInfo(oldInfo.getOwner(), oldInfo.getTrusted(), !oldInfo.isViewable(),
                oldInfo.isDonate());
        locks.put(key, newInfo);
        save();
    }

    public void loadLocks() {
        locks.clear();
        JsonObject json = this.getCustomJsonObject();
        if (json == null || json.getElements().isEmpty())
            return;

        for (String key : json.getElements().keySet()) {
            if (key == null)
                continue;
            try {
                LockInfo info = LockInfo.fromString(new ObjectConverter(json.get(key)).asString());
                locks.put(key, info);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void save() {
        JsonObject json = getCustomJsonObject();
        json.getElements().clear();

        for (Map.Entry<String, LockInfo> entry : locks.entrySet()) {
            json.set(entry.getKey(), entry.getValue().toString());
        }

        Path filePath = Paths.get(this.getConfigFolder().toString(), this.getConfigName());
        try {
            Files.writeString(filePath, json.toJsonString(true));
        } catch (IOException ignored) {
        }
    }
}