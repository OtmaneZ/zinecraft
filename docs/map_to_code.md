# 🗺️ MAP ZINECRAFT - Plan de Construction

**Projet**: Les Chroniques de ZineCraft (RPG Custom)
**Date**: 27 novembre 2025
**Monde**: Plat (flat world) - Coords centrées sur spawn 0,0

---

## 📍 LAYOUT GÉNÉRAL

```
                    NORD (-Z)
                        ↑
                        |
    [-500,-500]    [0,-500]    [500,-500]
    🏰 Château     🌲 Forêt     ⚔️ PvP
    LVL 50+        LVL 10-20    Tous niveaux
         |            |            |
         |            |            |
    [-500,0]       [0,0]       [500,0]
    🏔️ Glacé       🏠 SPAWN     🌋 Volcan
    LVL 20-30      LVL 5-10     LVL 40-50
         |            |            |
         |            |            |
    [-500,500]    [0,500]     [500,500]
    🏜️ Désert      🏝️ Tuto     💎 VIP
    LVL 30-40      LVL 1-5      Premium

OUEST (-X) ←──────────┼──────────→ EST (+X)
                       |
                       ↓
                    SUD (+Z)
```

---

## 🏗️ ZONES À CONSTRUIRE (Ordre de priorité)

### ✅ PRIORITÉ 1: BASE (Démo fils demain)

#### 🏘️ **VILLAGE DE DÉPART** - Coords: `0, 0`

**Niveau**: 5-10 | **Taille**: 150x150 blocs | **Temps**: 5-8h

**Structures essentielles**:

```
1. MAIRIE (Spawn principal)
   - 20x20 blocs
   - Pierre taillée, bois de chêne
   - Beacon au centre
   - Panneau: "Bienvenue dans les Chroniques de ZineCraft"

2. SHOP D'ARMES
   - 10x10 blocs
   - PNJ: Forgeron (ArmorStand custom)
   - Anvils, grindstones décoration
   - → ShopManager (futur)

3. SHOP D'ARMURES
   - 10x10 blocs
   - PNJ: Marchand
   - Item frames avec armures display

4. TAVERNE (Quêtes)
   - 15x12 blocs
   - Intérieur: tables, chaises, bar
   - PNJ: Tavernier (quêtes simples)
   - → QuestManager (futur)

5. BANQUE
   - 12x12 blocs
   - Coffres décoration
   - PNJ: Banquier
   - → EconomyManager (futur)

6. FORGE
   - 8x8 blocs
   - Furnaces, crafting tables
   - Lava décoration

7. 10-15 MAISONS décoratives
   - 6x6 blocs chacune
   - Variées (bois, pierre, mix)

8. MURAILLE DÉFENSIVE
   - Hauteur 8 blocs
   - Cobblestone + stone bricks
   - Torches tous les 5 blocs
   - 4 tours d'angle (12 blocs hauteur)

9. PLACE CENTRALE
   - Fontaine 5x5
   - Bancs (stairs)
   - Lampadaires (fences + lanterns)
   - Chemins pavés (stone bricks)
```

**Matériaux**:

- Cobblestone: ~5000 blocs
- Stone bricks: ~3000 blocs
- Oak wood: ~2000 blocs
- Glass: ~500 blocs
- Torches: ~300
- Lanterns: ~100

**Panneaux directionnels**:

```
Nord: "Forêt Mystique (LVL 10+)"
Est: "Volcan Infernal (LVL 40+)"
Ouest: "Montagnes Glacées (LVL 20+)"
Sud: "Tutorial Island (LVL 1)"
```

**Commandes de construction rapide**:

```
/fill X1 Y Z1 X2 Y Z2 stone_bricks    (muraille)
/fill X1 Y Z1 X2 Y Z2 cobblestone     (fondations)
/setblock X Y Z torch                 (éclairage)
```

---

#### 🏝️ **TUTORIAL ISLAND** - Coords: `0, 500`

**Niveau**: 1-5 | **Taille**: 100x100 blocs | **Temps**: 2-3h

**Structures**:

```
1. ÎLE PRINCIPALE
   - Grass blocks: 80x80
   - Élévation: Y=70 (10 blocs au-dessus spawn)
   - Forme circulaire/organique

2. SPAWN POINT TUTO
   - Panneau géant: "BIENVENUE HÉROS!"
   - Beacon coloré
   - Coffre starter (épée bois, nourriture)

3. ZONE TUTORIEL COMBAT
   - Arène 20x20
   - 4-5 Zombies faibles (spawner)
   - Panneau: "Éliminez ces monstres!"

4. PNJ TUTORIELS
   - Guerrier (armor stand): Enseigne combat
   - Mage (armor stand): Explique magie
   - Marchand (armor stand): Montre craft

5. PORTAIL VERS VILLAGE
   - Nether portal décoratif
   - Commande TP: /spawn (vers 0,0)
   - Panneau: "Prêt? Direction le Village!"
```

**Matériaux**:

- Grass blocks: ~6400
- Stone: ~1000
- Glowstone: ~50
- Oak wood: ~500
- Signs: ~20

**Ambiance**:

- Ciel dégagé (permanent day)
- Particules tutorial (flower particles)
- Musique calme (note blocks optionnel)

---

### ⭐ PRIORITÉ 2: ZONES DÉBUTANT

#### 🌲 **FORÊT MYSTIQUE** - Coords: `0, -500`

**Niveau**: 10-20 | **Taille**: 200x200 blocs | **Temps**: 6-10h

**Environnement naturel**:

```
1. ARBRES GÉANTS
   - Dark oak logs: hauteur 15-25 blocs
   - Feuillage dense (dark oak leaves)
   - 30-40 arbres custom
   - Lianes suspendues

2. SOL FORESTIER
   - Podzol, coarse dirt, moss
   - Champignons géants (5-8 blocs)
   - Fleurs sombres (wither rose, allium)
   - Buissons (azalea, leaves)

3. AMBIANCE
   - Torches rares (zones sombres)
   - Brouillard (smoke particles)
   - Sons: disques "13" ou "ward"
```

**Structures**:

```
4. RUINES ANCIENNES
   - Stone bricks moussus
   - Spawners zombies (2-3)
   - Coffres loot tier 1
   - 5-6 ruines dispersées

5. CABANE SORCIÈRE
   - 8x8 blocs
   - Spruce wood + chaudrons
   - PNJ: Sorcière (quête)
   - Potion brewing stands

6. DONJON: TEMPLE DANS LES ARBRES
   Entrée:
   - Escalier spiral autour tronc géant
   - Porte dark oak + iron bars

   Étage 1 (Y=80-90):
   - Salle 15x15
   - Spawners araignées (2)
   - Puzzle: 4 leviers = porte
   - Coffres: iron gear

   Étage 2 (Y=95-105):
   - Salle 12x12
   - Spawners zombies (2)
   - Piège: tripwire + TNT
   - Coffres: gold gear

   Étage 3 - BOSS (Y=110-120):
   - Arène 20x20
   - Plateforme centrale
   - 4 colonnes (cover)
   - Boss: WOLF ALPHA (ton système boss)
   - Commande: /boss <custom wolf>
   - Récompense: Diamond sword, XP
```

**Événements compatibles**:

- Blood Moon → Terreur maximale
- Treasure Hunt → Coffre dans ruines

---

### ⭐ PRIORITÉ 3: ZONES INTERMÉDIAIRES

#### 🏔️ **MONTAGNES GLACÉES** - Coords: `-500, 0`

**Niveau**: 20-30 | **Taille**: 250x250 blocs | **Temps**: 8-12h

**Terrain**:

```
1. MONTAGNES
   - Ice, packed ice, blue ice
   - Hauteur: 80-120 blocs
   - 5-6 pics distincts
   - Snow layers partout

2. GROTTES DE GLACE
   - Tunnels naturels (8x8)
   - Stalactites ice (pointed dripstone)
   - Lac gelé intérieur
   - Spawners strays (2-3)

3. LAC GELÉ
   - 50x50 blocs
   - Blue ice surface
   - Eau dessous (piège)
   - Poissons congelés (décoration)
```

**Structures**:

```
4. FORTERESSE DE GLACE (Donjon)
   Extérieur:
   - Château 40x40 base
   - 4 tours (hauteur 30 blocs)
   - Packed ice + snow blocks
   - Pont ice sur fossé

   Niveau 1 - Entrée (Y=70-80):
   - Hall 20x30
   - Spawners strays (2)
   - Coffres: diamond tier
   - Puzzle: pressure plates + ice

   Niveau 2 - Cryptes (Y=55-65):
   - 6 salles interconnectées
   - Spawners creepers (2)
   - Piège: lave sous ice
   - Coffres secrets dans murs

   Niveau 3 - BOSS (Y=85-100):
   - Tour centrale ouverte
   - Plateforme ice 20x20
   - Pas de cover (dur!)
   - Boss: ICE_GOLEM (existant)
   - Récompense: Netherite gear, XP massif

5. ZONE VIP SECRÈTE
   - Grotte cristaux (amethyst)
   - Accessible uniquement VIP
   - Coffres récompenses quotidiennes
   - Spawn familiers rares

6. CABANE CHAUDE (Point repos)
   - 10x10 blocs
   - Campfires, beds
   - Furnaces pour craft
   - PNJ: Ermite (quête)
```

**Défis spéciaux**:

- Glace glissante (blue ice paths)
- Chutes de neige permanentes
- Mobs cachés dans neige

---

#### 🏜️ **DÉSERT MAUDIT** - Coords: `-500, 500`

**Niveau**: 30-40 | **Taille**: 300x300 blocs | **Temps**: 10-15h

**Environnement**:

```
1. DÉSERT
   - Sand, red sand mix
   - Dead bushes partout
   - Cactus clusters
   - Fossiles apparents (bone blocks)

2. TEMPÊTE DE SABLE
   - Particules sand permanentes
   - Visibilité réduite
   - Effets sonores vent

3. CRATÈRES DE MÉTÉORES
   - 5-6 cratères (10-15 blocs rayon)
   - Obsidienne, netherrack
   - Minerais rares (diamond, ancient debris)
   - → Événement Meteor Strike!
```

**Structures**:

```
4. PYRAMIDE MASSIVE (Donjon principal)
   Base: 50x50 blocs
   Hauteur: 40 blocs

   Extérieur:
   - Sandstone smooth
   - Sphinx entrée (guardian statue)
   - Obélisques 4 coins

   Niveau -1 (Sous-sol Y=60-70):
   - Catacombes labyrinthes
   - Spawners husks (4-5)
   - Pièges: sand piège (fall damage)
   - Coffres funéraires

   Niveau 0 (Rez-de-chaussée Y=70-80):
   - Grande salle trône
   - Sarcophages (spawners)
   - Puzzle: redstone torches
   - Coffres royaux

   Niveau 1 (Chambre trésor Y=85-95):
   - Salle centrale
   - TNT pièges everywhere
   - Spawners zombies/skeletons (6)
   - Coffres trésor massifs

   Sommet - BOSS (Y=100-110):
   - Plateforme ouverte
   - Vue désert complet
   - Boss: DEMON_BLAZE (existant)
   - Récompense: Armes légendaires

5. VILLAGE ABANDONNÉ
   - 8-10 maisons effondrées
   - Sandstone ruins
   - Spawners random
   - Coffres cachés
   - Lore: "Ils ont fui..."

6. OASIS (Point repos)
   - Lac 20x20
   - Palmiers (jungle wood custom)
   - Verdure (grass, flowers)
   - PNJ: Nomade (vend potions)
```

**Particularités**:

- Hostile mobs jour ET nuit
- Husks (zombies désert) majoritaires
- Sandstorm effect (slowness zones)

---

### 🔥 PRIORITÉ 4: ZONES AVANCÉES

#### 🌋 **VOLCAN INFERNAL** - Coords: `500, 0`

**Niveau**: 40-50 | **Taille**: 300x300 blocs | **Temps**: 12-18h

**Structure principale**:

```
1. VOLCAN EXTÉRIEUR
   - Base: 120 blocs diamètre
   - Hauteur: 120 blocs (jusqu'à Y=170)
   - Matériaux: netherrack, basalt, blackstone

   Pente:
   - Escalier spiral externe
   - Magma blocks (damage)
   - Lava falls décoratifs
   - Smoke particles partout

   Cratère (sommet):
   - 40 blocs diamètre
   - Lava lake central
   - Plateforme boss (voir donjon)

2. TUNNELS INTÉRIEURS
   - 10+ tunnels interconnectés
   - Hauteur 5x5 blocs
   - Magma blocks, netherrack
   - Lava rivers (ponts étroits)
   - Spawners blazes (5-6)
```

**Donjon: Cœur du Volcan**:

```
Niveau 1 - Entrée (Y=80-90):
- Salle 25x25
- Passerelles au-dessus lava
- Spawners magma cubes (3)
- Puzzle: nether wart activation

Niveau 2 - Forges (Y=100-110):
- 4 salles forges
- Furnaces, blast furnaces
- Spawners blazes (4)
- Craft items légendaires
- PNJ: Maître Forgeron

Niveau 3 - Ascension (Y=120-140):
- Escalier spiral central
- Pas de rambardes (danger!)
- Spawners ghasts (2)
- Lava partout

Sommet - BOSS (Y=150-160):
- Plateforme 30x30 au-dessus cratère
- Netherite blocks décoration
- Aucun cover
- Boss: FIRE_DRAGON (existant)
- Mécanique: vol + lava breath
- Récompense: Dragon Egg, stuff ultime
```

**Structures annexes**:

```
3. FORGE LÉGENDAIRE
   - Bâtiment 15x15
   - Accessible post-boss
   - Craft: Netherite + custom enchants
   - PNJ: Ancien Forgeron

4. CAMPEMENT MERCENAIRES
   - 5-6 tentes
   - Point repos
   - Shop: potions fire resistance
   - PNJ: Vétérans (quêtes difficiles)
```

**Événements spéciaux**:

- Blood Moon ici = ENFER total
- Boss Invasion = 3 boss simultanés

---

### 👑 PRIORITÉ 5: ZONES ENDGAME

#### 🏰 **CHÂTEAU DU SEIGNEUR** - Coords: `-500, -500`

**Niveau**: 50+ | **Taille**: 400x400 blocs | **Temps**: 20-30h

**MEGA STRUCTURE**:

```
1. CHÂTEAU PRINCIPAL
   Base: 80x80 blocs
   Hauteur: 100 blocs
   Style: Gothique sombre

   Matériaux:
   - Stone bricks (dark variants)
   - Obsidian accents
   - End rods lighting
   - Soul lanterns

   Architecture:
   - 5 tours (hauteur 100 blocs chacune)
   - Tour centrale (120 blocs)
   - Pont-levis (functional redstone)
   - Douves lava 10 blocs large

2. EXTÉRIEUR FORTIFIÉ
   - Muraille 20 blocs hauteur
   - 12 tours garde (30 blocs)
   - 4 portes (Nord, Sud, Est, Ouest)
   - Spawners PARTOUT (husks, zombies, skeletons)

3. JARDIN MAUDIT
   - 50x50 blocs
   - Dead bushes, wither roses
   - Statues corrompues
   - Spawners wraiths (custom mobs?)
```

**Donjon Multi-Niveaux**:

```
Niveau -2 - CRYPTES (Y=50-60):
- Labyrinthes 100+ salles
- Spawners mass (10+)
- Coffres loot partout
- Puzzle: 12 leviers combo

Niveau -1 - DONJONS (Y=60-70):
- Cellules 20+
- Boss mini: Geôlier (Iron Golem custom)
- Spawners illagers (4)
- Clé pour niveau 0

Niveau 0 - HALL D'ENTRÉE (Y=70-80):
- Salle massive 40x40
- Escalier double vers trône
- Spawners vindicators (4)
- PNJ: Gardes (combats obligatoires)

Niveau 1 - BIBLIOTHÈQUE (Y=85-95):
- Livres lore histoire
- Puzzle: enchanting table code
- Spawners witches (3)
- Clé tour centrale

Niveau 2 - SALLES ROYALES (Y=100-110):
- 8 salles luxueuses
- Chaque salle = mini-boss
- Spawners variés
- Coffres récompenses massives

Niveau 3 - SALLE DU TRÔNE (Y=115-125):
- 50x50 blocs
- Trône obsidienne central
- 7 piédestaux (Cristaux de Pouvoir)
- Boss: SHADOW_TITAN (existant)
- Mécanique: Téléportation, shadows
- 4 Phases combat
- Récompense ULTIME: Fin de l'histoire

Tour Centrale - RÉCOMPENSE (Y=130-140):
- Salle des 7 Cristaux
- Display réussite joueur
- Portail vers Spawn
- Titre: "Sauveur de ZineCraft"
```

**Phases Boss Final**:

```
Phase 1 (100% HP): Normal attacks
Phase 2 (75% HP): Summon shadows (zombies)
Phase 3 (50% HP): Teleportation rapide
Phase 4 (25% HP): ENRAGE - double damage

Mécaniques:
- Éviter téléportations
- Kill shadows rapidement
- Potion healing obligatoire
- Team 3-5 joueurs recommandé
```

**Récompenses finales**:

```
- Dragon Egg unique
- Elytra custom "Ailes du Héros"
- Netherite armor "Set du Sauveur"
- Title: [HÉROS] dans chat
- Accès zone secrète post-game
- 10,000 gold (économie)
- XP massif (50 levels)
```

---

### 💎 **ZONE VIP PREMIUM** - Coords: `500, 500`

**Niveau**: Tous (VIP required) | **Taille**: 200x200 blocs | **Temps**: 6-10h

**Ville Luxueuse**:

```
1. ARCHITECTURE PREMIUM
   - Quartz blocks, gold blocks
   - Glazed terracotta accents
   - Sea lanterns lighting
   - Beacon pyramids (4 corners)

2. MAISONS VIP PERSONNALISABLES
   - 10 parcelles 20x20
   - Joueurs VIP peuvent build
   - Protections (claims)
   - Coffres personnels

3. SHOP COSMÉTIQUES
   - Bâtiment 25x25 quartz
   - PNJ: Marchand Luxe
   - Vente: Pets rares, auras, skins
   - → ShopManager + EffectManager

4. ARÈNE PVP PRIVÉE
   - 40x40 blocs
   - Gradins luxueux
   - Systèmes paris (gold)
   - Classements VIP only

5. ZOO FAMILIERS
   - Enclos 10x10 chacun
   - Display tous pets disponibles
   - PNJ: Dresseur
   - → PetManager (existant)

6. TÉLÉPORTEURS RAPIDES
   - 8 portails (1 par zone)
   - Instant TP (commande VIP)
   - Pas de cooldown VIP+

7. COFFRES RÉCOMPENSES
   - Daily rewards VIP
   - Weekly rewards VIP+
   - Monthly rewards LEGEND
   - Loot: cosmétiques, gold, XP boost

8. SALLE DES COMMANDES
   - Liste commandes VIP
   - /fly autorisé ici seulement
   - /heal, /feed cooldown réduit
```

**Zones Exclusives par Grade**:

```
VIP (15€):
- Maison + parcelle
- Téléporteurs
- Shop accès

VIP+ (30€):
- Tout VIP +
- Arène privée
- Coffres weekly
- Zone crafting avancé

LEGEND (60€):
- Tout VIP+ +
- Trône personnel
- Statue custom
- Salon privé
- Coffres monthly
```

---

### ⚔️ **ARÈNE PVP** - Coords: `500, -500`

**Niveau**: Tous | **Taille**: 150x150 | **État**: Déjà construite! ✅

**Améliorations à ajouter**:

```
1. GRADINS SPECTATEURS
   - 3 niveaux (Y=100, 105, 110)
   - Stairs + slabs
   - 50+ places assises
   - Vue parfaite sur arène

2. SALLES D'ATTENTE
   - 2 salles (team rouge, team bleue)
   - Coffres équipement
   - Respawn points

3. SYSTÈME CLASSEMENT
   - Signs avec Top 10
   - Stats: Kills, Deaths, K/D
   - Reset monthly

4. RÉCOMPENSES AUTO
   - Win = +50 gold
   - Kill = +10 gold
   - Streak bonus

5. MODES DE JEU
   - 1v1 (existant)
   - 2v2 (existant)
   - FFA (Free For All) - nouveau
   - Capture Flag - nouveau
```

---

## 🎨 PALETTE DE MATÉRIAUX PAR ZONE

```
🏘️ Village:       Stone, Oak, Cobblestone, Torches
🏝️ Tutorial:      Grass, Stone, Glowstone, Oak
🌲 Forêt:         Dark Oak, Podzol, Moss, Mushrooms
🏔️ Montagnes:     Ice, Snow, Quartz, Blue Ice
🏜️ Désert:        Sand, Sandstone, Terracotta, Bone
🌋 Volcan:        Netherrack, Basalt, Magma, Lava
🏰 Château:       Stone Bricks, Obsidian, End Rods, Soul
💎 VIP:           Quartz, Gold, Sea Lanterns, Glazed
⚔️ PvP:           Blackstone, Polished, Red, Blue
```

---

## 🛠️ COMMANDES WORLDEDIT ESSENTIELLES

```bash
# Sélection
//wand                    # Baguette sélection (axe bois)
//pos1                    # Point 1
//pos2                    # Point 2

# Remplissage
//set <block>             # Remplir sélection
//replace <old> <new>     # Remplacer blocs
//walls <block>           # Murs seulement
//faces <block>           # Faces extérieures

# Formes
//sphere <block> <rayon>  # Sphère
//hcyl <block> <r> <h>    # Cylindre creux
//pyramid <block> <size>  # Pyramide
//hpyramid <block> <size> # Pyramide creuse

# Copier/Coller
//copy                    # Copier sélection
//cut                     # Couper sélection
//paste                   # Coller
//rotate <angle>          # Rotation
//flip <direction>        # Miroir

# Terrain
//smooth <iterations>     # Lisser terrain
//naturalize               # Rendre naturel (dirt + grass)
//flora <density>         # Ajouter végétation

# Utiles
//undo                    # Annuler
//redo                    # Refaire
//drain <radius>          # Vider eau/lava
//fixlava <radius>        # Fixer lava statique
```

---

## 🎯 SCRIPT DE CONSTRUCTION AUTOMATIQUE

### Village Spawn (coords 0,0)

```bash
# Téléportation
/tp @s 0 70 0

# Sol place centrale (20x20)
/fill -10 69 -10 10 69 10 stone_bricks

# Fontaine centre (5x5)
/fill -2 69 -2 2 69 2 water
/fill -2 70 -2 2 72 2 stone_bricks hollow

# Muraille périphérique (75 blocs rayon)
/fill -75 70 -75 -75 77 75 cobblestone
/fill 75 70 -75 75 77 75 cobblestone
/fill -75 70 -75 75 77 -75 cobblestone
/fill -75 70 75 75 77 75 cobblestone

# Éclairage muraille (torches chaque 5 blocs)
/execute at @s run fill -75 78 -75 75 78 75 torch[facing=up] replace air

# Mairie (20x20 au nord)
/fill -10 70 -30 10 80 -10 oak_planks hollow
/fill -10 80 -30 10 81 -10 oak_stairs
/setblock 0 70 -20 beacon

# Tours d'angle (12 blocs hauteur)
/fill -75 70 -75 -70 82 -70 stone_bricks
/fill 70 70 -75 75 82 -70 stone_bricks
/fill -75 70 70 -70 82 75 stone_bricks
/fill 70 70 70 75 82 75 stone_bricks
```

### Forêt Mystique (coords 0,-500)

```bash
# Téléportation
/tp @s 0 70 -500

# Sol forestier (200x200)
/fill -100 69 -600 100 69 -400 podzol

# Arbres géants (commande répétée 30x)
/fill X 70 Z X 95 Z dark_oak_log
/fill X-5 95 Z-5 X+5 100 Z+5 dark_oak_leaves

# Ruines (spawner + coffres)
/setblock X 70 Z spawner{SpawnData:{entity:{id:"zombie"}}}
/setblock X 70 Z chest{LootTable:"minecraft:chests/simple_dungeon"}
```

### Pyramide Désert (coords -500,500)

```bash
# Téléportation
/tp @s -500 70 500

# Base pyramide (50x50)
/fill -525 70 475 -475 70 525 smooth_sandstone

# Pyramide creuse (commande itérative)
/execute at @s run fill -525 70 475 -475 110 525 smooth_sandstone hollow

# Chambre boss (sommet)
/fill -510 105 490 -490 110 510 air
/setblock -500 105 500 spawner{SpawnData:{entity:{id:"blaze"}}}
```

---

## 📅 PLANNING DE CONSTRUCTION (Optimisé)

### **Jour 1: Bases** (8h)

- ✅ Village spawn complet (5h)
- ✅ Tutorial island (2h)
- ✅ Chemins entre zones (1h)

### **Jour 2-3: Forêt** (12h)

- Environnement forestier (4h)
- Ruines + cabane (3h)
- Donjon temple arbres (5h)

### **Jour 4-5: Montagnes** (16h)

- Terrain montagneux (6h)
- Grottes ice (4h)
- Forteresse + boss room (6h)

### **Jour 6-7: Désert** (20h)

- Environnement désert (5h)
- Village abandonné (3h)
- Pyramide massive (12h)

### **Jour 8-10: Volcan** (24h)

- Structure volcan (8h)
- Tunnels intérieurs (6h)
- Donjon cœur + boss (10h)

### **Jour 11-15: Château** (40h)

- Extérieur château (12h)
- Donjons multi-niveaux (20h)
- Boss final + récompenses (8h)

### **Jour 16: VIP + PvP** (10h)

- Zone VIP complète (6h)
- Amélioration arène PvP (4h)

**TOTAL: ~130 heures = 3-4 semaines (1h/jour) OU 2 semaines (4h/jour)**

---

## 🎮 INTÉGRATION AVEC CODE (Copilot PC)

### Pendant que tu build, Copilot PC code

```java
// ClassManager.java
- PNJ Village: Choix classe au spawn
- Classes: Guerrier, Archer, Mage, Paladin, Assassin, Nécro, Druide, Archimage

// LevelManager.java
- XP par kill mob (varie selon zone)
- Level up = skill points
- Level cap selon VIP (30/50/75/100+)

// QuestManager.java
- PNJ Taverne: Quêtes simples (kill, collect)
- PNJ Forêt: Quêtes exploration
- PNJ chaque zone: Quêtes storyline

// EconomyManager.java
- Drops mobs = gold
- Shops villages = achats
- Banque = stockage

// ShopManager.java
- Shop armes village
- Shop cosmétiques VIP
- Shop potions zones
```

### Points de synchronisation

```
1. Village construit → ClassManager + ShopManager
2. Forêt finie → QuestManager (quêtes forêt)
3. Chaque zone → LevelManager (XP scaling)
4. Donjons → Boss integration (déjà fait!)
5. Zone VIP → ShopManager cosmétiques
```

---

## 🎬 TESTS AVEC ÉVÉNEMENTS (Moi)

### Pendant ta construction, teste

```bash
# Forêt Mystique
/event start blood_moon
→ Ambiance terreur maximale

# Désert Maudit
/event start meteor_strike
→ Cratères naturels

# Volcan Infernal
/event start boss_invasion
→ 5 boss simultanés CHAOS

# Château
/event start chaos_storm
→ Test difficulté extrême

# Zone VIP
/effect set rainbow_trail
→ Showcase effets premium
```

---

## 🚀 PROCHAINES ÉTAPES IMMÉDIATES

### MAINTENANT (pour démo fils demain)

1. **Village Spawn** (2-3h minimum)
   - Spawn point beau
   - 3-4 bâtiments clés
   - Muraille basique
   - Éclairage

2. **Petite forêt** (1h)
   - 10-15 arbres custom
   - Ambiance sombre
   - Test `/event start blood_moon`

3. **Tester boss in-game**
   - Spawn boss dans différentes zones
   - Filmer avec auras (`/effect set fire_aura`)
   - Screenshots épiques

### APRÈS-DEMAIN

4. **Tutorial Island** (2h)
5. **Compléter Village** (3h)
6. **Donjon Forêt** (4-5h)

---

## 💡 ASTUCES PRO

### Construction rapide

- Utilise **structure blocks** pour sauvegarder bâtiments
- Copie/colle avec WorldEdit
- Templates de maisons (5-6 designs)

### Décoration

- Armor stands custom = PNJ
- Item frames = décoration murale
- Banners = drapeaux zones
- Note blocks = ambiance sonore

### Optimisation

- Limite spawners par chunk (max 3-4)
- Pas trop de entities (lag)
- Lighting optimal (torches chaque 10 blocs)

### Storyline

- Livres dans coffres (lore histoire)
- Panneaux avec dialogues PNJ
- Easter eggs cachés partout

---

## 📊 PROGRESSION TRACKING

```
[ ] Tutorial Island (1-5)
[ ] Village Spawn (5-10)
[ ] Forêt Mystique (10-20)
[ ] Montagnes Glacées (20-30)
[ ] Désert Maudit (30-40)
[ ] Volcan Infernal (40-50)
[ ] Château du Seigneur (50+)
[ ] Zone VIP Premium
[ ] Arène PvP (améliorations)
```

**Coche au fur et à mesure! ✅**

---

**LET'S BUILD! 🏗️🔥**

*"Le monde de ZineCraft t'attend, Héros!"*
