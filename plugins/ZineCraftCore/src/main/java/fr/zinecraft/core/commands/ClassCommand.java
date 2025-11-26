package fr.zinecraft.core.commands;

import fr.zinecraft.core.ZineCraftCore;
import fr.zinecraft.core.rpg.ClassManager;
import fr.zinecraft.core.rpg.ClassType;
import fr.zinecraft.core.rpg.NPCManager;
import fr.zinecraft.core.rpg.PlayerManager;
import fr.zinecraft.core.rpg.RPGPlayer;
import fr.zinecraft.core.rpg.Skill;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Commande pour gérer les classes RPG
 * 
 * @author Otmane & Copilot
 */
public class ClassCommand implements CommandExecutor {
    
    private final ZineCraftCore plugin;
    private final PlayerManager playerManager;
    private final ClassManager classManager;
    private final NPCManager npcManager;
    
    public ClassCommand(PlayerManager playerManager, ClassManager classManager, NPCManager npcManager) {
        this.plugin = ZineCraftCore.getInstance();
        this.playerManager = playerManager;
        this.classManager = classManager;
        this.npcManager = npcManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Cette commande est réservée aux joueurs !");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length == 0) {
            showHelp(player);
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "list":
                listClasses(player);
                break;
                
            case "info":
                showClassInfo(player, args);
                break;
                
            case "choose":
                openClassSelection(player);
                break;
                
            case "spawnnpc":
                if (!player.hasPermission("zinecraft.admin")) {
                    player.sendMessage(ChatColor.RED + "Vous n'avez pas la permission !");
                    return true;
                }
                spawnNPC(player);
                break;
                
            case "skills":
                showSkills(player);
                break;
                
            default:
                showHelp(player);
                break;
        }
        
        return true;
    }
    
    /**
     * Afficher l'aide
     */
    private void showHelp(Player player) {
        RPGPlayer rpgPlayer = playerManager.getPlayer(player);
        
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "⚔ SYSTÈME DE CLASSES ⚔");
        player.sendMessage("");
        
        if (rpgPlayer != null && rpgPlayer.hasChosenClass()) {
            ClassType classType = rpgPlayer.getClassType();
            player.sendMessage(ChatColor.GREEN + "Votre classe: " + classType.getIcon() + " " + 
                             ChatColor.YELLOW + classType.getDisplayName() + ChatColor.GRAY + " (Niveau " + rpgPlayer.getLevel() + ")");
            player.sendMessage("");
        }
        
        player.sendMessage(ChatColor.GRAY + "  /class list " + ChatColor.WHITE + "- Liste des classes");
        player.sendMessage(ChatColor.GRAY + "  /class info <classe> " + ChatColor.WHITE + "- Infos d'une classe");
        player.sendMessage(ChatColor.GRAY + "  /class choose " + ChatColor.WHITE + "- Choisir sa classe");
        player.sendMessage(ChatColor.GRAY + "  /class skills " + ChatColor.WHITE + "- Voir vos compétences");
        player.sendMessage("");
    }
    
    /**
     * Lister toutes les classes
     */
    private void listClasses(Player player) {
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "📜 CLASSES DISPONIBLES 📜");
        player.sendMessage("");
        
        player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "GRATUITES:");
        for (ClassType type : new ClassType[]{ClassType.WARRIOR, ClassType.ARCHER, ClassType.MAGE}) {
            player.sendMessage(ChatColor.GRAY + "  " + type.getIcon() + " " + ChatColor.YELLOW + type.getDisplayName() + 
                             ChatColor.DARK_GRAY + " - " + type.getDescription());
        }
        
        player.sendMessage("");
        player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "VIP (15€):");
        for (ClassType type : new ClassType[]{ClassType.PALADIN, ClassType.ASSASSIN}) {
            player.sendMessage(ChatColor.GRAY + "  " + type.getIcon() + " " + ChatColor.YELLOW + type.getDisplayName() + 
                             ChatColor.DARK_GRAY + " - " + type.getDescription());
        }
        
        player.sendMessage("");
        player.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "VIP+ (30€):");
        for (ClassType type : new ClassType[]{ClassType.NECROMANCER, ClassType.DRUID}) {
            player.sendMessage(ChatColor.GRAY + "  " + type.getIcon() + " " + ChatColor.YELLOW + type.getDisplayName() + 
                             ChatColor.DARK_GRAY + " - " + type.getDescription());
        }
        
        player.sendMessage("");
        player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "LEGEND (60€):");
        player.sendMessage(ChatColor.GRAY + "  " + ClassType.ARCHMAGE.getIcon() + " " + ChatColor.YELLOW + ClassType.ARCHMAGE.getDisplayName() + 
                         ChatColor.DARK_GRAY + " - " + ClassType.ARCHMAGE.getDescription());
        
        player.sendMessage("");
        player.sendMessage(ChatColor.GRAY + "Tapez /class info <classe> pour plus de détails");
        player.sendMessage("");
    }
    
    /**
     * Afficher les infos d'une classe
     */
    private void showClassInfo(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /class info <classe>");
            return;
        }
        
        ClassType classType = ClassType.fromString(args[1]);
        if (classType == null) {
            player.sendMessage(ChatColor.RED + "Classe inconnue: " + args[1]);
            return;
        }
        
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + classType.getIcon() + " " + classType.getDisplayName().toUpperCase() + " " + classType.getIcon());
        player.sendMessage("");
        player.sendMessage(ChatColor.GRAY + classType.getDescription());
        player.sendMessage("");
        player.sendMessage(ChatColor.AQUA + "❤ Vie: " + ChatColor.WHITE + (int)(classType.getBaseHealth() / 2) + " cœurs");
        player.sendMessage(ChatColor.RED + "⚔ Dégâts: " + ChatColor.WHITE + "x" + classType.getBaseDamage());
        player.sendMessage(ChatColor.GREEN + "⚡ Vitesse: " + ChatColor.WHITE + "x" + classType.getBaseSpeed());
        player.sendMessage("");
        
        List<Skill> skills = classManager.getClassSkills(classType);
        if (!skills.isEmpty()) {
            player.sendMessage(ChatColor.GOLD + "✨ Compétences:");
            for (Skill skill : skills) {
                player.sendMessage(ChatColor.YELLOW + "  • " + skill.getDisplayName() + ChatColor.GRAY + " (Niveau " + skill.getRequiredLevel() + ")");
                player.sendMessage(ChatColor.DARK_GRAY + "    " + skill.getDescription());
            }
        }
        
        player.sendMessage("");
        if (classType.isPremium()) {
            player.sendMessage(ChatColor.RED + "💎 Classe premium: " + classType.getPriceEuros() + "€");
        } else {
            player.sendMessage(ChatColor.GREEN + "✔ Classe gratuite");
        }
        player.sendMessage("");
    }
    
    /**
     * Ouvrir le menu de sélection de classe
     */
    private void openClassSelection(Player player) {
        RPGPlayer rpgPlayer = playerManager.getPlayer(player);
        
        if (rpgPlayer == null) {
            player.sendMessage(ChatColor.RED + "Erreur: Profil RPG introuvable!");
            return;
        }
        
        if (rpgPlayer.hasChosenClass()) {
            player.sendMessage(ChatColor.RED + "Vous avez déjà choisi la classe " + 
                             rpgPlayer.getClassType().getIcon() + " " + 
                             ChatColor.YELLOW + rpgPlayer.getClassType().getDisplayName() + ChatColor.RED + " !");
            return;
        }
        
        npcManager.openClassSelectionMenu(player);
    }
    
    /**
     * Spawner le NPC au spawn
     */
    private void spawnNPC(Player player) {
        Location spawnLoc = new Location(player.getWorld(), 0.5, 64, 0.5);
        npcManager.spawnClassMasterNPC(spawnLoc);
        
        player.sendMessage("");
        player.sendMessage(ChatColor.GREEN + "✔ NPC Maître des Classes créé au spawn !");
        player.sendMessage(ChatColor.GRAY + "Position: " + spawnLoc.getBlockX() + ", " + spawnLoc.getBlockY() + ", " + spawnLoc.getBlockZ());
        player.sendMessage("");
    }
    
    /**
     * Afficher les compétences du joueur
     */
    private void showSkills(Player player) {
        RPGPlayer rpgPlayer = playerManager.getPlayer(player);
        
        if (rpgPlayer == null || !rpgPlayer.hasChosenClass()) {
            player.sendMessage(ChatColor.RED + "Vous devez d'abord choisir une classe !");
            return;
        }
        
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "✨ VOS COMPÉTENCES ✨");
        player.sendMessage("");
        player.sendMessage(ChatColor.GREEN + "Classe: " + rpgPlayer.getClassType().getIcon() + " " + 
                         ChatColor.YELLOW + rpgPlayer.getClassType().getDisplayName() + 
                         ChatColor.GRAY + " (Niveau " + rpgPlayer.getLevel() + ")");
        player.sendMessage("");
        
        List<Skill> available = classManager.getAvailableSkills(rpgPlayer);
        List<Skill> locked = classManager.getClassSkills(rpgPlayer.getClassType());
        locked.removeAll(available);
        
        if (!available.isEmpty()) {
            player.sendMessage(ChatColor.GREEN + "✔ Compétences disponibles:");
            for (Skill skill : available) {
                int level = rpgPlayer.getSkillLevel(skill.getName());
                player.sendMessage(ChatColor.YELLOW + "  • " + skill.getDisplayName() + 
                                 ChatColor.GRAY + " (Niveau " + level + "/" + skill.getMaxLevel() + ")");
            }
            player.sendMessage("");
        }
        
        if (!locked.isEmpty()) {
            player.sendMessage(ChatColor.RED + "✘ Compétences verrouillées:");
            for (Skill skill : locked) {
                player.sendMessage(ChatColor.DARK_GRAY + "  • " + skill.getDisplayName() + 
                                 ChatColor.GRAY + " (Niveau " + skill.getRequiredLevel() + " requis)");
            }
            player.sendMessage("");
        }
    }
}
