# Refactoring Architecture - ZineCraft Builders

## 🎯 Objectif du Refactoring

Transformer le **VillageBuilder V0** (monolithique, synchrone, 426 lignes) en **architecture modulaire, async, scalable** pour supporter les 9 zones du plan `map_to_code.md`.

---

## 📁 Nouvelle Structure de Dossiers

```
plugins/ZineCraftCore/src/main/java/
├── fr/zinecraft/core/              # Code existant (RPG, boss, arènes, etc.)
└── com/zinecraft/                  # Nouvelle architecture builders
    ├── commands/
    │   └── VillageCommand.java     # Commande refactorisée
    └── builders/
        ├── core/                   # Interfaces & classes abstraites
        │   ├── StructureBuilder.java           # Interface de base
        │   └── AbstractStructureBuilder.java   # Classe abstraite helper
        ├── terrain/                # Terraforming & routes
        │   └── TerrainBuilder.java # Aplanissement, chemins
        ├── structures/             # Structures réutilisables
        │   ├── HouseBuilder.java   # Maisons (3 styles)
        │   ├── FountainBuilder.java # Fontaines
        │   └── MarketBuilder.java  # Marchés
        └── zones/                  # Orchestrateurs de zones
            └── VillageZoneBuilder.java # Zone Village Spawn
```

---

## 🏗️ Architecture des Composants

### 1️⃣ **Interface `StructureBuilder`** (Contrat de base)

```java
public interface StructureBuilder {
    void build(EditSession editSession, Location center);
    String getName();
    int getRadius();
}
```

**Rôle :** Définit le contrat que toutes les structures doivent respecter.

---

### 2️⃣ **Classe `AbstractStructureBuilder`** (Helpers réutilisables)

```java
public abstract class AbstractStructureBuilder implements StructureBuilder {
    protected void setBlock(EditSession session, int x, int y, int z, BlockType type);
    protected void fillCuboid(EditSession session, int x1, int y1, int z1, ...);
    protected BlockVector3 toBlockVector(Location center, int offsetX, ...);
}
```

**Rôle :** Fournit des méthodes utilitaires pour éviter la duplication de code.

---

### 3️⃣ **Builders de Structures Individuelles**

#### **HouseBuilder** (Maisons)

- **3 styles** : WOOD, STONE, BRICK
- **Paramétrable** : largeur, profondeur, hauteur
- **Méthodes modulaires** :
  - `buildWalls()` - Murs avec matériaux configurables
  - `buildWindows()` - Fenêtres en verre
  - `buildRoof()` - Toit en pente

#### **FountainBuilder** (Fontaines)

- Rayon paramétrable
- Bassin circulaire en quartz
- Pilier central avec lanterne
- Eau animée

#### **MarketBuilder** (Marchés)

- Nombre de stands configurable
- Disposition circulaire autour d'un centre
- Toits colorés (8 couleurs)
- Comptoirs en bois

**Avantages :**
✅ Réutilisables dans toutes les zones
✅ Testables individuellement
✅ Configurables via constructeur

---

### 4️⃣ **TerrainBuilder** (Terraforming)

```java
public static void flattenArea(EditSession session, Location center, int radius, int groundLevel);
public static void createPath(EditSession session, Location start, Location end, int width);
```

**Rôle :** Gère l'aplanissement du terrain et la création de routes.
**Optimisation :** Utilise FAWE EditSession pour placement async massif de blocs.

---

### 5️⃣ **VillageZoneBuilder** (Orchestrateur de zone)

```java
public class VillageZoneBuilder {
    private final List<StructureBuilder> structures;

    public void generate() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (EditSession session = WorldEdit.getInstance()
                    .newEditSessionBuilder()
                    .world(weWorld)
                    .fastMode(true)
                    .limitUnlimited()
                    .build()) {

                TerrainBuilder.flattenArea(session, center, radius, groundLevel);
                buildMainRoads(session);
                buildStructures(session);
                session.flushQueue();
            }
        });
    }
}
```

**Rôle :** Orchestre la génération complète de la zone Village Spawn.
**Architecture :**

1. **Terraforming** du terrain (100x100)
2. **Routes principales** (croix Nord-Sud / Est-Ouest)
3. **Structures** (fontaine, marché, 15 maisons)

**Optimisations :**
✅ **Async** : Tout s'exécute en arrière-plan (pas de freeze serveur)
✅ **FAWE EditSession** : Placement ultra-rapide de millions de blocs
✅ **Modulaire** : Chaque structure est indépendante

---

## 🔧 Utilisation

### Commande In-Game

```
/village
```

**Résultat :**

- Génère le Village Spawn à (0, -60, 0)
- Rayon : 50 blocs (100x100)
- Structures : fontaine + marché + 15 maisons (3 styles)
- Exécution async (pas de lag)

---

## 📊 Comparaison V0 vs V1

| Critère | VillageBuilder V0 | Architecture Refactorisée V1 |
|---------|-------------------|------------------------------|
| **Lignes de code** | 426 lignes (1 fichier) | ~150 lignes par classe (7 fichiers) |
| **Placement blocs** | Synchrone (5-10s freeze) | Async FAWE (instantané) |
| **Réutilisabilité** | ❌ Copier-coller | ✅ HouseBuilder réutilisable partout |
| **Scalabilité** | ❌ 1 zone uniquement | ✅ 9 zones (map_to_code.md) |
| **Testabilité** | ❌ Monolithique | ✅ Chaque builder testable séparément |
| **Maintenabilité** | ❌ Code mixé | ✅ Séparation des responsabilités |
| **Performance** | ⚠️ Lag serveur | ✅ Async, pas de lag |

---

## 🚀 Prochaines Étapes

### Immediate (Phase 1)

1. ✅ **Structure créée** - Dossiers + interfaces
2. ✅ **Builders modulaires** - House, Fountain, Market
3. ✅ **VillageZoneBuilder** - Orchestrateur async
4. ✅ **Compilation** - Gradle build successful
5. ⏳ **Test in-game** - Tester /village

### Court terme (Phase 2)

- Ajouter **ForgeBuilder** et **InnBuilder**
- Créer **TutorialZoneBuilder** (0, 500)
- Créer **ForestZoneBuilder** (0, -500)
- Système de **templates JSON** pour configurer les structures

### Moyen terme (Phase 3)

- Builders pour les 6 zones restantes (Montagne, Désert, Volcan, Château, VIP, PvP)
- **Génération procédurale** pour donjons multi-niveaux
- **Système de sauvegarde** (schematic FAWE)
- **API publique** pour que d'autres plugins utilisent les builders

---

## 💡 Principes SOLID Appliqués

### **S - Single Responsibility**

Chaque classe a une responsabilité unique :

- `HouseBuilder` → Construire des maisons
- `TerrainBuilder` → Terraforming
- `VillageZoneBuilder` → Orchestrer la zone

### **O - Open/Closed**

Extensible sans modifier le code existant :

- Créer `CastleBuilder` → implémenter `StructureBuilder`
- Nouvelle zone → créer `CastleZoneBuilder`

### **L - Liskov Substitution**

Tous les builders implémentent `StructureBuilder` et sont interchangeables.

### **I - Interface Segregation**

Interface minimaliste (3 méthodes) - pas de méthodes inutiles.

### **D - Dependency Inversion**

`VillageZoneBuilder` dépend de l'interface `StructureBuilder`, pas des implémentations concrètes.

---

## 📦 Dépendances Ajoutées

### build.gradle

```gradle
repositories {
    maven {
        name = 'enginehub'
        url = 'https://maven.enginehub.org/repo/'
    }
}

dependencies {
    compileOnly('com.fastasyncworldedit:FastAsyncWorldEdit-Core:2.11.2')
    compileOnly('com.fastasyncworldedit:FastAsyncWorldEdit-Bukkit:2.11.2') {
        transitive = false
    }
}
```

---

## 🎓 Leçons Apprises

1. **Prototype vs Production**
   V0 était parfait pour valider le concept, mais non scalable.

2. **Architecture Modulaire**
   Séparer les responsabilités rend le code maintenable et réutilisable.

3. **Async avec FAWE**
   WorldEdit EditSession + async = génération sans lag (critique pour 9 zones).

4. **Interfaces & Abstraction**
   Facilite l'extension future (nouveaux builders sans toucher l'existant).

5. **map_to_code.md**
   Un plan détaillé permet de concevoir une architecture évolutive dès le départ.

---

## 📝 Notes Techniques

### Performance FAWE

- **V0 Sync** : 234,423 blocs → 5-10s de freeze
- **V1 Async** : Millions de blocs → 0s de freeze (traité en arrière-plan)

### Gestion des Coordonnées

- **Coordonnées fixes** selon `map_to_code.md`
- Village Spawn : (0, -60, 0)
- Flat world : Y=-60 niveau du sol

### Extensibilité

Pour ajouter une nouvelle zone (ex: Château) :

1. Créer `CastleZoneBuilder extends ZoneBuilder`
2. Créer structures spécifiques : `TowerBuilder`, `RampartBuilder`
3. Créer commande `/castle`
4. Ajouter dans `plugin.yml`

---

## ✅ Validation

### Critères de Succès

- [x] Code modulaire (7 fichiers < 150 lignes)
- [x] Async (pas de freeze serveur)
- [x] Réutilisable (builders séparés)
- [x] Scalable (prêt pour 9 zones)
- [x] Compilation réussie
- [ ] Test in-game validé

### Alignement avec map_to_code.md

✅ **100% aligné** : Architecture conçue pour gérer 9 zones massives (jusqu'à 400x400 pour le château).

---

**Date de refactoring :** 27 novembre 2025
**Auteur :** Otmane & Adam
**Version :** 1.0.0-SNAPSHOT
