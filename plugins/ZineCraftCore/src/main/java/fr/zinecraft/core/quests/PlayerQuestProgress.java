package fr.zinecraft.core.quests;

import java.util.HashMap;
import java.util.Map;

/**
 * Progression d'un joueur sur une quête
 * 
 * @author Otmane & Copilot
 */
public class PlayerQuestProgress {
    
    private final String playerUUID;
    private final int questId;
    private QuestStatus status;
    private final Map<String, Integer> objectiveProgress; // objectiveKey -> progress
    private long startedAt;
    private long completedAt;
    
    public PlayerQuestProgress(String playerUUID, int questId) {
        this.playerUUID = playerUUID;
        this.questId = questId;
        this.status = QuestStatus.NOT_STARTED;
        this.objectiveProgress = new HashMap<>();
        this.startedAt = 0;
        this.completedAt = 0;
    }
    
    public String getPlayerUUID() { return playerUUID; }
    public int getQuestId() { return questId; }
    public QuestStatus getStatus() { return status; }
    public void setStatus(QuestStatus status) { this.status = status; }
    public long getStartedAt() { return startedAt; }
    public void setStartedAt(long startedAt) { this.startedAt = startedAt; }
    public long getCompletedAt() { return completedAt; }
    public void setCompletedAt(long completedAt) { this.completedAt = completedAt; }
    
    /**
     * Obtenir la progression d'un objectif
     */
    public int getProgress(String objectiveKey) {
        return objectiveProgress.getOrDefault(objectiveKey, 0);
    }
    
    /**
     * Définir la progression d'un objectif
     */
    public void setProgress(String objectiveKey, int progress) {
        objectiveProgress.put(objectiveKey, progress);
    }
    
    /**
     * Incrémenter la progression d'un objectif
     */
    public void incrementProgress(String objectiveKey, int amount) {
        int current = getProgress(objectiveKey);
        objectiveProgress.put(objectiveKey, current + amount);
    }
    
    /**
     * Obtenir toutes les progressions
     */
    public Map<String, Integer> getAllProgress() {
        return new HashMap<>(objectiveProgress);
    }
    
    /**
     * Statuts de quête
     */
    public enum QuestStatus {
        NOT_STARTED("Non commencée"),
        IN_PROGRESS("En cours"),
        COMPLETED("Terminée"),
        FAILED("Échouée"),
        TURNED_IN("Rendue");
        
        private final String displayName;
        
        QuestStatus(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}
