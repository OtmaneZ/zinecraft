package fr.zinecraft.core.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import fr.zinecraft.core.ZineCraftCore;

/**
 * Commande pour rejoindre le mode 2v2
 * 
 * @author Otmane & Adam
 */
public class Combat2v2Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        
        Player player = (Player) sender;
        
        // Ajouter à la file d'attente
        ZineCraftCore.getInstance().getArenaManager().joinQueue2v2(player);
        
        return true;
    }
}
