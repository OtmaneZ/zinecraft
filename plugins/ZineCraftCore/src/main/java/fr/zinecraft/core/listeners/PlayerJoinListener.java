package fr.zinecraft.core.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Listener pour l'arrivée des joueurs
 * 
 * @author Otmane & Adam
 */
public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Message de bienvenue
        event.getPlayer().sendMessage("");
        event.getPlayer().sendMessage(Component.text("⚔ Bienvenue sur ZineCraft!", NamedTextColor.GOLD, TextDecoration.BOLD));
        event.getPlayer().sendMessage("");
        
        // Bouton cliquable "Combat"
        Component combatButton = Component.text("[Combat]", NamedTextColor.RED, TextDecoration.BOLD)
                .clickEvent(ClickEvent.runCommand("/combat"));
        
        event.getPlayer().sendMessage(
            Component.text("➤ Clique ici pour rejoindre la zone : ", NamedTextColor.GRAY)
                .append(combatButton)
        );
        event.getPlayer().sendMessage("");
    }
}
