package fr.zinecraft.core.commands;

import fr.zinecraft.core.ZineCraftCore;
import fr.zinecraft.core.economy.EconomyManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Commande /pay pour transférer des Zines entre joueurs
 * 
 * @author Otmane & Copilot
 */
public class PayCommand implements CommandExecutor {
    
    private final ZineCraftCore plugin;
    private final EconomyManager economyManager;
    
    public PayCommand(ZineCraftCore plugin) {
        this.plugin = plugin;
        this.economyManager = plugin.getEconomyManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Cette commande est réservée aux joueurs!");
            return true;
        }
        
        Player player = (Player) sender;
        
        // Vérifier les arguments
        if (args.length != 2) {
            player.sendMessage(ChatColor.RED + "Usage: /pay <joueur> <montant>");
            player.sendMessage(ChatColor.GRAY + "Exemple: /pay Adam 500");
            return true;
        }
        
        // Récupérer le joueur cible
        Player target = Bukkit.getPlayer(args[0]);
        
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Joueur introuvable: " + args[0]);
            return true;
        }
        
        // Ne pas s'envoyer à soi-même
        if (target.equals(player)) {
            player.sendMessage(ChatColor.RED + "Vous ne pouvez pas vous envoyer des Zines!");
            return true;
        }
        
        // Parser le montant
        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Montant invalide: " + args[1]);
            player.sendMessage(ChatColor.GRAY + "Le montant doit être un nombre entier.");
            return true;
        }
        
        // Vérifier le montant minimum
        if (amount < 1) {
            player.sendMessage(ChatColor.RED + "Le montant doit être au moins 1 Zine!");
            return true;
        }
        
        // Effectuer le transfert
        boolean success = economyManager.transferZines(player, target, amount);
        
        if (success) {
            // Message de confirmation (déjà envoyé par transferZines)
            plugin.getLogger().info(String.format("%s a envoyé %d Zines à %s", 
                player.getName(), amount, target.getName()));
        }
        
        return true;
    }
}
