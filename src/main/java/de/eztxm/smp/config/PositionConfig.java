package de.eztxm.smp.config;

import de.eztxm.ezlib.config.JsonConfig;
import de.eztxm.ezlib.config.object.JsonObject;
import de.eztxm.smp.SMP;
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
import java.util.Set;

public class PositionConfig extends JsonConfig {

    private final Player player;

    public PositionConfig(Player player) {
        super(SMP.getInstance().getDataFolder().getPath(), player.getUniqueId().toString(), false);
        this.player = player;
    }

    public void setPostion(String name, Location location) {
        this.set(name, new StringBuilder()
                .append(location.getX()).append(":")
                .append(location.getY()).append(":")
                .append(location.getZ()).append(":")
                .append(location.getPitch()).append(":")
                .append(location.getYaw()).append(":")
                .append(location.getWorld().getName()).toString());
        save();
    }

    public void removePosition(String name) {
        this.remove(name);
        save();
    }

    public Location getPosition(String name) {
        if(get(name) == null) return null;
        String[] args = name.split(":");
        return new Location(Bukkit.getWorld(args[5]), Double.parseDouble(args[0]),
                Double.parseDouble(args[1]), Double.parseDouble(args[2]), Float.parseFloat(args[3]), Float.parseFloat(args[4]));
    }

    public Set<String> getPositions() {
        JsonObject object = getCustomJsonObject();
        return object.getElements().keySet();
    }

    @Override
    public void save() {
        Path filePath = Paths.get(this.getConfigPath(), this.getConfigName());
        try {
            Files.writeString(filePath, this.getCustomJsonObject().toJsonString(true));
        } catch (IOException ignored) {}
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
        try (BufferedReader reader = new BufferedReader(new FileReader(this.getConfigPath()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            Field field = this.getClass().getDeclaredField("customJsonObject");
            field.setAccessible(true);
            field.set(this, JsonObject.parseValue(builder.toString()));
            field.setAccessible(false);
        } catch (Exception ignored) {}
    }
}
