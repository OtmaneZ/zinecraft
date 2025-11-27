package fr.zinecraft.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import fr.zinecraft.core.ZineCraftCore;
import fr.zinecraft.core.rpg.LevelManager;
import fr.zinecraft.core.rpg.PlayerManager;
import fr.zinecraft.core.rpg.RPGPlayer;

/**
 * Listener pour les boss
 * 
 * @author Otmane & Adam
 */
public class BossListener implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        // Vérifier si c'est un boss
        if (ZineCraftCore.getInstance().getBossManager().isBoss(event.getEntity())) {
            // Vérifier si tué par un joueur
            if (event.getEntity().getKiller() instanceof Player) {
                Player killer = event.getEntity().getKiller();
                
                // Donner XP bonus pour le boss
                LevelManager levelManager = ZineCraftCore.getInstance().getLevelManager();
                PlayerManager playerManager = ZineCraftCore.getInstance().getPlayerManager();
                
                if (levelManager != null && playerManager != null) {
                    RPGPlayer rpgPlayer = playerManager.getPlayer(killer);
                    if (rpgPlayer != null && rpgPlayer.hasChosenClass()) {
                        // Déterminer le type de boss (à partir du custom name)
                        String bossName = event.getEntity().getCustomName();
                        if (bossName != null) {
                            String bossType = "BOSS"; // Type par défaut
                            if (bossName.contains("Titan")) bossType = "TITAN";
                            else if (bossName.contains("Dragon")) bossType = "DRAGON";
                            else if (bossName.contains("Démon") || bossName.contains("Demon")) bossType = "DEMON";
                            else if (bossName.contains("Golem")) bossType = "GOLEM";
                            else if (bossName.contains("Phénix") || bossName.contains("Phoenix")) bossType = "PHOENIX";
                            else if (bossName.contains("Kraken")) bossType = "KRAKEN";
                            
                            int bossXP = levelManager.getBossKillXP(bossType);
                            levelManager.addExperience(killer, bossXP, "Boss Kill: " + bossType);
                            
                            // Incrémenter les stats
                            rpgPlayer.incrementBossesDefeated();
                            playerManager.savePlayer(rpgPlayer);
                        }
                    }
                }
                
                // Gérer la mort du boss
                ZineCraftCore.getInstance().getBossManager().onBossDeath(event.getEntity(), killer);
                
                // Empêcher le drop normal
                event.getDrops().clear();
                event.setDroppedExp(0);
            }
        }
    }
}
