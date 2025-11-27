package fr.zinecraft.core.weapons;

import fr.zinecraft.core.ZineCraftCore;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * Gestionnaire des armes légendaires
 * 
 * @author Otmane & Adam
 */
public class WeaponManager {
    
    private final ZineCraftCore plugin;
    private final Map<UUID, Long> cooldowns; // Player UUID -> Last use timestamp
    private final Map<UUID, WeaponType> activeWeapons; // Player UUID -> Current weapon
    
    public WeaponManager(ZineCraftCore plugin) {
        this.plugin = plugin;
        this.cooldowns = new HashMap<>();
        this.activeWeapons = new HashMap<>();
    }
    
    /**
     * Créer une arme légendaire
     */
    public ItemStack createWeapon(WeaponType type) {
        ItemStack weapon = new ItemStack(type.getMaterial());
        ItemMeta meta = weapon.getItemMeta();
        
        // Nom
        meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + type.getDisplayName());
        
        // Lore
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + type.getLore());
        lore.add("");
        lore.add(ChatColor.YELLOW + "Dégâts: " + ChatColor.RED + type.getDamage() + " ⚔");
        lore.add("");
        for (String ability : type.getAbilities()) {
            lore.add(ability);
        }
        lore.add("");
        lore.add(ChatColor.DARK_GRAY + "Clic droit pour utiliser le pouvoir spécial");
        lore.add("");
        lore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "✦ ARME LÉGENDAIRE ✦");
        
        meta.setLore(lore);
        
        // Durabilité illimitée
        meta.setUnbreakable(true);
        
        // Enchantements
        meta.addEnchant(Enchantment.DAMAGE_ALL, 10, true);
        meta.addEnchant(Enchantment.DURABILITY, 10, true);
        meta.addEnchant(Enchantment.MENDING, 1, true);
        
        // Cacher les flags
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        
        // Appliquer
        weapon.setItemMeta(meta);
        
        return weapon;
    }
    
    /**
     * Utiliser le pouvoir spécial d'une arme
     */
    public void useWeaponPower(Player player, WeaponType type) {
        // Vérifier le cooldown
        if (isOnCooldown(player)) {
            long timeLeft = getCooldownTimeLeft(player);
            player.sendMessage(ChatColor.RED + "⏳ Cooldown: " + (timeLeft / 1000) + " secondes");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5f, 1.0f);
            return;
        }
        
        // Activer le pouvoir
        Location loc = player.getLocation();
        World world = player.getWorld();
        
        switch (type) {
            case EXCALIBUR:
                excaliburPower(player);
                break;
            case FIRE_BLADE:
                fireBladePower(player);
                break;
            case ICE_SWORD:
                iceSwordPower(player);
                break;
            case THOR_HAMMER:
                thorHammerPower(player);
                break;
            case RAINBOW_BOW:
                rainbowBowPower(player);
                break;
            case SHADOW_DAGGER:
                shadowDaggerPower(player);
                break;
            case DRAGON_SLAYER:
                dragonSlayerPower(player);
                break;
            case HOLY_SWORD:
                holySwordPower(player);
                break;
            case POISON_BLADE:
                poisonBladePower(player);
                break;
            case VOID_SCYTHE:
                voidScythePower(player);
                break;
            case FIREBALL:
                fireballPower(player);
                break;
            case ICY_DUCK:
                icyDuckPower(player);
                break;
            case FLY_FLY:
                flyFlyPower(player);
                break;
        }
        
        // Mettre le cooldown
        setCooldown(player, 5000); // 5 secondes par défaut
        
        // Effets génériques
        world.spawnParticle(type.getParticle(), loc, 50, 1, 1, 1, 0.1);
        world.playSound(loc, type.getSound(), 1.0f, 1.0f);
    }
    
    /**
     * Excalibur: Projectile d'éclair
     */
    private void excaliburPower(Player player) {
        Vector direction = player.getLocation().getDirection();
        Location start = player.getEyeLocation();
        
        new BukkitRunnable() {
            Location current = start.clone();
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 30) {
                    cancel();
                    return;
                }
                
                current.add(direction.clone().multiply(0.5));
                
                // Particules
                current.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, current, 10, 0.2, 0.2, 0.2, 0);
                
                // Chercher les entités
                for (Entity entity : current.getWorld().getNearbyEntities(current, 1, 1, 1)) {
                    if (entity instanceof LivingEntity && entity != player) {
                        LivingEntity target = (LivingEntity) entity;
                        target.damage(20);
                        target.getWorld().strikeLightningEffect(target.getLocation());
                        cancel();
                        return;
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
        
        player.sendMessage(ChatColor.YELLOW + "⚡ Éclair lancé!");
    }
    
    /**
     * Lame de Feu: Explosion de feu
     */
    private void fireBladePower(Player player) {
        Location loc = player.getLocation();
        
        // Explosion de particules
        loc.getWorld().spawnParticle(Particle.FLAME, loc, 200, 3, 1, 3, 0.1);
        loc.getWorld().spawnParticle(Particle.LAVA, loc, 50, 3, 1, 3, 0);
        loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 2.0f, 0.8f);
        
        // Dégâts en zone
        for (Entity entity : loc.getWorld().getNearbyEntities(loc, 5, 5, 5)) {
            if (entity instanceof LivingEntity && entity != player) {
                LivingEntity target = (LivingEntity) entity;
                target.damage(15);
                target.setFireTicks(100); // 5 secondes de feu
            }
        }
        
        player.sendMessage(ChatColor.RED + "🔥 Explosion de feu!");
    }
    
    /**
     * Épée de Glace: Vague de glace
     */
    private void iceSwordPower(Player player) {
        Location loc = player.getLocation();
        
        // Vague de glace
        loc.getWorld().spawnParticle(Particle.SNOWFLAKE, loc, 300, 5, 1, 5, 0.1);
        loc.getWorld().spawnParticle(Particle.CLOUD, loc, 100, 5, 1, 5, 0.05);
        loc.getWorld().playSound(loc, Sound.BLOCK_GLASS_BREAK, 2.0f, 0.5f);
        
        // Freeze les ennemis
        for (Entity entity : loc.getWorld().getNearbyEntities(loc, 6, 6, 6)) {
            if (entity instanceof LivingEntity && entity != player) {
                LivingEntity target = (LivingEntity) entity;
                target.damage(12);
                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 5));
                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 5));
            }
        }
        
        player.sendMessage(ChatColor.AQUA + "❄️ Vague de glace!");
    }
    
    /**
     * Marteau de Thor: Foudre massive
     */
    private void thorHammerPower(Player player) {
        Location loc = player.getTargetBlock(null, 20).getLocation();
        
        // Foudres multiples
        for (int i = 0; i < 5; i++) {
            Location strikeLoc = loc.clone().add(
                (Math.random() - 0.5) * 10,
                0,
                (Math.random() - 0.5) * 10
            );
            strikeLoc.getWorld().strikeLightning(strikeLoc);
        }
        
        // Knockback massif
        for (Entity entity : loc.getWorld().getNearbyEntities(loc, 10, 10, 10)) {
            if (entity instanceof LivingEntity && entity != player) {
                LivingEntity target = (LivingEntity) entity;
                Vector direction = target.getLocation().subtract(loc).toVector().normalize();
                target.setVelocity(direction.multiply(3).setY(2));
                target.damage(25);
            }
        }
        
        player.sendMessage(ChatColor.GOLD + "⚡ Foudre de Thor!");
    }
    
    /**
     * Arc-en-Ciel: Flèches multiples
     */
    private void rainbowBowPower(Player player) {
        Location eyeLoc = player.getEyeLocation();
        Vector direction = eyeLoc.getDirection();
        
        // Tirer 5 flèches en éventail
        for (int i = -2; i <= 2; i++) {
            Vector spread = direction.clone().rotateAroundY(Math.toRadians(i * 10));
            Arrow arrow = player.getWorld().spawnArrow(eyeLoc, spread, 2.0f, 0);
            arrow.setShooter(player);
            arrow.setGlowing(true);
            arrow.setDamage(10);
        }
        
        player.sendMessage(ChatColor.LIGHT_PURPLE + "🌈 Salve arc-en-ciel!");
    }
    
    /**
     * Dague des Ombres: Téléportation + invisibilité
     */
    private void shadowDaggerPower(Player player) {
        // Invisibilité
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 60, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 2));
        
        // Effets
        Location loc = player.getLocation();
        loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 50, 1, 1, 1, 0.1);
        loc.getWorld().playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.5f);
        
        player.sendMessage(ChatColor.DARK_GRAY + "💀 Mode furtif activé!");
    }
    
    /**
     * Tueuse de Dragons: Souffle de dragon
     */
    private void dragonSlayerPower(Player player) {
        Vector direction = player.getLocation().getDirection();
        Location start = player.getEyeLocation();
        
        new BukkitRunnable() {
            Location current = start.clone();
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 40) {
                    cancel();
                    return;
                }
                
                current.add(direction.clone().multiply(0.5));
                
                // Souffle de dragon
                current.getWorld().spawnParticle(Particle.DRAGON_BREATH, current, 20, 0.5, 0.5, 0.5, 0.05);
                current.getWorld().spawnParticle(Particle.FLAME, current, 5, 0.3, 0.3, 0.3, 0);
                
                // Dégâts
                for (Entity entity : current.getWorld().getNearbyEntities(current, 2, 2, 2)) {
                    if (entity instanceof LivingEntity && entity != player) {
                        LivingEntity target = (LivingEntity) entity;
                        double damage = (entity.getType() == EntityType.ENDER_DRAGON) ? 80 : 15;
                        target.damage(damage);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
        
        player.sendMessage(ChatColor.DARK_RED + "🐉 Souffle de dragon!");
    }
    
    /**
     * Épée Sacrée: Aura de soin
     */
    private void holySwordPower(Player player) {
        Location loc = player.getLocation();
        
        // Aura de soin
        loc.getWorld().spawnParticle(Particle.TOTEM, loc, 100, 5, 2, 5, 0.1);
        loc.getWorld().playSound(loc, Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.5f);
        
        // Soin en zone
        for (Entity entity : loc.getWorld().getNearbyEntities(loc, 10, 10, 10)) {
            if (entity instanceof Player) {
                Player target = (Player) entity;
                target.setHealth(Math.min(target.getHealth() + 10, target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
                target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2));
            }
        }
        
        player.sendMessage(ChatColor.YELLOW + "✨ Aura de soin activée!");
    }
    
    /**
     * Lame Toxique: Nuage de poison
     */
    private void poisonBladePower(Player player) {
        Location loc = player.getLocation();
        
        // Nuage toxique
        loc.getWorld().spawnParticle(Particle.SLIME, loc, 200, 4, 2, 4, 0);
        loc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc, 100, 4, 2, 4, 0);
        
        // Poison en zone
        for (Entity entity : loc.getWorld().getNearbyEntities(loc, 5, 5, 5)) {
            if (entity instanceof LivingEntity && entity != player) {
                LivingEntity target = (LivingEntity) entity;
                target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 2));
                target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 1));
            }
        }
        
        player.sendMessage(ChatColor.GREEN + "☠️ Nuage toxique!");
    }
    
    /**
     * Faux du Vide: Vortex aspirant
     */
    private void voidScythePower(Player player) {
        Location loc = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ticks++ >= 60) {
                    cancel();
                    return;
                }
                
                // Vortex visuel
                for (int i = 0; i < 5; i++) {
                    double angle = (ticks * 30 + i * 72) * Math.PI / 180;
                    double radius = 5 - (ticks * 0.05);
                    double x = Math.cos(angle) * radius;
                    double z = Math.sin(angle) * radius;
                    
                    Location particleLoc = loc.clone().add(x, ticks * 0.1, z);
                    loc.getWorld().spawnParticle(Particle.PORTAL, particleLoc, 5, 0, 0, 0, 0);
                }
                
                // Aspirer les entités
                for (Entity entity : loc.getWorld().getNearbyEntities(loc, 8, 8, 8)) {
                    if (entity instanceof LivingEntity && entity != player) {
                        Vector direction = loc.toVector().subtract(entity.getLocation().toVector()).normalize();
                        entity.setVelocity(direction.multiply(0.5));
                        
                        if (entity.getLocation().distance(loc) < 2) {
                            LivingEntity target = (LivingEntity) entity;
                            target.damage(5);
                            player.setHealth(Math.min(player.getHealth() + 2, player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
        
        player.sendMessage(ChatColor.DARK_PURPLE + "🌀 Vortex du vide!");
    }
    
    /**
     * Boule de Feu: Lance une boule de feu explosive
     */
    private void fireballPower(Player player) {
        Location eyeLoc = player.getEyeLocation();
        Vector direction = eyeLoc.getDirection().normalize();
        
        // Créer une boule de feu
        Fireball fireball = player.getWorld().spawn(
            eyeLoc.add(direction.multiply(1.5)), 
            Fireball.class
        );
        
        // Configurer la boule de feu
        fireball.setShooter(player);
        fireball.setDirection(direction);
        fireball.setYield(3.0f); // Puissance de l'explosion
        fireball.setIsIncendiary(true); // Mettre le feu
        fireball.setVelocity(direction.multiply(1.5));
        
        // Effets visuels
        player.getWorld().spawnParticle(Particle.FLAME, eyeLoc, 30, 0.3, 0.3, 0.3, 0.05);
        player.getWorld().playSound(eyeLoc, Sound.ENTITY_GHAST_SHOOT, 1.5f, 1.0f);
        
        player.sendMessage(ChatColor.GOLD + "🔥 Boule de feu lancée!");
    }
    
    /**
     * Canard Glacial: Invoque un canard qui gèle la cible
     */
    private void icyDuckPower(Player player) {
        // Trouver la cible
        LivingEntity target = null;
        double closestDistance = 20.0;
        
        for (Entity entity : player.getNearbyEntities(20, 20, 20)) {
            if (entity instanceof LivingEntity && entity != player) {
                Location eyeLoc = player.getEyeLocation();
                Vector toEntity = entity.getLocation().toVector().subtract(eyeLoc.toVector()).normalize();
                Vector direction = eyeLoc.getDirection();
                
                // Vérifier si l'entité est dans la direction du regard
                if (toEntity.dot(direction) > 0.9) {
                    double distance = player.getLocation().distance(entity.getLocation());
                    if (distance < closestDistance) {
                        target = (LivingEntity) entity;
                        closestDistance = distance;
                    }
                }
            }
        }
        
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Aucune cible trouvée!");
            return;
        }
        
        final LivingEntity finalTarget = target;
        Location targetLoc = target.getLocation();
        
        // Faire apparaître un poulet (canard) au-dessus de la cible
        Location spawnLoc = targetLoc.clone().add(0, 10, 0);
        Chicken duck = (Chicken) player.getWorld().spawnEntity(spawnLoc, org.bukkit.entity.EntityType.CHICKEN);
        duck.setCustomName(ChatColor.AQUA + "🦆 Canard Glacial");
        duck.setCustomNameVisible(true);
        duck.setInvulnerable(true);
        duck.setSilent(false);
        
        // Effets de glace
        player.getWorld().spawnParticle(Particle.SNOWFLAKE, spawnLoc, 100, 2, 2, 2, 0.1);
        player.getWorld().playSound(spawnLoc, Sound.ENTITY_CHICKEN_AMBIENT, 1.5f, 0.8f);
        
        // Animation: le canard descend vers la cible
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                ticks++;
                
                if (!duck.isValid() || ticks > 40) {
                    if (duck.isValid()) {
                        duck.remove();
                    }
                    cancel();
                    return;
                }
                
                // Déplacer le canard vers la cible
                Location duckLoc = duck.getLocation();
                Vector direction = finalTarget.getLocation().toVector().subtract(duckLoc.toVector()).normalize();
                duck.setVelocity(direction.multiply(0.8));
                
                // Particules de glace
                duck.getWorld().spawnParticle(Particle.SNOWFLAKE, duckLoc, 10, 0.5, 0.5, 0.5, 0.05);
                
                // Si le canard est proche de la cible
                if (duckLoc.distance(finalTarget.getLocation()) < 2) {
                    // Geler la cible
                    finalTarget.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 10)); // 5 sec
                    finalTarget.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 10));
                    finalTarget.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 100, 250)); // Ne peut pas sauter
                    finalTarget.damage(15);
                    finalTarget.setFreezeTicks(200); // Effet visuel de gel
                    
                    // Explosion de glace
                    finalTarget.getWorld().spawnParticle(Particle.SNOWFLAKE, finalTarget.getLocation(), 200, 2, 2, 2, 0.2);
                    finalTarget.getWorld().spawnParticle(Particle.CLOUD, finalTarget.getLocation(), 50, 1, 1, 1, 0.1);
                    finalTarget.getWorld().playSound(finalTarget.getLocation(), Sound.BLOCK_GLASS_BREAK, 2.0f, 0.5f);
                    finalTarget.getWorld().playSound(finalTarget.getLocation(), Sound.ENTITY_PLAYER_HURT_FREEZE, 2.0f, 1.0f);
                    
                    // Message
                    if (finalTarget instanceof Player) {
                        ((Player) finalTarget).sendMessage(ChatColor.AQUA + "🦆 Vous avez été gelé par un canard!");
                    }
                    
                    duck.remove();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
        
        player.sendMessage(ChatColor.AQUA + "🦆 Canard glacial invoqué!");
    }
    
    /**
     * Vole Vole: Fait voler la cible dans les airs
     */
    private void flyFlyPower(Player player) {
        // Trouver la cible
        LivingEntity target = null;
        double closestDistance = 20.0;
        
        for (Entity entity : player.getNearbyEntities(20, 20, 20)) {
            if (entity instanceof LivingEntity && entity != player) {
                Location eyeLoc = player.getEyeLocation();
                Vector toEntity = entity.getLocation().toVector().subtract(eyeLoc.toVector()).normalize();
                Vector direction = eyeLoc.getDirection();
                
                // Vérifier si l'entité est dans la direction du regard
                if (toEntity.dot(direction) > 0.9) {
                    double distance = player.getLocation().distance(entity.getLocation());
                    if (distance < closestDistance) {
                        target = (LivingEntity) entity;
                        closestDistance = distance;
                    }
                }
            }
        }
        
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Aucune cible trouvée!");
            return;
        }
        
        final LivingEntity finalTarget = target;
        
        // Message à la cible
        if (finalTarget instanceof Player) {
            ((Player) finalTarget).sendMessage(ChatColor.YELLOW + "🪶 Vous volez dans les airs!");
        }
        
        // Effets de vol
        player.getWorld().spawnParticle(Particle.CLOUD, finalTarget.getLocation(), 50, 1, 1, 1, 0.1);
        player.getWorld().playSound(finalTarget.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.5f, 1.2f);
        
        // Animation de vol: 5 blocs/seconde pendant 5 secondes
        new BukkitRunnable() {
            int ticks = 0;
            final int maxTicks = 100; // 5 secondes (100 ticks)
            final double blocksPerSecond = 5.0;
            final double velocityPerTick = blocksPerSecond / 20.0; // Convertir en vélocité par tick
            
            @Override
            public void run() {
                ticks++;
                
                if (!finalTarget.isValid() || !finalTarget.isOnGround() && ticks > maxTicks) {
                    cancel();
                    return;
                }
                
                if (ticks <= maxTicks) {
                    // Faire voler vers le haut
                    Vector velocity = finalTarget.getVelocity();
                    velocity.setY(velocityPerTick);
                    finalTarget.setVelocity(velocity);
                    
                    // Particules de vol
                    finalTarget.getWorld().spawnParticle(Particle.CLOUD, 
                        finalTarget.getLocation(), 5, 0.3, 0.3, 0.3, 0.02);
                    
                    // Empêcher les dégâts de chute pendant le vol
                    finalTarget.setFallDistance(0);
                    
                    // Son de vol toutes les 20 ticks
                    if (ticks % 20 == 0) {
                        finalTarget.getWorld().playSound(finalTarget.getLocation(), 
                            Sound.ENTITY_BAT_AMBIENT, 0.5f, 1.5f);
                    }
                } else {
                    // Après 5 secondes, laisser tomber
                    finalTarget.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, 
                        finalTarget.getLocation(), 3, 0.5, 0.5, 0.5, 0);
                    
                    if (finalTarget instanceof Player) {
                        ((Player) finalTarget).sendMessage(ChatColor.RED + "💥 Vous retombez!");
                    }
                    
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
        
        player.sendMessage(ChatColor.YELLOW + "🪶 Cible envoyée dans les airs!");
    }
    
    /**
     * Vérifier le cooldown
     */
    private boolean isOnCooldown(Player player) {
        if (!cooldowns.containsKey(player.getUniqueId())) {
            return false;
        }
        return System.currentTimeMillis() - cooldowns.get(player.getUniqueId()) < 5000;
    }
    
    /**
     * Temps restant du cooldown
     */
    private long getCooldownTimeLeft(Player player) {
        if (!cooldowns.containsKey(player.getUniqueId())) {
            return 0;
        }
        return 5000 - (System.currentTimeMillis() - cooldowns.get(player.getUniqueId()));
    }
    
    /**
     * Mettre un cooldown
     */
    private void setCooldown(Player player, long duration) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
    }
    
    /**
     * Vérifier si un item est une arme légendaire
     */
    public boolean isLegendaryWeapon(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasDisplayName()) return false;
        
        String name = meta.getDisplayName();
        for (WeaponType type : WeaponType.values()) {
            if (name.contains(type.getDisplayName())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Obtenir le type d'arme
     */
    public WeaponType getWeaponType(ItemStack item) {
        if (!isLegendaryWeapon(item)) return null;
        
        String name = item.getItemMeta().getDisplayName();
        for (WeaponType type : WeaponType.values()) {
            if (name.contains(type.getDisplayName())) {
                return type;
            }
        }
        return null;
    }
}
