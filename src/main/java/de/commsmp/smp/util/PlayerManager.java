package de.commsmp.smp.util;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

@Setter
@Getter
public class PlayerManager {
    private HashMap<UUID, BukkitTask> scoreboardTasks = new HashMap<>();
    private HashMap<UUID, Boolean> teamchat = new HashMap<>();
    private HashMap<UUID, HashMap<Integer, GraveStone>> graveStones = new HashMap<>();
    private HashMap<UUID, Status> status = new HashMap<>();
    private HashMap<UUID, Mode> modes = new HashMap<>();
}
