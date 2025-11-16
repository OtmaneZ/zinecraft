# 🤝 Guide de Contribution - ZineCraft

## 👥 Équipe

- **Otmane** (Papa) - Infrastructure, DevOps, Docker, Architecture
- **Adam** (Fils) - Développement Java, Plugins Minecraft, Gameplay

## 🌿 Workflow Git

### Branches

- `main` → Version stable, production ready
- `dev` → Développement actif, tests
- `feature/nom-feature` → Nouvelles fonctionnalités
- `fix/nom-bug` → Corrections de bugs

### Règles simples

1. **Jamais commit direct sur `main`**
2. Toujours travailler sur une branche `feature/` ou `fix/`
3. Tester localement avant de push
4. Faire des commits clairs et réguliers

### Workflow typique

```bash
# 1. Créer une nouvelle branche depuis dev
git checkout dev
git pull origin dev
git checkout -b feature/ma-nouvelle-feature

# 2. Travailler sur le code
# ... modifications ...

# 3. Commit réguliers
git add .
git commit -m "feat: ajout système de quêtes"

# 4. Push vers GitHub
git push origin feature/ma-nouvelle-feature

# 5. Créer une Pull Request sur GitHub
# dev ← feature/ma-nouvelle-feature

# 6. Review + Merge par l'autre membre de l'équipe
```

## 📝 Convention de Commits

Format : `type: description`

**Types :**
- `feat:` - Nouvelle fonctionnalité
- `fix:` - Correction de bug
- `docs:` - Documentation
- `style:` - Formatting, point-virgules manquants
- `refactor:` - Refactoring du code
- `test:` - Ajout de tests
- `chore:` - Maintenance, mise à jour dépendances

**Exemples :**
```
feat: ajout système de pets
fix: correction bug scoreboard
docs: mise à jour README
refactor: amélioration structure plugin Core
```

## 🧪 Tests avant Push

Avant de push, vérifier :

1. ✅ Le plugin compile sans erreur
```bash
cd plugins/ZineCraftCore
./gradlew build
```

2. ✅ Le serveur démarre correctement
```bash
docker-compose restart papermc
docker-compose logs -f papermc
```

3. ✅ Tester en jeu la fonctionnalité

## 📁 Organisation du Code

### Structure Plugin
```
plugins/ZineCraftCore/
├── src/main/java/
│   └── fr/zinecraft/core/
│       ├── ZineCraftCore.java      # Classe principale
│       ├── commands/               # Commandes
│       ├── listeners/              # Events listeners
│       ├── managers/               # Gestionnaires (skills, quêtes, etc.)
│       ├── models/                 # Classes de données
│       └── utils/                  # Utilitaires
└── src/main/resources/
    ├── plugin.yml                  # Manifest du plugin
    └── config.yml                  # Configuration par défaut
```

### Conventions de code Java

- **Packages** : `fr.zinecraft.core.*`
- **Classes** : `PascalCase` (ex: `SkillManager`)
- **Méthodes** : `camelCase` (ex: `loadPlayer()`)
- **Constantes** : `UPPER_SNAKE_CASE` (ex: `MAX_LEVEL`)
- **Indentation** : 4 espaces
- **Commentaires** : JavaDoc pour les méthodes publiques

## 🔄 Répartition du Travail

### Otmane (Infrastructure)
- Configuration Docker
- Setup serveur PaperMC
- Base de données MySQL
- CI/CD GitHub Actions
- Backups automatiques
- Monitoring

### Adam (Développement)
- Plugin Core
- Système de skills
- Système de quêtes
- Pets & particules
- Menus GUI
- Commandes joueurs

### Ensemble
- Architecture globale
- Design des features
- Tests
- Documentation
- Décisions importantes

## 💬 Communication

- **Discord** : Channel dédié au projet
- **GitHub Issues** : Pour les bugs et features
- **GitHub Projects** : Pour le suivi des tâches
- **Reviews** : On review le code de l'autre avant merge

## 🚨 En cas de conflit Git

```bash
# 1. Récupérer les dernières modifs
git fetch origin

# 2. Rebaser votre branche sur dev
git checkout feature/ma-feature
git rebase origin/dev

# 3. Résoudre les conflits dans VS Code
# (VS Code va vous montrer les conflits)

# 4. Continuer le rebase
git add .
git rebase --continue

# 5. Force push (seulement sur votre branche feature)
git push -f origin feature/ma-feature
```

## ✨ Bonnes Pratiques

1. **Commits atomiques** : Un commit = Une fonctionnalité/fix
2. **Messages clairs** : Décrire ce qui a été fait
3. **Code propre** : Indentation, nommage cohérent
4. **Tests locaux** : Toujours tester avant de push
5. **Documentation** : Commenter le code complexe
6. **Communication** : Prévenir l'autre avant gros changement

## 🎯 Objectif

Travailler efficacement en équipe pour livrer **ZineCraft en 30 jours** ! 🚀

---

**Questions ?** Demander à l'autre membre de l'équipe ou checker la doc ! 😊
