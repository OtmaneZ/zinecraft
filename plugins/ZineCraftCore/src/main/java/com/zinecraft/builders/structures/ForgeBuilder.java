package com.zinecraft.builders.structures;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockTypes;
import com.zinecraft.builders.core.AbstractStructureBuilder;
import org.bukkit.Location;

/**
 * Builder pour générer une forge avec cheminée
 * Structure : 10x8 avec furnaces et décoration lava
 */
public class ForgeBuilder extends AbstractStructureBuilder {

    private final int width;
    private final int depth;
    private final int height;

    public ForgeBuilder(int width, int depth, int height) {
        super("Forge", Math.max(width, depth) / 2);
        this.width = width;
        this.depth = depth;
        this.height = height;
    }

    @Override
    public void build(EditSession session, Location center) {
        int x = center.getBlockX();
        int y = center.getBlockY();
        int z = center.getBlockZ();

        // 1. Sol en pierre
        fillCuboid(session, x, y, z, x + width - 1, y, z + depth - 1, BlockTypes.STONE);

        // 2. Murs en stone bricks
        buildWalls(session, x, y, z);

        // 3. Toit en stone brick stairs
        buildRoof(session, x, y, z);

        // 4. Porte d'entrée
        session.setBlock(BlockVector3.at(x + width/2, y + 1, z), BlockTypes.AIR);
        session.setBlock(BlockVector3.at(x + width/2, y + 2, z), BlockTypes.AIR);

        // 5. Furnaces (intérieur)
        session.setBlock(BlockVector3.at(x + 2, y + 1, z + 2), BlockTypes.FURNACE);
        session.setBlock(BlockVector3.at(x + 2, y + 1, z + depth - 3), BlockTypes.BLAST_FURNACE);
        session.setBlock(BlockVector3.at(x + width - 3, y + 1, z + 2), BlockTypes.BLAST_FURNACE);

        // 6. Anvils
        session.setBlock(BlockVector3.at(x + width/2, y + 1, z + depth/2), BlockTypes.ANVIL);

        // 7. Lava décorative (dans fosse sécurisée)
        session.setBlock(BlockVector3.at(x + width - 3, y, z + depth - 3), BlockTypes.LAVA);
        session.setBlock(BlockVector3.at(x + width - 3, y + 1, z + depth - 3), BlockTypes.IRON_BARS);

        // 8. Cheminée (caractéristique)
        buildChimney(session, x, y, z);
    }

    private void buildWalls(EditSession session, int x, int y, int z) {
        // Murs extérieurs en stone bricks
        for (int dy = 1; dy <= height; dy++) {
            // Murs avant/arrière
            for (int dx = 0; dx < width; dx++) {
                session.setBlock(BlockVector3.at(x + dx, y + dy, z), BlockTypes.STONE_BRICKS);
                session.setBlock(BlockVector3.at(x + dx, y + dy, z + depth - 1), BlockTypes.STONE_BRICKS);
            }
            // Murs latéraux
            for (int dz = 0; dz < depth; dz++) {
                session.setBlock(BlockVector3.at(x, y + dy, z + dz), BlockTypes.STONE_BRICKS);
                session.setBlock(BlockVector3.at(x + width - 1, y + dy, z + dz), BlockTypes.STONE_BRICKS);
            }
        }
    }

    private void buildRoof(EditSession session, int x, int y, int z) {
        int roofY = y + height + 1;
        // Toit plat simple
        fillCuboid(session, x, roofY, z, x + width - 1, roofY, z + depth - 1, BlockTypes.STONE_BRICK_STAIRS);
    }

    private void buildChimney(EditSession session, int x, int y, int z) {
        // Cheminée au coin arrière droit
        int chimneyX = x + width - 2;
        int chimneyZ = z + depth - 2;

        // Hauteur cheminée : 8 blocs au-dessus du toit
        for (int dy = height + 2; dy <= height + 10; dy++) {
            session.setBlock(BlockVector3.at(chimneyX, y + dy, chimneyZ), BlockTypes.STONE_BRICKS);
            session.setBlock(BlockVector3.at(chimneyX + 1, y + dy, chimneyZ), BlockTypes.STONE_BRICKS);
            session.setBlock(BlockVector3.at(chimneyX, y + dy, chimneyZ + 1), BlockTypes.STONE_BRICKS);
            session.setBlock(BlockVector3.at(chimneyX + 1, y + dy, chimneyZ + 1), BlockTypes.STONE_BRICKS);
        }

        // Fumée (campfire au sommet)
        session.setBlock(BlockVector3.at(chimneyX, y + height + 11, chimneyZ), BlockTypes.CAMPFIRE);
    }
}
