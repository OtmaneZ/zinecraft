package fr.zinecraft.core.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import fr.zinecraft.core.ZineCraftCore;
import fr.zinecraft.core.events.ServerEvent.EventType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gestionnaire des événements automatiques du serveur
 * Version améliorée avec support multi-events, multiplicateurs, et scheduling intelligent
 *
 * @author Otmane & Copilot
 * @version 2.0
 */
public class EventManager {

    private final ZineCraftCore plugin;
    private final Map<EventType, ServerEvent> activeEvents;
    private final List<BukkitTask> scheduledTasks;
    private BukkitTask mainScheduler;

    // Configuration des intervalles
    private static final long EVENT_CHECK_INTERVAL = 60 * 20L; // Vérifier toutes les minutes (1200 ticks)
    private static final long EVENT_MIN_INTERVAL = 30 * 60; // 30 minutes entre events (en secondes)
    private static final long EVENT_MAX_INTERVAL = 90 * 60; // 90 minutes max
    
    private long lastEventTime;
    private Random random;

    public EventManager(ZineCraftCore plugin) {
        this.plugin = plugin;
        this.activeEvents = new ConcurrentHashMap<>();
        this.scheduledTasks = new ArrayList<>();
        this.random = new Random();
        this.lastEventTime = System.currentTimeMillis();
        
        plugin.getLogger().info("EventManager initialisé - Events automatiques activés");
        startAutoScheduler();
    }

    /**
     * Démarrer le scheduler automatique d'événements
     */
    private void startAutoScheduler() {
        mainScheduler = new BukkitRunnable() {
            @Override
            public void run() {
                // Nettoyer les events expirés
                cleanExpiredEvents();
                
                // Vérifier s'il est temps de lancer un nouvel event
                long timeSinceLastEvent = (System.currentTimeMillis() - lastEventTime) / 1000;
                long nextEventDelay = EVENT_MIN_INTERVAL + random.nextInt((int)(EVENT_MAX_INTERVAL - EVENT_MIN_INTERVAL));
                
                if (timeSinceLastEvent >= nextEventDelay && Bukkit.getOnlinePlayers().size() > 0) {
                    startRandomEvent();
                    lastEventTime = System.currentTimeMillis();
                }
            }
        }.runTaskTimer(plugin, EVENT_CHECK_INTERVAL, EVENT_CHECK_INTERVAL);
        
        plugin.getLogger().info("Scheduler automatique démarré - Prochain event dans 30-90 minutes");
    }
    
    /**
     * Nettoyer les événements expirés
     */
    private void cleanExpiredEvents() {
        activeEvents.entrySet().removeIf(entry -> {
            ServerEvent event = entry.getValue();
            if (event.hasExpired()) {
                endEvent(event.getType());
                return true;
            }
            return false;
        });
    }
    
    /**
     * Démarrer un événement aléatoire
     */
    public void startRandomEvent() {
        EventType[] allTypes = EventType.values();
        EventType randomType = allTypes[random.nextInt(allTypes.length)];
        startEvent(randomType);
    }

    /**
     * Démarrer un événement spécifique
     */
    public boolean startEvent(EventType type) {
        return startEvent(type, 600); // 10 minutes par défaut
    }
    
    /**
     * Démarrer un événement avec durée custom
     */
    public boolean startEvent(EventType type, long durationSeconds) {
        // Vérifier si cet event est déjà actif
        if (activeEvents.containsKey(type)) {
            return false;
        }
        
        // Créer l'événement
        ServerEvent event = new ServerEvent(type, durationSeconds);
        activeEvents.put(type, event);
        
        // Annoncer l'événement
        announceEventStart(event);
        
        // Programmer des rappels (à mi-temps, 1 min restante)
        scheduleReminders(event);
        
        // Lancer la logique spécifique de l'event
        executeEventLogic(event);
        
        plugin.getLogger().info("Event démarré: " + type.name() + " (durée: " + durationSeconds + "s)");
        return true;
    }

    /**
     * Annoncer le début d'un événement
     */
    private void announceEventStart(ServerEvent event) {
        String border = ChatColor.GOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬";
        
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage("");
            player.sendMessage(border);
            player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "      ⚡ ÉVÉNEMENT SERVEUR ⚡");
            player.sendMessage("");
            player.sendMessage("  " + ChatColor.AQUA + ChatColor.BOLD + event.getType().getDisplayName());
            player.sendMessage("");
            player.sendMessage("  " + ChatColor.WHITE + event.getType().getAnnouncement());
            player.sendMessage("");
            player.sendMessage(ChatColor.GRAY + "  Durée: " + ChatColor.YELLOW + (event.getDuration() / 60) + " minutes");
            player.sendMessage(border);
            player.sendMessage("");
            
            // Son d'alerte
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 0.5f, 1.0f);
        }
        
        // Broadcast global
        Bukkit.broadcastMessage(ChatColor.GOLD + "⚡ " + event.getType().getAnnouncement());
    }
    
    /**
     * Programmer des rappels pendant l'événement
     */
    private void scheduleReminders(ServerEvent event) {
        long duration = event.getDuration();
        
        // Rappel à mi-temps
        if (duration >= 120) { // Si event > 2 minutes
            BukkitTask halfwayTask = new BukkitRunnable() {
                @Override
                public void run() {
                    if (event.isActive()) {
                        Bukkit.broadcastMessage(ChatColor.YELLOW + "⏰ Événement " + 
                            ChatColor.AQUA + event.getType().getDisplayName() + 
                            ChatColor.YELLOW + " : " + event.getFormattedRemainingTime() + " restant !");
                    }
                }
            }.runTaskLater(plugin, (duration / 2) * 20L);
            scheduledTasks.add(halfwayTask);
        }
        
        // Rappel 1 minute avant la fin
        if (duration >= 120) {
            BukkitTask oneMinuteTask = new BukkitRunnable() {
                @Override
                public void run() {
                    if (event.isActive()) {
                        Bukkit.broadcastMessage(ChatColor.RED + "⚠ Événement " + 
                            ChatColor.AQUA + event.getType().getDisplayName() + 
                            ChatColor.RED + " : " + ChatColor.BOLD + "1 minute" + 
                            ChatColor.RED + " restante !");
                    }
                }
            }.runTaskLater(plugin, (duration - 60) * 20L);
            scheduledTasks.add(oneMinuteTask);
        }
    }

    /**
     * Exécuter la logique spécifique d'un événement
     */
    private void executeEventLogic(ServerEvent event) {
        EventType type = event.getType();
        
        switch (type) {
            case BOSS_SPAWN -> spawnRandomBoss(event);
            case METEOR_SHOWER -> startMeteorShower(event);
            case TREASURE_HUNT -> spawnTreasureChests(event);
            case MOB_APOCALYPSE -> startMobApocalypse(event);
            case PVP_EVENT -> startPvPTournament(event);
            case LOTTERY -> startLottery(event);
            default -> {
                // Events passifs (multiplicateurs) gérés automatiquement
            }
        }
    }
    
    /**
     * Terminer un événement spécifique
     */
    public void endEvent(EventType type) {
        ServerEvent event = activeEvents.remove(type);
        if (event == null) {
            return;
        }
        
        event.setActive(false);
        
        // Annoncer la fin
        Bukkit.broadcastMessage(ChatColor.YELLOW + "⏰ Événement " + 
            ChatColor.AQUA + type.getDisplayName() + 
            ChatColor.YELLOW + " terminé !");
        
        plugin.getLogger().info("Event terminé: " + type.name());
    }
    
    /**
     * Terminer tous les événements
     */
    public void endAllEvents() {
        new ArrayList<>(activeEvents.keySet()).forEach(this::endEvent);
    }

    // ==================== Vérifications d'état ====================
    
    /**
     * Vérifier si un type d'événement est actif
     */
    public boolean isEventActive(EventType type) {
        return activeEvents.containsKey(type) && activeEvents.get(type).isActive();
    }
    
    /**
     * Vérifier si au moins un événement est actif
     */
    public boolean hasActiveEvents() {
        return !activeEvents.isEmpty();
    }
    
    /**
     * Récupérer un événement actif
     */
    public ServerEvent getActiveEvent(EventType type) {
        return activeEvents.get(type);
    }
    
    /**
     * Récupérer tous les événements actifs
     */
    public Collection<ServerEvent> getActiveEvents() {
        return activeEvents.values();
    }
    
    /**
     * Obtenir le multiplicateur XP actuel (combiné si plusieurs events)
     */
    public double getCurrentXPMultiplier() {
        return activeEvents.values().stream()
            .filter(ServerEvent::affectsXP)
            .mapToDouble(ServerEvent::getMultiplier)
            .max()
            .orElse(1.0);
    }
    
    /**
     * Obtenir le multiplicateur Zines actuel
     */
    public double getCurrentZinesMultiplier() {
        return activeEvents.values().stream()
            .filter(ServerEvent::affectsZines)
            .mapToDouble(ServerEvent::getMultiplier)
            .max()
            .orElse(1.0);
    }
    
    /**
     * Obtenir le multiplicateur Minage actuel
     */
    public double getCurrentMiningMultiplier() {
        return activeEvents.values().stream()
            .filter(ServerEvent::affectsMining)
            .mapToDouble(ServerEvent::getMultiplier)
            .max()
            .orElse(1.0);
    }
    
    // ==================== Logique d'événements spécifiques ====================
    
    private void spawnRandomBoss(ServerEvent event) {
        // TODO: Intégration avec BossManager
        plugin.getLogger().info("Boss spawn event démarré - Intégration BossManager à faire");
    }
    
    private void startMeteorShower(ServerEvent event) {
        // TODO: Spawner des météores avec récompenses
        plugin.getLogger().info("Meteor shower démarré");
    }
    
    private void spawnTreasureChests(ServerEvent event) {
        // TODO: Spawner des coffres dans le monde
        plugin.getLogger().info("Treasure hunt démarré");
    }
    
    private void startMobApocalypse(ServerEvent event) {
        // TODO: Augmenter spawn rate mobs
        plugin.getLogger().info("Mob apocalypse démarré");
    }
    
    private void startPvPTournament(ServerEvent event) {
        // TODO: Téléporter joueurs dans arène
        plugin.getLogger().info("PvP tournament démarré");
    }
    
    private void startLottery(ServerEvent event) {
        // TODO: Système de loterie
        plugin.getLogger().info("Lottery démarrée");
    }
    
    /**
     * Arrêter le gestionnaire d'événements
     */
    public void shutdown() {
        plugin.getLogger().info("Arrêt de l'EventManager...");
        
        if (mainScheduler != null) {
            mainScheduler.cancel();
        }
        
        scheduledTasks.forEach(BukkitTask::cancel);
        scheduledTasks.clear();
        
        endAllEvents();
        
        plugin.getLogger().info("EventManager arrêté avec succès");
    }
}
