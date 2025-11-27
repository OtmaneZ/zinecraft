package fr.zinecraft.core.economy;

import fr.zinecraft.core.ZineCraftCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * Gestionnaire de boutique pour acheter/vendre des items
 * 
 * @author Otmane & Copilot
 */
public class ShopManager {
    
    private final ZineCraftCore plugin;
    private final EconomyManager economyManager;
    private final Map<Material, ShopItem> shopItems;
    
    public ShopManager(ZineCraftCore plugin) {
        this.plugin = plugin;
        this.economyManager = plugin.getEconomyManager();
        this.shopItems = new HashMap<>();
        
        initializeShop();
    }
    
    /**
     * Initialiser les items du shop
     */
    private void initializeShop() {
        // ==================== Blocs de base ====================
        addShopItem(Material.COBBLESTONE, 1, 0.5, "Cobblestone");
        addShopItem(Material.DIRT, 1, 0.5, "Terre");
        addShopItem(Material.STONE, 2, 1, "Pierre");
        addShopItem(Material.OAK_LOG, 5, 2, "Bûche de chêne");
        addShopItem(Material.OAK_PLANKS, 2, 1, "Planches de chêne");
        
        // ==================== Minerais ====================
        addShopItem(Material.COAL, 5, 2, "Charbon");
        addShopItem(Material.IRON_INGOT, 20, 10, "Lingot de fer");
        addShopItem(Material.GOLD_INGOT, 50, 25, "Lingot d'or");
        addShopItem(Material.DIAMOND, 200, 100, "Diamant");
        addShopItem(Material.EMERALD, 300, 150, "Émeraude");
        addShopItem(Material.NETHERITE_INGOT, 1000, 500, "Lingot de netherite");
        
        // ==================== Nourriture ====================
        addShopItem(Material.BREAD, 5, 2, "Pain");
        addShopItem(Material.COOKED_BEEF, 10, 5, "Steak cuit");
        addShopItem(Material.GOLDEN_APPLE, 50, 25, "Pomme dorée");
        addShopItem(Material.ENCHANTED_GOLDEN_APPLE, 500, 250, "Pomme dorée enchantée");
        
        // ==================== Outils ====================
        addShopItem(Material.IRON_PICKAXE, 100, 50, "Pioche en fer");
        addShopItem(Material.IRON_AXE, 100, 50, "Hache en fer");
        addShopItem(Material.IRON_SHOVEL, 80, 40, "Pelle en fer");
        addShopItem(Material.DIAMOND_PICKAXE, 500, 250, "Pioche en diamant");
        addShopItem(Material.DIAMOND_AXE, 500, 250, "Hache en diamant");
        
        // ==================== Armes & Armures ====================
        addShopItem(Material.IRON_SWORD, 100, 50, "Épée en fer");
        addShopItem(Material.DIAMOND_SWORD, 500, 250, "Épée en diamant");
        addShopItem(Material.BOW, 50, 25, "Arc");
        addShopItem(Material.ARROW, 2, 1, "Flèche");
        addShopItem(Material.SHIELD, 80, 40, "Bouclier");
        
        addShopItem(Material.IRON_HELMET, 80, 40, "Casque en fer");
        addShopItem(Material.IRON_CHESTPLATE, 150, 75, "Plastron en fer");
        addShopItem(Material.IRON_LEGGINGS, 120, 60, "Jambières en fer");
        addShopItem(Material.IRON_BOOTS, 70, 35, "Bottes en fer");
        
        addShopItem(Material.DIAMOND_HELMET, 400, 200, "Casque en diamant");
        addShopItem(Material.DIAMOND_CHESTPLATE, 700, 350, "Plastron en diamant");
        addShopItem(Material.DIAMOND_LEGGINGS, 600, 300, "Jambières en diamant");
        addShopItem(Material.DIAMOND_BOOTS, 350, 175, "Bottes en diamant");
        
        // ==================== Potions & Enchantements ====================
        addShopItem(Material.EXPERIENCE_BOTTLE, 50, 25, "Bouteille d'XP");
        addShopItem(Material.ENCHANTED_BOOK, 100, 50, "Livre enchanté");
        addShopItem(Material.ENDER_PEARL, 30, 15, "Perle de l'Ender");
        addShopItem(Material.ENDER_EYE, 100, 50, "Œil de l'Ender");
        
        // ==================== Redstone & Techniques ====================
        addShopItem(Material.REDSTONE, 10, 5, "Redstone");
        addShopItem(Material.PISTON, 20, 10, "Piston");
        addShopItem(Material.TNT, 50, 25, "TNT");
        addShopItem(Material.OBSERVER, 30, 15, "Observateur");
        
        // ==================== Décoration ====================
        addShopItem(Material.GLASS, 5, 2, "Verre");
        addShopItem(Material.GLOWSTONE, 15, 7, "Pierre lumineuse");
        addShopItem(Material.SEA_LANTERN, 20, 10, "Lanterne aquatique");
        addShopItem(Material.BEACON, 1000, 500, "Balise");
        
        plugin.getLogger().info(shopItems.size() + " items ajoutés au shop!");
    }
    
    /**
     * Ajouter un item au shop
     */
    private void addShopItem(Material material, double buyPrice, double sellPrice, String displayName) {
        shopItems.put(material, new ShopItem(material, buyPrice, sellPrice, displayName));
    }
    
    /**
     * Obtenir un item du shop
     */
    public ShopItem getShopItem(Material material) {
        return shopItems.get(material);
    }
    
    /**
     * Vérifier si un item est dans le shop
     */
    public boolean isInShop(Material material) {
        return shopItems.containsKey(material);
    }
    
    /**
     * Ouvrir le menu du shop
     */
    public void openShop(Player player, ShopCategory category) {
        Inventory inventory = Bukkit.createInventory(null, 54, 
            ChatColor.GOLD + "" + ChatColor.BOLD + "💰 BOUTIQUE - " + category.getName());
        
        List<ShopItem> categoryItems = getItemsByCategory(category);
        
        int slot = 0;
        for (ShopItem item : categoryItems) {
            if (slot >= 45) break; // Max 45 items (5 rangées)
            
            ItemStack displayItem = createShopItemStack(item);
            inventory.setItem(slot, displayItem);
            slot++;
        }
        
        // Boutons de navigation (dernière rangée)
        inventory.setItem(45, createCategoryButton(ShopCategory.BLOCKS));
        inventory.setItem(46, createCategoryButton(ShopCategory.ORES));
        inventory.setItem(47, createCategoryButton(ShopCategory.FOOD));
        inventory.setItem(48, createCategoryButton(ShopCategory.TOOLS));
        inventory.setItem(49, createCategoryButton(ShopCategory.COMBAT));
        inventory.setItem(50, createCategoryButton(ShopCategory.POTIONS));
        inventory.setItem(51, createCategoryButton(ShopCategory.REDSTONE));
        inventory.setItem(52, createCategoryButton(ShopCategory.DECORATION));
        
        // Bouton fermer
        ItemStack closeButton = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = closeButton.getItemMeta();
        closeMeta.setDisplayName(ChatColor.RED + "Fermer");
        closeButton.setItemMeta(closeMeta);
        inventory.setItem(53, closeButton);
        
        player.openInventory(inventory);
    }
    
    /**
     * Créer un ItemStack pour afficher dans le shop
     */
    private ItemStack createShopItemStack(ShopItem item) {
        ItemStack stack = new ItemStack(item.getMaterial());
        ItemMeta meta = stack.getItemMeta();
        
        meta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + item.getDisplayName());
        
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.GREEN + "Prix d'achat: " + ChatColor.GOLD + (int)item.getBuyPrice() + " Zines");
        lore.add(ChatColor.RED + "Prix de vente: " + ChatColor.GOLD + (int)item.getSellPrice() + " Zines");
        lore.add("");
        lore.add(ChatColor.GRAY + "Clic gauche: Acheter x1");
        lore.add(ChatColor.GRAY + "Shift + Clic gauche: Acheter x64");
        lore.add(ChatColor.GRAY + "Clic droit: Vendre x1");
        lore.add(ChatColor.GRAY + "Shift + Clic droit: Vendre x64");
        
        meta.setLore(lore);
        stack.setItemMeta(meta);
        
        return stack;
    }
    
    /**
     * Créer un bouton de catégorie
     */
    private ItemStack createCategoryButton(ShopCategory category) {
        ItemStack button = new ItemStack(category.getIcon());
        ItemMeta meta = button.getItemMeta();
        
        meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + category.getName());
        button.setItemMeta(meta);
        
        return button;
    }
    
    /**
     * Obtenir les items d'une catégorie
     */
    private List<ShopItem> getItemsByCategory(ShopCategory category) {
        List<ShopItem> items = new ArrayList<>();
        
        for (ShopItem item : shopItems.values()) {
            if (item.getCategory() == category) {
                items.add(item);
            }
        }
        
        return items;
    }
    
    /**
     * Acheter un item
     */
    public boolean buyItem(Player player, Material material, int amount) {
        ShopItem shopItem = getShopItem(material);
        
        if (shopItem == null) {
            player.sendMessage(ChatColor.RED + "Cet item n'est pas disponible à l'achat!");
            return false;
        }
        
        int totalPrice = (int)(shopItem.getBuyPrice() * amount);
        
        // Vérifier le solde
        if (!economyManager.hasAmount(player, totalPrice)) {
            player.sendMessage(ChatColor.RED + "Vous n'avez pas assez de Zines!");
            player.sendMessage(ChatColor.GRAY + "Prix: " + totalPrice + " Zines");
            player.sendMessage(ChatColor.GRAY + "Votre solde: " + economyManager.getBalance(player) + " Zines");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return false;
        }
        
        // Retirer les Zines
        economyManager.removeZines(player, totalPrice, "Achat " + amount + "x " + shopItem.getDisplayName());
        
        // Donner l'item
        ItemStack item = new ItemStack(material, amount);
        player.getInventory().addItem(item);
        
        // Confirmation
        player.sendMessage(ChatColor.GREEN + "✔ Vous avez acheté " + ChatColor.YELLOW + amount + "x " + 
                          shopItem.getDisplayName() + ChatColor.GREEN + " pour " + 
                          ChatColor.GOLD + totalPrice + " Zines");
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
        
        return true;
    }
    
    /**
     * Vendre un item
     */
    public boolean sellItem(Player player, Material material, int amount) {
        ShopItem shopItem = getShopItem(material);
        
        if (shopItem == null) {
            player.sendMessage(ChatColor.RED + "Cet item ne peut pas être vendu!");
            return false;
        }
        
        // Vérifier l'inventaire
        if (!hasItemInInventory(player, material, amount)) {
            player.sendMessage(ChatColor.RED + "Vous n'avez pas assez d'items!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return false;
        }
        
        int totalPrice = (int)(shopItem.getSellPrice() * amount);
        
        // Retirer l'item
        removeItemFromInventory(player, material, amount);
        
        // Ajouter les Zines
        economyManager.addZines(player, totalPrice, "Vente " + amount + "x " + shopItem.getDisplayName());
        
        // Confirmation
        player.sendMessage(ChatColor.GREEN + "✔ Vous avez vendu " + ChatColor.YELLOW + amount + "x " + 
                          shopItem.getDisplayName() + ChatColor.GREEN + " pour " + 
                          ChatColor.GOLD + totalPrice + " Zines");
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.8f);
        
        return true;
    }
    
    /**
     * Vérifier si le joueur a l'item dans son inventaire
     */
    private boolean hasItemInInventory(Player player, Material material, int amount) {
        int count = 0;
        
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == material) {
                count += item.getAmount();
            }
        }
        
        return count >= amount;
    }
    
    /**
     * Retirer un item de l'inventaire
     */
    private void removeItemFromInventory(Player player, Material material, int amount) {
        int remaining = amount;
        
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == material && remaining > 0) {
                int itemAmount = item.getAmount();
                
                if (itemAmount <= remaining) {
                    remaining -= itemAmount;
                    item.setAmount(0);
                } else {
                    item.setAmount(itemAmount - remaining);
                    remaining = 0;
                }
            }
        }
    }
    
    // ==================== Classes internes ====================
    
    /**
     * Représente un item dans le shop
     */
    public static class ShopItem {
        private final Material material;
        private final double buyPrice;
        private final double sellPrice;
        private final String displayName;
        
        public ShopItem(Material material, double buyPrice, double sellPrice, String displayName) {
            this.material = material;
            this.buyPrice = buyPrice;
            this.sellPrice = sellPrice;
            this.displayName = displayName;
        }
        
        public Material getMaterial() { return material; }
        public double getBuyPrice() { return buyPrice; }
        public double getSellPrice() { return sellPrice; }
        public String getDisplayName() { return displayName; }
        
        public ShopCategory getCategory() {
            String name = material.name();
            
            if (name.contains("ORE") || name.contains("INGOT") || name.equals("DIAMOND") || 
                name.equals("EMERALD") || name.equals("COAL")) {
                return ShopCategory.ORES;
            } else if (name.contains("FOOD") || name.contains("BREAD") || name.contains("BEEF") || 
                      name.contains("APPLE")) {
                return ShopCategory.FOOD;
            } else if (name.contains("PICKAXE") || name.contains("AXE") || name.contains("SHOVEL")) {
                return ShopCategory.TOOLS;
            } else if (name.contains("SWORD") || name.contains("BOW") || name.contains("ARMOR") || 
                      name.contains("HELMET") || name.contains("CHESTPLATE") || 
                      name.contains("LEGGINGS") || name.contains("BOOTS") || name.contains("SHIELD")) {
                return ShopCategory.COMBAT;
            } else if (name.contains("POTION") || name.contains("BOTTLE") || name.contains("BOOK") || 
                      name.contains("ENDER")) {
                return ShopCategory.POTIONS;
            } else if (name.contains("REDSTONE") || name.contains("PISTON") || name.contains("TNT") || 
                      name.contains("OBSERVER")) {
                return ShopCategory.REDSTONE;
            } else if (name.contains("GLASS") || name.contains("LANTERN") || name.contains("BEACON") || 
                      name.contains("GLOWSTONE")) {
                return ShopCategory.DECORATION;
            } else {
                return ShopCategory.BLOCKS;
            }
        }
    }
    
    /**
     * Catégories du shop
     */
    public enum ShopCategory {
        BLOCKS("Blocs", Material.COBBLESTONE),
        ORES("Minerais", Material.DIAMOND),
        FOOD("Nourriture", Material.COOKED_BEEF),
        TOOLS("Outils", Material.DIAMOND_PICKAXE),
        COMBAT("Combat", Material.DIAMOND_SWORD),
        POTIONS("Potions & Enchantements", Material.EXPERIENCE_BOTTLE),
        REDSTONE("Redstone", Material.REDSTONE),
        DECORATION("Décoration", Material.GLOWSTONE);
        
        private final String name;
        private final Material icon;
        
        ShopCategory(String name, Material icon) {
            this.name = name;
            this.icon = icon;
        }
        
        public String getName() { return name; }
        public Material getIcon() { return icon; }
    }
}
