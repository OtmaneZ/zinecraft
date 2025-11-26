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
            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "=== 🔥 BOSS DISPONIBLES 🔥 ===");
            player.sendMessage("");
            player.sendMessage(ChatColor.GREEN + "Boss Faciles:");
            player.sendMessage(ChatColor.GRAY + "  /boss titan " + ChatColor.WHITE + "- Titan Zombie (200 HP)");
            player.sendMessage("");
            player.sendMessage(ChatColor.YELLOW + "Boss Moyens:");
            player.sendMessage(ChatColor.GRAY + "  /boss dragon " + ChatColor.WHITE + "- Dragon Skeleton (350 HP)");
            player.sendMessage(ChatColor.GRAY + "  /boss demon " + ChatColor.WHITE + "- Demon Blaze (500 HP)");
            player.sendMessage("");
            player.sendMessage(ChatColor.RED + "Boss ÉPIQUES:");
            player.sendMessage(ChatColor.GRAY + "  /boss firedragon " + ChatColor.WHITE + "- Dragon de Feu (800 HP)");
            player.sendMessage(ChatColor.GRAY + "  /boss icegolem " + ChatColor.WHITE + "- Golem de Glace (1000 HP)");
            player.sendMessage("");
            player.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Boss LÉGENDAIRE:");
            player.sendMessage(ChatColor.GRAY + "  /boss shadow " + ChatColor.WHITE + "- Titan des Ombres (1500 HP)");
            player.sendMessage("");
            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "========================");
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
            case "firedragon":
            case "fire":
                type = BossManager.BossType.FIRE_DRAGON;
                break;
            case "icegolem":
            case "ice":
                type = BossManager.BossType.ICE_GOLEM;
                break;
            case "shadow":
            case "titan_shadow":
                type = BossManager.BossType.SHADOW_TITAN;
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
