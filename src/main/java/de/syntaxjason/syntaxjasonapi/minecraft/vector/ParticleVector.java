package de.syntaxjason.syntaxjasonapi.minecraft.vector;

public interface ParticleVector {
    // Liefert den Nullvektor (0,0,0)
    static ParticleVector zero() {
        return DefaultParticleVector.ZERO;
    }

    double getX();

    double getY();

    double getZ();

    ParticleVector add(double dx, double dy, double dz);

    ParticleVector add(ParticleVector other);
}

