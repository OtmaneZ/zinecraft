package fr.zinecraft.core.listeners;

import fr.zinecraft.core.ZineCraftCore;
import fr.zinecraft.core.weapons.WeaponManager;
import fr.zinecraft.core.weapons.WeaponType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Listener pour les armes légendaires
 * 
 * @author Otmane & Adam
 */
public class WeaponListener implements Listener {
    
    private final WeaponManager weaponManager;
    
    public WeaponListener() {
        this.weaponManager = ZineCraftCore.getInstance().getWeaponManager();
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        
        // Vérifier si c'est un clic droit
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        
        // Vérifier si c'est une arme légendaire
        if (!weaponManager.isLegendaryWeapon(item)) {
            return;
        }
        
        // Obtenir le type d'arme
        WeaponType type = weaponManager.getWeaponType(item);
        if (type == null) {
            return;
        }
        
        // Utiliser le pouvoir
        event.setCancelled(true);
        weaponManager.useWeaponPower(player, type);
    }
}
