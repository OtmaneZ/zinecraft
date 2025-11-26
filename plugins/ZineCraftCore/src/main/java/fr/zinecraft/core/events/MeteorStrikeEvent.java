package fr.zinecraft.core.events;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import fr.zinecraft.core.ZineCraftCore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Événement: Pluie de Météores
 * Des météores tombent du ciel, créant des cratères et laissant des minerais rares
 *
 * @author Otmane & Copilot
 */
public class MeteorStrikeEvent {

    private final ZineCraftCore plugin;
    private BukkitTask meteorTask;
    private final List<Location> craters;
    private final Random random;

    public MeteorStrikeEvent(ZineCraftCore plugin) {
        this.plugin = plugin;
        this.craters = new ArrayList<>();
        this.random = new Random();
    }

    /**
     * Démarrer l'événement
     */
    public void start() {
        // Faire tomber un météore toutes les 30 secondes
        meteorTask = new BukkitRunnable() {
            @Override
            public void run() {
                spawnMeteor();
            }
        }.runTaskTimer(plugin, 0L, 20L * 30); // Toutes les 30 secondes
    }

    /**
     * Faire apparaître un météore
     */
    private void spawnMeteor() {
        World world = Bukkit.getWorlds().get(0); // Monde principal

        // Choisir une position aléatoire autour du spawn
        Location spawn = world.getSpawnLocation();
        int x = spawn.getBlockX() + random.nextInt(400) - 200; // -200 à +200
        int z = spawn.getBlockZ() + random.nextInt(400) - 200;
        int y = world.getHighestBlockYAt(x, z) + 50; // 50 blocs au-dessus

        Location meteorStart = new Location(world, x, y, z);
        Location impactPoint = new Location(world, x, world.getHighestBlockYAt(x, z), z);

        // Annoncer l'approche
        Bukkit.broadcastMessage(ChatColor.RED + "☄ " + ChatColor.YELLOW +
            "Un météore approche! Coordonnées: " + ChatColor.WHITE +
            "X:" + x + " Z:" + z);

        // Créer l'effet de météore qui tombe
        createFallingMeteor(meteorStart, impactPoint);
    }

    /**
     * Créer l'effet visuel du météore qui tombe
     */
    private void createFallingMeteor(Location start, Location impact) {
        World world = start.getWorld();

        // Particules de traînée pendant la chute
        new BukkitRunnable() {
            int ticks = 0;
            final int maxTicks = 40; // 2 secondes

            @Override
            public void run() {
                if (ticks >= maxTicks) {
                    // Impact!
                    createImpact(impact);
                    this.cancel();
                    return;
                }

                // Calculer la position actuelle
                double progress = (double) ticks / maxTicks;
                double currentY = start.getY() - (start.getY() - impact.getY()) * progress;
                Location current = new Location(world, start.getX(), currentY, start.getZ());

                // Effets visuels
                world.spawnParticle(Particle.FLAME, current, 20, 0.5, 0.5, 0.5, 0.1);
                world.spawnParticle(Particle.LAVA, current, 5, 0.3, 0.3, 0.3, 0);
                world.spawnParticle(Particle.SMOKE_LARGE, current, 10, 0.3, 0.3, 0.3, 0.05);

                // Son
                world.playSound(current, Sound.ENTITY_BLAZE_SHOOT, 0.5f, 0.5f);

                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    /**
     * Créer l'impact du météore
     */
    private void createImpact(Location impact) {
        World world = impact.getWorld();

        // EXPLOSION spectaculaire
        world.spawnParticle(Particle.EXPLOSION_HUGE, impact, 10, 2, 1, 2, 0);
        world.spawnParticle(Particle.LAVA, impact, 50, 3, 2, 3, 0);
        world.spawnParticle(Particle.FLAME, impact, 100, 4, 2, 4, 0.2);
        world.spawnParticle(Particle.SMOKE_LARGE, impact, 50, 3, 2, 3, 0.1);

        // Sons d'impact
        world.playSound(impact, Sound.ENTITY_GENERIC_EXPLODE, 2.0f, 0.5f);
        world.playSound(impact, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 2.0f, 0.8f);

        // Créer le cratère
        createCrater(impact);

        // Sauvegarder le cratère
        craters.add(impact.clone());
    }

    /**
     * Créer un cratère avec des minerais rares
     */
    private void createCrater(Location center) {
        World world = center.getWorld();
        int radius = 5; // Rayon du cratère

        for (int x = -radius; x <= radius; x++) {
            for (int y = -2; y <= 0; y++) {
                for (int z = -radius; z <= radius; z++) {
                    double distance = Math.sqrt(x * x + y * y + z * z);

                    if (distance <= radius) {
                        Block block = world.getBlockAt(
                            center.getBlockX() + x,
                            center.getBlockY() + y,
                            center.getBlockZ() + z
                        );

                        // Créer le cratère avec des matériaux variés
                        if (distance <= 2) {
                            // Centre: minerais rares
                            if (random.nextInt(100) < 30) {
                                block.setType(Material.DIAMOND_ORE);
                            } else if (random.nextInt(100) < 50) {
                                block.setType(Material.GOLD_ORE);
                            } else {
                                block.setType(Material.IRON_ORE);
                            }
                        } else if (distance <= 3) {
                            // Milieu: obsidienne et pierre
                            if (random.nextInt(100) < 40) {
                                block.setType(Material.OBSIDIAN);
                            } else {
                                block.setType(Material.STONE);
                            }
                        } else {
                            // Extérieur: roche volcanique
                            if (random.nextInt(100) < 60) {
                                block.setType(Material.BLACKSTONE);
                            } else {
                                block.setType(Material.COBBLESTONE);
                            }
                        }
                    }
                }
            }
        }

        // Ajouter un peu de lave au centre
        Block centerBlock = world.getBlockAt(center.getBlockX(), center.getBlockY() - 1, center.getBlockZ());
        centerBlock.setType(Material.LAVA);
    }

    /**
     * Arrêter l'événement
     */
    public void stop() {
        if (meteorTask != null) {
            meteorTask.cancel();
        }
        craters.clear();
    }
}
