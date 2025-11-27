package com.zinecraft.commands;

import com.zinecraft.builders.zones.TutorialZoneBuilder;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Commande pour générer la Tutorial Island
 * Coords: (0, 500) selon map_to_code.md
 */
public class TutorialCommand implements CommandExecutor {

    private final Plugin plugin;

    public TutorialCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cCette commande doit être exécutée par un joueur.");
            return true;
        }

        // Coordonnées fixes selon map_to_code.md : Tutorial Island (0, 500)
        Location center = new Location(player.getWorld(), 0, -60, 500);

        player.sendMessage("§a[TutorialIsland] Génération de l'île tutoriel...");
        player.sendMessage("§7Coordonnées: (0, -60, 500) - Taille: 100x100");
        player.sendMessage("§7Niveau: 1-5 | Île surélevée Y=70");

        // Créer et lancer le builder
        TutorialZoneBuilder builder = new TutorialZoneBuilder(plugin, player.getWorld(), center, 80);
        builder.generate();

        player.sendMessage("§a[TutorialIsland] Génération lancée en arrière-plan!");
        player.sendMessage("§7Consultez les logs pour suivre la progression.");

        return true;
    }
}
