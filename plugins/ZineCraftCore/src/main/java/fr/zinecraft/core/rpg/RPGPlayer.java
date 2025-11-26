package fr.zinecraft.core.rpg;

import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

/**
 * Représente un joueur RPG avec ses stats et progression
 * 
 * @author Otmane & Copilot
 */
public class RPGPlayer {
    
    private final UUID uuid;
    private String playerName;
    private ClassType classType;
    private int level;
    private int experience;
    private int zines;
    private int skillPoints;
    private long lastLogin;
    
    // Cache des compétences débloquées
    private Map<String, Integer> unlockedSkills;
    
    // Stats de jeu
    private int mobsKilled;
    private int playersKilled;
    private int deaths;
    private int blocksMined;
    private int itemsCrafted;
    private int bossesDefeated;
    private int questsCompleted;
    private int playtimeMinutes;
    
    /**
     * Constructeur pour nouveau joueur
     */
    public RPGPlayer(UUID uuid, String playerName) {
        this.uuid = uuid;
        this.playerName = playerName;
        this.classType = null; // Pas encore choisi
        this.level = 1;
        this.experience = 0;
        this.zines = 0;
        this.skillPoints = 0;
        this.lastLogin = System.currentTimeMillis();
        this.unlockedSkills = new HashMap<>();
        
        // Stats initiales
        this.mobsKilled = 0;
        this.playersKilled = 0;
        this.deaths = 0;
        this.blocksMined = 0;
        this.itemsCrafted = 0;
        this.bossesDefeated = 0;
        this.questsCompleted = 0;
        this.playtimeMinutes = 0;
    }
    
    /**
     * Constructeur pour charger depuis la BDD
     */
    public RPGPlayer(UUID uuid, String playerName, ClassType classType, int level, 
                     int experience, int zines, int skillPoints, long lastLogin) {
        this.uuid = uuid;
        this.playerName = playerName;
        this.classType = classType;
        this.level = level;
        this.experience = experience;
        this.zines = zines;
        this.skillPoints = skillPoints;
        this.lastLogin = lastLogin;
        this.unlockedSkills = new HashMap<>();
    }
    
    // ==================== Getters & Setters ====================
    
    public UUID getUuid() {
        return uuid;
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
    public ClassType getClassType() {
        return classType;
    }
    
    public void setClassType(ClassType classType) {
        this.classType = classType;
    }
    
    public boolean hasChosenClass() {
        return classType != null;
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
    
    public void setExperience(int experience) {
        this.experience = experience;
    }
    
    public int getZines() {
        return zines;
    }
    
    public void setZines(int zines) {
        this.zines = zines;
    }
    
    public int getSkillPoints() {
        return skillPoints;
    }
    
    public void setSkillPoints(int skillPoints) {
        this.skillPoints = skillPoints;
    }
    
    public long getLastLogin() {
        return lastLogin;
    }
    
    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    public Map<String, Integer> getUnlockedSkills() {
        return unlockedSkills;
    }
    
    public void setUnlockedSkills(Map<String, Integer> skills) {
        this.unlockedSkills = skills;
    }
    
    // ==================== Stats Getters & Setters ====================
    
    public int getMobsKilled() {
        return mobsKilled;
    }
    
    public void setMobsKilled(int mobsKilled) {
        this.mobsKilled = mobsKilled;
    }
    
    public void incrementMobsKilled() {
        this.mobsKilled++;
    }
    
    public int getPlayersKilled() {
        return playersKilled;
    }
    
    public void setPlayersKilled(int playersKilled) {
        this.playersKilled = playersKilled;
    }
    
    public void incrementPlayersKilled() {
        this.playersKilled++;
    }
    
    public int getDeaths() {
        return deaths;
    }
    
    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }
    
    public void incrementDeaths() {
        this.deaths++;
    }
    
    public int getBlocksMined() {
        return blocksMined;
    }
    
    public void setBlocksMined(int blocksMined) {
        this.blocksMined = blocksMined;
    }
    
    public void incrementBlocksMined() {
        this.blocksMined++;
    }
    
    public int getItemsCrafted() {
        return itemsCrafted;
    }
    
    public void setItemsCrafted(int itemsCrafted) {
        this.itemsCrafted = itemsCrafted;
    }
    
    public void incrementItemsCrafted() {
        this.itemsCrafted++;
    }
    
    public int getBossesDefeated() {
        return bossesDefeated;
    }
    
    public void setBossesDefeated(int bossesDefeated) {
        this.bossesDefeated = bossesDefeated;
    }
    
    public void incrementBossesDefeated() {
        this.bossesDefeated++;
    }
    
    public int getQuestsCompleted() {
        return questsCompleted;
    }
    
    public void setQuestsCompleted(int questsCompleted) {
        this.questsCompleted = questsCompleted;
    }
    
    public void incrementQuestsCompleted() {
        this.questsCompleted++;
    }
    
    public int getPlaytimeMinutes() {
        return playtimeMinutes;
    }
    
    public void setPlaytimeMinutes(int playtimeMinutes) {
        this.playtimeMinutes = playtimeMinutes;
    }
    
    // ==================== Méthodes utilitaires ====================
    
    /**
     * Ajouter de l'XP et vérifier si level up
     * @return true si level up, false sinon
     */
    public boolean addExperience(int amount) {
        this.experience += amount;
        int requiredXP = getRequiredExperience();
        
        if (this.experience >= requiredXP) {
            return true; // Level up nécessaire
        }
        return false;
    }
    
    /**
     * Calculer l'XP requise pour le prochain niveau
     */
    public int getRequiredExperience() {
        // Formule progressive : 100 * level^1.5
        return (int) (100 * Math.pow(level, 1.5));
    }
    
    /**
     * Progression en pourcentage vers le prochain niveau
     */
    public double getProgressPercentage() {
        int required = getRequiredExperience();
        return (double) experience / required * 100.0;
    }
    
    /**
     * Ajouter des Zines
     */
    public void addZines(int amount) {
        this.zines += amount;
    }
    
    /**
     * Retirer des Zines
     * @return true si succès, false si pas assez de Zines
     */
    public boolean removeZines(int amount) {
        if (this.zines >= amount) {
            this.zines -= amount;
            return true;
        }
        return false;
    }
    
    /**
     * Vérifier si le joueur a débloqué une compétence
     */
    public boolean hasSkill(String skillName) {
        return unlockedSkills.containsKey(skillName);
    }
    
    /**
     * Obtenir le niveau d'une compétence
     */
    public int getSkillLevel(String skillName) {
        return unlockedSkills.getOrDefault(skillName, 0);
    }
    
    /**
     * Débloquer ou améliorer une compétence
     */
    public void unlockSkill(String skillName, int level) {
        unlockedSkills.put(skillName, level);
    }
}
