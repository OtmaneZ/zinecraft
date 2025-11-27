package com.zinecraft.builders.core;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.bukkit.Location;

/**
 * Classe abstraite facilitant l'implémentation des builders
 */
public abstract class AbstractStructureBuilder implements StructureBuilder {
    
    protected final String name;
    protected final int radius;
    
    protected AbstractStructureBuilder(String name, int radius) {
        this.name = name;
        this.radius = radius;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public int getRadius() {
        return radius;
    }
    
    /**
     * Place un bloc via FAWE EditSession (async)
     */
    protected void setBlock(EditSession session, int x, int y, int z, BlockType type) {
        session.setBlock(BlockVector3.at(x, y, z), type);
    }
    
    /**
     * Remplit une zone cubique
     */
    protected void fillCuboid(EditSession session, int x1, int y1, int z1, int x2, int y2, int z2, BlockType type) {
        for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
            for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
                for (int z = Math.min(z1, z2); z <= Math.max(z1, z2); z++) {
                    setBlock(session, x, y, z, type);
                }
            }
        }
    }
    
    /**
     * Convertit Location Bukkit vers coordonnées relatives au centre
     */
    protected BlockVector3 toBlockVector(Location center, int offsetX, int offsetY, int offsetZ) {
        return BlockVector3.at(
            center.getBlockX() + offsetX,
            center.getBlockY() + offsetY,
            center.getBlockZ() + offsetZ
        );
    }
}
