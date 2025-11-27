package fr.zinecraft.core;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import fr.zinecraft.core.listeners.BlockBreakListener;
import fr.zinecraft.core.listeners.PlayerJoinListener;
import fr.zinecraft.core.listeners.ParkourListener;
import fr.zinecraft.core.listeners.BossListener;
import fr.zinecraft.core.listeners.WeaponListener;
import fr.zinecraft.core.listeners.RPGPlayerListener;
import fr.zinecraft.core.listeners.NPCListener;
import fr.zinecraft.core.listeners.ShopListener;
import fr.zinecraft.core.listeners.QuestListener;
import fr.zinecraft.core.listeners.EventMultiplierListener;
import fr.zinecraft.core.commands.CombatCommand;
import fr.zinecraft.core.commands.Combat1v1Command;
import fr.zinecraft.core.commands.Combat2v2Command;
import fr.zinecraft.core.commands.ParkourCommand;
import fr.zinecraft.core.commands.SpawnCommand;
import fr.zinecraft.core.commands.UnstuckCommand;
import fr.zinecraft.core.commands.BossCommand;
import fr.zinecraft.core.commands.BuildCommand;
import fr.zinecraft.core.commands.PetCommand;
import fr.zinecraft.core.commands.WeaponCommand;
import fr.zinecraft.core.commands.PowerCommand;
import fr.zinecraft.core.commands.ScaryZoneCommand;
import fr.zinecraft.core.commands.ClassCommand;
import fr.zinecraft.core.commands.EventCommand;
import fr.zinecraft.core.commands.EffectCommand;
import fr.zinecraft.core.commands.BalanceCommand;
import fr.zinecraft.core.commands.PayCommand;
import fr.zinecraft.core.commands.EconomyCommand;
import fr.zinecraft.core.commands.ShopCommand;
import com.zinecraft.commands.VillageCommand;
import com.zinecraft.commands.TutorialCommand;
import fr.zinecraft.core.commands.QuestCommand;
import fr.zinecraft.core.arena.ArenaManager;
import fr.zinecraft.core.economy.EconomyManager;
import fr.zinecraft.core.economy.ShopManager;
import fr.zinecraft.core.quests.QuestManager;
import fr.zinecraft.core.parkour.ParkourManager;
import fr.zinecraft.core.boss.BossManager;
import fr.zinecraft.core.pets.PetManager;
import fr.zinecraft.core.weapons.WeaponManager;
import fr.zinecraft.core.powers.PowerManager;
import fr.zinecraft.core.rpg.PlayerManager;
import fr.zinecraft.core.rpg.ClassManager;
import fr.zinecraft.core.rpg.NPCManager;
import fr.zinecraft.core.rpg.LevelManager;
import fr.zinecraft.core.commands.StatsCommand;
import fr.zinecraft.core.listeners.XPListener;
import fr.zinecraft.core.events.EventManager;
import fr.zinecraft.core.visuals.VisualEffectManager;

/**
 * ZineCraft Core Plugin
 * Plugin principal du serveur ZineCraft
 *
 * @author Otmane & Adam
 * @version 1.0.0
 */
public class ZineCraftCore extends JavaPlugin {

    private static ZineCraftCore instance;

    // Managers
    private ArenaManager arenaManager;
    private ParkourManager parkourManager;
    private BossManager bossManager;
    private PetManager petManager;
    private WeaponManager weaponManager;
    private PowerManager powerManager;
    private PlayerManager playerManager;
    private ClassManager classManager;
    private NPCManager npcManager;
    private NPCListener npcListener;
    private LevelManager levelManager;
    private EventManager eventManager;
    private VisualEffectManager visualEffectManager;
    private EconomyManager economyManager;
    private ShopManager shopManager;
    private QuestManager questManager;

    @Override
    public void onEnable() {
        instance = this;

        // ASCII Art du logo
        logInfo("  ______                ____            __ _   ");
        logInfo(" |___  /               / ___|_ __ __ _ / _| |_ ");
        logInfo("    / / _ _ __   ___  | |   | '__/ _` | |_| __|");
        logInfo("   / / | | '_ \\ / _ \\ | |___| | | (_| |  _| |_ ");
        logInfo("  /_/  |_| .__/ \\___/  \\____|_|  \\__,_|_|  \\__|");
        logInfo("         |_|                                    ");
        logInfo("");
        logInfo("Version: " + getDescription().getVersion());
        logInfo("Authors: Otmane & Adam");
        logInfo("");

        // 1. Charger la configuration
        saveDefaultConfig();
        logSuccess("Configuration loaded!");

        // 2. Initialiser la base de données MySQL
        playerManager = new PlayerManager(this);
        logSuccess("Database connected!");

        // 3. Enregistrer les managers
        arenaManager = new ArenaManager(this);
        parkourManager = new ParkourManager();
        bossManager = new BossManager(this);
        petManager = new PetManager(this);
        weaponManager = new WeaponManager(this);
        powerManager = new PowerManager(this);
        classManager = new ClassManager(this);
        npcManager = new NPCManager(this, classManager);
        levelManager = new LevelManager(this);
        economyManager = new EconomyManager(this);
        shopManager = new ShopManager(this);
        questManager = new QuestManager(this);
        eventManager = new EventManager(this);
        visualEffectManager = new VisualEffectManager(this);
        logSuccess("Managers initialized!");

        // 4. Enregistrer les commandes
        registerCommands();
        logSuccess("Commands registered!");

        // 5. Enregistrer les listeners
        registerListeners();
        logSuccess("Listeners registered!");

        logInfo("");
        logSuccess("ZineCraft Core enabled successfully!");
        logInfo("");
    }

    @Override
    public void onDisable() {
        logInfo("");
        logInfo("Disabling ZineCraft Core...");

        // Arrêter les événements
        if (eventManager != null) {
            eventManager.shutdown();
        }

        // Sauvegarder les données RPG
        if (playerManager != null) {
            playerManager.saveAllPlayers();
            playerManager.closeConnection();
        }

        logSuccess("ZineCraft Core disabled successfully!");
        logInfo("");
    }

    /**
     * Récupérer l'instance du plugin
     */
    public static ZineCraftCore getInstance() {
        return instance;
    }

    /**
     * Récupérer l'ArenaManager
     */
    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    /**
     * Récupérer le ParkourManager
     */
    public ParkourManager getParkourManager() {
        return parkourManager;
    }

    /**
     * Récupérer le BossManager
     */
    public BossManager getBossManager() {
        return bossManager;
    }

    /**
     * Récupérer le PetManager
     */
    public PetManager getPetManager() {
        return petManager;
    }

    /**
     * Récupérer le WeaponManager
     */
    public WeaponManager getWeaponManager() {
        return weaponManager;
    }

    /**
     * Récupérer le PowerManager
     */
    public PowerManager getPowerManager() {
        return powerManager;
    }

    /**
     * Récupérer le PlayerManager
     */
    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    /**
     * Récupérer le ClassManager
     */
    public ClassManager getClassManager() {
        return classManager;
    }

    /**
     * Récupérer le NPCManager
     */
    public NPCManager getNPCManager() {
        return npcManager;
    }

    /**
     * Récupérer le LevelManager
     */
    public LevelManager getLevelManager() {
        return levelManager;
    }

    /**
     * Récupérer l'EventManager
     */
    public EventManager getEventManager() {
        return eventManager;
    }

    /**
     * Récupérer le VisualEffectManager
     */
    public VisualEffectManager getVisualEffectManager() {
        return visualEffectManager;
    }

    /**
     * Récupérer l'EconomyManager
     */
    public EconomyManager getEconomyManager() {
        return economyManager;
    }

    /**
     * Récupérer le ShopManager
     */
    public ShopManager getShopManager() {
        return shopManager;
    }

    /**
     * Récupérer le QuestManager
     */
    public QuestManager getQuestManager() {
        return questManager;
    }

    /**
     * Enregistrer toutes les commandes
     */
    private void registerCommands() {
        getCommand("combat").setExecutor(new CombatCommand());
        getCommand("combat1v1").setExecutor(new Combat1v1Command());
        getCommand("combat2v2").setExecutor(new Combat2v2Command());
        getCommand("parkour").setExecutor(new ParkourCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("unstuck").setExecutor(new UnstuckCommand());
        getCommand("boss").setExecutor(new BossCommand());
        getCommand("build").setExecutor(new BuildCommand());
        getCommand("pet").setExecutor(new PetCommand());
        getCommand("weapon").setExecutor(new WeaponCommand());
        getCommand("power").setExecutor(new PowerCommand());
        getCommand("scary").setExecutor(new ScaryZoneCommand());
        getCommand("class").setExecutor(new ClassCommand(playerManager, classManager, npcManager));
        getCommand("stats").setExecutor(new StatsCommand(playerManager, levelManager));
        getCommand("balance").setExecutor(new BalanceCommand(this));
        getCommand("pay").setExecutor(new PayCommand(this));
        getCommand("economy").setExecutor(new EconomyCommand(this));
        getCommand("shop").setExecutor(new ShopCommand(this));
        getCommand("quest").setExecutor(new QuestCommand(this));
        getCommand("event").setExecutor(new EventCommand(this));
        getCommand("effect").setExecutor(new EffectCommand());
        getCommand("village").setExecutor(new VillageCommand(this));
        getCommand("tutorial").setExecutor(new TutorialCommand(this));
    }

    /**
     * Enregistrer tous les listeners
     */
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new ParkourListener(), this);
        getServer().getPluginManager().registerEvents(new BossListener(), this);
        getServer().getPluginManager().registerEvents(new WeaponListener(), this);
        getServer().getPluginManager().registerEvents(new RPGPlayerListener(), this);
        npcListener = new NPCListener();
        npcListener.setNPCManager(npcManager);
        getServer().getPluginManager().registerEvents(npcListener, this);
        getServer().getPluginManager().registerEvents(new XPListener(levelManager), this);
        getServer().getPluginManager().registerEvents(new ShopListener(this), this);
        getServer().getPluginManager().registerEvents(new QuestListener(this), this);
        getServer().getPluginManager().registerEvents(new EventMultiplierListener(this), this);
    }

    /**
     * Logger une info
     */
    private void logInfo(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + message);
    }

    /**
     * Logger un succès
     */
    private void logSuccess(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "✔ " + message);
    }

    /**
     * Logger une erreur
     */
    private void logError(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "✘ " + message);
    }

    /**
     * Logger un warning
     */
    private void logWarning(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "⚠ " + message);
    }
}
