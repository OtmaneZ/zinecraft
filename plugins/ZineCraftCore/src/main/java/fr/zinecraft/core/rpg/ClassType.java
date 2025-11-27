package fr.zinecraft.core.rpg;

/**
 * Types de classes RPG disponibles
 * 
 * @author Otmane & Copilot
 */
public enum ClassType {
    
    // ==================== Aucune classe ====================
    
    NONE("Aucune", "❓",
        "Pas encore choisi de classe",
        false, 0),
    
    // ==================== Classes Gratuites ====================
    
    WARRIOR("Guerrier", "⚔", 
        "Maître du combat rapproché avec haute défense",
        false, 0),
    
    ARCHER("Archer", "🏹",
        "Expert du combat à distance et de la précision",
        false, 0),
    
    MAGE("Mage", "🔮",
        "Manipulateur de magie élémentaire puissante",
        false, 0),
    
    // ==================== Classes VIP ====================
    
    PALADIN("Paladin", "✨",
        "Guerrier sacré avec pouvoirs de soin",
        true, 15), // 15€
    
    ASSASSIN("Assassin", "🗡",
        "Tueur furtif avec dégâts critiques",
        true, 15), // 15€
    
    // ==================== Classes VIP+ ====================
    
    NECROMANCER("Nécromancien", "💀",
        "Invoque les morts pour combattre",
        true, 30), // 30€
    
    DRUID("Druide", "🌿",
        "Contrôle la nature et les animaux",
        true, 30), // 30€
    
    // ==================== Classe LEGEND ====================
    
    ARCHMAGE("Archimage", "⚡",
        "Maître suprême de toutes les magies",
        true, 60); // 60€
    
    private final String displayName;
    private final String icon;
    private final String description;
    private final boolean isPremium;
    private final int priceEuros;
    
    ClassType(String displayName, String icon, String description, boolean isPremium, int priceEuros) {
        this.displayName = displayName;
        this.icon = icon;
        this.description = description;
        this.isPremium = isPremium;
        this.priceEuros = priceEuros;
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
        return isPremium;
    }
    
    public int getPriceEuros() {
        return priceEuros;
    }
    
    /**
     * Stats de base selon la classe
     */
    public double getBaseHealth() {
        switch (this) {
            case WARRIOR:
            case PALADIN:
                return 24.0; // 12 coeurs
            case NECROMANCER:
            case ARCHMAGE:
                return 16.0; // 8 coeurs
            case ARCHER:
            case ASSASSIN:
            case DRUID:
                return 18.0; // 9 coeurs
            case MAGE:
                return 14.0; // 7 coeurs
            default:
                return 20.0; // 10 coeurs (défaut)
        }
    }
    
    public double getBaseDamage() {
        switch (this) {
            case WARRIOR:
                return 1.5;
            case ARCHER:
                return 1.3;
            case MAGE:
            case ARCHMAGE:
                return 1.2;
            case PALADIN:
                return 1.4;
            case ASSASSIN:
                return 2.0; // Critique
            case NECROMANCER:
                return 1.1;
            case DRUID:
                return 1.0;
            default:
                return 1.0;
        }
    }
    
    public double getBaseSpeed() {
        switch (this) {
            case ASSASSIN:
                return 1.3;
            case ARCHER:
            case DRUID:
                return 1.15;
            case MAGE:
            case ARCHMAGE:
            case NECROMANCER:
                return 1.0;
            case WARRIOR:
            case PALADIN:
                return 0.9;
            default:
                return 1.0;
        }
    }
    
    /**
     * Récupérer une classe par son nom
     */
    public static ClassType fromString(String name) {
        for (ClassType type : values()) {
            if (type.name().equalsIgnoreCase(name) || 
                type.getDisplayName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }
}
