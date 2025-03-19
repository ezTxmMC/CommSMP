package de.eztxm.smp.config;

import de.eztxm.ezlib.config.annotation.JsonClassConfig;
import de.eztxm.ezlib.config.annotation.JsonClassElement;
import de.eztxm.ezlib.config.reflect.JsonProcessor;
import org.bukkit.Location;

import java.util.*;

@JsonClassConfig(path = "plugins/LockSystem", fileName = "locks.json")
public class LockConfig {

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
        return (info != null && info.owner != null) ? UUID.fromString(info.owner) : null;
    }

    public void lock(final Location location, final UUID owner) {
        String key = getLocationKey(location);
        LockInfo info = new LockInfo();
        info.owner = owner.toString();
        info.trusted = new ArrayList<>();
        info.viewable = false;
        info.donate = false;
        locks.put(key, info);
        save();
    }

    public void unlock(final Location location) {
        locks.remove(getLocationKey(location));
        save();
    }

    public boolean donate(final Location location) {
        LockInfo info = locks.get(getLocationKey(location));
        if (info == null) return false;
        if (!info.donate) {
            info.donate = true;
            save();
            return true;
        }
        info.donate = false;
        save();
        return false;
    }

    public List<UUID> getTrusted(final Location location) {
        LockInfo info = locks.get(getLocationKey(location));
        List<UUID> result = new ArrayList<>();
        if (info != null && info.trusted != null) {
            for (String uuidStr : info.trusted) {
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
        LockInfo info = locks.get(key);
        if (info == null) return;
        if (info.trusted == null) {
            info.trusted = new ArrayList<>();
        }
        info.trusted.add(playerUUID.toString());
        save();
    }

    public void removeTrusted(final Location location, final UUID playerUUID) {
        String key = getLocationKey(location);
        LockInfo info = locks.get(key);
        if (info == null || info.trusted == null) return;
        info.trusted.remove(playerUUID.toString());
        save();
    }

    public void setTrusted(final Location location, final List<UUID> trustedPlayers) {
        String key = getLocationKey(location);
        LockInfo info = locks.get(key);
        if (info == null) return;
        List<String> trusted = new ArrayList<>();
        for (UUID uuid : trustedPlayers) {
            trusted.add(uuid.toString());
        }
        info.trusted = trusted;
        save();
    }

    public boolean isDonatable(final Location location) {
        LockInfo info = locks.get(getLocationKey(location));
        return info != null && info.donate;
    }

    public boolean isViewable(final Location location) {
        LockInfo info = locks.get(getLocationKey(location));
        return info != null && (info.viewable || info.donate);
    }

    public void toggleViewable(final Location location) {
        LockInfo info = locks.get(getLocationKey(location));
        if (info == null) return;
        info.viewable = !info.viewable;
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

    public static class LockInfo {
        @JsonClassElement
        public String owner;
        @JsonClassElement
        public List<String> trusted = new ArrayList<>();
        @JsonClassElement
        public boolean viewable;
        @JsonClassElement
        public boolean donate;

        public LockInfo() {
        }
    }
}
