package com.zinecraft.builders.structures;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockTypes;
import com.zinecraft.builders.core.AbstractStructureBuilder;
import org.bukkit.Location;

/**
 * Builder pour créer un spawn point avec beacon et décoration
 * Inclut: Base gold, beacon, panneau bienvenue, coffre starter
 */
public class SpawnPointBuilder extends AbstractStructureBuilder {
    
    public SpawnPointBuilder() {
        super("SpawnPoint", 3);
    }
    
    @Override
    public void build(EditSession session, Location center) {
        int cx = center.getBlockX();
        int cy = center.getBlockY();
        int cz = center.getBlockZ();
        
        // 1. Base beacon 3x3 en gold blocks
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                session.setBlock(BlockVector3.at(cx + x, cy, cz + z), BlockTypes.GOLD_BLOCK);
            }
        }
        
        // 2. Beacon au centre
        session.setBlock(BlockVector3.at(cx, cy + 1, cz), BlockTypes.BEACON);
        
        // 3. Cercle décoratif en glowstone (rayon 2)
        for (int angle = 0; angle < 360; angle += 45) {
            double rad = Math.toRadians(angle);
            int x = cx + (int)(3 * Math.cos(rad));
            int z = cz + (int)(3 * Math.sin(rad));
            session.setBlock(BlockVector3.at(x, cy, z), BlockTypes.GLOWSTONE);
        }
        
        // 4. Panneau bienvenue (au nord)
        session.setBlock(BlockVector3.at(cx, cy + 2, cz - 3), BlockTypes.OAK_SIGN);
        
        // 5. Coffre starter (à l'ouest)
        session.setBlock(BlockVector3.at(cx - 3, cy + 1, cz), BlockTypes.CHEST);
        
        // 6. Petites fleurs décoratives autour
        session.setBlock(BlockVector3.at(cx + 2, cy + 1, cz + 2), BlockTypes.POPPY);
        session.setBlock(BlockVector3.at(cx - 2, cy + 1, cz + 2), BlockTypes.DANDELION);
        session.setBlock(BlockVector3.at(cx + 2, cy + 1, cz - 2), BlockTypes.BLUE_ORCHID);
        session.setBlock(BlockVector3.at(cx - 2, cy + 1, cz - 2), BlockTypes.ALLIUM);
    }
}
