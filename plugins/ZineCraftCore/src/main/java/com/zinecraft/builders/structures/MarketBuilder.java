package com.zinecraft.builders.structures;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import com.zinecraft.builders.core.AbstractStructureBuilder;
import org.bukkit.Location;

/**
 * Builder pour générer un marché avec stands colorés
 */
public class MarketBuilder extends AbstractStructureBuilder {

    private final int standCount;

    public MarketBuilder(int standCount) {
        super("Market", 15);
        this.standCount = standCount;
    }

    @Override
    public void build(EditSession session, Location center) {
        int cx = center.getBlockX();
        int cy = center.getBlockY();
        int cz = center.getBlockZ();

        BlockType[] colors = new BlockType[] {
            BlockTypes.RED_WOOL,
            BlockTypes.BLUE_WOOL,
            BlockTypes.GREEN_WOOL,
            BlockTypes.YELLOW_WOOL,
            BlockTypes.ORANGE_WOOL,
            BlockTypes.PURPLE_WOOL,
            BlockTypes.PINK_WOOL,
            BlockTypes.LIME_WOOL
        };        double angleStep = 2 * Math.PI / standCount;
        int radius = 12;

        for (int i = 0; i < standCount; i++) {
            double angle = i * angleStep;
            int x = cx + (int)(radius * Math.cos(angle));
            int z = cz + (int)(radius * Math.sin(angle));

            // Stand individuel
            buildStand(session, x, cy, z, colors[i % colors.length]);
        }
    }

    private void buildStand(EditSession session, int x, int y, int z, BlockType woolColor) {
        // Comptoir en bois
        fillCuboid(session, x - 1, y + 1, z - 1, x + 1, y + 1, z + 1, BlockTypes.OAK_PLANKS);

        // Poteaux en bois
        for (int dy = 2; dy <= 4; dy++) {
            session.setBlock(BlockVector3.at(x - 1, y + dy, z - 1), BlockTypes.OAK_FENCE);
            session.setBlock(BlockVector3.at(x + 1, y + dy, z - 1), BlockTypes.OAK_FENCE);
            session.setBlock(BlockVector3.at(x - 1, y + dy, z + 1), BlockTypes.OAK_FENCE);
            session.setBlock(BlockVector3.at(x + 1, y + dy, z + 1), BlockTypes.OAK_FENCE);
        }

        // Toit coloré en laine
        fillCuboid(session, x - 2, y + 5, z - 2, x + 2, y + 5, z + 2, woolColor);
    }
}
