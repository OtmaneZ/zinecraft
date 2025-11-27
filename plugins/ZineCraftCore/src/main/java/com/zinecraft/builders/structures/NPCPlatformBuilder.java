package com.zinecraft.builders.structures;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockTypes;
import com.zinecraft.builders.core.AbstractStructureBuilder;
import org.bukkit.Location;

/**
 * Builder pour créer une plateforme pour NPC (armor stand)
 * Plateforme 5x5 en stone bricks avec torches et pancarte
 */
public class NPCPlatformBuilder extends AbstractStructureBuilder {

    private String npcType; // GUERRIER, MAGE, MARCHAND

    public NPCPlatformBuilder(String npcType) {
        super("NPCPlatform_" + npcType, 3);
        this.npcType = npcType;
    }

    @Override
    public void build(EditSession session, Location center) {
        int cx = center.getBlockX();
        int cy = center.getBlockY();
        int cz = center.getBlockZ();

        // 1. Plateforme 5x5 en stone bricks
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                session.setBlock(BlockVector3.at(cx + x, cy, cz + z), BlockTypes.STONE_BRICKS);
            }
        }

        // 2. Bords en chiseled stone bricks
        for (int x = -2; x <= 2; x++) {
            session.setBlock(BlockVector3.at(cx + x, cy, cz - 2), BlockTypes.CHISELED_STONE_BRICKS);
            session.setBlock(BlockVector3.at(cx + x, cy, cz + 2), BlockTypes.CHISELED_STONE_BRICKS);
        }
        for (int z = -1; z <= 1; z++) {
            session.setBlock(BlockVector3.at(cx - 2, cy, cz + z), BlockTypes.CHISELED_STONE_BRICKS);
            session.setBlock(BlockVector3.at(cx + 2, cy, cz + z), BlockTypes.CHISELED_STONE_BRICKS);
        }

        // 3. Torches aux 4 coins
        session.setBlock(BlockVector3.at(cx - 2, cy + 1, cz - 2), BlockTypes.TORCH);
        session.setBlock(BlockVector3.at(cx + 2, cy + 1, cz - 2), BlockTypes.TORCH);
        session.setBlock(BlockVector3.at(cx - 2, cy + 1, cz + 2), BlockTypes.TORCH);
        session.setBlock(BlockVector3.at(cx + 2, cy + 1, cz + 2), BlockTypes.TORCH);

        // 4. Panneau devant (au sud)
        session.setBlock(BlockVector3.at(cx, cy + 1, cz + 3), BlockTypes.OAK_SIGN);

        // 5. Décoration spécifique selon le type NPC
        addTypeSpecificDecoration(session, cx, cy, cz);
    }

    private void addTypeSpecificDecoration(EditSession session, int cx, int cy, int cz) {
        switch (npcType.toUpperCase()) {
            case "GUERRIER":
                // Anvil derrière pour symboliser craft d'armes
                session.setBlock(BlockVector3.at(cx, cy + 1, cz - 3), BlockTypes.ANVIL);
                // Item frame pour épée (blocks seulement)
                session.setBlock(BlockVector3.at(cx - 1, cy + 2, cz), BlockTypes.STONE_BRICKS);
                break;

            case "MAGE":
                // Enchanting table derrière
                session.setBlock(BlockVector3.at(cx, cy + 1, cz - 3), BlockTypes.ENCHANTING_TABLE);
                // Bookshelves autour
                session.setBlock(BlockVector3.at(cx - 1, cy + 1, cz - 1), BlockTypes.BOOKSHELF);
                session.setBlock(BlockVector3.at(cx + 1, cy + 1, cz - 1), BlockTypes.BOOKSHELF);
                break;

            case "MARCHAND":
                // Barrel derrière pour commerce
                session.setBlock(BlockVector3.at(cx, cy + 1, cz - 3), BlockTypes.BARREL);
                // Emerald block pour décor
                session.setBlock(BlockVector3.at(cx - 1, cy, cz), BlockTypes.EMERALD_BLOCK);
                session.setBlock(BlockVector3.at(cx + 1, cy, cz), BlockTypes.EMERALD_BLOCK);
                break;
        }
    }
}
