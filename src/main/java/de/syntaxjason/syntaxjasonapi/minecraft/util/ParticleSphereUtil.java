package de.syntaxjason.syntaxjasonapi.minecraft.util;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class ParticleSphereUtil {

    /**
     * Erzeugt eine Liste von Locations, die eine Kugel (oder hohle Kugel) bilden.
     *
     * @param center Der Mittelpunkt.
     * @param radius Der Kugelradius.
     * @param hollow Ist true, wenn nur die Oberfläche (hohle Kugel) erzeugt werden soll.
     * @return Eine Liste von Locations.
     */
    public static List<Location> generateSphere(Location center, double radius, boolean hollow) {
        List<Location> points = new ArrayList<>();
        int precision = 10;
        double rSquared = radius * radius;
        for (int x = -precision; x <= precision; x++) {
            for (int y = -precision; y <= precision; y++) {
                for (int z = -precision; z <= precision; z++) {
                    double dx = (x / (double) precision) * radius;
                    double dy = (y / (double) precision) * radius;
                    double dz = (z / (double) precision) * radius;
                    double distSq = dx * dx + dy * dy + dz * dz;
                    if (hollow) {
                        if (Math.abs(distSq - rSquared) < radius * 0.5) {
                            points.add(center.clone().add(dx, dy, dz));
                        }
                    } else {
                        if (distSq <= rSquared) {
                            points.add(center.clone().add(dx, dy, dz));
                        }
                    }
                }
            }
        }
        return points;
    }
}
