# 🎮 Guide pour Adam - Créer des structures dans ZineCraft

> Guide complet pour créer et déployer des structures Minecraft avec GitHub Copilot

---

## 📥 ÉTAPE 1 : Récupérer les modifications d'Otmane

### Si tu n'as PAS de modifications locales :
```bash
cd ton/chemin/vers/zinecraft
git pull origin adam-config
```

### Si tu AS des modifications locales :
```bash
cd ton/chemin/vers/zinecraft
git stash                    # Sauvegarde tes modifs
git pull origin adam-config  # Récupère les modifs d'Otmane
git stash pop                # Réapplique tes modifs
```

---

## 🏗️ ÉTAPE 2 : Créer une nouvelle structure

### 2.1 - Créer le fichier de structure

Dans VS Code, crée un nouveau fichier dans `server/structures/` :

**Exemple** : `server/structures/pyramide.txt`

```txt
# Pyramide égyptienne
# Construit devant le joueur
# Taille: 15x15x15 blocs

# Base de la pyramide
fill ~0 ~ ~0 ~14 ~ ~14 sandstone

# Niveau 2
fill ~1 ~1 ~1 ~13 ~1 ~13 sandstone

# Niveau 3
fill ~2 ~2 ~2 ~12 ~2 ~12 sandstone

# Niveau 4
fill ~3 ~3 ~3 ~11 ~3 ~11 sandstone

# Niveau 5
fill ~4 ~4 ~4 ~10 ~4 ~10 sandstone

# Niveau 6
fill ~5 ~5 ~5 ~9 ~5 ~9 sandstone

# Niveau 7
fill ~6 ~6 ~6 ~8 ~6 ~8 sandstone

# Sommet
setblock ~7 ~7 ~7 gold_block

# Torches d'entrée
setblock ~7 ~ ~0 torch
setblock ~7 ~ ~14 torch

# Entrée secrète
fill ~7 ~ ~0 ~7 ~2 ~0 air
```

### 2.2 - Syntaxe des commandes

**Coordonnées relatives** (par rapport au joueur) :
- `~0` = position actuelle
- `~5` = 5 blocs devant
- `~-3` = 3 blocs derrière
- `~0 ~10 ~0` = 10 blocs au-dessus

**Commandes courantes** :
```txt
# Remplir une zone
fill ~x1 ~y1 ~z1 ~x2 ~y2 ~z2 block_type [hollow/outline/replace]

# Placer un bloc
setblock ~x ~y ~z block_type

# Exemples de blocs
stone, stone_bricks, diamond_block, glass_pane
oak_planks, obsidian, glowstone, torch
```

---

## 🚀 ÉTAPE 3 : Tester ta structure

### Option A : Via le script bash (TERMINAL)

**Sur Windows avec Git Bash :**
```bash
cd /c/Users/Ton_Nom/zinecraft
./build-structure.sh pyramide AdamLeDams
```

**Sur Mac/Linux :**
```bash
cd ~/zinecraft
./build-structure.sh pyramide AdamLeDams
```

### Option B : Via le plugin (IN-GAME) - Quand il sera compilé

Dans Minecraft :
```
/build pyramide
/build list
```

---

## 💡 ÉTAPE 4 : Demander à Copilot de créer une structure

### 4.1 - Ouvrir le Chat Copilot dans VS Code

1. Appuie sur `Ctrl+Shift+I` (Windows) ou `Cmd+Shift+I` (Mac)
2. Ou clique sur l'icône de chat dans la barre latérale

### 4.2 - Exemples de prompts à utiliser

**Exemple 1 - Simple :**
```
Crée un fichier server/structures/pont.txt qui construit un pont en pierre 
de 20 blocs de long avec des torches tous les 5 blocs
```

**Exemple 2 - Détaillé :**
```
Crée un fichier server/structures/eglise.txt qui construit une église médiévale :
- Murs en stone_bricks
- Toit en oak_stairs
- Vitraux en glass_pane coloré
- Clocher de 20 blocs de haut
- Taille totale : 15x20x15 blocs
Utilise les coordonnées relatives (~) pour que ce soit devant le joueur
```

**Exemple 3 - Complexe :**
```
Crée un fichier server/structures/donjon.txt qui construit un donjon souterrain :
- Creuse une salle de 10x10x5 dans le sol (utilise air)
- Murs en cobblestone
- Sol en stone
- Éclairage avec des torches
- Coffres avec du loot
- Spawner de zombies au centre
```

### 4.3 - Template de prompt optimal

```
Crée un fichier server/structures/[NOM].txt qui construit [DESCRIPTION].

Caractéristiques :
- Taille : [largeur]x[hauteur]x[profondeur] blocs
- Matériaux principaux : [liste]
- Éléments spéciaux : [liste]

Règles importantes :
- Utilise les coordonnées relatives (~)
- Commence par un commentaire expliquant la structure
- Optimise le nombre de commandes fill
- Ajoute de l'éclairage (torches, glowstone)
```

---

## 📝 ÉTAPE 5 : Committer et pusher ta structure

### 5.1 - Ajouter ton fichier
```bash
git add server/structures/ta_structure.txt
```

### 5.2 - Commit
```bash
git commit -m "feat: Ajout structure [nom] - [description courte]"
```

**Exemples de messages :**
```bash
git commit -m "feat: Ajout structure pyramide - Pyramide égyptienne 15x15"
git commit -m "feat: Ajout structure pont - Pont en pierre avec éclairage"
git commit -m "feat: Ajout structure donjon - Donjon souterrain avec spawner"
```

### 5.3 - Push vers GitHub
```bash
git push origin adam-config
```

---

## 🎨 IDÉES DE STRUCTURES À CRÉER

### Faciles (10-20 commandes)
- ✅ Maison (déjà fait)
- ✅ Tour (déjà fait)
- 🆕 Pont
- 🆕 Fontaine
- 🆕 Statue
- 🆕 Phare

### Moyennes (20-50 commandes)
- ✅ Château (déjà fait)
- ✅ Arène (déjà fait)
- 🆕 Église
- 🆕 Moulin
- 🆕 Pyramide
- 🆕 Temple

### Difficiles (50+ commandes)
- 🆕 Donjon souterrain
- 🆕 Vaisseau spatial
- 🆕 Labyrinthe
- 🆕 Ville médiévale
- 🆕 Stade de football

---

## 🔥 ASTUCES PRO

### 1. Tester rapidement
Connecte-toi au serveur et teste directement :
```bash
./build-structure.sh ta_structure AdamLeDams
```

### 2. Copier une structure existante
```bash
cp server/structures/castle.txt server/structures/ma_structure.txt
# Puis modifie le fichier
```

### 3. Utiliser des variables Copilot
Quand tu crées un fichier, Copilot va **auto-suggérer** des commandes similaires.
Appuie sur `Tab` pour accepter la suggestion.

### 4. Demander des variantes
```
Modifie server/structures/castle.txt pour le rendre 2x plus grand
```

### 5. Optimiser les commandes
Au lieu de :
```txt
setblock ~0 ~0 ~0 stone
setblock ~1 ~0 ~0 stone
setblock ~2 ~0 ~0 stone
```

Utilise :
```txt
fill ~0 ~0 ~0 ~2 ~0 ~0 stone
```

---

## 🐛 RÉSOLUTION DE PROBLÈMES

### Le script ne fonctionne pas
```bash
# Vérifie que tu es à la racine du projet
pwd
cd /chemin/vers/zinecraft

# Vérifie les permissions
chmod +x build-structure.sh

# Vérifie que Docker tourne
docker ps | grep zinecraft
```

### La structure apparaît mal
- Vérifie les coordonnées relatives (~)
- Teste d'abord avec une petite structure
- Utilise `/tp @s X Y Z` pour te placer au bon endroit

### Git push échoue
```bash
# Vérifie ta branche
git branch

# Si tu es sur master, change de branche
git checkout adam-config

# Pull avant de push
git pull origin adam-config
git push origin adam-config
```

---

## 📚 RESSOURCES UTILES

### Blocs Minecraft courants
```
stone, cobblestone, stone_bricks, mossy_stone_bricks
oak_planks, spruce_planks, birch_planks, dark_oak_planks
glass, glass_pane, white_stained_glass
oak_stairs, stone_brick_stairs, brick_stairs
oak_fence, iron_bars, torch, glowstone
diamond_block, gold_block, emerald_block
obsidian, bedrock, netherite_block
water, lava, air
```

### Commandes Minecraft
- [Wiki Minecraft - Commandes](https://minecraft.fandom.com/wiki/Commands)
- [Wiki Minecraft - Blocs](https://minecraft.fandom.com/wiki/Block)

---

## 🎯 CHECKLIST AVANT DE PUSH

- [ ] J'ai testé ma structure in-game
- [ ] Le fichier est dans `server/structures/`
- [ ] Le fichier se termine par `.txt`
- [ ] J'ai ajouté des commentaires au début
- [ ] J'utilise des coordonnées relatives (~)
- [ ] J'ai fait `git add` + `git commit` + `git push`
- [ ] Mon message de commit est clair

---

## 🤝 COLLABORATION AVEC OTMANE

### Workflow recommandé

1. **PULL** tous les matins
```bash
git pull origin adam-config
```

2. **CRÉE** tes structures

3. **TEST** localement
```bash
./build-structure.sh ma_structure AdamLeDams
```

4. **COMMIT + PUSH** le soir
```bash
git add server/structures/*.txt
git commit -m "feat: Ajout structures [liste]"
git push origin adam-config
```

### En cas de conflit
```bash
# Copilot peut t'aider !
# Dans le chat Copilot :
"J'ai un conflit Git sur le fichier X, aide-moi à le résoudre"
```

---

## 🎮 COMMANDES SERVEUR UTILES

### Se téléporter
```
/tp @s X Y Z
/tp @s OtmaneZ
```

### Vider son inventaire
```
/clear @s
```

### Changer le mode de jeu
```
/gamemode creative
/gamemode survival
```

### Remplir l'inventaire
```
/give @s diamond 64
/give @s stone_bricks 640
```

---

## 📞 BESOIN D'AIDE ?

**Demande à Copilot directement :**

1. Ouvre le chat (`Ctrl+Shift+I`)
2. Tape ton problème
3. Copilot va te guider !

**Exemples :**
```
"Comment créer une structure circulaire dans Minecraft ?"
"Aide-moi à débugger ma structure pyramide.txt"
"Donne-moi des idées de structures cool à créer"
"Comment faire un toit arrondi avec des commandes fill ?"
```

---

**🎉 Bon build Adam ! N'hésite pas à expérimenter et à créer des trucs fous ! 🚀**

*Créé par Otmane & GitHub Copilot*
