package fr.zinecraft.core.quests;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente une quête avec objectifs et récompenses
 * 
 * @author Otmane & Copilot
 */
public class Quest {
    
    private final int questId;
    private final String questKey;
    private final String questName;
    private final String description;
    private final int requiredLevel;
    private final int rewardXP;
    private final int rewardZines;
    private final QuestType questType;
    private final List<QuestObjective> objectives;
    
    public Quest(int questId, String questKey, String questName, String description,
                 int requiredLevel, int rewardXP, int rewardZines, QuestType questType) {
        this.questId = questId;
        this.questKey = questKey;
        this.questName = questName;
        this.description = description;
        this.requiredLevel = requiredLevel;
        this.rewardXP = rewardXP;
        this.rewardZines = rewardZines;
        this.questType = questType;
        this.objectives = new ArrayList<>();
    }
    
    public int getQuestId() { return questId; }
    public String getQuestKey() { return questKey; }
    public String getQuestName() { return questName; }
    public String getDescription() { return description; }
    public int getRequiredLevel() { return requiredLevel; }
    public int getRewardXP() { return rewardXP; }
    public int getRewardZines() { return rewardZines; }
    public QuestType getQuestType() { return questType; }
    public List<QuestObjective> getObjectives() { return objectives; }
    
    public void addObjective(QuestObjective objective) {
        objectives.add(objective);
    }
    
    /**
     * Vérifier si tous les objectifs sont complétés
     */
    public boolean isCompleted(PlayerQuestProgress progress) {
        if (progress == null) {
            return false;
        }
        
        for (QuestObjective objective : objectives) {
            if (progress.getProgress(objective.getObjectiveKey()) < objective.getTargetAmount()) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Types de quêtes
     */
    public enum QuestType {
        MAIN("Quête principale"),
        SIDE("Quête secondaire"),
        DAILY("Quête journalière"),
        WEEKLY("Quête hebdomadaire"),
        REPEATABLE("Quête répétable"),
        TUTORIAL("Tutoriel");
        
        private final String displayName;
        
        QuestType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}
