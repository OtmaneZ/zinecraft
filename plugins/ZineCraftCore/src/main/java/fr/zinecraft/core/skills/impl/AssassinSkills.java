package fr.zinecraft.core.skills.impl;

import fr.zinecraft.core.skills.Skill;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Collection;

/**
 * Compétences de la classe ASSASSIN
 * 4 skills : Shadow Step, Backstab, Poison, Blade Storm
 * 
 * @author Otmane & Copilot
 */
public class AssassinSkills {
    
    /**
     * Skill 1: Shadow Step - Pas de l'ombre
     */
    public static class ShadowStep extends Skill {
        
        public ShadowStep() {
            super(
                "assassin_shadow_step",
                "Shadow Step",
                "Invisibilité + Speed pendant 6 secondes",
                20,
                20,
                5,
                SkillType.UTILITY,
                SkillRarity.COMMON
            );
        }
        
        @Override
        public boolean execute(Player player) {
            player.addPotionEffect(new PotionEffect(
                PotionEffectType.INVISIBILITY, 120, 0, false, false
            ));
            player.addPotionEffect(new PotionEffect(
                PotionEffectType.SPEED, 120, 2, false, true
            ));
            
            return true;
        }
        
        @Override
        public void playEffects(Player player) {
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.5f, 0.8f);
            player.spawnParticle(Particle.SMOKE_NORMAL, player.getLocation(), 50, 0.5, 1, 0.5, 0.1);
        }
        
        @Override
        public ItemStack getIcon() {
            return new ItemStack(Material.PHANTOM_MEMBRANE);
        }
    }
    
    /**
     * Skill 2: Backstab - Coup de poignard
     */
    public static class Backstab extends Skill {
        
        public Backstab() {
            super(
                "assassin_backstab",
                "Backstab",
                "Dash + frappe critique (x3 dégâts si dans le dos)",
                15,
                30,
                10,
                SkillType.OFFENSIVE,
                SkillRarity.UNCOMMON
            );
        }
        
        @Override
        public boolean execute(Player player) {
            // Dash forward
            Vector direction = player.getLocation().getDirection().multiply(2.0);
            direction.setY(0.3);
            player.setVelocity(direction);
            
            // Trouver une cible proche
            Location loc = player.getLocation();
            Collection<LivingEntity> entities = loc.getWorld()
                .getNearbyLivingEntities(loc, 3.5);
            
            for (LivingEntity entity : entities) {
                if (entity.equals(player)) continue;
                
                // Vérifier si on est dans le dos (angle > 90°)
                Vector toPlayer = player.getLocation().toVector()
                    .subtract(entity.getLocation().toVector())
                    .normalize();
                Vector entityDirection = entity.getLocation().getDirection();
                
                double angle = toPlayer.dot(entityDirection);
                boolean isBackstab = angle > 0; // Dans le dos
                
                double damage = isBackstab ? 15.0 : 8.0;
                entity.damage(damage, player);
                
                // Effets
                entity.getWorld().spawnParticle(
                    Particle.DAMAGE_INDICATOR,
                    entity.getEyeLocation(),
                    isBackstab ? 20 : 10,
                    0.3, 0.3, 0.3, 0.1
                );
                
                if (isBackstab) {
                    player.sendMessage("§c⚔ §lBACKSTAB CRITIQUE ! x3 dégâts");
                    entity.getWorld().playSound(
                        entity.getLocation(), Sound.ENTITY_PLAYER_HURT, 1.0f, 0.5f
                    );
                }
                
                break; // Une seule cible
            }
            
            return true;
        }
        
        @Override
        public void playEffects(Player player) {
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.5f);
            player.spawnParticle(Particle.SWEEP_ATTACK, player.getLocation(), 3, 0, 0, 0, 0);
        }
        
        @Override
        public ItemStack getIcon() {
            return new ItemStack(Material.IRON_SWORD);
        }
    }
    
    /**
     * Skill 3: Poison - Poison mortel
     */
    public static class Poison extends Skill {
        
        public Poison() {
            super(
                "assassin_poison",
                "Poison",
                "Empoisonne tous les ennemis dans 6 blocs (12 secondes de poison II)",
                30,
                25,
                15,
                SkillType.OFFENSIVE,
                SkillRarity.RARE
            );
        }
        
        @Override
        public boolean execute(Player player) {
            Location loc = player.getLocation();
            Collection<LivingEntity> entities = loc.getWorld()
                .getNearbyLivingEntities(loc, 6.0);
            
            int poisoned = 0;
            for (LivingEntity entity : entities) {
                if (entity.equals(player)) continue;
                
                // Poison
                entity.addPotionEffect(new PotionEffect(
                    PotionEffectType.POISON, 240, 1, false, true
                ));
                
                // Wither pour plus de dégâts
                entity.addPotionEffect(new PotionEffect(
                    PotionEffectType.WITHER, 100, 0, false, true
                ));
                
                // Slowness
                entity.addPotionEffect(new PotionEffect(
                    PotionEffectType.SLOW, 120, 0, false, true
                ));
                
                // Effets visuels
                entity.getWorld().spawnParticle(
                    Particle.SPELL_WITCH, entity.getLocation(), 30, 0.5, 1, 0.5, 0.1
                );
                
                poisoned++;
            }
            
            player.sendMessage("§2☠ " + poisoned + " ennemi(s) empoisonné(s) !");
            return true;
        }
        
        @Override
        public void playEffects(Player player) {
            player.playSound(player.getLocation(), Sound.ENTITY_WITCH_DRINK, 1.0f, 0.8f);
            player.spawnParticle(Particle.SPELL_WITCH, player.getLocation(), 100, 2, 1, 2, 0.2);
        }
        
        @Override
        public ItemStack getIcon() {
            return new ItemStack(Material.SPIDER_EYE);
        }
    }
    
    /**
     * Skill 4: Blade Storm - Tempête de lames ultime
     */
    public static class BladeStorm extends Skill {
        
        public BladeStorm() {
            super(
                "assassin_blade_storm",
                "Blade Storm",
                "Rotation rapide infligeant des dégâts massifs autour de vous pendant 5 secondes",
                60,
                50,
                25,
                SkillType.ULTIMATE,
                SkillRarity.EPIC
            );
        }
        
        @Override
        public boolean execute(Player player) {
            // Speed boost
            player.addPotionEffect(new PotionEffect(
                PotionEffectType.SPEED, 100, 2, false, true
            ));
            
            // Animation sur 5 secondes (100 ticks)
            new org.bukkit.scheduler.BukkitRunnable() {
                int ticks = 0;
                
                @Override
                public void run() {
                    if (ticks >= 100) {
                        player.sendMessage("§c⚔ Blade Storm terminé !");
                        this.cancel();
                        return;
                    }
                    
                    // Tous les 10 ticks (0.5s), infliger des dégâts
                    if (ticks % 10 == 0) {
                        Location loc = player.getLocation();
                        Collection<LivingEntity> entities = loc.getWorld()
                            .getNearbyLivingEntities(loc, 4.0);
                        
                        for (LivingEntity entity : entities) {
                            if (entity.equals(player)) continue;
                            
                            entity.damage(4.0, player);
                            
                            // Knockback léger
                            Vector direction = entity.getLocation()
                                .subtract(loc)
                                .toVector()
                                .normalize()
                                .multiply(0.5);
                            entity.setVelocity(direction);
                        }
                        
                        // Effets visuels
                        loc.getWorld().spawnParticle(
                            Particle.SWEEP_ATTACK, loc, 20, 2, 0.5, 2, 0
                        );
                        loc.getWorld().spawnParticle(
                            Particle.CRIT, loc, 30, 2, 1, 2, 0.2
                        );
                        loc.getWorld().playSound(
                            loc, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 0.5f, 1.2f
                        );
                    }
                    
                    ticks++;
                }
            }.runTaskTimer(
                org.bukkit.Bukkit.getPluginManager().getPlugin("ZineCraftCore"),
                0L, 1L
            );
            
            return true;
        }
        
        @Override
        public void playEffects(Player player) {
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 0.5f, 1.5f);
        }
        
        @Override
        public String getSuccessMessage() {
            return "§c⚔ §lBLADE STORM ! §c⚔";
        }
        
        @Override
        public ItemStack getIcon() {
            return new ItemStack(Material.NETHERITE_SWORD);
        }
    }
}
