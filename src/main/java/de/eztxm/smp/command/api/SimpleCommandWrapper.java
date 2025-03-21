package de.eztxm.smp.command.api;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;

import java.util.Collection;

public class SimpleCommandWrapper implements BasicCommand {

    private final String label;
    private final SimpleCommand command;

    public SimpleCommandWrapper(String label, SimpleCommand command) {
        this.label = label;
        this.command = command;
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        CommandSender sender = source.getSender();
        command.execute(label, sender, args);
    }

    @Override
    public String permission() {
        return command.permission();
    }

    @Override
    public Collection<String> suggest(CommandSourceStack source, String[] args) {
        CommandSender sender = source.getSender();
        return command.suggest(label, sender, args);
    }
}
