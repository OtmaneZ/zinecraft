package fr.zinecraft.core.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import fr.zinecraft.core.ZineCraftCore;
import fr.zinecraft.core.boss.BossManager;

/**
 * Commande pour faire apparaître un boss
 * 
 * @author Otmane & Adam
 */
public class BossCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length == 0) {
            player.sendMessage("");
            player.sendMessage(ChatColor.RED + "⚔ Boss disponibles:");
            player.sendMessage(ChatColor.GRAY + "/boss titan " + ChatColor.WHITE + "- Titan Zombie (Facile)");
            player.sendMessage(ChatColor.GRAY + "/boss dragon " + ChatColor.WHITE + "- Dragon Skeleton (Moyen)");
            player.sendMessage(ChatColor.GRAY + "/boss demon " + ChatColor.WHITE + "- Demon Blaze (Difficile)");
            player.sendMessage("");
            return true;
        }
        
        BossManager.BossType type = null;
        
        switch (args[0].toLowerCase()) {
            case "titan":
                type = BossManager.BossType.TITAN_ZOMBIE;
                break;
            case "dragon":
                type = BossManager.BossType.DRAGON_SKELETON;
                break;
            case "demon":
                type = BossManager.BossType.DEMON_BLAZE;
                break;
            default:
                player.sendMessage(ChatColor.RED + "Boss inconnu! Utilise /boss pour voir la liste.");
                return true;
        }
        
        // Faire apparaître le boss devant le joueur sur le sol
        Location spawnLoc = player.getLocation().add(player.getLocation().getDirection().multiply(5));
        
        // Trouver le sol en dessous
        while (spawnLoc.getY() > 0 && spawnLoc.getBlock().getType().isAir()) {
            spawnLoc.subtract(0, 1, 0);
        }
        
        // Monter d'un bloc pour être au-dessus du sol
        spawnLoc.add(0, 1, 0);
        
        ZineCraftCore.getInstance().getBossManager().spawnBoss(spawnLoc, type);
        
        return true;
    }
}
