package fr.zinecraft.core.commands;

import fr.zinecraft.core.ZineCraftCore;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Commande /mega pour obtenir 20 cœurs supplémentaires pendant 3 secondes
 * 
 * @author Adam
 */
public class MegaHealthCommand implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Cette commande est réservée aux joueurs !");
            return true;
        }
        
        Player player = (Player) sender;
        
        // Sauvegarder la santé max actuelle
        double originalMaxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        double originalHealth = player.getHealth();
        
        // Nouvelle santé max: +20 cœurs = +40 HP (1 cœur = 2 HP)
        double newMaxHealth = originalMaxHealth + 40.0;
        
        // Appliquer la méga santé
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(newMaxHealth);
        player.setHealth(newMaxHealth); // Heal complet
        
        // Effets visuels
        player.getWorld().spawnParticle(Particle.HEART, player.getLocation().add(0, 1, 0), 30, 0.5, 0.5, 0.5, 0.1);
        player.getWorld().spawnParticle(Particle.TOTEM, player.getLocation(), 50, 0.5, 1, 0.5, 0.1);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1.0f, 2.0f);
        
        // Message
        player.sendMessage(ChatColor.RED + "❤ " + ChatColor.BOLD + "MÉGA SANTÉ ACTIVÉE!");
        player.sendMessage(ChatColor.GOLD + "✨ +20 cœurs pendant 3 secondes!");
        
        // Animation pendant 3 secondes
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                ticks++;
                
                // Particules de cœurs
                if (ticks % 5 == 0) {
                    player.getWorld().spawnParticle(Particle.HEART, 
                        player.getLocation().add(0, 1, 0), 5, 0.3, 0.3, 0.3, 0);
                }
                
                // Après 3 secondes (60 ticks)
                if (ticks >= 60) {
                    // Restaurer la santé max originale
                    player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(originalMaxHealth);
                    
                    // Ajuster la santé actuelle si elle dépasse le nouveau max
                    if (player.getHealth() > originalMaxHealth) {
                        player.setHealth(originalMaxHealth);
                    }
                    
                    // Effets de fin
                    player.getWorld().spawnParticle(Particle.SMOKE_NORMAL, 
                        player.getLocation().add(0, 1, 0), 20, 0.3, 0.5, 0.3, 0.05);
                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 0.8f);
                    
                    player.sendMessage(ChatColor.GRAY + "💔 Méga santé terminée.");
                    
                    cancel();
                }
            }
        }.runTaskTimer(ZineCraftCore.getInstance(), 0L, 1L);
        
        return true;
    }
}
