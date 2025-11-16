# 🎮 ZineCraft Server

> Serveur Minecraft professionnel créé par Otmane & Adam

[![Setup](https://img.shields.io/badge/Setup-✅_Complete-brightgreen)]()
[![Build](https://img.shields.io/badge/Build-✅_Success-brightgreen)]()
[![Progression](https://img.shields.io/badge/Progression-20%25-yellow)]()
[![Jour](https://img.shields.io/badge/Jour-1/30-blue)]()

## 📊 État Actuel du Projet

**Dernière mise à jour** : 16 novembre 2025
**Phase actuelle** : 🔧 Setup & Infrastructure (Semaine 1) - ✅ **SERVEUR OPÉRATIONNEL**

```
███████░░░░░░░░░░░░░░ 35% Complete (Jour 1/30)
```

**🎮 Serveur en ligne** : `91.99.237.55:25565` (Minecraft Java 1.21)
**👥 Joueurs actifs** : Otmane06000, AdamLeDams

## 🎯 Vision

Créer **en 30 jours** un serveur Minecraft **beau, monétisable, stable et évolutif** avec :

- ✨ Une expérience visuelle premium
- 🎮 Un gameplay simple mais addictif
- 🔧 Un pipeline DevOps propre
- 📈 Une base ready pour scaler

## 🚀 Stack Technique

- **Backend**: PaperMC 1.21 (Java 21)
- **Plugins**: Java + Gradle + Spigot/Paper API
- **Database**: MySQL 8.0
- **Infrastructure**: Docker Compose
- **Management**: Portainer, phpMyAdmin, RCON
- **CI/CD**: GitHub Actions (à venir)
- **Monétisation**: Tebex (à venir)

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
git clone https://github.com/OtmaneZ/zinecraft.git
cd zinecraft
git checkout adam-config
```

2. **Lancer le serveur (Docker)**

```bash
cd docker
docker compose up -d
```

3. **Gérer le serveur**

```bash
# Arrêter le serveur (sans toucher aux autres projets Docker)
docker compose stop

# Redémarrer le serveur
docker compose start

# Voir les logs
docker logs -f zinecraft-papermc

# Utiliser RCON (commandes Minecraft)
docker exec zinecraft-papermc rcon-cli "commande"

# OU utiliser l'alias simplifié (après setup)
mc "commande"
```

4. **Script utilitaire** (depuis la racine du projet)

```bash
# Exécuter une commande Minecraft
./rcon.sh cmd "give Player diamond 64"

# Faire un backup du monde
./rcon.sh backup

# Redémarrer le serveur
./rcon.sh restart

# Voir les logs
./rcon.sh logs

# Voir le status
./rcon.sh status
```

### Connexion au serveur

**IP du serveur** : `91.99.237.55:25565`
**Version** : Minecraft Java Edition 1.21
**Mode** : Créatif (pour le moment)

### 🔧 Configuration serveur

- **Port Minecraft** : 25565
- **Port RCON** : 25575
- **Portainer** : <http://91.99.237.55:9001>
- **phpMyAdmin** : <http://91.99.237.55:9003>
- **RAM** : 4GB allouée
- **Gamerules** :
  - Jour éternel (doDaylightCycle: false)
  - KeepInventory activé
  - Météo désactivée
  - Command blocks activés

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
- [x] ✅ Configuration PaperMC 1.21 avec Java 21
- [x] ✅ Firewall Hetzner (port 25565 ouvert)
- [x] ✅ RCON activé pour gestion à distance
- [x] ✅ Serveur en ligne et accessible
- [x] ✅ Multijoueur fonctionnel (2 joueurs testés)
- [x] ✅ Game rules configurés (eternal day, keepInventory)
- [x] ✅ Command blocks activés
- [x] ✅ Scripts utilitaires (rcon.sh, apply-config.sh)
- [x] ✅ Git workflow propre (world files ignorés)
- [x] ✅ Documentation (README, CONTRIBUTING, ARCHITECTURE)

#### 🚧 En cours

- [ ] 🔄 Installer Gradle 9.2.0 + Java 17 pour développement plugins
- [ ] 🔄 Créer projet plugin Core (build successful)
- [ ] 🔄 Configuration auto-copy JAR vers serveur
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
| 🎮 Serveur en ligne | ✅ Opérationnel | 100% ████████████ |
| 🔌 Plugin Core | 🚧 En cours | 30% ███░░░░░░░░░ |
| 🎨 Graphisme | ⏳ Pas commencé | 0% ░░░░░░░░░░░░ |
| 🎮 Gameplay | 🚧 Minimal | 5% █░░░░░░░░░░░ |
| 🪙 Monétisation | ⏳ Pas commencé | 0% ░░░░░░░░░░░░ |
| 📱 Marketing | ⏳ Pas commencé | 0% ░░░░░░░░░░░░ |
| **GLOBAL** | 🚧 En cours | **35%** ████░░░░░░░░ |

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
