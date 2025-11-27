package com.zinecraft.commands;

import com.zinecraft.builders.structures.VillageWallBuilder;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Commande pour construire/supprimer les fortifications du village
 * Usage: /villagewall [build|remove]
 * 
 * @author Otmane & Copilot
 */
public class VillageWallCommand implements CommandExecutor {
    
    private final Plugin plugin;
    private final VillageWallBuilder wallBuilder;
    
    // Coordonnées du village (à ajuster selon votre village)
    private static final int VILLAGE_CENTER_X = 0;
    private static final int VILLAGE_CENTER_Y = -60;
    private static final int VILLAGE_CENTER_Z = 0;
    private static final int VILLAGE_RADIUS = 100;
    
    public VillageWallCommand(Plugin plugin) {
        this.plugin = plugin;
        this.wallBuilder = new VillageWallBuilder(plugin);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cCette commande est réservée aux joueurs !");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.isOp()) {
            player.sendMessage("§cVous devez être OP pour utiliser cette commande !");
            return true;
        }
        
        if (args.length == 0) {
            player.sendMessage("§6§m                                   ");
            player.sendMessage("§e§lFortifications du Village");
            player.sendMessage("");
            player.sendMessage("§7/villagewall build §f- Construire les murs");
            player.sendMessage("§7/villagewall remove §f- Supprimer les murs");
            player.sendMessage("");
            player.sendMessage("§7Style : Médiéval en bois");
            player.sendMessage("§7Hauteur : 5 blocs + tours de 8 blocs");
            player.sendMessage("§7Portes : 4 directions (N/S/E/O)");
            player.sendMessage("§6§m                                   ");
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        Location center = new Location(player.getWorld(), VILLAGE_CENTER_X, VILLAGE_CENTER_Y, VILLAGE_CENTER_Z);
        
        switch (subCommand) {
            case "build":
            case "construct":
            case "create":
                player.sendMessage("§e[VillageWall] Construction des fortifications médiévales...");
                player.sendMessage("§7Centre : X=" + VILLAGE_CENTER_X + " Z=" + VILLAGE_CENTER_Z);
                player.sendMessage("§7Rayon : " + VILLAGE_RADIUS + " blocs");
                player.sendMessage("§c⚠ Cette opération peut prendre quelques secondes...");
                
                // Lancer la construction sur le thread principal
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    try {
                        wallBuilder.buildVillageWalls(center, VILLAGE_RADIUS);
                        player.sendMessage("§a[VillageWall] ✔ Fortifications terminées !");
                        player.sendMessage("§7- Mur circulaire en bois");
                        player.sendMessage("§7- 4 tours d'angle");
                        player.sendMessage("§7- 4 portes (Nord, Sud, Est, Ouest)");
                        player.sendMessage("§7- Créneaux défensifs");
                    } catch (Exception e) {
                        player.sendMessage("§c[VillageWall] ✘ Erreur lors de la construction !");
                        player.sendMessage("§c" + e.getMessage());
                    }
                });
                break;
                
            case "remove":
            case "delete":
            case "clear":
                player.sendMessage("§e[VillageWall] Suppression des fortifications...");
                
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    try {
                        wallBuilder.removeVillageWalls(center, VILLAGE_RADIUS);
                        player.sendMessage("§a[VillageWall] ✔ Fortifications supprimées !");
                    } catch (Exception e) {
                        player.sendMessage("§c[VillageWall] ✘ Erreur lors de la suppression !");
                    }
                });
                break;
                
            default:
                player.sendMessage("§cSous-commande inconnue : " + subCommand);
                player.sendMessage("§7Utilisez : build, remove");
                break;
        }
        
        return true;
    }
}
