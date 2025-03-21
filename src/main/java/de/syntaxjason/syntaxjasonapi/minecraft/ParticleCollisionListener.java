package de.syntaxjason.syntaxjasonapi.minecraft;

import de.syntaxjason.syntaxjasonapi.minecraft.raytrace.RayTraceResult;
import org.bukkit.Location;

public interface ParticleCollisionListener {
    void onCollision(Location hitLocation, RayTraceResult result);
}
