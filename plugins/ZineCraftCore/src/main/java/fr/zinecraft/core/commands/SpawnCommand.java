package fr.zinecraft.core.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Commande pour se téléporter au spawn
 * 
 * @author Otmane & Adam
 */
public class SpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        
        Player player = (Player) sender;
        
        // Téléporter au spawn du monde (0, 70, 0 par défaut)
        Location spawn = player.getWorld().getSpawnLocation();
        player.teleport(spawn);
        
        player.sendMessage(ChatColor.GREEN + "✓ Téléporté au spawn!");
        player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        
        return true;
    }
}
