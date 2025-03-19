package de.eztxm.smp.util;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

@Setter
@Getter
public class PlayerManager {
    private HashMap<UUID, BukkitTask> scoreboardTasks = new HashMap<>();
}
