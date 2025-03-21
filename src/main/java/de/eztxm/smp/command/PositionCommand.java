package de.eztxm.smp.command;

import de.eztxm.smp.SMP;
import de.eztxm.smp.util.BukkitColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PositionCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(commandSender instanceof Player player)) {
            return false;
        }

        switch (label.toLowerCase()) {
            case "setposition",
                    "setpos" -> setPostion(player, args);
            case "delposition",
                    "deletepostion",
                    "delpos" -> delPosition(player, args);
            case "positionlist",
                    "poslist" -> listPositions(player);
            default -> position(player, args);
        }
        return false;
    }

    private boolean checkArgs(Player player, String[] args) {
        if(args.length >= 10) {
            player.sendMessage("Du bist behindert... Aber mach weiter so xD");
            return false;
        }
        if(args.length != 1) {
            player.sendMessage(BukkitColor.apply(SMP.getInstance().getPrefix() + """
                    &7Guten Tag lieber Bürgergeld Empfänger. Leider ist mir aufgefallen das du ziemlich Unterkomplex bist. 
                    Diese Vermutung kam daher, das du ein Befehl verwendest, der mehrere Argumente benötigt, du jedoch keines oder zu viele angegeben hast.
                    Damit du für das nächste mal weißt wie du den Befehl hand haben musst, zeige ich dir mal, wie er richtig benutzt wird. 
                    Als erstes nutzt du ein Schrägstrich (Slash) und gibst ein "setPosition", machst ein Leerzeichen und gibst einen Namen ein.
                    Falls du jedoch eine Position anzeigen lassen willst, solltest du wieder ein Schrägstrich eingeben mit dem Wort "position". 
                    Nach diesem Wort fügst du ein Leerzeichen ein, anschließend schreibst du ein Namen, den du schon einmal verwendet hast um eine Position zu setzen.
                    In beiden Fällen drückst du am ende die Entertaste (der Pfeil der nach links zeigt und hinten einen strich nach oben hat oder die Numpad Taste "Enter")
                    
                    Falls du diese Erklärung nicht begreifen kontest, wünsche ich dir viel Erfolg im weiteren Leben als Unterkomplexe Persöhnlichkeit.
                    Ich hoffe du verlässt diesen Server für immer!
                    """));
            return false;
        }
        return true;
    }

    private void position(Player player, String[] args) {
        if(!checkArgs(player, args)) {
            return;
        }
    }

    private void listPositions(Player player, String[] args) {
        if(!checkArgs(player, args)) {
            return;
        }
    }

    private void delPosition(Player player, String[] args) {
        if(!checkArgs(player, args)) {
            return;
        }
    }

    private void setPostion(Player player, String[] args) {
        if(!checkArgs(player, args)) {
            return;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        return null;
    }
}
