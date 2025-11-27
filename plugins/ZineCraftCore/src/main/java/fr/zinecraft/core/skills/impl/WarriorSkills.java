package fr.zinecraft.core.skills.impl;

import fr.zinecraft.core.skills.Skill;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Collection;

/**
 * Compétences de la classe WARRIOR
 * 4 skills : Iron Skin, Power Strike, Battle Cry, Berserker
 * 
 * @author Otmane & Copilot
 */
public class WarriorSkills {
    
    /**
     * Skill 1: Iron Skin - Réduction de dégâts temporaire
     */
    public static class IronSkin extends Skill {
        
        public IronSkin() {
            super(
                "warrior_iron_skin",
                "Iron Skin",
                "Réduit les dégâts reçus de 30% pendant 10 secondes",
                30, // cooldown
                20, // mana cost
                5,  // niveau requis
                SkillType.DEFENSIVE,
                SkillRarity.COMMON
            );
        }
        
        @Override
        public boolean execute(Player player) {
            player.addPotionEffect(new PotionEffect(
                PotionEffectType.DAMAGE_RESISTANCE,
                200, // 10 secondes
                1,   // Niveau 2
                false,
                true
            ));
            
            return true;
        }
        
        @Override
        public void playEffects(Player player) {
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.5f);
            player.spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation(), 30, 0.5, 1, 0.5, 0.1);
        }
        
        @Override
        public ItemStack getIcon() {
            return new ItemStack(Material.IRON_CHESTPLATE);
        }
    }
    
    /**
     * Skill 2: Power Strike - Coup puissant qui repousse
     */
    public static class PowerStrike extends Skill {
        
        public PowerStrike() {
            super(
                "warrior_power_strike",
                "Power Strike",
                "Repousse violemment les ennemis proches et inflige +50% dégâts",
                15,
                25,
                10,
                SkillType.OFFENSIVE,
                SkillRarity.UNCOMMON
            );
        }
        
        @Override
        public boolean execute(Player player) {
            Location loc = player.getLocation();
            Collection<LivingEntity> nearby = loc.getWorld()
                .getNearbyLivingEntities(loc, 4.0);
            
            int hitCount = 0;
            
            for (LivingEntity entity : nearby) {
                if (entity.equals(player)) continue;
                
                // Knockback
                Vector direction = entity.getLocation().toVector()
                    .subtract(player.getLocation().toVector())
                    .normalize()
                    .multiply(2.0);
                entity.setVelocity(direction);
                
                // Dégâts
                entity.damage(8.0, player);
                
                hitCount++;
            }
            
            if (hitCount == 0) {
                player.sendMessage("§c❌ Aucune cible à proximité !");
                return false;
            }
            
            return true;
        }
        
        @Override
        public void playEffects(Player player) {
            player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 1.2f);
            player.spawnParticle(Particle.EXPLOSION_LARGE, player.getLocation(), 5, 2, 0.5, 2, 0.05);
            player.spawnParticle(Particle.SWEEP_ATTACK, player.getLocation(), 20, 2, 1, 2, 0.1);
        }
        
        @Override
        public ItemStack getIcon() {
            return new ItemStack(Material.IRON_SWORD);
        }
    }
    
    /**
     * Skill 3: Battle Cry - Buff d'équipe
     */
    public static class BattleCry extends Skill {
        
        public BattleCry() {
            super(
                "warrior_battle_cry",
                "Battle Cry",
                "Donne Force II et Vitesse I aux alliés proches pendant 15 secondes",
                45,
                30,
                15,
                SkillType.SUPPORT,
                SkillRarity.RARE
            );
        }
        
        @Override
        public boolean execute(Player player) {
            Location loc = player.getLocation();
            Collection<Player> nearby = loc.getWorld()
                .getNearbyPlayers(loc, 10.0);
            
            int buffedCount = 0;
            
            for (Player ally : nearby) {
                ally.addPotionEffect(new PotionEffect(
                    PotionEffectType.INCREASE_DAMAGE, 300, 1, false, true
                ));
                ally.addPotionEffect(new PotionEffect(
                    PotionEffectType.SPEED, 300, 0, false, true
                ));
                
                ally.sendMessage("§6⚔ " + player.getName() + " §ea utilisé Battle Cry !");
                ally.playSound(ally.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 0.5f, 1.8f);
                
                buffedCount++;
            }
            
            return buffedCount > 0;
        }
        
        @Override
        public void playEffects(Player player) {
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 0.8f);
            player.spawnParticle(Particle.CRIT_MAGIC, player.getLocation(), 50, 5, 1, 5, 0.2);
        }
        
        @Override
        public ItemStack getIcon() {
            return new ItemStack(Material.BELL);
        }
    }
    
    /**
     * Skill 4: Berserker - Mode rage ultime
     */
    public static class Berserker extends Skill {
        
        public Berserker() {
            super(
                "warrior_berserker",
                "Berserker",
                "Mode rage : Force III, Vitesse II, Regen II pendant 12 secondes",
                90,
                50,
                25,
                SkillType.ULTIMATE,
                SkillRarity.EPIC
            );
        }
        
        @Override
        public boolean execute(Player player) {
            // Effets de buff massifs
            player.addPotionEffect(new PotionEffect(
                PotionEffectType.INCREASE_DAMAGE, 240, 2, false, true
            ));
            player.addPotionEffect(new PotionEffect(
                PotionEffectType.SPEED, 240, 1, false, true
            ));
            player.addPotionEffect(new PotionEffect(
                PotionEffectType.REGENERATION, 240, 1, false, true
            ));
            player.addPotionEffect(new PotionEffect(
                PotionEffectType.FIRE_RESISTANCE, 240, 0, false, true
            ));
            
            // Heal partiel
            double currentHealth = player.getHealth();
            double maxHealth = player.getMaxHealth();
            player.setHealth(Math.min(maxHealth, currentHealth + 10));
            
            return true;
        }
        
        @Override
        public void playEffects(Player player) {
            player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 0.5f, 1.5f);
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 0.5f);
            player.spawnParticle(Particle.FLAME, player.getLocation(), 100, 1, 2, 1, 0.1);
            player.spawnParticle(Particle.VILLAGER_ANGRY, player.getLocation().add(0, 2, 0), 20, 0.5, 0.5, 0.5, 0);
        }
        
        @Override
        public String getSuccessMessage() {
            return "§c⚡ §lMODE BERSERKER ACTIVÉ ! §c⚡";
        }
        
        @Override
        public ItemStack getIcon() {
            return new ItemStack(Material.NETHERITE_AXE);
        }
    }
}
