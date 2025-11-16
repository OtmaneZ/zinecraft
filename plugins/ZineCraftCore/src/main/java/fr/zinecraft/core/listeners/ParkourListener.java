package fr.zinecraft.core.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import fr.zinecraft.core.ZineCraftCore;

/**
 * Listener pour détecter les mouvements dans le parkour
 * 
 * @author Otmane & Adam
 */
public class ParkourListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // Vérifier si le joueur est en parkour
        if (ZineCraftCore.getInstance().getParkourManager().isInParkour(event.getPlayer())) {
            // Vérifier la plateforme sous le joueur
            ZineCraftCore.getInstance().getParkourManager().checkPlatform(
                event.getPlayer(), 
                event.getPlayer().getLocation().subtract(0, 1, 0)
            );
        }
    }
}
