package fr.zinecraft.core.commands;

import fr.zinecraft.core.ZineCraftCore;
import fr.zinecraft.core.pets.Pet;
import fr.zinecraft.core.pets.PetManager;
import fr.zinecraft.core.pets.PetType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Commande pour gérer les familiers
 * 
 * @author Otmane & Adam
 */
public class PetCommand implements CommandExecutor {
    
    private final PetManager petManager;
    
    public PetCommand() {
        this.petManager = ZineCraftCore.getInstance().getPetManager();
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
        
        switch (args[0].toLowerCase()) {
            case "list":
                listPets(player);
                break;
                
            case "spawn":
                if (args.length < 2) {
                    player.sendMessage(ChatColor.RED + "Usage: /pet spawn <type>");
                    return true;
                }
                spawnPet(player, args[1]);
                break;
                
            case "remove":
                removePet(player);
                break;
                
            case "info":
                showPetInfo(player);
                break;
                
            case "evolve":
                evolvePet(player);
                break;
                
            case "rename":
                if (args.length < 2) {
                    player.sendMessage(ChatColor.RED + "Usage: /pet rename <nom>");
                    return true;
                }
                renamePet(player, args[1]);
                break;
                
            default:
                sendHelp(player);
                break;
        }
        
        return true;
    }
    
    /**
     * Afficher l'aide
     */
    private void sendHelp(Player player) {
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "═══ 🐾 FAMILIERS ÉVOLUTIFS 🐾 ═══");
        player.sendMessage("");
        player.sendMessage(ChatColor.YELLOW + "Commandes disponibles:");
        player.sendMessage(ChatColor.GRAY + "  /pet list " + ChatColor.WHITE + "- Liste des familiers");
        player.sendMessage(ChatColor.GRAY + "  /pet spawn <type> " + ChatColor.WHITE + "- Invoquer un familier");
        player.sendMessage(ChatColor.GRAY + "  /pet info " + ChatColor.WHITE + "- Info de votre familier");
        player.sendMessage(ChatColor.GRAY + "  /pet evolve " + ChatColor.WHITE + "- Faire évoluer (niveau 20)");
        player.sendMessage(ChatColor.GRAY + "  /pet rename <nom> " + ChatColor.WHITE + "- Renommer");
        player.sendMessage(ChatColor.GRAY + "  /pet remove " + ChatColor.WHITE + "- Retirer");
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "══════════════════════════════");
        player.sendMessage("");
    }
    
    /**
     * Lister les familiers disponibles
     */
    private void listPets(Player player) {
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "═══ 🐾 FAMILIERS DISPONIBLES 🐾 ═══");
        player.sendMessage("");
        
        player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "🐺 LOUPS:");
        player.sendMessage(ChatColor.GRAY + "  /pet spawn wolf " + ChatColor.WHITE + "- Louveteau");
        player.sendMessage(ChatColor.YELLOW + "    └→ Loup Alpha (niv.20)");
        player.sendMessage(ChatColor.GOLD + "       └→ Loup Légendaire (niv.20)");
        player.sendMessage("");
        
        player.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "🐉 DRAGONS:");
        player.sendMessage(ChatColor.GRAY + "  /pet spawn dragon " + ChatColor.WHITE + "- Bébé Dragon");
        player.sendMessage(ChatColor.LIGHT_PURPLE + "    └→ Dragon Adolescent (niv.20)");
        player.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "       └→ Dragon Adulte (niv.20)");
        player.sendMessage("");
        
        player.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + "🦅 AIGLES:");
        player.sendMessage(ChatColor.GRAY + "  /pet spawn eagle " + ChatColor.WHITE + "- Petit Aigle");
        player.sendMessage(ChatColor.GOLD + "    └→ Aigle Doré (niv.20)");
        player.sendMessage("");
        
        player.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "🗿 GOLEMS:");
        player.sendMessage(ChatColor.GRAY + "  /pet spawn golem " + ChatColor.WHITE + "- Golem de Pierre");
        player.sendMessage(ChatColor.WHITE + "    └→ Golem de Fer (niv.20)");
        player.sendMessage(ChatColor.AQUA + "       └→ Golem de Diamant (niv.20)");
        player.sendMessage("");
        
        player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "🔥 LÉGENDAIRES:");
        player.sendMessage(ChatColor.GRAY + "  /pet spawn phoenix " + ChatColor.WHITE + "- Phoenix de Flammes");
        player.sendMessage(ChatColor.GRAY + "  /pet spawn unicorn " + ChatColor.WHITE + "- Licorne Magique");
        player.sendMessage("");
        
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "═══════════════════════════════");
        player.sendMessage("");
    }
    
    /**
     * Invoquer un familier
     */
    private void spawnPet(Player player, String typeStr) {
        PetType type = null;
        
        switch (typeStr.toLowerCase()) {
            case "wolf":
            case "loup":
                type = PetType.WOLF_PUP;
                break;
            case "dragon":
                type = PetType.DRAGON_BABY;
                break;
            case "eagle":
            case "aigle":
                type = PetType.EAGLE_SMALL;
                break;
            case "golem":
                type = PetType.GOLEM_STONE;
                break;
            case "phoenix":
                type = PetType.PHOENIX_FLAME;
                break;
            case "unicorn":
            case "licorne":
                type = PetType.UNICORN;
                break;
            default:
                player.sendMessage(ChatColor.RED + "Type inconnu! Utilise /pet list");
                return;
        }
        
        petManager.spawnPet(player, type);
    }
    
    /**
     * Retirer le familier
     */
    private void removePet(Player player) {
        Pet pet = petManager.getPet(player);
        if (pet == null) {
            player.sendMessage(ChatColor.RED + "Vous n'avez pas de familier!");
            return;
        }
        
        petManager.removePet(player);
        player.sendMessage(ChatColor.YELLOW + "Votre familier est parti... À bientôt! 👋");
    }
    
    /**
     * Afficher les infos du familier
     */
    private void showPetInfo(Player player) {
        Pet pet = petManager.getPet(player);
        if (pet == null) {
            player.sendMessage(ChatColor.RED + "Vous n'avez pas de familier!");
            return;
        }
        
        PetType type = pet.getType();
        
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "═══ 📋 INFO FAMILIER 📋 ═══");
        player.sendMessage("");
        player.sendMessage(ChatColor.YELLOW + "Nom: " + type.getColor() + pet.getCustomName());
        player.sendMessage(ChatColor.YELLOW + "Type: " + type.getColor() + type.getDefaultName());
        player.sendMessage(ChatColor.YELLOW + "Niveau: " + ChatColor.WHITE + pet.getLevel());
        player.sendMessage(ChatColor.YELLOW + "Expérience: " + ChatColor.WHITE + pet.getExperience() + "/" + pet.getRequiredExperience() + " (" + pet.getExperiencePercent() + "%)");
        
        if (pet.canEvolve()) {
            player.sendMessage("");
            player.sendMessage(ChatColor.GREEN + "✨ Peut évoluer en: " + pet.getEvolution().getColor() + pet.getEvolution().getDefaultName());
            player.sendMessage(ChatColor.GRAY + "Utilise: /pet evolve");
        } else if (type.getEvolution() != null) {
            player.sendMessage("");
            player.sendMessage(ChatColor.GRAY + "Prochaine évolution: " + type.getEvolution().getColor() + type.getEvolution().getDefaultName());
            player.sendMessage(ChatColor.GRAY + "Niveau requis: " + type.getEvolutionLevel());
        }
        
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "═══════════════════════");
        player.sendMessage("");
    }
    
    /**
     * Faire évoluer le familier
     */
    private void evolvePet(Player player) {
        petManager.evolvePet(player);
    }
    
    /**
     * Renommer le familier
     */
    private void renamePet(Player player, String name) {
        Pet pet = petManager.getPet(player);
        if (pet == null) {
            player.sendMessage(ChatColor.RED + "Vous n'avez pas de familier!");
            return;
        }
        
        pet.setCustomName(name);
        pet.getEntity().setCustomName(pet.getType().getColor() + name + ChatColor.GRAY + " [Niv." + pet.getLevel() + "]");
        
        player.sendMessage(ChatColor.GREEN + "✓ Familier renommé en: " + pet.getType().getColor() + name);
    }
}
