# 📁 Configuration ZineCraft

Ce dossier contient **toutes les configurations versionnées** du serveur.

## 📋 Fichiers

### Templates de configuration serveur
- `server.properties.template` - Configuration principale du serveur
- `bukkit.yml.template` - Configuration Bukkit (spawns, performance)
- `spigot.yml.template` - Configuration Spigot (messages, optimisation)

### Configuration de jeu
- `gamerules.txt` - Règles du jeu (jour éternel, météo, etc)
- `spawn.txt` - Configuration du spawn
- `permissions.yml` - Permissions et grades

## 🚀 Utilisation

### En développement (local)
```bash
# Appliquer les configurations
./scripts/apply-config.sh
```

### En production (serveur distant)
```bash
# Déploiement complet (code + config)
./scripts/deploy.sh
```

## 📝 Notes importantes

- Les fichiers `.template` sont copiés vers `server/` au premier démarrage
- Les vraies configs dans `server/` ne sont PAS dans Git (elles changent en jeu)
- Modifiez toujours les templates, jamais directement les fichiers dans `server/`
- Les gamerules sont appliqués automatiquement via RCON au démarrage

## ⚙️ Personnalisation

Pour modifier une configuration :
1. Édite le fichier dans `config/`
2. Commit sur Git
3. Sur le serveur : `git pull && ./scripts/apply-config.sh`

## 🎮 Configurations de gameplay

### Jour éternel
```
gamerule doDaylightCycle false
time set 1000
```

### Farming accéléré
Dans `spigot.yml.template`, les crops poussent 50% plus vite (150 au lieu de 100)

### Moins de mobs
Spawn réduit de 30% pour de meilleures performances
