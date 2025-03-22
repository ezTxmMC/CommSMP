package de.commsmp.smp.command.api;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;

public class SimpleCommandRegistry {

    private final JavaPlugin plugin;

    public SimpleCommandRegistry(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(String label, SimpleCommand simpleCommand) {
        register(label, CommandAliases.of(), simpleCommand);
    }

    public void register(String label, CommandAliases aliases, SimpleCommand simpleCommand) {
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            event.registrar().register(
                    label,
                    null,
                    Collections.emptyList(),
                    new SimpleCommandWrapper(label, simpleCommand)
            );

            if (aliases.isEmpty()) {
                return;
            }
            for (String alias : aliases.asCollection()) {
                event.registrar().register(
                        alias,
                        null,
                        java.util.Collections.emptyList(),
                        new SimpleCommandWrapper(alias, simpleCommand)
                );
            }
        });
    }
}
