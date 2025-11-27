package fr.zinecraft.core.commands;

import fr.zinecraft.core.ZineCraftCore;
import fr.zinecraft.core.weapons.WeaponManager;
import fr.zinecraft.core.weapons.WeaponType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Commande pour obtenir des armes légendaires
 * 
 * @author Otmane & Adam
 */
public class WeaponCommand implements CommandExecutor {
    
    private final WeaponManager weaponManager;
    
    public WeaponCommand() {
        this.weaponManager = ZineCraftCore.getInstance().getWeaponManager();
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
            listWeapons(player);
            return true;
        }
        
        if (args[0].equalsIgnoreCase("give")) {
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /weapon give <nom>");
                return true;
            }
            giveWeapon(player, args[1]);
            return true;
        }
        
        sendHelp(player);
        return true;
    }
    
    /**
     * Afficher l'aide
     */
    private void sendHelp(Player player) {
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "═══ ⚔️ ARMES LÉGENDAIRES ⚔️ ═══");
        player.sendMessage("");
        player.sendMessage(ChatColor.YELLOW + "Commandes:");
        player.sendMessage(ChatColor.GRAY + "  /weapon list " + ChatColor.WHITE + "- Liste des armes");
        player.sendMessage(ChatColor.GRAY + "  /weapon give <nom> " + ChatColor.WHITE + "- Obtenir une arme");
        player.sendMessage("");
        player.sendMessage(ChatColor.GRAY + "💡 Clic droit pour utiliser le pouvoir spécial!");
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "══════════════════════════════");
        player.sendMessage("");
    }
    
    /**
     * Lister les armes
     */
    private void listWeapons(Player player) {
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "═══ ⚔️ ARMES DISPONIBLES ⚔️ ═══");
        player.sendMessage("");
        
        player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "⚔️ ÉPÉES:");
        player.sendMessage(ChatColor.GRAY + "  /weapon give excalibur " + ChatColor.WHITE + "- ⚡ Excalibur");
        player.sendMessage(ChatColor.GRAY + "  /weapon give fire " + ChatColor.WHITE + "- 🔥 Lame de Feu");
        player.sendMessage(ChatColor.GRAY + "  /weapon give ice " + ChatColor.WHITE + "- ❄️ Épée de Glace");
        player.sendMessage(ChatColor.GRAY + "  /weapon give dragon " + ChatColor.WHITE + "- 🐉 Tueuse de Dragons");
        player.sendMessage(ChatColor.GRAY + "  /weapon give holy " + ChatColor.WHITE + "- ✨ Épée Sacrée");
        player.sendMessage(ChatColor.GRAY + "  /weapon give poison " + ChatColor.WHITE + "- ☠️ Lame Toxique");
        player.sendMessage(ChatColor.GRAY + "  /weapon give shadow " + ChatColor.WHITE + "- 💀 Dague des Ombres");
        player.sendMessage("");
        
        player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "🪓 AUTRES:");
        player.sendMessage(ChatColor.GRAY + "  /weapon give thor " + ChatColor.WHITE + "- ⚡ Marteau de Thor");
        player.sendMessage(ChatColor.GRAY + "  /weapon give void " + ChatColor.WHITE + "- 🌀 Faux du Vide");
        player.sendMessage("");
        
        player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "🏹 ARCS:");
        player.sendMessage(ChatColor.GRAY + "  /weapon give rainbow " + ChatColor.WHITE + "- 🌈 Arc-en-Ciel");
        player.sendMessage("");
        
        player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "🔥 SPÉCIALES:");
        player.sendMessage(ChatColor.GRAY + "  /weapon give adamledams " + ChatColor.WHITE + "- 🔥 Boule de Feu");
        player.sendMessage("");
        
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "═════════════════════════════");
        player.sendMessage("");
    }
    
    /**
     * Donner une arme
     */
    private void giveWeapon(Player player, String weaponName) {
        WeaponType type = null;
        
        switch (weaponName.toLowerCase()) {
            case "excalibur":
                type = WeaponType.EXCALIBUR;
                break;
            case "fire":
            case "feu":
                type = WeaponType.FIRE_BLADE;
                break;
            case "ice":
            case "glace":
                type = WeaponType.ICE_SWORD;
                break;
            case "thor":
                type = WeaponType.THOR_HAMMER;
                break;
            case "rainbow":
            case "arcenciel":
                type = WeaponType.RAINBOW_BOW;
                break;
            case "shadow":
            case "ombre":
                type = WeaponType.SHADOW_DAGGER;
                break;
            case "dragon":
                type = WeaponType.DRAGON_SLAYER;
                break;
            case "holy":
            case "sacree":
                type = WeaponType.HOLY_SWORD;
                break;
            case "poison":
            case "toxique":
                type = WeaponType.POISON_BLADE;
                break;
            case "void":
            case "vide":
                type = WeaponType.VOID_SCYTHE;
                break;
            case "adamledams":
            case "boudefeu":
            case "bouldefeu":
            case "fireball":
                type = WeaponType.FIREBALL;
                break;
            default:
                player.sendMessage(ChatColor.RED + "Arme inconnue! Utilise /weapon list");
                return;
        }
        
        // Créer et donner l'arme
        ItemStack weapon = weaponManager.createWeapon(type);
        player.getInventory().addItem(weapon);
        
        // Message épique
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "════════════════════════════════");
        player.sendMessage(ChatColor.YELLOW + "✨ Vous avez obtenu: " + type.getDisplayName());
        player.sendMessage("");
        player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + type.getLore());
        player.sendMessage("");
        player.sendMessage(ChatColor.GREEN + "💡 Clic droit pour utiliser le pouvoir spécial!");
        player.sendMessage(ChatColor.GOLD + "════════════════════════════════");
        player.sendMessage("");
        
        // Effets
        player.getWorld().spawnParticle(org.bukkit.Particle.FIREWORKS_SPARK, player.getLocation(), 50, 1, 1, 1, 0.1);
        player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);
    }
}
