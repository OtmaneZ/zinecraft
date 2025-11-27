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
 * Compétences de la classe ARCHMAGE
 * 4 skills : Arcane Mastery, Time Warp, Elemental Fury, Apocalypse
 * 
 * @author Otmane & Copilot
 */
public class ArchmageSkills {
    
    /**
     * Skill 1: Arcane Mastery - Maîtrise arcanique
     */
    public static class ArcaneMastery extends Skill {
        
        public ArcaneMastery() {
            super(
                "archmage_arcane_mastery",
                "Arcane Mastery",
                "Régénération de mana x3 + réduction cooldown 50% pendant 20 secondes",
                30,
                15,
                5,
                SkillType.SUPPORT,
                SkillRarity.COMMON
            );
        }
        
        @Override
        public boolean execute(Player player) {
            // Buffs magiques
            player.addPotionEffect(new PotionEffect(
                PotionEffectType.LUCK, 400, 2, false, true
            ));
            player.addPotionEffect(new PotionEffect(
                PotionEffectType.NIGHT_VISION, 400, 0, false, true
            ));
            
            // TODO: Modifier temporairement la régénération de mana dans SkillManager
            // TODO: Modifier temporairement les cooldowns
            
            player.sendMessage("§d✨ Maîtrise arcanique activée !");
            return true;
        }
        
        @Override
        public void playEffects(Player player) {
            player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 2.0f);
            player.spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation(), 100, 1, 2, 1, 1);
        }
        
        @Override
        public ItemStack getIcon() {
            return new ItemStack(Material.ENCHANTED_BOOK);
        }
    }
    
    /**
     * Skill 2: Time Warp - Distorsion temporelle
     */
    public static class TimeWarp extends Skill {
        
        public TimeWarp() {
            super(
                "archmage_time_warp",
                "Time Warp",
                "Ralentit tous les ennemis (12 blocs) + accélère les alliés",
                25,
                35,
                10,
                SkillType.UTILITY,
                SkillRarity.UNCOMMON
            );
        }
        
        @Override
        public boolean execute(Player player) {
            Location loc = player.getLocation();
            
            // Ennemis ralentis
            Collection<LivingEntity> entities = loc.getWorld()
                .getNearbyLivingEntities(loc, 12.0);
            
            int affected = 0;
            for (LivingEntity entity : entities) {
                if (entity.equals(player)) continue;
                
                entity.addPotionEffect(new PotionEffect(
                    PotionEffectType.SLOW, 200, 2, false, true
                ));
                entity.addPotionEffect(new PotionEffect(
                    PotionEffectType.SLOW_DIGGING, 200, 2, false, true
                ));
                
                // Effets visuels
                entity.getWorld().spawnParticle(
                    Particle.PORTAL, entity.getLocation(), 50, 0.5, 1, 0.5, 0.5
                );
                
                affected++;
            }
            
            // Alliés accélérés
            Collection<Player> players = loc.getWorld().getNearbyPlayers(loc, 12.0);
            for (Player ally : players) {
                ally.addPotionEffect(new PotionEffect(
                    PotionEffectType.SPEED, 200, 2, false, true
                ));
                ally.addPotionEffect(new PotionEffect(
                    PotionEffectType.FAST_DIGGING, 200, 2, false, true
                ));
                
                if (!ally.equals(player)) {
                    ally.sendMessage("§d⏰ " + player.getName() + " a accéléré le temps pour vous !");
                }
            }
            
            player.sendMessage("§d⏰ Time Warp : " + affected + " ennemi(s) ralentis !");
            return true;
        }
        
        @Override
        public void playEffects(Player player) {
            player.playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 0.5f, 2.0f);
            player.spawnParticle(Particle.PORTAL, player.getLocation(), 200, 3, 2, 3, 2);
        }
        
        @Override
        public ItemStack getIcon() {
            return new ItemStack(Material.CLOCK);
        }
    }
    
    /**
     * Skill 3: Elemental Fury - Furie élémentaire
     */
    public static class ElementalFury extends Skill {
        
        public ElementalFury() {
            super(
                "archmage_elemental_fury",
                "Elemental Fury",
                "Déchaîne les 4 éléments : feu, glace, foudre, vent",
                50,
                45,
                15,
                SkillType.OFFENSIVE,
                SkillRarity.RARE
            );
        }
        
        @Override
        public boolean execute(Player player) {
            Location center = player.getLocation();
            
            // Animation sur 4 secondes
            new BukkitRunnable() {
                int phase = 0;
                
                @Override
                public void run() {
                    if (phase >= 4) {
                        this.cancel();
                        return;
                    }
                    
                    switch (phase) {
                        case 0: // FEU
                            spawnFireRing(center, player);
                            break;
                        case 1: // GLACE
                            spawnIceRing(center, player);
                            break;
                        case 2: // FOUDRE
                            spawnLightningRing(center, player);
                            break;
                        case 3: // VENT
                            spawnWindRing(center, player);
                            break;
                    }
                    
                    phase++;
                }
            }.runTaskTimer(
                org.bukkit.Bukkit.getPluginManager().getPlugin("ZineCraftCore"),
                0L, 20L
            );
            
            return true;
        }
        
        private void spawnFireRing(Location center, Player player) {
            Collection<LivingEntity> entities = center.getWorld()
                .getNearbyLivingEntities(center, 8.0);
            
            for (LivingEntity entity : entities) {
                if (entity.equals(player)) continue;
                entity.damage(6.0, player);
                entity.setFireTicks(100);
            }
            
            center.getWorld().spawnParticle(Particle.FLAME, center, 200, 4, 1, 4, 0.2);
            center.getWorld().playSound(center, Sound.ENTITY_BLAZE_SHOOT, 1.0f, 0.8f);
        }
        
        private void spawnIceRing(Location center, Player player) {
            Collection<LivingEntity> entities = center.getWorld()
                .getNearbyLivingEntities(center, 8.0);
            
            for (LivingEntity entity : entities) {
                if (entity.equals(player)) continue;
                entity.damage(4.0, player);
                entity.addPotionEffect(new PotionEffect(
                    PotionEffectType.SLOW, 100, 3, false, true
                ));
            }
            
            center.getWorld().spawnParticle(Particle.SNOWFLAKE, center, 200, 4, 1, 4, 0.2);
            center.getWorld().playSound(center, Sound.BLOCK_GLASS_BREAK, 1.0f, 0.5f);
        }
        
        private void spawnLightningRing(Location center, Player player) {
            Collection<LivingEntity> entities = center.getWorld()
                .getNearbyLivingEntities(center, 8.0);
            
            for (LivingEntity entity : entities) {
                if (entity.equals(player)) continue;
                entity.damage(8.0, player);
                entity.getWorld().strikeLightningEffect(entity.getLocation());
            }
            
            center.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, center, 300, 4, 2, 4, 0.5);
            center.getWorld().playSound(center, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.5f, 1.5f);
        }
        
        private void spawnWindRing(Location center, Player player) {
            Collection<LivingEntity> entities = center.getWorld()
                .getNearbyLivingEntities(center, 8.0);
            
            for (LivingEntity entity : entities) {
                if (entity.equals(player)) continue;
                
                // Knockback massif
                Vector direction = entity.getLocation()
                    .subtract(center)
                    .toVector()
                    .normalize()
                    .multiply(3.0)
                    .setY(1.5);
                entity.setVelocity(direction);
                
                entity.damage(5.0, player);
            }
            
            center.getWorld().spawnParticle(Particle.CLOUD, center, 300, 4, 1, 4, 0.3);
            center.getWorld().playSound(center, Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0f, 1.0f);
        }
        
        @Override
        public void playEffects(Player player) {
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 0.5f, 1.5f);
        }
        
        @Override
        public ItemStack getIcon() {
            return new ItemStack(Material.NETHER_STAR);
        }
    }
    
    /**
     * Skill 4: Apocalypse - Apocalypse ultime
     */
    public static class Apocalypse extends Skill {
        
        public Apocalypse() {
            super(
                "archmage_apocalypse",
                "Apocalypse",
                "Déclenche l'apocalypse magique : explosions massives + météores + foudre",
                120,
                80,
                25,
                SkillType.ULTIMATE,
                SkillRarity.LEGENDARY
            );
        }
        
        @Override
        public boolean execute(Player player) {
            Location center = player.getLocation();
            
            // Animation sur 10 secondes
            new BukkitRunnable() {
                int ticks = 0;
                
                @Override
                public void run() {
                    if (ticks >= 200) { // 10 secondes
                        player.sendMessage("§c☄ §lAPOCALYPSE TERMINÉE §c☄");
                        this.cancel();
                        return;
                    }
                    
                    // Toutes les 20 ticks (1 seconde)
                    if (ticks % 20 == 0) {
                        // Explosion aléatoire
                        double offsetX = (Math.random() - 0.5) * 20;
                        double offsetZ = (Math.random() - 0.5) * 20;
                        Location explosionLoc = center.clone().add(offsetX, 0, offsetZ);
                        
                        explosionLoc.getWorld().createExplosion(
                            explosionLoc, 3.0f, false, false, player
                        );
                        
                        // Foudre
                        explosionLoc.getWorld().strikeLightningEffect(explosionLoc);
                        
                        // Dégâts de zone
                        Collection<LivingEntity> entities = explosionLoc.getWorld()
                            .getNearbyLivingEntities(explosionLoc, 5.0);
                        
                        for (LivingEntity entity : entities) {
                            if (!entity.equals(player)) {
                                entity.damage(8.0, player);
                                entity.setFireTicks(100);
                            }
                        }
                    }
                    
                    // Particules constantes
                    if (ticks % 5 == 0) {
                        center.getWorld().spawnParticle(
                            Particle.FLAME, center, 50, 10, 5, 10, 0.5
                        );
                        center.getWorld().spawnParticle(
                            Particle.EXPLOSION_LARGE, center, 10, 8, 3, 8, 0.2
                        );
                        center.getWorld().spawnParticle(
                            Particle.SMOKE_NORMAL, center, 100, 10, 5, 10, 0.3
                        );
                    }
                    
                    ticks++;
                }
            }.runTaskTimer(
                org.bukkit.Bukkit.getPluginManager().getPlugin("ZineCraftCore"),
                0L, 1L
            );
            
            // Buff pour le joueur
            player.addPotionEffect(new PotionEffect(
                PotionEffectType.DAMAGE_RESISTANCE, 200, 2, false, true
            ));
            player.addPotionEffect(new PotionEffect(
                PotionEffectType.FIRE_RESISTANCE, 200, 0, false, true
            ));
            
            return true;
        }
        
        @Override
        public void playEffects(Player player) {
            player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.5f);
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 0.5f);
        }
        
        @Override
        public String getSuccessMessage() {
            return "§c☄ §l§nAPOCALYPSE MAGIQUE ! §c☄";
        }
        
        @Override
        public ItemStack getIcon() {
            return new ItemStack(Material.END_CRYSTAL);
        }
    }
}
