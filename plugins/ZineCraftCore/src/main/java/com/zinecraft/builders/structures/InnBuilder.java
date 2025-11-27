package com.zinecraft.builders.structures;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockTypes;
import com.zinecraft.builders.core.AbstractStructureBuilder;
import org.bukkit.Location;

/**
 * Builder pour générer une auberge/taverne à 2 étages
 * Structure : 12x10 avec tables, bar, et chambres
 */
public class InnBuilder extends AbstractStructureBuilder {

    private final int width;
    private final int depth;

    public InnBuilder(int width, int depth) {
        super("Inn", Math.max(width, depth) / 2);
        this.width = width;
        this.depth = depth;
    }

    @Override
    public void build(EditSession session, Location center) {
        int x = center.getBlockX();
        int y = center.getBlockY();
        int z = center.getBlockZ();

        // 1. Fondations en stone
        fillCuboid(session, x, y, z, x + width - 1, y, z + depth - 1, BlockTypes.STONE);

        // 2. Étage 1 (Rez-de-chaussée - Taverne)
        buildFloor1(session, x, y, z);

        // 3. Étage 2 (Chambres)
        buildFloor2(session, x, y + 5, z);

        // 4. Toit
        buildRoof(session, x, y + 10, z);

        // 5. Enseigne extérieure
        session.setBlock(BlockVector3.at(x + width/2, y + 4, z - 1), BlockTypes.OAK_SIGN);
    }

    private void buildFloor1(EditSession session, int x, int y, int z) {
        // Murs étage 1 (hauteur 4 blocs)
        for (int dy = 1; dy <= 4; dy++) {
            for (int dx = 0; dx < width; dx++) {
                session.setBlock(BlockVector3.at(x + dx, y + dy, z), BlockTypes.OAK_PLANKS);
                session.setBlock(BlockVector3.at(x + dx, y + dy, z + depth - 1), BlockTypes.OAK_PLANKS);
            }
            for (int dz = 0; dz < depth; dz++) {
                session.setBlock(BlockVector3.at(x, y + dy, z + dz), BlockTypes.OAK_PLANKS);
                session.setBlock(BlockVector3.at(x + width - 1, y + dy, z + dz), BlockTypes.OAK_PLANKS);
            }
        }

        // Porte d'entrée
        session.setBlock(BlockVector3.at(x + width/2, y + 1, z), BlockTypes.OAK_DOOR);
        session.setBlock(BlockVector3.at(x + width/2, y + 2, z), BlockTypes.OAK_DOOR);

        // Fenêtres
        session.setBlock(BlockVector3.at(x + 2, y + 2, z), BlockTypes.GLASS_PANE);
        session.setBlock(BlockVector3.at(x + width - 3, y + 2, z), BlockTypes.GLASS_PANE);
        session.setBlock(BlockVector3.at(x, y + 2, z + depth/2), BlockTypes.GLASS_PANE);
        session.setBlock(BlockVector3.at(x + width - 1, y + 2, z + depth/2), BlockTypes.GLASS_PANE);

        // Sol taverne en oak planks
        fillCuboid(session, x + 1, y + 1, z + 1, x + width - 2, y + 1, z + depth - 2, BlockTypes.OAK_PLANKS);

        // Bar (comptoir)
        fillCuboid(session, x + 2, y + 2, z + depth - 3, x + 5, y + 2, z + depth - 3, BlockTypes.DARK_OAK_PLANKS);

        // Tables (crafting tables comme déco)
        session.setBlock(BlockVector3.at(x + 3, y + 2, z + 3), BlockTypes.CRAFTING_TABLE);
        session.setBlock(BlockVector3.at(x + width - 4, y + 2, z + 3), BlockTypes.CRAFTING_TABLE);
        session.setBlock(BlockVector3.at(x + 3, y + 2, z + depth - 4), BlockTypes.CRAFTING_TABLE);

        // Chaises (stairs)
        session.setBlock(BlockVector3.at(x + 3, y + 2, z + 2), BlockTypes.OAK_STAIRS);
        session.setBlock(BlockVector3.at(x + width - 4, y + 2, z + 2), BlockTypes.OAK_STAIRS);

        // Cheminée (chaleur)
        session.setBlock(BlockVector3.at(x + width - 2, y + 2, z + 2), BlockTypes.CAMPFIRE);
    }

    private void buildFloor2(EditSession session, int x, int y, int z) {
        // Plancher étage 2
        fillCuboid(session, x, y, z, x + width - 1, y, z + depth - 1, BlockTypes.OAK_PLANKS);

        // Murs étage 2 (hauteur 4 blocs)
        for (int dy = 1; dy <= 4; dy++) {
            for (int dx = 0; dx < width; dx++) {
                session.setBlock(BlockVector3.at(x + dx, y + dy, z), BlockTypes.OAK_PLANKS);
                session.setBlock(BlockVector3.at(x + dx, y + dy, z + depth - 1), BlockTypes.OAK_PLANKS);
            }
            for (int dz = 0; dz < depth; dz++) {
                session.setBlock(BlockVector3.at(x, y + dy, z + dz), BlockTypes.OAK_PLANKS);
                session.setBlock(BlockVector3.at(x + width - 1, y + dy, z + dz), BlockTypes.OAK_PLANKS);
            }
        }

        // Fenêtres étage 2
        session.setBlock(BlockVector3.at(x + 2, y + 2, z), BlockTypes.GLASS_PANE);
        session.setBlock(BlockVector3.at(x + width - 3, y + 2, z), BlockTypes.GLASS_PANE);

        // Chambres (3 chambres avec lits)
        // Chambre 1
        session.setBlock(BlockVector3.at(x + 2, y + 1, z + 2), BlockTypes.RED_BED);

        // Chambre 2
        session.setBlock(BlockVector3.at(x + width - 3, y + 1, z + 2), BlockTypes.BLUE_BED);

        // Chambre 3
        session.setBlock(BlockVector3.at(x + 2, y + 1, z + depth - 3), BlockTypes.GREEN_BED);
    }

    private void buildRoof(EditSession session, int x, int y, int z) {
        // Toit en pente (oak stairs)
        int halfWidth = width / 2;
        for (int i = 0; i <= halfWidth; i++) {
            for (int dz = 0; dz < depth; dz++) {
                session.setBlock(BlockVector3.at(x + i, y + i, z + dz), BlockTypes.OAK_STAIRS);
                session.setBlock(BlockVector3.at(x + width - 1 - i, y + i, z + dz), BlockTypes.OAK_STAIRS);
            }
        }
    }
}
