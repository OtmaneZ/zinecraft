# 🏜️ Désert Mortel - Documentation

## Vue d'ensemble

Le **Désert Mortel** est une zone hostile de niveau 30-40 située aux coordonnées **`-500, 65, 500`** avec une **tempête de sable permanente** et une **pyramide massive** comme donjon principal.

---

## 🎯 Caractéristiques

### 📍 Localisation
- **Coordonnées**: `-500, 65, 500` (Sud-Ouest de la map)
- **Taille**: 300x300 blocs (rayon 150)
- **Niveau requis**: 30-40
- **Temps de construction**: 2-5 minutes (génération automatique)

### ⚠️ Dangers
1. **Tempête de sable permanente**
   - Particules de sable continues
   - Effet Slowness I permanent
   - Dégâts périodiques (1 HP toutes les 10s)
   - Visibilité réduite

2. **Mobs hostiles 24h/24**
   - Husks (zombies du désert) majoritaires
   - Spawners dans toutes les structures
   - Pas de répit jour/nuit

3. **Environnement hostile**
   - Cactus partout
   - Cratères de météorites
   - Pièges dans la pyramide

---

## 🏛️ Structures

### 1. **Pyramide Massive** (Structure principale)

**Dimensions**: 50x50 base, 40 blocs de hauteur

#### Niveaux:

**Sous-sol (-10 blocks)** - Catacombes
- Labyrinthe de salles interconnectées
- 4 spawners husks
- Pièges à sable (fall damage)
- Coffres funéraires (loot tier 2)

**Rez-de-chaussée (Y+0 à Y+12)** - Salle du Trône
- Grande salle 36x36
- Sarcophages (spawners déguisés)
- Puzzle redstone (torches)
- Trône en quartz
- Coffres royaux (loot tier 3)

**Étage 1 (Y+15 à Y+25)** - Chambre au Trésor
- Salle 30x30
- Coffres massifs (10+)
- 4 spawners zombies/skeletons
- Pièges TNT partout

**Sommet (Y+40)** - Plateforme Boss
- Plateforme ouverte 30x30
- Vue panoramique sur le désert
- 4 piliers avec glowstone
- Zone centrale en red sandstone
- **Boss: DEMON_BLAZE**

#### Extérieur:
- **Sphinx géant** à l'entrée (garde l'accès)
- **4 Obélisques** aux coins (20 blocs de haut)
- Entrée principale 6 blocs de large

---

### 2. **Cratères de Météorites** (6 cratères)

- Rayon: 12 blocs chacun
- Profondeur: 5 blocs
- Matériaux:
  - Centre: Obsidienne
  - Milieu: Netherrack
  - Bords: Sable brûlé
  - **Minerais rares**: Diamond ore, Ancient debris (spawns aléatoires)

**Positions**:
- Cratère 1: `-470, 63, 520`
- Cratère 2: `-530, 63, 480`
- Cratère 3: `-450, 63, 540`
- Cratère 4: `-540, 63, 460`
- Cratère 5: `-430, 63, 510`
- Cratère 6: `-520, 63, 440`

---

### 3. **Village Abandonné**

**Position**: `-600, 65, 420`

- 10 maisons en ruines (partiellement détruites)
- Structure: Sandstone
- État: 70% détruit (aléatoire)
- Loot: Coffres cachés (33% de chance par maison)
- Spawners: 25% de chance par maison
- Ambiance: Désolation, panneaux "Ils ont fui..."

---

### 4. **Oasis** (Point de repos)

**Position**: `-420, 65, 600`

**Caractéristiques**:
- Lac circulaire 20x20 blocs
- Eau + blocs d'argile
- 4 palmiers (jungle wood custom)
- Herbe et fleurs autour
- **Petite cabane** (7x7 acacia planks)
- **PNJ: Nomade** (marchand de potions)
  - Vend: Fire Resistance, Regeneration, Speed
  - Prix: 50-100 Zines par potion

**Sécurité**: Zone sans mobs (radius 30 blocs)

---

## 🎮 Commandes

### Pour les joueurs:
```
/tpdesert          # Se téléporter au désert (niveau 30+ requis)
/gotodesert        # Alias de /tpdesert
/deserttp          # Alias de /tpdesert
```

### Pour les admins:
```
/desert            # Générer la zone complète du désert
/deadlydesert      # Alias de /desert
/desertmortel      # Alias de /desert
```

---

## 💀 Boss: DEMON_BLAZE

**Localisation**: Sommet de la pyramide (`-500, 110, 500`)

**Caractéristiques**:
- Type: Blaze amélioré
- HP: ???
- Attaques:
  - Boules de feu multiples
  - Explosion de flammes (zone)
  - Invocation de mini-blazes
  
**Récompenses**:
- Armes légendaires
- Armure custom
- XP massif
- Accès à la Forge Légendaire (future feature)

**Stratégie**:
- Plateforme ouverte (pas de cover!)
- Résistance au feu recommandée
- Combat en groupe conseillé (3-5 joueurs)

---

## 🎨 Ambiance

### Effets visuels:
- Particules de sable (FALLING_DUST)
- Particules de nuages (CLOUD)
- Couleur sable rouge + jaune mélangé
- Dead bushes partout
- Fossiles (bone blocks) éparpillés

### Effets sonores:
- Vent continu (ITEM_ELYTRA_FLYING)
- Ambiance désertique
- Sons de tempête

### Météo:
- Ciel clair (pas de pluie possible)
- Ambiance chaude et oppressante

---

## 📊 Système de Tempête de Sable

**Code**: `SandstormManager.java`

**Fonctionnement**:
- Détection automatique des joueurs dans la zone
- Vérification toutes les 2 secondes (40 ticks)
- Application des effets:
  - Slowness I (5 secondes)
  - 1 HP dégâts (25% chance/check)
  - Particules continues
  - Messages d'avertissement

**Activation**: Automatique au lancement du serveur

**Désactivation**: Automatique à l'arrêt du serveur

---

## 🏗️ Génération Automatique

**Builder**: `DeadlyDesertZoneBuilder.java`

**Étapes de construction**:
1. Terraforming - Sol désertique (sable + red sand)
2. Cratères de météorites (6 cratères)
3. Pyramide massive (4 niveaux)
4. Village abandonné (10 maisons)
5. Oasis (lac + palmiers + cabane)
6. Décoration (200+ cactus, dead bushes, fossiles)

**Performance**:
- Génération asynchrone (FAWE)
- Pas de lag serveur
- Temps: 2-5 minutes
- ~50,000 blocs placés

---

## 🎯 Progression Recommandée

### Niveau 30-32 (Débutant)
- Explorer les cratères de météorites
- Farmer dans le village abandonné
- Éviter la pyramide

### Niveau 33-36 (Intermédiaire)
- Sous-sol de la pyramide (catacombes)
- Rez-de-chaussée (salle du trône)
- Se reposer à l'oasis

### Niveau 37-40 (Avancé)
- Chambre au trésor (étage 1)
- Préparation boss
- Combat DEMON_BLAZE

### Niveau 40+ (Expert)
- Farm boss pour loot légendaire
- Exploration complète
- Collection de minerais rares

---

## 💡 Conseils de Survie

1. **Apportez des potions**:
   - Fire Resistance (pour le boss)
   - Regeneration (dégâts tempête)
   - Speed (contre slowness)

2. **Équipement recommandé**:
   - Armure diamond minimum
   - Arme enchantée (Sharpness III+)
   - Nourriture en quantité
   - Torches (pyramide sombre)

3. **Stratégies**:
   - Voyagez en groupe (2-3 joueurs)
   - Utilisez l'oasis pour repos
   - Marquez votre chemin (torches)
   - Gardez une ender pearl (fuite d'urgence)

4. **À éviter**:
   - Explorer seul (première fois)
   - Entrer dans la pyramide sans préparation
   - Combattre le boss sous-équipé
   - Rester trop longtemps (dégâts cumulés)

---

## 🔮 Futures Améliorations

- [ ] Quêtes spécifiques au désert
- [ ] Forge Légendaire (post-boss)
- [ ] Événement "Meteor Strike" dynamique
- [ ] PNJ supplémentaires (nomades, marchands)
- [ ] Montures spéciales (chameaux?)
- [ ] Armes légendaires "du Désert"
- [ ] Achievements du désert

---

## 🐛 Bugs Connus

Aucun pour le moment.

Si vous trouvez un bug, veuillez le signaler dans les issues GitHub.

---

**Créé par**: Otmane & GitHub Copilot  
**Date**: 3 décembre 2025  
**Version**: 1.0.0
