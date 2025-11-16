package fr.zinecraft.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import fr.zinecraft.core.ZineCraftCore;

/**
 * Listener pour les boss
 * 
 * @author Otmane & Adam
 */
public class BossListener implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        // Vérifier si c'est un boss
        if (ZineCraftCore.getInstance().getBossManager().isBoss(event.getEntity())) {
            // Vérifier si tué par un joueur
            if (event.getEntity().getKiller() instanceof Player) {
                Player killer = event.getEntity().getKiller();
                
                // Gérer la mort du boss
                ZineCraftCore.getInstance().getBossManager().onBossDeath(event.getEntity(), killer);
                
                // Empêcher le drop normal
                event.getDrops().clear();
                event.setDroppedExp(0);
            }
        }
    }
}
