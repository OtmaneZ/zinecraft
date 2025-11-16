# 🎮 Guide de démarrage - Adam

> Bienvenue dans le projet ZineCraft ! Voici comment commencer à coder.

---

## 🚀 PREMIÈRE FOIS - Setup

### 1. **Récupérer le projet** (à faire UNE SEULE FOIS)

```bash
# Va dans ton dossier Documents
cd ~/Documents

# Clone le projet (demande à Papa le lien GitHub si besoin)
git clone <URL_DU_REPO> Zinecraft
cd Zinecraft
```

### 2. **Se positionner sur ta branche**

```bash
# Va sur la branche de développement
git checkout dev

# Crée ta branche de travail (selon ce que tu fais)
git checkout feature/adam-skills
```

---

## 💻 TOUS LES JOURS - Workflow

### 🌅 **Avant de commencer à coder**

```bash
# 1. Assure-toi d'être sur ta branche
git branch
# Tu dois voir une * devant feature/adam-XXX

# 2. Récupère les dernières modifications
git checkout dev
git pull
git checkout feature/adam-skills
git merge dev
```

### ✍️ **Pendant que tu codes**

- Code tranquillement sur **UN seul fichier à la fois**
- Teste ton code régulièrement
- Demande à Papa si tu bloques !

### 💾 **Sauvegarder ton travail (commit)**

```bash
# 1. Voir ce que tu as modifié
git status

# 2. Ajouter tes changements
git add .

# 3. Créer un commit avec un message
git commit -m "feat(skills): Add mining skill with XP"

# 4. Envoyer sur GitHub
git push origin feature/adam-skills
```

---

## 📝 **Messages de commit (exemples)**

```bash
# ✅ BIEN
git commit -m "feat(skills): Add mining skill"
git commit -m "feat(pets): Add cat pet with meow sound"
git commit -m "fix(quests): Fix daily quest reset"

# ❌ PAS BIEN
git commit -m "test"
git commit -m "ça marche"
git commit -m "modif"
```

**Format :**
- `feat(XXX):` → Nouvelle fonctionnalité
- `fix(XXX):` → Correction de bug
- `docs(XXX):` → Documentation
- `style(XXX):` → Changement visuel

---

## 🎯 **Tes premières missions**

### 🏆 Mission 1 : Système de Skills (facile)
**Fichier** : `SkillManager.java`

**Objectif :** Créer un système de compétences (Mining, Combat, Farming)

**Ce que tu dois faire :**
1. Créer la classe `SkillManager`
2. Ajouter 3 skills : MINING, COMBAT, FARMING
3. Chaque skill a un niveau (1-100) et XP
4. Commande `/skills` pour voir ses skills

**Demande à Papa pour :** La connexion avec la base de données

---

### 🐱 Mission 2 : Système de Pets (moyen)
**Fichier** : `PetManager.java`

**Objectif :** Créer des pets que les joueurs peuvent avoir

**Ce que tu dois faire :**
1. Créer 3-4 types de pets (Chat, Chien, Oiseau, Lapin)
2. Chaque pet suit le joueur
3. Commande `/pets` pour spawner un pet
4. Menu GUI pour choisir son pet

**Demande à Papa pour :** L'IA de déplacement des pets

---

### 📜 Mission 3 : Système de Quêtes (difficile)
**Fichier** : `QuestManager.java`

**Objectif :** Créer des quêtes quotidiennes

**Ce que tu dois faire :**
1. Créer 5 quêtes simples (miner 10 blocs, tuer 5 mobs, etc.)
2. Commande `/quests` pour voir ses quêtes
3. Système de récompenses (argent, XP)
4. Reset automatique chaque jour

**Demande à Papa pour :** Le timer de reset quotidien

---

## 🛠️ **Commandes utiles VS Code**

### **Builder le projet**
```bash
cd plugins/ZineCraftCore
gradle build
```

### **Copier le JAR vers le serveur**
```bash
# Le JAR est automatiquement copié après le build !
# Il va dans : server/plugins/
```

### **Voir les erreurs**
- Regarde les erreurs en rouge dans VS Code
- Demande à Papa si tu comprends pas

---

## 🎨 **Structure de ton code**

### **Où créer tes fichiers ?**

```
plugins/ZineCraftCore/src/main/java/fr/zinecraft/
├── core/
│   └── ZineCraftCore.java (le main - Papa s'en occupe)
├── managers/
│   ├── SkillManager.java     ← TU CRÉES CE FICHIER
│   ├── PetManager.java        ← TU CRÉES CE FICHIER
│   └── QuestManager.java      ← TU CRÉES CE FICHIER
├── commands/
│   ├── SkillsCommand.java     ← TU CRÉES CE FICHIER
│   ├── PetsCommand.java       ← TU CRÉES CE FICHIER
│   └── QuestsCommand.java     ← TU CRÉES CE FICHIER
└── models/
    ├── Skill.java
    ├── Pet.java
    └── Quest.java
```

---

## 💡 **Exemples de code simple**

### **Créer une commande `/hello`**

```java
package fr.zinecraft.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelloCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Cette commande est pour les joueurs !");
            return true;
        }

        Player player = (Player) sender;
        player.sendMessage("§a§lHello " + player.getName() + " !");

        return true;
    }
}
```

### **Codes couleur Minecraft**

```java
"§a" // Vert
"§c" // Rouge
"§e" // Jaune
"§b" // Bleu cyan
"§l" // Gras
"§o" // Italique
"§r" // Reset

// Exemple
player.sendMessage("§a✔ §fVotre niveau est §e§l" + level);
```

---

## 🆘 **En cas de problème**

### ❌ **"Je peux pas push"**
```bash
git pull origin feature/adam-skills
# Puis réessaye
git push origin feature/adam-skills
```

### ❌ **"J'ai des erreurs de compilation"**
1. Vérifie les imports en haut du fichier
2. Vérifie l'orthographe des variables
3. Demande à Papa !

### ❌ **"J'ai cassé quelque chose"**
```bash
# Annuler tes modifications
git checkout .

# Ou demande à Papa de t'aider
```

---

## 🎯 **Objectifs de la semaine**

### **Semaine 1 (Adam)**
- [ ] Premier commit réussi
- [ ] Commande `/skills` qui fonctionne
- [ ] Afficher le niveau de 3 skills
- [ ] Menu GUI avec les skills

### **Récompenses 🏆**
- ✅ Premier commit → Pizza ! 🍕
- ✅ Première feature complète → Jeu vidéo au choix ! 🎮
- ✅ Système de skills fini → Argent de poche bonus ! 💰

---

## 📚 **Ressources utiles**

- **Spigot API** : https://hub.spigotmc.org/javadocs/spigot/
- **Paper API** : https://jd.papermc.io/paper/1.20/
- **Tutoriels Bukkit** : https://www.spigotmc.org/wiki/

---

## 💬 **Questions fréquentes**

**Q: Sur quelle branche je travaille ?**
R: Toujours sur `feature/adam-XXX` (skills, pets ou quests)

**Q: Je peux modifier les fichiers de Papa ?**
R: Non ! Reste dans tes fichiers (managers, commands). Si besoin, demande à Papa.

**Q: Comment tester mon code ?**
R: Lance le serveur avec Docker et connecte-toi en jeu !

**Q: Je comprends pas Java...**
R: C'est normal ! Demande à Papa, on apprend ensemble 😊

---

**🎮 Amuse-toi bien et code des trucs cool ! 🚀**

*N'oublie pas : Même les meilleurs développeurs commencent par demander de l'aide !*
