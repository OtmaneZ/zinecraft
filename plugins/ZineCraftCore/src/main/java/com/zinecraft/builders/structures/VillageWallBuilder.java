package com.zinecraft.builders.structures;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

/**
 * Constructeur de fortifications médiévales en bois pour le village
 * Crée des murs, portes et tours de guet style château fort
 * 
 * @author Otmane & Copilot
 */
public class VillageWallBuilder {
    
    private final Plugin plugin;
    private static final int WALL_HEIGHT = 5;
    private static final int TOWER_HEIGHT = 8;
    
    public VillageWallBuilder(Plugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Construire les fortifications complètes autour du village
     * 
     * @param center Centre du village
     * @param radius Rayon du village (50 blocs recommandé)
     */
    public void buildVillageWalls(Location center, int radius) {
        plugin.getLogger().info("§e[VillageWalls] Construction des fortifications...");
        
        World world = center.getWorld();
        int cx = center.getBlockX();
        int cz = center.getBlockZ();
        int groundY = center.getBlockY();
        
        // 1. Construire le mur circulaire principal
        buildCircularWall(world, cx, cz, groundY, radius);
        
        // 2. Construire 4 tours d'angle
        buildTowers(world, cx, cz, groundY, radius);
        
        // 3. Créer les portes (Nord, Sud, Est, Ouest)
        buildGates(world, cx, cz, groundY, radius);
        
        // 4. Ajouter des créneaux sur le mur
        buildBattlements(world, cx, cz, groundY, radius);
        
        plugin.getLogger().info("§a[VillageWalls] Fortifications terminées !");
    }
    
    /**
     * Construire un mur circulaire en bois
     */
    private void buildCircularWall(World world, int cx, int cz, int groundY, int radius) {
        // Parcourir le cercle avec un angle plus fin pour éviter les trous
        double angleStep = 360.0 / (2 * Math.PI * radius); // 1 bloc par pas environ
        
        for (double angle = 0; angle < 360; angle += angleStep) {
            double radians = Math.toRadians(angle);
            int x = (int) (cx + radius * Math.cos(radians));
            int z = (int) (cz + radius * Math.sin(radians));
            
            // Trouver le sol
            int y = findGroundLevel(world, x, groundY, z);
            
            // Construire le mur (double épaisseur pour solidité)
            for (int dy = 0; dy < WALL_HEIGHT; dy++) {
                // Couche externe (planches de chêne foncé)
                world.getBlockAt(x, y + dy, z).setType(Material.DARK_OAK_PLANKS);
                
                // Couche interne (planches de chêne)
                double innerRadians = Math.toRadians(angle);
                int innerX = (int) (cx + (radius - 1) * Math.cos(innerRadians));
                int innerZ = (int) (cz + (radius - 1) * Math.sin(innerRadians));
                world.getBlockAt(innerX, y + dy, innerZ).setType(Material.OAK_PLANKS);
            }
            
            // Poutres verticales régulièrement espacées
            if ((int)(angle / angleStep) % 10 == 0) {
                for (int dy = 0; dy < WALL_HEIGHT; dy++) {
                    world.getBlockAt(x, y + dy, z).setType(Material.DARK_OAK_LOG);
                }
            }
        }
    }
    
    /**
     * Construire 4 tours d'angle
     */
    private void buildTowers(World world, int cx, int cz, int groundY, int radius) {
        // Angles : NE, SE, SW, NW (45°, 135°, 225°, 315°)
        int[] angles = {45, 135, 225, 315};
        
        for (int angle : angles) {
            double radians = Math.toRadians(angle);
            int x = (int) (cx + radius * Math.cos(radians));
            int z = (int) (cz + radius * Math.sin(radians));
            
            // Trouver le sol
            int y = findGroundLevel(world, x, groundY, z);
            
            // Tour 3x3
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    // Structure de la tour
                    for (int dy = 0; dy < TOWER_HEIGHT; dy++) {
                        boolean isCorner = (Math.abs(dx) == 1 && Math.abs(dz) == 1);
                        boolean isEdge = (Math.abs(dx) == 1 || Math.abs(dz) == 1) && !(dx == 0 && dz == 0);
                        
                        if (isCorner) {
                            // Coins : poutres
                            world.getBlockAt(x + dx, y + dy, z + dz).setType(Material.DARK_OAK_LOG);
                        } else if (isEdge) {
                            // Bords : planches
                            world.getBlockAt(x + dx, y + dy, z + dz).setType(Material.DARK_OAK_PLANKS);
                        }
                        // Centre : vide (escalier intérieur)
                    }
                    
                    // Toit en escaliers
                    if (Math.abs(dx) <= 1 && Math.abs(dz) <= 1) {
                        world.getBlockAt(x + dx, y + TOWER_HEIGHT, z + dz).setType(Material.DARK_OAK_STAIRS);
                    }
                }
            }
            
            // Drapeau au sommet
            world.getBlockAt(x, y + TOWER_HEIGHT + 1, z).setType(Material.OAK_FENCE);
            world.getBlockAt(x, y + TOWER_HEIGHT + 2, z).setType(Material.OAK_FENCE);
            world.getBlockAt(x, y + TOWER_HEIGHT + 3, z).setType(Material.WHITE_BANNER);
        }
    }
    
    /**
     * Créer des portes aux 4 points cardinaux
     */
    private void buildGates(World world, int cx, int cz, int groundY, int radius) {
        // Angles : Nord (0°), Est (90°), Sud (180°), Ouest (270°)
        int[] angles = {0, 90, 180, 270};
        
        for (int angle : angles) {
            double radians = Math.toRadians(angle);
            int x = (int) (cx + radius * Math.cos(radians));
            int z = (int) (cz + radius * Math.sin(radians));
            
            // Trouver le sol
            int y = findGroundLevel(world, x, groundY, z);
            
            // Ouverture de 3 blocs de large, 4 blocs de haut
            for (int dw = -1; dw <= 1; dw++) {
                for (int dh = 0; dh < 4; dh++) {
                    int gateX = x;
                    int gateZ = z;
                    
                    // Ajuster selon l'orientation
                    if (angle == 0 || angle == 180) {
                        gateX += dw;
                    } else {
                        gateZ += dw;
                    }
                    
                    // Vider l'espace
                    world.getBlockAt(gateX, y + dh, gateZ).setType(Material.AIR);
                    
                    // Ajouter les portes en bois (premier et deuxième bloc)
                    if (dh < 2 && Math.abs(dw) == 1) {
                        world.getBlockAt(gateX, y + dh, gateZ).setType(Material.DARK_OAK_DOOR);
                    }
                }
            }
            
            // Piliers de chaque côté
            for (int dh = 0; dh < 5; dh++) {
                int leftX = x, leftZ = z, rightX = x, rightZ = z;
                
                if (angle == 0 || angle == 180) {
                    leftX -= 2;
                    rightX += 2;
                } else {
                    leftZ -= 2;
                    rightZ += 2;
                }
                
                world.getBlockAt(leftX, y + dh, leftZ).setType(Material.DARK_OAK_LOG);
                world.getBlockAt(rightX, y + dh, rightZ).setType(Material.DARK_OAK_LOG);
            }
            
            // Arche au dessus
            if (angle == 0 || angle == 180) {
                world.getBlockAt(x - 1, y + 4, z).setType(Material.DARK_OAK_STAIRS);
                world.getBlockAt(x, y + 4, z).setType(Material.DARK_OAK_PLANKS);
                world.getBlockAt(x + 1, y + 4, z).setType(Material.DARK_OAK_STAIRS);
            } else {
                world.getBlockAt(x, y + 4, z - 1).setType(Material.DARK_OAK_STAIRS);
                world.getBlockAt(x, y + 4, z).setType(Material.DARK_OAK_PLANKS);
                world.getBlockAt(x, y + 4, z + 1).setType(Material.DARK_OAK_STAIRS);
            }
        }
    }
    
    /**
     * Ajouter des créneaux sur le haut du mur
     */
    private void buildBattlements(World world, int cx, int cz, int groundY, int radius) {
        // Parcourir le cercle avec le même pas que le mur
        double angleStep = 360.0 / (2 * Math.PI * radius);
        
        for (double angle = 0; angle < 360; angle += angleStep * 2) { // Un créneau tous les 2 blocs
            double radians = Math.toRadians(angle);
            int x = (int) (cx + radius * Math.cos(radians));
            int z = (int) (cz + radius * Math.sin(radians));
            
            // Trouver le sol
            int y = findGroundLevel(world, x, groundY, z);
            
            // Créneaux alternés (un bloc tous les 2)
            if ((int)(angle / angleStep) % 2 == 0) {
                world.getBlockAt(x, y + WALL_HEIGHT, z).setType(Material.DARK_OAK_FENCE);
                world.getBlockAt(x, y + WALL_HEIGHT + 1, z).setType(Material.DARK_OAK_FENCE);
            }
        }
    }
    
    /**
     * Supprimer les fortifications
     */
    public void removeVillageWalls(Location center, int radius) {
        plugin.getLogger().info("§e[VillageWalls] Suppression des fortifications...");
        
        World world = center.getWorld();
        int cx = center.getBlockX();
        int cz = center.getBlockZ();
        int groundY = center.getBlockY();
        
        // Supprimer tout dans la zone des murs
        for (double angle = 0; angle < 360; angle += 1) {
            double radians = Math.toRadians(angle);
            
            // Zone élargie pour inclure tours
            for (int r = radius - 3; r <= radius + 3; r++) {
                int x = (int) (cx + r * Math.cos(radians));
                int z = (int) (cz + r * Math.sin(radians));
                
                for (int dy = 0; dy < TOWER_HEIGHT + 4; dy++) {
                    world.getBlockAt(x, groundY + dy, z).setType(Material.AIR);
                }
            }
        }
        
        plugin.getLogger().info("§a[VillageWalls] Fortifications supprimées !");
    }
    
    /**
     * Trouver le niveau du sol
     */
    private int findGroundLevel(World world, int x, int y, int z) {
        Block block = world.getBlockAt(x, y, z);
        
        // Descendre jusqu'au sol
        while (y > 0 && block.getType() == Material.AIR) {
            y--;
            block = world.getBlockAt(x, y, z);
        }
        
        return y + 1; // Retourner le bloc au-dessus du sol
    }
}
