package com.zinecraft.commands;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Commande pour nettoyer/réinitialiser la zone du village
 * Supprime toutes les structures et recrée un terrain plat propre
 */
public class ResetVillageCommand implements CommandExecutor {

    private final Plugin plugin;

    public ResetVillageCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cCette commande doit être exécutée par un joueur.");
            return true;
        }

        // Coordonnées de la zone du village
        Location center = new Location(player.getWorld(), 0, -60, 0);
        int radius = 75;

        player.sendMessage("§e[ResetVillage] Nettoyage de la zone du village...");
        player.sendMessage("§7Zone: (-75, -60, -75) à (75, 100, 75)");
        player.sendMessage("§7Cela peut prendre quelques secondes...");

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                var weWorld = BukkitAdapter.adapt(player.getWorld());

                try (EditSession session = WorldEdit.getInstance()
                        .newEditSessionBuilder()
                        .world(weWorld)
                        .fastMode(true)
                        .limitUnlimited()
                        .build()) {

                    int cx = center.getBlockX();
                    int cy = center.getBlockY();
                    int cz = center.getBlockZ();

                    // 1. Nettoyer tout au-dessus du sol (Y=-59 à Y=100)
                    plugin.getLogger().info("[ResetVillage] Nettoyage des structures...");
                    BlockVector3 pos1 = BlockVector3.at(cx - radius, cy + 1, cz - radius);
                    BlockVector3 pos2 = BlockVector3.at(cx + radius, 100, cz + radius);
                    CuboidRegion region = new CuboidRegion(weWorld, pos1, pos2);

                    for (BlockVector3 pos : region) {
                        session.setBlock(pos, BlockTypes.AIR);
                    }

                    // 2. Recréer le sol plat en grass
                    plugin.getLogger().info("[ResetVillage] Recréation du sol plat...");
                    for (int x = cx - radius; x <= cx + radius; x++) {
                        for (int z = cz - radius; z <= cz + radius; z++) {
                            // Surface en grass
                            session.setBlock(BlockVector3.at(x, cy, z), BlockTypes.GRASS_BLOCK);

                            // Sous-sol en dirt (5 couches)
                            for (int dy = -5; dy < 0; dy++) {
                                session.setBlock(BlockVector3.at(x, cy + dy, z), BlockTypes.DIRT);
                            }
                        }
                    }

                    session.flushQueue();

                    plugin.getLogger().info("[ResetVillage] Zone nettoyée avec succès!");

                    // Message au joueur
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        player.sendMessage("§a[ResetVillage] Zone nettoyée avec succès!");
                        player.sendMessage("§7Vous pouvez maintenant utiliser /village pour régénérer.");
                    });
                }

            } catch (Exception e) {
                plugin.getLogger().severe("[ResetVillage] Erreur: " + e.getMessage());
                e.printStackTrace();

                Bukkit.getScheduler().runTask(plugin, () -> {
                    player.sendMessage("§c[ResetVillage] Erreur lors du nettoyage!");
                    player.sendMessage("§7Vérifiez les logs du serveur.");
                });
            }
        });

        return true;
    }
}
