package fr.zinecraft.core.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Commande pour se sortir d'un trou
 * 
 * @author Otmane & Adam
 */
public class UnstuckCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        
        Player player = (Player) sender;
        Location loc = player.getLocation();
        
        // Téléporter le joueur à 100 blocs de hauteur
        Location newLoc = loc.clone();
        newLoc.setY(100);
        player.teleport(newLoc);
        
        player.sendMessage(ChatColor.GREEN + "✓ Tu es sorti du trou!");
        player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        
        return true;
    }
}
