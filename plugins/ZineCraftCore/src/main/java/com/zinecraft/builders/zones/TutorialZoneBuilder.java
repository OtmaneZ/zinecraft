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
 * Coords: (0, 500) - Niveau 1-5
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

                    // 1. Terrain: Île circulaire surélevée (Y=70)
                    buildIsland(session);

                    // 2. Structures: Utilisation des builders modulaires
                    buildStructures(session);

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
     * Construit le terrain de l'île: Cercle de 50 blocs de rayon surélevé à Y=70
     */
    private void buildIsland(EditSession session) {
        int cx = center.getBlockX();
        int cy = center.getBlockY() + 10; // Surélévation Y=70
        int cz = center.getBlockZ();
        int radius = size / 2; // 50 blocs de rayon

        plugin.getLogger().info("[TutorialIsland] Construction du terrain (rayon " + radius + ")...");

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
     * - CombatArena: EST (25, 0) - rayon 10
     * - NPCPlatforms: OUEST (-20, 0), NORD (0, -20), NORD-EST (15, -15) - rayon 3 chacun
     * - Portal: SUD (0, 35) - rayon 3
     */
    private void buildStructures(EditSession session) {
        int cx = center.getBlockX();
        int cy = center.getBlockY() + 10; // Y=70
        int cz = center.getBlockZ();

        plugin.getLogger().info("[TutorialIsland] Placement des structures...");

        // 1. Spawn Point au centre (0, 0)
        SpawnPointBuilder spawnBuilder = new SpawnPointBuilder();
        Location spawnLoc = new Location(world, cx, cy, cz);
        spawnBuilder.build(session, spawnLoc);
        plugin.getLogger().info("  - SpawnPoint placé au centre");

        // 2. Arène de combat à l'EST (25, 0) - 25 blocs distance = rayon spawn (3) + rayon arena (10) + marge (12)
        CombatArenaBuilder arenaBuilder = new CombatArenaBuilder(20, 20);
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
}
