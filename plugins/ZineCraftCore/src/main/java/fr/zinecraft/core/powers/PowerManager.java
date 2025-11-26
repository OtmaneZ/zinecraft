package fr.zinecraft.core.powers;

import fr.zinecraft.core.ZineCraftCore;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * Gestionnaire des super-pouvoirs
 * 
 * @author Otmane & Adam
 */
public class PowerManager {
    
    private final ZineCraftCore plugin;
    private final Map<UUID, Map<PowerType, Long>> cooldowns;
    private final Map<UUID, Set<PowerType>> activePowers;
    private final Map<UUID, BukkitRunnable> activeEffects;
    
    public PowerManager(ZineCraftCore plugin) {
        this.plugin = plugin;
        this.cooldowns = new HashMap<>();
        this.activePowers = new HashMap<>();
        this.activeEffects = new HashMap<>();
    }
    
    /**
     * Utiliser un pouvoir
     */
    public void usePower(Player player, PowerType power) {
        // Vérifier le cooldown
        if (isOnCooldown(player, power)) {
            long timeLeft = getCooldownTimeLeft(player, power);
            player.sendMessage(ChatColor.RED + "⏳ Cooldown: " + timeLeft + " secondes");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5f, 1.0f);
            return;
        }
        
        // Activer le pouvoir
        Location loc = player.getLocation();
        
        // Effets visuels et sonores
        loc.getWorld().spawnParticle(power.getParticle(), loc, 50, 1, 1, 1, 0.1);
        loc.getWorld().playSound(loc, power.getSound(), 1.0f, 1.0f);
        
        // Message
        player.sendMessage("");
        player.sendMessage(power.getColor() + "✨ " + power.getDisplayName() + " activé!");
        player.sendMessage("");
        
        // Exécuter le pouvoir
        switch (power) {
            case SUPER_SPEED:
                activateSuperSpeed(player);
                break;
            case SUPER_JUMP:
                activateSuperJump(player);
                break;
            case FIREBALL:
                activateFireball(player);
                break;
            case FREEZE_ZONE:
                activateFreezeZone(player);
                break;
            case INVISIBILITY:
                activateInvisibility(player);
                break;
            case TORNADO:
                activateTornado(player);
                break;
            case LIGHTNING_STRIKE:
                activateLightningStrike(player);
                break;
            case SHIELD:
                activateShield(player);
                break;
            case TELEPORT:
                activateTeleport(player);
                break;
            case HEAL_AURA:
                activateHealAura(player);
                break;
            case FLIGHT:
                activateFlight(player);
                break;
            case EARTH_WALL:
                activateEarthWall(player);
                break;
        }
        
        // Mettre le cooldown
        setCooldown(player, power);
        
        // Marquer comme actif si durée > 0
        if (power.getDuration() > 0) {
            activePowers.computeIfAbsent(player.getUniqueId(), k -> new HashSet<>()).add(power);
        }
    }
    
    /**
     * Super Vitesse
     */
    private void activateSuperSpeed(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 4)); // Speed 5 pendant 5 secondes
        
        // Traînée de particules
        BukkitRunnable effect = new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks++ >= 100 || !player.isOnline()) {
                    cancel();
                    activePowers.getOrDefault(player.getUniqueId(), new HashSet<>()).remove(PowerType.SUPER_SPEED);
                    return;
                }
                Location loc = player.getLocation();
                loc.getWorld().spawnParticle(Particle.CRIT, loc, 10, 0.3, 0.1, 0.3, 0);
                loc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc, 5, 0.2, 0.1, 0.2, 0);
            }
        };
        effect.runTaskTimer(plugin, 0L, 1L);
        activeEffects.put(player.getUniqueId(), effect);
    }
    
    /**
     * Super Saut
     */
    private void activateSuperJump(Player player) {
        Vector velocity = player.getVelocity();
        velocity.setY(3.0); // Saut de 20 blocs
        player.setVelocity(velocity);
        
        // Pas de dégâts de chute temporaire
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 100, 0));
        
        // Effets
        Location loc = player.getLocation();
        loc.getWorld().spawnParticle(Particle.CLOUD, loc, 50, 1, 0.1, 1, 0.1);
        loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 3, 0, 0, 0, 0);
    }
    
    /**
     * Boule de Feu
     */
    private void activateFireball(Player player) {
        Fireball fireball = player.launchProjectile(Fireball.class);
        fireball.setYield(2.0f); // Explosion de taille moyenne
        fireball.setIsIncendiary(true);
        fireball.setShooter(player);
        
        // Traînée de feu
        new BukkitRunnable() {
            @Override
            public void run() {
                if (fireball.isDead() || !fireball.isValid()) {
                    cancel();
                    return;
                }
                Location loc = fireball.getLocation();
                loc.getWorld().spawnParticle(Particle.FLAME, loc, 20, 0.5, 0.5, 0.5, 0.05);
                loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 10, 0.3, 0.3, 0.3, 0.01);
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    /**
     * Zone Glaciale
     */
    private void activateFreezeZone(Player player) {
        Location center = player.getLocation();
        
        // Effets visuels massifs
        center.getWorld().spawnParticle(Particle.SNOWFLAKE, center, 500, 10, 3, 10, 0.1);
        center.getWorld().spawnParticle(Particle.CLOUD, center, 200, 10, 3, 10, 0.05);
        center.getWorld().playSound(center, Sound.BLOCK_GLASS_BREAK, 2.0f, 0.5f);
        
        // Freeze tous les ennemis dans 10 blocs
        for (Entity entity : center.getWorld().getNearbyEntities(center, 10, 10, 10)) {
            if (entity instanceof LivingEntity && entity != player) {
                LivingEntity target = (LivingEntity) entity;
                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 10));
                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 200, 10));
                target.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 200, 128)); // Empêche de sauter
                target.setFreezeTicks(200);
            }
        }
        
        // Transformer l'eau en glace
        for (int x = -10; x <= 10; x++) {
            for (int y = -3; y <= 3; y++) {
                for (int z = -10; z <= 10; z++) {
                    Block block = center.clone().add(x, y, z).getBlock();
                    if (block.getType() == Material.WATER) {
                        block.setType(Material.ICE);
                    }
                }
            }
        }
    }
    
    /**
     * Invisibilité
     */
    private void activateInvisibility(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 600, 0)); // 30 secondes
        
        // Effets visuels durant l'invisibilité
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks++ >= 600 || !player.isOnline()) {
                    cancel();
                    activePowers.getOrDefault(player.getUniqueId(), new HashSet<>()).remove(PowerType.INVISIBILITY);
                    player.sendMessage(ChatColor.GRAY + "👻 Invisibilité terminée");
                    return;
                }
                if (ticks % 20 == 0) { // Toutes les secondes
                    Location loc = player.getLocation();
                    loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 5, 0.3, 0.5, 0.3, 0.01);
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    /**
     * Tornade
     */
    private void activateTornado(Player player) {
        Location center = player.getLocation().add(player.getLocation().getDirection().multiply(3));
        
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks++ >= 160) { // 8 secondes
                    cancel();
                    activePowers.getOrDefault(player.getUniqueId(), new HashSet<>()).remove(PowerType.TORNADO);
                    return;
                }
                
                // Animation de tornade
                for (int i = 0; i < 10; i++) {
                    double angle = (ticks * 30 + i * 36) * Math.PI / 180;
                    double radius = 3 + Math.sin(ticks * 0.1) * 0.5;
                    double x = Math.cos(angle) * radius;
                    double z = Math.sin(angle) * radius;
                    double y = (ticks % 40) * 0.2;
                    
                    Location particleLoc = center.clone().add(x, y, z);
                    center.getWorld().spawnParticle(Particle.CLOUD, particleLoc, 1, 0, 0, 0, 0);
                    center.getWorld().spawnParticle(Particle.CRIT, particleLoc, 2, 0, 0, 0, 0);
                }
                
                // Aspirer les entités
                for (Entity entity : center.getWorld().getNearbyEntities(center, 8, 8, 8)) {
                    if (entity instanceof LivingEntity && entity != player) {
                        Vector direction = center.toVector().subtract(entity.getLocation().toVector()).normalize();
                        direction.setY(0.3);
                        entity.setVelocity(direction.multiply(0.8));
                        
                        // Dégâts si proche du centre
                        if (entity.getLocation().distance(center) < 2) {
                            ((LivingEntity) entity).damage(2);
                        }
                    }
                }
                
                // Son périodique
                if (ticks % 10 == 0) {
                    center.getWorld().playSound(center, Sound.ENTITY_WITHER_SHOOT, 0.3f, 1.5f);
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    /**
     * Éclair Ciblé
     */
    private void activateLightningStrike(Player player) {
        Block target = player.getTargetBlock(null, 50);
        if (target != null) {
            Location strikeLoc = target.getLocation();
            strikeLoc.getWorld().strikeLightning(strikeLoc);
            
            // Dégâts en zone
            for (Entity entity : strikeLoc.getWorld().getNearbyEntities(strikeLoc, 3, 3, 3)) {
                if (entity instanceof LivingEntity && entity != player) {
                    ((LivingEntity) entity).damage(30);
                }
            }
        }
    }
    
    /**
     * Bouclier
     */
    private void activateShield(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 4)); // Résistance 5
        player.setInvulnerable(true);
        
        // Aura de protection visuelle
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks++ >= 100 || !player.isOnline()) {
                    player.setInvulnerable(false);
                    cancel();
                    activePowers.getOrDefault(player.getUniqueId(), new HashSet<>()).remove(PowerType.SHIELD);
                    player.sendMessage(ChatColor.BLUE + "🛡️ Bouclier désactivé");
                    return;
                }
                
                // Sphère de particules
                for (int i = 0; i < 20; i++) {
                    double angle1 = Math.random() * 2 * Math.PI;
                    double angle2 = Math.random() * 2 * Math.PI;
                    double x = Math.cos(angle1) * Math.sin(angle2) * 2;
                    double y = Math.sin(angle1) * 2;
                    double z = Math.cos(angle1) * Math.cos(angle2) * 2;
                    
                    Location particleLoc = player.getLocation().add(x, y + 1, z);
                    player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, particleLoc, 1, 0, 0, 0, 0);
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    /**
     * Téléportation
     */
    private void activateTeleport(Player player) {
        Block target = player.getTargetBlock(null, 50);
        if (target != null) {
            Location teleportLoc = target.getLocation().add(0, 1, 0);
            Location oldLoc = player.getLocation();
            
            // Effets au départ
            oldLoc.getWorld().spawnParticle(Particle.PORTAL, oldLoc, 100, 0.5, 1, 0.5, 0.5);
            oldLoc.getWorld().playSound(oldLoc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.5f);
            
            // Téléporter
            player.teleport(teleportLoc);
            
            // Effets à l'arrivée
            teleportLoc.getWorld().spawnParticle(Particle.PORTAL, teleportLoc, 100, 0.5, 1, 0.5, 0.5);
            teleportLoc.getWorld().playSound(teleportLoc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.5f);
        }
    }
    
    /**
     * Aura de Soin
     */
    private void activateHealAura(Player player) {
        Location center = player.getLocation();
        
        // Effets visuels
        center.getWorld().spawnParticle(Particle.HEART, center, 100, 8, 3, 8, 0);
        center.getWorld().spawnParticle(Particle.TOTEM, center, 50, 8, 3, 8, 0.1);
        center.getWorld().playSound(center, Sound.BLOCK_BEACON_POWER_SELECT, 1.0f, 1.5f);
        
        // Soigner en zone
        for (Entity entity : center.getWorld().getNearbyEntities(center, 8, 8, 8)) {
            if (entity instanceof Player) {
                Player target = (Player) entity;
                double maxHealth = target.getMaxHealth();
                target.setHealth(Math.min(target.getHealth() + 10, maxHealth));
                target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
                
                if (target != player) {
                    target.sendMessage(ChatColor.GREEN + "💚 " + player.getName() + " vous a soigné!");
                }
            }
        }
    }
    
    /**
     * Vol
     */
    private void activateFlight(Player player) {
        player.setAllowFlight(true);
        player.setFlying(true);
        
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks++ >= 300 || !player.isOnline()) { // 15 secondes
                    if (!player.getGameMode().equals(GameMode.CREATIVE)) {
                        player.setAllowFlight(false);
                        player.setFlying(false);
                    }
                    cancel();
                    activePowers.getOrDefault(player.getUniqueId(), new HashSet<>()).remove(PowerType.FLIGHT);
                    player.sendMessage(ChatColor.WHITE + "🕊️ Vol terminé");
                    return;
                }
                
                // Particules de vol
                Location loc = player.getLocation();
                loc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc, 10, 0.5, 0.2, 0.5, 0.05);
                loc.getWorld().spawnParticle(Particle.CLOUD, loc, 5, 0.3, 0.1, 0.3, 0.01);
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    /**
     * Mur de Terre
     */
    private void activateEarthWall(Player player) {
        Location start = player.getLocation().add(player.getLocation().getDirection().multiply(2));
        Vector direction = player.getLocation().getDirection();
        Vector right = direction.clone().rotateAroundY(Math.PI / 2).normalize();
        
        List<Block> wallBlocks = new ArrayList<>();
        
        // Créer un mur 5x5
        for (int x = -2; x <= 2; x++) {
            for (int y = 0; y < 5; y++) {
                Location blockLoc = start.clone().add(right.clone().multiply(x)).add(0, y, 0);
                Block block = blockLoc.getBlock();
                if (block.getType() == Material.AIR) {
                    block.setType(Material.STONE);
                    wallBlocks.add(block);
                }
            }
        }
        
        // Effets
        start.getWorld().spawnParticle(Particle.BLOCK_CRACK, start, 100, 2, 2, 2, 0, Material.STONE.createBlockData());
        start.getWorld().playSound(start, Sound.BLOCK_STONE_PLACE, 2.0f, 0.8f);
        
        // Retirer le mur après 20 secondes
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Block block : wallBlocks) {
                    if (block.getType() == Material.STONE) {
                        block.setType(Material.AIR);
                        block.getWorld().spawnParticle(Particle.BLOCK_DUST, block.getLocation(), 10, 0.5, 0.5, 0.5, 0, Material.STONE.createBlockData());
                    }
                }
            }
        }.runTaskLater(plugin, 400L); // 20 secondes
    }
    
    /**
     * Vérifier le cooldown
     */
    private boolean isOnCooldown(Player player, PowerType power) {
        Map<PowerType, Long> playerCooldowns = cooldowns.get(player.getUniqueId());
        if (playerCooldowns == null || !playerCooldowns.containsKey(power)) {
            return false;
        }
        
        long timeElapsed = (System.currentTimeMillis() - playerCooldowns.get(power)) / 1000;
        return timeElapsed < power.getCooldown();
    }
    
    /**
     * Temps restant du cooldown
     */
    private long getCooldownTimeLeft(Player player, PowerType power) {
        Map<PowerType, Long> playerCooldowns = cooldowns.get(player.getUniqueId());
        if (playerCooldowns == null || !playerCooldowns.containsKey(power)) {
            return 0;
        }
        
        long timeElapsed = (System.currentTimeMillis() - playerCooldowns.get(power)) / 1000;
        return power.getCooldown() - timeElapsed;
    }
    
    /**
     * Mettre un cooldown
     */
    private void setCooldown(Player player, PowerType power) {
        cooldowns.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>()).put(power, System.currentTimeMillis());
    }
    
    /**
     * Nettoyer les effets d'un joueur
     */
    public void cleanup(Player player) {
        UUID uuid = player.getUniqueId();
        
        if (activeEffects.containsKey(uuid)) {
            activeEffects.get(uuid).cancel();
            activeEffects.remove(uuid);
        }
        
        activePowers.remove(uuid);
        cooldowns.remove(uuid);
    }
}
