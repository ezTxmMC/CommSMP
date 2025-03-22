package de.commsmp.smp.generation;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class CustomChunkGen extends ChunkGenerator {

    @NotNull
    @Override
    public ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int x, int z, @NotNull ChunkGenerator.BiomeGrid biome) {
        ChunkData data = createChunkData(world);
        if (world.getName().toLowerCase().contains("nether")) {
            for (int cx = 0; x < 16; x++) {
                for (int y = 8; y < 22; y++) {
                    for (int cz = 0; z < 16; z++) {
                        if (data.getBlockData(cx, y, cz).getMaterial().equals(Material.ANCIENT_DEBRIS)) {
                            data.setBlock(cx, y, cz, Material.NETHERRACK);
                        }
                    }
                }
            }
            return data;
        }
        return data;
    }
}
