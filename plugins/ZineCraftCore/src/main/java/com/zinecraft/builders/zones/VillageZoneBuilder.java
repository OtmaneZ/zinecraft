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
import com.zinecraft.builders.structures.ForgeBuilder;
import com.zinecraft.builders.structures.InnBuilder;
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
        // Fontaine centrale (PLUS GRANDE)
        structures.add(new FountainBuilder(8)); // Rayon 8 au lieu de 5

        // Marché autour de la fontaine (PLUS DE STANDS)
        structures.add(new MarketBuilder(12)); // 12 stands au lieu de 8

        // Forge (PLUS GRANDE)
        structures.add(new ForgeBuilder(15, 12, 5)); // 15x12 au lieu de 10x8

        // Auberge/Taverne (PLUS GRANDE)
        structures.add(new InnBuilder(18, 15)); // 18x15 au lieu de 12x10

        // Maisons dispersées (PLUS GRANDES et plus variées)
        for (int i = 0; i < 5; i++) {
            structures.add(new HouseBuilder(HouseBuilder.HouseStyle.WOOD, 8, 8, 4)); // 8x8 au lieu de 6x6
        }
        for (int i = 0; i < 5; i++) {
            structures.add(new HouseBuilder(HouseBuilder.HouseStyle.STONE, 10, 10, 5)); // 10x10 au lieu de 7x7
        }
        for (int i = 0; i < 5; i++) {
            structures.add(new HouseBuilder(HouseBuilder.HouseStyle.BRICK, 12, 12, 5)); // 12x12 au lieu de 8x8
        }
    }    /**
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
        // 1. FONTAINE AU CENTRE (0, 0) - Rayon 5 blocs
        structures.get(0).build(session, center);

        // 2. MARCHÉ EN CERCLE AUTOUR FONTAINE (Rayon 15 blocs depuis centre)
        // MarketBuilder place les stands à rayon 12 depuis son centre
        // Donc on décale le centre du marché pour éviter la fontaine
        Location marketCenter = center.clone();
        structures.get(1).build(session, marketCenter);

        // 3. BÂTIMENTS PRINCIPAUX (bien espacés)
        // Forge au NORD (distance 40 blocs)
        Location forgeLocation = center.clone().add(0, 0, -40);
        structures.get(2).build(session, forgeLocation);

        // Auberge au SUD (distance 40 blocs)
        Location innLocation = center.clone().add(0, 0, 40);
        structures.get(3).build(session, innLocation);

        // 4. MAISONS (bien espacées en grille régulière)
        int index = 4;
        int spacing = 15; // 15 blocs entre chaque maison

        // Quadrant Nord-Ouest (5 maisons WOOD)
        int startX = -50;
        int startZ = -50;
        for (int i = 0; i < 5; i++) {
            int col = i % 3; // 3 colonnes
            int row = i / 3; // 2 rangées
            Location loc = center.clone().add(startX + col * spacing, 0, startZ + row * spacing);
            structures.get(index++).build(session, loc);
        }

        // Quadrant Nord-Est (5 maisons STONE)
        startX = 20; // Commence après le marché
        startZ = -50;
        for (int i = 0; i < 5; i++) {
            int col = i % 3;
            int row = i / 3;
            Location loc = center.clone().add(startX + col * spacing, 0, startZ + row * spacing);
            structures.get(index++).build(session, loc);
        }

        // Quadrant Sud (5 maisons BRICK)
        startX = -50;
        startZ = 20; // Commence après marché/fontaine
        for (int i = 0; i < 5; i++) {
            int col = i % 5; // 5 colonnes (ligne horizontale)
            Location loc = center.clone().add(startX + col * spacing, 0, startZ);
            structures.get(index++).build(session, loc);
        }
    }
}
