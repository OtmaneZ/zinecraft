# 🎮 ZineCraft Server

> Serveur Minecraft Survival+ monétisable - Par Otmane

[![Setup](https://img.shields.io/badge/Setup-✅_Complete-brightgreen)]()
[![Type](https://img.shields.io/badge/Type-Survival+-blue)]()
[![Budget](https://img.shields.io/badge/Budget-150€-yellow)]()
[![Status](https://img.shields.io/badge/Status-En_développement-orange)]()

## 📊 État Actuel du Projet

**Dernière mise à jour** : 26 novembre 2025
**Phase actuelle** : 🎨 Redesign & Assets (Pivot solo)

```
███████░░░░░░░░░░░░░░ 35% Complete (Infrastructure OK)
```

**🎮 Serveur** : `91.99.237.55:25565` (Minecraft Java 1.21)
**� Développeur** : Otmane (projet solo, Adam a abandonné)
**💰 Budget investi** : 150€ en assets premium

## 🎯 Vision du Projet

Créer un serveur Minecraft **Survival+ monétisable** avec :

- ✨ Expérience visuelle premium (assets achetés)
- 🎮 Gameplay survie amélioré (économie, jobs, quêtes)
- 💰 Monétisation intelligente (grades VIP, cosmetics)
- 📈 Infrastructure scalable et stable

**Type de serveur** : Survie Vanilla+ avec économie et système de grades
**Objectif** : Lancement en **4-6 semaines** avec première rentabilité

## 💰 Budget & Investissements (150€)

Pour accélérer le développement et garantir une qualité pro, investissement de **150€** en assets premium :

### � Assets visuels (105€)

1. **Map spawn premium** (40€) - Fiverr
   - Spawn central professionnel
   - Première impression critique pour retenir les joueurs

2. **Logo + bannière pack** (25€) - Fiverr
   - Logo serveur HD
   - Bannières Discord/Site web
   - Identité visuelle cohérente

3. **Trailer vidéo 30 sec** (40€) - Fiverr
   - Vidéo marketing professionnelle
   - Pour TikTok/YouTube/Discord
   - Attire les joueurs

### �️ Outils & Plugins (45€)

4. **Template site Tebex** (30€) - ThemeForest
   - Site web avec boutique intégrée
   - Monétisation immédiate
   - Design moderne responsive

5. **Pack plugins "Survival+"** (15€) - Polymart
   - Économie + Jobs + Quêtes préconfigurés
   - Gain de temps : 2-3 semaines de configuration
   - Système complet clé en main

### � ROI estimé

- **Investissement** : 150€
- **Gain de temps** : 3-4 semaines de développement
- **Qualité visuelle** : x10 vs fait maison
- **Rentabilité** : 10-15 ventes VIP à 10-15€ = breakeven
- **Timeline** : Rentable en 1-2 mois avec bon marketing

## 🚀 Stack Technique

### Backend & Serveur

- **PaperMC 1.21** (Java 21) - Performance optimale
- **MySQL 8.0** - Base de données (économie, joueurs, stats)
- **RCON** - Gestion à distance

### Infrastructure

- **Hetzner Cloud** - Serveur dédié Ubuntu (4GB RAM)
- **Docker Compose** - Déploiement simplifié
- **Git/GitHub** - Versioning

### Plugins (Gratuits + Premium)

- **EssentialsX** - Commandes de base + économie
- **Vault** - API économie
- **LuckPerms** - Système de grades VIP
- **WorldEdit/WorldGuard** - Protection & édition
- **Pack Survival+ Premium** (15€) - Jobs, quêtes, économie avancée
- **BuycraftX/Tebex** - Boutique en ligne (monétisation)

### Assets Premium

- Map spawn custom (Fiverr)
- Resource pack moderne
- Logo & branding
- Site web avec boutique

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

## 👥 Équipe & Contexte

**Développeur solo** : Otmane

- Infrastructure & DevOps
- Configuration plugins
- Marketing & monétisation

**Note** : Projet initialement en duo avec Adam (fils, développement plugins), qui a abandonné le 18 novembre 2025. Pivot vers approche solo avec plugins existants au lieu de développement Java from scratch.

## 📅 Roadmap Révisée (Solo)

### � Phase 1 - Achat Assets (Semaine 1) - EN COURS

**Budget** : 150€

- [ ] 🛒 Commander map spawn premium sur Fiverr (40€)
- [ ] 🛒 Commander logo + bannières sur Fiverr (25€)
- [ ] 🛒 Commander trailer vidéo sur Fiverr (40€)
- [ ] 🛒 Acheter template site Tebex sur ThemeForest (30€)
- [ ] 🛒 Acheter pack plugins Survival+ sur Polymart (15€)

### 🔧 Phase 2 - Setup & Configuration (Semaine 2-3)

**Objectif** : Serveur jouable avec économie

- [ ] ⚙️ Installer et configurer plugins essentiels
  - EssentialsX, Vault, LuckPerms
  - Pack Survival+ (jobs, économie, quêtes)
  - WorldGuard, CoreProtect
- [ ] �️ Intégrer map spawn premium
- [ ] � Configurer grades VIP (Joueur, VIP, VIP+, LEGEND)
- [ ] � Setup Tebex avec produits (grades, cosmetics)
- [ ] 🌐 Déployer site web avec boutique
- [ ] 🎨 Appliquer branding (logo, bannières)

### 🎮 Phase 3 - Gameplay & Polish (Semaine 4)

**Objectif** : Expérience joueur complète

- [ ] ⚡ Créer zones de ressources (fermes, mines)
- [ ] 📜 Configurer quêtes de démarrage
- [ ] 🏆 Système de récompenses (votes, événements)
- [ ] 🎨 Resource pack moderne installé
- [ ] 🛡️ Protection spawn + zones PvP/PvE
- [ ] 📊 Balancing économie (prix, salaires, récompenses)

### 🚀 Phase 4 - Marketing & Lancement (Semaine 5-6)

**Objectif** : Attirer les premiers joueurs

- [ ] 🎬 Publier trailer sur TikTok, YouTube, Twitter
- [ ] 💬 Créer Discord communauté (+ bot vote rewards)
- [ ] 📢 Listing sur serveurs top (MC-Market, TopG, etc.)
- [ ] 🧪 Beta test avec 5-10 joueurs
- [ ] 🐛 Correction bugs & ajustements
- [ ] 🎉 Lancement officiel + campagne marketing
- [ ] 📈 Suivi analytics (joueurs, ventes, rétention)

---

## 📈 Métriques de Progression

| Catégorie | Statut | Progression |
|-----------|--------|-------------|
| 🏗️ Infrastructure | ✅ Complet | 100% ████████████ |
| 🎮 Serveur opérationnel | ✅ OK | 100% ████████████ |
| � Budget assets | � En cours | 0% ░░░░░░░░░░░░ |
| 🔌 Plugins installés | ⏳ À faire | 0% ░░░░░░░░░░░░ |
| 🎨 Design & Map | ⏳ Commandé | 0% ░░░░░░░░░░░░ |
| 🎮 Gameplay configuré | ⏳ À faire | 0% ░░░░░░░░░░░░ |
| 🪙 Monétisation (Tebex) | ⏳ À faire | 0% ░░░░░░░░░░░░ |
| 📱 Marketing | ⏳ À faire | 0% ░░░░░░░░░░░░ |
| **GLOBAL** | 🚧 Pivot solo | **25%** ███░░░░░░░░░ |

## ✅ Changelog Important

**26 novembre 2025** - Pivot stratégique

- 🔄 Passage en mode solo (Adam a quitté le projet)
- ❌ Abandon développement plugin Java from scratch
- ✅ Nouvelle approche : plugins existants + assets premium
- 💰 Budget de 150€ alloué pour assets professionnels
- 🎯 Focus : Survival+ monétisable plutôt que serveur custom complexe

**16-18 novembre 2025** - Infrastructure

- ✅ Serveur PaperMC 1.21 opérationnel
- ✅ Docker + MySQL + RCON configurés
- ✅ Multiplayer testé avec succès
- ✅ Scripts de gestion créés

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
