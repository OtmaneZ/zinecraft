package fr.zinecraft.core.skills;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Classe abstraite de base pour toutes les compétences actives
 * Architecture modulaire pour faciliter l'ajout de nouvelles skills
 * 
 * @author Otmane & Copilot
 * @version 2.0
 */
public abstract class Skill {
    
    protected final String id;
    protected final String displayName;
    protected final String description;
    protected final int cooldownSeconds;
    protected final int manaCost;
    protected final int minLevel;
    protected final SkillType type;
    protected final SkillRarity rarity;
    
    /**
     * Types de compétences
     */
    public enum SkillType {
        OFFENSIVE("⚔️ Offensif", "Inflige des dégâts"),
        DEFENSIVE("🛡️ Défensif", "Protection et survie"),
        SUPPORT("💚 Support", "Aide aux alliés"),
        MOBILITY("⚡ Mobilité", "Déplacement rapide"),
        UTILITY("🔧 Utilitaire", "Effets variés"),
        ULTIMATE("🌟 Ultime", "Compétence puissante");
        
        private final String displayName;
        private final String description;
        
        SkillType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    /**
     * Rareté des compétences
     */
    public enum SkillRarity {
        COMMON("§f", "Commun"),
        UNCOMMON("§a", "Peu commun"),
        RARE("§9", "Rare"),
        EPIC("§5", "Épique"),
        LEGENDARY("§6", "Légendaire");
        
        private final String colorCode;
        private final String displayName;
        
        SkillRarity(String colorCode, String displayName) {
            this.colorCode = colorCode;
            this.displayName = displayName;
        }
        
        public String getColorCode() {
            return colorCode;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        public String getColor() {
            return colorCode;
        }
    }
    
    /**
     * Constructeur de base pour une compétence
     */
    protected Skill(String id, String displayName, String description, 
                   int cooldownSeconds, int manaCost, int minLevel, 
                   SkillType type, SkillRarity rarity) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.cooldownSeconds = cooldownSeconds;
        this.manaCost = manaCost;
        this.minLevel = minLevel;
        this.type = type;
        this.rarity = rarity;
    }
    
    // ==================== Méthodes abstraites ====================
    
    /**
     * Logique d'exécution de la compétence
     * @return true si la compétence a été exécutée avec succès
     */
    public abstract boolean execute(Player player);
    
    /**
     * Vérifications custom avant l'exécution (optionnel)
     * @return true si la compétence peut être utilisée
     */
    public boolean canUse(Player player) {
        return true; // Override dans les sous-classes si nécessaire
    }
    
    /**
     * Effets visuels/sonores de la compétence (optionnel)
     */
    public void playEffects(Player player) {
        // Override dans les sous-classes
    }
    
    /**
     * Message de succès custom (optionnel)
     */
    public String getSuccessMessage() {
        return "§a✔ " + displayName + " activé !";
    }
    
    // ==================== Getters ====================
    
    public String getId() {
        return id;
    }
    
    public String getDisplayName() {
        return rarity.getColorCode() + displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getCooldownSeconds() {
        return cooldownSeconds;
    }
    
    public int getManaCost() {
        return manaCost;
    }
    
    public int getMinLevel() {
        return minLevel;
    }
    
    public SkillType getType() {
        return type;
    }
    
    public SkillRarity getRarity() {
        return rarity;
    }
    
    /**
     * Retourne un lore formaté pour GUI/Items
     */
    public String[] getLore() {
        return new String[] {
            "§7" + description,
            "",
            "§7Type: " + type.getDisplayName(),
            "§7Rareté: " + rarity.getColorCode() + rarity.getDisplayName(),
            "§7Cooldown: §e" + cooldownSeconds + "s",
            "§7Mana: §b" + manaCost,
            "§7Niveau requis: §6" + minLevel,
            "",
            "§eClique pour utiliser !"
        };
    }
    
    /**
     * Retourne l'item représentant la skill (pour GUI)
     */
    public abstract ItemStack getIcon();
}
