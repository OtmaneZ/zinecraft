package com.zinecraft.commands;

import com.zinecraft.builders.zones.VillageZoneBuilder;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Commande pour générer le village spawn
 * Version refactorisée avec architecture modulaire
 */
public class VillageCommand implements CommandExecutor {

    private final Plugin plugin;

    public VillageCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cCette commande doit être exécutée par un joueur.");
            return true;
        }

        // Coordonnées fixes selon map_to_code.md : Village Spawn (0, 0)
        Location center = new Location(player.getWorld(), 0, -60, 0);

        player.sendMessage("§a[VillageZone] Génération du village spawn...");
        player.sendMessage("§7Coordonnées: (0, -60, 0) - Rayon: 75 blocs");
        player.sendMessage("§7Architecture modulaire avec FAWE async");
        player.sendMessage("§7Structures: Fontaine + Marché + Forge + Auberge + 15 Maisons");

        // Créer et lancer le builder (rayon 75 au lieu de 50)
        VillageZoneBuilder builder = new VillageZoneBuilder(plugin, player.getWorld(), center, 75);
        builder.generate();        player.sendMessage("§a[VillageZone] Génération lancée en arrière-plan!");
        player.sendMessage("§7Consultez les logs du serveur pour suivre la progression.");

        return true;
    }
}
