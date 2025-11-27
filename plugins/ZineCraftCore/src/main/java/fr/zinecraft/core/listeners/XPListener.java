package fr.zinecraft.core.listeners;

import fr.zinecraft.core.ZineCraftCore;
import fr.zinecraft.core.rpg.LevelManager;
import fr.zinecraft.core.rpg.PlayerManager;
import fr.zinecraft.core.rpg.RPGPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.Material;

/**
 * Listener pour donner de l'XP selon les actions
 * 
 * @author Otmane & Copilot
 */
public class XPListener implements Listener {
    
    private final ZineCraftCore plugin;
    private final LevelManager levelManager;
    private final PlayerManager playerManager;
    
    public XPListener(LevelManager levelManager) {
        this.plugin = ZineCraftCore.getInstance();
        this.levelManager = levelManager;
        this.playerManager = plugin.getPlayerManager();
    }
    
    /**
     * XP pour avoir tué un mob
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player killer = entity.getKiller();
        
        if (killer == null) {
            return;
        }
        
        RPGPlayer rpgPlayer = playerManager.getPlayer(killer);
        if (rpgPlayer == null || !rpgPlayer.hasChosenClass()) {
            return;
        }
        
        // Obtenir l'XP selon le type de mob
        int xp = levelManager.getMobKillXP(entity.getType());
        
        if (xp > 0) {
            // Incrémenter les stats
            rpgPlayer.incrementMobsKilled();
            
            // Donner l'XP
            levelManager.addExperience(killer, xp, "Kill " + entity.getType().name());
        }
    }
    
    /**
     * XP pour avoir miné un bloc
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Material material = event.getBlock().getType();
        
        RPGPlayer rpgPlayer = playerManager.getPlayer(player);
        if (rpgPlayer == null || !rpgPlayer.hasChosenClass()) {
            return;
        }
        
        // Obtenir l'XP selon le bloc
        int xp = levelManager.getMiningXP(material);
        
        if (xp > 0) {
            // Incrémenter les stats
            rpgPlayer.incrementBlocksMined();
            
            // Donner l'XP
            levelManager.addExperience(player, xp, "Mine " + material.name());
        }
    }
    
    /**
     * XP pour avoir crafté un item
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCraft(CraftItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getWhoClicked();
        Material material = event.getRecipe().getResult().getType();
        
        RPGPlayer rpgPlayer = playerManager.getPlayer(player);
        if (rpgPlayer == null || !rpgPlayer.hasChosenClass()) {
            return;
        }
        
        // Obtenir l'XP selon l'item crafté
        int xp = levelManager.getCraftingXP(material);
        
        if (xp > 0) {
            // Incrémenter les stats
            rpgPlayer.incrementItemsCrafted();
            
            // Donner l'XP
            levelManager.addExperience(player, xp, "Craft " + material.name());
        }
    }
    
    /**
     * Annuler l'XP vanilla de Minecraft (on utilise notre système)
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVanillaExpChange(PlayerExpChangeEvent event) {
        // Annuler l'XP vanilla pour utiliser uniquement notre système RPG
        event.setAmount(0);
    }
}
