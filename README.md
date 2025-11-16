# 🎮 ZineCraft Server

> Serveur Minecraft professionnel créé par Otmane & Adam

[![Setup](https://img.shields.io/badge/Setup-✅_Complete-brightgreen)]()
[![Build](https://img.shields.io/badge/Build-✅_Success-brightgreen)]()
[![Progression](https://img.shields.io/badge/Progression-20%25-yellow)]()
[![Jour](https://img.shields.io/badge/Jour-1/30-blue)]()

## 📊 État Actuel du Projet

**Dernière mise à jour** : 16 novembre 2025
**Phase actuelle** : 🔧 Setup & Infrastructure (Semaine 1)

```
█████░░░░░░░░░░░░░░░░ 20% Complete (Jour 1/30)
```

## 🎯 Vision

Créer **en 30 jours** un serveur Minecraft **beau, monétisable, stable et évolutif** avec :
- ✨ Une expérience visuelle premium
- 🎮 Un gameplay simple mais addictif
- 🔧 Un pipeline DevOps propre
- 📈 Une base ready pour scaler

## 🚀 Stack Technique

- **Backend**: PaperMC 1.20+
- **Plugins**: Java + Gradle + Spigot/Paper API
- **Database**: MySQL
- **Infrastructure**: Docker Compose
- **CI/CD**: GitHub Actions
- **Monétisation**: Tebex

## 📁 Structure du Projet

```
Zinecraft/
├── server/              # Serveur PaperMC
│   ├── plugins/         # Plugins compilés
│   └── config/          # Configurations serveur
├── plugins/             # Code source des plugins
│   └── ZineCraftCore/   # Plugin principal
├── web/                 # Landing page
├── docker/              # Configuration Docker
├── docs/                # Documentation
└── scripts/             # Scripts utilitaires
```

## 🛠️ Installation & Setup

### Prérequis
- Docker & Docker Compose
- Java 17+
- Gradle 8+
- Git

### Démarrage rapide

1. **Cloner le repo**
```bash
git clone <votre-repo>
cd Zinecraft
```

2. **Lancer le serveur (Docker)**
```bash
cd docker
docker-compose up -d
```

3. **Builder le plugin Core**
```bash
cd plugins/ZineCraftCore
./gradlew build
```

4. **Copier le JAR dans le serveur**
```bash
cp build/libs/ZineCraftCore-*.jar ../../server/plugins/
```

5. **Redémarrer le serveur**
```bash
docker-compose restart papermc
```

## 👥 Collaboration

Ce projet est développé en équipe par :
- **Otmane** - Infrastructure, DevOps, Architecture
- **Adam** - Développement plugins, Gameplay

Voir [CONTRIBUTING.md](./CONTRIBUTING.md) pour les règles de contribution.

## 📅 Roadmap Détaillée

### 🔧 Semaine 1 - Base technique + beauté (Jours 1-7)
**Progression** : `███░░░░░░░ 30%`

#### ✅ Infrastructure & Setup (FAIT)
- [x] ✅ Initialiser le repository Git
- [x] ✅ Créer la structure de dossiers
- [x] ✅ Setup Docker Compose (PaperMC + MySQL + Portainer)
- [x] ✅ Installer Gradle 9.2.0 + Java 17
- [x] ✅ Créer projet plugin Core (build successful)
- [x] ✅ Configuration auto-copy JAR vers serveur
- [x] ✅ Documentation (README, CONTRIBUTING, ARCHITECTURE)

#### 🚧 En cours
- [ ] 🔄 Démarrer le serveur PaperMC via Docker
- [ ] 🔄 Tester le plugin Core in-game
- [ ] 🔄 Installer map premium (8-15€)

#### ⏳ À venir cette semaine
- [ ] ⏳ Créer système de config (config.yml)
- [ ] ⏳ Scoreboard personnalisé
- [ ] ⏳ Menu GUI principal (/zc menu)
- [ ] ⏳ Logo serveur + MOTD
- [ ] ⏳ Bannière Discord
- [ ] ⏳ Landing page simple (HTML/CSS)

---

### 🎮 Semaine 2 - Gameplay simple (Jours 8-14)
**Progression** : `░░░░░░░░░░ 0%`

- [ ] Système de skills (Mining, Combat, Farming)
- [ ] XP & niveaux par skill
- [ ] Quêtes quotidiennes (3-5 quêtes)
- [ ] Système de récompenses
- [ ] Pets basiques (3-4 pets)
- [ ] Zones de farm optimisées
- [ ] Commands joueur (/skills, /quests, /pets)
- [ ] Test load serveur (50 joueurs)

---

### 🪙 Semaine 3 - Monétisation + polish (Jours 15-21)
**Progression** : `░░░░░░░░░░ 0%`

- [ ] Compte Tebex + intégration
- [ ] Shop web (grades, cosmetics, boosts)
- [ ] Système de grades (VIP, VIP+, LEGEND)
- [ ] Permissions par grade
- [ ] Cosmetics (particules, titles, trails)
- [ ] Pets premium
- [ ] Polish UI/UX
- [ ] Trailer TikTok 20s
- [ ] Screenshots marketing

---

### 🚀 Semaine 4 - Finalisation + Lancement (Jours 22-30)
**Progression** : `░░░░░░░░░░ 0%`

- [ ] Tests complets gameplay
- [ ] Tests charge serveur
- [ ] Correction bugs critiques
- [ ] Équilibrage XP/ressources
- [ ] Documentation joueur
- [ ] Règles serveur
- [ ] Setup Discord communauté
- [ ] Mise en production
- [ ] Beta test (10-20 joueurs)
- [ ] Lancement officiel
- [ ] Communication réseaux sociaux

---

## 📈 Métriques de Progression

| Catégorie | Statut | Progression |
|-----------|--------|-------------|
| 🏗️ Infrastructure | ✅ Complet | 100% ████████████ |
| 🔌 Plugin Core | ✅ Base OK | 30% ███░░░░░░░░░ |
| 🎨 Graphisme | ⏳ Pas commencé | 0% ░░░░░░░░░░░░ |
| 🎮 Gameplay | ⏳ Pas commencé | 0% ░░░░░░░░░░░░ |
| 🪙 Monétisation | ⏳ Pas commencé | 0% ░░░░░░░░░░░░ |
| 📱 Marketing | ⏳ Pas commencé | 0% ░░░░░░░░░░░░ |
| **GLOBAL** | 🚧 En cours | **20%** ██░░░░░░░░░░ |

## 📝 Documentation

- [Architecture](./docs/ARCHITECTURE.md)
- [Guide de développement](./docs/DEVELOPMENT.md)
- [API des plugins](./docs/API.md)
- [Déploiement](./docs/DEPLOYMENT.md)

## 🪙 Monétisation

- Grades (3-10€)
- Pets & particules premium
- Cosmetics
- Boost XP
- Shop web via Tebex

## 📄 Licence

Projet privé - Tous droits réservés © 2025 Otmane & Adam

---

**🎯 Objectif : Un serveur pro qui génère des revenus dès le premier mois !**
