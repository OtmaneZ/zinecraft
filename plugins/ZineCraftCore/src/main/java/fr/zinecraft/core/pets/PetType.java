package fr.zinecraft.core.pets;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

/**
 * Types de familiers disponibles avec leurs évolutions
 * 
 * @author Otmane & Adam
 */
public enum PetType {
    
    // === LOUPS ===
    WOLF_PUP(
        "Louveteau",
        EntityType.WOLF,
        ChatColor.GRAY,
        1,
        10,
        null,
        Material.BONE
    ),
    
    WOLF_ALPHA(
        "Loup Alpha",
        EntityType.WOLF,
        ChatColor.YELLOW,
        1.3,
        20,
        null,
        Material.BONE
    ),
    
    WOLF_LEGENDARY(
        "Loup Légendaire",
        EntityType.WOLF,
        ChatColor.GOLD,
        1.5,
        30,
        null,
        Material.BONE
    ),
    
    // === DRAGONS ===
    DRAGON_BABY(
        "Bébé Dragon",
        EntityType.ENDER_DRAGON,
        ChatColor.LIGHT_PURPLE,
        0.5,
        15,
        null,
        Material.DRAGON_BREATH
    ),
    
    DRAGON_TEEN(
        "Dragon Adolescent",
        EntityType.ENDER_DRAGON,
        ChatColor.DARK_PURPLE,
        0.7,
        25,
        null,
        Material.DRAGON_BREATH
    ),
    
    DRAGON_ADULT(
        "Dragon Adulte",
        EntityType.ENDER_DRAGON,
        ChatColor.DARK_PURPLE,
        1.0,
        40,
        null,
        Material.DRAGON_EGG
    ),
    
    // === AIGLES ===
    EAGLE_SMALL(
        "Petit Aigle",
        EntityType.PARROT,
        ChatColor.WHITE,
        1.0,
        12,
        null,
        Material.FEATHER
    ),
    
    EAGLE_GOLDEN(
        "Aigle Doré",
        EntityType.PARROT,
        ChatColor.GOLD,
        1.2,
        20,
        null,
        Material.GOLDEN_APPLE
    ),
    
    // === GOLEMS ===
    GOLEM_STONE(
        "Golem de Pierre",
        EntityType.IRON_GOLEM,
        ChatColor.GRAY,
        0.5,
        25,
        null,
        Material.IRON_INGOT
    ),
    
    GOLEM_IRON(
        "Golem de Fer",
        EntityType.IRON_GOLEM,
        ChatColor.WHITE,
        0.8,
        35,
        null,
        Material.IRON_BLOCK
    ),
    
    GOLEM_DIAMOND(
        "Golem de Diamant",
        EntityType.IRON_GOLEM,
        ChatColor.AQUA,
        1.0,
        50,
        null,
        Material.DIAMOND_BLOCK
    ),
    
    // === PHOENIX ===
    PHOENIX_FLAME(
        "Phoenix de Flammes",
        EntityType.BLAZE,
        ChatColor.RED,
        1.0,
        30,
        null,
        Material.FIRE_CHARGE
    ),
    
    // === LICORNE ===
    UNICORN(
        "Licorne Magique",
        EntityType.HORSE,
        ChatColor.LIGHT_PURPLE,
        1.2,
        35,
        null,
        Material.ENCHANTED_GOLDEN_APPLE
    );
    
    private final String defaultName;
    private final EntityType entityType;
    private final ChatColor color;
    private final double scale;
    private final int damage;
    private PetType evolution;
    private final Material foodItem;
    
    PetType(String defaultName, EntityType entityType, ChatColor color, double scale, int damage, PetType evolution, Material foodItem) {
        this.defaultName = defaultName;
        this.entityType = entityType;
        this.color = color;
        this.scale = scale;
        this.damage = damage;
        this.evolution = evolution;
        this.foodItem = foodItem;
    }
    
    public String getDefaultName() {
        return defaultName;
    }
    
    public EntityType getEntityType() {
        return entityType;
    }
    
    public ChatColor getColor() {
        return color;
    }
    
    public double getScale() {
        return scale;
    }
    
    public int getDamage() {
        return damage;
    }
    
    public PetType getEvolution() {
        return evolution;
    }
    
    public Material getFoodItem() {
        return foodItem;
    }
    
    /**
     * Niveau requis pour évoluer
     */
    public int getEvolutionLevel() {
        if (evolution == null) return 999;
        return 20; // Niveau 20 pour évoluer
    }
    
    static {
        // Définir les évolutions
        WOLF_PUP.evolution = WOLF_ALPHA;
        WOLF_ALPHA.evolution = WOLF_LEGENDARY;
        
        DRAGON_BABY.evolution = DRAGON_TEEN;
        DRAGON_TEEN.evolution = DRAGON_ADULT;
        
        EAGLE_SMALL.evolution = EAGLE_GOLDEN;
        
        GOLEM_STONE.evolution = GOLEM_IRON;
        GOLEM_IRON.evolution = GOLEM_DIAMOND;
    }
}
