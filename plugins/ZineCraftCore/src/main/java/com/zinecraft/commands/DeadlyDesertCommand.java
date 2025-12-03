package com.zinecraft.commands;

import com.zinecraft.builders.zones.DeadlyDesertZoneBuilder;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Commande pour générer le Désert Mortel
 * Coords: -500, 65, 500
 */
public class DeadlyDesertCommand implements CommandExecutor {

    private final Plugin plugin;

    public DeadlyDesertCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cCette commande doit être exécutée par un joueur.");
            return true;
        }

        // Coordonnées fixes selon map_to_code.md : Désert Mortel (-500, 65, 500)
        Location center = new Location(player.getWorld(), -500, 65, 500);

        player.sendMessage("§6§l[Désert Mortel] §eGénération en cours...");
        player.sendMessage("§7Coordonnées: (-500, 65, 500)");
        player.sendMessage("§7Niveau: 30-40 | Rayon: 150 blocs");
        player.sendMessage("");
        player.sendMessage("§c⚠ Zone hostile: Tempête de sable permanente");
        player.sendMessage("§7Structures:");
        player.sendMessage("§7  • Pyramide massive (50x50x40)");
        player.sendMessage("§7  • 6 cratères de météorites");
        player.sendMessage("§7  • Village abandonné");
        player.sendMessage("§7  • Oasis avec PNJ marchand");
        player.sendMessage("§7  • Boss: DEMON_BLAZE (sommet pyramide)");

        // Créer et lancer le builder (rayon 150)
        DeadlyDesertZoneBuilder builder = new DeadlyDesertZoneBuilder(plugin, player.getWorld(), center, 150);
        builder.generate();

        player.sendMessage("");
        player.sendMessage("§a[Désert Mortel] Génération lancée en arrière-plan!");
        player.sendMessage("§7Temps estimé: 2-5 minutes");
        player.sendMessage("§7Consultez les logs du serveur pour suivre la progression.");

        return true;
    }
}
