package de.eztxm.smp.command.api;

import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

@FunctionalInterface
public interface SimpleCommand {

    void execute(String label, CommandSender sender, String[] args);

    default String permission() {
        return null;
    }

    default List<String> suggest(String label, CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
