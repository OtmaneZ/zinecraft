package fr.zinecraft.core.commands;

import fr.zinecraft.core.ZineCraftCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Commande pour se téléporter au Désert Mortel
 */
public class DesertTeleportCommand implements CommandExecutor {

    private final ZineCraftCore plugin;

    public DesertTeleportCommand(ZineCraftCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Cette commande est réservée aux joueurs.");
            return true;
        }

        // Vérifier le niveau du joueur
        var rpgPlayer = plugin.getPlayerManager().getPlayer(player.getUniqueId());
        if (rpgPlayer != null && rpgPlayer.getLevel() < 30) {
            player.sendMessage("");
            player.sendMessage(ChatColor.RED + "⚠ NIVEAU INSUFFISANT ⚠");
            player.sendMessage(ChatColor.YELLOW + "Le Désert Mortel est une zone de niveau 30-40");
            player.sendMessage(ChatColor.GRAY + "Votre niveau: " + rpgPlayer.getLevel());
            player.sendMessage(ChatColor.GRAY + "Niveau requis: 30");
            player.sendMessage("");
            return true;
        }

        // Téléporter au désert
        plugin.getSandstormManager().teleportToDesert(player);

        return true;
    }
}
