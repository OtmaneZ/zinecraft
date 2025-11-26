package fr.zinecraft.core.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import fr.zinecraft.core.ZineCraftCore;
import fr.zinecraft.core.events.EventManager;
import fr.zinecraft.core.events.EventType;

/**
 * Commande pour gérer les événements dynamiques
 *
 * @author Otmane & Copilot
 */
public class EventCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "start":
                if (!sender.hasPermission("zinecraft.event.start")) {
                    sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission!");
                    return true;
                }

                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /event start <type>");
                    listEventTypes(sender);
                    return true;
                }

                startEvent(sender, args[1]);
                break;

            case "stop":
                if (!sender.hasPermission("zinecraft.event.stop")) {
                    sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission!");
                    return true;
                }

                stopEvent(sender);
                break;

            case "info":
                showEventInfo(sender);
                break;

            case "list":
                listEventTypes(sender);
                break;

            default:
                sendHelp(sender);
                break;
        }

        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "=== Commandes Événements ===");
        sender.sendMessage(ChatColor.YELLOW + "/event start <type> " +
            ChatColor.GRAY + "- Démarrer un événement");
        sender.sendMessage(ChatColor.YELLOW + "/event stop " +
            ChatColor.GRAY + "- Arrêter l'événement en cours");
        sender.sendMessage(ChatColor.YELLOW + "/event info " +
            ChatColor.GRAY + "- Infos sur l'événement actuel");
        sender.sendMessage(ChatColor.YELLOW + "/event list " +
            ChatColor.GRAY + "- Liste des événements");
    }

    private void startEvent(CommandSender sender, String typeName) {
        EventManager eventManager = ZineCraftCore.getInstance().getEventManager();

        if (eventManager.isEventActive()) {
            sender.sendMessage(ChatColor.RED + "Un événement est déjà en cours!");
            return;
        }

        try {
            EventType type = EventType.valueOf(typeName.toUpperCase());
            eventManager.startEvent(type);
            sender.sendMessage(ChatColor.GREEN + "✔ Événement " +
                type.getFormattedName() + ChatColor.GREEN + " démarré!");
        } catch (IllegalArgumentException e) {
            sender.sendMessage(ChatColor.RED + "Type d'événement invalide!");
            listEventTypes(sender);
        }
    }

    private void stopEvent(CommandSender sender) {
        EventManager eventManager = ZineCraftCore.getInstance().getEventManager();

        if (!eventManager.isEventActive()) {
            sender.sendMessage(ChatColor.RED + "Aucun événement en cours!");
            return;
        }

        eventManager.endEvent();
        sender.sendMessage(ChatColor.GREEN + "✔ Événement arrêté!");
    }

    private void showEventInfo(CommandSender sender) {
        EventManager eventManager = ZineCraftCore.getInstance().getEventManager();

        if (!eventManager.isEventActive()) {
            sender.sendMessage(ChatColor.YELLOW + "Aucun événement en cours.");
            return;
        }

        EventType current = eventManager.getCurrentEvent();
        sender.sendMessage(ChatColor.GOLD + "=== Événement Actuel ===");
        sender.sendMessage(ChatColor.YELLOW + "Type: " + current.getFormattedName());
        sender.sendMessage(ChatColor.GRAY + "Description: " + current.getAnnouncement());
        sender.sendMessage(ChatColor.GRAY + "Durée: " + current.getDurationMinutes() + " minutes");
    }

    private void listEventTypes(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "=== Types d'Événements ===");
        for (EventType type : EventType.values()) {
            String premium = type.isPremiumOnly() ?
                ChatColor.GOLD + " [PREMIUM]" : "";
            sender.sendMessage(ChatColor.YELLOW + type.name().toLowerCase() +
                ChatColor.GRAY + " - " + type.getDisplayName() + premium);
        }
    }
}
