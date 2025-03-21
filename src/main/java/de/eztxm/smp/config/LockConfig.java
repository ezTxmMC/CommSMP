package de.eztxm.smp.config;

import de.eztxm.ezlib.config.annotation.JsonClassConfig;
import de.eztxm.ezlib.config.annotation.JsonClassElement;
import de.eztxm.ezlib.config.reflect.JsonProcessor;
import org.bukkit.Location;

import java.util.*;

@JsonClassConfig(path = "plugins/LockSystem", fileName = "locks.json")
public class LockConfig {

    public record LockInfo(
            @JsonClassElement String owner,
            @JsonClassElement List<String> trusted,
            @JsonClassElement boolean viewable,
            @JsonClassElement boolean donate
    ) {}

    private static JsonProcessor<LockConfig> jsonProcessor;

    @JsonClassElement
    private Map<String, LockInfo> locks = new HashMap<>();

    public LockConfig() {
    }

    private String getLocationKey(final Location location) {
        return location.getWorld().getName() + "." +
                location.getBlockX() + "_" +
                location.getBlockY() + "_" +
                location.getBlockZ();
    }

    public boolean isLocked(final Location location) {
        return locks.containsKey(getLocationKey(location));
    }

    public UUID getOwner(final Location location) {
        LockInfo info = locks.get(getLocationKey(location));
        return (info != null && info.owner() != null) ? UUID.fromString(info.owner()) : null;
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
        if (oldInfo == null) return false;
        boolean newDonate = !oldInfo.donate();
        LockInfo newInfo = new LockInfo(oldInfo.owner(), oldInfo.trusted(), oldInfo.viewable(), newDonate);
        locks.put(key, newInfo);
        save();
        return newDonate;
    }

    public List<UUID> getTrusted(final Location location) {
        LockInfo info = locks.get(getLocationKey(location));
        List<UUID> result = new ArrayList<>();
        if (info != null && info.trusted() != null) {
            for (String uuidStr : info.trusted()) {
                try {
                    result.add(UUID.fromString(uuidStr));
                } catch (IllegalArgumentException e) {
                }
            }
        }
        return result;
    }

    public void addTrusted(final Location location, final UUID playerUUID) {
        String key = getLocationKey(location);
        LockInfo oldInfo = locks.get(key);
        if (oldInfo == null) return;
        List<String> newTrusted = new ArrayList<>(oldInfo.trusted());
        newTrusted.add(playerUUID.toString());
        LockInfo newInfo = new LockInfo(oldInfo.owner(), newTrusted, oldInfo.viewable(), oldInfo.donate());
        locks.put(key, newInfo);
        save();
    }

    public void removeTrusted(final Location location, final UUID playerUUID) {
        String key = getLocationKey(location);
        LockInfo oldInfo = locks.get(key);
        if (oldInfo == null) return;
        List<String> newTrusted = new ArrayList<>(oldInfo.trusted());
        newTrusted.remove(playerUUID.toString());
        LockInfo newInfo = new LockInfo(oldInfo.owner(), newTrusted, oldInfo.viewable(), oldInfo.donate());
        locks.put(key, newInfo);
        save();
    }

    public void setTrusted(final Location location, final List<UUID> trustedPlayers) {
        String key = getLocationKey(location);
        LockInfo oldInfo = locks.get(key);
        if (oldInfo == null) return;
        List<String> newTrusted = new ArrayList<>();
        for (UUID uuid : trustedPlayers) {
            newTrusted.add(uuid.toString());
        }
        LockInfo newInfo = new LockInfo(oldInfo.owner(), newTrusted, oldInfo.viewable(), oldInfo.donate());
        locks.put(key, newInfo);
        save();
    }

    public boolean isDonatable(final Location location) {
        LockInfo info = locks.get(getLocationKey(location));
        return info != null && info.donate();
    }

    public boolean isViewable(final Location location) {
        LockInfo info = locks.get(getLocationKey(location));
        return info != null && (info.viewable() || info.donate());
    }

    public void toggleViewable(final Location location) {
        String key = getLocationKey(location);
        LockInfo oldInfo = locks.get(key);
        if (oldInfo == null) return;
        LockInfo newInfo = new LockInfo(oldInfo.owner(), oldInfo.trusted(), !oldInfo.viewable(), oldInfo.donate());
        locks.put(key, newInfo);
        save();
    }

    private void save() {
        try {
            jsonProcessor.getInstance().locks = this.locks;
            jsonProcessor.saveConfiguration();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        try {
            jsonProcessor = JsonProcessor.loadConfiguration(LockConfig.class);
            LockConfig newConfig = jsonProcessor.getInstance();
            this.locks = newConfig.locks;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static LockConfig load() {
        try {
            jsonProcessor = JsonProcessor.loadConfiguration(LockConfig.class);
            return jsonProcessor.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new LockConfig();
    }
}
