package fr.zinecraft.core.commands;

import fr.zinecraft.core.ZineCraftCore;
import fr.zinecraft.core.quests.Quest;
import fr.zinecraft.core.quests.QuestManager;
import fr.zinecraft.core.quests.QuestObjective;
import fr.zinecraft.core.quests.PlayerQuestProgress;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Commande /quest pour gérer les quêtes
 * 
 * @author Otmane & Copilot
 */
public class QuestCommand implements CommandExecutor {
    
    private final ZineCraftCore plugin;
    private final QuestManager questManager;
    
    public QuestCommand(ZineCraftCore plugin) {
        this.plugin = plugin;
        this.questManager = plugin.getQuestManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Cette commande est réservée aux joueurs!");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length == 0) {
            showHelp(player);
            return true;
        }
        
        String action = args[0].toLowerCase();
        
        switch (action) {
            case "list":
                return handleList(player);
                
            case "available":
                return handleAvailable(player);
                
            case "start":
            case "accept":
                if (args.length < 2) {
                    player.sendMessage(ChatColor.RED + "Usage: /quest start <questKey>");
                    return true;
                }
                return handleStart(player, args[1]);
                
            case "info":
                if (args.length < 2) {
                    player.sendMessage(ChatColor.RED + "Usage: /quest info <questKey>");
                    return true;
                }
                return handleInfo(player, args[1]);
                
            case "progress":
                return handleProgress(player);
                
            case "complete":
            case "finish":
                if (args.length < 2) {
                    player.sendMessage(ChatColor.RED + "Usage: /quest complete <questKey>");
                    return true;
                }
                return handleComplete(player, args[1]);
                
            default:
                showHelp(player);
                return true;
        }
    }
    
    /**
     * Afficher l'aide
     */
    private void showHelp(Player player) {
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "═══════════ QUÊTES ═══════════");
        player.sendMessage(ChatColor.YELLOW + "/quest list - Vos quêtes en cours");
        player.sendMessage(ChatColor.YELLOW + "/quest available - Quêtes disponibles");
        player.sendMessage(ChatColor.YELLOW + "/quest start <nom> - Démarrer une quête");
        player.sendMessage(ChatColor.YELLOW + "/quest info <nom> - Infos sur une quête");
        player.sendMessage(ChatColor.YELLOW + "/quest progress - Voir votre progression");
        player.sendMessage(ChatColor.YELLOW + "/quest complete <nom> - Terminer une quête");
        player.sendMessage(ChatColor.GOLD + "════════════════════════════════");
        player.sendMessage("");
    }
    
    /**
     * Lister les quêtes en cours
     */
    private boolean handleList(Player player) {
        List<PlayerQuestProgress> progressList = questManager.getPlayerQuests(player);
        
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "═══════════ VOS QUÊTES ═══════════");
        
        if (progressList.isEmpty()) {
            player.sendMessage(ChatColor.GRAY + "Aucune quête en cours.");
            player.sendMessage(ChatColor.YELLOW + "Utilisez /quest available pour voir les quêtes disponibles.");
        } else {
            for (PlayerQuestProgress progress : progressList) {
                Quest quest = questManager.getQuest(progress.getQuestId());
                
                if (quest != null) {
                    String statusColor = getStatusColor(progress.getStatus());
                    player.sendMessage("");
                    player.sendMessage(ChatColor.YELLOW + "• " + quest.getQuestName());
                    player.sendMessage(ChatColor.GRAY + "  Statut: " + statusColor + progress.getStatus().getDisplayName());
                    
                    if (progress.getStatus() == PlayerQuestProgress.QuestStatus.IN_PROGRESS) {
                        // Afficher progression
                        for (QuestObjective objective : quest.getObjectives()) {
                            int current = progress.getProgress(objective.getObjectiveKey());
                            int target = objective.getTargetAmount();
                            String progressBar = createProgressBar(current, target, 10);
                            
                            player.sendMessage(ChatColor.GRAY + "  " + objective.getDescription() + 
                                             " " + progressBar + " " + ChatColor.WHITE + current + "/" + target);
                        }
                    }
                }
            }
        }
        
        player.sendMessage(ChatColor.GOLD + "════════════════════════════════════");
        player.sendMessage("");
        
        return true;
    }
    
    /**
     * Lister les quêtes disponibles
     */
    private boolean handleAvailable(Player player) {
        List<Quest> available = questManager.getAvailableQuests(player);
        
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "═══════ QUÊTES DISPONIBLES ═══════");
        
        if (available.isEmpty()) {
            player.sendMessage(ChatColor.GRAY + "Aucune quête disponible pour votre niveau.");
        } else {
            for (Quest quest : available) {
                player.sendMessage("");
                player.sendMessage(ChatColor.YELLOW + "• " + quest.getQuestName() + 
                                 ChatColor.GRAY + " [Niv. " + quest.getRequiredLevel() + "]");
                player.sendMessage(ChatColor.GRAY + "  " + quest.getDescription());
                player.sendMessage(ChatColor.GREEN + "  Récompenses: " + 
                                 ChatColor.AQUA + quest.getRewardXP() + " XP " +
                                 ChatColor.YELLOW + quest.getRewardZines() + " Zines");
                player.sendMessage(ChatColor.GRAY + "  /quest start " + quest.getQuestKey());
            }
        }
        
        player.sendMessage(ChatColor.GOLD + "════════════════════════════════════");
        player.sendMessage("");
        
        return true;
    }
    
    /**
     * Démarrer une quête
     */
    private boolean handleStart(Player player, String questKey) {
        Quest quest = questManager.getQuestByKey(questKey);
        
        if (quest == null) {
            player.sendMessage(ChatColor.RED + "Quête introuvable: " + questKey);
            return true;
        }
        
        questManager.startQuest(player, quest.getQuestId());
        return true;
    }
    
    /**
     * Afficher les infos d'une quête
     */
    private boolean handleInfo(Player player, String questKey) {
        Quest quest = questManager.getQuestByKey(questKey);
        
        if (quest == null) {
            player.sendMessage(ChatColor.RED + "Quête introuvable: " + questKey);
            return true;
        }
        
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "═══════════════════════════════");
        player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + quest.getQuestName());
        player.sendMessage("");
        player.sendMessage(ChatColor.GRAY + quest.getDescription());
        player.sendMessage("");
        player.sendMessage(ChatColor.WHITE + "Type: " + ChatColor.AQUA + quest.getQuestType().getDisplayName());
        player.sendMessage(ChatColor.WHITE + "Niveau requis: " + ChatColor.YELLOW + quest.getRequiredLevel());
        player.sendMessage("");
        player.sendMessage(ChatColor.YELLOW + "Objectifs:");
        
        for (QuestObjective objective : quest.getObjectives()) {
            player.sendMessage(ChatColor.WHITE + "  • " + objective.getDescription());
        }
        
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "Récompenses:");
        player.sendMessage(ChatColor.AQUA + "  +" + quest.getRewardXP() + " XP");
        player.sendMessage(ChatColor.YELLOW + "  +" + quest.getRewardZines() + " Zines");
        player.sendMessage(ChatColor.GOLD + "═══════════════════════════════");
        player.sendMessage("");
        
        return true;
    }
    
    /**
     * Afficher la progression détaillée
     */
    private boolean handleProgress(Player player) {
        List<PlayerQuestProgress> progressList = questManager.getPlayerQuests(player);
        
        List<PlayerQuestProgress> inProgress = progressList.stream()
            .filter(p -> p.getStatus() == PlayerQuestProgress.QuestStatus.IN_PROGRESS)
            .toList();
        
        if (inProgress.isEmpty()) {
            player.sendMessage(ChatColor.RED + "Aucune quête en cours!");
            return true;
        }
        
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "═══════ PROGRESSION DÉTAILLÉE ═══════");
        
        for (PlayerQuestProgress progress : inProgress) {
            Quest quest = questManager.getQuest(progress.getQuestId());
            
            if (quest != null) {
                player.sendMessage("");
                player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + quest.getQuestName());
                
                for (QuestObjective objective : quest.getObjectives()) {
                    int current = progress.getProgress(objective.getObjectiveKey());
                    int target = objective.getTargetAmount();
                    String progressBar = createProgressBar(current, target, 20);
                    boolean completed = current >= target;
                    
                    String icon = completed ? ChatColor.GREEN + "✔" : ChatColor.GRAY + "○";
                    
                    player.sendMessage(icon + ChatColor.WHITE + " " + objective.getDescription());
                    player.sendMessage("  " + progressBar + " " + ChatColor.YELLOW + current + "/" + target);
                }
            }
        }
        
        player.sendMessage(ChatColor.GOLD + "═══════════════════════════════════");
        player.sendMessage("");
        
        return true;
    }
    
    /**
     * Terminer une quête
     */
    private boolean handleComplete(Player player, String questKey) {
        Quest quest = questManager.getQuestByKey(questKey);
        
        if (quest == null) {
            player.sendMessage(ChatColor.RED + "Quête introuvable: " + questKey);
            return true;
        }
        
        questManager.completeQuest(player, quest.getQuestId());
        return true;
    }
    
    /**
     * Obtenir la couleur selon le statut
     */
    private String getStatusColor(PlayerQuestProgress.QuestStatus status) {
        switch (status) {
            case IN_PROGRESS:
                return ChatColor.YELLOW.toString();
            case COMPLETED:
                return ChatColor.GREEN.toString();
            case FAILED:
                return ChatColor.RED.toString();
            case TURNED_IN:
                return ChatColor.DARK_GREEN.toString();
            default:
                return ChatColor.GRAY.toString();
        }
    }
    
    /**
     * Créer une barre de progression visuelle
     */
    private String createProgressBar(int current, int target, int barLength) {
        if (target == 0) return ChatColor.GREEN + "█".repeat(barLength);
        
        int filled = Math.min((int) ((double) current / target * barLength), barLength);
        int empty = barLength - filled;
        
        return ChatColor.GREEN + "█".repeat(filled) + ChatColor.GRAY + "█".repeat(empty);
    }
}
