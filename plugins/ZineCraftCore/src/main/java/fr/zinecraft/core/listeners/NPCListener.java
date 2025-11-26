package fr.zinecraft.core.listeners;

import fr.zinecraft.core.ZineCraftCore;
import fr.zinecraft.core.rpg.NPCManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Listener pour les interactions avec les NPCs
 * 
 * @author Otmane & Copilot
 */
public class NPCListener implements Listener {
    
    private final ZineCraftCore plugin;
    private NPCManager npcManager;
    
    public NPCListener() {
        this.plugin = ZineCraftCore.getInstance();
    }
    
    /**
     * Définir le NPCManager (appelé après son initialisation)
     */
    public void setNPCManager(NPCManager npcManager) {
        this.npcManager = npcManager;
    }
    
    /**
     * Gérer l'interaction avec un NPC (clic droit)
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (npcManager == null) {
            return;
        }
        
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        
        // Vérifier si c'est le NPC de classe
        if (npcManager.isClassMasterNPC(entity)) {
            event.setCancelled(true);
            npcManager.handleNPCInteraction(player, entity);
        }
    }
    
    /**
     * Empêcher de frapper le NPC
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (npcManager == null) {
            return;
        }
        
        Entity entity = event.getEntity();
        
        if (npcManager.isClassMasterNPC(entity)) {
            event.setCancelled(true);
            
            if (event.getDamager() instanceof Player) {
                Player player = (Player) event.getDamager();
                player.sendMessage(ChatColor.RED + "Vous ne pouvez pas attaquer le Maître des Classes !");
            }
        }
    }
    
    /**
     * Gérer le clic dans le menu de sélection de classe
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClick(InventoryClickEvent event) {
        if (npcManager == null) {
            return;
        }
        
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();
        
        // Vérifier si c'est notre GUI
        if (!title.contains("Choix de Classe")) {
            return;
        }
        
        event.setCancelled(true);
        
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) {
            return;
        }
        
        // Gérer la sélection
        npcManager.handleClassSelection(player, clickedItem);
    }
}
