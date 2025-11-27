package fr.zinecraft.core.skills.impl;

import fr.zinecraft.core.skills.Skill;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Collection;

/**
 * Compétences de la classe NECROMANCER
 * 4 skills : Summon Skeleton, Life Drain, Curse, Undead Army
 * 
 * @author Otmane & Copilot
 */
public class NecromancerSkills {
    
    /**
     * Skill 1: Summon Skeleton - Invocation de squelette
     */
    public static class SummonSkeleton extends Skill {
        
        public SummonSkeleton() {
            super(
                "necromancer_summon_skeleton",
                "Summon Skeleton",
                "Invoque 2 squelettes armés pour 60 secondes",
                30,
                25,
                5,
                SkillType.SUPPORT,
                SkillRarity.COMMON
            );
        }
        
        @Override
        public boolean execute(Player player) {
            Location loc = player.getLocation();
            
            for (int i = 0; i < 2; i++) {
                Skeleton skeleton = (Skeleton) loc.getWorld().spawnEntity(
                    loc.add(Math.random() * 2 - 1, 0, Math.random() * 2 - 1),
                    EntityType.SKELETON
                );
                
                // Équipement
                skeleton.getEquipment().setItemInMainHand(new ItemStack(Material.BOW));
                skeleton.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
                
                // Stats améliorées
                skeleton.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(30.0);
                skeleton.setHealth(30.0);
                
                // Nom custom
                skeleton.setCustomName("§7Minion de " + player.getName());
                skeleton.setCustomNameVisible(true);
                
                // Disparaît après 60 secondes
                skeleton.setRemoveWhenFarAway(true);
                
                // Effets visuels
                loc.getWorld().spawnParticle(
                    Particle.SOUL, skeleton.getLocation(), 30, 0.5, 1, 0.5, 0.05
                );
            }
            
            return true;
        }
        
        @Override
        public void playEffects(Player player) {
            player.playSound(player.getLocation(), Sound.ENTITY_SKELETON_AMBIENT, 1.0f, 0.8f);
            player.spawnParticle(Particle.SOUL, player.getLocation(), 50, 1, 0.5, 1, 0.1);
        }
        
        @Override
        public ItemStack getIcon() {
            return new ItemStack(Material.SKELETON_SKULL);
        }
    }
    
    /**
     * Skill 2: Life Drain - Drain de vie
     */
    public static class LifeDrain extends Skill {
        
        public LifeDrain() {
            super(
                "necromancer_life_drain",
                "Life Drain",
                "Draine la vie des ennemis proches (6 blocs) et vous soigne",
                20,
                30,
                10,
                SkillType.OFFENSIVE,
                SkillRarity.UNCOMMON
            );
        }
        
        @Override
        public boolean execute(Player player) {
            Location loc = player.getLocation();
            Collection<LivingEntity> entities = loc.getWorld()
                .getNearbyLivingEntities(loc, 6.0);
            
            double totalDrained = 0;
            int victims = 0;
            
            for (LivingEntity entity : entities) {
                if (entity.equals(player)) continue;
                
                // Drain 4 HP
                entity.damage(4.0, player);
                totalDrained += 4.0;
                victims++;
                
                // Wither effect
                entity.addPotionEffect(new PotionEffect(
                    PotionEffectType.WITHER, 60, 0, false, true
                ));
                
                // Effets visuels
                entity.getWorld().spawnParticle(
                    Particle.DAMAGE_INDICATOR,
                    entity.getEyeLocation(),
                    10, 0.3, 0.3, 0.3, 0.1
                );
                
                // Ligne de particules vers le joueur
                drawParticleLine(entity.getEyeLocation(), player.getEyeLocation(), Particle.SOUL);
            }
            
            if (victims > 0) {
                // Soigner le joueur
                double currentHealth = player.getHealth();
                double maxHealth = player.getMaxHealth();
                player.setHealth(Math.min(currentHealth + totalDrained, maxHealth));
                
                player.sendMessage("§c❤ Vie drainée : " + totalDrained + " HP de " + victims + " ennemi(s)");
            }
            
            return true;
        }
        
        private void drawParticleLine(Location from, Location to, Particle particle) {
            World world = from.getWorld();
            double distance = from.distance(to);
            Vector direction = to.toVector().subtract(from.toVector()).normalize().multiply(0.2);
            
            for (double i = 0; i < distance; i += 0.2) {
                Location particleLoc = from.clone().add(direction.clone().multiply(i));
                world.spawnParticle(particle, particleLoc, 1, 0, 0, 0, 0);
            }
        }
        
        @Override
        public void playEffects(Player player) {
            player.playSound(player.getLocation(), Sound.ENTITY_WITHER_HURT, 1.0f, 0.8f);
        }
        
        @Override
        public ItemStack getIcon() {
            return new ItemStack(Material.WITHER_ROSE);
        }
    }
    
    /**
     * Skill 3: Curse - Malédiction
     */
    public static class Curse extends Skill {
        
        public Curse() {
            super(
                "necromancer_curse",
                "Curse",
                "Maudit tous les ennemis (8 blocs) : Weakness + Slowness + Blindness",
                35,
                35,
                15,
                SkillType.UTILITY,
                SkillRarity.RARE
            );
        }
        
        @Override
        public boolean execute(Player player) {
            Location loc = player.getLocation();
            Collection<LivingEntity> entities = loc.getWorld()
                .getNearbyLivingEntities(loc, 8.0);
            
            int cursed = 0;
            for (LivingEntity entity : entities) {
                if (entity.equals(player)) continue;
                
                // Debuffs massifs
                entity.addPotionEffect(new PotionEffect(
                    PotionEffectType.WEAKNESS, 200, 2, false, true
                ));
                entity.addPotionEffect(new PotionEffect(
                    PotionEffectType.SLOW, 200, 1, false, true
                ));
                entity.addPotionEffect(new PotionEffect(
                    PotionEffectType.BLINDNESS, 100, 0, false, true
                ));
                entity.addPotionEffect(new PotionEffect(
                    PotionEffectType.WITHER, 100, 0, false, true
                ));
                
                // Effets visuels
                entity.getWorld().spawnParticle(
                    Particle.SPELL_WITCH, entity.getLocation(), 50, 0.5, 1, 0.5, 0.1
                );
                entity.getWorld().spawnParticle(
                    Particle.SQUID_INK, entity.getLocation(), 30, 0.5, 1, 0.5, 0.05
                );
                
                cursed++;
            }
            
            player.sendMessage("§5☠ " + cursed + " ennemi(s) maudit(s) !");
            return true;
        }
        
        @Override
        public void playEffects(Player player) {
            player.playSound(player.getLocation(), Sound.ENTITY_WITCH_AMBIENT, 1.0f, 0.5f);
            player.spawnParticle(Particle.SPELL_WITCH, player.getLocation(), 100, 3, 1, 3, 0.2);
        }
        
        @Override
        public ItemStack getIcon() {
            return new ItemStack(Material.FERMENTED_SPIDER_EYE);
        }
    }
    
    /**
     * Skill 4: Undead Army - Armée de morts-vivants ultime
     */
    public static class UndeadArmy extends Skill {
        
        public UndeadArmy() {
            super(
                "necromancer_undead_army",
                "Undead Army",
                "Invoque une armée de 6 zombies et 4 squelettes puissants",
                80,
                60,
                25,
                SkillType.ULTIMATE,
                SkillRarity.LEGENDARY
            );
        }
        
        @Override
        public boolean execute(Player player) {
            Location loc = player.getLocation();
            
            // 6 Zombies
            for (int i = 0; i < 6; i++) {
                Zombie zombie = (Zombie) loc.getWorld().spawnEntity(
                    loc.clone().add((Math.random() - 0.5) * 4, 0, (Math.random() - 0.5) * 4),
                    EntityType.ZOMBIE
                );
                
                zombie.getEquipment().setItemInMainHand(new ItemStack(Material.IRON_SWORD));
                zombie.getEquipment().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
                zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40.0);
                zombie.setHealth(40.0);
                zombie.setCustomName("§2Zombie de " + player.getName());
                zombie.setCustomNameVisible(true);
            }
            
            // 4 Skeletons
            for (int i = 0; i < 4; i++) {
                Skeleton skeleton = (Skeleton) loc.getWorld().spawnEntity(
                    loc.clone().add((Math.random() - 0.5) * 4, 0, (Math.random() - 0.5) * 4),
                    EntityType.SKELETON
                );
                
                skeleton.getEquipment().setItemInMainHand(new ItemStack(Material.BOW));
                skeleton.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
                skeleton.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(35.0);
                skeleton.setHealth(35.0);
                skeleton.setCustomName("§7Archer de " + player.getName());
                skeleton.setCustomNameVisible(true);
            }
            
            // Effets visuels massifs
            loc.getWorld().spawnParticle(
                Particle.SOUL, loc, 200, 3, 1, 3, 0.2
            );
            loc.getWorld().spawnParticle(
                Particle.SMOKE_NORMAL, loc, 100, 2, 0.5, 2, 0.1
            );
            
            return true;
        }
        
        @Override
        public void playEffects(Player player) {
            player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 0.5f, 0.8f);
            player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_AMBIENT, 1.0f, 0.5f);
        }
        
        @Override
        public String getSuccessMessage() {
            return "§5☠ §lARMÉE MORTE-VIVANTE INVOQUÉE ! §5☠";
        }
        
        @Override
        public ItemStack getIcon() {
            return new ItemStack(Material.ZOMBIE_HEAD);
        }
    }
}
