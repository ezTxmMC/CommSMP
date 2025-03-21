package de.syntaxjason.syntaxjasonapi.minecraft.raytrace;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class EntityRayTraceResult extends RayTraceResult {
    private final Entity entity;

    public EntityRayTraceResult(Location hitLocation, Entity entity) {
        super(hitLocation);
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
