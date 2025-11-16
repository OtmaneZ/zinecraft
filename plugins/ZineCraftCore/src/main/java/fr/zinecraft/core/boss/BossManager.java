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
        }
        
        if (boss != null) {
            activeBosses.put(boss.getUniqueId(), new BossData(boss, type));
            startBossEffects(boss, type);
            announceSpawn(type);
        }
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
        }
        
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage("");
            player.sendMessage(message);
            player.sendMessage(ChatColor.YELLOW + "Tuez-le pour obtenir des récompenses!");
            player.sendMessage("");
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 0.5f);
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
        }
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
        DEMON_BLAZE("Demon Blaze");
        
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
