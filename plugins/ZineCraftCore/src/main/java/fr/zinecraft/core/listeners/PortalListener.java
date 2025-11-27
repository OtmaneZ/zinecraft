package fr.zinecraft.core.listeners;

import fr.zinecraft.core.ZineCraftCore;
import fr.zinecraft.core.portals.PortalManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Listener pour les portails de téléportation
 * 
 * @author Adam
 */
public class PortalListener implements Listener {
    
    private final PortalManager portalManager;
    
    public PortalListener() {
        this.portalManager = ZineCraftCore.getInstance().getPortalManager();
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        
        // Vérifier si c'est un clic droit sur un bloc
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        
        // Vérifier si le bloc cliqué existe
        if (event.getClickedBlock() == null) {
            return;
        }
        
        Location clickedLoc = event.getClickedBlock().getLocation();
        
        // Vérifier si c'est un portail (glowstone)
        if (event.getClickedBlock().getType() != Material.GLOWSTONE) {
            return;
        }
        
        // Vérifier si c'est un portail enregistré
        if (!portalManager.isPortal(clickedLoc)) {
            return;
        }
        
        // Téléporter le joueur
        event.setCancelled(true);
        portalManager.teleportPlayer(player, clickedLoc);
    }
}
