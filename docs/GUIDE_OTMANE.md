# 🔧 Guide de travail - Otmane (Papa Dev)

> Workflow et bonnes pratiques pour le lead dev

---

## 🎯 Ton rôle

- **Architecte** : Structure du projet, décisions techniques
- **Backend** : Database, API, performance
- **DevOps** : Docker, CI/CD, déploiement
- **Mentor** : Guider Adam dans son apprentissage
- **Code Review** : Valider le code avant merge

---

## 🌿 Tes branches de travail

```bash
feature/otmane-database    # DatabaseManager, MySQL, HikariCP
feature/otmane-docker      # Docker, infrastructure
feature/otmane-api         # API REST (si besoin)
feature/otmane-security    # Permissions, anti-cheat
```

---

## 💻 Workflow quotidien

### 🌅 **Matin - Synchronisation**

```bash
# Récupérer les modifs d'Adam
git checkout dev
git pull origin dev

# Vérifier ce qu'Adam a poussé
git log --oneline --author="Adam" -5

# Merge dans ta branche si besoin
git checkout feature/otmane-database
git merge dev
```

### 🔍 **Review du code d'Adam**

```bash
# Voir les changements d'Adam
git diff dev feature/adam-skills

# Ou via GitHub PR (Pull Request)
```

**Checklist de review :**
- ✅ Le code compile sans erreur
- ✅ Pas de code dupliqué
- ✅ Nommage des variables clair
- ✅ Commentaires sur les parties complexes
- ✅ Pas de `System.out.println()` (utiliser logger)
- ✅ Gestion des erreurs (try/catch)

### 💾 **Merger le travail d'Adam**

```bash
git checkout dev

# Si tout est OK
git merge feature/adam-skills
git push origin dev

# Dire à Adam que c'est mergé ! 🎉
```

---

## 🛠️ Tes tâches prioritaires

### **Semaine 1 - Infrastructure**

#### 1. **DatabaseManager** (critique)
**Branche** : `feature/otmane-database`

```java
// À créer dans : plugins/ZineCraftCore/src/main/java/fr/zinecraft/core/database/

DatabaseManager.java
- initConnection()
- closeConnection()
- executeQuery()
- executeUpdate()
- getConnection()

PlayerDataManager.java
- loadPlayerData()
- savePlayerData()
- createPlayerData()
```

**Config MySQL** :
```yaml
database:
  host: localhost
  port: 3306
  name: zinecraft
  user: root
  password: password
  pool:
    max-connections: 10
    timeout: 30000
```

---

#### 2. **Docker & Infrastructure** (critique)
**Branche** : `feature/otmane-docker`

**À faire :**
- [ ] Tester docker-compose up
- [ ] Vérifier PaperMC démarre
- [ ] Vérifier MySQL connecté
- [ ] Configurer volumes pour persistence
- [ ] Script de backup automatique
- [ ] Health checks

**Test :**
```bash
cd docker
docker-compose up -d
docker-compose logs -f papermc
docker-compose ps
```

---

#### 3. **ConfigManager** (important)
**Branche** : `feature/otmane-database`

```java
// Gérer config.yml proprement
ConfigManager.java
- loadConfig()
- saveConfig()
- reloadConfig()
- getString(path)
- getInt(path)
- etc.
```

---

#### 4. **Architecture des Managers** (important)

**Structure à créer :**
```
managers/
├── DatabaseManager.java     (Toi)
├── ConfigManager.java        (Toi)
├── PlayerManager.java        (Toi - base, Adam - features)
├── SkillManager.java         (Adam)
├── PetManager.java           (Adam)
└── QuestManager.java         (Adam)
```

**Pattern Singleton pour les managers :**
```java
public class DatabaseManager {
    private static DatabaseManager instance;

    private DatabaseManager() {
        // Init
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
}
```

---

## 👨‍🏫 Mentorat d'Adam

### **Sessions de pair programming**

**Lundi** (30 min) :
- Expliquer la structure du projet
- Montrer comment créer une classe
- Premier commit ensemble

**Mercredi** (30 min) :
- Review de son code
- Expliquer les erreurs
- Refactoring ensemble

**Vendredi** (30 min) :
- Merge de sa feature
- Célébrer les réussites ! 🎉
- Planifier la semaine suivante

---

### **Concepts à lui enseigner progressivement**

**Semaine 1 :**
- ✅ Classes et objets
- ✅ Méthodes (paramètres, return)
- ✅ Variables (types de base)
- ✅ Conditions (if/else)

**Semaine 2 :**
- ✅ Boucles (for, while)
- ✅ Listes (ArrayList)
- ✅ HashMaps
- ✅ Events Bukkit

**Semaine 3 :**
- ✅ Héritage
- ✅ Interfaces
- ✅ Try/catch
- ✅ Enums

**Semaine 4 :**
- ✅ Design patterns (Singleton, Observer)
- ✅ Best practices
- ✅ Tests unitaires (basics)

---

## 🔐 Sécurité & Permissions

### **À mettre en place toi-même**

```java
// PermissionManager.java
- checkPermission(player, permission)
- hasGrade(player, grade)
- grantPermission()
- revokePermission()
```

**Ne laisse PAS Adam gérer ça** (trop complexe pour débuter)

---

## 🚀 CI/CD (Semaine 2-3)

### **GitHub Actions**

```yaml
# .github/workflows/build.yml
name: Build Plugin

on:
  push:
    branches: [dev, main]
  pull_request:
    branches: [dev, main]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
      - name: Build with Gradle
        run: |
          cd plugins/ZineCraftCore
          gradle build
```

---

## 📊 Monitoring

### **Metrics à suivre**

- TPS serveur (doit rester > 19.5)
- RAM usage
- Joueurs connectés
- Queries BDD/seconde
- Temps de réponse API

**Outils :**
- Spark (profiling)
- Plan (analytics)
- Prometheus (monitoring)

---

## 🐛 Debugging

### **Commandes utiles**

```bash
# Logs du serveur
docker-compose logs -f papermc

# Logs MySQL
docker-compose logs -f mysql

# Shell dans le container
docker-compose exec papermc bash

# Rebuild le plugin
cd plugins/ZineCraftCore && gradle clean build

# Restart serveur
docker-compose restart papermc
```

### **Logger proprement**

```java
import java.util.logging.Logger;

public class ZineCraftCore extends JavaPlugin {
    private static final Logger log = Logger.getLogger("ZineCraft");

    log.info("Message info");
    log.warning("Message warning");
    log.severe("Message erreur");
}
```

---

## 📅 Planning hebdomadaire suggéré

### **Lundi**
- Planning de la semaine avec Adam
- Définir ses objectifs
- Pair programming 30min

### **Mardi-Jeudi**
- Travail autonome chacun de son côté
- Questions/réponses sur Discord

### **Mercredi**
- Review du code d'Adam
- Corrections/refactoring ensemble

### **Vendredi**
- Merge des features
- Test du serveur complet
- Rétrospective : qu'est-ce qui a bien/mal marché ?

### **Weekend**
- Travail optionnel
- Exploration de nouvelles features
- Veille techno

---

## 🎯 KPIs du projet (pour toi)

### **Technique**
- ✅ 0 erreur de compilation
- ✅ Build time < 10s
- ✅ Code coverage > 50% (semaine 3+)
- ✅ TPS > 19.5
- ✅ RAM < 2GB

### **Pédagogique (Adam)**
- ✅ 1 commit minimum/jour
- ✅ 1 feature complète/semaine
- ✅ Compréhension des concepts de base
- ✅ Autonomie croissante

### **Projet**
- ✅ Respect du planning 30 jours
- ✅ Fonctionnalités core finies semaine 2
- ✅ Monétisation prête semaine 3
- ✅ Lancement semaine 4

---

## 💡 Tips

1. **Garde le code simple** pour qu'Adam puisse comprendre
2. **Commente ton code** - il va le lire
3. **Utilise des noms de variables explicites**
4. **Fais des fonctions courtes** (< 20 lignes)
5. **Évite les design patterns complexes** au début

---

## 🆘 Ressources techniques

- **Paper API Docs** : https://jd.papermc.io/paper/1.20/
- **HikariCP** : https://github.com/brettwooldridge/HikariCP
- **Docker Compose** : https://docs.docker.com/compose/
- **Gradle** : https://docs.gradle.org/

---

**🚀 Let's build something awesome together!**
