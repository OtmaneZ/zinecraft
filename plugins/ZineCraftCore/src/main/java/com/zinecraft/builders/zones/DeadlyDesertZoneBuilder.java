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
 * Générateur de la zone Désert Mortel
 * Coordonnées: -500, 65, 500
 * Niveau: 30-40
 * Taille: 300x300 blocs
 */
public class DeadlyDesertZoneBuilder {

    private final Plugin plugin;
    private final World world;
    private final Location center;
    private final int radius;

    public DeadlyDesertZoneBuilder(Plugin plugin, World world, Location center, int radius) {
        this.plugin = plugin;
        this.world = world;
        this.center = center;
        this.radius = radius;
    }

    /**
     * Génère la zone complète du désert
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

                    plugin.getLogger().info("[DeadlyDesert] Génération du désert mortel...");

                    // 1. Terraforming - Sol désertique
                    plugin.getLogger().info("[DeadlyDesert] Création du terrain désertique...");
                    createDesertTerrain(session);

                    // 2. Cratères de météorites
                    plugin.getLogger().info("[DeadlyDesert] Création des cratères...");
                    createMeteorCraters(session);

                    // 3. Pyramide massive (structure principale)
                    plugin.getLogger().info("[DeadlyDesert] Construction de la pyramide...");
                    buildPyramid(session);

                    // 4. Village abandonné
                    plugin.getLogger().info("[DeadlyDesert] Village abandonné...");
                    buildAbandonedVillage(session);

                    // 5. Oasis (point de repos)
                    plugin.getLogger().info("[DeadlyDesert] Création de l'oasis...");
                    buildOasis(session);

                    // 6. Décoration (cactus, dead bushes)
                    plugin.getLogger().info("[DeadlyDesert] Décoration du désert...");
                    addDesertDecoration(session);

                    session.flushQueue();

                    plugin.getLogger().info("[DeadlyDesert] ✔ Génération terminée!");
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        plugin.getLogger().info("[DeadlyDesert] Coordonnées: " + 
                            center.getBlockX() + ", " + center.getBlockY() + ", " + center.getBlockZ());
                    });

                } catch (Exception e) {
                    plugin.getLogger().severe("[DeadlyDesert] Erreur lors de la génération: " + e.getMessage());
                    e.printStackTrace();
                }
            } catch (Exception e) {
                plugin.getLogger().severe("[DeadlyDesert] Erreur critique: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * Créer le terrain désertique de base
     */
    private void createDesertTerrain(EditSession session) {
        int cx = center.getBlockX();
        int cy = center.getBlockY();
        int cz = center.getBlockZ();

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                // Sol varié (sand + red sand)
                boolean useRedSand = (x + z) % 3 == 0;
                var sandType = useRedSand ? BlockTypes.RED_SAND : BlockTypes.SAND;

                // 3 couches de sable
                for (int y = 0; y < 3; y++) {
                    session.setBlock(BlockVector3.at(cx + x, cy + y, cz + z), sandType);
                }

                // Sandstone en dessous
                for (int y = -5; y < 0; y++) {
                    session.setBlock(BlockVector3.at(cx + x, cy + y, cz + z), BlockTypes.SANDSTONE);
                }

                // Variations de hauteur (dunes)
                if ((x * x + z * z) % 30 < 10) {
                    session.setBlock(BlockVector3.at(cx + x, cy + 3, cz + z), sandType);
                }
                if ((x * x + z * z) % 50 < 5) {
                    session.setBlock(BlockVector3.at(cx + x, cy + 4, cz + z), sandType);
                }
            }
        }
    }

    /**
     * Créer les cratères de météorites
     */
    private void createMeteorCraters(EditSession session) {
        int cx = center.getBlockX();
        int cy = center.getBlockY();
        int cz = center.getBlockZ();

        // 6 cratères dispersés
        int[][] craters = {
            {30, 20},
            {-30, -20},
            {50, -40},
            {-40, 50},
            {70, 10},
            {-20, -60}
        };

        for (int[] crater : craters) {
            int craterX = cx + crater[0];
            int craterZ = cz + crater[1];
            int craterRadius = 12;

            // Creuser le cratère
            for (int x = -craterRadius; x <= craterRadius; x++) {
                for (int z = -craterRadius; z <= craterRadius; z++) {
                    double distance = Math.sqrt(x * x + z * z);
                    if (distance <= craterRadius) {
                        int depth = (int) (5 - (distance / craterRadius) * 5);
                        for (int y = 0; y < depth; y++) {
                            session.setBlock(BlockVector3.at(craterX + x, cy - y, craterZ + z), BlockTypes.AIR);
                        }

                        // Obsidienne au centre
                        if (distance < 3) {
                            session.setBlock(BlockVector3.at(craterX + x, cy - depth, craterZ + z), BlockTypes.OBSIDIAN);
                        }
                        // Netherrack au milieu
                        else if (distance < 6) {
                            session.setBlock(BlockVector3.at(craterX + x, cy - depth, craterZ + z), BlockTypes.NETHERRACK);
                        }

                        // Minerais rares aléatoires
                        if (distance < 4 && (x + z) % 5 == 0) {
                            session.setBlock(BlockVector3.at(craterX + x, cy - depth + 1, craterZ + z), BlockTypes.DIAMOND_ORE);
                        }
                    }
                }
            }
        }
    }

    /**
     * Construire la pyramide massive
     */
    private void buildPyramid(EditSession session) {
        int cx = center.getBlockX();
        int cy = center.getBlockY();
        int cz = center.getBlockZ();

        int baseSize = 50;
        int height = 40;

        // Construction de la pyramide par couches
        for (int y = 0; y < height; y++) {
            int layerSize = baseSize - (y * baseSize / height);

            for (int x = -layerSize / 2; x <= layerSize / 2; x++) {
                for (int z = -layerSize / 2; z <= layerSize / 2; z++) {
                    // Murs extérieurs uniquement
                    if (Math.abs(x) == layerSize / 2 || Math.abs(z) == layerSize / 2) {
                        session.setBlock(BlockVector3.at(cx + x, cy + y, cz + z), BlockTypes.SMOOTH_SANDSTONE);
                    }
                }
            }
        }

        // Sphinx à l'entrée (sud)
        buildSphinx(session, cx, cy, cz - 30);

        // Obélisques aux 4 coins
        buildObelisk(session, cx - 30, cy, cz - 30);
        buildObelisk(session, cx + 30, cy, cz - 30);
        buildObelisk(session, cx - 30, cy, cz + 30);
        buildObelisk(session, cx + 30, cy, cz + 30);

        // Entrée de la pyramide
        for (int x = -3; x <= 3; x++) {
            for (int y = 0; y < 8; y++) {
                session.setBlock(BlockVector3.at(cx + x, cy + y, cz - 25), BlockTypes.AIR);
            }
        }

        // Intérieur - Sous-sol (Catacombes)
        buildPyramidBasement(session, cx, cy, cz);

        // Intérieur - Rez-de-chaussée
        buildPyramidMainFloor(session, cx, cy, cz);

        // Intérieur - Chambre au trésor
        buildPyramidTreasure(session, cx, cy, cz);

        // Sommet - Plateforme boss
        buildPyramidBossRoom(session, cx, cy + height, cz);
    }

    /**
     * Construire le sphinx
     */
    private void buildSphinx(EditSession session, int x, int y, int z) {
        // Corps du sphinx (simplifié)
        for (int dx = -8; dx <= 8; dx++) {
            for (int dz = -4; dz <= 4; dz++) {
                for (int dy = 0; dy < 6; dy++) {
                    if (Math.abs(dx) > 2 || Math.abs(dz) > 2 || dy < 3) {
                        session.setBlock(BlockVector3.at(x + dx, y + dy, z + dz), BlockTypes.SANDSTONE);
                    }
                }
            }
        }

        // Tête
        for (int dx = -4; dx <= 4; dx++) {
            for (int dz = -3; dz <= 3; dz++) {
                for (int dy = 6; dy < 12; dy++) {
                    session.setBlock(BlockVector3.at(x + dx, y + dy, z - 6 + dz), BlockTypes.CHISELED_SANDSTONE);
                }
            }
        }
    }

    /**
     * Construire un obélisque
     */
    private void buildObelisk(EditSession session, int x, int y, int z) {
        for (int dy = 0; dy < 20; dy++) {
            int size = 3 - (dy / 10);
            for (int dx = -size; dx <= size; dx++) {
                for (int dz = -size; dz <= size; dz++) {
                    session.setBlock(BlockVector3.at(x + dx, y + dy, z + dz), BlockTypes.SANDSTONE);
                }
            }
        }
        // Pointe dorée
        session.setBlock(BlockVector3.at(x, y + 20, z), BlockTypes.GOLD_BLOCK);
    }

    /**
     * Sous-sol de la pyramide - Catacombes
     */
    private void buildPyramidBasement(EditSession session, int cx, int cy, int cz) {
        // Salle principale sous-sol
        for (int x = -20; x <= 20; x++) {
            for (int z = -20; z <= 20; z++) {
                for (int y = -10; y < 0; y++) {
                    session.setBlock(BlockVector3.at(cx + x, cy + y, cz + z), BlockTypes.AIR);
                }
                session.setBlock(BlockVector3.at(cx + x, cy - 11, cz + z), BlockTypes.SANDSTONE);
                session.setBlock(BlockVector3.at(cx + x, cy, cz + z), BlockTypes.CHISELED_SANDSTONE);
            }
        }

        // Piliers
        for (int px = -15; px <= 15; px += 10) {
            for (int pz = -15; pz <= 15; pz += 10) {
                for (int py = -10; py < 0; py++) {
                    session.setBlock(BlockVector3.at(cx + px, cy + py, cz + pz), BlockTypes.SANDSTONE);
                }
            }
        }

        // Spawners (positions pour placement manuel)
        session.setBlock(BlockVector3.at(cx - 10, cy - 5, cz - 10), BlockTypes.SPAWNER);
        session.setBlock(BlockVector3.at(cx + 10, cy - 5, cz - 10), BlockTypes.SPAWNER);
        session.setBlock(BlockVector3.at(cx - 10, cy - 5, cz + 10), BlockTypes.SPAWNER);
        session.setBlock(BlockVector3.at(cx + 10, cy - 5, cz + 10), BlockTypes.SPAWNER);

        // Coffres
        session.setBlock(BlockVector3.at(cx, cy - 5, cz - 15), BlockTypes.CHEST);
        session.setBlock(BlockVector3.at(cx, cy - 5, cz + 15), BlockTypes.CHEST);
    }

    /**
     * Rez-de-chaussée - Salle du trône
     */
    private void buildPyramidMainFloor(EditSession session, int cx, int cy, int cz) {
        // Grande salle
        for (int x = -18; x <= 18; x++) {
            for (int z = -18; z <= 18; z++) {
                for (int y = 1; y < 12; y++) {
                    session.setBlock(BlockVector3.at(cx + x, cy + y, cz + z), BlockTypes.AIR);
                }
            }
        }

        // Sarcophages (spawners déguisés)
        for (int i = -12; i <= 12; i += 6) {
            session.setBlock(BlockVector3.at(cx + i, cy + 1, cz - 12), BlockTypes.SPAWNER);
            session.setBlock(BlockVector3.at(cx + i, cy + 1, cz + 12), BlockTypes.SPAWNER);
        }

        // Trône
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                session.setBlock(BlockVector3.at(cx + x, cy + 1, cz + 15 + z), BlockTypes.QUARTZ_BLOCK);
            }
        }
        session.setBlock(BlockVector3.at(cx, cy + 2, cz + 15), BlockTypes.QUARTZ_STAIRS);
    }

    /**
     * Étage 1 - Chambre au trésor
     */
    private void buildPyramidTreasure(EditSession session, int cx, int cy, int cz) {
        int floorY = cy + 15;

        // Salle du trésor
        for (int x = -15; x <= 15; x++) {
            for (int z = -15; z <= 15; z++) {
                for (int y = 0; y < 10; y++) {
                    session.setBlock(BlockVector3.at(cx + x, floorY + y, cz + z), BlockTypes.AIR);
                }
            }
        }

        // Coffres massifs
        for (int i = -10; i <= 10; i += 5) {
            session.setBlock(BlockVector3.at(cx + i, floorY + 1, cz), BlockTypes.CHEST);
            session.setBlock(BlockVector3.at(cx, floorY + 1, cz + i), BlockTypes.CHEST);
        }

        // Spawners (pièges)
        session.setBlock(BlockVector3.at(cx - 8, floorY + 1, cz - 8), BlockTypes.SPAWNER);
        session.setBlock(BlockVector3.at(cx + 8, floorY + 1, cz - 8), BlockTypes.SPAWNER);
        session.setBlock(BlockVector3.at(cx - 8, floorY + 1, cz + 8), BlockTypes.SPAWNER);
        session.setBlock(BlockVector3.at(cx + 8, floorY + 1, cz + 8), BlockTypes.SPAWNER);
    }

    /**
     * Sommet - Plateforme boss
     */
    private void buildPyramidBossRoom(EditSession session, int cx, int cy, int cz) {
        // Plateforme ouverte sur le désert
        for (int x = -15; x <= 15; x++) {
            for (int z = -15; z <= 15; z++) {
                session.setBlock(BlockVector3.at(cx + x, cy, cz + z), BlockTypes.SMOOTH_SANDSTONE);
            }
        }

        // Piliers aux coins
        for (int px : new int[]{-12, 12}) {
            for (int pz : new int[]{-12, 12}) {
                for (int py = 1; py <= 5; py++) {
                    session.setBlock(BlockVector3.at(cx + px, cy + py, cz + pz), BlockTypes.CHISELED_SANDSTONE);
                }
                session.setBlock(BlockVector3.at(cx + px, cy + 6, cz + pz), BlockTypes.GLOWSTONE);
            }
        }

        // Zone centrale boss
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                session.setBlock(BlockVector3.at(cx + x, cy, cz + z), BlockTypes.RED_SANDSTONE);
            }
        }
    }

    /**
     * Village abandonné
     */
    private void buildAbandonedVillage(EditSession session) {
        int vx = center.getBlockX() - 100;
        int vy = center.getBlockY();
        int vz = center.getBlockZ() - 80;

        // 10 maisons en ruines
        for (int i = 0; i < 10; i++) {
            int hx = vx + (i % 4) * 20;
            int hz = vz + (i / 4) * 20;

            // Maison partiellement détruite
            for (int x = 0; x < 8; x++) {
                for (int z = 0; z < 8; z++) {
                    // Murs partiels
                    if ((x == 0 || x == 7 || z == 0 || z == 7) && Math.random() > 0.3) {
                        for (int y = 0; y < 4; y++) {
                            if (Math.random() > 0.2) {
                                session.setBlock(BlockVector3.at(hx + x, vy + y, hz + z), BlockTypes.SANDSTONE);
                            }
                        }
                    }
                }
            }

            // Coffre caché (1 chance sur 3)
            if (i % 3 == 0) {
                session.setBlock(BlockVector3.at(hx + 4, vy + 1, hz + 4), BlockTypes.CHEST);
            }

            // Spawner (1 chance sur 4)
            if (i % 4 == 0) {
                session.setBlock(BlockVector3.at(hx + 2, vy + 1, hz + 2), BlockTypes.SPAWNER);
            }
        }
    }

    /**
     * Oasis (point de repos)
     */
    private void buildOasis(EditSession session) {
        int ox = center.getBlockX() + 80;
        int oy = center.getBlockY();
        int oz = center.getBlockZ() + 100;

        // Lac circulaire
        for (int x = -10; x <= 10; x++) {
            for (int z = -10; z <= 10; z++) {
                double distance = Math.sqrt(x * x + z * z);
                if (distance <= 10) {
                    // Creuser
                    session.setBlock(BlockVector3.at(ox + x, oy, oz + z), BlockTypes.AIR);
                    session.setBlock(BlockVector3.at(ox + x, oy - 1, oz + z), BlockTypes.WATER);
                    session.setBlock(BlockVector3.at(ox + x, oy - 2, oz + z), BlockTypes.CLAY);

                    // Herbe autour
                    if (distance > 8 && distance <= 10) {
                        session.setBlock(BlockVector3.at(ox + x, oy, oz + z), BlockTypes.GRASS_BLOCK);
                    }
                }
            }
        }

        // Palmiers (4 arbres)
        for (int[] palm : new int[][]{{-8, -8}, {8, -8}, {-8, 8}, {8, 8}}) {
            int px = ox + palm[0];
            int pz = oz + palm[1];

            // Tronc
            for (int y = 1; y <= 8; y++) {
                session.setBlock(BlockVector3.at(px, oy + y, pz), BlockTypes.JUNGLE_LOG);
            }

            // Feuillage
            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    if (Math.abs(x) + Math.abs(z) <= 3) {
                        session.setBlock(BlockVector3.at(px + x, oy + 9, pz + z), BlockTypes.JUNGLE_LEAVES);
                    }
                }
            }
        }

        // Petite cabane (NPC marchand)
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                if (Math.abs(x) == 3 || Math.abs(z) == 3) {
                    for (int y = 1; y <= 3; y++) {
                        session.setBlock(BlockVector3.at(ox + x, oy + y, oz + 15 + z), BlockTypes.ACACIA_PLANKS);
                    }
                }
            }
        }
        // Toit
        for (int x = -4; x <= 4; x++) {
            for (int z = -4; z <= 4; z++) {
                session.setBlock(BlockVector3.at(ox + x, oy + 4, oz + 15 + z), BlockTypes.ACACIA_SLAB);
            }
        }
    }

    /**
     * Décoration du désert (cactus, dead bushes, fossiles)
     */
    private void addDesertDecoration(EditSession session) {
        int cx = center.getBlockX();
        int cy = center.getBlockY();
        int cz = center.getBlockZ();

        // Placer aléatoirement dans la zone
        for (int i = 0; i < 200; i++) {
            int rx = cx + (int) (Math.random() * radius * 2 - radius);
            int rz = cz + (int) (Math.random() * radius * 2 - radius);

            // Éviter la pyramide et l'oasis
            if (Math.abs(rx - cx) < 60 && Math.abs(rz - cz) < 60) continue;

            double rand = Math.random();

            if (rand < 0.3) {
                // Cactus
                int height = 2 + (int) (Math.random() * 3);
                for (int y = 1; y <= height; y++) {
                    session.setBlock(BlockVector3.at(rx, cy + y, rz), BlockTypes.CACTUS);
                }
            } else if (rand < 0.5) {
                // Dead bush
                session.setBlock(BlockVector3.at(rx, cy + 1, rz), BlockTypes.DEAD_BUSH);
            } else if (rand < 0.55) {
                // Fossile (bone blocks)
                for (int x = 0; x < 3; x++) {
                    session.setBlock(BlockVector3.at(rx + x, cy, rz), BlockTypes.BONE_BLOCK);
                }
            }
        }
    }
}
