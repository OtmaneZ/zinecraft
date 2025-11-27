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
 * Compétences de la classe ARCHER
 * 4 skills : Eagle Eye, Multi Shot, Evasion, Arrow Rain
 * 
 * @author Otmane & Copilot
 */
public class ArcherSkills {
    
    /**
     * Skill 1: Eagle Eye - Précision améliorée
     */
    public static class EagleEye extends Skill {
        
        public EagleEye() {
            super(
                "archer_eagle_eye",
                "Eagle Eye",
                "Augmente la vitesse des flèches et les dégâts pendant 15 secondes",
                25,
                20,
                5,
                SkillType.OFFENSIVE,
                SkillRarity.COMMON
            );
        }
        
        @Override
        public boolean execute(Player player) {
            // Potion effects pour simulation
            player.addPotionEffect(new PotionEffect(
                PotionEffectType.NIGHT_VISION, 300, 0, false, true
            ));
            player.addPotionEffect(new PotionEffect(
                PotionEffectType.LUCK, 300, 1, false, true
            ));
            
            // TODO: Ajouter un metadata temporaire pour augmenter les dégâts des flèches
            
            return true;
        }
        
        @Override
        public void playEffects(Player player) {
            player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0f, 1.8f);
            player.spawnParticle(Particle.CRIT, player.getEyeLocation(), 30, 0.3, 0.3, 0.3, 0.1);
        }
        
        @Override
        public ItemStack getIcon() {
            return new ItemStack(Material.SPYGLASS);
        }
    }
    
    /**
     * Skill 2: Multi Shot - Tir multiple
     */
    public static class MultiShot extends Skill {
        
        public MultiShot() {
            super(
                "archer_multi_shot",
                "Multi Shot",
                "Tire 5 flèches en éventail",
                20,
                30,
                10,
                SkillType.OFFENSIVE,
                SkillRarity.UNCOMMON
            );
        }
        
        @Override
        public boolean execute(Player player) {
            Location eyeLoc = player.getEyeLocation();
            Vector direction = eyeLoc.getDirection();
            
            // Tirer 5 flèches en éventail (-20°, -10°, 0°, +10°, +20°)
            int[] angles = {-20, -10, 0, 10, 20};
            
            for (int angle : angles) {
                Vector rotated = rotateAroundY(direction.clone(), Math.toRadians(angle));
                
                Arrow arrow = player.getWorld().spawnArrow(
                    eyeLoc,
                    rotated,
                    2.5f, // vitesse
                    0f    // spread
                );
                arrow.setShooter(player);
                arrow.setDamage(4.0);
                arrow.setCritical(true);
                
                // Particle trail
                arrow.getWorld().spawnParticle(
                    Particle.CRIT,
                    arrow.getLocation(),
                    3, 0.1, 0.1, 0.1, 0
                );
            }
            
            return true;
        }
        
        private Vector rotateAroundY(Vector vector, double radians) {
            double cos = Math.cos(radians);
            double sin = Math.sin(radians);
            double x = vector.getX() * cos + vector.getZ() * sin;
            double z = vector.getX() * (-sin) + vector.getZ() * cos;
            return vector.setX(x).setZ(z);
        }
        
        @Override
        public void playEffects(Player player) {
            player.playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0f, 0.8f);
            player.spawnParticle(Particle.FIREWORKS_SPARK, player.getLocation(), 20, 1, 0.5, 1, 0.1);
        }
        
        @Override
        public ItemStack getIcon() {
            return new ItemStack(Material.BOW);
        }
    }
    
    /**
     * Skill 3: Evasion - Esquive et dash
     */
    public static class Evasion extends Skill {
        
        public Evasion() {
            super(
                "archer_evasion",
                "Evasion",
                "Dash rapide en arrière + 5 secondes d'invisibilité",
                30,
                25,
                15,
                SkillType.MOBILITY,
                SkillRarity.RARE
            );
        }
        
        @Override
        public boolean execute(Player player) {
            // Dash en arrière
            Vector direction = player.getLocation().getDirection().multiply(-1.5);
            direction.setY(0.5); // Petit saut
            player.setVelocity(direction);
            
            // Invisibilité temporaire
            player.addPotionEffect(new PotionEffect(
                PotionEffectType.INVISIBILITY, 100, 0, false, false
            ));
            
            // Speed boost
            player.addPotionEffect(new PotionEffect(
                PotionEffectType.SPEED, 100, 1, false, true
            ));
            
            return true;
        }
        
        @Override
        public void playEffects(Player player) {
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.5f);
            player.spawnParticle(Particle.CLOUD, player.getLocation(), 30, 0.5, 0.5, 0.5, 0.1);
        }
        
        @Override
        public ItemStack getIcon() {
            return new ItemStack(Material.FEATHER);
        }
    }
    
    /**
     * Skill 4: Arrow Rain - Pluie de flèches ultime
     */
    public static class ArrowRain extends Skill {
        
        public ArrowRain() {
            super(
                "archer_arrow_rain",
                "Arrow Rain",
                "Déclenche une pluie de 30 flèches dans une zone de 10 blocs",
                60,
                50,
                25,
                SkillType.ULTIMATE,
                SkillRarity.EPIC
            );
        }
        
        @Override
        public boolean execute(Player player) {
            Location targetLoc = player.getTargetBlock(null, 30).getLocation().add(0, 1, 0);
            
            // Spawner 30 flèches qui tombent du ciel
            for (int i = 0; i < 30; i++) {
                // Position aléatoire dans un rayon de 10 blocs
                double offsetX = (Math.random() - 0.5) * 20;
                double offsetZ = (Math.random() - 0.5) * 20;
                
                Location spawnLoc = targetLoc.clone().add(offsetX, 15, offsetZ);
                
                Arrow arrow = player.getWorld().spawnArrow(
                    spawnLoc,
                    new Vector(0, -1, 0),
                    2.0f,
                    12f // spread pour effet naturel
                );
                arrow.setShooter(player);
                arrow.setDamage(6.0);
                arrow.setCritical(true);
                arrow.setFireTicks(100);
            }
            
            // Effet visuel de zone
            targetLoc.getWorld().spawnParticle(
                Particle.EXPLOSION_LARGE,
                targetLoc,
                5, 5, 1, 5, 0.05
            );
            
            return true;
        }
        
        @Override
        public void playEffects(Player player) {
            player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.5f, 1.5f);
            player.spawnParticle(Particle.FIREWORKS_SPARK, player.getLocation(), 50, 1, 1, 1, 0.2);
        }
        
        @Override
        public String getSuccessMessage() {
            return "§b⚡ §lARROW RAIN ! §b⚡";
        }
        
        @Override
        public ItemStack getIcon() {
            return new ItemStack(Material.CROSSBOW);
        }
    }
}
