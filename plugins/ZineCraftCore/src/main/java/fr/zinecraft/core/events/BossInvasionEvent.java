package fr.zinecraft.core.events;

import org.bukkit.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import fr.zinecraft.core.ZineCraftCore;
import fr.zinecraft.core.boss.BossManager;

import java.util.Random;

/**
 * Événement: Invasion de Boss
 * Plusieurs boss apparaissent simultanément dans le monde
 *
 * @author Otmane & Copilot
 */
public class BossInvasionEvent {

    private final ZineCraftCore plugin;
    private BukkitTask spawnTask;
    private final Random random;
    private int bossesSpawned;

    public BossInvasionEvent(ZineCraftCore plugin) {
        this.plugin = plugin;
        this.random = new Random();
        this.bossesSpawned = 0;
    }

    /**
     * Démarrer l'événement
     */
    public void start() {
        bossesSpawned = 0;

        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD +
            "⚔ INVASION DE BOSS! ⚔");
        Bukkit.broadcastMessage(ChatColor.RED +
            "Des boss apparaissent partout dans le monde!");

        // Faire apparaître des boss toutes les 2 minutes
        spawnTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (bossesSpawned < 5) { // Maximum 5 boss
                    spawnRandomBoss();
                    bossesSpawned++;
                }
            }
        }.runTaskTimer(plugin, 0L, 20L * 120); // Toutes les 2 minutes
    }

    /**
     * Faire apparaître un boss aléatoire
     */
    private void spawnRandomBoss() {
        World world = Bukkit.getWorlds().get(0);
        BossManager bossManager = plugin.getBossManager();

        // Position aléatoire
        Location spawn = world.getSpawnLocation();
        int x = spawn.getBlockX() + random.nextInt(400) - 200;
        int z = spawn.getBlockZ() + random.nextInt(400) - 200;
        int y = world.getHighestBlockYAt(x, z) + 1;

        Location bossLocation = new Location(world, x, y, z);

        // Choisir un boss aléatoire
        BossManager.BossType[] bossTypes = BossManager.BossType.values();
        BossManager.BossType randomBoss = bossTypes[random.nextInt(bossTypes.length)];        // Faire apparaître le boss
        bossManager.spawnBoss(bossLocation, randomBoss);

        // Annoncer
        Bukkit.broadcastMessage(ChatColor.RED + "⚔ Un " + randomBoss.getName() +
            ChatColor.RED + " est apparu en X:" + x + " Z:" + z + "!");
    }

    /**
     * Arrêter l'événement
     */
    public void stop() {
        if (spawnTask != null) {
            spawnTask.cancel();
        }

        Bukkit.broadcastMessage(ChatColor.GREEN +
            "✔ L'invasion de boss est repoussée!");

        bossesSpawned = 0;
    }
}
