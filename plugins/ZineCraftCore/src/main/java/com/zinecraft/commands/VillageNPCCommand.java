package com.zinecraft.commands;

import com.zinecraft.npcs.VillageNPCManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Commande pour gérer les PNJ et animaux du village
 * Usage: /villagenpc [spawn|clear|count]
 * 
 * @author Otmane & Copilot
 */
public class VillageNPCCommand implements CommandExecutor {
    
    private final VillageNPCManager npcManager;
    
    public VillageNPCCommand(VillageNPCManager npcManager) {
        this.npcManager = npcManager;
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
            player.sendMessage("§e§lVillage NPC Manager");
            player.sendMessage("");
            player.sendMessage("§7/villagenpc spawn §f- Spawner la population");
            player.sendMessage("§7/villagenpc clear §f- Supprimer la population");
            player.sendMessage("§7/villagenpc count §f- Compter les entités");
            player.sendMessage("§6§m                                   ");
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "spawn":
                player.sendMessage("§e[VillageNPC] Spawn de la population du village...");
                npcManager.spawnVillagePopulation();
                player.sendMessage("§a[VillageNPC] Population spawnée avec succès !");
                player.sendMessage("§78 villageois + 30 animaux");
                break;
                
            case "clear":
                player.sendMessage("§e[VillageNPC] Suppression de la population...");
                npcManager.clearVillagePopulation();
                player.sendMessage("§a[VillageNPC] Population supprimée !");
                break;
                
            case "count":
                int count = npcManager.getActiveEntityCount();
                player.sendMessage("§e[VillageNPC] Entités actives : §a" + count);
                break;
                
            default:
                player.sendMessage("§cSous-commande inconnue : " + subCommand);
                player.sendMessage("§7Utilisez : spawn, clear, count");
                break;
        }
        
        return true;
    }
}
