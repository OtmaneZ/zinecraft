package fr.zinecraft.core.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import fr.zinecraft.core.ZineCraftCore;
import fr.zinecraft.core.events.EventManager;
import fr.zinecraft.core.events.ServerEvent;
import fr.zinecraft.core.events.ServerEvent.EventType;

/**
 * Commande /event pour gérer les événements serveur
 * Usage: /event [start|stop|list|info] [type]
 * 
 * @author Otmane & Copilot
 * @version 2.0
 */
public class EventCommand implements CommandExecutor {
    
    private final ZineCraftCore plugin;
    private final EventManager eventManager;
    
    public EventCommand(ZineCraftCore plugin) {
        this.plugin = plugin;
        this.eventManager = plugin.getEventManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        // Commande sans arguments = liste des events actifs
        if (args.length == 0) {
            showActiveEvents(sender);
            return true;
        }
        
        String action = args[0].toLowerCase();
        
        switch (action) {
            case "list" -> showEventTypes(sender);
            case "start" -> {
                if (!sender.hasPermission("zinecraft.admin")) {
                    sender.sendMessage(ChatColor.RED + "Permission refusée.");
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /event start <type>");
                    return true;
                }
                startEvent(sender, args[1]);
            }
            case "stop" -> {
                if (!sender.hasPermission("zinecraft.admin")) {
                    sender.sendMessage(ChatColor.RED + "Permission refusée.");
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /event stop <type>");
                    return true;
                }
                stopEvent(sender, args[1]);
            }
            case "info" -> {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /event info <type>");
                    return true;
                }
                showEventInfo(sender, args[1]);
            }
            case "active" -> showActiveEvents(sender);
            default -> {
                sender.sendMessage(ChatColor.RED + "Usage: /event [start|stop|list|info|active]");
                return true;
            }
        }
        
        return true;
    }
    
    /**
     * Afficher les événements actifs
     */
    private void showActiveEvents(CommandSender sender) {
        if (!eventManager.hasActiveEvents()) {
            sender.sendMessage(ChatColor.YELLOW + "Aucun événement actif actuellement.");
            return;
        }
        
        sender.sendMessage(ChatColor.GOLD + "━━━━━ Événements Actifs ━━━━━");
        
        for (ServerEvent event : eventManager.getActiveEvents()) {
            sender.sendMessage(ChatColor.AQUA + "▸ " + event.getType().getDisplayName());
            sender.sendMessage(ChatColor.GRAY + "  Temps restant: " + 
                ChatColor.WHITE + event.getFormattedRemainingTime());
            
            if (event.affectsXP() || event.affectsZines() || event.affectsMining()) {
                sender.sendMessage(ChatColor.GRAY + "  Bonus: " + 
                    ChatColor.GREEN + "x" + event.getMultiplier());
            }
        }
        
        sender.sendMessage(ChatColor.GOLD + "━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }
    
    /**
     * Afficher tous les types d'événements disponibles
     */
    private void showEventTypes(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "━━━━━ Types d'Événements ━━━━━");
        
        for (EventType type : EventType.values()) {
            sender.sendMessage(ChatColor.AQUA + "▸ " + type.name().toLowerCase());
            sender.sendMessage(ChatColor.GRAY + "  " + type.getDisplayName());
            sender.sendMessage(ChatColor.WHITE + "  " + type.getAnnouncement());
            sender.sendMessage("");
        }
        
        sender.sendMessage(ChatColor.GRAY + "Usage: /event start <type>");
        sender.sendMessage(ChatColor.GOLD + "━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }
    
    /**
     * Démarrer un événement
     */
    private void startEvent(CommandSender sender, String typeName) {
        try {
            EventType type = EventType.valueOf(typeName.toUpperCase());
            
            if (eventManager.isEventActive(type)) {
                sender.sendMessage(ChatColor.RED + "Cet événement est déjà actif !");
                return;
            }
            
            boolean success = eventManager.startEvent(type);
            
            if (success) {
                sender.sendMessage(ChatColor.GREEN + "✔ Événement " + 
                    type.getDisplayName() + ChatColor.GREEN + " démarré !");
            } else {
                sender.sendMessage(ChatColor.RED + "Impossible de démarrer l'événement.");
            }
            
        } catch (IllegalArgumentException e) {
            sender.sendMessage(ChatColor.RED + "Type d'événement invalide.");
            sender.sendMessage(ChatColor.GRAY + "Utilisez: /event list");
        }
    }
    
    /**
     * Arrêter un événement
     */
    private void stopEvent(CommandSender sender, String typeName) {
        try {
            EventType type = EventType.valueOf(typeName.toUpperCase());
            
            if (!eventManager.isEventActive(type)) {
                sender.sendMessage(ChatColor.RED + "Cet événement n'est pas actif.");
                return;
            }
            
            eventManager.endEvent(type);
            sender.sendMessage(ChatColor.GREEN + "✔ Événement arrêté.");
            
        } catch (IllegalArgumentException e) {
            sender.sendMessage(ChatColor.RED + "Type d'événement invalide.");
        }
    }
    
    /**
     * Afficher les infos d'un type d'événement
     */
    private void showEventInfo(CommandSender sender, String typeName) {
        try {
            EventType type = EventType.valueOf(typeName.toUpperCase());
            
            sender.sendMessage(ChatColor.GOLD + "━━━━━ " + type.getDisplayName() + " ━━━━━");
            sender.sendMessage(ChatColor.WHITE + type.getAnnouncement());
            sender.sendMessage("");
            
            boolean isActive = eventManager.isEventActive(type);
            sender.sendMessage(ChatColor.GRAY + "Statut: " + 
                (isActive ? ChatColor.GREEN + "Actif" : ChatColor.RED + "Inactif"));
            
            if (isActive) {
                ServerEvent event = eventManager.getActiveEvent(type);
                sender.sendMessage(ChatColor.GRAY + "Temps restant: " + 
                    ChatColor.WHITE + event.getFormattedRemainingTime());
            }
            
            sender.sendMessage(ChatColor.GOLD + "━━━━━━━━━━━━━━━━━━━━━━━━━━");
            
        } catch (IllegalArgumentException e) {
            sender.sendMessage(ChatColor.RED + "Type d'événement invalide.");
        }
    }
}
