package fr.zinecraft.core.events;

import org.bukkit.ChatColor;

/**
 * Types d'événements dynamiques du serveur
 *
 * @author Otmane & Copilot
 */
public enum EventType {

    METEOR_STRIKE(
        "Pluie de Météores",
        "☄",
        ChatColor.RED + "Des météores tombent du ciel!",
        30, // durée en minutes
        false // pas premium
    ),

    BLOOD_MOON(
        "Lune de Sang",
        "🌙",
        ChatColor.DARK_RED + "Une lune de sang se lève...",
        20,
        false
    ),

    TREASURE_HUNT(
        "Chasse au Trésor",
        "💎",
        ChatColor.GOLD + "Un trésor légendaire est apparu quelque part!",
        15,
        false
    ),

    BOSS_INVASION(
        "Invasion de Boss",
        "⚔",
        ChatColor.DARK_PURPLE + "Une horde de boss envahit le monde!",
        25,
        false
    ),

    DOUBLE_XP(
        "XP Double",
        "✨",
        ChatColor.AQUA + "Période d'XP double activée!",
        60,
        true // VIP seulement
    ),

    SUPER_DROP(
        "Super Drops",
        "🎁",
        ChatColor.YELLOW + "Les mobs lâchent des loots incroyables!",
        30,
        false
    ),

    PEACEFUL_HOUR(
        "Heure Paisible",
        "☀",
        ChatColor.GREEN + "Une heure de paix et de régénération...",
        60,
        false
    ),

    CHAOS_STORM(
        "Tempête du Chaos",
        "⚡",
        ChatColor.LIGHT_PURPLE + "Le chaos déchaîné sur le monde!",
        15,
        true // VIP+
    );

    private final String displayName;
    private final String icon;
    private final String announcement;
    private final int durationMinutes;
    private final boolean premiumOnly;

    EventType(String displayName, String icon, String announcement, int durationMinutes, boolean premiumOnly) {
        this.displayName = displayName;
        this.icon = icon;
        this.announcement = announcement;
        this.durationMinutes = durationMinutes;
        this.premiumOnly = premiumOnly;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIcon() {
        return icon;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public boolean isPremiumOnly() {
        return premiumOnly;
    }

    public String getFormattedName() {
        return icon + " " + ChatColor.BOLD + displayName;
    }
}
