package fr.zinecraft.core.commands;

import fr.zinecraft.core.ZineCraftCore;
import fr.zinecraft.core.powers.PowerManager;
import fr.zinecraft.core.powers.PowerType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Commande pour utiliser des super-pouvoirs
 * 
 * @author Otmane & Adam
 */
public class PowerCommand implements CommandExecutor {
    
    private final PowerManager powerManager;
    
    public PowerCommand() {
        this.powerManager = ZineCraftCore.getInstance().getPowerManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Cette commande est réservée aux joueurs !");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length == 0) {
            sendHelp(player);
            return true;
        }
        
        if (args[0].equalsIgnoreCase("list")) {
            listPowers(player);
            return true;
        }
        
        // Utiliser un pouvoir
        PowerType power = getPowerFromString(args[0]);
        if (power == null) {
            player.sendMessage(ChatColor.RED + "Pouvoir inconnu! Utilise /power list");
            return true;
        }
        
        powerManager.usePower(player, power);
        return true;
    }
    
    /**
     * Afficher l'aide
     */
    private void sendHelp(Player player) {
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "═══ 🦸 SUPER-POUVOIRS 🦸 ═══");
        player.sendMessage("");
        player.sendMessage(ChatColor.YELLOW + "Commandes:");
        player.sendMessage(ChatColor.GRAY + "  /power list " + ChatColor.WHITE + "- Liste des pouvoirs");
        player.sendMessage(ChatColor.GRAY + "  /power <nom> " + ChatColor.WHITE + "- Utiliser un pouvoir");
        player.sendMessage("");
        player.sendMessage(ChatColor.GRAY + "💡 Exemples:");
        player.sendMessage(ChatColor.GRAY + "  /power speed");
        player.sendMessage(ChatColor.GRAY + "  /power fireball");
        player.sendMessage(ChatColor.GRAY + "  /power fly");
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "═════════════════════════");
        player.sendMessage("");
    }
    
    /**
     * Lister les pouvoirs
     */
    private void listPowers(Player player) {
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "═══ 🦸 SUPER-POUVOIRS DISPONIBLES 🦸 ═══");
        player.sendMessage("");
        
        player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "⚡ MOUVEMENT:");
        sendPowerInfo(player, PowerType.SUPER_SPEED, "speed");
        sendPowerInfo(player, PowerType.SUPER_JUMP, "jump");
        sendPowerInfo(player, PowerType.TELEPORT, "teleport");
        sendPowerInfo(player, PowerType.FLIGHT, "fly");
        player.sendMessage("");
        
        player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "🔥 ATTAQUE:");
        sendPowerInfo(player, PowerType.FIREBALL, "fireball");
        sendPowerInfo(player, PowerType.LIGHTNING_STRIKE, "lightning");
        sendPowerInfo(player, PowerType.TORNADO, "tornado");
        player.sendMessage("");
        
        player.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "🛡️ DÉFENSE:");
        sendPowerInfo(player, PowerType.SHIELD, "shield");
        sendPowerInfo(player, PowerType.INVISIBILITY, "invisibility");
        sendPowerInfo(player, PowerType.EARTH_WALL, "wall");
        player.sendMessage("");
        
        player.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "💚 SOUTIEN:");
        sendPowerInfo(player, PowerType.HEAL_AURA, "heal");
        sendPowerInfo(player, PowerType.FREEZE_ZONE, "freeze");
        player.sendMessage("");
        
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "═══════════════════════════════════");
        player.sendMessage("");
    }
    
    /**
     * Afficher info d'un pouvoir
     */
    private void sendPowerInfo(Player player, PowerType power, String command) {
        player.sendMessage(ChatColor.GRAY + "  /power " + command + " " + 
                          power.getColor() + "- " + power.getDisplayName());
        player.sendMessage(ChatColor.DARK_GRAY + "    " + power.getDescription());
        for (String info : power.getInfo()) {
            player.sendMessage(ChatColor.DARK_GRAY + "    " + info);
        }
    }
    
    /**
     * Obtenir le type de pouvoir depuis une string
     */
    private PowerType getPowerFromString(String str) {
        switch (str.toLowerCase()) {
            case "speed":
            case "vitesse":
                return PowerType.SUPER_SPEED;
                
            case "jump":
            case "saut":
                return PowerType.SUPER_JUMP;
                
            case "fireball":
            case "feu":
            case "fire":
                return PowerType.FIREBALL;
                
            case "freeze":
            case "glace":
            case "ice":
                return PowerType.FREEZE_ZONE;
                
            case "invisibility":
            case "invisible":
            case "invis":
                return PowerType.INVISIBILITY;
                
            case "tornado":
            case "tornade":
                return PowerType.TORNADO;
                
            case "lightning":
            case "eclair":
            case "thunder":
                return PowerType.LIGHTNING_STRIKE;
                
            case "shield":
            case "bouclier":
                return PowerType.SHIELD;
                
            case "teleport":
            case "tp":
                return PowerType.TELEPORT;
                
            case "heal":
            case "soin":
                return PowerType.HEAL_AURA;
                
            case "fly":
            case "flight":
            case "vol":
                return PowerType.FLIGHT;
                
            case "wall":
            case "mur":
            case "earth":
                return PowerType.EARTH_WALL;
                
            default:
                return null;
        }
    }
}
