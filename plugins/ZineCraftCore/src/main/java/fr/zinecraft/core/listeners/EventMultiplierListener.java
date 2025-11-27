package fr.zinecraft.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import fr.zinecraft.core.ZineCraftCore;
import fr.zinecraft.core.events.EventManager;

/**
 * Listener pour appliquer les multiplicateurs d'événements
 * aux gains d'XP et de Zines
 */
public class EventMultiplierListener implements Listener {
    
    private final EventManager eventManager;
    
    public EventMultiplierListener(ZineCraftCore plugin) {
        this.eventManager = plugin.getEventManager();
    }
    
    /**
     * Appliquer multiplicateur sur XP des mobs
     * Priority HIGHEST pour s'exécuter après XPListener
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMobKillDuringEvent(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) {
            return;
        }
        
        Player killer = event.getEntity().getKiller();
        double xpMultiplier = eventManager.getCurrentXPMultiplier();
        
        if (xpMultiplier > 1.0) {
            // L'XP de base a déjà été donnée par XPListener
            // On applique le bonus multiplicateur
            int baseXP = event.getDroppedExp();
            int bonusXP = (int) (baseXP * (xpMultiplier - 1.0));
            
            if (bonusXP > 0) {
                event.setDroppedExp(baseXP + bonusXP);
            }
        }
    }
    
    /**
     * Appliquer multiplicateur sur XP du minage
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMineDuringEvent(BlockBreakEvent event) {
        double miningMultiplier = eventManager.getCurrentMiningMultiplier();
        
        if (miningMultiplier > 1.0) {
            // TODO: Intégrer avec le système de minage XP
            // Pour l'instant, juste logger
        }
    }
    
    /**
     * Appliquer multiplicateur sur XP du craft
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCraftDuringEvent(CraftItemEvent event) {
        double xpMultiplier = eventManager.getCurrentXPMultiplier();
        
        if (xpMultiplier > 1.0) {
            // TODO: Intégrer avec le système de craft XP
        }
    }
}
