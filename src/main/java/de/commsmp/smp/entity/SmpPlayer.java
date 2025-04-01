package de.commsmp.smp.entity;

import de.commsmp.smp.SMP;
import de.commsmp.smp.config.LockConfig;
import de.commsmp.smp.config.PositionConfig;
import de.commsmp.smp.util.GraveStone;
import de.commsmp.smp.util.cache.IntegerCache;
import de.commsmp.smp.util.cache.UUIDCache;
import org.bukkit.Location;

import java.util.UUID;


public class SmpPlayer {

    private static final UUIDCache<SmpPlayer> cache = new UUIDCache<>(3600);
    private final LockConfig lockConfig;
    private final PositionConfig positionConfig;
    private final IntegerCache<GraveStone> graveStones;
    private boolean teamchat = false;

    public SmpPlayer(LockConfig lockConfig, PositionConfig positionConfig) {
        this.lockConfig = lockConfig;
        this.positionConfig = positionConfig;
        this.graveStones = new IntegerCache<>(Integer.MAX_VALUE);
        // Implement Logic to put Gravestones in "graveStones HashMap"
    }

    public static SmpPlayer get(UUID uniqueId) {
        if (!cache.has(uniqueId)) {
            return new SmpPlayer(SMP.getInstance().getLockConfig(), new PositionConfig(uniqueId));
        }
        return cache.get(uniqueId);
    }

    public void addPosition(String name, Location location) {

    }

    public IntegerCache<GraveStone> getGraveStones() {
        return graveStones;
    }

    public LockConfig getLockConfig() {
        return lockConfig;
    }

    public PositionConfig getPositionConfig() {
        return positionConfig;
    }
}
