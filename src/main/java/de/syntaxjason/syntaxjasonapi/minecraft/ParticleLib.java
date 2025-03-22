package de.syntaxjason.syntaxjasonapi.minecraft;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class ParticleLib {

    private static JavaPlugin plugin;

    private ParticleLib() {
    }

    public static void initialize(JavaPlugin plugin) {
        ParticleLib.plugin = plugin;
    }

    public static void sendParticleToAll(World world, ParticleData particle, Location location) {
        try {
            Particle spigotParticle = Particle.valueOf(particle.getName().toUpperCase());
            if (spigotParticle == Particle.DUST) {
                Particle.DustOptions options = new Particle.DustOptions(Color.RED, 1.0F);
                world.spawnParticle(spigotParticle, location, 1, 0, 0, 0, 0, options);
                return;
            }
            world.spawnParticle(spigotParticle, location, 1, 0, 0, 0, 0, null);
        } catch (IllegalArgumentException ex) {
            plugin.getLogger().warning("Unbekannter Partikel: " + particle.getName());
        }
    }

    public static void sendParticleToBound(Player boundCenter, ParticleData particle, Location location) {
        try {
            Particle spigotParticle = Particle.valueOf(particle.getName().toUpperCase());
            if (spigotParticle == Particle.DUST) {
                Particle.DustOptions options = new Particle.DustOptions(Color.RED, 1.0F);
                boundCenter.spawnParticle(spigotParticle, location, 1, 0, 0, 0, 0, options);
                return;
            }
            boundCenter.spawnParticle(spigotParticle, location, 1, 0, 0, 0, 0, null);
        } catch (IllegalArgumentException ex) {
            plugin.getLogger().warning("Unbekannter Partikel: " + particle.getName());
        }
    }
}
