package fr.zinecraft.core.skills.impl;

import fr.zinecraft.core.skills.Skill;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;

/**
 * Compétences de la classe PALADIN
 * 4 skills : Holy Aura, Divine Strike, Healing Wave, Resurrection
 * 
 * @author Otmane & Copilot
 */
public class PaladinSkills {
    
    /**
     * Skill 1: Holy Aura - Aura sacrée
     */
    public static class HolyAura extends Skill {
        
        public HolyAura() {
            super(
                "paladin_holy_aura",
                "Holy Aura",
                "Régénération + résistance pour vous et vos alliés (10 blocs) pendant 12 secondes",
                40,
                20,
                5,
                SkillType.SUPPORT,
                SkillRarity.COMMON
            );
        }
        
        @Override
        public boolean execute(Player player) {
            Collection<Player> nearbyPlayers = player.getWorld()
                .getNearbyPlayers(player.getLocation(), 10.0);
            
            int affected = 0;
            for (Player ally : nearbyPlayers) {
                ally.addPotionEffect(new PotionEffect(
                    PotionEffectType.REGENERATION, 240, 1, false, true
                ));
                ally.addPotionEffect(new PotionEffect(
                    PotionEffectType.DAMAGE_RESISTANCE, 240, 0, false, true
                ));
                
                // Effets visuels sur l'allié
                ally.spawnParticle(Particle.END_ROD, ally.getLocation(), 20, 0.5, 1, 0.5, 0.1);
                
                if (!ally.equals(player)) {
                    ally.sendMessage("§e✟ " + player.getName() + " vous a béni avec Holy Aura !");
                    affected++;
                }
            }
            
            player.sendMessage("§a✔ " + affected + " allié(s) béni(s) !");
            return true;
        }
        
        @Override
        public void playEffects(Player player) {
            player.playSound(player.getLocation(), Sound.BLOCK_BELL_USE, 1.0f, 1.5f);
            player.spawnParticle(Particle.END_ROD, player.getLocation(), 50, 2, 1, 2, 0.1);
        }
        
        @Override
        public ItemStack getIcon() {
            return new ItemStack(Material.TOTEM_OF_UNDYING);
        }
    }
    
    /**
     * Skill 2: Divine Strike - Frappe divine
     */
    public static class DivineStrike extends Skill {
        
        public DivineStrike() {
            super(
                "paladin_divine_strike",
                "Divine Strike",
                "Frappe sacrée qui repousse et inflige 10 dégâts + brûlure aux ennemis proches",
                25,
                25,
                10,
                SkillType.OFFENSIVE,
                SkillRarity.UNCOMMON
            );
        }
        
        @Override
        public boolean execute(Player player) {
            Location loc = player.getLocation();
            Collection<LivingEntity> entities = loc.getWorld()
                .getNearbyLivingEntities(loc, 5.0);
            
            int hit = 0;
            for (LivingEntity entity : entities) {
                if (entity.equals(player)) continue;
                
                // Dégâts
                entity.damage(10.0, player);
                
                // Knockback
                org.bukkit.util.Vector direction = entity.getLocation()
                    .subtract(loc)
                    .toVector()
                    .normalize()
                    .multiply(1.5)
                    .setY(0.5);
                entity.setVelocity(direction);
                
                // Fire
                entity.setFireTicks(60);
                
                // Effets
                entity.getWorld().spawnParticle(
                    Particle.END_ROD, entity.getLocation(), 15, 0.3, 0.5, 0.3, 0.1
                );
                
                hit++;
            }
            
            player.sendMessage("§e⚔ " + hit + " ennemi(s) touché(s) par Divine Strike !");
            return true;
        }
        
        @Override
        public void playEffects(Player player) {
            player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 0.5f, 1.5f);
            player.spawnParticle(Particle.END_ROD, player.getLocation(), 100, 3, 0.5, 3, 0.2);
            player.spawnParticle(Particle.FLASH, player.getLocation(), 3, 0, 0, 0, 0);
        }
        
        @Override
        public ItemStack getIcon() {
            return new ItemStack(Material.GOLDEN_SWORD);
        }
    }
    
    /**
     * Skill 3: Healing Wave - Vague de soins
     */
    public static class HealingWave extends Skill {
        
        public HealingWave() {
            super(
                "paladin_healing_wave",
                "Healing Wave",
                "Soigne tous les alliés dans un rayon de 12 blocs (8 HP + régénération)",
                35,
                35,
                15,
                SkillType.SUPPORT,
                SkillRarity.RARE
            );
        }
        
        @Override
        public boolean execute(Player player) {
            Collection<Player> nearbyPlayers = player.getWorld()
                .getNearbyPlayers(player.getLocation(), 12.0);
            
            int healed = 0;
            for (Player ally : nearbyPlayers) {
                // Soins instantanés
                double currentHealth = ally.getHealth();
                double maxHealth = ally.getMaxHealth();
                double newHealth = Math.min(currentHealth + 8.0, maxHealth);
                ally.setHealth(newHealth);
                
                // Régénération bonus
                ally.addPotionEffect(new PotionEffect(
                    PotionEffectType.REGENERATION, 100, 2, false, true
                ));
                
                // Effets visuels
                ally.spawnParticle(Particle.HEART, ally.getLocation().add(0, 1, 0), 10, 0.5, 0.5, 0.5, 0);
                ally.playSound(ally.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 2.0f);
                
                if (!ally.equals(player)) {
                    ally.sendMessage("§a❤ " + player.getName() + " vous a soigné !");
                    healed++;
                }
            }
            
            player.sendMessage("§a✔ " + healed + " allié(s) soigné(s) !");
            return true;
        }
        
        @Override
        public void playEffects(Player player) {
            player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.5f);
            player.spawnParticle(Particle.HEART, player.getLocation(), 50, 3, 1, 3, 0.1);
        }
        
        @Override
        public ItemStack getIcon() {
            return new ItemStack(Material.GOLDEN_APPLE);
        }
    }
    
    /**
     * Skill 4: Resurrection - Résurrection ultime
     */
    public static class Resurrection extends Skill {
        
        public Resurrection() {
            super(
                "paladin_resurrection",
                "Resurrection",
                "Empêche la mort pendant 8 secondes + soins complets",
                90,
                70,
                25,
                SkillType.ULTIMATE,
                SkillRarity.LEGENDARY
            );
        }
        
        @Override
        public boolean execute(Player player) {
            // Soins complets
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
            player.setSaturation(20);
            player.setFireTicks(0);
            
            // Effets de protection
            player.addPotionEffect(new PotionEffect(
                PotionEffectType.DAMAGE_RESISTANCE, 160, 4, false, true
            ));
            player.addPotionEffect(new PotionEffect(
                PotionEffectType.REGENERATION, 160, 3, false, true
            ));
            player.addPotionEffect(new PotionEffect(
                PotionEffectType.FIRE_RESISTANCE, 160, 0, false, true
            ));
            player.addPotionEffect(new PotionEffect(
                PotionEffectType.ABSORPTION, 160, 2, false, true
            ));
            
            // Effet visuel divin
            player.getWorld().spawnParticle(
                Particle.END_ROD, player.getLocation(), 200, 2, 3, 2, 0.3
            );
            player.getWorld().spawnParticle(
                Particle.TOTEM, player.getLocation(), 100, 1, 2, 1, 0.2
            );
            
            return true;
        }
        
        @Override
        public void playEffects(Player player) {
            player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1.0f, 1.0f);
            player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 2.0f);
        }
        
        @Override
        public String getSuccessMessage() {
            return "§e✟ §lRÉSURRECTION DIVINE ! §e✟";
        }
        
        @Override
        public ItemStack getIcon() {
            return new ItemStack(Material.ENCHANTED_GOLDEN_APPLE);
        }
    }
}
