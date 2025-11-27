package fr.zinecraft.core.listeners;

import fr.zinecraft.core.ZineCraftCore;
import fr.zinecraft.core.quests.QuestManager;
import fr.zinecraft.core.quests.QuestObjective;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Listener pour mettre à jour automatiquement la progression des quêtes
 * 
 * @author Otmane & Copilot
 */
public class QuestListener implements Listener {
    
    private final ZineCraftCore plugin;
    private final QuestManager questManager;
    
    public QuestListener(ZineCraftCore plugin) {
        this.plugin = plugin;
        this.questManager = plugin.getQuestManager();
    }
    
    /**
     * Charger les quêtes à la connexion
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Charger les quêtes du joueur
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            questManager.loadPlayerQuests(player);
        }, 20L); // 1 seconde après connexion
    }
    
    /**
     * Sauvegarder les quêtes à la déconnexion
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        questManager.savePlayerQuests(event.getPlayer());
    }
    
    /**
     * Progression: Tuer des mobs
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) {
            return;
        }
        
        Player killer = event.getEntity().getKiller();
        EntityType entityType = event.getEntityType();
        
        // Mettre à jour les quêtes de type KILL_MOBS
        questManager.updateObjectiveProgress(killer, 
            QuestObjective.ObjectiveType.KILL_MOBS, 
            entityType.name(), 
            1);
    }
    
    /**
     * Progression: Tuer des joueurs
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player killed = event.getEntity();
        Player killer = killed.getKiller();
        
        if (killer != null && killer != killed) {
            // Mettre à jour les quêtes de type KILL_PLAYERS
            questManager.updateObjectiveProgress(killer, 
                QuestObjective.ObjectiveType.KILL_PLAYERS, 
                null, 
                1);
        }
    }
    
    /**
     * Progression: Miner des blocs
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Material blockType = event.getBlock().getType();
        
        // Mettre à jour les quêtes de type MINE_BLOCKS
        questManager.updateObjectiveProgress(player, 
            QuestObjective.ObjectiveType.MINE_BLOCKS, 
            blockType.name(), 
            1);
    }
    
    /**
     * Progression: Crafter des items
     */
    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getWhoClicked();
        Material craftedType = event.getRecipe().getResult().getType();
        int amount = event.getRecipe().getResult().getAmount();
        
        // Si shift-click, calculer le nombre d'items craftés
        if (event.isShiftClick()) {
            // Compter combien de fois on peut crafter
            int maxCrafts = 64; // Maximum estimé
            amount *= Math.min(maxCrafts, 64 / amount);
        }
        
        // Mettre à jour les quêtes de type CRAFT_ITEMS
        questManager.updateObjectiveProgress(player, 
            QuestObjective.ObjectiveType.CRAFT_ITEMS, 
            craftedType.name(), 
            amount);
    }
}
