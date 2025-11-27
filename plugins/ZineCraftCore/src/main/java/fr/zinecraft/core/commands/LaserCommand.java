package fr.zinecraft.core.commands;

import fr.zinecraft.core.ZineCraftCore;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * Commande /laser pour créer des lasers défensifs
 * 
 * @author Adam
 */
public class LaserCommand implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Cette commande est réservée aux joueurs !");
            return true;
        }
        
        Player player = (Player) sender;
        Location playerLoc = player.getLocation();
        
        // Message et effets
        player.sendMessage(ChatColor.RED + "⚡ Système de lasers activé!");
        player.getWorld().playSound(playerLoc, Sound.BLOCK_BEACON_ACTIVATE, 1.5f, 2.0f);
        
        // Créer 8 lasers autour du joueur
        int numLasers = 8;
        double radius = 4.0; // Rayon du cercle de lasers
        double height = 8.0; // Hauteur des lasers au-dessus du joueur
        
        for (int i = 0; i < numLasers; i++) {
            double angle = (2 * Math.PI * i) / numLasers;
            double x = playerLoc.getX() + radius * Math.cos(angle);
            double z = playerLoc.getZ() + radius * Math.sin(angle);
            double y = playerLoc.getY() + height;
            
            Location laserTop = new Location(playerLoc.getWorld(), x, y, z);
            
            // Créer l'animation du laser
            createLaser(player, laserTop, i * 2); // Décalage pour effet de vague
        }
        
        return true;
    }
    
    /**
     * Créer un laser qui tire vers le bas
     */
    private void createLaser(Player owner, Location startLoc, int delayTicks) {
        new BukkitRunnable() {
            int ticks = 0;
            final int maxTicks = 80; // 4 secondes (80 ticks)
            
            @Override
            public void run() {
                ticks++;
                
                if (ticks > maxTicks) {
                    // Effet final
                    startLoc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, 
                        startLoc, 1, 0, 0, 0, 0);
                    cancel();
                    return;
                }
                
                // Particules au sommet du laser
                startLoc.getWorld().spawnParticle(Particle.REDSTONE, 
                    startLoc, 5, 0.2, 0.2, 0.2, 0);
                
                // Tirer le laser vers le bas
                Location currentLoc = startLoc.clone();
                
                for (int i = 0; i < 10; i++) { // 10 blocs de hauteur
                    currentLoc.subtract(0, 1, 0);
                    
                    // Particules rouges pour le laser
                    currentLoc.getWorld().spawnParticle(Particle.FLAME, 
                        currentLoc, 3, 0.1, 0.1, 0.1, 0);
                    currentLoc.getWorld().spawnParticle(Particle.REDSTONE, 
                        currentLoc, 2, 0.05, 0.05, 0.05, 0);
                    
                    // Vérifier les entités touchées
                    for (Entity entity : currentLoc.getWorld().getNearbyEntities(currentLoc, 0.5, 0.5, 0.5)) {
                        if (entity instanceof LivingEntity && entity != owner) {
                            LivingEntity target = (LivingEntity) entity;
                            
                            // Tuer la cible
                            target.damage(1000); // Dégâts massifs pour tuer instantanément
                            target.setHealth(0);
                            
                            // Effets de mort
                            target.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, 
                                target.getLocation(), 5, 0.5, 0.5, 0.5, 0);
                            target.getWorld().playSound(target.getLocation(), 
                                Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1.0f, 1.0f);
                            
                            // Message
                            if (target instanceof Player) {
                                ((Player) target).sendMessage(ChatColor.RED + "⚡ Vous avez été désintégré par un laser!");
                            }
                            owner.sendMessage(ChatColor.GOLD + "⚡ " + target.getName() + " a été éliminé!");
                        }
                    }
                }
                
                // Son du laser toutes les 10 ticks
                if (ticks % 10 == 0) {
                    startLoc.getWorld().playSound(startLoc, Sound.BLOCK_BEACON_AMBIENT, 0.3f, 2.0f);
                }
            }
        }.runTaskTimer(ZineCraftCore.getInstance(), delayTicks, 1L);
    }
}
