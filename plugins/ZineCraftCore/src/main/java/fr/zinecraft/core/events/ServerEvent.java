package fr.zinecraft.core.events;

import org.bukkit.Location;
import java.util.HashMap;
import java.util.Map;

/**
 * Représente un événement serveur automatique
 */
public class ServerEvent {
    
    public enum EventType {
        DOUBLE_XP("Double XP", "⭐ Double XP actif pendant 10 minutes !"),
        DOUBLE_ZINES("Double Zines", "💰 Double Zines actif pendant 10 minutes !"),
        BOSS_SPAWN("Boss Spawn", "👹 Un boss sauvage est apparu !"),
        METEOR_SHOWER("Pluie de Météores", "☄️ Des météores tombent du ciel avec des récompenses !"),
        HAPPY_HOUR("Happy Hour", "🎉 Happy Hour : +50% XP et Zines !"),
        TREASURE_HUNT("Chasse au Trésor", "🗺️ Des coffres mystérieux sont apparus dans le monde !"),
        MOB_APOCALYPSE("Invasion de Mobs", "⚔️ Une horde de monstres envahit le serveur !"),
        MINING_BONUS("Bonus Minage", "⛏️ Les minerais donnent 3x plus de récompenses !"),
        PVP_EVENT("Tournoi PvP", "🏆 Tournoi PvP : Dernier survivant gagne 5000 Zines !"),
        LOTTERY("Loterie", "🎰 Loterie lancée : 1 gagnant gagnera 10,000 Zines !");
        
        private final String displayName;
        private final String announcement;
        
        EventType(String displayName, String announcement) {
            this.displayName = displayName;
            this.announcement = announcement;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        public String getAnnouncement() {
            return announcement;
        }
    }
    
    private final EventType type;
    private final long startTime;
    private final long duration; // en secondes
    private boolean active;
    private Location location; // pour events localisés (boss spawn, treasure, etc.)
    private Map<String, Object> metadata; // données custom par event
    
    public ServerEvent(EventType type, long duration) {
        this.type = type;
        this.startTime = System.currentTimeMillis();
        this.duration = duration;
        this.active = true;
        this.metadata = new HashMap<>();
    }
    
    public ServerEvent(EventType type, long duration, Location location) {
        this(type, duration);
        this.location = location;
    }
    
    public EventType getType() {
        return type;
    }
    
    public long getStartTime() {
        return startTime;
    }
    
    public long getDuration() {
        return duration;
    }
    
    public boolean isActive() {
        return active && !hasExpired();
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public boolean hasExpired() {
        return (System.currentTimeMillis() - startTime) >= (duration * 1000);
    }
    
    public long getRemainingTime() {
        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        return Math.max(0, duration - elapsed);
    }
    
    public String getFormattedRemainingTime() {
        long remaining = getRemainingTime();
        long minutes = remaining / 60;
        long seconds = remaining % 60;
        return String.format("%d:%02d", minutes, seconds);
    }
    
    public Location getLocation() {
        return location;
    }
    
    public void setLocation(Location location) {
        this.location = location;
    }
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(String key, Object value) {
        metadata.put(key, value);
    }
    
    public Object getMetadata(String key) {
        return metadata.get(key);
    }
    
    /**
     * Retourne le multiplicateur pour les events XP/Zines
     */
    public double getMultiplier() {
        return switch (type) {
            case DOUBLE_XP, DOUBLE_ZINES -> 2.0;
            case HAPPY_HOUR -> 1.5;
            case MINING_BONUS -> 3.0;
            default -> 1.0;
        };
    }
    
    /**
     * Vérifie si cet event affecte l'XP
     */
    public boolean affectsXP() {
        return type == EventType.DOUBLE_XP || type == EventType.HAPPY_HOUR;
    }
    
    /**
     * Vérifie si cet event affecte les Zines
     */
    public boolean affectsZines() {
        return type == EventType.DOUBLE_ZINES || type == EventType.HAPPY_HOUR;
    }
    
    /**
     * Vérifie si cet event affecte le minage
     */
    public boolean affectsMining() {
        return type == EventType.MINING_BONUS;
    }
}
