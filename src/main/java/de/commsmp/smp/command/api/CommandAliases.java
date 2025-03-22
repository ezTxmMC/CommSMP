package de.commsmp.smp.command.api;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CommandAliases {

    private final List<String> aliases;

    private CommandAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public static CommandAliases of(Collection<String> aliases) {
        return new CommandAliases(List.copyOf(aliases));
    }

    public static CommandAliases of(String... aliases) {
        return new CommandAliases(Arrays.asList(aliases));
    }

    public Collection<String> asCollection() {
        return Collections.unmodifiableList(aliases);
    }

    public boolean isEmpty() {
        return aliases.isEmpty();
    }
}
