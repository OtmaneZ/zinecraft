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
 * Coords: (0, -60, 500) - Niveau débutant 1-5
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

        // Coordonnées fixes : Tutorial Island (0, -60, 500)
        // L'île sera créée AU NIVEAU -60 (niveau du sol actuel)
        Location center = new Location(player.getWorld(), 0, -60, 500);

        player.sendMessage("§a§l[Tutorial Island] Génération de l'île...");
        player.sendMessage("§7Coordonnées: (0, -60, 500)");
        player.sendMessage("§7Rayon: 50 blocs | Niveau débutant (1-5)");
        player.sendMessage("§e⏳ Cela prendra 10-20 secondes...");

        // Créer l'île avec rayon 50 (diamètre 100)
        TutorialZoneBuilder builder = new TutorialZoneBuilder(plugin, player.getWorld(), center, 50);
        builder.generate();

        player.sendMessage("§a[TutorialIsland] Génération lancée en arrière-plan!");
        player.sendMessage("§7Consultez les logs pour suivre la progression.");

        return true;
    }
}
