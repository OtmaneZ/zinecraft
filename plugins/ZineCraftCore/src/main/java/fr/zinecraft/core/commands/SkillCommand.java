package fr.zinecraft.core.commands;

import fr.zinecraft.core.ZineCraftCore;
import fr.zinecraft.core.rpg.ClassType;
import fr.zinecraft.core.rpg.RPGPlayer;
import fr.zinecraft.core.skills.Skill;
import fr.zinecraft.core.skills.SkillManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Commande /skill pour le système de compétences
 * Usage: /skill [list|use|info|mana] [skill_id]
 * 
 * @author Otmane & Copilot
 */
public class SkillCommand implements CommandExecutor, TabCompleter {
    
    private final ZineCraftCore plugin;
    private final SkillManager skillManager;
    
    public SkillCommand(ZineCraftCore plugin) {
        this.plugin = plugin;
        this.skillManager = plugin.getSkillManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c❌ Cette commande est réservée aux joueurs !");
            return true;
        }
        
        Player player = (Player) sender;
        
        // /skill sans args -> list
        if (args.length == 0) {
            return showSkillList(player);
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "list":
                return showSkillList(player);
                
            case "use":
                if (args.length < 2) {
                    player.sendMessage("§c❌ Usage : /skill use <skill_id>");
                    return true;
                }
                return useSkill(player, args[1]);
                
            case "info":
                if (args.length < 2) {
                    player.sendMessage("§c❌ Usage : /skill info <skill_id>");
                    return true;
                }
                return showSkillInfo(player, args[1]);
                
            case "mana":
                return showMana(player);
                
            default:
                player.sendMessage("§c❌ Sous-commande inconnue !");
                player.sendMessage("§e/skill [list|use|info|mana]");
                return true;
        }
    }
    
    /**
     * Affiche la liste des skills disponibles pour la classe du joueur
     */
    private boolean showSkillList(Player player) {
        RPGPlayer rpgPlayer = plugin.getPlayerManager().getRPGPlayer(player.getUniqueId());
        
        if (rpgPlayer == null) {
            player.sendMessage("§c❌ Profil RPG introuvable !");
            return true;
        }
        
        ClassType playerClass = rpgPlayer.getPlayerClass();
        
        if (playerClass == ClassType.NONE) {
            player.sendMessage("§c❌ Vous devez choisir une classe avec /class !");
            return true;
        }
        
        List<Skill> classSkills = skillManager.getSkillsByClass(playerClass);
        
        if (classSkills == null || classSkills.isEmpty()) {
            player.sendMessage("§c❌ Aucune compétence disponible pour " + playerClass.name());
            return true;
        }
        
        player.sendMessage("§8§m                                              ");
        player.sendMessage("§6✦ §e§lCompétences de " + playerClass.name() + " §6✦");
        player.sendMessage("");
        
        int currentLevel = rpgPlayer.getLevel();
        int mana = skillManager.getMana(player.getUniqueId());
        
        player.sendMessage("§7Niveau actuel : §a" + currentLevel + " §8| §bMana : §3" + mana + "/" + SkillManager.MAX_MANA);
        player.sendMessage("");
        
        for (Skill skill : classSkills) {
            boolean canUse = skill.getMinLevel() <= currentLevel;
            boolean onCooldown = skillManager.isOnCooldown(player.getUniqueId(), skill.getId());
            boolean enoughMana = mana >= skill.getManaCost();
            
            String prefix = canUse ? "§a✔" : "§c✘";
            String skillName = skill.getRarity().getColor() + skill.getDisplayName();
            String status = "";
            
            if (!canUse) {
                status = " §7(Requis: lvl " + skill.getMinLevel() + ")";
            } else if (onCooldown) {
                long remaining = skillManager.getRemainingCooldown(player.getUniqueId(), skill.getId());
                status = " §c(Cooldown: " + remaining + "s)";
            } else if (!enoughMana) {
                status = " §9(Pas assez de mana)";
            } else {
                status = " §a(Prêt !)";
            }
            
            player.sendMessage(prefix + " " + skillName + status);
            player.sendMessage("   §7▸ " + skill.getDescription());
            player.sendMessage("   §7▸ Cooldown: §e" + skill.getCooldownSeconds() + "s §7| Mana: §3" + skill.getManaCost());
            player.sendMessage("");
        }
        
        player.sendMessage("§7Utilisez §e/skill use <id> §7pour utiliser une compétence");
        player.sendMessage("§7Utilisez §e/skill info <id> §7pour plus de détails");
        player.sendMessage("§8§m                                              ");
        
        return true;
    }
    
    /**
     * Utilise une compétence
     */
    private boolean useSkill(Player player, String skillId) {
        RPGPlayer rpgPlayer = plugin.getPlayerManager().getRPGPlayer(player.getUniqueId());
        
        if (rpgPlayer == null) {
            player.sendMessage("§c❌ Profil RPG introuvable !");
            return true;
        }
        
        Skill skill = skillManager.getSkill(skillId);
        
        if (skill == null) {
            player.sendMessage("§c❌ Compétence introuvable : " + skillId);
            player.sendMessage("§7Utilisez §e/skill list §7pour voir les compétences disponibles");
            return true;
        }
        
        // Vérifier que le joueur a la bonne classe
        ClassType playerClass = rpgPlayer.getPlayerClass();
        List<Skill> classSkills = skillManager.getSkillsByClass(playerClass);
        
        if (classSkills == null || !classSkills.contains(skill)) {
            player.sendMessage("§c❌ Cette compétence n'est pas disponible pour votre classe !");
            return true;
        }
        
        // Utiliser la compétence via le manager
        boolean success = skillManager.useSkill(player, skillId);
        
        if (!success) {
            // Le manager a déjà envoyé un message d'erreur
            return true;
        }
        
        // Afficher les cooldowns et mana
        long cooldown = skillManager.getRemainingCooldown(player.getUniqueId(), skillId);
        int mana = skillManager.getMana(player.getUniqueId());
        
        player.sendMessage("§7Cooldown : §e" + cooldown + "s §8| §bMana restante : §3" + mana);
        
        return true;
    }
    
    /**
     * Affiche les détails d'une compétence
     */
    private boolean showSkillInfo(Player player, String skillId) {
        Skill skill = skillManager.getSkill(skillId);
        
        if (skill == null) {
            player.sendMessage("§c❌ Compétence introuvable : " + skillId);
            return true;
        }
        
        player.sendMessage("§8§m                                              ");
        player.sendMessage(skill.getRarity().getColor() + "§l" + skill.getDisplayName());
        player.sendMessage("");
        player.sendMessage("§7Description : §f" + skill.getDescription());
        player.sendMessage("");
        player.sendMessage("§7Type : " + skill.getType().name());
        player.sendMessage("§7Rareté : " + skill.getRarity().getColor() + skill.getRarity().name());
        player.sendMessage("§7Niveau requis : §a" + skill.getMinLevel());
        player.sendMessage("§7Cooldown : §e" + skill.getCooldownSeconds() + " secondes");
        player.sendMessage("§7Coût en mana : §3" + skill.getManaCost());
        player.sendMessage("");
        
        // État actuel
        RPGPlayer rpgPlayer = plugin.getPlayerManager().getRPGPlayer(player.getUniqueId());
        if (rpgPlayer != null) {
            boolean canUse = rpgPlayer.getLevel() >= skill.getMinLevel();
            boolean onCooldown = skillManager.isOnCooldown(player.getUniqueId(), skill.getId());
            int mana = skillManager.getMana(player.getUniqueId());
            boolean enoughMana = mana >= skill.getManaCost();
            
            if (canUse && !onCooldown && enoughMana) {
                player.sendMessage("§a✔ Compétence prête à l'utilisation !");
            } else {
                if (!canUse) {
                    player.sendMessage("§c✘ Niveau insuffisant (vous êtes niveau " + rpgPlayer.getLevel() + ")");
                }
                if (onCooldown) {
                    long remaining = skillManager.getRemainingCooldown(player.getUniqueId(), skill.getId());
                    player.sendMessage("§c✘ Cooldown : " + remaining + " secondes restantes");
                }
                if (!enoughMana) {
                    player.sendMessage("§c✘ Mana insuffisante (vous avez " + mana + "/" + skill.getManaCost() + ")");
                }
            }
        }
        
        player.sendMessage("");
        player.sendMessage("§7Utilisez §e/skill use " + skill.getId() + " §7pour utiliser cette compétence");
        player.sendMessage("§8§m                                              ");
        
        return true;
    }
    
    /**
     * Affiche la mana actuelle
     */
    private boolean showMana(Player player) {
        int mana = skillManager.getMana(player.getUniqueId());
        int maxMana = SkillManager.MAX_MANA;
        
        // Barre de progression
        int bars = (mana * 20) / maxMana;
        StringBuilder progressBar = new StringBuilder("§3[");
        
        for (int i = 0; i < 20; i++) {
            if (i < bars) {
                progressBar.append("§b|");
            } else {
                progressBar.append("§7|");
            }
        }
        progressBar.append("§3]");
        
        player.sendMessage("§8§m                    ");
        player.sendMessage("§b✦ §3§lMana");
        player.sendMessage("");
        player.sendMessage(progressBar.toString());
        player.sendMessage("§3" + mana + " §7/ §b" + maxMana);
        player.sendMessage("");
        player.sendMessage("§7Régénération : §b+" + SkillManager.MANA_REGEN + " §7par seconde");
        player.sendMessage("§8§m                    ");
        
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return new ArrayList<>();
        }
        
        Player player = (Player) sender;
        
        if (args.length == 1) {
            // Tab complete pour les sous-commandes
            return Arrays.asList("list", "use", "info", "mana").stream()
                .filter(s -> s.startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
        }
        
        if (args.length == 2 && (args[0].equalsIgnoreCase("use") || args[0].equalsIgnoreCase("info"))) {
            // Tab complete pour les skill IDs
            RPGPlayer rpgPlayer = ((ZineCraftCore) plugin).getPlayerManager()
                .getRPGPlayer(player.getUniqueId());
            
            if (rpgPlayer == null || rpgPlayer.getPlayerClass() == ClassType.NONE) {
                return new ArrayList<>();
            }
            
            List<Skill> classSkills = skillManager.getSkillsByClass(rpgPlayer.getPlayerClass());
            
            if (classSkills == null) {
                return new ArrayList<>();
            }
            
            return classSkills.stream()
                .map(Skill::getId)
                .filter(id -> id.startsWith(args[1].toLowerCase()))
                .collect(Collectors.toList());
        }
        
        return new ArrayList<>();
    }
}
