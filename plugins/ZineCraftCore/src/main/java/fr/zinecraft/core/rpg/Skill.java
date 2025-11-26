package fr.zinecraft.core.rpg;

/**
 * Représente une compétence RPG
 * 
 * @author Otmane & Copilot
 */
public class Skill {
    
    private final String name;
    private final String displayName;
    private final String description;
    private final ClassType requiredClass;
    private final int requiredLevel;
    private final int maxLevel;
    private final SkillType type;
    
    public Skill(String name, String displayName, String description, 
                 ClassType requiredClass, int requiredLevel, int maxLevel, SkillType type) {
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.requiredClass = requiredClass;
        this.requiredLevel = requiredLevel;
        this.maxLevel = maxLevel;
        this.type = type;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public ClassType getRequiredClass() {
        return requiredClass;
    }
    
    public int getRequiredLevel() {
        return requiredLevel;
    }
    
    public int getMaxLevel() {
        return maxLevel;
    }
    
    public SkillType getType() {
        return type;
    }
    
    /**
     * Types de compétences
     */
    public enum SkillType {
        PASSIVE,    // Bonus permanent
        ACTIVE,     // Compétence activable
        ULTIMATE    // Compétence ultime
    }
}
