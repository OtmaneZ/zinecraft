package com.zinecraft.builders.structures;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockTypes;
import com.zinecraft.builders.core.AbstractStructureBuilder;
import org.bukkit.Location;

/**
 * Builder pour générer des maisons de styles variés
 */
public class HouseBuilder extends AbstractStructureBuilder {
    
    public enum HouseStyle {
        WOOD,
        STONE,
        BRICK
    }
    
    private final HouseStyle style;
    private final int width;
    private final int depth;
    private final int height;
    
    public HouseBuilder(HouseStyle style, int width, int depth, int height) {
        super("House_" + style.name(), Math.max(width, depth) / 2);
        this.style = style;
        this.width = width;
        this.depth = depth;
        this.height = height;
    }
    
    @Override
    public void build(EditSession session, Location center) {
        int x = center.getBlockX();
        int y = center.getBlockY();
        int z = center.getBlockZ();
        
        // Choix des matériaux selon le style
        var wallMaterial = switch (style) {
            case WOOD -> BlockTypes.OAK_PLANKS;
            case STONE -> BlockTypes.STONE_BRICKS;
            case BRICK -> BlockTypes.BRICKS;
        };
        
        var roofMaterial = switch (style) {
            case WOOD -> BlockTypes.OAK_STAIRS;
            case STONE -> BlockTypes.STONE_BRICK_STAIRS;
            case BRICK -> BlockTypes.BRICK_STAIRS;
        };
        
        // 1. Sol
        fillCuboid(session, x, y, z, x + width - 1, y, z + depth - 1, BlockTypes.OAK_PLANKS);
        
        // 2. Murs (creux à l'intérieur)
        buildWalls(session, x, y, z, wallMaterial);
        
        // 3. Porte
        session.setBlock(BlockVector3.at(x + width/2, y + 1, z), BlockTypes.OAK_DOOR);
        session.setBlock(BlockVector3.at(x + width/2, y + 2, z), BlockTypes.OAK_DOOR);
        
        // 4. Fenêtres
        buildWindows(session, x, y, z);
        
        // 5. Toit en pente
        buildRoof(session, x, y, z, roofMaterial);
    }
    
    private void buildWalls(EditSession session, int x, int y, int z, var material) {
        // Murs extérieurs
        for (int dy = 1; dy <= height; dy++) {
            // Mur avant et arrière
            for (int dx = 0; dx < width; dx++) {
                session.setBlock(BlockVector3.at(x + dx, y + dy, z), material);
                session.setBlock(BlockVector3.at(x + dx, y + dy, z + depth - 1), material);
            }
            // Murs latéraux
            for (int dz = 0; dz < depth; dz++) {
                session.setBlock(BlockVector3.at(x, y + dy, z + dz), material);
                session.setBlock(BlockVector3.at(x + width - 1, y + dy, z + dz), material);
            }
        }
    }
    
    private void buildWindows(EditSession session, int x, int y, int z) {
        // Fenêtres latérales
        session.setBlock(BlockVector3.at(x + 2, y + 2, z), BlockTypes.GLASS_PANE);
        session.setBlock(BlockVector3.at(x + width - 3, y + 2, z), BlockTypes.GLASS_PANE);
        session.setBlock(BlockVector3.at(x, y + 2, z + depth/2), BlockTypes.GLASS_PANE);
        session.setBlock(BlockVector3.at(x + width - 1, y + 2, z + depth/2), BlockTypes.GLASS_PANE);
    }
    
    private void buildRoof(EditSession session, int x, int y, int z, var roofMaterial) {
        int roofY = y + height + 1;
        int halfWidth = width / 2;
        
        // Toit en pente simple
        for (int i = 0; i <= halfWidth; i++) {
            for (int dz = 0; dz < depth; dz++) {
                session.setBlock(BlockVector3.at(x + i, roofY + i, z + dz), roofMaterial);
                session.setBlock(BlockVector3.at(x + width - 1 - i, roofY + i, z + dz), roofMaterial);
            }
        }
    }
}
