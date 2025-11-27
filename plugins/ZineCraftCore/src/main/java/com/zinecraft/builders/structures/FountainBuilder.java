package com.zinecraft.builders.structures;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockTypes;
import com.zinecraft.builders.core.AbstractStructureBuilder;
import org.bukkit.Location;

/**
 * Builder pour générer une fontaine décorative
 */
public class FountainBuilder extends AbstractStructureBuilder {

    private final int radius;

    public FountainBuilder(int radius) {
        super("Fountain", radius + 2);
        this.radius = radius;
    }

    @Override
    public void build(EditSession session, Location center) {
        int cx = center.getBlockX();
        int cy = center.getBlockY();
        int cz = center.getBlockZ();

        // 1. Bassin circulaire en quartz
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                double distance = Math.sqrt(x * x + z * z);
                if (distance <= radius) {
                    // Bord du bassin
                    if (distance > radius - 1) {
                        session.setBlock(BlockVector3.at(cx + x, cy, cz + z), BlockTypes.QUARTZ_BLOCK);
                        session.setBlock(BlockVector3.at(cx + x, cy + 1, cz + z), BlockTypes.QUARTZ_BLOCK);
                    } else {
                        // Eau à l'intérieur
                        session.setBlock(BlockVector3.at(cx + x, cy, cz + z), BlockTypes.QUARTZ_BLOCK);
                        session.setBlock(BlockVector3.at(cx + x, cy + 1, cz + z), BlockTypes.WATER);
                    }
                }
            }
        }

        // 2. Pilier central en quartz
        for (int dy = 0; dy <= 3; dy++) {
            session.setBlock(BlockVector3.at(cx, cy + dy, cz), BlockTypes.QUARTZ_PILLAR);
        }

        // 3. Lanterne au sommet
        session.setBlock(BlockVector3.at(cx, cy + 4, cz), BlockTypes.SEA_LANTERN);

        // 4. Petits jets d'eau (optionnel)
        session.setBlock(BlockVector3.at(cx, cy + 2, cz), BlockTypes.WATER);
    }
}
