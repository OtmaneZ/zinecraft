package com.zinecraft.builders.zones;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

/**
 * Orchestrateur pour la zone Tutorial Island
 * Coords: (0, 500) - Niveau 1-5
 */
public class TutorialZoneBuilder {

    private final Plugin plugin;
    private final World world;
    private final Location center;
    private final int size;

    public TutorialZoneBuilder(Plugin plugin, World world, Location center, int size) {
        this.plugin = plugin;
        this.world = world;
        this.center = center;
        this.size = size;
    }

    /**
     * Génère la Tutorial Island de manière asynchrone
     */
    public void generate() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                var weWorld = BukkitAdapter.adapt(world);

                try (EditSession session = WorldEdit.getInstance()
                        .newEditSessionBuilder()
                        .world(weWorld)
                        .fastMode(true)
                        .limitUnlimited()
                        .build()) {

                    plugin.getLogger().info("[TutorialIsland] Création de l'île...");

                    // 1. Île circulaire surélevée (Y=70)
                    buildIsland(session);

                    // 2. Spawn point avec beacon
                    buildSpawnPoint(session);

                    // 3. Arène tutoriel combat
                    buildCombatArena(session);

                    // 4. PNJ tutoriels (armor stands)
                    buildNPCStands(session);

                    // 5. Portail vers village
                    buildPortal(session);

                    session.flushQueue();

                    plugin.getLogger().info("[TutorialIsland] Île générée avec succès!");
                }

            } catch (Exception e) {
                plugin.getLogger().severe("[TutorialIsland] Erreur: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void buildIsland(EditSession session) {
        int cx = center.getBlockX();
        int cy = center.getBlockY() + 10; // Surélévation Y=70
        int cz = center.getBlockZ();
        int radius = size / 2;

        // Île circulaire en grass
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                double distance = Math.sqrt(x * x + z * z);
                if (distance <= radius) {
                    // Surface en grass
                    session.setBlock(BlockVector3.at(cx + x, cy, cz + z), BlockTypes.GRASS_BLOCK);

                    // Sous-sol en dirt (5 couches)
                    for (int dy = -5; dy < 0; dy++) {
                        session.setBlock(BlockVector3.at(cx + x, cy + dy, cz + z), BlockTypes.DIRT);
                    }

                    // Base en stone (3 couches)
                    for (int dy = -8; dy < -5; dy++) {
                        session.setBlock(BlockVector3.at(cx + x, cy + dy, cz + z), BlockTypes.STONE);
                    }
                }
            }
        }
    }

    private void buildSpawnPoint(EditSession session) {
        int cx = center.getBlockX();
        int cy = center.getBlockY() + 10;
        int cz = center.getBlockZ();

        // Beacon coloré
        session.setBlock(BlockVector3.at(cx, cy + 1, cz), BlockTypes.BEACON);

        // Base beacon (3x3 gold blocks)
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                session.setBlock(BlockVector3.at(cx + x, cy, cz + z), BlockTypes.GOLD_BLOCK);
            }
        }

        // Panneau bienvenue
        session.setBlock(BlockVector3.at(cx + 3, cy + 2, cz), BlockTypes.OAK_SIGN);

        // Coffre starter
        session.setBlock(BlockVector3.at(cx - 3, cy + 1, cz), BlockTypes.CHEST);
    }

    private void buildCombatArena(EditSession session) {
        int cx = center.getBlockX() + 20;
        int cy = center.getBlockY() + 10;
        int cz = center.getBlockZ();

        // Arène 20x20 en stone bricks
        for (int x = -10; x <= 10; x++) {
            for (int z = -10; z <= 10; z++) {
                session.setBlock(BlockVector3.at(cx + x, cy, cz + z), BlockTypes.STONE_BRICKS);
            }
        }

        // Bordure arena
        for (int x = -10; x <= 10; x++) {
            session.setBlock(BlockVector3.at(cx + x, cy + 1, cz - 10), BlockTypes.COBBLESTONE_WALL);
            session.setBlock(BlockVector3.at(cx + x, cy + 1, cz + 10), BlockTypes.COBBLESTONE_WALL);
        }
        for (int z = -10; z <= 10; z++) {
            session.setBlock(BlockVector3.at(cx - 10, cy + 1, cz + z), BlockTypes.COBBLESTONE_WALL);
            session.setBlock(BlockVector3.at(cx + 10, cy + 1, cz + z), BlockTypes.COBBLESTONE_WALL);
        }

        // Spawner zombies (faibles)
        session.setBlock(BlockVector3.at(cx, cy + 1, cz), BlockTypes.SPAWNER);

        // Panneau instructions
        session.setBlock(BlockVector3.at(cx, cy + 2, cz - 15), BlockTypes.OAK_SIGN);
    }

    private void buildNPCStands(EditSession session) {
        int cx = center.getBlockX();
        int cy = center.getBlockY() + 10;
        int cz = center.getBlockZ();

        // PNJ Guerrier (à placer via commande in-game)
        // Position: -15, cy+1, 0
        session.setBlock(BlockVector3.at(cx - 15, cy, cz), BlockTypes.STONE_BRICKS);

        // PNJ Mage
        // Position: 0, cy+1, -15
        session.setBlock(BlockVector3.at(cx, cy, cz - 15), BlockTypes.STONE_BRICKS);

        // PNJ Marchand
        // Position: 15, cy+1, 0
        session.setBlock(BlockVector3.at(cx + 15, cy, cz), BlockTypes.STONE_BRICKS);
    }

    private void buildPortal(EditSession session) {
        int cx = center.getBlockX();
        int cy = center.getBlockY() + 10;
        int cz = center.getBlockZ() + 30;

        // Portail décoratif (obsidienne)
        // Cadre 4x5
        for (int y = 0; y <= 4; y++) {
            session.setBlock(BlockVector3.at(cx - 2, cy + y, cz), BlockTypes.OBSIDIAN);
            session.setBlock(BlockVector3.at(cx + 2, cy + y, cz), BlockTypes.OBSIDIAN);
        }
        for (int x = -2; x <= 2; x++) {
            session.setBlock(BlockVector3.at(cx + x, cy, cz), BlockTypes.OBSIDIAN);
            session.setBlock(BlockVector3.at(cx + x, cy + 4, cz), BlockTypes.OBSIDIAN);
        }

        // Intérieur portal (purple stained glass)
        for (int y = 1; y <= 3; y++) {
            for (int x = -1; x <= 1; x++) {
                session.setBlock(BlockVector3.at(cx + x, cy + y, cz), BlockTypes.PURPLE_STAINED_GLASS);
            }
        }

        // Panneau "Prêt?"
        session.setBlock(BlockVector3.at(cx, cy + 2, cz - 2), BlockTypes.OAK_SIGN);
    }
}
