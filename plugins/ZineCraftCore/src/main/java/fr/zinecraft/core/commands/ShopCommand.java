package fr.zinecraft.core.commands;

import fr.zinecraft.core.ZineCraftCore;
import fr.zinecraft.core.economy.ShopManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Commande /shop pour ouvrir la boutique
 * 
 * @author Otmane & Copilot
 */
public class ShopCommand implements CommandExecutor {
    
    private final ZineCraftCore plugin;
    private final ShopManager shopManager;
    
    public ShopCommand(ZineCraftCore plugin) {
        this.plugin = plugin;
        this.shopManager = plugin.getShopManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Cette commande est réservée aux joueurs!");
            return true;
        }
        
        Player player = (Player) sender;
        
        // Ouvrir le shop avec la catégorie par défaut (BLOCKS)
        shopManager.openShop(player, ShopManager.ShopCategory.BLOCKS);
        
        player.sendMessage(ChatColor.GREEN + "Bienvenue dans la boutique ZineCraft!");
        player.sendMessage(ChatColor.GRAY + "Utilisez les boutons pour naviguer entre les catégories.");
        
        return true;
    }
}
