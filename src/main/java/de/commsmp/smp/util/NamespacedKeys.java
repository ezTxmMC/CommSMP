package de.commsmp.smp.util;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class NamespacedKeys {

    public static NamespacedKey BACKPACK_DATA;
    public static NamespacedKey BACKPACK_ID;
    public static NamespacedKey CREATOR;
    public static NamespacedKey CREATION_DATE;
    public static NamespacedKey LAST_UPDATED;

    public static void initialize(JavaPlugin plugin) {
        BACKPACK_DATA = new NamespacedKey(plugin, "backpack_data");
        BACKPACK_ID = new NamespacedKey(plugin, "backpack_id");
        CREATOR = new NamespacedKey(plugin, "creator");
        CREATION_DATE = new NamespacedKey(plugin, "creation_date");
        LAST_UPDATED = new NamespacedKey(plugin, "last_updated");
    }
}
