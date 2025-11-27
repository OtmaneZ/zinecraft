package com.zinecraft.builders.terrain;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.bukkit.Location;

/**
 * Gère le terraforming et l'aplanissement des zones
 */
public class TerrainBuilder {

    /**
     * Aplanit le terrain dans un rayon donné (async avec FAWE)
     */
    public static void flattenArea(EditSession session, Location center, int radius, int groundLevel) {
        BlockVector3 pos1 = BlockVector3.at(
            center.getBlockX() - radius,
            groundLevel - 5,
            center.getBlockZ() - radius
        );
        BlockVector3 pos2 = BlockVector3.at(
            center.getBlockX() + radius,
            groundLevel + 20,
            center.getBlockZ() + radius
        );

        CuboidRegion region = new CuboidRegion(pos1, pos2);

        // Remplir le sol avec de l'herbe
        for (int x = pos1.getX(); x <= pos2.getX(); x++) {
            for (int z = pos1.getZ(); z <= pos2.getZ(); z++) {
                // Vide au-dessus
                for (int y = groundLevel + 1; y <= pos2.getY(); y++) {
                    session.setBlock(BlockVector3.at(x, y, z), BlockTypes.AIR);
                }
                // Sol en herbe
                session.setBlock(BlockVector3.at(x, groundLevel, z), BlockTypes.GRASS_BLOCK);
                // Sous-sol en terre
                for (int y = groundLevel - 1; y >= pos1.getY(); y--) {
                    session.setBlock(BlockVector3.at(x, y, z), BlockTypes.DIRT);
                }
            }
        }
    }

    /**
     * Crée un chemin entre deux points
     */
    public static void createPath(EditSession session, Location start, Location end, int width) {
        int x1 = start.getBlockX();
        int z1 = start.getBlockZ();
        int x2 = end.getBlockX();
        int z2 = end.getBlockZ();
        int y = start.getBlockY();

        // Chemin horizontal
        if (Math.abs(x2 - x1) > Math.abs(z2 - z1)) {
            for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
                for (int w = -width/2; w <= width/2; w++) {
                    session.setBlock(BlockVector3.at(x, y, z1 + w), BlockTypes.COBBLESTONE);
                }
            }
        } else {
            // Chemin vertical
            for (int z = Math.min(z1, z2); z <= Math.max(z1, z2); z++) {
                for (int w = -width/2; w <= width/2; w++) {
                    session.setBlock(BlockVector3.at(x1 + w, y, z), BlockTypes.COBBLESTONE);
                }
            }
        }
    }
}
