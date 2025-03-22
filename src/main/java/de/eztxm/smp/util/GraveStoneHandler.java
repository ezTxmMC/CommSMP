package de.eztxm.smp.util;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.UUID;

@Setter
@Getter
public class GraveStoneHandler {
    private final HashMap<UUID, HashMap<Integer, Location>> graveStoneInteractables = new HashMap<>();
}
