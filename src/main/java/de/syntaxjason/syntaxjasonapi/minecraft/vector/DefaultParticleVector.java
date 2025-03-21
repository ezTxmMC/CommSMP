package de.syntaxjason.syntaxjasonapi.minecraft.vector;

public class DefaultParticleVector implements ParticleVector {
    public static final DefaultParticleVector ZERO = new DefaultParticleVector(0, 0, 0);

    private final double x;
    private final double y;
    private final double z;

    public DefaultParticleVector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public double getX() { return x; }

    @Override
    public double getY() { return y; }

    @Override
    public double getZ() { return z; }

    @Override
    public ParticleVector add(double dx, double dy, double dz) {
        return new DefaultParticleVector(x + dx, y + dy, z + dz);
    }

    @Override
    public ParticleVector add(ParticleVector other) {
        return new DefaultParticleVector(x + other.getX(), y + other.getY(), z + other.getZ());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParticleVector)) return false;
        ParticleVector other = (ParticleVector) o;
        return Double.compare(x, other.getX()) == 0 &&
                Double.compare(y, other.getY()) == 0 &&
                Double.compare(z, other.getZ()) == 0;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(x, y, z);
    }
}

