package com.zinecraft.npcs;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Gestionnaire des PNJ et animaux du village
 * Spawne des villageois et animaux passifs qui restent dans la zone
 * 
 * @author Otmane & Copilot
 */
public class VillageNPCManager implements Listener {
    
    private final Plugin plugin;
    private final Set<UUID> villageEntities = new HashSet<>();
    
    // Zone du village (coordonnées à ajuster selon votre village)
    private static final int VILLAGE_CENTER_X = 0;
    private static final int VILLAGE_CENTER_Z = 0;
    private static final int VILLAGE_RADIUS = 50; // Rayon en blocs
    
    public VillageNPCManager(Plugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    /**
     * Spawner tous les PNJ et animaux du village
     */
    public void spawnVillagePopulation() {
        World world = Bukkit.getWorld("world");
        if (world == null) return;
        
        Location center = new Location(world, VILLAGE_CENTER_X, 65, VILLAGE_CENTER_Z);
        
        // Spawner 8 villageois
        spawnVillagers(center, 8);
        
        // Spawner animaux passifs
        spawnLlamas(center, 4);
        spawnHorses(center, 3);
        spawnCats(center, 5);
        spawnChickens(center, 6);
        spawnCows(center, 4);
        spawnSheep(center, 4);
        
        plugin.getLogger().info("§a[VillageNPC] Population du village spawnée !");
        
        // Démarrer le système de confinement
        startBoundaryCheck();
    }
    
    /**
     * Spawner des villageois avec professions variées
     */
    private void spawnVillagers(Location center, int count) {
        Villager.Profession[] professions = {
            Villager.Profession.FARMER,
            Villager.Profession.LIBRARIAN,
            Villager.Profession.CLERIC,
            Villager.Profession.ARMORER,
            Villager.Profession.BUTCHER,
            Villager.Profession.NITWIT,
            Villager.Profession.CARTOGRAPHER,
            Villager.Profession.SHEPHERD
        };
        
        for (int i = 0; i < count; i++) {
            Location spawnLoc = getRandomLocationInVillage(center);
            Villager villager = (Villager) center.getWorld().spawnEntity(spawnLoc, EntityType.VILLAGER);
            
            // Définir profession
            villager.setProfession(professions[i % professions.length]);
            villager.setVillagerLevel(new Random().nextInt(3) + 2); // Level 2-4
            
            // Rendre invulnérable
            villager.setInvulnerable(true);
            villager.setAI(true);
            villager.setRemoveWhenFarAway(false);
            
            // Nom custom
            villager.setCustomName("§eVillageois " + getProfessionName(villager.getProfession()));
            villager.setCustomNameVisible(false);
            
            villageEntities.add(villager.getUniqueId());
        }
    }
    
    /**
     * Spawner des lamas
     */
    private void spawnLlamas(Location center, int count) {
        Llama.Color[] colors = {Llama.Color.WHITE, Llama.Color.BROWN, Llama.Color.GRAY, Llama.Color.CREAMY};
        
        for (int i = 0; i < count; i++) {
            Location spawnLoc = getRandomLocationInVillage(center);
            Llama llama = (Llama) center.getWorld().spawnEntity(spawnLoc, EntityType.LLAMA);
            
            llama.setColor(colors[i % colors.length]);
            llama.setTamed(true);
            llama.setInvulnerable(true);
            llama.setRemoveWhenFarAway(false);
            
            llama.setCustomName("§6Lama du Village");
            llama.setCustomNameVisible(false);
            
            villageEntities.add(llama.getUniqueId());
        }
    }
    
    /**
     * Spawner des chevaux
     */
    private void spawnHorses(Location center, int count) {
        Horse.Color[] colors = {Horse.Color.WHITE, Horse.Color.BROWN, Horse.Color.BLACK};
        
        for (int i = 0; i < count; i++) {
            Location spawnLoc = getRandomLocationInVillage(center);
            Horse horse = (Horse) center.getWorld().spawnEntity(spawnLoc, EntityType.HORSE);
            
            horse.setColor(colors[i % colors.length]);
            horse.setTamed(true);
            horse.setInvulnerable(true);
            horse.setRemoveWhenFarAway(false);
            horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.15); // Lent
            
            horse.setCustomName("§7Cheval");
            horse.setCustomNameVisible(false);
            
            villageEntities.add(horse.getUniqueId());
        }
    }
    
    /**
     * Spawner des chats
     */
    private void spawnCats(Location center, int count) {
        Cat.Type[] types = {Cat.Type.TABBY, Cat.Type.BLACK, Cat.Type.SIAMESE, Cat.Type.RED, Cat.Type.CALICO};
        
        for (int i = 0; i < count; i++) {
            Location spawnLoc = getRandomLocationInVillage(center);
            Cat cat = (Cat) center.getWorld().spawnEntity(spawnLoc, EntityType.CAT);
            
            cat.setCatType(types[i % types.length]);
            cat.setTamed(true);
            cat.setInvulnerable(true);
            cat.setRemoveWhenFarAway(false);
            
            villageEntities.add(cat.getUniqueId());
        }
    }
    
    /**
     * Spawner des poules
     */
    private void spawnChickens(Location center, int count) {
        for (int i = 0; i < count; i++) {
            Location spawnLoc = getRandomLocationInVillage(center);
            Chicken chicken = (Chicken) center.getWorld().spawnEntity(spawnLoc, EntityType.CHICKEN);
            
            chicken.setInvulnerable(true);
            chicken.setRemoveWhenFarAway(false);
            
            villageEntities.add(chicken.getUniqueId());
        }
    }
    
    /**
     * Spawner des vaches
     */
    private void spawnCows(Location center, int count) {
        for (int i = 0; i < count; i++) {
            Location spawnLoc = getRandomLocationInVillage(center);
            Cow cow = (Cow) center.getWorld().spawnEntity(spawnLoc, EntityType.COW);
            
            cow.setInvulnerable(true);
            cow.setRemoveWhenFarAway(false);
            
            villageEntities.add(cow.getUniqueId());
        }
    }
    
    /**
     * Spawner des moutons
     */
    private void spawnSheep(Location center, int count) {
        DyeColor[] colors = {DyeColor.WHITE, DyeColor.GRAY, DyeColor.BLACK, DyeColor.BROWN};
        
        for (int i = 0; i < count; i++) {
            Location spawnLoc = getRandomLocationInVillage(center);
            Sheep sheep = (Sheep) center.getWorld().spawnEntity(spawnLoc, EntityType.SHEEP);
            
            sheep.setColor(colors[i % colors.length]);
            sheep.setInvulnerable(true);
            sheep.setRemoveWhenFarAway(false);
            
            villageEntities.add(sheep.getUniqueId());
        }
    }
    
    /**
     * Obtenir une location aléatoire dans le village
     */
    private Location getRandomLocationInVillage(Location center) {
        Random random = new Random();
        double angle = random.nextDouble() * 2 * Math.PI;
        double radius = random.nextDouble() * (VILLAGE_RADIUS - 10);
        
        int x = (int) (center.getX() + radius * Math.cos(angle));
        int z = (int) (center.getZ() + radius * Math.sin(angle));
        int y = center.getWorld().getHighestBlockYAt(x, z) + 1;
        
        return new Location(center.getWorld(), x, y, z);
    }
    
    /**
     * Système de confinement - vérifie toutes les 5 secondes
     */
    private void startBoundaryCheck() {
        new BukkitRunnable() {
            @Override
            public void run() {
                checkAndTeleportEntities();
            }
        }.runTaskTimer(plugin, 100L, 100L); // Toutes les 5 secondes
    }
    
    /**
     * Vérifier et téléporter les entités qui sortent de la zone
     */
    private void checkAndTeleportEntities() {
        World world = Bukkit.getWorld("world");
        if (world == null) return;
        
        Location center = new Location(world, VILLAGE_CENTER_X, 65, VILLAGE_CENTER_Z);
        
        for (UUID uuid : new HashSet<>(villageEntities)) {
            Entity entity = Bukkit.getEntity(uuid);
            
            if (entity == null || !entity.isValid()) {
                villageEntities.remove(uuid);
                continue;
            }
            
            Location loc = entity.getLocation();
            double distance = Math.sqrt(
                Math.pow(loc.getX() - VILLAGE_CENTER_X, 2) +
                Math.pow(loc.getZ() - VILLAGE_CENTER_Z, 2)
            );
            
            // Si l'entité sort du rayon, la ramener
            if (distance > VILLAGE_RADIUS) {
                Location safeLoc = getRandomLocationInVillage(center);
                entity.teleport(safeLoc);
                
                // Effet visuel
                entity.getWorld().spawnParticle(Particle.PORTAL, entity.getLocation(), 20, 0.5, 0.5, 0.5, 0.1);
            }
        }
    }
    
    /**
     * Empêcher les dégâts sur les entités du village
     */
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (villageEntities.contains(event.getEntity().getUniqueId())) {
            event.setCancelled(true);
        }
    }
    
    /**
     * Empêcher les entités d'attaquer
     */
    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        if (villageEntities.contains(event.getEntity().getUniqueId())) {
            event.setCancelled(true);
        }
    }
    
    /**
     * Supprimer tous les PNJ et animaux du village
     */
    public void clearVillagePopulation() {
        int removed = 0;
        for (UUID uuid : new HashSet<>(villageEntities)) {
            Entity entity = Bukkit.getEntity(uuid);
            if (entity != null) {
                entity.remove();
                removed++;
            }
        }
        villageEntities.clear();
        plugin.getLogger().info("§c[VillageNPC] " + removed + " entités supprimées");
    }
    
    /**
     * Obtenir le nom de la profession
     */
    private String getProfessionName(Villager.Profession profession) {
        switch (profession) {
            case FARMER: return "Fermier";
            case LIBRARIAN: return "Bibliothécaire";
            case CLERIC: return "Clerc";
            case ARMORER: return "Forgeron";
            case BUTCHER: return "Boucher";
            case CARTOGRAPHER: return "Cartographe";
            case SHEPHERD: return "Berger";
            default: return "Villageois";
        }
    }
    
    /**
     * Obtenir le nombre d'entités actives
     */
    public int getActiveEntityCount() {
        int count = 0;
        for (UUID uuid : villageEntities) {
            Entity entity = Bukkit.getEntity(uuid);
            if (entity != null && entity.isValid()) {
                count++;
            }
        }
        return count;
    }
}
