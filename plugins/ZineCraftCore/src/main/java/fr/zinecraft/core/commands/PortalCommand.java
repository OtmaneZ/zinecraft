package fr.zinecraft.core.commands;

import fr.zinecraft.core.ZineCraftCore;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Commande /6 pour créer des portails de téléportation
 * 
 * @author Adam
 */
public class PortalCommand implements CommandExecutor {
    
    private final Map<UUID, Location> firstPortal = new HashMap<>();
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Cette commande est réservée aux joueurs !");
            return true;
        }
        
        Player player = (Player) sender;
        Location playerLoc = player.getLocation();
        Location blockBelow = playerLoc.clone().subtract(0, 1, 0);
        
        // Poser un bloc en dessous du joueur
        blockBelow.getBlock().setType(Material.GLOWSTONE);
        
        // Effets visuels
        player.getWorld().spawnParticle(Particle.PORTAL, blockBelow.clone().add(0.5, 0.5, 0.5), 30, 0.3, 0.3, 0.3, 0.1);
        player.getWorld().playSound(blockBelow, Sound.BLOCK_PORTAL_AMBIENT, 1.0f, 1.5f);
        
        // Vérifier si c'est le premier ou le deuxième portail
        if (!firstPortal.containsKey(player.getUniqueId())) {
            // Premier portail
            firstPortal.put(player.getUniqueId(), blockBelow.clone());
            player.sendMessage(ChatColor.GREEN + "✓ Premier portail créé! Utilise /6 à nouveau pour créer le deuxième portail.");
            player.sendMessage(ChatColor.GRAY + "Clic sur le bloc pour te téléporter une fois les deux portails créés.");
        } else {
            // Deuxième portail - créer la liaison
            Location firstLoc = firstPortal.get(player.getUniqueId());
            Location secondLoc = blockBelow.clone();
            
            // Enregistrer le lien dans le gestionnaire de portails
            ZineCraftCore.getInstance().getPortalManager().linkPortals(firstLoc, secondLoc);
            
            // Retirer de la map temporaire
            firstPortal.remove(player.getUniqueId());
            
            // Message de succès
            player.sendMessage(ChatColor.GOLD + "✓ Portails liés avec succès!");
            player.sendMessage(ChatColor.YELLOW + "🌀 Clic sur l'un des blocs pour te téléporter à l'autre!");
            
            // Effets spéciaux pour le lien
            player.getWorld().spawnParticle(Particle.END_ROD, secondLoc.clone().add(0.5, 0.5, 0.5), 50, 0.5, 0.5, 0.5, 0.1);
            player.getWorld().playSound(secondLoc, Sound.BLOCK_PORTAL_TRAVEL, 0.5f, 2.0f);
        }
        
        return true;
    }
}
