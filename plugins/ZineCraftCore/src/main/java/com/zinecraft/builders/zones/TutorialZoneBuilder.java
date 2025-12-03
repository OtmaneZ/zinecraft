package com.zinecraft.builders.zones;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockTypes;
import com.zinecraft.builders.structures.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

/**
 * Orchestrateur pour la zone Tutorial Island
 * Coords: (0, -60, 500) - Niveau 1-5 (débutant)
 * Taille: Rayon 50 blocs (diamètre 100)
 *
 * Architecture modulaire:
 * - SpawnPointBuilder: Beacon + chest spawn
 * - CombatArenaBuilder: Arène 20x20 avec spawner
 * - NPCPlatformBuilder: 3 plateformes (Guerrier, Mage, Marchand)
 * - PortalBuilder: Portail décoratif vers village
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

                    // 1. Terrain: Île circulaire au niveau Y=-60
                    buildIsland(session);

                    // 2. Structures: Utilisation des builders modulaires
                    buildStructures(session);

                    // 3. AMÉLIORATIONS: Chemins, éclairage, décoration
                    buildPaths(session);
                    addLighting(session);
                    addDecoration(session);

                    session.flushQueue();

                    plugin.getLogger().info("[TutorialIsland] Île générée avec succès!");
                }

            } catch (Exception e) {
                plugin.getLogger().severe("[TutorialIsland] Erreur: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * Construit le terrain de l'île: Cercle de rayon 50 au niveau Y=-60
     * Structure: Surface grass, sous-sol dirt (5 couches), base stone (3 couches)
     */
    private void buildIsland(EditSession session) {
        int cx = center.getBlockX();
        int cy = center.getBlockY(); // Y=-60 (niveau du sol)
        int cz = center.getBlockZ();
        int radius = size; // Rayon 50 blocs

        plugin.getLogger().info("[TutorialIsland] Construction du terrain à Y=" + cy + " (rayon " + radius + ")...");

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

    /**
     * Place toutes les structures sur l'île
     *
     * Distances calculées pour éviter chevauchement:
     * - SpawnPoint: Centre (0, 0) - rayon 3
     * - CombatArena: EST (+25, 0) - rayon 10, distance 25 blocs
     * - NPCPlatforms:
     * - Guerrier: OUEST (-20, 0) - rayon 3
     * - Mage: NORD (0, -20) - rayon 3
     * - Marchand: NORD-EST (+15, -15) - rayon 3
     * - Portal: SUD (0, +35) - rayon 3, sortie vers village
     */
    private void buildStructures(EditSession session) {
        int cx = center.getBlockX();
        int cy = center.getBlockY(); // Y=-60 (même niveau que l'île)
        int cz = center.getBlockZ();

        plugin.getLogger().info("[TutorialIsland] Placement des structures à Y=" + cy + "...");

        // 1. Spawn Point au centre (0, 0)
        SpawnPointBuilder spawnBuilder = new SpawnPointBuilder();
        Location spawnLoc = new Location(world, cx, cy, cz);
        spawnBuilder.build(session, spawnLoc);
        plugin.getLogger().info("  - SpawnPoint placé au centre");

        // 2. Arène de combat à l'EST (25, 0) - 25 blocs distance = rayon spawn (3) +
        // rayon arena (10) + marge (12)
        CombatArenaBuilder arenaBuilder = new CombatArenaBuilder(20);
        Location arenaLoc = new Location(world, cx + 25, cy, cz);
        arenaBuilder.build(session, arenaLoc);
        plugin.getLogger().info("  - CombatArena placée à l'EST (+25, 0)");

        // 3. PNJ Guerrier à l'OUEST (-20, 0)
        NPCPlatformBuilder guerrierBuilder = new NPCPlatformBuilder("GUERRIER");
        Location guerrierLoc = new Location(world, cx - 20, cy, cz);
        guerrierBuilder.build(session, guerrierLoc);
        plugin.getLogger().info("  - PNJ Guerrier placé à l'OUEST (-20, 0)");

        // 4. PNJ Mage au NORD (0, -20)
        NPCPlatformBuilder mageBuilder = new NPCPlatformBuilder("MAGE");
        Location mageLoc = new Location(world, cx, cy, cz - 20);
        mageBuilder.build(session, mageLoc);
        plugin.getLogger().info("  - PNJ Mage placé au NORD (0, -20)");

        // 5. PNJ Marchand au NORD-EST (15, -15)
        NPCPlatformBuilder marchandBuilder = new NPCPlatformBuilder("MARCHAND");
        Location marchandLoc = new Location(world, cx + 15, cy, cz - 15);
        marchandBuilder.build(session, marchandLoc);
        plugin.getLogger().info("  - PNJ Marchand placé au NORD-EST (15, -15)");

        // 6. Portail au SUD (0, 35) - Sortie vers village
        PortalBuilder portalBuilder = new PortalBuilder(4, 5);
        Location portalLoc = new Location(world, cx, cy, cz + 35);
        portalBuilder.build(session, portalLoc);
        plugin.getLogger().info("  - Portal placé au SUD (0, 35)");
    }

    /**
     * Crée des chemins en stone_bricks entre les structures
     * Chemins de largeur 3 blocs pour faciliter la navigation
     */
    private void buildPaths(EditSession session) {
        int cx = center.getBlockX();
        int cy = center.getBlockY();
        int cz = center.getBlockZ();

        plugin.getLogger().info("[TutorialIsland] Construction des chemins...");

        // Chemin 1: Spawn → Arena (EST)
        buildPath(session, cx, cy, cz, cx + 25, cy, cz, 3);

        // Chemin 2: Spawn → Guerrier (OUEST)
        buildPath(session, cx, cy, cz, cx - 20, cy, cz, 3);

        // Chemin 3: Spawn → Mage (NORD)
        buildPath(session, cx, cy, cz, cx, cy, cz - 20, 3);

        // Chemin 4: Spawn → Marchand (NORD-EST)
        buildPath(session, cx, cy, cz, cx + 15, cy, cz - 15, 3);

        // Chemin 5: Spawn → Portal (SUD)
        buildPath(session, cx, cy, cz, cx, cy, cz + 35, 3);
    }

    /**
     * Construit un chemin entre deux points
     */
    private void buildPath(EditSession session, int x1, int y1, int z1, int x2, int y2, int z2, int width) {
        double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(z2 - z1, 2));
        double dirX = (x2 - x1) / distance;
        double dirZ = (z2 - z1) / distance;

        for (int i = 0; i <= (int) distance; i++) {
            int centerX = x1 + (int) (dirX * i);
            int centerZ = z1 + (int) (dirZ * i);

            // Chemin principal en stone_bricks
            for (int w = -width / 2; w <= width / 2; w++) {
                int perpX = (int) (-dirZ * w);
                int perpZ = (int) (dirX * w);
                session.setBlock(BlockVector3.at(centerX + perpX, y1, centerZ + perpZ), BlockTypes.STONE_BRICKS);

                // Bordures en polished_andesite
                if (Math.abs(w) == width / 2) {
                    session.setBlock(BlockVector3.at(centerX + perpX, y1, centerZ + perpZ),
                            BlockTypes.POLISHED_ANDESITE);
                }
            }
        }
    }

    /**
     * Ajoute de l'éclairage sur toute l'île
     * Lanternes tous les 8 blocs le long des chemins et autour des structures
     */
    private void addLighting(EditSession session) {
        int cx = center.getBlockX();
        int cy = center.getBlockY();
        int cz = center.getBlockZ();

        plugin.getLogger().info("[TutorialIsland] Ajout de l'éclairage...");

        // Lanternes autour du spawn (cercle rayon 6)
        for (int angle = 0; angle < 360; angle += 45) {
            double rad = Math.toRadians(angle);
            int x = cx + (int) (6 * Math.cos(rad));
            int z = cz + (int) (6 * Math.sin(rad));
            session.setBlock(BlockVector3.at(x, cy + 1, z), BlockTypes.LANTERN);
        }

        // Lanternes le long du chemin vers l'arena (tous les 8 blocs)
        for (int i = 8; i < 25; i += 8) {
            session.setBlock(BlockVector3.at(cx + i, cy + 2, cz), BlockTypes.LANTERN);
        }

        // Lanternes vers le portal (tous les 8 blocs)
        for (int i = 8; i < 35; i += 8) {
            session.setBlock(BlockVector3.at(cx, cy + 2, cz + i), BlockTypes.LANTERN);
        }

        // Lanternes autour du bord de l'île (cercle rayon 45)
        for (int angle = 0; angle < 360; angle += 30) {
            double rad = Math.toRadians(angle);
            int x = cx + (int) (45 * Math.cos(rad));
            int z = cz + (int) (45 * Math.sin(rad));
            session.setBlock(BlockVector3.at(x, cy + 3, z), BlockTypes.TORCH);
        }
    }

    /**
     * Ajoute de la décoration naturelle
     * Arbres, fleurs, panneaux directionnels
     */
    private void addDecoration(EditSession session) {
        int cx = center.getBlockX();
        int cy = center.getBlockY();
        int cz = center.getBlockZ();

        plugin.getLogger().info("[TutorialIsland] Ajout de la décoration...");

        // Arbres aux 4 coins de l'île
        buildSmallTree(session, cx + 30, cy, cz + 30);
        buildSmallTree(session, cx - 30, cy, cz + 30);
        buildSmallTree(session, cx + 30, cy, cz - 30);
        buildSmallTree(session, cx - 30, cy, cz - 30);

        // Fleurs aléatoires autour de l'île
        addFlowerCluster(session, cx + 10, cy, cz + 15);
        addFlowerCluster(session, cx - 10, cy, cz + 15);
        addFlowerCluster(session, cx + 15, cy, cz - 10);
        addFlowerCluster(session, cx - 15, cy, cz - 10);

        // Panneaux directionnels au spawn
        session.setBlock(BlockVector3.at(cx + 3, cy + 1, cz), BlockTypes.OAK_SIGN); // → Arena
        session.setBlock(BlockVector3.at(cx - 3, cy + 1, cz), BlockTypes.OAK_SIGN); // ← Guerrier
        session.setBlock(BlockVector3.at(cx, cy + 1, cz - 3), BlockTypes.OAK_SIGN); // ↑ Mage
        session.setBlock(BlockVector3.at(cx, cy + 1, cz + 3), BlockTypes.OAK_SIGN); // ↓ Portal
    }

    /**
     * Construit un petit arbre (5 blocs de haut)
     */
    private void buildSmallTree(EditSession session, int x, int y, int z) {
        // Tronc (4 blocs)
        for (int dy = 1; dy <= 4; dy++) {
            session.setBlock(BlockVector3.at(x, y + dy, z), BlockTypes.OAK_LOG);
        }

        // Feuillage (3x3x2)
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                session.setBlock(BlockVector3.at(x + dx, y + 4, z + dz), BlockTypes.OAK_LEAVES);
                session.setBlock(BlockVector3.at(x + dx, y + 5, z + dz), BlockTypes.OAK_LEAVES);
            }
        }

        // Top
        session.setBlock(BlockVector3.at(x, y + 6, z), BlockTypes.OAK_LEAVES);
    }

    /**
     * Ajoute un groupe de fleurs
     */
    private void addFlowerCluster(EditSession session, int x, int y, int z) {
        session.setBlock(BlockVector3.at(x, y + 1, z), BlockTypes.POPPY);
        session.setBlock(BlockVector3.at(x + 1, y + 1, z), BlockTypes.DANDELION);
        session.setBlock(BlockVector3.at(x - 1, y + 1, z), BlockTypes.BLUE_ORCHID);
        session.setBlock(BlockVector3.at(x, y + 1, z + 1), BlockTypes.ALLIUM);
        session.setBlock(BlockVector3.at(x, y + 1, z - 1), BlockTypes.OXEYE_DAISY);
    }
}
