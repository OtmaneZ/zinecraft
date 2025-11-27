package fr.zinecraft.core.skills;

import fr.zinecraft.core.ZineCraftCore;
import fr.zinecraft.core.rpg.ClassType;
import fr.zinecraft.core.rpg.RPGPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gestionnaire central des compétences actives
 * Gère l'enregistrement, les cooldowns, et l'exécution des skills
 * 
 * @author Otmane & Copilot
 * @version 2.0
 */
public class SkillManager {
    
    private final ZineCraftCore plugin;
    
    // Registry de toutes les compétences disponibles
    private final Map<String, Skill> skillRegistry;
    
    // Compétences par classe
    private final Map<ClassType, List<Skill>> skillsByClass;
    
    // Cooldowns actifs : UUID -> (SkillID -> ExpirationTime)
    private final Map<UUID, Map<String, Long>> activeCooldowns;
    
    // Mana des joueurs : UUID -> CurrentMana
    private final Map<UUID, Integer> playerMana;
    
    // Configuration
    public static final int MAX_MANA = 100;
    public static final int MANA_REGEN = 2;
    private static final int MANA_REGEN_PER_SECOND = 2;
    
    public SkillManager(ZineCraftCore plugin) {
        this.plugin = plugin;
        this.skillRegistry = new HashMap<>();
        this.skillsByClass = new EnumMap<>(ClassType.class);
        this.activeCooldowns = new ConcurrentHashMap<>();
        this.playerMana = new ConcurrentHashMap<>();
        
        // Initialiser les skills par classe
        initializeClassSkills();
        
        // Démarrer la régénération de mana
        startManaRegeneration();
        
        plugin.getLogger().info("SkillManager initialisé - " + skillRegistry.size() + " compétences enregistrées");
    }
    
    /**
     * Initialiser toutes les compétences des 8 classes
     */
    private void initializeClassSkills() {
        // Pour chaque classe, on va créer leurs 4 skills
        for (ClassType classType : ClassType.values()) {
            skillsByClass.put(classType, new ArrayList<>());
        }
        
        // Les skills seront ajoutées dans des classes séparées par type
        // Pour l'instant, on prépare juste la structure
        plugin.getLogger().info("Structure des skills par classe initialisée");
    }
    
    /**
     * Enregistrer une compétence dans le registry
     */
    public void registerSkill(Skill skill, ClassType... classes) {
        skillRegistry.put(skill.getId(), skill);
        
        for (ClassType classType : classes) {
            skillsByClass.get(classType).add(skill);
        }
        
        plugin.getLogger().info("Skill enregistrée: " + skill.getId() + " pour " + classes.length + " classe(s)");
    }
    
    /**
     * Utiliser une compétence
     */
    public boolean useSkill(Player player, String skillId) {
        Skill skill = skillRegistry.get(skillId);
        
        if (skill == null) {
            player.sendMessage("§c❌ Compétence inconnue !");
            return false;
        }
        
        // Vérifier le niveau requis
        RPGPlayer rpgPlayer = plugin.getPlayerManager().getPlayer(player.getUniqueId());
        if (rpgPlayer == null) {
            return false;
        }
        
        if (rpgPlayer.getLevel() < skill.getMinLevel()) {
            player.sendMessage("§c❌ Niveau " + skill.getMinLevel() + " requis !");
            return false;
        }
        
        // Vérifier le cooldown
        if (isOnCooldown(player.getUniqueId(), skillId)) {
            long remaining = getRemainingCooldown(player.getUniqueId(), skillId);
            player.sendMessage("§c❌ Cooldown: " + remaining + "s restantes");
            return false;
        }
        
        // Vérifier le mana
        int currentMana = getMana(player.getUniqueId());
        if (currentMana < skill.getManaCost()) {
            player.sendMessage("§c❌ Mana insuffisant ! (" + currentMana + "/" + skill.getManaCost() + ")");
            return false;
        }
        
        // Vérifications custom de la skill
        if (!skill.canUse(player)) {
            return false;
        }
        
        // Exécuter la compétence
        boolean success = skill.execute(player);
        
        if (success) {
            // Consommer le mana
            consumeMana(player.getUniqueId(), skill.getManaCost());
            
            // Appliquer le cooldown
            setCooldown(player.getUniqueId(), skillId, skill.getCooldownSeconds());
            
            // Effets visuels/sonores
            skill.playEffects(player);
            
            // Message de succès
            player.sendMessage(skill.getSuccessMessage());
            
            plugin.getLogger().info(player.getName() + " a utilisé la skill: " + skillId);
            return true;
        }
        
        return false;
    }
    
    /**
     * Obtenir les skills d'une classe
     */
    public List<Skill> getClassSkills(ClassType classType) {
        return skillsByClass.getOrDefault(classType, new ArrayList<>());
    }
    
    /**
     * Obtenir les skills par classe (alias)
     */
    public List<Skill> getSkillsByClass(ClassType classType) {
        return skillsByClass.getOrDefault(classType, new ArrayList<>());
    }
    
    /**
     * Obtenir toutes les skills disponibles
     */
    public Collection<Skill> getAllSkills() {
        return skillRegistry.values();
    }
    
    /**
     * Obtenir une skill par son ID
     */
    public Skill getSkill(String skillId) {
        return skillRegistry.get(skillId);
    }
    
    // ==================== Gestion du Mana ====================
    
    /**
     * Obtenir le mana d'un joueur
     */
    public int getMana(UUID playerId) {
        return playerMana.getOrDefault(playerId, MAX_MANA);
    }
    
    /**
     * Définir le mana d'un joueur
     */
    public void setMana(UUID playerId, int mana) {
        playerMana.put(playerId, Math.max(0, Math.min(MAX_MANA, mana)));
    }
    
    /**
     * Consommer du mana
     */
    public void consumeMana(UUID playerId, int amount) {
        int current = getMana(playerId);
        setMana(playerId, current - amount);
    }
    
    /**
     * Régénérer du mana
     */
    public void regenerateMana(UUID playerId, int amount) {
        int current = getMana(playerId);
        setMana(playerId, current + amount);
    }
    
    /**
     * Démarrer la régénération automatique de mana
     */
    private void startManaRegeneration() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (UUID playerId : playerMana.keySet()) {
                    int current = getMana(playerId);
                    if (current < MAX_MANA) {
                        regenerateMana(playerId, MANA_REGEN_PER_SECOND);
                    }
                }
            }
        }.runTaskTimer(plugin, 20L, 20L); // Chaque seconde
    }
    
    // ==================== Gestion des Cooldowns ====================
    
    /**
     * Vérifier si une skill est en cooldown
     */
    public boolean isOnCooldown(UUID playerId, String skillId) {
        Map<String, Long> playerCooldowns = activeCooldowns.get(playerId);
        if (playerCooldowns == null) {
            return false;
        }
        
        Long expirationTime = playerCooldowns.get(skillId);
        if (expirationTime == null) {
            return false;
        }
        
        boolean onCooldown = System.currentTimeMillis() < expirationTime;
        
        // Nettoyer si expiré
        if (!onCooldown) {
            playerCooldowns.remove(skillId);
        }
        
        return onCooldown;
    }
    
    /**
     * Obtenir le temps restant de cooldown (en secondes)
     */
    public long getRemainingCooldown(UUID playerId, String skillId) {
        Map<String, Long> playerCooldowns = activeCooldowns.get(playerId);
        if (playerCooldowns == null) {
            return 0;
        }
        
        Long expirationTime = playerCooldowns.get(skillId);
        if (expirationTime == null) {
            return 0;
        }
        
        long remaining = (expirationTime - System.currentTimeMillis()) / 1000;
        return Math.max(0, remaining);
    }
    
    /**
     * Définir un cooldown
     */
    public void setCooldown(UUID playerId, String skillId, int seconds) {
        activeCooldowns.computeIfAbsent(playerId, k -> new HashMap<>())
            .put(skillId, System.currentTimeMillis() + (seconds * 1000L));
    }
    
    /**
     * Réinitialiser tous les cooldowns d'un joueur
     */
    public void resetCooldowns(UUID playerId) {
        activeCooldowns.remove(playerId);
    }
    
    /**
     * Nettoyer les données d'un joueur à la déconnexion
     */
    public void cleanupPlayer(UUID playerId) {
        activeCooldowns.remove(playerId);
        playerMana.remove(playerId);
    }
    
    /**
     * Arrêter le manager proprement
     */
    public void shutdown() {
        activeCooldowns.clear();
        playerMana.clear();
        plugin.getLogger().info("SkillManager arrêté proprement");
    }
}
