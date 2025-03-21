package de.eztxm.smp.command;

import de.eztxm.smp.command.api.SimpleCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class TestCommand implements SimpleCommand {

    @Override
    public void execute(String label, CommandSender sender, String[] args) {

    }

    @Override
    public List<String> suggest(String label, CommandSender sender, String[] args) {
        return SimpleCommand.super.suggest(label, sender, args);
    }
}
