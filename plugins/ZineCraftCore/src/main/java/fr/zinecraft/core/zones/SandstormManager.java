package fr.zinecraft.core.zones;

import fr.zinecraft.core.ZineCraftCore;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestionnaire de la tempête de sable dans le Désert Mortel
 * Effets: Particules + Slowness + Damage périodique
 */
public class SandstormManager {

    private final ZineCraftCore plugin;
    private BukkitRunnable sandstormTask;
    private boolean isActive;

    // Zone du désert mortel
    private static final int DESERT_CENTER_X = -500;
    private static final int DESERT_CENTER_Z = 500;
    private static final int DESERT_RADIUS = 150;

    public SandstormManager(ZineCraftCore plugin) {
        this.plugin = plugin;
        this.isActive = false;
    }

    /**
     * Démarrer la tempête de sable permanente
     */
    public void startSandstorm() {
        if (isActive) {
            return;
        }

        isActive = true;

        sandstormTask = new BukkitRunnable() {
            @Override
            public void run() {
                World world = Bukkit.getWorld("world");
                if (world == null)
                    return;

                for (Player player : Bukkit.getOnlinePlayers()) {
                    Location loc = player.getLocation();

                    // Vérifier si le joueur est dans la zone du désert
                    if (isInDesertZone(loc)) {
                        applyDesertEffects(player);
                    }
                }
            }
        };

        // Exécuter toutes les 2 secondes (40 ticks)
        sandstormTask.runTaskTimer(plugin, 0L, 40L);

        plugin.getLogger().info("[SandstormManager] Tempête de sable activée dans le Désert Mortel");
    }

    /**
     * Arrêter la tempête de sable
     */
    public void stopSandstorm() {
        if (sandstormTask != null) {
            sandstormTask.cancel();
            isActive = false;
            plugin.getLogger().info("[SandstormManager] Tempête de sable désactivée");
        }
    }

    /**
     * Vérifier si une location est dans la zone du désert
     */
    private boolean isInDesertZone(Location loc) {
        int x = loc.getBlockX();
        int z = loc.getBlockZ();

        int dx = x - DESERT_CENTER_X;
        int dz = z - DESERT_CENTER_Z;

        return Math.sqrt(dx * dx + dz * dz) <= DESERT_RADIUS;
    }

    /**
     * Appliquer les effets de la tempête à un joueur
     */
    private void applyDesertEffects(Player player) {
        Location loc = player.getLocation();
        World world = loc.getWorld();
        if (world == null)
            return;

        // 1. Particules de sable
        spawnSandParticles(player);

        // 2. Effet Slowness I (5 secondes)
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0, false, false));

        // 3. Dégâts mineurs toutes les 10 secondes (25% de chance)
        if (Math.random() < 0.25) {
            player.damage(1.0);
            player.sendMessage(ChatColor.YELLOW + "⚠ La tempête de sable vous blesse!");
        }

        // 4. Son de vent
        if (Math.random() < 0.1) {
            player.playSound(loc, Sound.ITEM_ELYTRA_FLYING, 0.5f, 0.8f);
        }

        // 5. Message d'avertissement (première entrée)
        if (!player.hasMetadata("desert_warned")) {
            player.sendMessage("");
            player.sendMessage(ChatColor.GOLD + "═══════════════════════════════════");
            player.sendMessage(ChatColor.RED + "⚠ DÉSERT MORTEL ⚠");
            player.sendMessage(ChatColor.YELLOW + "Tempête de sable active!");
            player.sendMessage(ChatColor.GRAY + "• Ralentissement permanent");
            player.sendMessage(ChatColor.GRAY + "• Dégâts périodiques");
            player.sendMessage(ChatColor.GRAY + "• Visibilité réduite");
            player.sendMessage(ChatColor.GOLD + "═══════════════════════════════════");
            player.sendMessage("");

            // Marquer le joueur comme averti (temporaire)
            player.setMetadata("desert_warned", new org.bukkit.metadata.FixedMetadataValue(plugin, true));

            // Retirer le flag après 30 secondes
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.removeMetadata("desert_warned", plugin);
            }, 600L); // 30 secondes
        }
    }

    /**
     * Spawner des particules de sable autour du joueur
     */
    private void spawnSandParticles(Player player) {
        Location loc = player.getLocation();
        World world = loc.getWorld();
        if (world == null)
            return;

        // Particules de sable dans un rayon de 10 blocs
        for (int i = 0; i < 30; i++) {
            double offsetX = (Math.random() - 0.5) * 20;
            double offsetY = Math.random() * 4;
            double offsetZ = (Math.random() - 0.5) * 20;

            Location particleLoc = loc.clone().add(offsetX, offsetY, offsetZ);

            // Particules FALLING_DUST (sable qui tombe)
            world.spawnParticle(
                    Particle.FALLING_DUST,
                    particleLoc,
                    1,
                    0.2, 0.2, 0.2,
                    0.02,
                    Material.SAND.createBlockData());
        }

        // Particules supplémentaires proches
        for (int i = 0; i < 10; i++) {
            double offsetX = (Math.random() - 0.5) * 5;
            double offsetY = Math.random() * 2;
            double offsetZ = (Math.random() - 0.5) * 5;

            Location particleLoc = loc.clone().add(offsetX, offsetY, offsetZ);

            world.spawnParticle(
                    Particle.CLOUD,
                    particleLoc,
                    1,
                    0.1, 0.1, 0.1,
                    0.01);
        }
    }

    /**
     * Téléporter un joueur au désert
     */
    public void teleportToDesert(Player player) {
        World world = Bukkit.getWorld("world");
        if (world == null) {
            player.sendMessage(ChatColor.RED + "Monde introuvable!");
            return;
        }

        Location desertSpawn = new Location(world, DESERT_CENTER_X, 67, DESERT_CENTER_Z);
        player.teleport(desertSpawn);

        player.sendMessage(ChatColor.GOLD + "⚠ Téléporté au Désert Mortel!");
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
    }

    /**
     * Récupérer l'état de la tempête
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Obtenir les coordonnées du centre du désert
     */
    public Location getDesertCenter() {
        World world = Bukkit.getWorld("world");
        if (world == null)
            return null;
        return new Location(world, DESERT_CENTER_X, 67, DESERT_CENTER_Z);
    }
}
