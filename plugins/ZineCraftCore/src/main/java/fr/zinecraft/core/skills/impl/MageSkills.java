package fr.zinecraft.core.skills.impl;

import fr.zinecraft.core.skills.Skill;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collection;

/**
 * Compétences de la classe MAGE
 * 4 skills : Mana Shield, Fireball, Teleport, Meteor
 * 
 * @author Otmane & Copilot
 */
public class MageSkills {
    
    /**
     * Skill 1: Mana Shield - Bouclier magique
     */
    public static class ManaShield extends Skill {
        
        public ManaShield() {
            super(
                "mage_mana_shield",
                "Mana Shield",
                "Absorption des dégâts + régénération pendant 10 secondes",
                35,
                25,
                5,
                SkillType.DEFENSIVE,
                SkillRarity.COMMON
            );
        }
        
        @Override
        public boolean execute(Player player) {
            player.addPotionEffect(new PotionEffect(
                PotionEffectType.ABSORPTION, 200, 1, false, true
            ));
            player.addPotionEffect(new PotionEffect(
                PotionEffectType.REGENERATION, 200, 0, false, true
            ));
            
            return true;
        }
        
        @Override
        public void playEffects(Player player) {
            player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.5f);
            player.spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation(), 50, 1, 1, 1, 1);
        }
        
        @Override
        public ItemStack getIcon() {
            return new ItemStack(Material.SHIELD);
        }
    }
    
    /**
     * Skill 2: Fireball - Boule de feu
     */
    public static class Fireball extends Skill {
        
        public Fireball() {
            super(
                "mage_fireball",
                "Fireball",
                "Lance une boule de feu explosive",
                15,
                30,
                10,
                SkillType.OFFENSIVE,
                SkillRarity.UNCOMMON
            );
        }
        
        @Override
        public boolean execute(Player player) {
            Location eyeLoc = player.getEyeLocation();
            Vector direction = eyeLoc.getDirection().multiply(1.5);
            
            org.bukkit.entity.Fireball fireball = player.getWorld().spawn(
                eyeLoc.add(direction),
                org.bukkit.entity.Fireball.class
            );
            
            fireball.setShooter(player);
            fireball.setDirection(direction);
            fireball.setYield(2.0f); // Puissance de l'explosion
            fireball.setIsIncendiary(true);
            
            return true;
        }
        
        @Override
        public void playEffects(Player player) {
            player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 0.8f);
            player.spawnParticle(Particle.FLAME, player.getEyeLocation(), 20, 0.3, 0.3, 0.3, 0.05);
        }
        
        @Override
        public ItemStack getIcon() {
            return new ItemStack(Material.FIRE_CHARGE);
        }
    }
    
    /**
     * Skill 3: Teleport - Téléportation courte distance
     */
    public static class Teleport extends Skill {
        
        public Teleport() {
            super(
                "mage_teleport",
                "Teleport",
                "Téléporte jusqu'à 15 blocs dans la direction du regard",
                20,
                35,
                15,
                SkillType.MOBILITY,
                SkillRarity.RARE
            );
        }
        
        @Override
        public boolean execute(Player player) {
            Location targetLoc = player.getTargetBlock(null, 15).getLocation().add(0, 1, 0);
            
            // Vérifier que la location est sûre
            if (!isSafeLocation(targetLoc)) {
                player.sendMessage("§c❌ Destination dangereuse !");
                return false;
            }
            
            // Effets au départ
            Location startLoc = player.getLocation();
            startLoc.getWorld().spawnParticle(Particle.PORTAL, startLoc, 50, 0.5, 1, 0.5, 0.5);
            startLoc.getWorld().playSound(startLoc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
            
            // Téléportation
            player.teleport(targetLoc);
            
            // Effets à l'arrivée
            targetLoc.getWorld().spawnParticle(Particle.PORTAL, targetLoc, 50, 0.5, 1, 0.5, 0.5);
            targetLoc.getWorld().playSound(targetLoc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.2f);
            
            return true;
        }
        
        private boolean isSafeLocation(Location loc) {
            return loc.getBlock().isPassable() 
                && loc.clone().add(0, 1, 0).getBlock().isPassable();
        }
        
        @Override
        public void playEffects(Player player) {
            // Effets déjà dans execute()
        }
        
        @Override
        public ItemStack getIcon() {
            return new ItemStack(Material.ENDER_PEARL);
        }
    }
    
    /**
     * Skill 4: Meteor - Météore ultime
     */
    public static class Meteor extends Skill {
        
        public Meteor() {
            super(
                "mage_meteor",
                "Meteor",
                "Invoque un météore destructeur sur la cible",
                75,
                60,
                25,
                SkillType.ULTIMATE,
                SkillRarity.LEGENDARY
            );
        }
        
        @Override
        public boolean execute(Player player) {
            Location targetLoc = player.getTargetBlock(null, 50).getLocation();
            
            // Animation du météore qui tombe
            new BukkitRunnable() {
                int ticks = 0;
                final Location meteorLoc = targetLoc.clone().add(0, 30, 0);
                
                @Override
                public void run() {
                    if (ticks >= 30) {
                        // Impact final
                        targetLoc.getWorld().createExplosion(
                            targetLoc, 5.0f, true, false, player
                        );
                        
                        // Dégâts de zone
                        Collection<LivingEntity> entities = targetLoc.getWorld()
                            .getNearbyLivingEntities(targetLoc, 8.0);
                        
                        for (LivingEntity entity : entities) {
                            if (!entity.equals(player)) {
                                entity.damage(20.0, player);
                                entity.setFireTicks(100);
                            }
                        }
                        
                        // Effets visuels massifs
                        targetLoc.getWorld().spawnParticle(
                            Particle.EXPLOSION_HUGE, targetLoc, 3, 0, 0, 0, 0
                        );
                        targetLoc.getWorld().spawnParticle(
                            Particle.FLAME, targetLoc, 200, 4, 2, 4, 0.2
                        );
                        targetLoc.getWorld().spawnParticle(
                            Particle.LAVA, targetLoc, 100, 4, 1, 4, 0.1
                        );
                        targetLoc.getWorld().playSound(
                            targetLoc, Sound.ENTITY_GENERIC_EXPLODE, 2.0f, 0.5f
                        );
                        
                        this.cancel();
                        return;
                    }
                    
                    // Animation de chute
                    meteorLoc.subtract(0, 1, 0);
                    meteorLoc.getWorld().spawnParticle(
                        Particle.FLAME, meteorLoc, 20, 1, 1, 1, 0.1
                    );
                    meteorLoc.getWorld().spawnParticle(
                        Particle.SMOKE_NORMAL, meteorLoc, 10, 0.5, 0.5, 0.5, 0.05
                    );
                    
                    if (ticks % 5 == 0) {
                        meteorLoc.getWorld().playSound(
                            meteorLoc, Sound.ENTITY_BLAZE_SHOOT, 1.0f, 0.5f
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
            player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 0.5f, 1.5f);
        }
        
        @Override
        public String getSuccessMessage() {
            return "§c🔥 §lMÉTÉORE INVOQUÉ ! §c🔥";
        }
        
        @Override
        public ItemStack getIcon() {
            return new ItemStack(Material.MAGMA_BLOCK);
        }
    }
}
