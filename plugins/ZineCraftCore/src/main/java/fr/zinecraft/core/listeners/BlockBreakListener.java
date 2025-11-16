package fr.zinecraft.core.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Listener pour empêcher la casse et la pose de blocs
 * 
 * @author Otmane & Adam
 */
public class BlockBreakListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onBlockBreak(BlockBreakEvent event) {
        // Annuler l'événement pour empêcher la casse
        event.setCancelled(true);
        
        // Message au joueur
        event.getPlayer().sendMessage(ChatColor.RED + "⚠ Vous ne pouvez pas casser de blocs sur ce serveur!");
        
        // Debug dans la console
        System.out.println("[ZineCraft] Blocage de casse de bloc par " + event.getPlayer().getName());
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onBlockPlace(BlockPlaceEvent event) {
        // Annuler la pose de blocs aussi
        event.setCancelled(true);
        
        // Message au joueur
        event.getPlayer().sendMessage(ChatColor.RED + "⚠ Vous ne pouvez pas placer de blocs sur ce serveur!");
        
        // Debug dans la console
        System.out.println("[ZineCraft] Blocage de pose de bloc par " + event.getPlayer().getName());
    }
}
