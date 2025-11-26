package fr.zinecraft.core.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import fr.zinecraft.core.ZineCraftCore;

import java.util.*;

/**
 * Gestionnaire des événements dynamiques du serveur
 *
 * @author Otmane & Copilot
 */
public class EventManager {

    private final ZineCraftCore plugin;
    private EventType currentEvent;
    private BukkitTask eventTask;
    private long eventStartTime;
    private boolean eventActive;

    // Cooldowns entre événements (en secondes)
    private static final int MIN_COOLDOWN = 600; // 10 minutes
    private static final int MAX_COOLDOWN = 1800; // 30 minutes

    // Gestionnaires d'événements spécifiques
    private MeteorStrikeEvent meteorEvent;
    private BloodMoonEvent bloodMoonEvent;
    private TreasureHuntEvent treasureEvent;
    private BossInvasionEvent bossInvasionEvent;

    public EventManager(ZineCraftCore plugin) {
        this.plugin = plugin;
        this.eventActive = false;

        // Initialiser les gestionnaires d'événements
        this.meteorEvent = new MeteorStrikeEvent(plugin);
        this.bloodMoonEvent = new BloodMoonEvent(plugin);
        this.treasureEvent = new TreasureHuntEvent(plugin);
        this.bossInvasionEvent = new BossInvasionEvent(plugin);

        startEventScheduler();
    }

    /**
     * Démarrer le système d'événements automatiques
     */
    private void startEventScheduler() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!eventActive) {
                    // Démarrer un événement aléatoire
                    startRandomEvent();
                }
            }
        }.runTaskTimer(plugin, 20L * MIN_COOLDOWN, 20L * MIN_COOLDOWN);
    }

    /**
     * Démarrer un événement aléatoire
     */
    public void startRandomEvent() {
        // Filtrer les événements premium si nécessaire
        List<EventType> availableEvents = new ArrayList<>();
        for (EventType type : EventType.values()) {
            // Pour l'instant, inclure tous les événements
            availableEvents.add(type);
        }

        if (!availableEvents.isEmpty()) {
            Random random = new Random();
            EventType randomEvent = availableEvents.get(random.nextInt(availableEvents.size()));
            startEvent(randomEvent);
        }
    }

    /**
     * Démarrer un événement spécifique
     */
    public void startEvent(EventType type) {
        if (eventActive) {
            return; // Un événement est déjà en cours
        }

        currentEvent = type;
        eventActive = true;
        eventStartTime = System.currentTimeMillis();

        // Annoncer l'événement
        announceEvent(type);

        // Démarrer l'événement spécifique
        switch (type) {
            case METEOR_STRIKE:
                meteorEvent.start();
                break;
            case BLOOD_MOON:
                bloodMoonEvent.start();
                break;
            case TREASURE_HUNT:
                treasureEvent.start();
                break;
            case BOSS_INVASION:
                bossInvasionEvent.start();
                break;
            case DOUBLE_XP:
                startDoubleXP();
                break;
            case SUPER_DROP:
                startSuperDrop();
                break;
            case PEACEFUL_HOUR:
                startPeacefulHour();
                break;
            case CHAOS_STORM:
                startChaosStorm();
                break;
        }

        // Programmer la fin de l'événement
        scheduleEventEnd(type.getDurationMinutes() * 60);
    }

    /**
     * Annoncer un événement à tous les joueurs
     */
    private void announceEvent(EventType type) {
        String border = ChatColor.GOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬";

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage("");
            player.sendMessage(border);
            player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "        🎉 ÉVÉNEMENT SPÉCIAL 🎉");
            player.sendMessage("");
            player.sendMessage("  " + type.getFormattedName());
            player.sendMessage("");
            player.sendMessage("  " + type.getAnnouncement());
            player.sendMessage("");
            player.sendMessage(ChatColor.GRAY + "  Durée: " + ChatColor.WHITE + type.getDurationMinutes() + " minutes");
            player.sendMessage(border);
            player.sendMessage("");
        }
    }

    /**
     * Programmer la fin d'un événement
     */
    private void scheduleEventEnd(int durationSeconds) {
        eventTask = new BukkitRunnable() {
            @Override
            public void run() {
                endEvent();
            }
        }.runTaskLater(plugin, 20L * durationSeconds);
    }

    /**
     * Terminer l'événement en cours
     */
    public void endEvent() {
        if (!eventActive) {
            return;
        }

        // Arrêter l'événement spécifique
        switch (currentEvent) {
            case METEOR_STRIKE:
                meteorEvent.stop();
                break;
            case BLOOD_MOON:
                bloodMoonEvent.stop();
                break;
            case TREASURE_HUNT:
                treasureEvent.stop();
                break;
            case BOSS_INVASION:
                bossInvasionEvent.stop();
                break;
            case DOUBLE_XP:
                endDoubleXP();
                break;
            case SUPER_DROP:
                endSuperDrop();
                break;
            case PEACEFUL_HOUR:
                endPeacefulHour();
                break;
            case CHAOS_STORM:
                endChaosStorm();
                break;
        }

        // Annoncer la fin
        Bukkit.broadcastMessage(ChatColor.YELLOW + "⚠ L'événement " +
            currentEvent.getFormattedName() + ChatColor.YELLOW + " est terminé!");

        eventActive = false;
        currentEvent = null;

        if (eventTask != null) {
            eventTask.cancel();
        }
    }

    /**
     * Vérifier si un événement est actif
     */
    public boolean isEventActive() {
        return eventActive;
    }

    /**
     * Récupérer l'événement actuel
     */
    public EventType getCurrentEvent() {
        return currentEvent;
    }

    // ==================== Événements Simples ====================

    private void startDoubleXP() {
        Bukkit.broadcastMessage(ChatColor.AQUA + "✨ Tous les gains d'XP sont doublés!");
    }

    private void endDoubleXP() {
        // Géré automatiquement
    }

    private void startSuperDrop() {
        Bukkit.broadcastMessage(ChatColor.YELLOW + "🎁 Les mobs lâchent maintenant des loots rares!");
    }

    private void endSuperDrop() {
        // Géré automatiquement
    }

    private void startPeacefulHour() {
        Bukkit.broadcastMessage(ChatColor.GREEN + "☀ Mobs hostiles désactivés, régénération améliorée!");
    }

    private void endPeacefulHour() {
        // Géré automatiquement
    }

    private void startChaosStorm() {
        Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "⚡ Le chaos règne! Tout peut arriver...");
    }

    private void endChaosStorm() {
        // Géré automatiquement
    }

    /**
     * Arrêter le gestionnaire d'événements
     */
    public void shutdown() {
        if (eventActive) {
            endEvent();
        }
    }
}
