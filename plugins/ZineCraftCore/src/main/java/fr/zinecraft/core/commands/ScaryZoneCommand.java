package fr.zinecraft.core.commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import fr.zinecraft.core.ZineCraftCore;

/**
 * Commande pour créer une zone effrayante
 * 
 * @author Otmane & Adam
 */
public class ScaryZoneCommand implements CommandExecutor {
    
    private final ZineCraftCore plugin;
    private BukkitRunnable scaryTask;
    private boolean isActive = false;
    
    public ScaryZoneCommand() {
        this.plugin = ZineCraftCore.getInstance();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Cette commande est réservée aux joueurs !");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length == 0) {
            player.sendMessage("");
            player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "👻 ZONE EFFRAYANTE 👻");
            player.sendMessage("");
            player.sendMessage(ChatColor.GRAY + "  /scary on " + ChatColor.WHITE + "- Activer (position actuelle)");
            player.sendMessage(ChatColor.GRAY + "  /scary off " + ChatColor.WHITE + "- Désactiver");
            player.sendMessage("");
            return true;
        }
        
        if (args[0].equalsIgnoreCase("on")) {
            activateScaryZone(player);
        } else if (args[0].equalsIgnoreCase("off")) {
            deactivateScaryZone(player);
        }
        
        return true;
    }
    
    /**
     * Activer la zone effrayante
     */
    private void activateScaryZone(Player player) {
        if (isActive) {
            player.sendMessage(ChatColor.RED + "La zone effrayante est déjà active!");
            return;
        }
        
        Location center = new Location(player.getWorld(), 132, 100, -17);
        World world = center.getWorld();
        
        // Configuration initiale
        world.setTime(18000); // Nuit
        world.setStorm(true); // Orage
        world.setThundering(true);
        world.setWeatherDuration(999999);
        
        isActive = true;
        
        player.sendMessage("");
        player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "👻 ZONE EFFRAYANTE ACTIVÉE! 👻");
        player.sendMessage(ChatColor.GRAY + "Coordonnées: 132, 100, -17");
        player.sendMessage("");
        
        // Effets continus
        scaryTask = new BukkitRunnable() {
            int tick = 0;
            
            @Override
            public void run() {
                if (!isActive) {
                    cancel();
                    return;
                }
                
                tick++;
                
                // Maintenir la nuit
                if (world.getTime() < 13000 || world.getTime() > 23000) {
                    world.setTime(18000);
                }
                
                // Effets toutes les 2 secondes
                if (tick % 40 == 0) {
                    // Foudres aléatoires autour de la zone
                    if (Math.random() < 0.7) {
                        Location strikeLoc = center.clone().add(
                            (Math.random() - 0.5) * 30,
                            0,
                            (Math.random() - 0.5) * 30
                        );
                        world.strikeLightningEffect(strikeLoc);
                    }
                    
                    // Particules de fumée noire
                    world.spawnParticle(Particle.SMOKE_LARGE, center, 50, 15, 10, 15, 0.1);
                    world.spawnParticle(Particle.SOUL, center, 30, 15, 10, 15, 0.05);
                    world.spawnParticle(Particle.ASH, center, 40, 15, 10, 15, 0.1);
                }
                
                // Sons effrayants toutes les 5 secondes
                if (tick % 100 == 0) {
                    Sound[] scarySounds = {
                        Sound.ENTITY_WITHER_AMBIENT,
                        Sound.ENTITY_ENDER_DRAGON_GROWL,
                        Sound.ENTITY_GHAST_SCREAM,
                        Sound.ENTITY_ZOMBIE_AMBIENT,
                        Sound.AMBIENT_SOUL_SAND_VALLEY_MOOD,
                        Sound.AMBIENT_BASALT_DELTAS_MOOD
                    };
                    
                    Sound randomSound = scarySounds[(int) (Math.random() * scarySounds.length)];
                    world.playSound(center, randomSound, 2.0f, 0.5f);
                }
                
                // Bruits de fond constants
                if (tick % 20 == 0) { // Toutes les secondes
                    world.playSound(center, Sound.AMBIENT_CAVE, 1.0f, 0.8f);
                }
                
                // Particules rouges sang
                if (tick % 10 == 0) {
                    world.spawnParticle(Particle.REDSTONE, center, 20, 15, 10, 15, 0, 
                        new Particle.DustOptions(Color.fromRGB(139, 0, 0), 2.0f));
                }
                
                // Brouillard de particules
                if (tick % 5 == 0) {
                    world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, center, 10, 15, 5, 15, 0.01);
                }
                
                // Sons aléatoires de pas/respiration
                if (tick % 60 == 0 && Math.random() < 0.5) {
                    world.playSound(center, Sound.ENTITY_PLAYER_BREATH, 1.5f, 0.6f);
                }
                
                // Effet de portail du Nether
                if (tick % 30 == 0) {
                    world.spawnParticle(Particle.PORTAL, center, 30, 10, 10, 10, 0.5);
                }
            }
        };
        
        scaryTask.runTaskTimer(plugin, 0L, 1L);
    }
    
    /**
     * Désactiver la zone effrayante
     */
    private void deactivateScaryZone(Player player) {
        if (!isActive) {
            player.sendMessage(ChatColor.RED + "La zone effrayante n'est pas active!");
            return;
        }
        
        isActive = false;
        
        if (scaryTask != null) {
            scaryTask.cancel();
        }
        
        // Remettre le temps normal
        World world = player.getWorld();
        world.setStorm(false);
        world.setThundering(false);
        world.setTime(6000); // Midi
        
        player.sendMessage("");
        player.sendMessage(ChatColor.GREEN + "✓ Zone effrayante désactivée");
        player.sendMessage("");
    }
}
