package de.syntaxjason.syntaxjasonapi.minecraft.raytrace;

import org.bukkit.Location;

public abstract class RayTraceResult {
    private final Location hitLocation;

    public RayTraceResult(Location hitLocation) {
        this.hitLocation = hitLocation;
    }

    public Location getHitLocation() {
        return hitLocation;
    }
}
