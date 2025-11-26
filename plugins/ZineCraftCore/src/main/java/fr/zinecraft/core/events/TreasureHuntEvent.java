package fr.zinecraft.core.events;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import fr.zinecraft.core.ZineCraftCore;

import java.util.Random;

/**
 * Événement: Chasse au Trésor
 * Un coffre légendaire apparaît quelque part dans le monde
 *
 * @author Otmane & Copilot
 */
public class TreasureHuntEvent {

    private final ZineCraftCore plugin;
    private Location treasureLocation;
    private BukkitTask hintTask;
    private final Random random;

    public TreasureHuntEvent(ZineCraftCore plugin) {
        this.plugin = plugin;
        this.random = new Random();
    }

    /**
     * Démarrer l'événement
     */
    public void start() {
        World world = Bukkit.getWorlds().get(0);

        // Choisir une position aléatoire
        Location spawn = world.getSpawnLocation();
        int x = spawn.getBlockX() + random.nextInt(600) - 300; // -300 à +300
        int z = spawn.getBlockZ() + random.nextInt(600) - 300;
        int y = world.getHighestBlockYAt(x, z);

        treasureLocation = new Location(world, x, y, z);

        // Placer le coffre
        placeTreasureChest(treasureLocation);

        // Annoncer la chasse
        Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD +
            "💎 UN TRÉSOR LÉGENDAIRE EST APPARU! 💎");
        Bukkit.broadcastMessage(ChatColor.YELLOW +
            "Cherchez les indices pour le trouver...");

        // Donner des indices toutes les minutes
        hintTask = new BukkitRunnable() {
            int hintsGiven = 0;

            @Override
            public void run() {
                hintsGiven++;
                giveHint(hintsGiven);
            }
        }.runTaskTimer(plugin, 20L * 60, 20L * 60); // Toutes les 60 secondes
    }

    /**
     * Placer le coffre au trésor
     */
    private void placeTreasureChest(Location location) {
        World world = location.getWorld();

        // Créer une petite plateforme
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                Block block = world.getBlockAt(
                    location.getBlockX() + x,
                    location.getBlockY() - 1,
                    location.getBlockZ() + z
                );
                block.setType(Material.GOLD_BLOCK);
            }
        }

        // Placer le coffre
        Block chestBlock = world.getBlockAt(location);
        chestBlock.setType(Material.CHEST);

        // Remplir le coffre de trésors
        if (chestBlock.getState() instanceof Chest) {
            Chest chest = (Chest) chestBlock.getState();

            // Ajouter des items légendaires
            chest.getInventory().addItem(new ItemStack(Material.DIAMOND, 32));
            chest.getInventory().addItem(new ItemStack(Material.EMERALD, 16));
            chest.getInventory().addItem(new ItemStack(Material.NETHERITE_INGOT, 8));
            chest.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 5));
            chest.getInventory().addItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 2));
            chest.getInventory().addItem(new ItemStack(Material.ELYTRA, 1));
            chest.getInventory().addItem(new ItemStack(Material.TOTEM_OF_UNDYING, 1));

            chest.update();
        }

        // Effets visuels permanents
        new BukkitRunnable() {
            @Override
            public void run() {
                if (chestBlock.getType() != Material.CHEST) {
                    // Le coffre a été ouvert/détruit
                    this.cancel();
                    return;
                }

                // Particules dorées
                world.spawnParticle(Particle.END_ROD,
                    location.clone().add(0.5, 1.5, 0.5),
                    5, 0.3, 0.3, 0.3, 0.05);
                world.spawnParticle(Particle.TOTEM,
                    location.clone().add(0.5, 1.5, 0.5),
                    3, 0.2, 0.2, 0.2, 0.1);
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    /**
     * Donner un indice aux joueurs
     */
    private void giveHint(int hintNumber) {
        if (treasureLocation == null) {
            return;
        }

        int x = treasureLocation.getBlockX();
        int z = treasureLocation.getBlockZ();

        switch (hintNumber) {
            case 1:
                // Indice général
                String direction = getCardinalDirection(x, z);
                Bukkit.broadcastMessage(ChatColor.GOLD + "💡 Indice 1: " +
                    ChatColor.YELLOW + "Le trésor se trouve vers le " + direction + "...");
                break;

            case 2:
                // Distance approximative
                Bukkit.broadcastMessage(ChatColor.GOLD + "💡 Indice 2: " +
                    ChatColor.YELLOW + "Cherchez dans un rayon de 300 blocs du spawn...");
                break;

            case 3:
                // Coordonnées partielles
                Bukkit.broadcastMessage(ChatColor.GOLD + "💡 Indice 3: " +
                    ChatColor.YELLOW + "Coordonnée X proche de " + (x / 50 * 50) + "...");
                break;

            case 4:
                // Plus précis
                Bukkit.broadcastMessage(ChatColor.GOLD + "💡 Indice 4: " +
                    ChatColor.YELLOW + "Coordonnée Z proche de " + (z / 50 * 50) + "...");
                break;

            default:
                // Coordonnées exactes après 5 indices
                Bukkit.broadcastMessage(ChatColor.GOLD + "💡 Indice Final: " +
                    ChatColor.YELLOW + "X: " + x + " Z: " + z);
                break;
        }
    }

    /**
     * Déterminer la direction cardinale
     */
    private String getCardinalDirection(int x, int z) {
        Location spawn = Bukkit.getWorlds().get(0).getSpawnLocation();
        int spawnX = spawn.getBlockX();
        int spawnZ = spawn.getBlockZ();

        int deltaX = x - spawnX;
        int deltaZ = z - spawnZ;

        if (Math.abs(deltaX) > Math.abs(deltaZ)) {
            return deltaX > 0 ? "Est" : "Ouest";
        } else {
            return deltaZ > 0 ? "Sud" : "Nord";
        }
    }

    /**
     * Arrêter l'événement
     */
    public void stop() {
        if (hintTask != null) {
            hintTask.cancel();
        }

        // Vérifier si le coffre est encore là
        if (treasureLocation != null) {
            Block block = treasureLocation.getWorld().getBlockAt(treasureLocation);
            if (block.getType() == Material.CHEST) {
                Bukkit.broadcastMessage(ChatColor.RED +
                    "⚠ Le trésor n'a pas été trouvé et disparaît...");
                block.setType(Material.AIR);

                // Nettoyer la plateforme
                for (int x = -2; x <= 2; x++) {
                    for (int z = -2; z <= 2; z++) {
                        Block platformBlock = treasureLocation.getWorld().getBlockAt(
                            treasureLocation.getBlockX() + x,
                            treasureLocation.getBlockY() - 1,
                            treasureLocation.getBlockZ() + z
                        );
                        if (platformBlock.getType() == Material.GOLD_BLOCK) {
                            platformBlock.setType(Material.GRASS_BLOCK);
                        }
                    }
                }
            }
        }

        treasureLocation = null;
    }
}
