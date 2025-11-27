package fr.zinecraft.core.commands;

import fr.zinecraft.core.ZineCraftCore;
import fr.zinecraft.core.economy.EconomyManager;
import fr.zinecraft.core.rpg.RPGPlayer;
import fr.zinecraft.core.rpg.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Commande /balance pour voir son solde de Zines
 * 
 * @author Otmane & Copilot
 */
public class BalanceCommand implements CommandExecutor {
    
    private final ZineCraftCore plugin;
    private final EconomyManager economyManager;
    private final PlayerManager playerManager;
    
    public BalanceCommand(ZineCraftCore plugin) {
        this.plugin = plugin;
        this.economyManager = plugin.getEconomyManager();
        this.playerManager = plugin.getPlayerManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Cette commande est réservée aux joueurs!");
            return true;
        }
        
        Player player = (Player) sender;
        RPGPlayer rpgPlayer = playerManager.getPlayer(player);
        
        if (rpgPlayer == null) {
            player.sendMessage(ChatColor.RED + "Erreur: Profil RPG introuvable!");
            return true;
        }
        
        // Afficher le solde
        int balance = rpgPlayer.getZines();
        
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "═══════════════════════════════");
        player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "        💰 VOTRE SOLDE 💰");
        player.sendMessage("");
        player.sendMessage(ChatColor.GREEN + "  Zines: " + ChatColor.YELLOW + "" + ChatColor.BOLD + balance);
        player.sendMessage("");
        
        // Rang économique basé sur le solde
        String rank = getEconomicRank(balance);
        player.sendMessage(ChatColor.GRAY + "  Rang: " + rank);
        
        player.sendMessage(ChatColor.GOLD + "═══════════════════════════════");
        player.sendMessage("");
        
        return true;
    }
    
    /**
     * Déterminer le rang économique du joueur
     */
    private String getEconomicRank(int balance) {
        if (balance >= 100000) {
            return ChatColor.DARK_PURPLE + "⭐ Magnat";
        } else if (balance >= 50000) {
            return ChatColor.LIGHT_PURPLE + "💎 Riche";
        } else if (balance >= 20000) {
            return ChatColor.GOLD + "🏆 Prospère";
        } else if (balance >= 10000) {
            return ChatColor.YELLOW + "⚜ Aisé";
        } else if (balance >= 5000) {
            return ChatColor.GREEN + "✓ Stable";
        } else if (balance >= 1000) {
            return ChatColor.WHITE + "○ Modeste";
        } else {
            return ChatColor.GRAY + "- Débutant";
        }
    }
}
