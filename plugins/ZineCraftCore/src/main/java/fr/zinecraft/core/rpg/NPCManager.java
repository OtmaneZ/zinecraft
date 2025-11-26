package fr.zinecraft.core.rpg;

import fr.zinecraft.core.ZineCraftCore;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Gestionnaire des NPCs pour la sélection de classe
 * 
 * @author Otmane & Copilot
 */
public class NPCManager {
    
    private final ZineCraftCore plugin;
    private final ClassManager classManager;
    private Villager classNPC;
    private final Map<UUID, Long> npcCooldowns;
    
    public NPCManager(ZineCraftCore plugin, ClassManager classManager) {
        this.plugin = plugin;
        this.classManager = classManager;
        this.npcCooldowns = new HashMap<>();
    }
    
    /**
     * Créer le NPC Maître des Classes au spawn
     */
    public void spawnClassMasterNPC(Location location) {
        // Supprimer l'ancien NPC s'il existe
        if (classNPC != null && !classNPC.isDead()) {
            classNPC.remove();
        }
        
        // Créer le villager
        classNPC = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
        
        // Configuration
        classNPC.setCustomName(ChatColor.GOLD + "" + ChatColor.BOLD + "⚔ Maître des Classes ⚔");
        classNPC.setCustomNameVisible(true);
        classNPC.setAI(false);
        classNPC.setSilent(true);
        classNPC.setInvulnerable(true);
        classNPC.setPersistent(true);
        classNPC.setVillagerType(Villager.Type.PLAINS);
        classNPC.setProfession(Villager.Profession.CLERIC);
        
        plugin.getLogger().info("NPC Maître des Classes créé à " + 
            location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ());
    }
    
    /**
     * Vérifier si une entité est le NPC de classe
     */
    public boolean isClassMasterNPC(Entity entity) {
        return classNPC != null && entity.getUniqueId().equals(classNPC.getUniqueId());
    }
    
    /**
     * Gérer l'interaction avec le NPC
     */
    public void handleNPCInteraction(Player player, Entity npc) {
        if (!isClassMasterNPC(npc)) {
            return;
        }
        
        // Cooldown (éviter le spam)
        UUID playerId = player.getUniqueId();
        long now = System.currentTimeMillis();
        if (npcCooldowns.containsKey(playerId)) {
            long lastInteraction = npcCooldowns.get(playerId);
            if (now - lastInteraction < 1000) { // 1 seconde
                return;
            }
        }
        npcCooldowns.put(playerId, now);
        
        // Ouvrir le menu de sélection
        openClassSelectionMenu(player);
    }
    
    /**
     * Ouvrir le menu GUI de sélection de classe
     */
    public void openClassSelectionMenu(Player player) {
        Inventory inv = plugin.getServer().createInventory(null, 27, 
            ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "⚔ Choix de Classe ⚔");
        
        // Classes gratuites
        inv.setItem(10, createClassItem(ClassType.WARRIOR));
        inv.setItem(12, createClassItem(ClassType.ARCHER));
        inv.setItem(14, createClassItem(ClassType.MAGE));
        
        // Classes VIP
        inv.setItem(19, createClassItem(ClassType.PALADIN));
        inv.setItem(21, createClassItem(ClassType.ASSASSIN));
        
        // Classes VIP+
        inv.setItem(23, createClassItem(ClassType.NECROMANCER));
        inv.setItem(25, createClassItem(ClassType.DRUID));
        
        // Classe LEGEND (centre bas)
        inv.setItem(16, createClassItem(ClassType.ARCHMAGE));
        
        player.openInventory(inv);
    }
    
    /**
     * Créer un ItemStack représentant une classe
     */
    private ItemStack createClassItem(ClassType classType) {
        Material material;
        
        switch (classType) {
            case WARRIOR:
                material = Material.IRON_SWORD;
                break;
            case ARCHER:
                material = Material.BOW;
                break;
            case MAGE:
                material = Material.BLAZE_ROD;
                break;
            case PALADIN:
                material = Material.GOLDEN_SWORD;
                break;
            case ASSASSIN:
                material = Material.NETHERITE_SWORD;
                break;
            case NECROMANCER:
                material = Material.BONE;
                break;
            case DRUID:
                material = Material.OAK_SAPLING;
                break;
            case ARCHMAGE:
                material = Material.NETHER_STAR;
                break;
            default:
                material = Material.STICK;
        }
        
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        // Nom
        meta.setDisplayName(classType.getIcon() + " " + ChatColor.YELLOW + ChatColor.BOLD + classType.getDisplayName());
        
        // Lore (description)
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.GRAY + classType.getDescription());
        lore.add("");
        lore.add(ChatColor.AQUA + "❤ Vie: " + ChatColor.WHITE + (int)(classType.getBaseHealth() / 2) + " cœurs");
        lore.add(ChatColor.RED + "⚔ Dégâts: " + ChatColor.WHITE + "x" + classType.getBaseDamage());
        lore.add(ChatColor.GREEN + "⚡ Vitesse: " + ChatColor.WHITE + "x" + classType.getBaseSpeed());
        lore.add("");
        
        // Compétences
        List<Skill> skills = classManager.getClassSkills(classType);
        if (!skills.isEmpty()) {
            lore.add(ChatColor.GOLD + "✨ Compétences:");
            for (Skill skill : skills) {
                lore.add(ChatColor.GRAY + "  • " + ChatColor.YELLOW + skill.getDisplayName() + 
                        ChatColor.DARK_GRAY + " (Lv." + skill.getRequiredLevel() + ")");
            }
            lore.add("");
        }
        
        // Prix si premium
        if (classType.isPremium()) {
            lore.add(ChatColor.RED + "💎 PREMIUM: " + classType.getPriceEuros() + "€");
            lore.add(ChatColor.GRAY + "(Achat sur la boutique)");
        } else {
            lore.add(ChatColor.GREEN + "✔ GRATUIT");
        }
        
        lore.add("");
        lore.add(ChatColor.YELLOW + "» Cliquez pour choisir «");
        
        meta.setLore(lore);
        item.setItemMeta(meta);
        
        return item;
    }
    
    /**
     * Gérer la sélection d'une classe dans le GUI
     */
    public void handleClassSelection(Player player, ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return;
        }
        
        String displayName = item.getItemMeta().getDisplayName();
        
        // Identifier la classe par l'item
        ClassType selectedClass = null;
        for (ClassType type : ClassType.values()) {
            if (displayName.contains(type.getDisplayName())) {
                selectedClass = type;
                break;
            }
        }
        
        if (selectedClass == null) {
            return;
        }
        
        // Fermer l'inventaire
        player.closeInventory();
        
        // Assigner la classe
        classManager.assignClass(player, selectedClass);
    }
    
    /**
     * Obtenir le NPC
     */
    public Villager getClassNPC() {
        return classNPC;
    }
    
    /**
     * Vérifier si le NPC existe
     */
    public boolean hasNPC() {
        return classNPC != null && !classNPC.isDead();
    }
}
