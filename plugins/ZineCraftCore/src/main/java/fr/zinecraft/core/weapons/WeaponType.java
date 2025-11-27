package fr.zinecraft.core.weapons;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;

/**
 * Types d'armes légendaires
 * 
 * @author Otmane & Adam
 */
public enum WeaponType {
    
    EXCALIBUR(
        "⚡ Excalibur",
        "L'épée légendaire du roi Arthur",
        Material.DIAMOND_SWORD,
        30,
        new String[]{
            "§7Effet: §eFoudre sur chaque coup",
            "§7Spécial: §bProjectile d'éclair",
            "§7Cooldown: §a3 secondes"
        },
        Particle.ELECTRIC_SPARK,
        Sound.ENTITY_LIGHTNING_BOLT_THUNDER,
        0xFFFF00 // Jaune
    ),
    
    FIRE_BLADE(
        "🔥 Lame de Feu",
        "Forgée dans les flammes de l'enfer",
        Material.NETHERITE_SWORD,
        25,
        new String[]{
            "§7Effet: §cMet le feu aux ennemis",
            "§7Spécial: §6Explosion de feu (zone)",
            "§7Cooldown: §a5 secondes"
        },
        Particle.FLAME,
        Sound.ENTITY_BLAZE_SHOOT,
        0xFF4500 // Orange-rouge
    ),
    
    ICE_SWORD(
        "❄️ Épée de Glace",
        "Taillée dans la glace éternelle",
        Material.DIAMOND_SWORD,
        20,
        new String[]{
            "§7Effet: §bFreeze les ennemis",
            "§7Spécial: §fVague de glace",
            "§7Cooldown: §a4 secondes"
        },
        Particle.SNOWFLAKE,
        Sound.BLOCK_GLASS_BREAK,
        0x00FFFF // Cyan
    ),
    
    THOR_HAMMER(
        "⚡ Marteau de Thor",
        "Le légendaire Mjölnir",
        Material.NETHERITE_AXE,
        35,
        new String[]{
            "§7Effet: §eInvoque la foudre",
            "§7Spécial: §6Knockback massif",
            "§7Cooldown: §a6 secondes"
        },
        Particle.ELECTRIC_SPARK,
        Sound.ITEM_TRIDENT_THUNDER,
        0xFFD700 // Or
    ),
    
    RAINBOW_BOW(
        "🌈 Arc-en-Ciel",
        "Tire des flèches multicolores",
        Material.BOW,
        15,
        new String[]{
            "§7Effet: §dTire 5 flèches simultanées",
            "§7Spécial: §5Flèches explosives",
            "§7Cooldown: §a3 secondes"
        },
        Particle.GLOW,
        Sound.ENTITY_FIREWORK_ROCKET_LAUNCH,
        0xFF00FF // Magenta
    ),
    
    SHADOW_DAGGER(
        "💀 Dague des Ombres",
        "Attaque depuis les ténèbres",
        Material.NETHERITE_SWORD,
        18,
        new String[]{
            "§7Effet: §8Invisibilité 3 secondes",
            "§7Spécial: §5Téléportation derrière",
            "§7Cooldown: §a8 secondes"
        },
        Particle.SMOKE_LARGE,
        Sound.ENTITY_ENDERMAN_TELEPORT,
        0x8B008B // Violet foncé
    ),
    
    DRAGON_SLAYER(
        "🐉 Tueuse de Dragons",
        "L'arme ultime anti-dragons",
        Material.NETHERITE_SWORD,
        40,
        new String[]{
            "§7Effet: §4+200% dégâts vs Dragons",
            "§7Spécial: §cSouffle de dragon",
            "§7Cooldown: §a10 secondes"
        },
        Particle.DRAGON_BREATH,
        Sound.ENTITY_ENDER_DRAGON_GROWL,
        0x8B0000 // Rouge foncé
    ),
    
    HOLY_SWORD(
        "✨ Épée Sacrée",
        "Bénie par les dieux",
        Material.GOLDEN_SWORD,
        22,
        new String[]{
            "§7Effet: §eRégénération au combat",
            "§7Spécial: §6Aura de soin (zone)",
            "§7Cooldown: §a7 secondes"
        },
        Particle.TOTEM,
        Sound.BLOCK_BEACON_ACTIVATE,
        0xFFD700 // Or
    ),
    
    POISON_BLADE(
        "☠️ Lame Toxique",
        "Empoisonne tout ce qu'elle touche",
        Material.IRON_SWORD,
        16,
        new String[]{
            "§7Effet: §2Poison niveau 3",
            "§7Spécial: §aNuage toxique",
            "§7Cooldown: §a5 secondes"
        },
        Particle.SLIME,
        Sound.ENTITY_SLIME_ATTACK,
        0x00FF00 // Vert
    ),
    
    VOID_SCYTHE(
        "🌀 Faux du Vide",
        "Aspire l'âme des ennemis",
        Material.NETHERITE_HOE,
        28,
        new String[]{
            "§7Effet: §5Vol de vie",
            "§7Spécial: §8Vortex aspirant",
            "§7Cooldown: §a9 secondes"
        },
        Particle.PORTAL,
        Sound.BLOCK_PORTAL_TRIGGER,
        0x4B0082 // Indigo
    ),
    
    FIREBALL(
        "🔥 Boule de Feu",
        "Lance des boules de feu dévastatrices",
        Material.FIRE_CHARGE,
        22,
        new String[]{
            "§7Effet: §cExplosion au contact",
            "§7Spécial: §6Lance une boule de feu",
            "§7Cooldown: §a4 secondes"
        },
        Particle.FLAME,
        Sound.ENTITY_GHAST_SHOOT,
        0xFF6600 // Orange
    );
    
    private final String displayName;
    private final String lore;
    private final Material material;
    private final int damage;
    private final String[] abilities;
    private final Particle particle;
    private final Sound sound;
    private final int glowColor;
    
    WeaponType(String displayName, String lore, Material material, int damage, 
               String[] abilities, Particle particle, Sound sound, int glowColor) {
        this.displayName = displayName;
        this.lore = lore;
        this.material = material;
        this.damage = damage;
        this.abilities = abilities;
        this.particle = particle;
        this.sound = sound;
        this.glowColor = glowColor;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getLore() {
        return lore;
    }
    
    public Material getMaterial() {
        return material;
    }
    
    public int getDamage() {
        return damage;
    }
    
    public String[] getAbilities() {
        return abilities;
    }
    
    public Particle getParticle() {
        return particle;
    }
    
    public Sound getSound() {
        return sound;
    }
    
    public int getGlowColor() {
        return glowColor;
    }
}
