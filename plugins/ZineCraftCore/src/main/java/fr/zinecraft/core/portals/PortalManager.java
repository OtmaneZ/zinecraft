package fr.zinecraft.core.portals;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Gestionnaire des portails de téléportation
 * 
 * @author Adam
 */
public class PortalManager {
    
    private final Map<String, Location> portalLinks = new HashMap<>();
    
    /**
     * Lier deux portails ensemble
     */
    public void linkPortals(Location loc1, Location loc2) {
        String key1 = locationToString(loc1);
        String key2 = locationToString(loc2);
        
        // Lier les deux portails entre eux
        portalLinks.put(key1, loc2.clone());
        portalLinks.put(key2, loc1.clone());
    }
    
    /**
     * Vérifier si une location est un portail
     */
    public boolean isPortal(Location loc) {
        return portalLinks.containsKey(locationToString(loc));
    }
    
    /**
     * Obtenir la destination d'un portail
     */
    public Location getDestination(Location loc) {
        return portalLinks.get(locationToString(loc));
    }
    
    /**
     * Téléporter un joueur via un portail
     */
    public void teleportPlayer(Player player, Location portalLoc) {
        Location destination = getDestination(portalLoc);
        
        if (destination == null) {
            player.sendMessage("§cPortail non lié!");
            return;
        }
        
        // Téléporter au-dessus du bloc de destination
        Location teleportLoc = destination.clone().add(0.5, 1, 0.5);
        teleportLoc.setYaw(player.getLocation().getYaw());
        teleportLoc.setPitch(player.getLocation().getPitch());
        
        // Effets avant téléportation
        player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 100, 0.5, 1, 0.5, 0.5);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        
        // Téléportation
        player.teleport(teleportLoc);
        
        // Effets après téléportation
        player.getWorld().spawnParticle(Particle.PORTAL, teleportLoc, 100, 0.5, 1, 0.5, 0.5);
        player.playSound(teleportLoc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.2f);
        
        player.sendMessage("§a✓ Téléporté!");
    }
    
    /**
     * Convertir une location en string pour utiliser comme clé
     */
    private String locationToString(Location loc) {
        return loc.getWorld().getName() + ":" + 
               loc.getBlockX() + ":" + 
               loc.getBlockY() + ":" + 
               loc.getBlockZ();
    }
}
