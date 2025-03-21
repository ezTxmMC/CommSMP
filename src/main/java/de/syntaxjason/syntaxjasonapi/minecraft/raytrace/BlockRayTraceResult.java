package de.syntaxjason.syntaxjasonapi.minecraft.raytrace;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class BlockRayTraceResult extends RayTraceResult {
    private final Block block;

    public BlockRayTraceResult(Location hitLocation, Block block) {
        super(hitLocation);
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }
}
