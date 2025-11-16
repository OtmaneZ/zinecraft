package fr.zinecraft.core.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import fr.zinecraft.core.ZineCraftCore;

/**
 * Commande pour démarrer le parkour
 * 
 * @author Otmane & Adam
 */
public class ParkourCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length > 0 && args[0].equalsIgnoreCase("stop")) {
            ZineCraftCore.getInstance().getParkourManager().stopParkour(player);
            return true;
        }
        
        ZineCraftCore.getInstance().getParkourManager().startParkour(player);
        
        return true;
    }
}
