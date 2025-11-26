package fr.zinecraft.core.visuals;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import fr.zinecraft.core.ZineCraftCore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Gestionnaire des effets visuels et particules
 *
 * @author Otmane & Copilot
 */
public class VisualEffectManager {

    private final ZineCraftCore plugin;
    private final Map<UUID, EffectType> activeEffects;

    public VisualEffectManager(ZineCraftCore plugin) {
        this.plugin = plugin;
        this.activeEffects = new HashMap<>();
        startEffectLoop();
    }

    /**
     * Démarrer la boucle d'effets visuels
     */
    private void startEffectLoop() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    UUID uuid = player.getUniqueId();
                    if (activeEffects.containsKey(uuid)) {
                        EffectType effect = activeEffects.get(uuid);
                        playEffect(player, effect);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L); // Toutes les 2 ticks
    }

    /**
     * Activer un effet pour un joueur
     */
    public void setEffect(Player player, EffectType effect) {
        activeEffects.put(player.getUniqueId(), effect);

        player.sendMessage(ChatColor.LIGHT_PURPLE + "✨ Effet visuel activé: " +
            effect.getDisplayName());
    }

    /**
     * Désactiver l'effet d'un joueur
     */
    public void removeEffect(Player player) {
        activeEffects.remove(player.getUniqueId());
        player.sendMessage(ChatColor.GRAY + "Effet visuel désactivé.");
    }

    /**
     * Jouer un effet visuel pour un joueur
     */
    private void playEffect(Player player, EffectType effect) {
        Location loc = player.getLocation();
        World world = player.getWorld();

        switch (effect) {
            case FIRE_AURA:
                playFireAura(world, loc);
                break;
            case ICE_AURA:
                playIceAura(world, loc);
                break;
            case MAGIC_AURA:
                playMagicAura(world, loc);
                break;
            case HOLY_AURA:
                playHolyAura(world, loc);
                break;
            case SHADOW_AURA:
                playShadowAura(world, loc);
                break;
            case NATURE_AURA:
                playNatureAura(world, loc);
                break;
            case LIGHTNING_AURA:
                playLightningAura(world, loc);
                break;
            case RAINBOW_TRAIL:
                playRainbowTrail(world, loc);
                break;
        }
    }

    // ==================== Effets Individuels ====================

    private void playFireAura(World world, Location loc) {
        world.spawnParticle(Particle.FLAME, loc.clone().add(0, 0.5, 0), 3, 0.3, 0.3, 0.3, 0.01);
        world.spawnParticle(Particle.LAVA, loc.clone().add(0, 1, 0), 1, 0.2, 0.2, 0.2, 0);
    }

    private void playIceAura(World world, Location loc) {
        world.spawnParticle(Particle.SNOWFLAKE, loc.clone().add(0, 0.5, 0), 3, 0.3, 0.3, 0.3, 0.01);
        world.spawnParticle(Particle.CLOUD, loc.clone().add(0, 1, 0), 1, 0.2, 0.2, 0.2, 0);
    }

    private void playMagicAura(World world, Location loc) {
        world.spawnParticle(Particle.ENCHANTMENT_TABLE, loc.clone().add(0, 1, 0), 5, 0.5, 0.5, 0.5, 1);
        world.spawnParticle(Particle.PORTAL, loc.clone().add(0, 0.5, 0), 2, 0.3, 0.3, 0.3, 0.5);
    }

    private void playHolyAura(World world, Location loc) {
        world.spawnParticle(Particle.END_ROD, loc.clone().add(0, 1, 0), 3, 0.3, 0.3, 0.3, 0.01);
        world.spawnParticle(Particle.TOTEM, loc.clone().add(0, 0.5, 0), 1, 0.2, 0.2, 0.2, 0);
    }

    private void playShadowAura(World world, Location loc) {
        world.spawnParticle(Particle.SMOKE_LARGE, loc.clone().add(0, 0.5, 0), 3, 0.3, 0.3, 0.3, 0.01);
        world.spawnParticle(Particle.SQUID_INK, loc.clone().add(0, 1, 0), 1, 0.2, 0.2, 0.2, 0);
    }

    private void playNatureAura(World world, Location loc) {
        world.spawnParticle(Particle.VILLAGER_HAPPY, loc.clone().add(0, 1, 0), 3, 0.3, 0.3, 0.3, 0);
        world.spawnParticle(Particle.COMPOSTER, loc.clone().add(0, 0.5, 0), 1, 0.2, 0.2, 0.2, 0);
    }

    private void playLightningAura(World world, Location loc) {
        world.spawnParticle(Particle.ELECTRIC_SPARK, loc.clone().add(0, 1, 0), 5, 0.3, 0.3, 0.3, 0.1);
        world.spawnParticle(Particle.SOUL_FIRE_FLAME, loc.clone().add(0, 0.5, 0), 2, 0.2, 0.2, 0.2, 0.01);
    }

    private void playRainbowTrail(World world, Location loc) {
        // Créer un arc-en-ciel de particules
        Color[] colors = {
            Color.RED, Color.ORANGE, Color.YELLOW,
            Color.GREEN, Color.BLUE, Color.PURPLE
        };

        for (Color color : colors) {
            world.spawnParticle(
                Particle.REDSTONE,
                loc.clone().add(Math.random() - 0.5, Math.random(), Math.random() - 0.5),
                1,
                new Particle.DustOptions(color, 1.0f)
            );
        }
    }

    /**
     * Effet de level up spectaculaire
     */
    public void playLevelUpEffect(Player player) {
        Location loc = player.getLocation();
        World world = player.getWorld();

        // Animation en spirale ascendante
        new BukkitRunnable() {
            int ticks = 0;
            final int maxTicks = 40; // 2 secondes

            @Override
            public void run() {
                if (ticks >= maxTicks) {
                    this.cancel();
                    return;
                }

                double angle = (ticks * 18) % 360; // Rotation
                double height = (double) ticks / maxTicks * 3; // Monte jusqu'à 3 blocs
                double radius = 1.5;

                double x = radius * Math.cos(Math.toRadians(angle));
                double z = radius * Math.sin(Math.toRadians(angle));

                Location particleLoc = loc.clone().add(x, height, z);

                // Particules dorées
                world.spawnParticle(Particle.END_ROD, particleLoc, 1, 0, 0, 0, 0);
                world.spawnParticle(Particle.TOTEM, particleLoc, 1, 0, 0, 0, 0);

                // Étoiles au sommet
                if (ticks == maxTicks - 1) {
                    world.spawnParticle(Particle.FIREWORKS_SPARK,
                        loc.clone().add(0, 3, 0), 50, 0.5, 0.5, 0.5, 0.2);
                    world.playSound(loc, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                    world.playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 0.5f, 1.5f);
                }

                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);

        // Message
        player.sendTitle(
            ChatColor.GOLD + "✨ LEVEL UP! ✨",
            ChatColor.YELLOW + "Félicitations!",
            10, 40, 10
        );
    }

    /**
     * Effet de mort spectaculaire
     */
    public void playDeathEffect(Location loc) {
        World world = loc.getWorld();

        // Explosion de particules
        world.spawnParticle(Particle.SOUL, loc, 50, 1, 1, 1, 0.1);
        world.spawnParticle(Particle.SMOKE_LARGE, loc, 30, 0.5, 0.5, 0.5, 0.1);
        world.spawnParticle(Particle.REDSTONE, loc, 20, 1, 1, 1,
            new Particle.DustOptions(Color.RED, 2.0f));

        world.playSound(loc, Sound.ENTITY_WITHER_DEATH, 0.5f, 1.0f);
    }

    /**
     * Effet de téléportation
     */
    public void playTeleportEffect(Location from, Location to) {
        World world = from.getWorld();

        // Effet au départ
        world.spawnParticle(Particle.PORTAL, from, 100, 0.5, 1, 0.5, 1);
        world.spawnParticle(Particle.REVERSE_PORTAL, from, 50, 0.3, 0.5, 0.3, 0.5);
        world.playSound(from, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);

        // Effet à l'arrivée (après 1 tick)
        new BukkitRunnable() {
            @Override
            public void run() {
                world.spawnParticle(Particle.PORTAL, to, 100, 0.5, 1, 0.5, 1);
                world.spawnParticle(Particle.REVERSE_PORTAL, to, 50, 0.3, 0.5, 0.3, 0.5);
                world.playSound(to, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
            }
        }.runTaskLater(plugin, 1L);
    }

    /**
     * Nettoyer les effets à la déconnexion
     */
    public void cleanup(Player player) {
        activeEffects.remove(player.getUniqueId());
    }
}
