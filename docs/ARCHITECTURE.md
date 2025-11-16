# 🏗️ Architecture ZineCraft

## Vue d'ensemble

```
┌─────────────────────────────────────────────────────────┐
│                    Joueurs Minecraft                     │
└───────────────────────┬─────────────────────────────────┘
                        │ Port 25565
                        ▼
┌─────────────────────────────────────────────────────────┐
│                   PaperMC Server                         │
│  ┌────────────────────────────────────────────────────┐ │
│  │           ZineCraft Core Plugin (Java)             │ │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐          │ │
│  │  │ Skills   │ │ Quests   │ │ Pets     │          │ │
│  │  │ Manager  │ │ Manager  │ │ Manager  │          │ │
│  │  └────┬─────┘ └────┬─────┘ └────┬─────┘          │ │
│  │       └────────────┼────────────┘                 │ │
│  │                    ▼                               │ │
│  │           ┌─────────────────┐                     │ │
│  │           │ Database Manager│                     │ │
│  │           └────────┬────────┘                     │ │
│  └────────────────────┼──────────────────────────────┘ │
└────────────────────────┼────────────────────────────────┘
                         │
                         ▼
              ┌──────────────────┐
              │  MySQL Database  │
              └──────────────────┘
```

## Structure des Packages

```
fr.zinecraft.core/
├── ZineCraftCore.java          # Classe principale du plugin
├── commands/                   # Toutes les commandes
│   ├── CommandManager.java
│   ├── ZCCommand.java
│   ├── SkillsCommand.java
│   ├── QuestsCommand.java
│   └── PetsCommand.java
├── listeners/                  # Event listeners
│   ├── PlayerJoinListener.java
│   ├── PlayerQuitListener.java
│   ├── SkillListener.java
│   └── QuestListener.java
├── managers/                   # Gestionnaires métier
│   ├── DatabaseManager.java
│   ├── PlayerManager.java
│   ├── SkillManager.java
│   ├── QuestManager.java
│   ├── PetManager.java
│   └── EconomyManager.java
├── models/                     # Classes de données
│   ├── ZCPlayer.java
│   ├── Skill.java
│   ├── Quest.java
│   ├── Pet.java
│   └── Transaction.java
├── utils/                      # Utilitaires
│   ├── ConfigUtil.java
│   ├── MessageUtil.java
│   ├── ItemBuilder.java
│   └── TimeUtil.java
└── api/                        # API publique (pour autres plugins)
    └── ZineCraftAPI.java
```

## Flow des données

### Connexion d'un joueur
```
Joueur rejoint
    ↓
PlayerJoinListener
    ↓
PlayerManager.loadPlayer(uuid)
    ↓
DatabaseManager.getPlayer(uuid)
    ↓
MySQL Query
    ↓
ZCPlayer créé en mémoire
    ↓
Scoreboard affiché
```

### Gain d'XP dans un skill
```
Joueur mine un bloc
    ↓
SkillListener.onBlockBreak()
    ↓
SkillManager.addExperience(player, MINING, 10)
    ↓
Calcul level up ?
    ↓
DatabaseManager.updateSkill()
    ↓
Message au joueur
```

## Technologies

### Backend
- **Java 17** - Langage
- **Paper API 1.20** - API Minecraft
- **Gradle 8** - Build tool
- **HikariCP** - Connection pool BDD
- **MySQL** - Base de données

### Infrastructure
- **Docker** - Containerisation
- **Docker Compose** - Orchestration
- **GitHub Actions** - CI/CD

## Patterns de conception utilisés

1. **Singleton** - Pour les managers (accès global)
2. **Factory** - Pour créer les objets complexes
3. **Observer** - Pour les events Bukkit
4. **Builder** - Pour construire des items/menus
5. **Repository** - Pour l'accès aux données

## Scalabilité

### Phase 1 - Mono-serveur (actuel)
```
1 serveur PaperMC + 1 MySQL
```

### Phase 2 - Multi-serveurs (futur)
```
Proxy (BungeeCord/Velocity)
    ↓
Serveur 1, Serveur 2, Serveur 3...
    ↓
Redis (cache + pub/sub)
    ↓
MySQL Master-Slave
```

## Sécurité

- Passwords hashés (BCrypt)
- Prepared statements (anti-SQL injection)
- Rate limiting sur les commandes
- Validation des inputs
- Permissions strictes

## Performance

- Connection pooling (HikariCP)
- Cache en mémoire des joueurs online
- Async queries pour BDD
- Batch updates quand possible
- Optimisation des requêtes SQL
