package fr.zinecraft.core.economy;

import fr.zinecraft.core.ZineCraftCore;
import fr.zinecraft.core.rpg.PlayerManager;
import fr.zinecraft.core.rpg.RPGPlayer;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 * Gestionnaire de l'économie avec la monnaie Zines
 * 
 * @author Otmane & Copilot
 */
public class EconomyManager {
    
    private final ZineCraftCore plugin;
    private final PlayerManager playerManager;
    
    public EconomyManager(ZineCraftCore plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
    }
    
    // ==================== Transactions ====================
    
    /**
     * Ajouter des Zines à un joueur
     */
    public boolean addZines(Player player, int amount, String reason) {
        RPGPlayer rpgPlayer = playerManager.getPlayer(player);
        
        if (rpgPlayer == null) {
            return false;
        }
        
        int oldBalance = rpgPlayer.getZines();
        rpgPlayer.addZines(amount);
        int newBalance = rpgPlayer.getZines();
        
        // Log transaction
        logTransaction(rpgPlayer, "ADD", amount, newBalance, reason);
        
        // Sauvegarder
        playerManager.savePlayer(rpgPlayer);
        
        // Notification
        player.sendMessage(ChatColor.GOLD + "+ " + amount + " Zines " + 
                          ChatColor.GRAY + "(" + reason + ")");
        player.sendMessage(ChatColor.YELLOW + "Solde: " + newBalance + " Zines");
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        
        return true;
    }
    
    /**
     * Retirer des Zines à un joueur
     */
    public boolean removeZines(Player player, int amount, String reason) {
        RPGPlayer rpgPlayer = playerManager.getPlayer(player);
        
        if (rpgPlayer == null) {
            return false;
        }
        
        int oldBalance = rpgPlayer.getZines();
        boolean success = rpgPlayer.removeZines(amount);
        
        if (!success) {
            player.sendMessage(ChatColor.RED + "Vous n'avez pas assez de Zines!");
            player.sendMessage(ChatColor.YELLOW + "Requis: " + amount + " Zines");
            player.sendMessage(ChatColor.YELLOW + "Solde: " + oldBalance + " Zines");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return false;
        }
        
        int newBalance = rpgPlayer.getZines();
        
        // Log transaction
        logTransaction(rpgPlayer, "REMOVE", -amount, newBalance, reason);
        
        // Sauvegarder
        playerManager.savePlayer(rpgPlayer);
        
        // Notification
        player.sendMessage(ChatColor.RED + "- " + amount + " Zines " + 
                          ChatColor.GRAY + "(" + reason + ")");
        player.sendMessage(ChatColor.YELLOW + "Solde: " + newBalance + " Zines");
        player.playSound(player.getLocation(), Sound.BLOCK_CHAIN_BREAK, 1.0f, 1.0f);
        
        return true;
    }
    
    /**
     * Transférer des Zines entre joueurs
     */
    public boolean transferZines(Player sender, Player receiver, int amount) {
        RPGPlayer senderRPG = playerManager.getPlayer(sender);
        RPGPlayer receiverRPG = playerManager.getPlayer(receiver);
        
        if (senderRPG == null || receiverRPG == null) {
            sender.sendMessage(ChatColor.RED + "Erreur: Profil RPG introuvable!");
            return false;
        }
        
        // Vérifier le solde
        if (senderRPG.getZines() < amount) {
            sender.sendMessage(ChatColor.RED + "Vous n'avez pas assez de Zines!");
            return false;
        }
        
        // Minimum 1 Zine
        if (amount < 1) {
            sender.sendMessage(ChatColor.RED + "Montant invalide!");
            return false;
        }
        
        // Retirer au sender
        senderRPG.removeZines(amount);
        logTransaction(senderRPG, "TRANSFER_OUT", -amount, senderRPG.getZines(), 
                      "Envoyé à " + receiver.getName());
        
        // Ajouter au receiver
        receiverRPG.addZines(amount);
        logTransaction(receiverRPG, "TRANSFER_IN", amount, receiverRPG.getZines(), 
                      "Reçu de " + sender.getName());
        
        // Sauvegarder
        playerManager.savePlayer(senderRPG);
        playerManager.savePlayer(receiverRPG);
        
        // Notifications
        sender.sendMessage(ChatColor.GREEN + "✔ Vous avez envoyé " + amount + " Zines à " + 
                          ChatColor.YELLOW + receiver.getName());
        sender.sendMessage(ChatColor.GRAY + "Nouveau solde: " + senderRPG.getZines() + " Zines");
        
        receiver.sendMessage(ChatColor.GREEN + "✔ Vous avez reçu " + amount + " Zines de " + 
                            ChatColor.YELLOW + sender.getName());
        receiver.sendMessage(ChatColor.GRAY + "Nouveau solde: " + receiverRPG.getZines() + " Zines");
        
        // Sons
        sender.playSound(sender.getLocation(), Sound.BLOCK_CHAIN_BREAK, 1.0f, 1.0f);
        receiver.playSound(receiver.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.5f);
        
        return true;
    }
    
    /**
     * Obtenir le solde d'un joueur
     */
    public int getBalance(Player player) {
        RPGPlayer rpgPlayer = playerManager.getPlayer(player);
        return rpgPlayer != null ? rpgPlayer.getZines() : 0;
    }
    
    /**
     * Vérifier si un joueur a assez de Zines
     */
    public boolean hasAmount(Player player, int amount) {
        return getBalance(player) >= amount;
    }
    
    /**
     * Définir le solde d'un joueur (admin)
     */
    public void setBalance(Player player, int amount) {
        RPGPlayer rpgPlayer = playerManager.getPlayer(player);
        
        if (rpgPlayer != null) {
            int oldBalance = rpgPlayer.getZines();
            rpgPlayer.setZines(amount);
            
            logTransaction(rpgPlayer, "SET", amount - oldBalance, amount, "Admin set balance");
            
            playerManager.savePlayer(rpgPlayer);
        }
    }
    
    // ==================== Logging ====================
    
    /**
     * Logger une transaction dans la BDD
     */
    private void logTransaction(RPGPlayer rpgPlayer, String type, int amount, int balanceAfter, String description) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                // Note: On réutilise la connexion du PlayerManager
                // TODO: Implémenter avec une connexion propre si nécessaire
                plugin.getLogger().info(String.format("Transaction: %s | %s | %d Zines | Balance: %d | %s",
                    rpgPlayer.getPlayerName(), type, amount, balanceAfter, description));
                
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Erreur log transaction", e);
            }
        });
    }
    
    // ==================== Récompenses automatiques ====================
    
    /**
     * Récompense journalière
     */
    public void grantDailyReward(Player player) {
        RPGPlayer rpgPlayer = playerManager.getPlayer(player);
        
        if (rpgPlayer == null) {
            return;
        }
        
        // Récompense basée sur le niveau
        int reward = 50 + (rpgPlayer.getLevel() * 10);
        
        // Bonus pour classes premium
        if (rpgPlayer.getClassType() != null && rpgPlayer.getClassType().isPremium()) {
            reward = (int) (reward * 1.5);
        }
        
        addZines(player, reward, "Récompense journalière");
        
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "🎁 RÉCOMPENSE JOURNALIÈRE 🎁");
        player.sendMessage(ChatColor.GREEN + "Vous avez reçu " + ChatColor.YELLOW + reward + " Zines!");
        player.sendMessage("");
    }
    
    /**
     * Récompense pour première connexion
     */
    public void grantWelcomeBonus(Player player) {
        addZines(player, 100, "Bonus de bienvenue");
        
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "🎉 BIENVENUE SUR ZINECRAFT ! 🎉");
        player.sendMessage(ChatColor.GREEN + "Vous avez reçu " + ChatColor.YELLOW + "100 Zines" + 
                          ChatColor.GREEN + " de démarrage !");
        player.sendMessage("");
    }
}
