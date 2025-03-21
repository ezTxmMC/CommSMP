package de.syntaxjason.syntaxjasonapi.minecraft;

import de.syntaxjason.syntaxjasonapi.minecraft.tick.TickedAnimation;
import de.syntaxjason.syntaxjasonapi.minecraft.raytrace.BlockRayTraceResult;
import de.syntaxjason.syntaxjasonapi.minecraft.raytrace.RayTraceResult;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.List;

public class ProjectileAnimation implements TickedAnimation {

    private final JavaPlugin plugin;
    private final ParticleData particle;
    private final List<Location> points;
    private final Vector velocity;
    private final World world;
    private final ParticleCollisionListener collisionListener;
    private boolean finished = false;

    public ProjectileAnimation(JavaPlugin plugin, ParticleData particle, List<Location> points, Vector velocity, ParticleCollisionListener collisionListener) {
        this.plugin = plugin;
        this.particle = particle;
        this.points = points;
        if (points.isEmpty()) {
            throw new IllegalArgumentException("Die Punkte-Liste darf nicht leer sein!");
        }
        this.world = points.get(0).getWorld();
        this.velocity = velocity;
        this.collisionListener = collisionListener;
    }

    @Override
    public void tick(long delta) {
        if (finished) return;
        for (int i = 0; i < points.size(); i++) {
            Location loc = points.get(i);
            loc.add(velocity);

            if (collisionListener != null && loc.getWorld().getBlockAt(loc).getType() != Material.AIR) {
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    RayTraceResult result = new BlockRayTraceResult(loc, loc.getWorld().getBlockAt(loc));
                    collisionListener.onCollision(loc, result);
                });
                finished = true;
                break;
            }
            ParticleLib.sendParticleToAll(world, particle, loc);
        }
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}
