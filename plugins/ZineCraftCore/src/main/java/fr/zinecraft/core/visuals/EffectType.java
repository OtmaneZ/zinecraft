package fr.zinecraft.core.visuals;

import org.bukkit.ChatColor;

/**
 * Types d'effets visuels disponibles
 *
 * @author Otmane & Copilot
 */
public enum EffectType {

    FIRE_AURA(
        "Aura de Feu",
        "🔥",
        ChatColor.RED + "Flammes ardentes",
        false
    ),

    ICE_AURA(
        "Aura de Glace",
        "❄",
        ChatColor.AQUA + "Froid glacial",
        false
    ),

    MAGIC_AURA(
        "Aura Magique",
        "✨",
        ChatColor.LIGHT_PURPLE + "Pouvoir mystique",
        false
    ),

    HOLY_AURA(
        "Aura Sacrée",
        "✨",
        ChatColor.YELLOW + "Lumière divine",
        true // VIP
    ),

    SHADOW_AURA(
        "Aura d'Ombre",
        "🌑",
        ChatColor.DARK_GRAY + "Ténèbres",
        true // VIP
    ),

    NATURE_AURA(
        "Aura de Nature",
        "🌿",
        ChatColor.GREEN + "Force naturelle",
        true // VIP
    ),

    LIGHTNING_AURA(
        "Aura Électrique",
        "⚡",
        ChatColor.GOLD + "Foudre",
        true // VIP+
    ),

    RAINBOW_TRAIL(
        "Traînée Arc-en-ciel",
        "🌈",
        ChatColor.LIGHT_PURPLE + "Toutes les couleurs",
        true // VIP+
    );

    private final String displayName;
    private final String icon;
    private final String description;
    private final boolean premium;

    EffectType(String displayName, String icon, String description, boolean premium) {
        this.displayName = displayName;
        this.icon = icon;
        this.description = description;
        this.premium = premium;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPremium() {
        return premium;
    }

    public String getFormattedName() {
        return icon + " " + displayName;
    }
}
