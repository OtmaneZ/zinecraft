package fr.zinecraft.core.boss;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import fr.zinecraft.core.ZineCraftCore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Gestionnaire de boss customs
 *
 * @author Otmane & Adam
 */
public class BossManager {

    private final ZineCraftCore plugin;
    private final Map<UUID, BossData> activeBosses;

    public BossManager(ZineCraftCore plugin) {
        this.plugin = plugin;
        this.activeBosses = new HashMap<>();
    }

    /**
     * Faire apparaître un boss
     */
    public void spawnBoss(Location location, BossType type) {
        Entity boss = null;

        // EFFET SPECTACULAIRE AU SPAWN
        spawnEffects(location);

        switch (type) {
            case TITAN_ZOMBIE:
                boss = spawnTitanZombie(location);
                break;
            case DRAGON_SKELETON:
                boss = spawnDragonSkeleton(location);
                break;
            case DEMON_BLAZE:
                boss = spawnDemonBlaze(location);
                break;
            case FIRE_DRAGON:
                boss = spawnFireDragon(location);
                break;
            case ICE_GOLEM:
                boss = spawnIceGolem(location);
                break;
            case SHADOW_TITAN:
                boss = spawnShadowTitan(location);
                break;
        }

        if (boss != null) {
            activeBosses.put(boss.getUniqueId(), new BossData(boss, type));
            startBossEffects(boss, type);
            announceSpawn(type);
        }
    }

    /**
     * Effets spectaculaires au spawn du boss
     */
    private void spawnEffects(Location location) {
        World world = location.getWorld();

        // FOUDRE dramatique
        world.strikeLightningEffect(location);

        // EXPLOSION de particules
        world.spawnParticle(Particle.EXPLOSION_HUGE, location, 5, 1, 1, 1, 0);
        world.spawnParticle(Particle.CLOUD, location, 50, 2, 1, 2, 0.1);
        world.spawnParticle(Particle.SMOKE_LARGE, location, 30, 1.5, 1, 1.5, 0.05);

        // SON épique
        world.playSound(location, Sound.ENTITY_ENDER_DRAGON_GROWL, 2.0f, 0.5f);
        world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1.5f, 0.8f);
        world.playSound(location, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);
    }

    /**
     * Titan Zombie - Boss facile
     */
    private Entity spawnTitanZombie(Location location) {
        Zombie zombie = (Zombie) location.getWorld().spawnEntity(location, EntityType.ZOMBIE);
        zombie.setCustomName(ChatColor.RED + "⚔ TITAN ZOMBIE ⚔");
        zombie.setCustomNameVisible(true);
        zombie.setAdult();
        zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(200);
        zombie.setHealth(200);
        zombie.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(8);
        zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3);

        // Equipment
        zombie.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
        zombie.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        zombie.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        zombie.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
        zombie.getEquipment().setItemInMainHand(new ItemStack(Material.DIAMOND_SWORD));

        return zombie;
    }

    /**
     * Dragon Skeleton - Boss moyen
     */
    private Entity spawnDragonSkeleton(Location location) {
        Skeleton skeleton = (Skeleton) location.getWorld().spawnEntity(location, EntityType.SKELETON);
        skeleton.setCustomName(ChatColor.DARK_PURPLE + "☠ DRAGON SKELETON ☠");
        skeleton.setCustomNameVisible(true);
        skeleton.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(350);
        skeleton.setHealth(350);
        skeleton.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(12);
        skeleton.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.35);

        // Equipment
        skeleton.getEquipment().setHelmet(new ItemStack(Material.NETHERITE_HELMET));
        skeleton.getEquipment().setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
        skeleton.getEquipment().setLeggings(new ItemStack(Material.NETHERITE_LEGGINGS));
        skeleton.getEquipment().setBoots(new ItemStack(Material.NETHERITE_BOOTS));
        skeleton.getEquipment().setItemInMainHand(new ItemStack(Material.BOW));

        // Effets
        skeleton.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 0));
        skeleton.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 1));

        return skeleton;
    }

    /**
     * Demon Blaze - Boss difficile
     */
    private Entity spawnDemonBlaze(Location location) {
        Blaze blaze = (Blaze) location.getWorld().spawnEntity(location, EntityType.BLAZE);
        blaze.setCustomName(ChatColor.GOLD + "🔥 DEMON BLAZE 🔥");
        blaze.setCustomNameVisible(true);
        blaze.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(500);
        blaze.setHealth(500);
        blaze.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(15);

        // Effets
        blaze.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 1));
        blaze.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 2));

        return blaze;
    }

    /**
     * 🔥 DRAGON DE FEU - Boss ÉPIQUE
     */
    private Entity spawnFireDragon(Location location) {
        // On utilise un Wither Skeleton géant avec effets de feu
        WitherSkeleton dragon = (WitherSkeleton) location.getWorld().spawnEntity(location, EntityType.WITHER_SKELETON);
        dragon.setCustomName(ChatColor.RED + "" + ChatColor.BOLD + "🔥 DRAGON DE FEU 🔥");
        dragon.setCustomNameVisible(true);
        dragon.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(800);
        dragon.setHealth(800);
        dragon.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(20);
        dragon.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.4);
        dragon.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.8);

        // Équipement du dragon
        dragon.getEquipment().setHelmet(new ItemStack(Material.NETHERITE_HELMET));
        dragon.getEquipment().setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
        dragon.getEquipment().setLeggings(new ItemStack(Material.NETHERITE_LEGGINGS));
        dragon.getEquipment().setBoots(new ItemStack(Material.NETHERITE_BOOTS));
        dragon.getEquipment().setItemInMainHand(new ItemStack(Material.NETHERITE_SWORD));

        // Effets permanents
        dragon.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 2));
        dragon.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 2));
        dragon.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 1));
        dragon.setFireTicks(999999); // Toujours en feu!

        return dragon;
    }

    /**
     * ❄️ GOLEM DE GLACE - Boss ÉPIQUE
     */
    private Entity spawnIceGolem(Location location) {
        IronGolem golem = (IronGolem) location.getWorld().spawnEntity(location, EntityType.IRON_GOLEM);
        golem.setCustomName(ChatColor.AQUA + "" + ChatColor.BOLD + "❄️ GOLEM DE GLACE ❄️");
        golem.setCustomNameVisible(true);
        golem.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(1000);
        golem.setHealth(1000);
        golem.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(25);
        golem.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1.0);

        // Effets de glace
        golem.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 2));
        golem.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999, 1)); // Lent mais puissant
        golem.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 999999, 1));

        return golem;
    }

    /**
     * 💀 TITAN DES OMBRES - Boss LÉGENDAIRE
     */
    private Entity spawnShadowTitan(Location location) {
        Giant titan = (Giant) location.getWorld().spawnEntity(location, EntityType.GIANT);
        titan.setCustomName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "💀 TITAN DES OMBRES 💀");
        titan.setCustomNameVisible(true);
        titan.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(1500);
        titan.setHealth(1500);
        titan.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(30);
        titan.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.35);
        titan.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1.0);

        // Effets d'ombre
        titan.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 2));
        titan.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 1));
        titan.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 2));
        titan.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 999999, 1));

        return titan;
    }

    /**
     * Effets visuels du boss
     */
    private void startBossEffects(Entity boss, BossType type) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (boss.isDead() || !boss.isValid()) {
                    cancel();
                    return;
                }

                // Particules autour du boss
                Location loc = boss.getLocation().add(0, 1, 0);

                switch (type) {
                    case TITAN_ZOMBIE:
                        boss.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, loc, 5, 0.5, 0.5, 0.5, 0);
                        break;

                    case DRAGON_SKELETON:
                        boss.getWorld().spawnParticle(Particle.DRAGON_BREATH, loc, 10, 0.5, 0.5, 0.5, 0);
                        break;

                    case DEMON_BLAZE:
                        boss.getWorld().spawnParticle(Particle.FLAME, loc, 15, 0.5, 0.5, 0.5, 0);
                        break;

                    case FIRE_DRAGON:
                        // SPIRALE DE FEU épique
                        boss.getWorld().spawnParticle(Particle.FLAME, loc, 30, 1, 1, 1, 0.1);
                        boss.getWorld().spawnParticle(Particle.LAVA, loc, 10, 0.8, 0.5, 0.8, 0);
                        boss.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 15, 0.5, 0.5, 0.5, 0.05);
                        // Foudre occasionnelle pour effet dramatique
                        if (Math.random() < 0.05) {
                            boss.getWorld().strikeLightningEffect(loc);
                        }
                        break;

                    case ICE_GOLEM:
                        // AURA DE GLACE
                        boss.getWorld().spawnParticle(Particle.SNOWFLAKE, loc, 20, 1.5, 1, 1.5, 0);
                        boss.getWorld().spawnParticle(Particle.CLOUD, loc, 10, 1, 0.5, 1, 0.02);
                        boss.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc, 15, 1, 1, 1, 0.1);
                        // Freezer le sol autour
                        Location ground = boss.getLocation();
                        for (int x = -2; x <= 2; x++) {
                            for (int z = -2; z <= 2; z++) {
                                Location blockLoc = ground.clone().add(x, -1, z);
                                if (blockLoc.getBlock().getType() == Material.WATER) {
                                    blockLoc.getBlock().setType(Material.ICE);
                                }
                            }
                        }
                        break;

                    case SHADOW_TITAN:
                        // AURA D'OMBRE épique
                        boss.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 25, 1.5, 2, 1.5, 0.05);
                        boss.getWorld().spawnParticle(Particle.SOUL, loc, 15, 1, 1.5, 1, 0.05);
                        boss.getWorld().spawnParticle(Particle.PORTAL, loc, 30, 1, 1.5, 1, 0.5);
                        boss.getWorld().spawnParticle(Particle.END_ROD, loc, 10, 1.5, 2, 1.5, 0.1);
                        // Son ambient effrayant
                        if (Math.random() < 0.1) {
                            boss.getWorld().playSound(loc, Sound.ENTITY_ENDER_DRAGON_GROWL, 0.5f, 0.3f);
                        }
                        break;
                }
            }
        }.runTaskTimer(plugin, 0L, 10L);
    }

    /**
     * Annoncer l'apparition du boss
     */
    private void announceSpawn(BossType type) {
        String message = "";

        switch (type) {
            case TITAN_ZOMBIE:
                message = ChatColor.RED + "" + ChatColor.BOLD + "⚔ UN TITAN ZOMBIE EST APPARU!";
                break;
            case DRAGON_SKELETON:
                message = ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "☠ UN DRAGON SKELETON EST APPARU!";
                break;
            case DEMON_BLAZE:
                message = ChatColor.GOLD + "" + ChatColor.BOLD + "🔥 UN DEMON BLAZE EST APPARU!";
                break;
            case FIRE_DRAGON:
                message = ChatColor.RED + "" + ChatColor.BOLD + "🔥🔥🔥 LE DRAGON DE FEU EST APPARU! 🔥🔥🔥";
                break;
            case ICE_GOLEM:
                message = ChatColor.AQUA + "" + ChatColor.BOLD + "❄️❄️❄️ LE GOLEM DE GLACE EST APPARU! ❄️❄️❄️";
                break;
            case SHADOW_TITAN:
                message = ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "💀💀💀 LE TITAN DES OMBRES EST APPARU! 💀💀💀";
                break;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage("");
            player.sendMessage(message);
            player.sendMessage(ChatColor.YELLOW + "Tuez-le pour obtenir des récompenses ÉPIQUES!");
            player.sendMessage("");
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 0.5f);
            player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 0.5f, 1.0f);
        }
    }

    /**
     * Gérer la mort du boss
     */
    public void onBossDeath(Entity boss, Player killer) {
        BossData data = activeBosses.remove(boss.getUniqueId());
        if (data == null) return;

        // Récompenses
        giveRewards(killer, data.type);

        // Annonce
        announceDeath(killer, data.type);

        // Effets
        Location loc = boss.getLocation();
        boss.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, loc, 3, 0, 0, 0, 0);
        boss.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 2.0f, 0.5f);
    }

    /**
     * Donner les récompenses
     */
    private void giveRewards(Player player, BossType type) {
        Location loc = player.getLocation();
        World world = player.getWorld();

        switch (type) {
            case TITAN_ZOMBIE:
                world.dropItem(loc, new ItemStack(Material.DIAMOND, 5));
                world.dropItem(loc, new ItemStack(Material.GOLDEN_APPLE, 3));
                world.dropItem(loc, new ItemStack(Material.EXPERIENCE_BOTTLE, 10));
                player.sendMessage(ChatColor.GREEN + "⚔ Récompenses: 5 Diamants, 3 Pommes en or, 10 Bouteilles d'XP");
                break;

            case DRAGON_SKELETON:
                world.dropItem(loc, new ItemStack(Material.DIAMOND, 10));
                world.dropItem(loc, new ItemStack(Material.NETHERITE_INGOT, 2));
                world.dropItem(loc, new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1));
                world.dropItem(loc, new ItemStack(Material.EXPERIENCE_BOTTLE, 20));
                player.sendMessage(ChatColor.GREEN + "☠ Récompenses: 10 Diamants, 2 Netherite, 1 Pomme enchantée, 20 XP");
                break;

            case DEMON_BLAZE:
                world.dropItem(loc, new ItemStack(Material.DIAMOND, 20));
                world.dropItem(loc, new ItemStack(Material.NETHERITE_INGOT, 5));
                world.dropItem(loc, new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 3));
                world.dropItem(loc, new ItemStack(Material.EXPERIENCE_BOTTLE, 32));
                world.dropItem(loc, new ItemStack(Material.TOTEM_OF_UNDYING, 1));
                player.sendMessage(ChatColor.GREEN + "🔥 Récompenses: 20 Diamants, 5 Netherite, 3 Pommes enchantées, 32 XP, 1 Totem!");
                break;

            case FIRE_DRAGON:
                world.dropItem(loc, new ItemStack(Material.DIAMOND, 32));
                world.dropItem(loc, new ItemStack(Material.NETHERITE_INGOT, 10));
                world.dropItem(loc, new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 5));
                world.dropItem(loc, new ItemStack(Material.EXPERIENCE_BOTTLE, 64));
                world.dropItem(loc, new ItemStack(Material.TOTEM_OF_UNDYING, 2));
                world.dropItem(loc, new ItemStack(Material.ELYTRA, 1));
                player.sendMessage(ChatColor.GOLD + "🔥 Récompenses ÉPIQUES: 32 Diamants, 10 Netherite, 5 Pommes enchantées, 64 XP, 2 Totems, 1 Elytra!");
                break;

            case ICE_GOLEM:
                world.dropItem(loc, new ItemStack(Material.DIAMOND, 40));
                world.dropItem(loc, new ItemStack(Material.NETHERITE_INGOT, 12));
                world.dropItem(loc, new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 8));
                world.dropItem(loc, new ItemStack(Material.EXPERIENCE_BOTTLE, 64));
                world.dropItem(loc, new ItemStack(Material.TOTEM_OF_UNDYING, 3));
                world.dropItem(loc, new ItemStack(Material.TRIDENT, 1));
                player.sendMessage(ChatColor.AQUA + "❄️ Récompenses ÉPIQUES: 40 Diamants, 12 Netherite, 8 Pommes enchantées, 64 XP, 3 Totems, 1 Trident!");
                break;

            case SHADOW_TITAN:
                world.dropItem(loc, new ItemStack(Material.DIAMOND, 64));
                world.dropItem(loc, new ItemStack(Material.NETHERITE_INGOT, 20));
                world.dropItem(loc, new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 16));
                world.dropItem(loc, new ItemStack(Material.EXPERIENCE_BOTTLE, 64));
                world.dropItem(loc, new ItemStack(Material.TOTEM_OF_UNDYING, 5));
                world.dropItem(loc, new ItemStack(Material.NETHER_STAR, 3));
                world.dropItem(loc, new ItemStack(Material.DRAGON_EGG, 1));
                player.sendMessage(ChatColor.LIGHT_PURPLE + "💀 Récompenses LÉGENDAIRES: 64 Diamants, 20 Netherite, 16 Pommes enchantées, 64 XP, 5 Totems, 3 Étoiles du Nether, 1 ŒUF DE DRAGON!");
                break;
        }

        // Effets visuels de récompense
        world.spawnParticle(Particle.TOTEM, loc, 50, 1, 1, 1, 0.1);
        world.spawnParticle(Particle.FIREWORKS_SPARK, loc, 30, 0.5, 0.5, 0.5, 0.2);
        world.playSound(loc, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
    }

    /**
     * Annoncer la mort du boss
     */
    private void announceDeath(Player killer, BossType type) {
        String message = ChatColor.GOLD + "" + ChatColor.BOLD + killer.getName() +
                        ChatColor.YELLOW + " a vaincu " +
                        ChatColor.RED + "" + ChatColor.BOLD + type.getName() + "!";

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage("");
            player.sendMessage(message);
            player.sendMessage("");
            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
        }
    }

    /**
     * Vérifier si une entité est un boss
     */
    public boolean isBoss(Entity entity) {
        return activeBosses.containsKey(entity.getUniqueId());
    }

    /**
     * Types de boss
     */
    public enum BossType {
        TITAN_ZOMBIE("Titan Zombie"),
        DRAGON_SKELETON("Dragon Skeleton"),
        DEMON_BLAZE("Demon Blaze"),
        FIRE_DRAGON("Dragon de Feu"),
        ICE_GOLEM("Golem de Glace"),
        SHADOW_TITAN("Titan des Ombres");

        private final String name;

        BossType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * Données d'un boss
     */
    private static class BossData {
        Entity entity;
        BossType type;

        BossData(Entity entity, BossType type) {
            this.entity = entity;
            this.type = type;
        }
    }
}
