package fr.zinecraft.core.listeners;

import fr.zinecraft.core.ZineCraftCore;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Listener pour gérer l'entrée/sortie du Désert Mortel
 */
public class DesertZoneListener implements Listener {

    private final ZineCraftCore plugin;
    
    private static final int DESERT_CENTER_X = -500;
    private static final int DESERT_CENTER_Z = 500;
    private static final int DESERT_RADIUS = 150;

    public DesertZoneListener(ZineCraftCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();

        if (to == null) return;

        boolean wasInDesert = isInDesertZone(from);
        boolean nowInDesert = isInDesertZone(to);

        // Entrée dans le désert
        if (!wasInDesert && nowInDesert) {
            onEnterDesert(player);
        }
        
        // Sortie du désert
        else if (wasInDesert && !nowInDesert) {
            onLeaveDesert(player);
        }
    }

    /**
     * Vérifier si une location est dans le désert
     */
    private boolean isInDesertZone(Location loc) {
        int x = loc.getBlockX();
        int z = loc.getBlockZ();
        
        int dx = x - DESERT_CENTER_X;
        int dz = z - DESERT_CENTER_Z;
        
        return Math.sqrt(dx * dx + dz * dz) <= DESERT_RADIUS;
    }

    /**
     * Quand un joueur entre dans le désert
     */
    private void onEnterDesert(Player player) {
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "═══════════════════════════════════");
        player.sendMessage(ChatColor.RED + "⚠ DÉSERT MORTEL ⚠");
        player.sendMessage(ChatColor.YELLOW + "Vous entrez dans une zone hostile!");
        player.sendMessage(ChatColor.GRAY + "• Tempête de sable active");
        player.sendMessage(ChatColor.GRAY + "• Ralentissement permanent");
        player.sendMessage(ChatColor.GRAY + "• Dégâts périodiques");
        player.sendMessage(ChatColor.GRAY + "• Niveau recommandé: 30-40");
        player.sendMessage(ChatColor.GOLD + "═══════════════════════════════════");
        player.sendMessage("");
        
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 0.5f, 0.8f);
        player.sendTitle(
            ChatColor.RED + "⚠ DÉSERT MORTEL",
            ChatColor.YELLOW + "Zone hostile - Niveau 30+",
            10, 70, 20
        );
    }

    /**
     * Quand un joueur sort du désert
     */
    private void onLeaveDesert(Player player) {
        player.sendMessage(ChatColor.GREEN + "✔ Vous quittez le Désert Mortel");
        player.sendMessage(ChatColor.GRAY + "La tempête de sable s'apaise...");
        
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 1.2f);
    }
}
