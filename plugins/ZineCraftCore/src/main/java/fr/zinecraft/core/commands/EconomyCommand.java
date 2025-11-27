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
 * Commande /economy pour l'administration des Zines
 * 
 * @author Otmane & Copilot
 */
public class EconomyCommand implements CommandExecutor {
    
    private final ZineCraftCore plugin;
    private final EconomyManager economyManager;
    
    public EconomyCommand(ZineCraftCore plugin) {
        this.plugin = plugin;
        this.economyManager = plugin.getEconomyManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        // Permission admin requise
        if (!sender.hasPermission("zinecraft.admin")) {
            sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission!");
            return true;
        }
        
        // Afficher l'aide si pas d'arguments
        if (args.length == 0) {
            showHelp(sender);
            return true;
        }
        
        String action = args[0].toLowerCase();
        
        switch (action) {
            case "give":
                return handleGive(sender, args);
                
            case "take":
                return handleTake(sender, args);
                
            case "set":
                return handleSet(sender, args);
                
            case "check":
                return handleCheck(sender, args);
                
            default:
                showHelp(sender);
                return true;
        }
    }
    
    /**
     * Afficher l'aide
     */
    private void showHelp(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(ChatColor.GOLD + "═════════ ECONOMY ADMIN ═════════");
        sender.sendMessage(ChatColor.YELLOW + "/economy give <joueur> <montant> - Donner des Zines");
        sender.sendMessage(ChatColor.YELLOW + "/economy take <joueur> <montant> - Retirer des Zines");
        sender.sendMessage(ChatColor.YELLOW + "/economy set <joueur> <montant> - Définir le solde");
        sender.sendMessage(ChatColor.YELLOW + "/economy check <joueur> - Voir le solde");
        sender.sendMessage(ChatColor.GOLD + "═════════════════════════════════");
        sender.sendMessage("");
    }
    
    /**
     * Donner des Zines à un joueur
     */
    private boolean handleGive(CommandSender sender, String[] args) {
        if (args.length != 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /economy give <joueur> <montant>");
            return true;
        }
        
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Joueur introuvable: " + args[1]);
            return true;
        }
        
        int amount;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Montant invalide: " + args[2]);
            return true;
        }
        
        if (amount <= 0) {
            sender.sendMessage(ChatColor.RED + "Le montant doit être positif!");
            return true;
        }
        
        economyManager.addZines(target, amount, "Admin give");
        sender.sendMessage(ChatColor.GREEN + "✔ Vous avez donné " + amount + " Zines à " + target.getName());
        
        plugin.getLogger().info(String.format("%s a donné %d Zines à %s", 
            sender.getName(), amount, target.getName()));
        
        return true;
    }
    
    /**
     * Retirer des Zines à un joueur
     */
    private boolean handleTake(CommandSender sender, String[] args) {
        if (args.length != 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /economy take <joueur> <montant>");
            return true;
        }
        
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Joueur introuvable: " + args[1]);
            return true;
        }
        
        int amount;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Montant invalide: " + args[2]);
            return true;
        }
        
        if (amount <= 0) {
            sender.sendMessage(ChatColor.RED + "Le montant doit être positif!");
            return true;
        }
        
        boolean success = economyManager.removeZines(target, amount, "Admin take");
        
        if (success) {
            sender.sendMessage(ChatColor.GREEN + "✔ Vous avez retiré " + amount + " Zines à " + target.getName());
            
            plugin.getLogger().info(String.format("%s a retiré %d Zines à %s", 
                sender.getName(), amount, target.getName()));
        }
        
        return true;
    }
    
    /**
     * Définir le solde d'un joueur
     */
    private boolean handleSet(CommandSender sender, String[] args) {
        if (args.length != 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /economy set <joueur> <montant>");
            return true;
        }
        
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Joueur introuvable: " + args[1]);
            return true;
        }
        
        int amount;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Montant invalide: " + args[2]);
            return true;
        }
        
        if (amount < 0) {
            sender.sendMessage(ChatColor.RED + "Le montant doit être positif ou nul!");
            return true;
        }
        
        economyManager.setBalance(target, amount);
        sender.sendMessage(ChatColor.GREEN + "✔ Solde de " + target.getName() + " défini à " + amount + " Zines");
        
        plugin.getLogger().info(String.format("%s a défini le solde de %s à %d Zines", 
            sender.getName(), target.getName(), amount));
        
        return true;
    }
    
    /**
     * Vérifier le solde d'un joueur
     */
    private boolean handleCheck(CommandSender sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /economy check <joueur>");
            return true;
        }
        
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Joueur introuvable: " + args[1]);
            return true;
        }
        
        int balance = economyManager.getBalance(target);
        
        sender.sendMessage("");
        sender.sendMessage(ChatColor.GOLD + "══════════════════════════════");
        sender.sendMessage(ChatColor.YELLOW + "Joueur: " + ChatColor.WHITE + target.getName());
        sender.sendMessage(ChatColor.YELLOW + "Solde: " + ChatColor.GREEN + balance + " Zines");
        sender.sendMessage(ChatColor.GOLD + "══════════════════════════════");
        sender.sendMessage("");
        
        return true;
    }
}
