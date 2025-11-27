package fr.zinecraft.core.builders;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

/**
 * Générateur automatique du Village Spawn
 * Crée 15 maisons + fontaine centrale + routes + marché
 */
public class VillageBuilder implements CommandExecutor {

    private final Random random = new Random();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cCette commande doit être exécutée par un joueur !");
            return true;
        }

        Player player = (Player) sender;
        World world = player.getWorld();

        if (world == null) {
            player.sendMessage("§cErreur: Monde invalide !");
            return true;
        }

        // COORDONNÉES FIXES selon map_to_code.md
        Location center = new Location(world, 0, -60, 0);

        player.sendMessage("§a§l[ZINECRAFT] §eGénération du Village Spawn...");
        player.sendMessage("§7Coordonnées MAP: X=0, Y=-60, Z=0 (Centre monde)");

        // Aplatis le terrain (100x100)
        player.sendMessage("§71/6 §eAplatissement du terrain...");
        flattenTerrain(world, center, 50);

        // Routes principales (croix)
        player.sendMessage("§72/6 §eConstruction des routes...");
        buildMainRoads(world, center);

        // Fontaine centrale
        player.sendMessage("§73/6 §eConstruction de la fontaine centrale...");
        buildFountain(world, center);

        // Marché (8 stands)
        player.sendMessage("§74/6 §eConstruction du marché...");
        buildMarket(world, center);

        // Maisons (15 autour du village)
        player.sendMessage("§75/6 §eConstruction de 15 maisons...");
        buildHouses(world, center);

        // Forge + Auberge
        player.sendMessage("§76/6 §eConstruction de la forge et de l'auberge...");
        buildForge(world, center.clone().add(25, 0, 25));
        buildInn(world, center.clone().add(-25, 0, 25));

        player.sendMessage("§a§l✔ Village Spawn généré avec succès !");
        player.sendMessage("§715 maisons + fontaine + marché + forge + auberge");

        return true;
    }

    /**
     * Aplatit le terrain sur une zone carrée
     */
    private void flattenTerrain(World world, Location center, int radius) {
        int baseY = center.getBlockY();

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                Location loc = center.clone().add(x, 0, z);

                // Remplit en dessous avec de la terre
                for (int y = baseY - 3; y < baseY; y++) {
                    world.getBlockAt(loc.getBlockX(), y, loc.getBlockZ()).setType(Material.DIRT);
                }

                // Sol en herbe
                world.getBlockAt(loc.getBlockX(), baseY, loc.getBlockZ()).setType(Material.GRASS_BLOCK);

                // Vide au-dessus
                for (int y = baseY + 1; y < baseY + 20; y++) {
                    world.getBlockAt(loc.getBlockX(), y, loc.getBlockZ()).setType(Material.AIR);
                }
            }
        }
    }

    /**
     * Construit les routes principales (croix)
     */
    private void buildMainRoads(World world, Location center) {
        int baseY = center.getBlockY();

        // Route Nord-Sud (axe Z)
        for (int z = -40; z <= 40; z++) {
            for (int x = -2; x <= 2; x++) {
                Block block = world.getBlockAt(center.getBlockX() + x, baseY, center.getBlockZ() + z);
                block.setType(Material.COBBLESTONE);

                // Bordures
                if (x == -2 || x == 2) {
                    block.setType(Material.STONE_BRICKS);
                }
            }
        }

        // Route Est-Ouest (axe X)
        for (int x = -40; x <= 40; x++) {
            for (int z = -2; z <= 2; z++) {
                Block block = world.getBlockAt(center.getBlockX() + x, baseY, center.getBlockZ() + z);
                block.setType(Material.COBBLESTONE);

                // Bordures
                if (z == -2 || z == 2) {
                    block.setType(Material.STONE_BRICKS);
                }
            }
        }
    }

    /**
     * Construit la fontaine centrale
     */
    private void buildFountain(World world, Location center) {
        int baseY = center.getBlockY();

        // Base circulaire (rayon 5)
        for (int x = -5; x <= 5; x++) {
            for (int z = -5; z <= 5; z++) {
                double distance = Math.sqrt(x * x + z * z);
                if (distance <= 5) {
                    Location loc = center.clone().add(x, 0, z);

                    // Bord de la fontaine
                    if (distance > 4) {
                        world.getBlockAt(loc.getBlockX(), baseY, loc.getBlockZ()).setType(Material.STONE_BRICKS);
                        world.getBlockAt(loc.getBlockX(), baseY + 1, loc.getBlockZ()).setType(Material.STONE_BRICK_WALL);
                    } else {
                        // Eau
                        world.getBlockAt(loc.getBlockX(), baseY, loc.getBlockZ()).setType(Material.WATER);
                    }
                }
            }
        }

        // Pilier central
        for (int y = 0; y <= 3; y++) {
            world.getBlockAt(center.getBlockX(), baseY + y, center.getBlockZ()).setType(Material.QUARTZ_PILLAR);
        }
        world.getBlockAt(center.getBlockX(), baseY + 4, center.getBlockZ()).setType(Material.SEA_LANTERN);
    }

    /**
     * Construit le marché (8 stands)
     */
    private void buildMarket(World world, Location center) {
        int baseY = center.getBlockY();

        // 8 stands autour de la fontaine
        int[][] positions = {
            {15, 0}, {-15, 0}, {0, 15}, {0, -15},
            {10, 10}, {-10, 10}, {10, -10}, {-10, -10}
        };

        for (int[] pos : positions) {
            Location standLoc = center.clone().add(pos[0], 0, pos[1]);
            buildMarketStand(world, standLoc, baseY);
        }
    }

    /**
     * Construit un stand de marché (3x3)
     */
    private void buildMarketStand(World world, Location loc, int baseY) {
        int x = loc.getBlockX();
        int z = loc.getBlockZ();

        // Toit (laine colorée aléatoire)
        Material[] colors = {Material.RED_WOOL, Material.YELLOW_WOOL, Material.LIME_WOOL, Material.CYAN_WOOL};
        Material color = colors[random.nextInt(colors.length)];

        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                // Poteaux aux coins
                if ((dx == -1 || dx == 1) && (dz == -1 || dz == 1)) {
                    world.getBlockAt(x + dx, baseY + 1, z + dz).setType(Material.OAK_FENCE);
                    world.getBlockAt(x + dx, baseY + 2, z + dz).setType(Material.OAK_FENCE);
                }

                // Toit
                world.getBlockAt(x + dx, baseY + 3, z + dz).setType(color);
            }
        }

        // Comptoir (barrière)
        world.getBlockAt(x, baseY + 1, z).setType(Material.OAK_FENCE);
    }

    /**
     * Construit 15 maisons variées
     */
    private void buildHouses(World world, Location center) {
        int baseY = center.getBlockY();

        // Positions autour du village
        int[][] positions = {
            // Quartier Nord
            {-20, -20}, {-10, -25}, {0, -30}, {10, -25}, {20, -20},
            // Quartier Sud
            {-20, 20}, {-10, 25}, {0, 30}, {10, 25}, {20, 20},
            // Quartier Est
            {25, -10}, {30, 0}, {25, 10},
            // Quartier Ouest
            {-25, -10}, {-30, 0}
        };

        for (int i = 0; i < positions.length; i++) {
            int[] pos = positions[i];
            Location houseLoc = center.clone().add(pos[0], 0, pos[1]);

            // Alterne entre 3 styles de maisons
            if (i % 3 == 0) {
                buildHouseStyle1(world, houseLoc, baseY);
            } else if (i % 3 == 1) {
                buildHouseStyle2(world, houseLoc, baseY);
            } else {
                buildHouseStyle3(world, houseLoc, baseY);
            }
        }
    }

    /**
     * Maison Style 1 : Petite maison en bois (5x5)
     */
    private void buildHouseStyle1(World world, Location loc, int baseY) {
        int x = loc.getBlockX();
        int z = loc.getBlockZ();

        // Murs (5x5)
        for (int dx = 0; dx < 5; dx++) {
            for (int dz = 0; dz < 5; dz++) {
                // Sol
                world.getBlockAt(x + dx, baseY, z + dz).setType(Material.OAK_PLANKS);

                // Murs
                if (dx == 0 || dx == 4 || dz == 0 || dz == 4) {
                    world.getBlockAt(x + dx, baseY + 1, z + dz).setType(Material.OAK_PLANKS);
                    world.getBlockAt(x + dx, baseY + 2, z + dz).setType(Material.OAK_PLANKS);
                    world.getBlockAt(x + dx, baseY + 3, z + dz).setType(Material.OAK_PLANKS);
                }

                // Toit
                if (dx >= 0 && dx < 5 && dz >= 0 && dz < 5) {
                    world.getBlockAt(x + dx, baseY + 4, z + dz).setType(Material.OAK_STAIRS);
                }
            }
        }

        // Porte
        world.getBlockAt(x + 2, baseY + 1, z).setType(Material.OAK_DOOR);
        world.getBlockAt(x + 2, baseY + 2, z).setType(Material.AIR);

        // Fenêtres
        world.getBlockAt(x, baseY + 2, z + 2).setType(Material.GLASS_PANE);
        world.getBlockAt(x + 4, baseY + 2, z + 2).setType(Material.GLASS_PANE);
    }

    /**
     * Maison Style 2 : Maison en pierre (6x6)
     */
    private void buildHouseStyle2(World world, Location loc, int baseY) {
        int x = loc.getBlockX();
        int z = loc.getBlockZ();

        for (int dx = 0; dx < 6; dx++) {
            for (int dz = 0; dz < 6; dz++) {
                // Sol
                world.getBlockAt(x + dx, baseY, z + dz).setType(Material.STONE);

                // Murs
                if (dx == 0 || dx == 5 || dz == 0 || dz == 5) {
                    world.getBlockAt(x + dx, baseY + 1, z + dz).setType(Material.COBBLESTONE);
                    world.getBlockAt(x + dx, baseY + 2, z + dz).setType(Material.COBBLESTONE);
                    world.getBlockAt(x + dx, baseY + 3, z + dz).setType(Material.COBBLESTONE);
                }

                // Toit
                world.getBlockAt(x + dx, baseY + 4, z + dz).setType(Material.STONE_BRICK_STAIRS);
            }
        }

        // Porte
        world.getBlockAt(x + 2, baseY + 1, z).setType(Material.IRON_DOOR);
        world.getBlockAt(x + 2, baseY + 2, z).setType(Material.AIR);
    }

    /**
     * Maison Style 3 : Maison en briques (7x5)
     */
    private void buildHouseStyle3(World world, Location loc, int baseY) {
        int x = loc.getBlockX();
        int z = loc.getBlockZ();

        for (int dx = 0; dx < 7; dx++) {
            for (int dz = 0; dz < 5; dz++) {
                // Sol
                world.getBlockAt(x + dx, baseY, z + dz).setType(Material.BRICKS);

                // Murs
                if (dx == 0 || dx == 6 || dz == 0 || dz == 4) {
                    world.getBlockAt(x + dx, baseY + 1, z + dz).setType(Material.BRICKS);
                    world.getBlockAt(x + dx, baseY + 2, z + dz).setType(Material.BRICKS);
                    world.getBlockAt(x + dx, baseY + 3, z + dz).setType(Material.BRICKS);
                }

                // Toit
                world.getBlockAt(x + dx, baseY + 4, z + dz).setType(Material.BRICK_STAIRS);
            }
        }

        // Porte
        world.getBlockAt(x + 3, baseY + 1, z).setType(Material.SPRUCE_DOOR);
        world.getBlockAt(x + 3, baseY + 2, z).setType(Material.AIR);
    }

    /**
     * Forge (10x8 avec cheminée)
     */
    private void buildForge(World world, Location loc) {
        int baseY = loc.getBlockY();
        int x = loc.getBlockX();
        int z = loc.getBlockZ();

        // Structure principale
        for (int dx = 0; dx < 10; dx++) {
            for (int dz = 0; dz < 8; dz++) {
                // Sol
                world.getBlockAt(x + dx, baseY, z + dz).setType(Material.STONE);

                // Murs
                if (dx == 0 || dx == 9 || dz == 0 || dz == 7) {
                    for (int y = 1; y <= 4; y++) {
                        world.getBlockAt(x + dx, baseY + y, z + dz).setType(Material.STONE_BRICKS);
                    }
                }

                // Toit
                world.getBlockAt(x + dx, baseY + 5, z + dz).setType(Material.STONE_BRICK_SLAB);
            }
        }

        // Cheminée (coin)
        for (int y = 0; y <= 7; y++) {
            world.getBlockAt(x + 1, baseY + y, z + 1).setType(Material.BRICKS);
        }

        // Fumée (laine grise)
        world.getBlockAt(x + 1, baseY + 8, z + 1).setType(Material.GRAY_WOOL);

        // Porte
        world.getBlockAt(x + 4, baseY + 1, z).setType(Material.IRON_DOOR);
        world.getBlockAt(x + 4, baseY + 2, z).setType(Material.AIR);
    }

    /**
     * Auberge (12x10 avec étage)
     */
    private void buildInn(World world, Location loc) {
        int baseY = loc.getBlockY();
        int x = loc.getBlockX();
        int z = loc.getBlockZ();

        // Rez-de-chaussée
        for (int dx = 0; dx < 12; dx++) {
            for (int dz = 0; dz < 10; dz++) {
                // Sol
                world.getBlockAt(x + dx, baseY, z + dz).setType(Material.DARK_OAK_PLANKS);

                // Murs
                if (dx == 0 || dx == 11 || dz == 0 || dz == 9) {
                    for (int y = 1; y <= 3; y++) {
                        world.getBlockAt(x + dx, baseY + y, z + dz).setType(Material.SPRUCE_PLANKS);
                    }
                }

                // Plafond/Sol étage
                world.getBlockAt(x + dx, baseY + 4, z + dz).setType(Material.DARK_OAK_PLANKS);
            }
        }

        // Étage (plus petit)
        for (int dx = 2; dx < 10; dx++) {
            for (int dz = 2; dz < 8; dz++) {
                // Murs étage
                if (dx == 2 || dx == 9 || dz == 2 || dz == 7) {
                    for (int y = 5; y <= 7; y++) {
                        world.getBlockAt(x + dx, baseY + y, z + dz).setType(Material.SPRUCE_PLANKS);
                    }
                }

                // Toit
                world.getBlockAt(x + dx, baseY + 8, z + dz).setType(Material.SPRUCE_STAIRS);
            }
        }

        // Porte principale
        world.getBlockAt(x + 5, baseY + 1, z).setType(Material.DARK_OAK_DOOR);
        world.getBlockAt(x + 5, baseY + 2, z).setType(Material.AIR);

        // Enseigne (laine orange)
        world.getBlockAt(x + 6, baseY + 3, z - 1).setType(Material.ORANGE_WOOL);
    }
}
