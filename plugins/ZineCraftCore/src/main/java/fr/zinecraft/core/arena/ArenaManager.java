package fr.zinecraft.core.arena;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import fr.zinecraft.core.ZineCraftCore;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestionnaire d'arène de combat
 * 
 * @author Otmane & Adam
 */
public class ArenaManager {
    
    private final ZineCraftCore plugin;
    private final List<Player> waitingPlayers1v1;
    private final List<Player> waitingPlayers2v2;
    
    public ArenaManager(ZineCraftCore plugin) {
        this.plugin = plugin;
        this.waitingPlayers1v1 = new ArrayList<>();
        this.waitingPlayers2v2 = new ArrayList<>();
    }
    
    /**
     * Ajouter un joueur en file d'attente 1v1
     */
    public void joinQueue1v1(Player player) {
        waitingPlayers1v1.add(player);
        
        if (waitingPlayers1v1.size() >= 2) {
            // Démarrer un combat 1v1
            Player player1 = waitingPlayers1v1.remove(0);
            Player player2 = waitingPlayers1v1.remove(0);
            startArena(player1, player2);
        } else {
            player.sendMessage(ChatColor.YELLOW + "⏳ En attente d'un adversaire... (" + waitingPlayers1v1.size() + "/2)");
        }
    }
    
    /**
     * Ajouter un joueur en file d'attente 2v2
     */
    public void joinQueue2v2(Player player) {
        waitingPlayers2v2.add(player);
        
        if (waitingPlayers2v2.size() >= 4) {
            // Démarrer un combat 2v2
            Player p1 = waitingPlayers2v2.remove(0);
            Player p2 = waitingPlayers2v2.remove(0);
            Player p3 = waitingPlayers2v2.remove(0);
            Player p4 = waitingPlayers2v2.remove(0);
            startArena2v2(p1, p2, p3, p4);
        } else {
            player.sendMessage(ChatColor.YELLOW + "⏳ En attente de joueurs... (" + waitingPlayers2v2.size() + "/4)");
        }
    }
    
    /**
     * Démarrer une arène 1v1
     */
    private void startArena(Player player1, Player player2) {
        // Coordonnées de l'arène (très loin)
        Location arenaCenter = new Location(player1.getWorld(), 10000, 100, 10000);
        
        // Téléporter les joueurs dans le cube
        Location pos1 = arenaCenter.clone().add(0, 0, 0);
        Location pos2 = arenaCenter.clone().add(10, 0, 0);
        
        player1.teleport(pos1);
        player2.teleport(pos2);
        
        // Créer le cube vert autour d'eux
        createGreenCube(arenaCenter);
        
        // Messages
        player1.sendMessage(ChatColor.GREEN + "⚔ Combat 1v1 trouvé!");
        player2.sendMessage(ChatColor.GREEN + "⚔ Combat 1v1 trouvé!");
        
        // Démarrer le compte à rebours
        startCountdown(arenaCenter, player1, player2);
    }
    
    /**
     * Démarrer une arène 2v2
     */
    private void startArena2v2(Player p1, Player p2, Player p3, Player p4) {
        // Coordonnées de l'arène (très loin)
        Location arenaCenter = new Location(p1.getWorld(), 20000, 100, 20000);
        
        // Téléporter les joueurs
        p1.teleport(arenaCenter.clone().add(-5, 0, 0));
        p2.teleport(arenaCenter.clone().add(-5, 0, 2));
        p3.teleport(arenaCenter.clone().add(15, 0, 0));
        p4.teleport(arenaCenter.clone().add(15, 0, 2));
        
        // Créer le cube vert
        createGreenCube(arenaCenter);
        
        // Messages
        for (Player p : new Player[]{p1, p2, p3, p4}) {
            p.sendMessage(ChatColor.GREEN + "⚔ Combat 2v2 trouvé!");
        }
        
        // Démarrer le compte à rebours
        startCountdown2v2(arenaCenter, p1, p2, p3, p4);
    }
    
    /**
     * Créer un cube vert autour de l'arène
     */
    private void createGreenCube(Location center) {
        World world = center.getWorld();
        int x = center.getBlockX();
        int y = center.getBlockY();
        int z = center.getBlockZ();
        
        // Créer les murs du cube (5x5x5)
        for (int dx = -2; dx <= 12; dx++) {
            for (int dy = -1; dy <= 4; dy++) {
                for (int dz = -2; dz <= 2; dz++) {
                    // Murs extérieurs seulement
                    if (dx == -2 || dx == 12 || dy == -1 || dy == 4 || dz == -2 || dz == 2) {
                        world.getBlockAt(x + dx, y + dy, z + dz).setType(Material.LIME_STAINED_GLASS);
                    }
                }
            }
        }
    }
    
    /**
     * Détruire le cube vert
     */
    private void destroyGreenCube(Location center) {
        World world = center.getWorld();
        int x = center.getBlockX();
        int y = center.getBlockY();
        int z = center.getBlockZ();
        
        // Détruire le cube
        for (int dx = -2; dx <= 12; dx++) {
            for (int dy = -1; dy <= 4; dy++) {
                for (int dz = -2; dz <= 2; dz++) {
                    if (dx == -2 || dx == 12 || dy == -1 || dy == 4 || dz == -2 || dz == 2) {
                        world.getBlockAt(x + dx, y + dy, z + dz).setType(Material.AIR);
                    }
                }
            }
        }
    }
    
    /**
     * Démarrer le compte à rebours 1v1
     */
    private void startCountdown(Location center, Player p1, Player p2) {
        new BukkitRunnable() {
            int countdown = 3;
            
            @Override
            public void run() {
                if (countdown > 0) {
                    // Afficher le compte à rebours
                    String message = ChatColor.YELLOW + "" + ChatColor.BOLD + countdown;
                    p1.sendTitle(message, "", 0, 20, 10);
                    p2.sendTitle(message, "", 0, 20, 10);
                    
                    // Son
                    p1.playSound(p1.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 1.0f);
                    p2.playSound(p2.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 1.0f);
                    
                    countdown--;
                } else {
                    // GO!
                    p1.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "GO!", "", 0, 20, 10);
                    p2.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "GO!", "", 0, 20, 10);
                    
                    p1.playSound(p1.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
                    p2.playSound(p2.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
                    
                    // Détruire le cube
                    destroyGreenCube(center);
                    
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // Toutes les secondes
    }
    
    /**
     * Démarrer le compte à rebours 2v2
     */
    private void startCountdown2v2(Location center, Player p1, Player p2, Player p3, Player p4) {
        Player[] players = {p1, p2, p3, p4};
        
        new BukkitRunnable() {
            int countdown = 3;
            
            @Override
            public void run() {
                if (countdown > 0) {
                    String message = ChatColor.YELLOW + "" + ChatColor.BOLD + countdown;
                    for (Player p : players) {
                        p.sendTitle(message, "", 0, 20, 10);
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 1.0f);
                    }
                    countdown--;
                } else {
                    for (Player p : players) {
                        p.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "GO!", "", 0, 20, 10);
                        p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
                    }
                    
                    destroyGreenCube(center);
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
}
