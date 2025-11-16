package fr.zinecraft.core.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Commande pour afficher le menu de sélection de combat
 * 
 * @author Otmane & Adam
 */
public class CombatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        
        Player player = (Player) sender;
        
        // Afficher le menu de sélection
        player.sendMessage("");
        player.sendMessage(Component.text("═══════════════════════════", NamedTextColor.GRAY, TextDecoration.STRIKETHROUGH));
        player.sendMessage(Component.text("    ⚔ MODE DE COMBAT ⚔", NamedTextColor.GOLD, TextDecoration.BOLD));
        player.sendMessage(Component.text("═══════════════════════════", NamedTextColor.GRAY, TextDecoration.STRIKETHROUGH));
        player.sendMessage("");
        
        // Bouton 1v1
        Component button1v1 = Component.text("   [1v1]   ", NamedTextColor.RED, TextDecoration.BOLD)
                .clickEvent(ClickEvent.runCommand("/combat1v1"));
        
        // Bouton 2v2
        Component button2v2 = Component.text("   [2v2]   ", NamedTextColor.DARK_RED, TextDecoration.BOLD)
                .clickEvent(ClickEvent.runCommand("/combat2v2"));
        
        player.sendMessage(
            Component.text("  ➤ ", NamedTextColor.GRAY)
                .append(button1v1)
                .append(Component.text("  ", NamedTextColor.GRAY))
                .append(button2v2)
        );
        
        player.sendMessage("");
        player.sendMessage(Component.text("Clique sur un mode pour rejoindre!", NamedTextColor.YELLOW));
        player.sendMessage(Component.text("═══════════════════════════", NamedTextColor.GRAY, TextDecoration.STRIKETHROUGH));
        player.sendMessage("");
        
        return true;
    }
}
