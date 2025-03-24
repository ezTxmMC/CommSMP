package de.syntaxjason.syntaxjasonapi.minecraft;

import de.syntaxjason.syntaxjasonapi.minecraft.raytrace.BlockRayTraceResult;
import de.syntaxjason.syntaxjasonapi.minecraft.raytrace.RayTraceResult;
import de.syntaxjason.syntaxjasonapi.minecraft.tick.TickedAnimation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class ProjectileAnimation implements TickedAnimation {

    private final JavaPlugin plugin;
    private final ParticleData particle;
    private final List<Location> points;
    private final List<Location> originalPoints;
    private final Vector velocity;
    private final World world;
    private final ParticleCollisionListener collisionListener;
    private final boolean loop;
    private final double effectiveDistance;
    private boolean finished = false;

    public ProjectileAnimation(JavaPlugin plugin, ParticleData particle, List<Location> points, Vector velocity,
                               ParticleCollisionListener collisionListener, boolean loop, double effectiveDistance) {
        this.plugin = plugin;
        this.particle = particle;
        if (points.isEmpty()) {
            throw new IllegalArgumentException("Die Punkte-Liste darf nicht leer sein!");
        }
        this.originalPoints = new ArrayList<>();
        for (Location loc : points) {
            this.originalPoints.add(loc.clone());
        }
        this.points = points;
        this.world = points.get(0).getWorld();
        this.velocity = velocity;
        this.collisionListener = collisionListener;
        this.loop = loop;
        this.effectiveDistance = effectiveDistance;
    }

    @Override
    public void tick(long delta) {
        if (finished) return;
        for (Location loc : points) {
            loc.add(velocity);
            if (collisionListener != null && loc.getWorld().getBlockAt(loc).getType() != Material.AIR) {
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    RayTraceResult result = new BlockRayTraceResult(loc, loc.getWorld().getBlockAt(loc));
                    collisionListener.onCollision(loc, result);
                });
                finished = true;
                return;
            }
            ParticleLib.sendParticleToAll(world, particle, loc);
        }

        Location start = originalPoints.get(0);
        Location current = points.get(0);
        if (start.distance(current) >= effectiveDistance) {
            if (loop) {
                for (int i = 0; i < points.size(); i++) {
                    points.get(i).setX(originalPoints.get(i).getX());
                    points.get(i).setY(originalPoints.get(i).getY());
                    points.get(i).setZ(originalPoints.get(i).getZ());
                }
                return;
            }
            finished = true;
        }
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}
