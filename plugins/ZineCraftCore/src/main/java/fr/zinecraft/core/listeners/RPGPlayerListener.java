package fr.zinecraft.core.listeners;

import fr.zinecraft.core.ZineCraftCore;
import fr.zinecraft.core.rpg.PlayerManager;
import fr.zinecraft.core.rpg.RPGPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.ChatColor;

/**
 * Listener pour gérer les événements RPG des joueurs
 * 
 * @author Otmane & Copilot
 */
public class RPGPlayerListener implements Listener {
    
    private final ZineCraftCore plugin;
    private final PlayerManager playerManager;
    
    public RPGPlayerListener() {
        this.plugin = ZineCraftCore.getInstance();
        this.playerManager = plugin.getPlayerManager();
    }
    
    /**
     * Charger le joueur RPG à la connexion
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Charger de manière asynchrone pour ne pas bloquer
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            RPGPlayer rpgPlayer = playerManager.loadPlayer(player);
            
            if (rpgPlayer != null) {
                // Message de bienvenue
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    if (!rpgPlayer.hasChosenClass()) {
                        player.sendMessage("");
                        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "🎮 BIENVENUE SUR ZINECRAFT! 🎮");
                        player.sendMessage("");
                        player.sendMessage(ChatColor.YELLOW + "Ceci est un serveur RPG unique!");
                        player.sendMessage(ChatColor.GRAY + "Vous devez choisir une classe pour commencer.");
                        player.sendMessage("");
                        player.sendMessage(ChatColor.GREEN + "➜ Parlez au " + ChatColor.GOLD + "Maître des Classes" + ChatColor.GREEN + " au spawn!");
                        player.sendMessage("");
                    } else {
                        player.sendMessage("");
                        player.sendMessage(ChatColor.GREEN + "✔ Bienvenue " + rpgPlayer.getClassType().getIcon() + " " + ChatColor.GOLD + rpgPlayer.getClassType().getDisplayName() + ChatColor.GREEN + " !");
                        player.sendMessage(ChatColor.GRAY + "Level " + rpgPlayer.getLevel() + " | " + rpgPlayer.getZines() + " Zines");
                        player.sendMessage("");
                    }
                });
            }
        });
    }
    
    /**
     * Sauvegarder et décharger le joueur à la déconnexion
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        // Sauvegarder de manière asynchrone
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            playerManager.unloadPlayer(player.getUniqueId());
        });
    }
}
