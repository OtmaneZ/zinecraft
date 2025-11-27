package fr.zinecraft.core.commands;

import fr.zinecraft.core.ZineCraftCore;
import fr.zinecraft.core.rpg.LevelManager;
import fr.zinecraft.core.rpg.PlayerManager;
import fr.zinecraft.core.rpg.RPGPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Commande pour voir son niveau et ses stats
 * 
 * @author Otmane & Copilot
 */
public class StatsCommand implements CommandExecutor {
    
    private final ZineCraftCore plugin;
    private final PlayerManager playerManager;
    private final LevelManager levelManager;
    
    public StatsCommand(PlayerManager playerManager, LevelManager levelManager) {
        this.plugin = ZineCraftCore.getInstance();
        this.playerManager = playerManager;
        this.levelManager = levelManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Cette commande est réservée aux joueurs !");
            return true;
        }
        
        Player player = (Player) sender;
        RPGPlayer rpgPlayer = playerManager.getPlayer(player);
        
        if (rpgPlayer == null) {
            player.sendMessage(ChatColor.RED + "Erreur: Profil RPG introuvable!");
            return true;
        }
        
        if (!rpgPlayer.hasChosenClass()) {
            player.sendMessage(ChatColor.RED + "Vous devez d'abord choisir une classe!");
            player.sendMessage(ChatColor.YELLOW + "Tapez /class choose");
            return true;
        }
        
        // Afficher les stats
        showStats(player, rpgPlayer);
        
        return true;
    }
    
    /**
     * Afficher les statistiques du joueur
     */
    private void showStats(Player player, RPGPlayer rpgPlayer) {
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "📊 VOS STATISTIQUES 📊");
        player.sendMessage("");
        
        // Classe et niveau
        player.sendMessage(ChatColor.GREEN + "Classe: " + rpgPlayer.getClassType().getIcon() + " " + 
                          ChatColor.YELLOW + rpgPlayer.getClassType().getDisplayName());
        player.sendMessage(ChatColor.GREEN + "Niveau: " + ChatColor.YELLOW + ChatColor.BOLD + rpgPlayer.getLevel());
        
        // Progression XP
        int currentXP = rpgPlayer.getExperience();
        int requiredXP = rpgPlayer.getRequiredExperience();
        double progress = rpgPlayer.getProgressPercentage();
        
        player.sendMessage(ChatColor.AQUA + "XP: " + ChatColor.WHITE + currentXP + "/" + requiredXP + 
                          ChatColor.GRAY + " (" + String.format("%.1f", progress) + "%)");
        
        // Barre de progression visuelle
        String progressBar = createProgressBar(progress);
        player.sendMessage(progressBar);
        
        player.sendMessage("");
        
        // Argent et points
        player.sendMessage(ChatColor.GOLD + "💰 Zines: " + ChatColor.WHITE + rpgPlayer.getZines());
        player.sendMessage(ChatColor.LIGHT_PURPLE + "✨ Points de Compétence: " + ChatColor.WHITE + rpgPlayer.getSkillPoints());
        
        player.sendMessage("");
        
        // Multiplicateur XP
        double xpMultiplier = levelManager.getClassXPMultiplier(rpgPlayer.getClassType());
        if (xpMultiplier > 1.0) {
            player.sendMessage(ChatColor.YELLOW + "⚡ Bonus XP: " + ChatColor.GREEN + "+" + 
                             (int)((xpMultiplier - 1.0) * 100) + "% " + ChatColor.GRAY + "(Classe premium)");
            player.sendMessage("");
        }
        
        // Statistiques de jeu
        player.sendMessage(ChatColor.GRAY + "--- Statistiques ---");
        player.sendMessage(ChatColor.WHITE + "⚔ Mobs tués: " + ChatColor.YELLOW + rpgPlayer.getMobsKilled());
        player.sendMessage(ChatColor.WHITE + "👤 Joueurs tués: " + ChatColor.YELLOW + rpgPlayer.getPlayersKilled());
        player.sendMessage(ChatColor.WHITE + "💀 Morts: " + ChatColor.YELLOW + rpgPlayer.getDeaths());
        player.sendMessage(ChatColor.WHITE + "⛏ Blocs minés: " + ChatColor.YELLOW + rpgPlayer.getBlocksMined());
        player.sendMessage(ChatColor.WHITE + "🔨 Items craftés: " + ChatColor.YELLOW + rpgPlayer.getItemsCrafted());
        player.sendMessage(ChatColor.WHITE + "👹 Boss vaincus: " + ChatColor.YELLOW + rpgPlayer.getBossesDefeated());
        player.sendMessage(ChatColor.WHITE + "📜 Quêtes complétées: " + ChatColor.YELLOW + rpgPlayer.getQuestsCompleted());
        
        // Ratio K/D
        if (rpgPlayer.getDeaths() > 0) {
            double kd = (double) (rpgPlayer.getMobsKilled() + rpgPlayer.getPlayersKilled()) / rpgPlayer.getDeaths();
            player.sendMessage(ChatColor.WHITE + "📈 Ratio K/D: " + ChatColor.YELLOW + String.format("%.2f", kd));
        }
        
        player.sendMessage("");
        player.sendMessage(ChatColor.GRAY + "Tapez /class skills pour voir vos compétences");
        player.sendMessage("");
    }
    
    /**
     * Créer une barre de progression visuelle
     */
    private String createProgressBar(double percentage) {
        int totalBars = 20;
        int filledBars = (int) (totalBars * percentage / 100.0);
        
        StringBuilder bar = new StringBuilder(ChatColor.DARK_GRAY + "[");
        
        for (int i = 0; i < totalBars; i++) {
            if (i < filledBars) {
                bar.append(ChatColor.GREEN + "█");
            } else {
                bar.append(ChatColor.GRAY + "█");
            }
        }
        
        bar.append(ChatColor.DARK_GRAY + "]");
        
        return bar.toString();
    }
}
