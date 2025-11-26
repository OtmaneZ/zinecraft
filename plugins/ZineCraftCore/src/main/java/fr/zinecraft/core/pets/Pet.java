package fr.zinecraft.core.pets;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Classe représentant un familier
 * 
 * @author Otmane & Adam
 */
public class Pet {
    
    private final UUID ownerId;
    private final PetType type;
    private Entity entity;
    private int level;
    private int experience;
    private String customName;
    
    public Pet(UUID ownerId, PetType type) {
        this.ownerId = ownerId;
        this.type = type;
        this.level = 1;
        this.experience = 0;
        this.customName = type.getDefaultName();
    }
    
    public UUID getOwnerId() {
        return ownerId;
    }
    
    public PetType getType() {
        return type;
    }
    
    public Entity getEntity() {
        return entity;
    }
    
    public void setEntity(Entity entity) {
        this.entity = entity;
    }
    
    public int getLevel() {
        return level;
    }
    
    public void setLevel(int level) {
        this.level = level;
    }
    
    public int getExperience() {
        return experience;
    }
    
    public void addExperience(int amount) {
        this.experience += amount;
        checkLevelUp();
    }
    
    public String getCustomName() {
        return customName;
    }
    
    public void setCustomName(String name) {
        this.customName = name;
    }
    
    /**
     * Vérifie si le pet peut évoluer
     */
    public boolean canEvolve() {
        return level >= type.getEvolutionLevel() && type.getEvolution() != null;
    }
    
    /**
     * Obtenir le type d'évolution
     */
    public PetType getEvolution() {
        return type.getEvolution();
    }
    
    /**
     * Vérifier et appliquer le level up
     */
    private void checkLevelUp() {
        int requiredXP = getRequiredExperience();
        if (experience >= requiredXP && level < 100) {
            level++;
            experience -= requiredXP;
        }
    }
    
    /**
     * XP requis pour le prochain niveau
     */
    public int getRequiredExperience() {
        return level * 100;
    }
    
    /**
     * Obtenir le pourcentage d'XP
     */
    public int getExperiencePercent() {
        return (experience * 100) / getRequiredExperience();
    }
}
