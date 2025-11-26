package fr.zinecraft.core.events;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import fr.zinecraft.core.ZineCraftCore;

/**
 * Événement: Lune de Sang
 * Les mobs sont plus forts, plus nombreux, et l'ambiance est terrifiante
 *
 * @author Otmane & Copilot
 */
public class BloodMoonEvent {

    private final ZineCraftCore plugin;
    private BukkitTask effectTask;

    public BloodMoonEvent(ZineCraftCore plugin) {
        this.plugin = plugin;
    }

    /**
     * Démarrer l'événement
     */
    public void start() {
        World world = Bukkit.getWorlds().get(0);

        // Changer le temps en nuit
        world.setTime(13000); // Minuit
        world.setStorm(true);
        world.setThundering(true);

        // Message terrifiant
        Bukkit.broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD +
            "🌙 LA LUNE DE SANG SE LÈVE... 🌙");
        Bukkit.broadcastMessage(ChatColor.RED + "Les créatures de la nuit sont déchaînées!");

        // Effets continus
        effectTask = new BukkitRunnable() {
            @Override
            public void run() {
                applyBloodMoonEffects();
            }
        }.runTaskTimer(plugin, 0L, 20L * 10); // Toutes les 10 secondes
    }

    /**
     * Appliquer les effets de la lune de sang
     */
    private void applyBloodMoonEffects() {
        World world = Bukkit.getWorlds().get(0);

        // Buff tous les mobs hostiles
        world.getEntities().stream()
            .filter(entity -> entity instanceof Monster)
            .forEach(entity -> {
                Monster monster = (Monster) entity;

                // Augmenter les stats
                if (monster.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null) {
                    double currentHealth = monster.getHealth();
                    double maxHealth = monster.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
                    monster.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth * 1.5);
                    monster.setHealth(Math.min(currentHealth * 1.5, maxHealth * 1.5));
                }

                if (monster.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null) {
                    double damage = monster.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue();
                    monster.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(damage * 1.3);
                }

                // Effets de potion
                monster.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 0));
                monster.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 0));

                // Particules rouges
                monster.getWorld().spawnParticle(
                    Particle.REDSTONE,
                    monster.getLocation().add(0, 1, 0),
                    10,
                    0.5, 0.5, 0.5,
                    new Particle.DustOptions(Color.RED, 1.0f)
                );
            });

        // Spawn des mobs supplémentaires aléatoirement
        spawnBloodMoonCreatures();
    }

    /**
     * Faire apparaître des créatures de la lune de sang
     */
    private void spawnBloodMoonCreatures() {
        World world = Bukkit.getWorlds().get(0);

        // Choisir des positions aléatoires
        world.getPlayers().forEach(player -> {
            if (Math.random() < 0.3) { // 30% de chance
                Location spawnLoc = player.getLocation().add(
                    (Math.random() - 0.5) * 20,
                    0,
                    (Math.random() - 0.5) * 20
                );
                spawnLoc.setY(world.getHighestBlockYAt(spawnLoc));

                // Spawn un mob aléatoire
                EntityType[] hostileMobs = {
                    EntityType.ZOMBIE,
                    EntityType.SKELETON,
                    EntityType.SPIDER,
                    EntityType.CREEPER,
                    EntityType.WITCH
                };

                EntityType randomType = hostileMobs[(int) (Math.random() * hostileMobs.length)];
                Monster mob = (Monster) world.spawnEntity(spawnLoc, randomType);

                // Effets spéciaux
                mob.setGlowing(true);
                mob.setCustomName(ChatColor.DARK_RED + "Créature de la Lune de Sang");
                mob.setCustomNameVisible(true);

                // Particules au spawn
                world.spawnParticle(Particle.SOUL_FIRE_FLAME, spawnLoc, 30, 1, 1, 1, 0.1);
                world.playSound(spawnLoc, Sound.ENTITY_ENDER_DRAGON_GROWL, 0.5f, 0.5f);
            }
        });
    }

    /**
     * Arrêter l'événement
     */
    public void stop() {
        if (effectTask != null) {
            effectTask.cancel();
        }

        World world = Bukkit.getWorlds().get(0);

        // Restaurer le temps normal
        world.setTime(0); // Jour
        world.setStorm(false);
        world.setThundering(false);

        Bukkit.broadcastMessage(ChatColor.GREEN + "☀ La lune de sang s'estompe... Le calme revient.");
    }
}
