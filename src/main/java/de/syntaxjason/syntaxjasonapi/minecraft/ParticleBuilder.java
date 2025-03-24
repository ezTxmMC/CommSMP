package de.syntaxjason.syntaxjasonapi.minecraft;

import de.syntaxjason.syntaxjasonapi.minecraft.tick.TickedAnimation;
import de.syntaxjason.syntaxjasonapi.minecraft.util.ParticleSphereUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class ParticleBuilder {
    private final JavaPlugin plugin;
    private ParticleData particle;
    private Location center;
    private double radius = 1.0;
    private int totalSteps = 36;
    private int particleCount = 1;
    private ParticleAnimationTicked.AnimationType animationType = ParticleAnimationTicked.AnimationType.CIRCLE;
    private double rotation = 0;
    private ParticleCollisionListener collisionListener = null;
    private Player boundCenter = null;
    private Player boundPlayer = null;
    private boolean loop = false;
    private boolean reverse = false;
    private boolean loopedReverse = false;
    private boolean shoot = false;
    private boolean boundPlayerOnly = false;
    private Vector shootDirection = null;
    private double maxDistance = -1;

    public ParticleBuilder(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public static ParticleBuilder builder(JavaPlugin plugin) {
        return new ParticleBuilder(plugin);
    }

    public ParticleBuilder particle(ParticleData particle) {
        this.particle = particle;
        return this;
    }

    public ParticleBuilder boundPlayerOnly(boolean bool) {
        this.boundPlayerOnly = bool;
        return this;
    }

    public ParticleBuilder boundPlayer(Player player) {
        this.boundPlayer = player;
        return this;
    }

    public ParticleBuilder center(Location center) {
        this.center = center;
        return this;
    }

    public ParticleBuilder boundCenter(Player player) {
        this.boundCenter = player;
        return this;
    }

    public ParticleBuilder radius(double radius) {
        this.radius = radius;
        return this;
    }

    public ParticleBuilder steps(int steps) {
        this.totalSteps = steps;
        return this;
    }

    public ParticleBuilder particleCount(int count) {
        this.particleCount = count;
        return this;
    }

    public ParticleBuilder animationType(ParticleAnimationTicked.AnimationType type) {
        this.animationType = type;
        return this;
    }

    public ParticleBuilder rotation(double rotation) {
        this.rotation = rotation;
        return this;
    }

    public ParticleBuilder onCollision(ParticleCollisionListener listener) {
        this.collisionListener = listener;
        return this;
    }

    public ParticleBuilder loop(boolean loop) {
        this.loop = loop;
        return this;
    }

    public ParticleBuilder reverse(boolean reverse) {
        this.reverse = reverse;
        return this;
    }

    public ParticleBuilder loopedReverse(boolean loopedReverse) {
        this.loopedReverse = loopedReverse;
        return this;
    }

    public ParticleBuilder shoot(boolean shoot) {
        this.shoot = shoot;
        return this;
    }

    public ParticleBuilder shootDirection(Vector direction) {
        this.shootDirection = direction;
        return this;
    }

    public ParticleBuilder maxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
        return this;
    }


    public TickedAnimation build() {
        if (particle == null || center == null) {
            throw new IllegalStateException("Particle und Center müssen gesetzt sein!");
        }
        if (shoot && animationType == ParticleAnimationTicked.AnimationType.BEAM && shootDirection != null) {
            double distance = shootDirection.length();
            double effectiveDistance = (maxDistance > 0) ? Math.min(distance, maxDistance) : distance;
            List<Location> points = generateBeamPoints(center, shootDirection, totalSteps, effectiveDistance);
            return new ProjectileAnimation(plugin, particle, points, shootDirection.clone().normalize().multiply(0.5), loop, collisionListener);
        }
        if (shoot && (animationType == ParticleAnimationTicked.AnimationType.SPHERE ||
                animationType == ParticleAnimationTicked.AnimationType.HSPHERE)) {
            boolean hollow = (animationType == ParticleAnimationTicked.AnimationType.HSPHERE);
            List<Location> points = ParticleSphereUtil.generateSphere(center, radius, hollow);
            org.bukkit.util.Vector vel = (boundCenter != null)
                    ? boundCenter.getEyeLocation().getDirection().normalize().multiply(0.5)
                    : new org.bukkit.util.Vector(0, 0, 1);
            return new ProjectileAnimation(plugin, particle, points, vel, collisionListener);
        }
        return new ParticleAnimationTicked(
                plugin,
                particle,
                center,
                radius,
                totalSteps,
                particleCount,
                animationType,
                rotation,
                collisionListener,
                loop,
                reverse,
                loopedReverse,
                boundPlayerOnly,
                boundCenter,
                boundPlayer
        );
    }

    private List<Location> generateBeamPoints(Location start, Vector direction, int steps, double length) {
        List<Location> points = new ArrayList<>();
        Vector normDir = direction.normalize();
        double stepLength = length / steps;
        for (int i = 0; i < steps; i++) {
            Location point = start.clone().add(normDir.clone().multiply(stepLength * i));
            points.add(point);
        }
        return points;
    }
}
