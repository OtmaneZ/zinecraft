# Tutorial Island - Architecture Modulaire

## Vue d'ensemble

Tutorial Island est la deuxième zone de ZineCraft (Priority 1), construite avec la même architecture modulaire que le Village. L'île est surélevée à Y=70, circulaire (100x100), et contient 5 structures principales.

## Structures créées

### 1. **SpawnPointBuilder** - Point d'apparition

- **Fichier**: `SpawnPointBuilder.java`
- **Rayon**: 3 blocs
- **Composants**:
  - Base beacon 3x3 en gold blocks
  - Beacon actif au centre
  - Cercle décoratif en glowstone (rayon 3, tous les 45°)
  - Panneau bienvenue (nord)
  - Coffre starter (ouest)
  - 4 fleurs décoratives (Poppy, Dandelion, Blue Orchid, Allium)

### 2. **CombatArenaBuilder** - Arène de combat

- **Fichier**: `CombatArenaBuilder.java`
- **Dimensions**: 20x20 blocs
- **Rayon**: 10 blocs
- **Composants**:
  - Sol en stone bricks (20x20)
  - Bordure en cobblestone walls (1 bloc de hauteur)
  - Spawner de zombies au centre
  - 4 torches aux coins
  - Panneau d'instructions

### 3. **NPCPlatformBuilder** - Plateformes PNJ (x3)

- **Fichier**: `NPCPlatformBuilder.java`
- **Dimensions**: 5x5 blocs par plateforme
- **Rayon**: 3 blocs chacune
- **Types**: GUERRIER, MAGE, MARCHAND
- **Composants communs**:
  - Plateforme 5x5 en stone bricks
  - Bords en chiseled stone bricks
  - 4 torches aux coins
  - Panneau devant (sud)
- **Décorations spécifiques**:
  - **GUERRIER**: Anvil derrière
  - **MAGE**: Enchanting table + 2 bookshelves
  - **MARCHAND**: Barrel + 2 emerald blocks

### 4. **PortalBuilder** - Portail vers Village

- **Fichier**: `PortalBuilder.java`
- **Dimensions**: 4x5 blocs (largeur x hauteur)
- **Rayon**: 3 blocs
- **Composants**:
  - Cadre en obsidienne (colonnes verticales + haut/bas horizontal)
  - Intérieur en purple stained glass
  - Glowstone au sommet pour lumière
  - Panneau "Prêt?" devant

## Architecture du TutorialZoneBuilder

### Méthode `generate()`

```
1. buildIsland()      - Terrain circulaire Y=70
2. buildStructures()  - Placement de toutes les structures
3. flushQueue()       - Commit FAWE async
```

### Calcul des positions (buildStructures)

**Centre de l'île**: `(0, 70, 500)` selon map_to_code.md

**Distances calculées pour éviter chevauchement**:

```
SpawnPoint:      (0, 0)        - Centre, rayon 3
CombatArena:     (25, 0)       - EST, rayon 10, distance 25 = 3 + 10 + 12 marge
NPCGuerrier:     (-20, 0)      - OUEST, rayon 3
NPCMage:         (0, -20)      - NORD, rayon 3
NPCMarchand:     (15, -15)     - NORD-EST, rayon 3
Portal:          (0, 35)       - SUD, rayon 3, sortie vers village
```

**Logique de distance**:

- SpawnPoint au centre pour apparition joueur
- Arena à l'EST (25 blocs) = assez loin pour ne pas gêner spawn
- NPCs répartis en triangle (OUEST, NORD, NORD-EST) à ~20 blocs
- Portal au SUD (35 blocs) = sortie claire vers village

## Terrain de l'île (buildIsland)

**Forme**: Cercle de rayon 50 blocs (diamètre 100)
**Élévation**: Y=70 (10 blocs au-dessus du sol Y=60)

**Couches**:

- **Surface**: Grass blocks (Y=70)
- **Sous-sol**: 5 couches de dirt (Y=65 à Y=69)
- **Base**: 3 couches de stone (Y=62 à Y=64)

**Algorithme circulaire**:

```java
for (x, z) in [-50, 50]:
    if sqrt(x² + z²) <= 50:
        place blocks
```

## Commande associée

**TutorialCommand.java**:

- `/tutorial` - Génère l'île complète
- Alias: `/tuto`, `/tutoisland`
- Permission: `zinecraft.tutorial`
- Radius: 100 (size parameter)

## Comparaison avec Village

| Critère | Village | Tutorial Island |
|---------|---------|-----------------|
| **Terrain** | Flat terrain plat | Île circulaire surélevée |
| **Y Level** | -60 | 70 (+130 différence) |
| **Structures** | 6 types (15 houses) | 5 types (3 NPCs) |
| **Rayon zone** | 75 blocs | 50 blocs |
| **Builders** | 6 fichiers | 4 fichiers |
| **Complexité** | Élevée (grille maisons) | Moyenne (répartition simple) |

## État actuel

### ✅ Complété

- [x] SpawnPointBuilder.java (beacon + décor)
- [x] CombatArenaBuilder.java (20x20 arena)
- [x] NPCPlatformBuilder.java (3 plateformes paramétriques)
- [x] PortalBuilder.java (4x5 obsidian frame)
- [x] TutorialZoneBuilder.java refactoré (architecture modulaire)
- [x] Calculs de distance commentés
- [x] Logs informatifs pour debug

### ❌ À tester

- [ ] Compiler le plugin (bloqué par erreurs SkillCommand)
- [ ] Tester `/tutorial` in-game
- [ ] Vérifier espacement des structures
- [ ] Ajuster tailles si chevauchement
- [ ] Placer armor stands manuellement (PNJs)
- [ ] Configurer spawner zombies (faibles)

## Prochaines étapes

1. **Compilation**: Résoudre erreurs SkillCommand/WarriorSkills
2. **Test in-game**: `/tutorial` pour générer l'île
3. **Validation**: Vérifier positions, distances, sizes
4. **Itération**: Ajuster si nécessaire (comme village V1→V2)
5. **Décoration**: Ajouter armor stands avec commandes
6. **Zone 3**: Passer à Forest (0, -500) selon map_to_code.md

## Notes techniques

- **FAWE async**: Même pattern que Village (non-blocking)
- **EditSession**: Auto-close avec try-with-resources
- **AbstractStructureBuilder**: Héritage pour réduire duplication
- **Parametric builders**: Portal(width, height), Arena(width, depth)
- **Type-specific decoration**: NPCPlatformBuilder adapte décor selon type

## Map_to_code.md conformité

✅ **Respect du plan**:

- Coordonnées: (0, 500) ✓
- Île 100x100 ✓
- Beacon spawn ✓
- Arène 20x20 combat ✓
- 3 PNJs (Guerrier, Mage, Marchand) ✓
- Portal décoratif ✓
- Niveau 1-5 (à implémenter côté mobs)

**Différences**:

- Y=70 au lieu de "niveau mer" (meilleure visibilité)
- Portal en stained glass au lieu de nether portal (décoratif)
- Positions calculées mathématiquement (non spécifiées dans map)
