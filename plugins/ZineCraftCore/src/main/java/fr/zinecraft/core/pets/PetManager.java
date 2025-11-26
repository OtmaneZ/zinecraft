package fr.zinecraft.core.pets;

import fr.zinecraft.core.ZineCraftCore;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Gestionnaire des familiers
 * 
 * @author Otmane & Adam
 */
public class PetManager {
    
    private final ZineCraftCore plugin;
    private final Map<UUID, Pet> activePets; // Player UUID -> Pet
    private final Map<UUID, Pet> petsByEntity; // Entity UUID -> Pet
    
    public PetManager(ZineCraftCore plugin) {
        this.plugin = plugin;
        this.activePets = new HashMap<>();
        this.petsByEntity = new HashMap<>();
        
        // Démarrer les tâches périodiques
        startPetTasks();
    }
    
    /**
     * Faire apparaître un familier
     */
    public Pet spawnPet(Player player, PetType type) {
        // Retirer l'ancien pet s'il existe
        removePet(player);
        
        Location spawnLoc = player.getLocation();
        Entity entity = spawnPetEntity(spawnLoc, type);
        
        if (entity == null) {
            player.sendMessage(ChatColor.RED + "Erreur lors du spawn du familier!");
            return null;
        }
        
        Pet pet = new Pet(player.getUniqueId(), type);
        pet.setEntity(entity);
        
        activePets.put(player.getUniqueId(), pet);
        petsByEntity.put(entity.getUniqueId(), pet);
        
        // Effets visuels du spawn
        spawnEffects(spawnLoc, type);
        
        // Message
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "✨ " + type.getColor() + type.getDefaultName() + ChatColor.GOLD + " est maintenant votre compagnon!");
        player.sendMessage(ChatColor.GRAY + "Il vous suivra partout et vous aidera au combat!");
        player.sendMessage("");
        
        return pet;
    }
    
    /**
     * Créer l'entité du pet
     */
    private Entity spawnPetEntity(Location location, PetType type) {
        World world = location.getWorld();
        Entity entity = world.spawnEntity(location, type.getEntityType());
        
        // Configuration de base
        entity.setCustomName(type.getColor() + type.getDefaultName() + ChatColor.GRAY + " [Niv.1]");
        entity.setCustomNameVisible(true);
        entity.setGlowing(true);
        
        // Configuration spécifique selon le type
        if (entity instanceof Tameable) {
            ((Tameable) entity).setTamed(true);
        }
        
        if (entity instanceof Wolf) {
            Wolf wolf = (Wolf) entity;
            wolf.setCollarColor(DyeColor.ORANGE);
            wolf.setAngry(false);
        }
        
        if (entity instanceof Parrot) {
            Parrot parrot = (Parrot) entity;
            parrot.setVariant(Parrot.Variant.BLUE);
        }
        
        if (entity instanceof Horse) {
            Horse horse = (Horse) entity;
            horse.setStyle(Horse.Style.WHITE);
            horse.setColor(Horse.Color.WHITE);
            horse.setAdult();
        }
        
        if (entity instanceof IronGolem) {
            IronGolem golem = (IronGolem) entity;
            golem.setPlayerCreated(true);
        }
        
        // Augmenter la santé selon le type
        if (entity instanceof LivingEntity) {
            LivingEntity living = (LivingEntity) entity;
            living.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(type.getDamage() * 2);
            living.setHealth(type.getDamage() * 2);
            
            // Effets permanents
            living.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 1));
            living.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 1));
            
            if (type == PetType.PHOENIX_FLAME) {
                living.setFireTicks(999999);
            }
        }
        
        return entity;
    }
    
    /**
     * Effets visuels au spawn
     */
    private void spawnEffects(Location loc, PetType type) {
        World world = loc.getWorld();
        
        world.spawnParticle(Particle.EXPLOSION_LARGE, loc, 1, 0, 0, 0, 0);
        world.spawnParticle(Particle.FIREWORKS_SPARK, loc, 50, 0.5, 0.5, 0.5, 0.1);
        world.spawnParticle(Particle.TOTEM, loc, 30, 1, 1, 1, 0.1);
        
        world.playSound(loc, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);
        world.playSound(loc, Sound.ENTITY_ENDER_DRAGON_GROWL, 0.5f, 2.0f);
    }
    
    /**
     * Retirer le familier d'un joueur
     */
    public void removePet(Player player) {
        Pet pet = activePets.remove(player.getUniqueId());
        if (pet != null && pet.getEntity() != null && pet.getEntity().isValid()) {
            Entity entity = pet.getEntity();
            
            // Effets de disparition
            Location loc = entity.getLocation();
            loc.getWorld().spawnParticle(Particle.CLOUD, loc, 30, 0.5, 0.5, 0.5, 0.05);
            loc.getWorld().playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 0.5f, 1.0f);
            
            petsByEntity.remove(entity.getUniqueId());
            entity.remove();
        }
    }
    
    /**
     * Faire évoluer un familier
     */
    public boolean evolvePet(Player player) {
        Pet pet = activePets.get(player.getUniqueId());
        if (pet == null) {
            player.sendMessage(ChatColor.RED + "Vous n'avez pas de familier!");
            return false;
        }
        
        if (!pet.canEvolve()) {
            player.sendMessage(ChatColor.RED + "Votre familier ne peut pas encore évoluer!");
            player.sendMessage(ChatColor.GRAY + "Niveau requis: " + pet.getType().getEvolutionLevel());
            return false;
        }
        
        PetType evolution = pet.getEvolution();
        Location loc = pet.getEntity().getLocation();
        
        // Effets d'évolution spectaculaires
        evolutionEffects(loc);
        
        // Retirer l'ancien
        removePet(player);
        
        // Spawner la nouvelle forme
        Pet evolved = spawnPet(player, evolution);
        evolved.setLevel(pet.getLevel());
        evolved.setCustomName(pet.getCustomName());
        
        // Message épique
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "════════════════════════════════");
        player.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "✨ ÉVOLUTION RÉUSSIE! ✨");
        player.sendMessage("");
        player.sendMessage(ChatColor.YELLOW + pet.getType().getDefaultName() + " → " + evolution.getColor() + evolution.getDefaultName());
        player.sendMessage("");
        player.sendMessage(ChatColor.GREEN + "Nouvelles capacités débloquées!");
        player.sendMessage(ChatColor.GOLD + "════════════════════════════════");
        player.sendMessage("");
        
        return true;
    }
    
    /**
     * Effets visuels d'évolution
     */
    private void evolutionEffects(Location loc) {
        World world = loc.getWorld();
        
        // Animation spectaculaire
        new BukkitRunnable() {
            int count = 0;
            
            @Override
            public void run() {
                if (count >= 20) {
                    cancel();
                    return;
                }
                
                // Spirale de particules
                for (int i = 0; i < 3; i++) {
                    double angle = (count * 18 + i * 120) * Math.PI / 180;
                    double x = Math.cos(angle) * 2;
                    double z = Math.sin(angle) * 2;
                    double y = count * 0.2;
                    
                    Location particleLoc = loc.clone().add(x, y, z);
                    world.spawnParticle(Particle.END_ROD, particleLoc, 1, 0, 0, 0, 0);
                    world.spawnParticle(Particle.FIREWORKS_SPARK, particleLoc, 2, 0, 0, 0, 0);
                }
                
                count++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
        
        // Sons
        world.playSound(loc, Sound.ENTITY_PLAYER_LEVELUP, 2.0f, 0.5f);
        world.playSound(loc, Sound.BLOCK_PORTAL_TRIGGER, 1.0f, 2.0f);
        world.playSound(loc, Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.5f);
        
        // Foudre
        world.strikeLightningEffect(loc);
    }
    
    /**
     * Obtenir le familier d'un joueur
     */
    public Pet getPet(Player player) {
        return activePets.get(player.getUniqueId());
    }
    
    /**
     * Vérifier si une entité est un familier
     */
    public boolean isPet(Entity entity) {
        return petsByEntity.containsKey(entity.getUniqueId());
    }
    
    /**
     * Tâches périodiques pour les familiers
     */
    private void startPetTasks() {
        // Faire suivre les familiers
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, Pet> entry : activePets.entrySet()) {
                    Player player = Bukkit.getPlayer(entry.getKey());
                    Pet pet = entry.getValue();
                    
                    if (player == null || !player.isOnline() || pet.getEntity() == null || !pet.getEntity().isValid()) {
                        continue;
                    }
                    
                    followOwner(player, pet);
                    petEffects(pet);
                }
            }
        }.runTaskTimer(plugin, 0L, 10L);
    }
    
    /**
     * Faire suivre le familier
     */
    private void followOwner(Player player, Pet pet) {
        Entity entity = pet.getEntity();
        Location petLoc = entity.getLocation();
        Location playerLoc = player.getLocation();
        
        double distance = petLoc.distance(playerLoc);
        
        // Téléporter si trop loin
        if (distance > 20) {
            entity.teleport(playerLoc);
            petLoc.getWorld().spawnParticle(Particle.PORTAL, petLoc, 20, 0.5, 0.5, 0.5, 0.1);
            return;
        }
        
        // Faire avancer vers le joueur
        if (distance > 3 && entity instanceof Mob) {
            Mob mob = (Mob) entity;
            mob.setTarget(null);
            mob.getPathfinder().moveTo(playerLoc);
        }
    }
    
    /**
     * Effets visuels du familier
     */
    private void petEffects(Pet pet) {
        Entity entity = pet.getEntity();
        Location loc = entity.getLocation().add(0, 1, 0);
        World world = loc.getWorld();
        
        PetType type = pet.getType();
        
        switch (type) {
            case WOLF_LEGENDARY:
                world.spawnParticle(Particle.CRIT_MAGIC, loc, 3, 0.3, 0.3, 0.3, 0);
                break;
                
            case DRAGON_BABY:
            case DRAGON_TEEN:
            case DRAGON_ADULT:
                world.spawnParticle(Particle.DRAGON_BREATH, loc, 5, 0.3, 0.3, 0.3, 0);
                if (pet.getLevel() >= 20) {
                    world.spawnParticle(Particle.FLAME, loc, 2, 0.2, 0.2, 0.2, 0);
                }
                break;
                
            case EAGLE_GOLDEN:
                world.spawnParticle(Particle.CRIT, loc, 2, 0.3, 0.3, 0.3, 0);
                break;
                
            case GOLEM_DIAMOND:
                world.spawnParticle(Particle.VILLAGER_HAPPY, loc, 2, 0.3, 0.3, 0.3, 0);
                break;
                
            case PHOENIX_FLAME:
                world.spawnParticle(Particle.FLAME, loc, 10, 0.3, 0.3, 0.3, 0.02);
                world.spawnParticle(Particle.SMOKE_NORMAL, loc, 5, 0.2, 0.2, 0.2, 0.01);
                break;
                
            case UNICORN:
                world.spawnParticle(Particle.TOTEM, loc, 3, 0.3, 0.3, 0.3, 0);
                world.spawnParticle(Particle.END_ROD, loc, 1, 0.2, 0.2, 0.2, 0);
                break;
        }
    }
}
