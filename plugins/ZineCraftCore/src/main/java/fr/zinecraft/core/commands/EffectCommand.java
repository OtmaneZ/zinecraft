package fr.zinecraft.core.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import fr.zinecraft.core.ZineCraftCore;
import fr.zinecraft.core.visuals.VisualEffectManager;
import fr.zinecraft.core.visuals.EffectType;

/**
 * Commande pour gérer les effets visuels
 *
 * @author Otmane & Copilot
 */
public class EffectCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Cette commande est réservée aux joueurs!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            sendHelp(player);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "set":
                if (args.length < 2) {
                    player.sendMessage(ChatColor.RED + "Usage: /effect set <type>");
                    listEffects(player);
                    return true;
                }

                setEffect(player, args[1]);
                break;

            case "remove":
            case "off":
                removeEffect(player);
                break;

            case "list":
                listEffects(player);
                break;

            case "levelup":
                // Easter egg pour tester l'effet
                if (player.hasPermission("zinecraft.effect.test")) {
                    ZineCraftCore.getInstance().getVisualEffectManager().playLevelUpEffect(player);
                    player.sendMessage(ChatColor.GREEN + "✨ Effet level up!");
                }
                break;

            default:
                sendHelp(player);
                break;
        }

        return true;
    }

    private void sendHelp(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== Commandes Effets Visuels ===");
        player.sendMessage(ChatColor.YELLOW + "/effect set <type> " +
            ChatColor.GRAY + "- Activer un effet");
        player.sendMessage(ChatColor.YELLOW + "/effect remove " +
            ChatColor.GRAY + "- Désactiver l'effet");
        player.sendMessage(ChatColor.YELLOW + "/effect list " +
            ChatColor.GRAY + "- Liste des effets");
    }

    private void setEffect(Player player, String typeName) {
        VisualEffectManager effectManager = ZineCraftCore.getInstance().getVisualEffectManager();

        try {
            EffectType type = EffectType.valueOf(typeName.toUpperCase());

            // Vérifier les permissions VIP
            if (type.isPremium() && !player.hasPermission("zinecraft.effect.premium")) {
                player.sendMessage(ChatColor.RED + "✘ Cet effet est réservé aux VIP!");
                player.sendMessage(ChatColor.YELLOW + "Visitez notre boutique pour débloquer plus d'effets!");
                return;
            }

            effectManager.setEffect(player, type);

        } catch (IllegalArgumentException e) {
            player.sendMessage(ChatColor.RED + "Effet invalide!");
            listEffects(player);
        }
    }

    private void removeEffect(Player player) {
        VisualEffectManager effectManager = ZineCraftCore.getInstance().getVisualEffectManager();
        effectManager.removeEffect(player);
    }

    private void listEffects(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== Effets Visuels Disponibles ===");

        for (EffectType type : EffectType.values()) {
            String status;
            if (type.isPremium()) {
                if (player.hasPermission("zinecraft.effect.premium")) {
                    status = ChatColor.GREEN + " ✔";
                } else {
                    status = ChatColor.GOLD + " [VIP]";
                }
            } else {
                status = ChatColor.GREEN + " ✔";
            }

            player.sendMessage(ChatColor.YELLOW + type.name().toLowerCase() +
                ChatColor.GRAY + " - " + type.getDescription() + status);
        }
    }
}
