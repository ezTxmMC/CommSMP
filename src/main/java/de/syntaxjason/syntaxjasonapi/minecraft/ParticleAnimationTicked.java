package de.syntaxjason.syntaxjasonapi.minecraft;

import de.syntaxjason.syntaxjasonapi.minecraft.raytrace.BlockRayTraceResult;
import de.syntaxjason.syntaxjasonapi.minecraft.raytrace.RayTraceResult;
import de.syntaxjason.syntaxjasonapi.minecraft.tick.TickedAnimation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class ParticleAnimationTicked implements TickedAnimation {

    private final JavaPlugin plugin;
    private final ParticleData particle;
    private final Location center;
    private final double radius;
    private final int totalSteps;
    private final int particleCount;
    private final AnimationType animationType;
    private final double rotation;
    private final ParticleCollisionListener collisionListener;
    private final boolean loop;
    private final boolean reverse;
    private final boolean loopedReverse;
    private final boolean boundPlayerOnly;
    private final Player boundCenter;
    private final Player boundPlayer;
    private int direction;
    private int currentStep;
    private boolean finished = false;
    public ParticleAnimationTicked(JavaPlugin plugin, ParticleData particle, Location center, double radius,
                                   int totalSteps, int particleCount, AnimationType animationType,
                                   double rotation, ParticleCollisionListener collisionListener,
                                   boolean loop, boolean reverse, boolean loopedReverse, boolean boundPlayerOnly, Player boundCenter, Player boundPlayer) {
        this.plugin = plugin;
        this.particle = particle;
        this.center = center;
        this.radius = radius;
        this.totalSteps = totalSteps;
        this.particleCount = particleCount;
        this.animationType = animationType;
        this.rotation = rotation;
        this.collisionListener = collisionListener;
        this.loop = loop;
        this.reverse = reverse;
        this.loopedReverse = loopedReverse;
        this.boundPlayerOnly = boundPlayerOnly;
        this.boundCenter = boundCenter;
        this.boundPlayer = boundPlayer;

        if (reverse && !loopedReverse) {
            this.currentStep = totalSteps;
            this.direction = -1;
        } else {
            this.currentStep = 0;
            this.direction = 1;
        }
    }

    @Override
    public void tick(long delta) {
        if (finished) return;
        World world = center.getWorld();
        Location effectiveCenter = (boundCenter != null) ? boundCenter.getLocation() : center;
        Location newPos = computeNextPosition(effectiveCenter);
        if (collisionListener != null && world.getBlockAt(newPos).getType() != Material.AIR) {
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                RayTraceResult result = new BlockRayTraceResult(newPos, world.getBlockAt(newPos));
                collisionListener.onCollision(newPos, result);
            });
        }
        spawnParticles(world, newPos);
        updateStep();
    }

    private void updateStep() {
        currentStep += direction;
        if (loopedReverse) {
            if (currentStep >= totalSteps) {
                direction = -1;
            } else if (currentStep <= 0) {
                direction = 1;
            }
        } else if (reverse && !loopedReverse) {
            if (currentStep <= 0) {
                finished = true;
            }
        } else if (loop) {
            if (currentStep >= totalSteps) {
                currentStep = 0;
            }
        } else {
            if (currentStep >= totalSteps) {
                finished = true;
            }
        }
    }

    private Location computeNextPosition(Location effectiveCenter) {
        double angle = (2 * Math.PI * currentStep) / totalSteps;
        double xOffset = 0, yOffset = 0, zOffset = 0;
        switch (animationType) {
            case CIRCLE:
                xOffset = radius * Math.cos(angle);
                zOffset = radius * Math.sin(angle);
                break;
            case SPIRAL:
                double spiralRadius = radius * (1 + (currentStep / (double) totalSteps));
                xOffset = spiralRadius * Math.cos(angle);
                zOffset = spiralRadius * Math.sin(angle);
                break;
            case OVAL:
                double a = radius;
                double b = radius * 0.6;
                xOffset = a * Math.cos(angle);
                zOffset = b * Math.sin(angle);
                break;
            case OVAL_SPIRAL:
                double ovalA = radius;
                double ovalB = radius * 0.6;
                xOffset = ovalA * Math.cos(angle);
                zOffset = ovalB * Math.sin(angle);
                yOffset = (currentStep / (double) totalSteps) * 2.0;
                break;
            case RECTANGLE:
                int side = (currentStep * 4) / totalSteps; // 0 bis 3
                double progress = (currentStep % (totalSteps / 4)) / (double) (totalSteps / 4);
                double half = radius;
                switch (side) {
                    case 0:
                        xOffset = half;
                        zOffset = half - (2 * half * progress);
                        break;
                    case 1:
                        xOffset = half - (2 * half * progress);
                        zOffset = -half;
                        break;
                    case 2:
                        xOffset = -half;
                        zOffset = -half + (2 * half * progress);
                        break;
                    case 3:
                        xOffset = -half + (2 * half * progress);
                        zOffset = half;
                        break;
                }
                break;
            case BEAM:
                Vector lookDirection = effectiveCenter.getDirection().normalize();
                double factor = (currentStep / (double) totalSteps) * radius * 2;
                xOffset = lookDirection.getX() * factor;
                yOffset = lookDirection.getY() * factor;
                zOffset = lookDirection.getZ() * factor;
                break;
            case ELLIPTICAL_WAVE:
                double aElliptical = radius;
                double bElliptical = radius * 0.5;
                xOffset = aElliptical * Math.cos(angle);
                zOffset = bElliptical * Math.sin(angle);
                yOffset = Math.sin(angle * 2) * (radius * 0.3);
                break;
        }
        if (rotation != 0) {
            double rotatedX = xOffset * Math.cos(rotation) - zOffset * Math.sin(rotation);
            double rotatedZ = xOffset * Math.sin(rotation) + zOffset * Math.cos(rotation);
            xOffset = rotatedX;
            zOffset = rotatedZ;
        }
        Location newPos = effectiveCenter.clone().add(xOffset, yOffset, zOffset);
        newPos.add(randomOffset(), 0, randomOffset());
        return newPos;
    }

    private double randomOffset() {
        return (Math.random() - 0.5) * 0.2;
    }

    private void spawnParticles(World world, Location location) {
        for (int i = 0; i < particleCount; i++) {
            Location spawnLoc = location.clone().add(randomOffset(), 0, randomOffset());
            if (boundPlayerOnly) {
                ParticleLib.sendParticleToBound(boundPlayer, particle, spawnLoc);
                continue;
            }
            ParticleLib.sendParticleToAll(world, particle, spawnLoc);
        }
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    public enum AnimationType {
        CIRCLE,
        SPIRAL,
        OVAL,
        OVAL_SPIRAL,
        RECTANGLE,
        BEAM,
        ELLIPTICAL_WAVE,
        SPHERE,
        HSPHERE
    }
}
