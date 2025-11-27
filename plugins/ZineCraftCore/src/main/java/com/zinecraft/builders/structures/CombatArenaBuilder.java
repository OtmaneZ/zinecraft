package com.zinecraft.builders.structures;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockTypes;
import com.zinecraft.builders.core.AbstractStructureBuilder;
import org.bukkit.Location;

/**
 * Builder pour créer une arène de combat tutoriel
 * Taille: 20x20 avec bordure et spawner central
 */
public class CombatArenaBuilder extends AbstractStructureBuilder {

    private final int size;

    public CombatArenaBuilder(int size) {
        super("CombatArena", size / 2);
        this.size = size;
    }

    @Override
    public void build(EditSession session, Location center) {
        int cx = center.getBlockX();
        int cy = center.getBlockY();
        int cz = center.getBlockZ();
        int halfSize = size / 2;

        // 1. Sol de l'arène en stone bricks
        for (int x = -halfSize; x <= halfSize; x++) {
            for (int z = -halfSize; z <= halfSize; z++) {
                session.setBlock(BlockVector3.at(cx + x, cy, cz + z), BlockTypes.STONE_BRICKS);
            }
        }

        // 2. Bordure en cobblestone wall
        for (int x = -halfSize; x <= halfSize; x++) {
            session.setBlock(BlockVector3.at(cx + x, cy + 1, cz - halfSize), BlockTypes.COBBLESTONE_WALL);
            session.setBlock(BlockVector3.at(cx + x, cy + 1, cz + halfSize), BlockTypes.COBBLESTONE_WALL);
        }
        for (int z = -halfSize; z <= halfSize; z++) {
            session.setBlock(BlockVector3.at(cx - halfSize, cy + 1, cz + z), BlockTypes.COBBLESTONE_WALL);
            session.setBlock(BlockVector3.at(cx + halfSize, cy + 1, cz + z), BlockTypes.COBBLESTONE_WALL);
        }

        // 3. Spawner central (zombies faibles)
        session.setBlock(BlockVector3.at(cx, cy + 1, cz), BlockTypes.SPAWNER);

        // 4. Torches aux 4 coins
        session.setBlock(BlockVector3.at(cx - halfSize + 1, cy + 2, cz - halfSize + 1), BlockTypes.TORCH);
        session.setBlock(BlockVector3.at(cx + halfSize - 1, cy + 2, cz - halfSize + 1), BlockTypes.TORCH);
        session.setBlock(BlockVector3.at(cx - halfSize + 1, cy + 2, cz + halfSize - 1), BlockTypes.TORCH);
        session.setBlock(BlockVector3.at(cx + halfSize - 1, cy + 2, cz + halfSize - 1), BlockTypes.TORCH);

        // 5. Panneau d'instructions
        session.setBlock(BlockVector3.at(cx, cy + 2, cz - halfSize - 3), BlockTypes.OAK_SIGN);
    }
}
