package fr.zinecraft.core.rpg;

import fr.zinecraft.core.ZineCraftCore;
import org.bukkit.entity.Player;
import org.bukkit.entity.EntityType;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.Particle;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Gestionnaire de l'XP et des niveaux
 * Sources d'XP : mobs, boss, quêtes, craft, mining, exploration
 * 
 * @author Otmane & Copilot
 */
public class LevelManager {
    
    private final ZineCraftCore plugin;
    private final PlayerManager playerManager;
    private final Map<UUID, BossBar> xpBars;
    
    // Multiplicateurs d'XP selon la classe
    private static final Map<ClassType, Double> CLASS_XP_MULTIPLIERS = new HashMap<>();
    
    static {
        // Classes gratuites : XP normale
        CLASS_XP_MULTIPLIERS.put(ClassType.WARRIOR, 1.0);
        CLASS_XP_MULTIPLIERS.put(ClassType.ARCHER, 1.0);
        CLASS_XP_MULTIPLIERS.put(ClassType.MAGE, 1.0);
        
        // Classes VIP : +50% XP
        CLASS_XP_MULTIPLIERS.put(ClassType.PALADIN, 1.5);
        CLASS_XP_MULTIPLIERS.put(ClassType.ASSASSIN, 1.5);
        
        // Classes VIP+ : +100% XP
        CLASS_XP_MULTIPLIERS.put(ClassType.NECROMANCER, 2.0);
        CLASS_XP_MULTIPLIERS.put(ClassType.DRUID, 2.0);
        
        // Classe LEGEND : +200% XP
        CLASS_XP_MULTIPLIERS.put(ClassType.ARCHMAGE, 3.0);
    }
    
    public LevelManager(ZineCraftCore plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
        this.xpBars = new HashMap<>();
    }
    
    // ==================== XP par source ====================
    
    /**
     * XP pour avoir tué un mob
     */
    public int getMobKillXP(EntityType entityType) {
        switch (entityType) {
            // Mobs faciles (10-20 XP)
            case ZOMBIE:
            case SKELETON:
            case SPIDER:
            case CAVE_SPIDER:
                return 15;
            
            case CREEPER:
            case DROWNED:
            case HUSK:
            case STRAY:
                return 20;
            
            // Mobs moyens (25-40 XP)
            case ENDERMAN:
            case WITCH:
            case PILLAGER:
            case VINDICATOR:
                return 30;
            
            case BLAZE:
            case GHAST:
            case PIGLIN_BRUTE:
                return 40;
            
            // Mobs difficiles (50-80 XP)
            case WITHER_SKELETON:
            case RAVAGER:
            case EVOKER:
                return 60;
            
            case SHULKER:
            case PHANTOM:
                return 50;
            
            // Boss vanilla (100-500 XP)
            case ELDER_GUARDIAN:
                return 150;
            
            case WARDEN:
                return 300;
            
            case ENDER_DRAGON:
                return 1000;
            
            case WITHER:
                return 500;
            
            // Animaux passifs (5 XP)
            case COW:
            case PIG:
            case SHEEP:
            case CHICKEN:
                return 5;
            
            default:
                return 10;
        }
    }
    
    /**
     * XP pour avoir miné un bloc
     */
    public int getMiningXP(Material material) {
        switch (material) {
            // Pierres communes (1-3 XP)
            case STONE:
            case COBBLESTONE:
            case ANDESITE:
            case DIORITE:
            case GRANITE:
                return 1;
            
            case DEEPSLATE:
            case TUFF:
                return 2;
            
            // Minerais communs (5-10 XP)
            case COAL_ORE:
            case DEEPSLATE_COAL_ORE:
                return 5;
            
            case COPPER_ORE:
            case DEEPSLATE_COPPER_ORE:
                return 6;
            
            case IRON_ORE:
            case DEEPSLATE_IRON_ORE:
                return 8;
            
            // Minerais rares (15-30 XP)
            case GOLD_ORE:
            case DEEPSLATE_GOLD_ORE:
                return 15;
            
            case LAPIS_ORE:
            case DEEPSLATE_LAPIS_ORE:
                return 12;
            
            case REDSTONE_ORE:
            case DEEPSLATE_REDSTONE_ORE:
                return 10;
            
            // Minerais très rares (40-100 XP)
            case DIAMOND_ORE:
            case DEEPSLATE_DIAMOND_ORE:
                return 50;
            
            case EMERALD_ORE:
            case DEEPSLATE_EMERALD_ORE:
                return 80;
            
            case ANCIENT_DEBRIS:
                return 100;
            
            default:
                return 0;
        }
    }
    
    /**
     * XP pour avoir crafté un item
     */
    public int getCraftingXP(Material material) {
        // Items basiques (2-5 XP)
        if (material.name().contains("PLANKS") || material.name().contains("STICK")) {
            return 2;
        }
        
        // Outils en bois/pierre (5-10 XP)
        if (material.name().contains("WOODEN_") || material.name().contains("STONE_")) {
            return 5;
        }
        
        // Outils en fer (15 XP)
        if (material.name().contains("IRON_")) {
            return 15;
        }
        
        // Outils en diamant (30 XP)
        if (material.name().contains("DIAMOND_")) {
            return 30;
        }
        
        // Outils en netherite (50 XP)
        if (material.name().contains("NETHERITE_")) {
            return 50;
        }
        
        // Armures
        if (material.name().contains("HELMET") || material.name().contains("CHESTPLATE") ||
            material.name().contains("LEGGINGS") || material.name().contains("BOOTS")) {
            if (material.name().contains("DIAMOND")) return 40;
            if (material.name().contains("IRON")) return 20;
            return 10;
        }
        
        return 3;
    }
    
    /**
     * XP bonus pour avoir tué un boss custom
     */
    public int getBossKillXP(String bossType) {
        switch (bossType.toUpperCase()) {
            case "TITAN":
                return 500;
            case "DRAGON":
                return 800;
            case "DEMON":
                return 600;
            case "GOLEM":
                return 400;
            case "PHOENIX":
                return 700;
            case "KRAKEN":
                return 1000;
            default:
                return 300;
        }
    }
    
    // ==================== Gestion de l'XP ====================
    
    /**
     * Ajouter de l'XP à un joueur avec gestion du level up
     */
    public void addExperience(Player player, int baseXP, String source) {
        RPGPlayer rpgPlayer = playerManager.getPlayer(player);
        
        if (rpgPlayer == null || !rpgPlayer.hasChosenClass()) {
            return;
        }
        
        // Appliquer le multiplicateur de classe
        double multiplier = CLASS_XP_MULTIPLIERS.getOrDefault(rpgPlayer.getClassType(), 1.0);
        int finalXP = (int) (baseXP * multiplier);
        
        // Ajouter l'XP
        boolean leveledUp = rpgPlayer.addExperience(finalXP);
        
        // Afficher la barre d'XP
        showXPBar(player, rpgPlayer);
        
        // Message XP
        player.sendMessage(ChatColor.GOLD + "+ " + finalXP + " XP " + 
                          ChatColor.GRAY + "(" + source + ")");
        
        // Level up !
        if (leveledUp) {
            handleLevelUp(player, rpgPlayer);
        }
        
        // Sauvegarder
        playerManager.savePlayer(rpgPlayer);
    }
    
    /**
     * Gérer le level up
     */
    private void handleLevelUp(Player player, RPGPlayer rpgPlayer) {
        int oldLevel = rpgPlayer.getLevel();
        int newLevel = oldLevel + 1;
        
        // Calculer l'XP restante après le level up
        int requiredXP = rpgPlayer.getRequiredExperience();
        int excessXP = rpgPlayer.getExperience() - requiredXP;
        
        // Monter de niveau
        rpgPlayer.setLevel(newLevel);
        rpgPlayer.setExperience(excessXP);
        
        // Donner un skill point
        rpgPlayer.setSkillPoints(rpgPlayer.getSkillPoints() + 1);
        
        // Effets visuels
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        player.spawnParticle(Particle.FIREWORKS_SPARK, player.getLocation().add(0, 1, 0), 50, 0.5, 0.5, 0.5, 0.1);
        player.spawnParticle(Particle.TOTEM, player.getLocation().add(0, 1, 0), 30, 0.3, 0.3, 0.3, 0.05);
        
        // Messages
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "✨ LEVEL UP ! ✨");
        player.sendMessage("");
        player.sendMessage(ChatColor.GREEN + "Nouveau niveau: " + ChatColor.YELLOW + ChatColor.BOLD + newLevel);
        player.sendMessage(ChatColor.AQUA + "+1 Point de Compétence");
        player.sendMessage("");
        player.sendMessage(ChatColor.GRAY + "Tapez /class skills pour utiliser vos points !");
        player.sendMessage("");
        
        // Récompenses selon le niveau
        giveLevelRewards(player, rpgPlayer, newLevel);
        
        // Vérifier si de nouvelles compétences sont débloquées
        checkUnlockedSkills(player, rpgPlayer, newLevel);
    }
    
    /**
     * Donner des récompenses selon le niveau atteint
     */
    private void giveLevelRewards(Player player, RPGPlayer rpgPlayer, int level) {
        // Tous les 5 niveaux : bonus de Zines
        if (level % 5 == 0) {
            int bonus = level * 10;
            rpgPlayer.addZines(bonus);
            player.sendMessage(ChatColor.GOLD + "🪙 Bonus: +" + bonus + " Zines !");
        }
        
        // Niveaux spéciaux
        switch (level) {
            case 10:
                player.sendMessage(ChatColor.LIGHT_PURPLE + "🎁 Vous avez débloqué votre première compétence active !");
                break;
            case 20:
                player.sendMessage(ChatColor.LIGHT_PURPLE + "🎁 Vous avez débloqué votre compétence ultime !");
                break;
            case 50:
                player.sendMessage(ChatColor.RED + "🎁 Niveau LÉGENDAIRE atteint ! Récompense spéciale à venir...");
                break;
            case 100:
                player.sendMessage(ChatColor.DARK_RED + "🎁 NIVEAU MAXIMUM ! Vous êtes une LÉGENDE !");
                break;
        }
    }
    
    /**
     * Vérifier les nouvelles compétences débloquées
     */
    private void checkUnlockedSkills(Player player, RPGPlayer rpgPlayer, int level) {
        ClassManager classManager = plugin.getClassManager();
        
        for (Skill skill : classManager.getClassSkills(rpgPlayer.getClassType())) {
            if (skill.getRequiredLevel() == level) {
                player.sendMessage(ChatColor.YELLOW + "🔓 Nouvelle compétence: " + 
                                 ChatColor.GOLD + skill.getDisplayName());
            }
        }
    }
    
    /**
     * Afficher la barre d'XP au joueur
     */
    public void showXPBar(Player player, RPGPlayer rpgPlayer) {
        UUID playerId = player.getUniqueId();
        
        // Récupérer ou créer la barre
        BossBar bar = xpBars.get(playerId);
        if (bar == null) {
            bar = plugin.getServer().createBossBar(
                "XP Progress",
                BarColor.GREEN,
                BarStyle.SEGMENTED_10
            );
            bar.addPlayer(player);
            xpBars.put(playerId, bar);
        }
        
        // Calculer le pourcentage
        int currentXP = rpgPlayer.getExperience();
        int requiredXP = rpgPlayer.getRequiredExperience();
        double progress = Math.min(1.0, (double) currentXP / requiredXP);
        
        // Mettre à jour la barre
        bar.setTitle(ChatColor.GREEN + "Niveau " + rpgPlayer.getLevel() + " " + 
                    ChatColor.YELLOW + currentXP + "/" + requiredXP + " XP " +
                    ChatColor.GRAY + "(" + (int)(progress * 100) + "%)");
        bar.setProgress(progress);
        bar.setVisible(true);
        
        // Cacher après 5 secondes
        final BossBar finalBar = bar;
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (finalBar != null) {
                finalBar.setVisible(false);
            }
        }, 100L); // 5 secondes
    }
    
    /**
     * Retirer la barre XP d'un joueur
     */
    public void removeXPBar(UUID playerId) {
        BossBar bar = xpBars.remove(playerId);
        if (bar != null) {
            bar.removeAll();
        }
    }
    
    /**
     * Obtenir le multiplicateur XP d'une classe
     */
    public double getClassXPMultiplier(ClassType classType) {
        return CLASS_XP_MULTIPLIERS.getOrDefault(classType, 1.0);
    }
}
