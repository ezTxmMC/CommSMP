package de.eztxm.smp.util;

import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

public class Registry {
    private final Plugin plugin;

    public Registry(Plugin plugin) {
        this.plugin = plugin;
    }

    public void registerCommand(String command, CommandExecutor executor) {
        Objects.requireNonNull(this.plugin.getServer().getPluginCommand(command)).setExecutor(executor);
    }

    public void registerListener(Listener listener) {
        this.plugin.getServer().getPluginManager().registerEvents(listener, this.plugin);
    }
}
