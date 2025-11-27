package fr.zinecraft.core.skills.impl;

import fr.zinecraft.core.skills.Skill;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;

/**
 * Compétences de la classe DRUID
 * 4 skills : Nature Heal, Vine Trap, Wild Shape, Force of Nature
 * 
 * @author Otmane & Copilot
 */
public class DruidSkills {
    
    /**
     * Skill 1: Nature Heal - Soins naturels
     */
    public static class NatureHeal extends Skill {
        
        public NatureHeal() {
            super(
                "druid_nature_heal",
                "Nature Heal",
                "Soigne 6 HP + régénération II pendant 10 secondes",
                25,
                20,
                5,
                SkillType.SUPPORT,
                SkillRarity.COMMON
            );
        }
        
        @Override
        public boolean execute(Player player) {
            // Soins instantanés
            double currentHealth = player.getHealth();
            double maxHealth = player.getMaxHealth();
            player.setHealth(Math.min(currentHealth + 6.0, maxHealth));
            
            // Régénération
            player.addPotionEffect(new PotionEffect(
                PotionEffectType.REGENERATION, 200, 1, false, true
            ));
            
            return true;
        }
        
        @Override
        public void playEffects(Player player) {
            player.playSound(player.getLocation(), Sound.BLOCK_GRASS_BREAK, 1.0f, 1.5f);
            player.spawnParticle(Particle.VILLAGER_HAPPY, player.getLocation(), 30, 0.5, 1, 0.5, 0);
        }
        
        @Override
        public ItemStack getIcon() {
            return new ItemStack(Material.GLOW_BERRIES);
        }
    }
    
    /**
     * Skill 2: Vine Trap - Piège de lianes
     */
    public static class VineTrap extends Skill {
        
        public VineTrap() {
            super(
                "druid_vine_trap",
                "Vine Trap",
                "Immobilise les ennemis proches (5 blocs) pendant 5 secondes",
                20,
                25,
                10,
                SkillType.UTILITY,
                SkillRarity.UNCOMMON
            );
        }
        
        @Override
        public boolean execute(Player player) {
            Location loc = player.getLocation();
            Collection<LivingEntity> entities = loc.getWorld()
                .getNearbyLivingEntities(loc, 5.0);
            
            int trapped = 0;
            for (LivingEntity entity : entities) {
                if (entity.equals(player)) continue;
                
                // Immobilisation
                entity.addPotionEffect(new PotionEffect(
                    PotionEffectType.SLOW, 100, 10, false, true
                ));
                entity.addPotionEffect(new PotionEffect(
                    PotionEffectType.JUMP, 100, 200, false, true // Jump négatif
                ));
                
                // Effets visuels - lianes
                Location entityLoc = entity.getLocation();
                entityLoc.getWorld().spawnParticle(
                    Particle.BLOCK_CRACK, entityLoc, 50, 0.5, 0.5, 0.5, 0,
                    Material.VINE.createBlockData()
                );
                
                trapped++;
            }
            
            player.sendMessage("§2🌿 " + trapped + " ennemi(s) piégé(s) !");
            return true;
        }
        
        @Override
        public void playEffects(Player player) {
            player.playSound(player.getLocation(), Sound.BLOCK_GRASS_PLACE, 1.0f, 0.8f);
            player.spawnParticle(Particle.BLOCK_CRACK, player.getLocation(), 100, 2, 0.5, 2, 0,
                Material.VINE.createBlockData());
        }
        
        @Override
        public ItemStack getIcon() {
            return new ItemStack(Material.VINE);
        }
    }
    
    /**
     * Skill 3: Wild Shape - Transformation animale
     */
    public static class WildShape extends Skill {
        
        public WildShape() {
            super(
                "druid_wild_shape",
                "Wild Shape",
                "Transformation en loup : Speed III + Jump Boost II + Force pendant 15 secondes",
                35,
                30,
                15,
                SkillType.UTILITY,
                SkillRarity.RARE
            );
        }
        
        @Override
        public boolean execute(Player player) {
            // Buffs de loup
            player.addPotionEffect(new PotionEffect(
                PotionEffectType.SPEED, 300, 2, false, true
            ));
            player.addPotionEffect(new PotionEffect(
                PotionEffectType.JUMP, 300, 1, false, true
            ));
            player.addPotionEffect(new PotionEffect(
                PotionEffectType.INCREASE_DAMAGE, 300, 0, false, true
            ));
            player.addPotionEffect(new PotionEffect(
                PotionEffectType.NIGHT_VISION, 300, 0, false, true
            ));
            
            // Spawner des loups temporaires autour pour l'effet visuel
            for (int i = 0; i < 3; i++) {
                Wolf wolf = (Wolf) player.getWorld().spawnEntity(
                    player.getLocation(),
                    EntityType.WOLF
                );
                wolf.setOwner(player);
                wolf.setAngry(false);
                
                // Disparaît après 15 secondes
                new org.bukkit.scheduler.BukkitRunnable() {
                    @Override
                    public void run() {
                        wolf.remove();
                    }
                }.runTaskLater(
                    org.bukkit.Bukkit.getPluginManager().getPlugin("ZineCraftCore"),
                    300L
                );
            }
            
            return true;
        }
        
        @Override
        public void playEffects(Player player) {
            player.playSound(player.getLocation(), Sound.ENTITY_WOLF_HOWL, 1.0f, 1.0f);
            player.spawnParticle(Particle.CLOUD, player.getLocation(), 50, 1, 1, 1, 0.1);
        }
        
        @Override
        public ItemStack getIcon() {
            return new ItemStack(Material.WOLF_SPAWN_EGG);
        }
    }
    
    /**
     * Skill 4: Force of Nature - Force de la nature ultime
     */
    public static class ForceOfNature extends Skill {
        
        public ForceOfNature() {
            super(
                "druid_force_of_nature",
                "Force of Nature",
                "Invoque la colère de la nature : dégâts de zone + soins + invocation d'Iron Golem",
                70,
                55,
                25,
                SkillType.ULTIMATE,
                SkillRarity.LEGENDARY
            );
        }
        
        @Override
        public boolean execute(Player player) {
            Location loc = player.getLocation();
            
            // Dégâts de zone
            Collection<LivingEntity> entities = loc.getWorld()
                .getNearbyLivingEntities(loc, 10.0);
            
            for (LivingEntity entity : entities) {
                if (entity.equals(player)) continue;
                
                entity.damage(12.0, player);
                entity.addPotionEffect(new PotionEffect(
                    PotionEffectType.POISON, 200, 1, false, true
                ));
                
                // Knockback
                org.bukkit.util.Vector direction = entity.getLocation()
                    .subtract(loc)
                    .toVector()
                    .normalize()
                    .multiply(2.0)
                    .setY(1.0);
                entity.setVelocity(direction);
            }
            
            // Soins pour le joueur
            player.setHealth(player.getMaxHealth());
            player.addPotionEffect(new PotionEffect(
                PotionEffectType.REGENERATION, 200, 2, false, true
            ));
            player.addPotionEffect(new PotionEffect(
                PotionEffectType.DAMAGE_RESISTANCE, 200, 1, false, true
            ));
            
            // Invocation d'Iron Golem allié
            IronGolem golem = (IronGolem) loc.getWorld().spawnEntity(
                loc.clone().add(2, 0, 2),
                EntityType.IRON_GOLEM
            );
            golem.setCustomName("§aGardien de " + player.getName());
            golem.setCustomNameVisible(true);
            
            // Effets visuels massifs
            loc.getWorld().spawnParticle(
                Particle.VILLAGER_HAPPY, loc, 300, 5, 2, 5, 0.5
            );
            loc.getWorld().spawnParticle(
                Particle.BLOCK_CRACK, loc, 200, 4, 1, 4, 0,
                Material.OAK_LEAVES.createBlockData()
            );
            loc.getWorld().spawnParticle(
                Particle.EXPLOSION_LARGE, loc, 10, 3, 1, 3, 0
            );
            
            return true;
        }
        
        @Override
        public void playEffects(Player player) {
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 0.8f);
            player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_HURT, 1.0f, 0.5f);
        }
        
        @Override
        public String getSuccessMessage() {
            return "§2🌿 §lFORCE DE LA NATURE ! §2🌿";
        }
        
        @Override
        public ItemStack getIcon() {
            return new ItemStack(Material.MOSS_BLOCK);
        }
    }
}
