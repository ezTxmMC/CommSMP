package de.eztxm.smp.scoreboard;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

@Setter
@Getter
public class ScoreboardBuilder {
    private Scoreboard scoreboard;
    private Objective objective;

    public ScoreboardBuilder create() {
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        return this;
    }

    public ScoreboardBuilder objective(String objectiveName, Criteria criteria, Component displayName) {
        this.objective = scoreboard.registerNewObjective(objectiveName, criteria, displayName);
        return this;
    }

    public ScoreboardBuilder displaySlot(DisplaySlot displaySlot) {
        this.objective.setDisplaySlot(displaySlot);
        return this;
    }

    public ScoreboardBuilder score(int slot, String context) {
        this.objective.getScore(context).setScore(slot);
        return this;
    }
}
