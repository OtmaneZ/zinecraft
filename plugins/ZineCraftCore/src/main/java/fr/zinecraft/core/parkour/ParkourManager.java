package fr.zinecraft.core.parkour;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gestionnaire de parkour
 * 
 * @author Otmane & Adam
 */
public class ParkourManager {
    
    private final List<Location> checkpoints;
    private final Map<Player, ParkourSession> activeSessions;
    
    public ParkourManager() {
        this.checkpoints = new ArrayList<>();
        this.activeSessions = new HashMap<>();
    }
    
    /**
     * Démarrer une session de parkour
     */
    public void startParkour(Player player) {
        ParkourSession session = new ParkourSession(player);
        activeSessions.put(player, session);
        
        player.sendMessage("");
        player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "🏃 PARKOUR DÉMARRÉ!");
        player.sendMessage(ChatColor.GRAY + "Saute sur les blocs pour avancer!");
        player.sendMessage(ChatColor.YELLOW + "Temps: " + ChatColor.WHITE + "0.0s");
        player.sendMessage("");
    }
    
    /**
     * Vérifier si un joueur est sur une plateforme de parkour
     */
    public void checkPlatform(Player player, Location location) {
        if (!activeSessions.containsKey(player)) {
            return;
        }
        
        Material blockType = location.getBlock().getType();
        
        // Checkpoint (bloc de diamant)
        if (blockType == Material.DIAMOND_BLOCK) {
            onCheckpoint(player, location);
        }
        // Fin (bloc d'émeraude)
        else if (blockType == Material.EMERALD_BLOCK) {
            onFinish(player);
        }
        // Échec (lave/eau)
        else if (blockType == Material.LAVA || blockType == Material.WATER) {
            onFail(player);
        }
    }
    
    /**
     * Checkpoint atteint
     */
    private void onCheckpoint(Player player, Location location) {
        ParkourSession session = activeSessions.get(player);
        if (session == null) return;
        
        session.addCheckpoint(location);
        
        player.sendMessage(ChatColor.AQUA + "✓ Checkpoint " + session.getCheckpointCount() + " atteint!");
        player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
    }
    
    /**
     * Parkour terminé
     */
    private void onFinish(Player player) {
        ParkourSession session = activeSessions.remove(player);
        if (session == null) return;
        
        double timeInSeconds = session.getElapsedTime() / 1000.0;
        
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "🏆 PARKOUR TERMINÉ!");
        player.sendMessage(ChatColor.YELLOW + "Temps: " + ChatColor.WHITE + String.format("%.2f", timeInSeconds) + "s");
        player.sendMessage(ChatColor.YELLOW + "Checkpoints: " + ChatColor.WHITE + session.getCheckpointCount());
        player.sendMessage("");
        
        player.playSound(player.getLocation(), org.bukkit.Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
    }
    
    /**
     * Échec du parkour
     */
    private void onFail(Player player) {
        ParkourSession session = activeSessions.get(player);
        if (session == null) return;
        
        Location lastCheckpoint = session.getLastCheckpoint();
        
        if (lastCheckpoint != null) {
            player.teleport(lastCheckpoint);
            player.sendMessage(ChatColor.RED + "✗ Retour au dernier checkpoint!");
        } else {
            activeSessions.remove(player);
            player.sendMessage(ChatColor.RED + "✗ Parkour échoué!");
        }
        
        player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
    }
    
    /**
     * Arrêter le parkour
     */
    public void stopParkour(Player player) {
        ParkourSession session = activeSessions.remove(player);
        if (session != null) {
            player.sendMessage(ChatColor.YELLOW + "Parkour arrêté!");
        }
    }
    
    /**
     * Vérifier si un joueur est en parkour
     */
    public boolean isInParkour(Player player) {
        return activeSessions.containsKey(player);
    }
    
    /**
     * Session de parkour d'un joueur
     */
    private static class ParkourSession {
        private final Player player;
        private final long startTime;
        private final List<Location> checkpoints;
        
        public ParkourSession(Player player) {
            this.player = player;
            this.startTime = System.currentTimeMillis();
            this.checkpoints = new ArrayList<>();
        }
        
        public void addCheckpoint(Location location) {
            checkpoints.add(location);
        }
        
        public Location getLastCheckpoint() {
            if (checkpoints.isEmpty()) {
                return null;
            }
            return checkpoints.get(checkpoints.size() - 1);
        }
        
        public int getCheckpointCount() {
            return checkpoints.size();
        }
        
        public long getElapsedTime() {
            return System.currentTimeMillis() - startTime;
        }
    }
}
