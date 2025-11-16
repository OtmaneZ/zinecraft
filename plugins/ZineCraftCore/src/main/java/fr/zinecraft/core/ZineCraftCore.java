package fr.zinecraft.core;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * ZineCraft Core Plugin
 * Plugin principal du serveur ZineCraft
 *
 * @author Otmane & Adam
 * @version 1.0.0
 */
public class ZineCraftCore extends JavaPlugin {

    private static ZineCraftCore instance;

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
        // TODO: Initialiser les managers
        logSuccess("Managers initialized!");

        // 4. Enregistrer les commandes
        // TODO: Créer et enregistrer les commandes
        logSuccess("Commands registered!");

        // 5. Enregistrer les listeners
        // TODO: Créer et enregistrer les events
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
