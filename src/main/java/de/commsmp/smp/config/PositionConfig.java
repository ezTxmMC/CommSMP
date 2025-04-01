package de.commsmp.smp.config;

import de.commsmp.smp.SMP;
import de.eztxm.ezlib.config.JsonConfig;
import de.eztxm.ezlib.config.object.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class PositionConfig extends JsonConfig {

    private static final HashMap<UUID, PositionConfig> instances = new HashMap<>();

    public PositionConfig(Player player) {
        super(SMP.getInstance().getDataFolder().getPath(), player.getUniqueId().toString(), false);
    }

    public PositionConfig(UUID uniqueId) {
        super(SMP.getInstance().getDataFolder().getPath(), uniqueId.toString(), false);
    }

    public static PositionConfig getPositionData(Player player) {
        if (instances.containsKey(player.getUniqueId())) {
            return instances.get(player.getUniqueId());
        }
        return new PositionConfig(player);
    }

    public static void updatePositionData(Player player, PositionConfig config) {
        instances.remove(player.getUniqueId());
        instances.put(player.getUniqueId(), config);
    }

    public void setPosition(String name, Location location) {
        this.set(name, location.getX() + ";" +
                location.getY() + ";" +
                location.getZ() + ";" +
                location.getPitch() + ";" +
                location.getYaw() + ";" +
                location.getWorld().getName());
        save();
    }

    public void removePosition(String name) {
        this.remove(name);
        save();
    }

    public Location getPosition(String name) {
        if (get(name) == null)
            return null;
        String[] args = get(name).asString().split(";");
        return new Location(Bukkit.getWorld(args[5]), Double.parseDouble(args[0]),
                Double.parseDouble(args[1]), Double.parseDouble(args[2]), Float.parseFloat(args[3]),
                Float.parseFloat(args[4]));
    }

    public Set<String> getPositions() {
        JsonObject object = getCustomJsonObject();
        return object.getElements().keySet();
    }

    @Override
    public void save() {
        Path filePath = Paths.get(this.getConfigFolder().toString(), this.getConfigName());
        try {
            Files.writeString(filePath, this.getCustomJsonObject().toJsonString(true));
        } catch (IOException ignored) {
        }
    }

    public void reload() {
        try {
            Field field = this.getClass().getDeclaredField("customJsonObject");
            field.setAccessible(true);
            field.set(this, null);
            field.setAccessible(false);
            load();
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    private void load() {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(this.getConfigFolder().toString()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            Field field = this.getClass().getDeclaredField("customJsonObject");
            field.setAccessible(true);
            field.set(this, JsonObject.parseValue(builder.toString()));
            field.setAccessible(false);
        } catch (Exception ignored) {
        }
    }
}
