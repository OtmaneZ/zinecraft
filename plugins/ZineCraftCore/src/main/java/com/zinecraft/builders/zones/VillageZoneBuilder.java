package com.zinecraft.builders.zones;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.zinecraft.builders.core.StructureBuilder;
import com.zinecraft.builders.structures.FountainBuilder;
import com.zinecraft.builders.structures.HouseBuilder;
import com.zinecraft.builders.structures.MarketBuilder;
import com.zinecraft.builders.terrain.TerrainBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Orchestrateur pour la zone Village Spawn
 * Architecture modulaire avec FAWE async
 */
public class VillageZoneBuilder {

    private final Plugin plugin;
    private final World world;
    private final Location center;
    private final int radius;

    private final List<StructureBuilder> structures = new ArrayList<>();

    public VillageZoneBuilder(Plugin plugin, World world, Location center, int radius) {
        this.plugin = plugin;
        this.world = world;
        this.center = center;
        this.radius = radius;

        // Configuration des structures du village
        setupStructures();
    }

    private void setupStructures() {
        // Fontaine centrale
        structures.add(new FountainBuilder(5));

        // Marché autour de la fontaine
        structures.add(new MarketBuilder(8));

        // Maisons dispersées (style varié)
        for (int i = 0; i < 5; i++) {
            structures.add(new HouseBuilder(HouseBuilder.HouseStyle.WOOD, 6, 6, 3));
        }
        for (int i = 0; i < 5; i++) {
            structures.add(new HouseBuilder(HouseBuilder.HouseStyle.STONE, 7, 7, 4));
        }
        for (int i = 0; i < 5; i++) {
            structures.add(new HouseBuilder(HouseBuilder.HouseStyle.BRICK, 8, 8, 4));
        }
    }

    /**
     * Génère la zone complète de manière asynchrone avec FAWE
     */
    public void generate() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                // Créer une EditSession FAWE pour placement async
                var weWorld = BukkitAdapter.adapt(world);

                try (EditSession session = WorldEdit.getInstance()
                        .newEditSessionBuilder()
                        .world(weWorld)
                        .fastMode(true)
                        .limitUnlimited()
                        .build()) {

                    // 1. Terraforming du terrain
                    plugin.getLogger().info("[VillageZone] Aplanissement du terrain...");
                    TerrainBuilder.flattenArea(session, center, radius, center.getBlockY());

                    // 2. Construction des routes principales
                    plugin.getLogger().info("[VillageZone] Construction des routes...");
                    buildMainRoads(session);

                    // 3. Construction des structures une par une
                    plugin.getLogger().info("[VillageZone] Construction des structures...");
                    buildStructures(session);

                    // Flush toutes les modifications
                    session.flushQueue();

                    plugin.getLogger().info("[VillageZone] Village généré avec succès! (" + structures.size() + " structures)");
                }

            } catch (Exception e) {
                plugin.getLogger().severe("[VillageZone] Erreur lors de la génération: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void buildMainRoads(EditSession session) {
        // Route Nord-Sud
        Location north = center.clone().add(0, 0, -radius);
        Location south = center.clone().add(0, 0, radius);
        TerrainBuilder.createPath(session, north, south, 5);

        // Route Est-Ouest
        Location east = center.clone().add(radius, 0, 0);
        Location west = center.clone().add(-radius, 0, 0);
        TerrainBuilder.createPath(session, east, west, 5);
    }

    private void buildStructures(EditSession session) {
        // Fontaine au centre (index 0)
        structures.get(0).build(session, center);

        // Marché autour de la fontaine (index 1)
        structures.get(1).build(session, center);

        // Maisons dispersées dans le village
        int index = 2;
        int houseRadius = 25;

        // Quadrant Nord-Ouest
        for (int i = 0; i < 5; i++) {
            Location loc = center.clone().add(-houseRadius + i * 8, 0, -houseRadius + i * 5);
            structures.get(index++).build(session, loc);
        }

        // Quadrant Nord-Est
        for (int i = 0; i < 5; i++) {
            Location loc = center.clone().add(houseRadius - i * 8, 0, -houseRadius + i * 5);
            structures.get(index++).build(session, loc);
        }

        // Quadrant Sud
        for (int i = 0; i < 5; i++) {
            Location loc = center.clone().add(-20 + i * 10, 0, houseRadius - 5);
            structures.get(index++).build(session, loc);
        }
    }
}
