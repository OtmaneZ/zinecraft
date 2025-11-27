package fr.zinecraft.core.listeners;

import fr.zinecraft.core.ZineCraftCore;
import fr.zinecraft.core.economy.ShopManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Listener pour gérer les clics dans le shop
 * 
 * @author Otmane & Copilot
 */
public class ShopListener implements Listener {
    
    private final ZineCraftCore plugin;
    private final ShopManager shopManager;
    
    public ShopListener(ZineCraftCore plugin) {
        this.plugin = plugin;
        this.shopManager = plugin.getShopManager();
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        
        // Vérifier si c'est le shop
        if (!title.contains("BOUTIQUE")) {
            return;
        }
        
        event.setCancelled(true);
        
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        
        if (clicked == null || clicked.getType() == Material.AIR) {
            return;
        }
        
        // Bouton fermer
        if (clicked.getType() == Material.BARRIER) {
            player.closeInventory();
            return;
        }
        
        // Boutons de catégorie (dernière rangée: slots 45-52)
        int slot = event.getSlot();
        if (slot >= 45 && slot <= 52) {
            handleCategoryClick(player, clicked.getType());
            return;
        }
        
        // Achat/Vente d'item
        Material material = clicked.getType();
        
        if (!shopManager.isInShop(material)) {
            return;
        }
        
        boolean isLeftClick = event.getClick().isLeftClick();
        boolean isShiftClick = event.getClick().isShiftClick();
        
        int amount = isShiftClick ? 64 : 1;
        
        if (isLeftClick) {
            // Acheter
            shopManager.buyItem(player, material, amount);
        } else {
            // Vendre
            shopManager.sellItem(player, material, amount);
        }
    }
    
    /**
     * Gérer le clic sur un bouton de catégorie
     */
    private void handleCategoryClick(Player player, Material material) {
        ShopManager.ShopCategory category = getCategoryFromMaterial(material);
        
        if (category != null) {
            shopManager.openShop(player, category);
        }
    }
    
    /**
     * Obtenir la catégorie depuis le material du bouton
     */
    private ShopManager.ShopCategory getCategoryFromMaterial(Material material) {
        switch (material) {
            case COBBLESTONE:
                return ShopManager.ShopCategory.BLOCKS;
            case DIAMOND:
                return ShopManager.ShopCategory.ORES;
            case COOKED_BEEF:
                return ShopManager.ShopCategory.FOOD;
            case DIAMOND_PICKAXE:
                return ShopManager.ShopCategory.TOOLS;
            case DIAMOND_SWORD:
                return ShopManager.ShopCategory.COMBAT;
            case EXPERIENCE_BOTTLE:
                return ShopManager.ShopCategory.POTIONS;
            case REDSTONE:
                return ShopManager.ShopCategory.REDSTONE;
            case GLOWSTONE:
                return ShopManager.ShopCategory.DECORATION;
            default:
                return null;
        }
    }
}
