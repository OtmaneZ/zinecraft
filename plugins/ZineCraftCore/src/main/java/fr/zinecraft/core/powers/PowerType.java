package fr.zinecraft.core.powers;

import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;

/**
 * Types de super-pouvoirs disponibles
 * 
 * @author Otmane & Adam
 */
public enum PowerType {
    
    SUPER_SPEED(
        "⚡ Super Vitesse",
        "Courez à la vitesse de l'éclair!",
        ChatColor.YELLOW,
        10, // 10 secondes de cooldown
        5,  // 5 secondes de durée
        Particle.CRIT,
        Sound.ENTITY_FIREWORK_ROCKET_LAUNCH,
        "§7Vitesse: §eX5 pendant 5 secondes",
        "§7Cooldown: §a10 secondes"
    ),
    
    SUPER_JUMP(
        "🦘 Super Saut",
        "Sautez jusqu'aux nuages!",
        ChatColor.GREEN,
        15,
        0,
        Particle.CLOUD,
        Sound.ENTITY_ENDER_DRAGON_FLAP,
        "§7Saut: §a20 blocs de haut",
        "§7Cooldown: §a15 secondes",
        "§7Bonus: §bPas de dégâts de chute"
    ),
    
    FIREBALL(
        "🔥 Boule de Feu",
        "Lancez une boule de feu explosive!",
        ChatColor.RED,
        8,
        0,
        Particle.FLAME,
        Sound.ENTITY_BLAZE_SHOOT,
        "§7Dégâts: §c20 HP",
        "§7Explosion: §65 blocs",
        "§7Cooldown: §a8 secondes"
    ),
    
    FREEZE_ZONE(
        "❄️ Zone Glaciale",
        "Gelez tous les ennemis autour!",
        ChatColor.AQUA,
        20,
        10,
        Particle.SNOWFLAKE,
        Sound.BLOCK_GLASS_BREAK,
        "§7Rayon: §b10 blocs",
        "§7Effet: §fFreeze 10 secondes",
        "§7Cooldown: §a20 secondes"
    ),
    
    INVISIBILITY(
        "👻 Invisibilité",
        "Devenez invisible comme un fantôme!",
        ChatColor.GRAY,
        30,
        30,
        Particle.SMOKE_NORMAL,
        Sound.ENTITY_ENDERMAN_TELEPORT,
        "§7Durée: §730 secondes",
        "§7Cooldown: §a30 secondes",
        "§7Bonus: §8Pas de son de pas"
    ),
    
    TORNADO(
        "🌪️ Tornade",
        "Créez une tornade qui aspire les ennemis!",
        ChatColor.WHITE,
        25,
        8,
        Particle.CLOUD,
        Sound.ENTITY_WITHER_SHOOT,
        "§7Rayon: §f8 blocs",
        "§7Durée: §78 secondes",
        "§7Effet: §eAspiration + Vol",
        "§7Cooldown: §a25 secondes"
    ),
    
    LIGHTNING_STRIKE(
        "⚡ Éclair Ciblé",
        "Invoquez la foudre sur vos ennemis!",
        ChatColor.GOLD,
        12,
        0,
        Particle.ELECTRIC_SPARK,
        Sound.ENTITY_LIGHTNING_BOLT_THUNDER,
        "§7Dégâts: §e30 HP",
        "§7Portée: §650 blocs",
        "§7Cooldown: §a12 secondes"
    ),
    
    SHIELD(
        "🛡️ Bouclier",
        "Protection absolue pendant quelques secondes!",
        ChatColor.BLUE,
        30,
        5,
        Particle.ENCHANTMENT_TABLE,
        Sound.BLOCK_BEACON_ACTIVATE,
        "§7Durée: §95 secondes",
        "§7Effet: §bInvulnérable",
        "§7Bonus: §eRésistance IV",
        "§7Cooldown: §a30 secondes"
    ),
    
    TELEPORT(
        "🌀 Téléportation",
        "Téléportez-vous où vous regardez!",
        ChatColor.DARK_PURPLE,
        10,
        0,
        Particle.PORTAL,
        Sound.ENTITY_ENDERMAN_TELEPORT,
        "§7Portée: §550 blocs",
        "§7Cooldown: §a10 secondes"
    ),
    
    HEAL_AURA(
        "💚 Aura de Soin",
        "Soignez-vous et vos alliés!",
        ChatColor.LIGHT_PURPLE,
        20,
        0,
        Particle.HEART,
        Sound.BLOCK_BEACON_POWER_SELECT,
        "§7Soin: §c10 HP",
        "§7Rayon: §d8 blocs",
        "§7Effet: §aRégénération II (10s)",
        "§7Cooldown: §a20 secondes"
    ),
    
    FLIGHT(
        "🕊️ Vol",
        "Volez comme un oiseau!",
        ChatColor.WHITE,
        40,
        15,
        Particle.FIREWORKS_SPARK,
        Sound.ENTITY_BAT_TAKEOFF,
        "§7Durée: §f15 secondes",
        "§7Cooldown: §a40 secondes"
    ),
    
    EARTH_WALL(
        "🧱 Mur de Terre",
        "Créez un mur protecteur devant vous!",
        ChatColor.DARK_GREEN,
        15,
        0,
        Particle.BLOCK_CRACK,
        Sound.BLOCK_STONE_PLACE,
        "§7Taille: §25x5 blocs",
        "§7Durée: §620 secondes",
        "§7Cooldown: §a15 secondes"
    );
    
    private final String displayName;
    private final String description;
    private final ChatColor color;
    private final int cooldown;
    private final int duration;
    private final Particle particle;
    private final Sound sound;
    private final String[] info;
    
    PowerType(String displayName, String description, ChatColor color, 
              int cooldown, int duration, Particle particle, Sound sound, String... info) {
        this.displayName = displayName;
        this.description = description;
        this.color = color;
        this.cooldown = cooldown;
        this.duration = duration;
        this.particle = particle;
        this.sound = sound;
        this.info = info;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public ChatColor getColor() {
        return color;
    }
    
    public int getCooldown() {
        return cooldown;
    }
    
    public int getDuration() {
        return duration;
    }
    
    public Particle getParticle() {
        return particle;
    }
    
    public Sound getSound() {
        return sound;
    }
    
    public String[] getInfo() {
        return info;
    }
}
