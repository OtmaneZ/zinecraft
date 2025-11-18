package fr.zinecraft.core.structures;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Classe pour construire des structures depuis des fichiers
 */
public class StructureBuilder {

    /**
     * Construire une structure à la position du joueur
     */
    public int build(Player player, File structureFile) throws IOException {
        int commandsExecuted = 0;
        Location loc = player.getLocation();

        try (BufferedReader reader = new BufferedReader(new FileReader(structureFile))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                // Ignorer les commentaires et lignes vides
                if (line.trim().isEmpty() || line.trim().startsWith("#")) {
                    continue;
                }

                // Remplacer @s par le nom du joueur
                String command = line.replace("@s", player.getName());
                
                // Exécuter la commande en tant que le joueur
                String fullCommand = "execute at " + player.getName() + " run " + command;
                
                // Exécuter via la console
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), fullCommand);
                
                commandsExecuted++;
                
                // Petit délai pour éviter la surcharge (1 tick = 50ms)
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return commandsExecuted;
    }
}
