package com.zinecraft.commands;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Commande pour DÉTRUIRE la Tutorial Island
 * Utile pour tests et reconstructions
 */
public class ResetTutorialCommand implements CommandExecutor {

    private final Plugin plugin;

    public ResetTutorialCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cCette commande doit être exécutée par un joueur.");
            return true;
        }

        // Demander confirmation
        if (args.length == 0 || !args[0].equalsIgnoreCase("confirm")) {
            player.sendMessage("§c§l⚠ ATTENTION ⚠");
            player.sendMessage("§eCette commande va DÉTRUIRE complètement Tutorial Island!");
            player.sendMessage("§eCoordonnées: (0, -60, 500) - Rayon: 60 blocs");
            player.sendMessage("§eNettoie de Y=-65 à Y=100 (toutes hauteurs)");
            player.sendMessage("");
            player.sendMessage("§7Pour confirmer, tapez: §e/resettutorial confirm");
            return true;
        }

        player.sendMessage("§c[ResetTutorial] Destruction de l'île en cours...");
        player.sendMessage("§7Cela peut prendre 5-15 secondes...");

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                var weWorld = BukkitAdapter.adapt(player.getWorld());

                try (EditSession session = WorldEdit.getInstance()
                        .newEditSessionBuilder()
                        .world(weWorld)
                        .fastMode(true)
                        .limitUnlimited()
                        .build()) {

                    // Coordonnées Tutorial Island - NETTOYER TOUTES LES HAUTEURS
                    int cx = 0;
                    int cz = 500;
                    int radius = 60; // Rayon élargi pour tout détruire

                    plugin.getLogger().info(
                            "[ResetTutorial] Destruction complète zone (" + cx + ", toutes hauteurs, " + cz + ") rayon "
                                    + radius);

                    // Détruire TOUTES les hauteurs de Y=-65 à Y=100
                    for (int x = -radius; x <= radius; x++) {
                        for (int z = -radius; z <= radius; z++) {
                            for (int y = -65; y <= 100; y++) { // TOUT nettoyer de -65 à 100
                                double distance = Math.sqrt(x * x + z * z);
                                if (distance <= radius) {
                                    session.setBlock(BlockVector3.at(cx + x, y, cz + z), BlockTypes.AIR);
                                }
                            }
                        }
                    }

                    session.flushQueue();

                    Bukkit.getScheduler().runTask(plugin, () -> {
                        player.sendMessage("§a✔ Tutorial Island détruite avec succès!");
                        player.sendMessage("§7Zone nettoyée: " + (radius * 2) + "x" + (radius * 2) + " blocs");
                        player.sendMessage("§7Vous pouvez maintenant reconstruire avec: §e/tutorial");
                    });

                    plugin.getLogger().info("[ResetTutorial] Île détruite!");

                } catch (Exception e) {
                    plugin.getLogger().severe("[ResetTutorial] Erreur: " + e.getMessage());
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        player.sendMessage("§c✘ Erreur lors de la destruction: " + e.getMessage());
                    });
                }

            } catch (Exception e) {
                plugin.getLogger().severe("[ResetTutorial] Erreur critique: " + e.getMessage());
                e.printStackTrace();
            }
        });

        return true;
    }
}
