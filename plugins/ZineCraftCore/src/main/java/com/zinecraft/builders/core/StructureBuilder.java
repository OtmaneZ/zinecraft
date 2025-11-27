package com.zinecraft.builders.core;

import com.sk89q.worldedit.EditSession;
import org.bukkit.Location;

/**
 * Interface de base pour tous les builders de structures
 */
public interface StructureBuilder {
    
    /**
     * Construit la structure de manière asynchrone avec FAWE
     * @param editSession Session FAWE pour placement async
     * @param center Point central de la structure
     */
    void build(EditSession editSession, Location center);
    
    /**
     * Retourne le nom de la structure
     */
    String getName();
    
    /**
     * Retourne les dimensions approximatives (rayon)
     */
    int getRadius();
}
