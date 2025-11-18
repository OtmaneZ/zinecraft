package fr.zinecraft.core.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import fr.zinecraft.core.structures.StructureBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Commande /build pour construire des structures
 */
public class BuildCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        // Vérifier que c'est un joueur
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Cette commande est réservée aux joueurs !");
            return true;
        }

        Player player = (Player) sender;

        // /build sans arguments = liste
        if (args.length == 0) {
            listStructures(player);
            return true;
        }

        String structureName = args[0].toLowerCase();

        // /build list = liste
        if (structureName.equals("list")) {
            listStructures(player);
            return true;
        }

        // Construire la structure
        buildStructure(player, structureName);
        return true;
    }

    /**
     * Afficher la liste des structures disponibles
     */
    private void listStructures(Player player) {
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "=== STRUCTURES DISPONIBLES ===");
        player.sendMessage("");

        List<String> structures = getAvailableStructures();
        
        if (structures.isEmpty()) {
            player.sendMessage(ChatColor.RED + "Aucune structure disponible !");
            return;
        }

        for (String structure : structures) {
            player.sendMessage(ChatColor.YELLOW + "  • " + ChatColor.WHITE + structure 
                + ChatColor.GRAY + " - /build " + structure);
        }

        player.sendMessage("");
        player.sendMessage(ChatColor.GRAY + "Utilisation: " + ChatColor.WHITE + "/build <structure>");
    }

    /**
     * Construire une structure
     */
    private void buildStructure(Player player, String structureName) {
        player.sendMessage(ChatColor.YELLOW + "🏗️ Construction de '" + structureName + "'...");

        // Vérifier si la structure existe
        File structureFile = new File("structures/" + structureName + ".txt");
        if (!structureFile.exists()) {
            player.sendMessage(ChatColor.RED + "❌ Structure '" + structureName + "' introuvable !");
            player.sendMessage(ChatColor.GRAY + "Utilisez /build list pour voir les structures disponibles");
            return;
        }

        // Construire avec StructureBuilder
        try {
            StructureBuilder builder = new StructureBuilder();
            int blocksPlaced = builder.build(player, structureFile);
            
            player.sendMessage(ChatColor.GREEN + "✅ Structure construite avec succès !");
            player.sendMessage(ChatColor.GRAY + "Blocs placés: " + ChatColor.WHITE + blocksPlaced);
        } catch (Exception e) {
            player.sendMessage(ChatColor.RED + "❌ Erreur lors de la construction !");
            player.sendMessage(ChatColor.RED + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Récupérer la liste des structures disponibles
     */
    private List<String> getAvailableStructures() {
        List<String> structures = new ArrayList<>();
        File structuresDir = new File("structures/");
        
        if (!structuresDir.exists()) {
            return structures;
        }

        File[] files = structuresDir.listFiles((dir, name) -> name.endsWith(".txt"));
        
        if (files != null) {
            for (File file : files) {
                String name = file.getName().replace(".txt", "");
                structures.add(name);
            }
        }

        return structures;
    }
}
