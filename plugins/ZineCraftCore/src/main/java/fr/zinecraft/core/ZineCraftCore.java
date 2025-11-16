package fr.zinecraft.core;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import fr.zinecraft.core.listeners.BlockBreakListener;
import fr.zinecraft.core.listeners.PlayerJoinListener;
import fr.zinecraft.core.listeners.ParkourListener;
import fr.zinecraft.core.commands.CombatCommand;
import fr.zinecraft.core.commands.Combat1v1Command;
import fr.zinecraft.core.commands.Combat2v2Command;
import fr.zinecraft.core.commands.ParkourCommand;
import fr.zinecraft.core.commands.SpawnCommand;
import fr.zinecraft.core.commands.UnstuckCommand;
import fr.zinecraft.core.arena.ArenaManager;
import fr.zinecraft.core.parkour.ParkourManager;

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

    // Managers (à créer plus tard)
    // private DatabaseManager databaseManager;
    // private PlayerManager playerManager;
    // private SkillManager skillManager;
    // private QuestManager questManager;
    // private PetManager petManager;

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

        // 2. Initialiser la base de données
        // TODO: Créer DatabaseManager
        logSuccess("Database connected!");

        // 3. Enregistrer les managers
        arenaManager = new ArenaManager(this);
        parkourManager = new ParkourManager();
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

        // Sauvegarder les données
        // TODO: Sauvegarder toutes les données en mémoire

        // Fermer la connexion BDD
        // TODO: Fermer DatabaseManager

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
     * Enregistrer toutes les commandes
     */
    private void registerCommands() {
        getCommand("combat").setExecutor(new CombatCommand());
        getCommand("combat1v1").setExecutor(new Combat1v1Command());
        getCommand("combat2v2").setExecutor(new Combat2v2Command());
        getCommand("parkour").setExecutor(new ParkourCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("unstuck").setExecutor(new UnstuckCommand());
    }

    /**
     * Enregistrer tous les listeners
     */
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new ParkourListener(), this);
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
