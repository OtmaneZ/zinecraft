package com.zinecraft.builders.structures;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockTypes;
import com.zinecraft.builders.core.AbstractStructureBuilder;
import org.bukkit.Location;

/**
 * Builder pour créer un portail décoratif vers le village
 * Style: Cadre obsidienne + intérieur purple glass
 */
public class PortalBuilder extends AbstractStructureBuilder {
    
    private final int width;
    private final int height;
    
    public PortalBuilder(int width, int height) {
        super("Portal", Math.max(width, height) / 2);
        this.width = width;
        this.height = height;
    }
    
    @Override
    public void build(EditSession session, Location center) {
        int cx = center.getBlockX();
        int cy = center.getBlockY();
        int cz = center.getBlockZ();
        
        // 1. Cadre en obsidienne (4x5)
        int halfWidth = width / 2;
        
        // Colonnes latérales
        for (int y = 0; y <= height; y++) {
            session.setBlock(BlockVector3.at(cx - halfWidth, cy + y, cz), BlockTypes.OBSIDIAN);
            session.setBlock(BlockVector3.at(cx + halfWidth, cy + y, cz), BlockTypes.OBSIDIAN);
        }
        
        // Haut et bas
        for (int x = -halfWidth; x <= halfWidth; x++) {
            session.setBlock(BlockVector3.at(cx + x, cy, cz), BlockTypes.OBSIDIAN);
            session.setBlock(BlockVector3.at(cx + x, cy + height, cz), BlockTypes.OBSIDIAN);
        }
        
        // 2. Intérieur en purple stained glass
        for (int y = 1; y < height; y++) {
            for (int x = -halfWidth + 1; x < halfWidth; x++) {
                session.setBlock(BlockVector3.at(cx + x, cy + y, cz), BlockTypes.PURPLE_STAINED_GLASS);
            }
        }
        
        // 3. Glowstone pour éclairage en haut
        session.setBlock(BlockVector3.at(cx, cy + height + 1, cz), BlockTypes.GLOWSTONE);
        
        // 4. Panneau devant
        session.setBlock(BlockVector3.at(cx, cy + 2, cz - 2), BlockTypes.OAK_SIGN);
    }
}
