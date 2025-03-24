package de.commsmp.smp.command;

import de.commsmp.smp.SMP;
import de.commsmp.smp.command.api.SimpleCommand;
import de.commsmp.smp.config.PositionConfig;
import de.commsmp.smp.util.AdventureColor;
import de.syntaxjason.syntaxjasonapi.minecraft.ParticleAnimationTicked;
import de.syntaxjason.syntaxjasonapi.minecraft.ParticleBuilder;
import de.syntaxjason.syntaxjasonapi.minecraft.ParticleData;
import de.syntaxjason.syntaxjasonapi.minecraft.tick.AnimationHolder;
import de.syntaxjason.syntaxjasonapi.minecraft.tick.TickedAnimation;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;

public class PositionCommand implements SimpleCommand {

    private HashMap<UUID, AnimationHolder> animation = new HashMap<>();

    private PositionConfig config;

    @Override
    public void execute(String label, CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player player)) {
            return;
        }
        this.config = PositionConfig.getPositionData(player);

        switch (label.toLowerCase()) {
            case "setposition", "setpos" -> setPosition(player, args);
            case "delposition", "deleteposition", "delpos" -> delPosition(player, args);
            case "positionlist", "poslist" -> listPositions(player);
            default -> position(player, args);
        }
    }

    private void position(Player player, String[] args) {
        if (!checkArgs(player, args)) {
            return;
        }
        if (config == null) {
            player.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix() + "&cEs sind keine Positionen gespeichert!"));
            return;
        }
        Location targetLocation = config.getPosition(args[0]);
        if (targetLocation == null) {
            player.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix() + "&cDiese Position existiert nicht!"));
            return;
        }

        if (animation.containsKey(player.getUniqueId())) {
            animation.get(player.getUniqueId()).stop();
            animation.remove(player.getUniqueId());
            player.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix() + "&aPartikeleffekte für die Position " + args[0] + " deaktiviert!"));
            return;
        }

        // Berechne den Richtungsvektor vom Spieler zur Target-Location
        Vector shootDirection = targetLocation.toVector().subtract(player.getLocation().toVector());

        AnimationHolder animationHolder = new AnimationHolder();

        TickedAnimation beamAnimation = ParticleBuilder.builder(SMP.getInstance())
                .particle(ParticleData.DUST)
                .boundPlayerOnly(true)
                .boundPlayer(player)
                .center(player.getLocation())
                .loop(true)
                .steps(20)
                .particleCount(5)
                .animationType(ParticleAnimationTicked.AnimationType.BEAM)
                .shoot(true)
                .shootDirection(shootDirection)
                .build();

        TickedAnimation circleAnimation = ParticleBuilder.builder(SMP.getInstance())
                .particle(ParticleData.DUST)
                .boundPlayerOnly(true)
                .boundPlayer(player)
                .center(targetLocation)
                .loop(true)
                .steps(50)
                .particleCount(1)
                .animationType(ParticleAnimationTicked.AnimationType.CIRCLE)
                .shoot(false)
                .build();

        animationHolder.addAnimation(beamAnimation);
        animationHolder.addAnimation(circleAnimation);
        animationHolder.start();

        animation.put(player.getUniqueId(), animationHolder);

        player.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix() + "&aPartikeleffekte für die Position " + args[0] + " aktiviert!"));
    }

    private void listPositions(Player player) {
        if (config == null) {
            player.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix() + "&cEs sind keine Positionen gespeichert!"));
            return;
        }
        Set<String> positions = config.getPositions();
        if (positions.isEmpty()) {
            player.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix() + "&cDu hast noch keine Positionen gesetzt!"));
            return;
        }
        player.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix() + "&aGespeicherte Positionen:"));
        positions.forEach(name -> player.sendMessage(AdventureColor.apply("&7- " + name)));
    }

    private void delPosition(Player player, String[] args) {
        if (!checkArgs(player, args)) {
            return;
        }
        if (config == null) {
            player.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix() + "&cEs sind keine Positionen gespeichert!"));
            return;
        }
        if (config.get(nameOrKey(args[0])) == null) {
            player.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix() + "&cDiese Position existiert nicht!"));
            return;
        }
        config.removePosition(args[0]);
        PositionConfig.updatePositionData(player, config);
        player.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix() + "&aPosition " + args[0] + " wurde gelöscht."));
    }

    private void setPosition(Player player, String[] args) {
        if (!checkArgs(player, args)) {
            return;
        }
        if (config == null) {
            player.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix() + "&cFehler beim Laden der Positionen!"));
            return;
        }
        config.setPosition(args[0], player.getLocation());
        PositionConfig.updatePositionData(player, config);
        player.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix() + "&aPosition " + args[0] + " wurde gespeichert."));
    }

    @Override
    public List<String> suggest(String label, CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            return List.of();
        }
        config = PositionConfig.getPositionData(player);
        Set<String> positions = config.getPositions();

        if (positions.isEmpty()) {
            return List.of();
        }

        List<String> suggestions = new ArrayList<>();
        String input = (args.length > 0) ? args[0].toLowerCase() : "";

        for (String pos : positions) {
            if (pos.toLowerCase().contains(input)) {
                suggestions.add(pos);
            }
        }

        suggestions.sort((a, b) -> {
            int distanceA = levenshteinDistance(a.toLowerCase(), input);
            int distanceB = levenshteinDistance(b.toLowerCase(), input);
            return Integer.compare(distanceA, distanceB);
        });

        return suggestions;
    }

    private int levenshteinDistance(String s, String t) {
        int[][] dp = new int[s.length() + 1][t.length() + 1];

        for (int i = 0; i <= s.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= t.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= s.length(); i++) {
            for (int j = 1; j <= t.length(); j++) {
                int cost = (s.charAt(i - 1) == t.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
            }
        }

        return dp[s.length()][t.length()];
    }

    private boolean checkArgs(Player player, String[] args) {
        if (args.length >= 10) {
            player.sendMessage("Du bist behindert... Aber mach weiter so xD");
            return false;
        }
        if (args.length != 1) {
            player.sendMessage(AdventureColor.apply(SMP.getInstance().getPrefix() + """
                    &7Guten Tag lieber Bürgergeld Empfänger. Leider ist mir aufgefallen das du ziemlich Unterkomplex bist. 
                    Diese Vermutung kam daher, das du einen Befehl verwendest, der ein einziges Argument benötigt, du jedoch keines oder zu viele angegeben hast.
                    Damit du beim nächsten Mal weißt, wie du den Befehl korrekt benutzt, zeige ich dir mal, wie er richtig aussehen sollte: 
                    Nutze zuerst ein Schrägstrich (/) gefolgt von "setPosition" und gib dann einen Namen an.
                    Möchtest du hingegen eine Position anzeigen, gib "/position" gefolgt von einem bereits vergebenen Namen ein.
                    """));
            return false;
        }
        return true;
    }

    private String nameOrKey(String arg) {
        return arg;
    }
}
