package fr.zinecraft.core.quests;

/**
 * Objectif d'une quête
 * 
 * @author Otmane & Copilot
 */
public class QuestObjective {
    
    private final int objectiveId;
    private final String objectiveKey;
    private final ObjectiveType objectiveType;
    private final String targetType;
    private final int targetAmount;
    private final String description;
    
    public QuestObjective(int objectiveId, String objectiveKey, ObjectiveType objectiveType,
                          String targetType, int targetAmount, String description) {
        this.objectiveId = objectiveId;
        this.objectiveKey = objectiveKey;
        this.objectiveType = objectiveType;
        this.targetType = targetType;
        this.targetAmount = targetAmount;
        this.description = description;
    }
    
    public int getObjectiveId() { return objectiveId; }
    public String getObjectiveKey() { return objectiveKey; }
    public ObjectiveType getObjectiveType() { return objectiveType; }
    public String getTargetType() { return targetType; }
    public int getTargetAmount() { return targetAmount; }
    public String getDescription() { return description; }
    
    /**
     * Types d'objectifs
     */
    public enum ObjectiveType {
        KILL("Tuer"),
        KILL_MOBS("Tuer des monstres"),
        KILL_PLAYERS("Tuer des joueurs"),
        MINE("Miner"),
        MINE_BLOCKS("Miner des blocs"),
        CRAFT("Crafter"),
        CRAFT_ITEMS("Crafter des items"),
        COLLECT_ITEMS("Collecter des items"),
        REACH_LOCATION("Atteindre un lieu"),
        TALK_TO_NPC("Parler à un PNJ"),
        INTERACT("Interagir"),
        DEFEAT_BOSS("Vaincre un boss"),
        REACH_LEVEL("Atteindre un niveau"),
        EARN_ZINES("Gagner des Zines");
        
        private final String displayName;
        
        ObjectiveType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}
