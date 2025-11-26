package fr.zinecraft.core.rpg;

import fr.zinecraft.core.ZineCraftCore;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

/**
 * Gestionnaire des classes RPG et de leurs compétences
 * 
 * @author Otmane & Copilot
 */
public class ClassManager {
    
    private final ZineCraftCore plugin;
    private final PlayerManager playerManager;
    private final Map<ClassType, List<Skill>> classSkills;
    
    public ClassManager(ZineCraftCore plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
        this.classSkills = new HashMap<>();
        
        initializeSkills();
    }
    
    /**
     * Initialiser toutes les compétences de toutes les classes
     */
    private void initializeSkills() {
        // ==================== WARRIOR (Guerrier) ====================
        List<Skill> warriorSkills = new ArrayList<>();
        warriorSkills.add(new Skill("iron_skin", "Peau de Fer", 
            "Augmente votre armure de 20%", ClassType.WARRIOR, 1, 5, Skill.SkillType.PASSIVE));
        warriorSkills.add(new Skill("power_strike", "Frappe Puissante", 
            "Augmente les dégâts de 15%", ClassType.WARRIOR, 5, 5, Skill.SkillType.PASSIVE));
        warriorSkills.add(new Skill("battle_cry", "Cri de Guerre", 
            "Augmente la force pendant 10s", ClassType.WARRIOR, 10, 3, Skill.SkillType.ACTIVE));
        warriorSkills.add(new Skill("berserker", "Berserker", 
            "Plus de dégâts quand HP bas", ClassType.WARRIOR, 20, 1, Skill.SkillType.ULTIMATE));
        classSkills.put(ClassType.WARRIOR, warriorSkills);
        
        // ==================== ARCHER ====================
        List<Skill> archerSkills = new ArrayList<>();
        archerSkills.add(new Skill("eagle_eye", "Œil d'Aigle", 
            "Augmente la précision de 25%", ClassType.ARCHER, 1, 5, Skill.SkillType.PASSIVE));
        archerSkills.add(new Skill("swift_feet", "Pieds Agiles", 
            "Augmente la vitesse de 10%", ClassType.ARCHER, 5, 5, Skill.SkillType.PASSIVE));
        archerSkills.add(new Skill("multi_shot", "Tir Multiple", 
            "Tire 3 flèches à la fois", ClassType.ARCHER, 10, 3, Skill.SkillType.ACTIVE));
        archerSkills.add(new Skill("arrow_rain", "Pluie de Flèches", 
            "Invoque une pluie mortelle", ClassType.ARCHER, 20, 1, Skill.SkillType.ULTIMATE));
        classSkills.put(ClassType.ARCHER, archerSkills);
        
        // ==================== MAGE ====================
        List<Skill> mageSkills = new ArrayList<>();
        mageSkills.add(new Skill("mana_pool", "Réserve de Mana", 
            "Augmente votre mana de 50%", ClassType.MAGE, 1, 5, Skill.SkillType.PASSIVE));
        mageSkills.add(new Skill("spell_power", "Puissance Magique", 
            "Augmente les dégâts magiques", ClassType.MAGE, 5, 5, Skill.SkillType.PASSIVE));
        mageSkills.add(new Skill("fireball", "Boule de Feu", 
            "Lance une boule de feu explosive", ClassType.MAGE, 10, 3, Skill.SkillType.ACTIVE));
        mageSkills.add(new Skill("meteor", "Météore", 
            "Invoque un météore dévastateur", ClassType.MAGE, 20, 1, Skill.SkillType.ULTIMATE));
        classSkills.put(ClassType.MAGE, mageSkills);
        
        // ==================== PALADIN ====================
        List<Skill> paladinSkills = new ArrayList<>();
        paladinSkills.add(new Skill("holy_shield", "Bouclier Sacré", 
            "Réduit les dégâts de 15%", ClassType.PALADIN, 1, 5, Skill.SkillType.PASSIVE));
        paladinSkills.add(new Skill("divine_heal", "Soin Divin", 
            "Régénère automatiquement", ClassType.PALADIN, 5, 5, Skill.SkillType.PASSIVE));
        paladinSkills.add(new Skill("consecration", "Consécration", 
            "Zone sacrée qui soigne les alliés", ClassType.PALADIN, 10, 3, Skill.SkillType.ACTIVE));
        paladinSkills.add(new Skill("divine_intervention", "Intervention Divine", 
            "Invulnérabilité pendant 5s", ClassType.PALADIN, 20, 1, Skill.SkillType.ULTIMATE));
        classSkills.put(ClassType.PALADIN, paladinSkills);
        
        // ==================== ASSASSIN ====================
        List<Skill> assassinSkills = new ArrayList<>();
        assassinSkills.add(new Skill("critical_strike", "Coup Critique", 
            "30% de chance de critique x2", ClassType.ASSASSIN, 1, 5, Skill.SkillType.PASSIVE));
        assassinSkills.add(new Skill("shadow_step", "Pas de l'Ombre", 
            "Téléportation courte distance", ClassType.ASSASSIN, 5, 5, Skill.SkillType.PASSIVE));
        assassinSkills.add(new Skill("stealth", "Furtivité", 
            "Invisibilité pendant 10s", ClassType.ASSASSIN, 10, 3, Skill.SkillType.ACTIVE));
        assassinSkills.add(new Skill("death_mark", "Marque de Mort", 
            "Tue instantanément si HP<20%", ClassType.ASSASSIN, 20, 1, Skill.SkillType.ULTIMATE));
        classSkills.put(ClassType.ASSASSIN, assassinSkills);
        
        // ==================== NECROMANCER ====================
        List<Skill> necroSkills = new ArrayList<>();
        necroSkills.add(new Skill("life_drain", "Drain de Vie", 
            "Vole 10% HP des dégâts infligés", ClassType.NECROMANCER, 1, 5, Skill.SkillType.PASSIVE));
        necroSkills.add(new Skill("death_touch", "Contact Mortel", 
            "Empoisonne les ennemis", ClassType.NECROMANCER, 5, 5, Skill.SkillType.PASSIVE));
        necroSkills.add(new Skill("summon_skeleton", "Invocation Squelette", 
            "Invoque un squelette allié", ClassType.NECROMANCER, 10, 3, Skill.SkillType.ACTIVE));
        necroSkills.add(new Skill("army_of_dead", "Armée des Morts", 
            "Invoque 5 squelettes puissants", ClassType.NECROMANCER, 20, 1, Skill.SkillType.ULTIMATE));
        classSkills.put(ClassType.NECROMANCER, necroSkills);
        
        // ==================== DRUID ====================
        List<Skill> druidSkills = new ArrayList<>();
        druidSkills.add(new Skill("nature_blessing", "Bénédiction Nature", 
            "Régénération naturelle +50%", ClassType.DRUID, 1, 5, Skill.SkillType.PASSIVE));
        druidSkills.add(new Skill("animal_companion", "Compagnon Animal", 
            "Un loup vous accompagne", ClassType.DRUID, 5, 5, Skill.SkillType.PASSIVE));
        druidSkills.add(new Skill("wild_shape", "Forme Sauvage", 
            "Transformez-vous en loup", ClassType.DRUID, 10, 3, Skill.SkillType.ACTIVE));
        druidSkills.add(new Skill("force_of_nature", "Force de la Nature", 
            "Invoque des arbres combattants", ClassType.DRUID, 20, 1, Skill.SkillType.ULTIMATE));
        classSkills.put(ClassType.DRUID, druidSkills);
        
        // ==================== ARCHMAGE ====================
        List<Skill> archmageSkills = new ArrayList<>();
        archmageSkills.add(new Skill("mastery_all", "Maîtrise Absolue", 
            "Tous les éléments maîtrisés", ClassType.ARCHMAGE, 1, 5, Skill.SkillType.PASSIVE));
        archmageSkills.add(new Skill("mana_shield", "Bouclier de Mana", 
            "Absorbe 50% des dégâts", ClassType.ARCHMAGE, 5, 5, Skill.SkillType.PASSIVE));
        archmageSkills.add(new Skill("time_warp", "Distorsion Temporelle", 
            "Ralentit les ennemis", ClassType.ARCHMAGE, 10, 3, Skill.SkillType.ACTIVE));
        archmageSkills.add(new Skill("apocalypse", "Apocalypse", 
            "Détruit tout dans un rayon", ClassType.ARCHMAGE, 20, 1, Skill.SkillType.ULTIMATE));
        classSkills.put(ClassType.ARCHMAGE, archmageSkills);
    }
    
    /**
     * Assigner une classe à un joueur
     */
    public boolean assignClass(Player player, ClassType classType) {
        RPGPlayer rpgPlayer = playerManager.getPlayer(player);
        
        if (rpgPlayer == null) {
            player.sendMessage(ChatColor.RED + "Erreur: Profil RPG introuvable!");
            return false;
        }
        
        if (rpgPlayer.hasChosenClass()) {
            player.sendMessage(ChatColor.RED + "Vous avez déjà choisi une classe!");
            return false;
        }
        
        // Vérifier si c'est une classe premium (on vérifie juste, pas de paiement pour l'instant)
        if (classType.isPremium()) {
            // TODO: Vérifier le grade VIP du joueur via permissions
            player.sendMessage(ChatColor.YELLOW + "⚠ Classe premium : " + classType.getPriceEuros() + "€");
        }
        
        // Assigner la classe
        rpgPlayer.setClassType(classType);
        
        // Appliquer les stats de base
        applyClassStats(player, classType);
        
        // Sauvegarder
        playerManager.savePlayer(rpgPlayer);
        
        // Messages
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "✨ CLASSE CHOISIE ! ✨");
        player.sendMessage("");
        player.sendMessage(ChatColor.GREEN + "Vous êtes maintenant " + classType.getIcon() + " " + 
                          ChatColor.YELLOW + ChatColor.BOLD + classType.getDisplayName());
        player.sendMessage(ChatColor.GRAY + classType.getDescription());
        player.sendMessage("");
        player.sendMessage(ChatColor.AQUA + "Tapez /skills pour voir vos compétences !");
        player.sendMessage("");
        
        return true;
    }
    
    /**
     * Appliquer les stats de la classe au joueur
     */
    public void applyClassStats(Player player, ClassType classType) {
        // HP
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(classType.getBaseHealth());
        player.setHealth(classType.getBaseHealth());
        
        // Speed
        double baseSpeed = 0.2; // Vitesse par défaut Minecraft
        player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(baseSpeed * classType.getBaseSpeed());
        
        // Effets permanents selon la classe
        switch (classType) {
            case WARRIOR:
            case PALADIN:
                // Résistance permanente
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
                break;
            case ASSASSIN:
                // Vitesse permanente
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
                break;
            case MAGE:
            case ARCHMAGE:
                // Régénération permanente
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0, false, false));
                break;
        }
    }
    
    /**
     * Obtenir les compétences d'une classe
     */
    public List<Skill> getClassSkills(ClassType classType) {
        return classSkills.getOrDefault(classType, new ArrayList<>());
    }
    
    /**
     * Obtenir les compétences disponibles pour un joueur (selon son niveau)
     */
    public List<Skill> getAvailableSkills(RPGPlayer rpgPlayer) {
        if (!rpgPlayer.hasChosenClass()) {
            return new ArrayList<>();
        }
        
        List<Skill> allSkills = getClassSkills(rpgPlayer.getClassType());
        List<Skill> available = new ArrayList<>();
        
        for (Skill skill : allSkills) {
            if (rpgPlayer.getLevel() >= skill.getRequiredLevel()) {
                available.add(skill);
            }
        }
        
        return available;
    }
}
