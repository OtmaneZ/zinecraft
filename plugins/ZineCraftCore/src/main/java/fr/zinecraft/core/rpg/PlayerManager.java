package fr.zinecraft.core.rpg;

import fr.zinecraft.core.ZineCraftCore;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Gestionnaire des joueurs RPG avec persistance MySQL
 * 
 * @author Otmane & Copilot
 */
public class PlayerManager {
    
    private final ZineCraftCore plugin;
    private Connection connection;
    private final Map<UUID, RPGPlayer> playerCache;
    
    // Configuration BDD
    private static final String HOST = "zinecraft-mysql";
    private static final int PORT = 3306;
    private static final String DATABASE = "zinecraft";
    private static final String USERNAME = "zinecraft_user";
    private static final String PASSWORD = "zinecraft_password_2025";
    
    public PlayerManager(ZineCraftCore plugin) {
        this.plugin = plugin;
        this.playerCache = new HashMap<>();
        connectDatabase();
    }
    
    /**
     * Connexion à MySQL
     */
    private void connectDatabase() {
        try {
            String url = String.format("jdbc:mysql://%s:%d/%s?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true",
                    HOST, PORT, DATABASE);
            
            connection = DriverManager.getConnection(url, USERNAME, PASSWORD);
            plugin.getLogger().info("✔ Connexion MySQL établie !");
            
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "✘ Erreur connexion MySQL", e);
        }
    }
    
    /**
     * Vérifier et reconnecter si nécessaire
     */
    private void checkConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connectDatabase();
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Vérification connexion MySQL échouée", e);
        }
    }
    
    /**
     * Charger un joueur depuis la BDD (ou créer s'il n'existe pas)
     */
    public RPGPlayer loadPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        
        // Vérifier le cache d'abord
        if (playerCache.containsKey(uuid)) {
            return playerCache.get(uuid);
        }
        
        checkConnection();
        
        try {
            // Chercher dans la BDD
            String sql = "SELECT * FROM rpg_players WHERE uuid = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();
            
            RPGPlayer rpgPlayer;
            
            if (rs.next()) {
                // Joueur existant
                String classTypeStr = rs.getString("class_type");
                ClassType classType = classTypeStr != null ? ClassType.valueOf(classTypeStr) : null;
                
                rpgPlayer = new RPGPlayer(
                    uuid,
                    rs.getString("player_name"),
                    classType,
                    rs.getInt("level"),
                    rs.getInt("experience"),
                    rs.getInt("zines"),
                    rs.getInt("skill_points"),
                    rs.getTimestamp("last_login").getTime()
                );
                
                // Charger les compétences
                loadPlayerSkills(rpgPlayer);
                
                // Charger les stats
                loadPlayerStats(rpgPlayer);
                
            } else {
                // Nouveau joueur
                rpgPlayer = new RPGPlayer(uuid, player.getName());
                createNewPlayer(rpgPlayer);
            }
            
            // Mettre en cache
            playerCache.put(uuid, rpgPlayer);
            
            return rpgPlayer;
            
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Erreur chargement joueur " + player.getName(), e);
            return null;
        }
    }
    
    /**
     * Créer un nouveau joueur dans la BDD
     */
    private void createNewPlayer(RPGPlayer rpgPlayer) throws SQLException {
        String sql = "INSERT INTO rpg_players (uuid, player_name, level, experience, zines, skill_points) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, rpgPlayer.getUuid().toString());
        stmt.setString(2, rpgPlayer.getPlayerName());
        stmt.setInt(3, rpgPlayer.getLevel());
        stmt.setInt(4, rpgPlayer.getExperience());
        stmt.setInt(5, rpgPlayer.getZines());
        stmt.setInt(6, rpgPlayer.getSkillPoints());
        stmt.executeUpdate();
        
        // Créer les stats
        String statsSql = "INSERT INTO rpg_player_stats (player_uuid) VALUES (?)";
        PreparedStatement statsStmt = connection.prepareStatement(statsSql);
        statsStmt.setString(1, rpgPlayer.getUuid().toString());
        statsStmt.executeUpdate();
        
        plugin.getLogger().info("Nouveau joueur RPG créé : " + rpgPlayer.getPlayerName());
    }
    
    /**
     * Charger les compétences d'un joueur
     */
    private void loadPlayerSkills(RPGPlayer rpgPlayer) throws SQLException {
        String sql = "SELECT skill_name, skill_level FROM rpg_player_skills WHERE player_uuid = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, rpgPlayer.getUuid().toString());
        ResultSet rs = stmt.executeQuery();
        
        Map<String, Integer> skills = new HashMap<>();
        while (rs.next()) {
            skills.put(rs.getString("skill_name"), rs.getInt("skill_level"));
        }
        
        rpgPlayer.setUnlockedSkills(skills);
    }
    
    /**
     * Charger les stats d'un joueur
     */
    private void loadPlayerStats(RPGPlayer rpgPlayer) throws SQLException {
        String sql = "SELECT * FROM rpg_player_stats WHERE player_uuid = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, rpgPlayer.getUuid().toString());
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            rpgPlayer.setMobsKilled(rs.getInt("mobs_killed"));
            rpgPlayer.setPlayersKilled(rs.getInt("players_killed"));
            rpgPlayer.setDeaths(rs.getInt("deaths"));
            rpgPlayer.setBlocksMined(rs.getInt("blocks_mined"));
            rpgPlayer.setItemsCrafted(rs.getInt("items_crafted"));
            rpgPlayer.setBossesDefeated(rs.getInt("bosses_defeated"));
            rpgPlayer.setQuestsCompleted(rs.getInt("quests_completed"));
            rpgPlayer.setPlaytimeMinutes(rs.getInt("playtime_minutes"));
        }
    }
    
    /**
     * Sauvegarder un joueur dans la BDD
     */
    public void savePlayer(RPGPlayer rpgPlayer) {
        checkConnection();
        
        try {
            // Sauvegarder les données principales
            String sql = "UPDATE rpg_players SET player_name = ?, class_type = ?, level = ?, experience = ?, zines = ?, skill_points = ?, last_login = NOW() WHERE uuid = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, rpgPlayer.getPlayerName());
            stmt.setString(2, rpgPlayer.getClassType() != null ? rpgPlayer.getClassType().name() : null);
            stmt.setInt(3, rpgPlayer.getLevel());
            stmt.setInt(4, rpgPlayer.getExperience());
            stmt.setInt(5, rpgPlayer.getZines());
            stmt.setInt(6, rpgPlayer.getSkillPoints());
            stmt.setString(7, rpgPlayer.getUuid().toString());
            stmt.executeUpdate();
            
            // Sauvegarder les compétences
            savePlayerSkills(rpgPlayer);
            
            // Sauvegarder les stats
            savePlayerStats(rpgPlayer);
            
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Erreur sauvegarde joueur " + rpgPlayer.getPlayerName(), e);
        }
    }
    
    /**
     * Sauvegarder les compétences
     */
    private void savePlayerSkills(RPGPlayer rpgPlayer) throws SQLException {
        // Supprimer les anciennes
        String deleteSql = "DELETE FROM rpg_player_skills WHERE player_uuid = ?";
        PreparedStatement deleteStmt = connection.prepareStatement(deleteSql);
        deleteStmt.setString(1, rpgPlayer.getUuid().toString());
        deleteStmt.executeUpdate();
        
        // Insérer les nouvelles
        String insertSql = "INSERT INTO rpg_player_skills (player_uuid, skill_name, skill_level) VALUES (?, ?, ?)";
        PreparedStatement insertStmt = connection.prepareStatement(insertSql);
        
        for (Map.Entry<String, Integer> entry : rpgPlayer.getUnlockedSkills().entrySet()) {
            insertStmt.setString(1, rpgPlayer.getUuid().toString());
            insertStmt.setString(2, entry.getKey());
            insertStmt.setInt(3, entry.getValue());
            insertStmt.addBatch();
        }
        
        if (!rpgPlayer.getUnlockedSkills().isEmpty()) {
            insertStmt.executeBatch();
        }
    }
    
    /**
     * Sauvegarder les stats
     */
    private void savePlayerStats(RPGPlayer rpgPlayer) throws SQLException {
        String sql = "UPDATE rpg_player_stats SET mobs_killed = ?, players_killed = ?, deaths = ?, blocks_mined = ?, items_crafted = ?, bosses_defeated = ?, quests_completed = ?, playtime_minutes = ? WHERE player_uuid = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, rpgPlayer.getMobsKilled());
        stmt.setInt(2, rpgPlayer.getPlayersKilled());
        stmt.setInt(3, rpgPlayer.getDeaths());
        stmt.setInt(4, rpgPlayer.getBlocksMined());
        stmt.setInt(5, rpgPlayer.getItemsCrafted());
        stmt.setInt(6, rpgPlayer.getBossesDefeated());
        stmt.setInt(7, rpgPlayer.getQuestsCompleted());
        stmt.setInt(8, rpgPlayer.getPlaytimeMinutes());
        stmt.setString(9, rpgPlayer.getUuid().toString());
        stmt.executeUpdate();
    }
    
    /**
     * Décharger un joueur (déconnexion)
     */
    public void unloadPlayer(UUID uuid) {
        RPGPlayer rpgPlayer = playerCache.get(uuid);
        if (rpgPlayer != null) {
            savePlayer(rpgPlayer);
            playerCache.remove(uuid);
        }
    }
    
    /**
     * Récupérer un joueur du cache
     */
    public RPGPlayer getPlayer(UUID uuid) {
        return playerCache.get(uuid);
    }
    
    /**
     * Récupérer un joueur du cache par Player Bukkit
     */
    public RPGPlayer getPlayer(Player player) {
        return getPlayer(player.getUniqueId());
    }
    
    /**
     * Sauvegarder tous les joueurs
     */
    public void saveAllPlayers() {
        for (RPGPlayer rpgPlayer : playerCache.values()) {
            savePlayer(rpgPlayer);
        }
        plugin.getLogger().info("Tous les joueurs RPG sauvegardés (" + playerCache.size() + ")");
    }
    
    /**
     * Fermer la connexion BDD
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                saveAllPlayers();
                connection.close();
                plugin.getLogger().info("Connexion MySQL fermée");
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Erreur fermeture MySQL", e);
        }
    }
    
    /**
     * Obtenir la connexion MySQL (pour QuestManager)
     */
    public Connection getConnection() {
        return connection;
    }
}
