package fr.zinecraft.core.quests;

import fr.zinecraft.core.ZineCraftCore;
import fr.zinecraft.core.economy.EconomyManager;
import fr.zinecraft.core.rpg.LevelManager;
import fr.zinecraft.core.rpg.PlayerManager;
import fr.zinecraft.core.rpg.RPGPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.FireworkEffect;
import org.bukkit.Color;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;

/**
 * Gestionnaire du système de quêtes
 * 
 * @author Otmane & Copilot
 */
public class QuestManager {
    
    private final ZineCraftCore plugin;
    private final PlayerManager playerManager;
    private final LevelManager levelManager;
    private final EconomyManager economyManager;
    private final Map<Integer, Quest> quests; // questId -> Quest
    private final Map<String, List<PlayerQuestProgress>> playerQuests; // UUID -> List<Progress>
    
    public QuestManager(ZineCraftCore plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
        this.levelManager = plugin.getLevelManager();
        this.economyManager = plugin.getEconomyManager();
        this.quests = new HashMap<>();
        this.playerQuests = new HashMap<>();
        
        loadQuests();
    }
    
    // ==================== Chargement des quêtes ====================
    
    /**
     * Charger toutes les quêtes depuis la BDD
     */
    private void loadQuests() {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                Connection conn = playerManager.getConnection();
                
                // Charger les quêtes
                String sql = "SELECT * FROM rpg_quests ORDER BY id";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    int questId = rs.getInt("id");
                    String questKey = rs.getString("quest_key");
                    String questName = rs.getString("quest_name");
                    String description = rs.getString("description");
                    int requiredLevel = rs.getInt("required_level");
                    int rewardXP = rs.getInt("reward_xp");
                    int rewardZines = rs.getInt("reward_zines");
                    String questTypeStr = rs.getString("quest_type");
                    
                    Quest.QuestType questType = Quest.QuestType.valueOf(questTypeStr);
                    
                    Quest quest = new Quest(questId, questKey, questName, description,
                                          requiredLevel, rewardXP, rewardZines, questType);
                    
                    // Charger les objectifs
                    loadQuestObjectives(quest);
                    
                    quests.put(questId, quest);
                }
                
                rs.close();
                stmt.close();
                
                plugin.getLogger().info("✔ " + quests.size() + " quêtes chargées depuis la BDD!");
                
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, "Erreur chargement quêtes", e);
            }
        });
    }
    
    /**
     * Charger les objectifs d'une quête
     */
    private void loadQuestObjectives(Quest quest) throws SQLException {
        Connection conn = playerManager.getConnection();
        
        String sql = "SELECT * FROM rpg_quest_objectives WHERE quest_id = ? ORDER BY id";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, quest.getQuestId());
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            int objectiveId = rs.getInt("id");
            String objectiveKey = rs.getString("objective_key");
            String objectiveTypeStr = rs.getString("objective_type");
            String targetType = rs.getString("target_type");
            int targetAmount = rs.getInt("target_amount");
            String description = objectiveKey; // Utiliser la clé comme description par défaut
            
            QuestObjective.ObjectiveType objectiveType = 
                QuestObjective.ObjectiveType.valueOf(objectiveTypeStr);
            
            QuestObjective objective = new QuestObjective(objectiveId, objectiveKey, objectiveType,
                                                         targetType, targetAmount, description);
            quest.addObjective(objective);
        }
        
        rs.close();
        stmt.close();
    }
    
    // ==================== Gestion des quêtes joueur ====================
    
    /**
     * Charger les quêtes d'un joueur
     */
    public void loadPlayerQuests(Player player) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                Connection conn = playerManager.getConnection();
                String uuid = player.getUniqueId().toString();
                List<PlayerQuestProgress> progressList = new ArrayList<>();
                
                String sql = "SELECT * FROM rpg_player_quests WHERE player_uuid = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, uuid);
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    int questId = rs.getInt("quest_id");
                    String statusStr = rs.getString("status");
                    String progressJson = rs.getString("progress");
                    long startedAt = rs.getTimestamp("started_at").getTime();
                    Timestamp completedTimestamp = rs.getTimestamp("completed_at");
                    long completedAt = completedTimestamp != null ? completedTimestamp.getTime() : 0;
                    
                    PlayerQuestProgress progress = new PlayerQuestProgress(uuid, questId);
                    progress.setStatus(PlayerQuestProgress.QuestStatus.valueOf(statusStr));
                    progress.setStartedAt(startedAt);
                    progress.setCompletedAt(completedAt);
                    
                    // Parser le JSON de progression (format simple: "key1:value1,key2:value2")
                    if (progressJson != null && !progressJson.isEmpty()) {
                        String[] entries = progressJson.split(",");
                        for (String entry : entries) {
                            String[] parts = entry.split(":");
                            if (parts.length == 2) {
                                progress.setProgress(parts[0], Integer.parseInt(parts[1]));
                            }
                        }
                    }
                    
                    progressList.add(progress);
                }
                
                rs.close();
                stmt.close();
                
                playerQuests.put(uuid, progressList);
                
            } catch (SQLException e) {
                plugin.getLogger().log(Level.WARNING, "Erreur chargement quêtes joueur", e);
            }
        });
    }
    
    /**
     * Sauvegarder les quêtes d'un joueur
     */
    public void savePlayerQuests(Player player) {
        String uuid = player.getUniqueId().toString();
        List<PlayerQuestProgress> progressList = playerQuests.get(uuid);
        
        if (progressList == null) {
            return;
        }
        
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                Connection conn = playerManager.getConnection();
                
                for (PlayerQuestProgress progress : progressList) {
                    // Convertir la progression en JSON simple
                    StringBuilder progressJson = new StringBuilder();
                    Map<String, Integer> allProgress = progress.getAllProgress();
                    
                    for (Map.Entry<String, Integer> entry : allProgress.entrySet()) {
                        if (progressJson.length() > 0) {
                            progressJson.append(",");
                        }
                        progressJson.append(entry.getKey()).append(":").append(entry.getValue());
                    }
                    
                    String sql = "INSERT INTO rpg_player_quests (player_uuid, quest_id, status, progress, started_at, completed_at) " +
                               "VALUES (?, ?, ?, ?, ?, ?) " +
                               "ON DUPLICATE KEY UPDATE status = ?, progress = ?, completed_at = ?";
                    
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, uuid);
                    stmt.setInt(2, progress.getQuestId());
                    stmt.setString(3, progress.getStatus().name());
                    stmt.setString(4, progressJson.toString());
                    stmt.setTimestamp(5, new Timestamp(progress.getStartedAt()));
                    
                    if (progress.getCompletedAt() > 0) {
                        stmt.setTimestamp(6, new Timestamp(progress.getCompletedAt()));
                    } else {
                        stmt.setNull(6, Types.TIMESTAMP);
                    }
                    
                    stmt.setString(7, progress.getStatus().name());
                    stmt.setString(8, progressJson.toString());
                    
                    if (progress.getCompletedAt() > 0) {
                        stmt.setTimestamp(9, new Timestamp(progress.getCompletedAt()));
                    } else {
                        stmt.setNull(9, Types.TIMESTAMP);
                    }
                    
                    stmt.executeUpdate();
                    stmt.close();
                }
                
            } catch (SQLException e) {
                plugin.getLogger().log(Level.WARNING, "Erreur sauvegarde quêtes joueur", e);
            }
        });
    }
    
    // ==================== Actions de quêtes ====================
    
    /**
     * Démarrer une quête
     */
    public boolean startQuest(Player player, int questId) {
        Quest quest = quests.get(questId);
        
        if (quest == null) {
            player.sendMessage(ChatColor.RED + "Quête introuvable!");
            return false;
        }
        
        RPGPlayer rpgPlayer = playerManager.getPlayer(player);
        
        if (rpgPlayer == null) {
            return false;
        }
        
        // Vérifier le niveau requis
        if (rpgPlayer.getLevel() < quest.getRequiredLevel()) {
            player.sendMessage(ChatColor.RED + "Niveau " + quest.getRequiredLevel() + " requis!");
            return false;
        }
        
        // Vérifier si déjà commencée
        String uuid = player.getUniqueId().toString();
        List<PlayerQuestProgress> progressList = playerQuests.computeIfAbsent(uuid, k -> new ArrayList<>());
        
        for (PlayerQuestProgress progress : progressList) {
            if (progress.getQuestId() == questId) {
                if (progress.getStatus() == PlayerQuestProgress.QuestStatus.IN_PROGRESS) {
                    player.sendMessage(ChatColor.RED + "Vous avez déjà commencé cette quête!");
                    return false;
                } else if (progress.getStatus() == PlayerQuestProgress.QuestStatus.TURNED_IN) {
                    player.sendMessage(ChatColor.RED + "Vous avez déjà terminé cette quête!");
                    return false;
                }
            }
        }
        
        // Créer la progression
        PlayerQuestProgress progress = new PlayerQuestProgress(uuid, questId);
        progress.setStatus(PlayerQuestProgress.QuestStatus.IN_PROGRESS);
        progress.setStartedAt(System.currentTimeMillis());
        
        // Initialiser les objectifs à 0
        for (QuestObjective objective : quest.getObjectives()) {
            progress.setProgress(objective.getObjectiveKey(), 0);
        }
        
        progressList.add(progress);
        
        // Notification
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "═══════════════════════════════");
        player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "  📜 NOUVELLE QUÊTE 📜");
        player.sendMessage("");
        player.sendMessage(ChatColor.GREEN + quest.getQuestName());
        player.sendMessage(ChatColor.GRAY + quest.getDescription());
        player.sendMessage("");
        player.sendMessage(ChatColor.YELLOW + "Objectifs:");
        
        for (QuestObjective objective : quest.getObjectives()) {
            player.sendMessage(ChatColor.WHITE + "  • " + objective.getDescription());
        }
        
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "Récompenses:");
        player.sendMessage(ChatColor.AQUA + "  +" + quest.getRewardXP() + " XP");
        player.sendMessage(ChatColor.YELLOW + "  +" + quest.getRewardZines() + " Zines");
        player.sendMessage(ChatColor.GOLD + "═══════════════════════════════");
        player.sendMessage("");
        
        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
        
        savePlayerQuests(player);
        
        return true;
    }
    
    /**
     * Compléter une quête et donner les récompenses
     */
    public boolean completeQuest(Player player, int questId) {
        Quest quest = quests.get(questId);
        
        if (quest == null) {
            return false;
        }
        
        String uuid = player.getUniqueId().toString();
        List<PlayerQuestProgress> progressList = playerQuests.get(uuid);
        
        if (progressList == null) {
            return false;
        }
        
        PlayerQuestProgress progress = null;
        for (PlayerQuestProgress p : progressList) {
            if (p.getQuestId() == questId) {
                progress = p;
                break;
            }
        }
        
        if (progress == null || progress.getStatus() != PlayerQuestProgress.QuestStatus.IN_PROGRESS) {
            player.sendMessage(ChatColor.RED + "Cette quête n'est pas en cours!");
            return false;
        }
        
        // Vérifier si tous les objectifs sont complétés
        if (!quest.isCompleted(progress)) {
            player.sendMessage(ChatColor.RED + "Vous n'avez pas terminé tous les objectifs!");
            return false;
        }
        
        // Marquer comme complétée
        progress.setStatus(PlayerQuestProgress.QuestStatus.COMPLETED);
        progress.setCompletedAt(System.currentTimeMillis());
        
        // Donner les récompenses
        giveQuestRewards(player, quest);
        
        // Incrémenter le compteur de quêtes terminées
        RPGPlayer rpgPlayer = playerManager.getPlayer(player);
        if (rpgPlayer != null) {
            rpgPlayer.setQuestsCompleted(rpgPlayer.getQuestsCompleted() + 1);
            playerManager.savePlayer(rpgPlayer);
        }
        
        savePlayerQuests(player);
        
        return true;
    }
    
    /**
     * Donner les récompenses d'une quête
     */
    private void giveQuestRewards(Player player, Quest quest) {
        // XP
        levelManager.addExperience(player, quest.getRewardXP(), "Quête: " + quest.getQuestName());
        
        // Zines
        economyManager.addZines(player, quest.getRewardZines(), "Quête: " + quest.getQuestName());
        
        // Notification
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "═══════════════════════════════");
        player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "  ✔ QUÊTE TERMINÉE ! ✔");
        player.sendMessage("");
        player.sendMessage(ChatColor.YELLOW + quest.getQuestName());
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "Récompenses reçues:");
        player.sendMessage(ChatColor.AQUA + "  +" + quest.getRewardXP() + " XP");
        player.sendMessage(ChatColor.YELLOW + "  +" + quest.getRewardZines() + " Zines");
        player.sendMessage(ChatColor.GOLD + "═══════════════════════════════");
        player.sendMessage("");
        
        // Effets
        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.2f);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);
    }
    
    /**
     * Mettre à jour la progression d'un objectif
     */
    public void updateObjectiveProgress(Player player, QuestObjective.ObjectiveType type, 
                                       String targetType, int amount) {
        String uuid = player.getUniqueId().toString();
        List<PlayerQuestProgress> progressList = playerQuests.get(uuid);
        
        if (progressList == null) {
            return;
        }
        
        for (PlayerQuestProgress progress : progressList) {
            if (progress.getStatus() != PlayerQuestProgress.QuestStatus.IN_PROGRESS) {
                continue;
            }
            
            Quest quest = quests.get(progress.getQuestId());
            if (quest == null) {
                continue;
            }
            
            boolean updated = false;
            
            for (QuestObjective objective : quest.getObjectives()) {
                if (objective.getObjectiveType() == type && 
                    (targetType == null || objective.getTargetType().equalsIgnoreCase(targetType))) {
                    
                    int currentProgress = progress.getProgress(objective.getObjectiveKey());
                    
                    if (currentProgress < objective.getTargetAmount()) {
                        progress.incrementProgress(objective.getObjectiveKey(), amount);
                        updated = true;
                        
                        int newProgress = progress.getProgress(objective.getObjectiveKey());
                        
                        // Notification
                        if (newProgress >= objective.getTargetAmount()) {
                            player.sendMessage(ChatColor.GREEN + "✔ Objectif terminé: " + 
                                             ChatColor.YELLOW + objective.getDescription());
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.5f);
                        } else {
                            player.sendMessage(ChatColor.GRAY + "Progression: " + 
                                             ChatColor.WHITE + objective.getDescription() + 
                                             ChatColor.YELLOW + " (" + newProgress + "/" + 
                                             objective.getTargetAmount() + ")");
                        }
                        
                        // Vérifier si la quête est complète
                        if (quest.isCompleted(progress)) {
                            player.sendMessage("");
                            player.sendMessage(ChatColor.GOLD + "✔ Quête prête à être rendue: " + 
                                             ChatColor.YELLOW + quest.getQuestName());
                            player.sendMessage(ChatColor.GRAY + "Utilisez " + ChatColor.WHITE + 
                                             "/quest complete " + quest.getQuestKey());
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
                        }
                    }
                }
            }
            
            if (updated) {
                savePlayerQuests(player);
            }
        }
    }
    
    // ==================== Getters ====================
    
    public Quest getQuest(int questId) {
        return quests.get(questId);
    }
    
    public Quest getQuestByKey(String questKey) {
        for (Quest quest : quests.values()) {
            if (quest.getQuestKey().equalsIgnoreCase(questKey)) {
                return quest;
            }
        }
        return null;
    }
    
    public List<Quest> getAllQuests() {
        return new ArrayList<>(quests.values());
    }
    
    public List<Quest> getAvailableQuests(Player player) {
        RPGPlayer rpgPlayer = playerManager.getPlayer(player);
        
        if (rpgPlayer == null) {
            return new ArrayList<>();
        }
        
        List<Quest> available = new ArrayList<>();
        String uuid = player.getUniqueId().toString();
        List<PlayerQuestProgress> progressList = playerQuests.get(uuid);
        
        for (Quest quest : quests.values()) {
            // Vérifier le niveau
            if (rpgPlayer.getLevel() < quest.getRequiredLevel()) {
                continue;
            }
            
            // Vérifier si déjà faite
            boolean alreadyDone = false;
            if (progressList != null) {
                for (PlayerQuestProgress progress : progressList) {
                    if (progress.getQuestId() == quest.getQuestId() && 
                        (progress.getStatus() == PlayerQuestProgress.QuestStatus.IN_PROGRESS ||
                         progress.getStatus() == PlayerQuestProgress.QuestStatus.TURNED_IN)) {
                        alreadyDone = true;
                        break;
                    }
                }
            }
            
            if (!alreadyDone) {
                available.add(quest);
            }
        }
        
        return available;
    }
    
    public List<PlayerQuestProgress> getPlayerQuests(Player player) {
        return playerQuests.getOrDefault(player.getUniqueId().toString(), new ArrayList<>());
    }
    
    public PlayerQuestProgress getPlayerQuestProgress(Player player, int questId) {
        List<PlayerQuestProgress> progressList = getPlayerQuests(player);
        
        for (PlayerQuestProgress progress : progressList) {
            if (progress.getQuestId() == questId) {
                return progress;
            }
        }
        
        return null;
    }
}
