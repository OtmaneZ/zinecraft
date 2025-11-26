#!/bin/bash

# 🌍 Script pour créer un monde PLAT (Superflat)

echo "🌍 ========================================"
echo "🌍   CRÉATION D'UN MONDE PLAT"
echo "🌍 ========================================"
echo ""
echo "⚠️  ATTENTION : Cela va remplacer le monde actuel !"
echo ""
read -p "Êtes-vous SÛR ? (tapez 'OUI' en majuscules) : " confirm

if [ "$confirm" != "OUI" ]; then
    echo "❌ Annulé"
    exit 0
fi

echo ""
echo "🛑 Arrêt du serveur..."
docker stop zinecraft-papermc

echo ""
echo "📦 Sauvegarde de l'ancien monde..."
timestamp=$(date +%Y%m%d_%H%M%S)
mkdir -p /root/projects/zinecraft/backups
tar -czf /root/projects/zinecraft/backups/world_backup_$timestamp.tar.gz \
    /root/projects/zinecraft/server/world* 2>/dev/null

echo ""
echo "🗑️  Suppression de l'ancien monde..."
rm -rf /root/projects/zinecraft/server/world
rm -rf /root/projects/zinecraft/server/world_nether
rm -rf /root/projects/zinecraft/server/world_the_end

echo ""
echo "📝 Configuration pour monde PLAT..."

# Créer le fichier de configuration pour monde plat
cat > /root/projects/zinecraft/server/bukkit.yml << 'EOF'
settings:
  allow-end: true
  warn-on-overload: true
  permissions-file: permissions.yml
  update-folder: update
  plugin-profiling: false
  connection-throttle: 4000
  query-plugins: true
  deprecated-verbose: default
  shutdown-message: Le serveur redémarre!
spawn-limits:
  monsters: 70
  animals: 10
  water-animals: 15
  water-ambient: 20
  ambient: 15
chunk-gc:
  period-in-ticks: 600
ticks-per:
  animal-spawns: 400
  monster-spawns: 1
  water-spawns: 1
  water-ambient-spawns: 1
  ambient-spawns: 1
  autosave: 6000
EOF

# Configurer server.properties pour monde plat
sed -i 's/level-type=.*/level-type=flat/' /root/projects/zinecraft/server/server.properties
sed -i 's/generate-structures=.*/generate-structures=false/' /root/projects/zinecraft/server/server.properties

# Configuration du terrain plat (1 couche de bedrock + 2 couches de stone + 1 couche de grass)
echo "generator-settings=minecraft:bedrock,2*minecraft:stone,minecraft:grass_block;minecraft:plains" >> /root/projects/zinecraft/server/server.properties

echo ""
echo "🔄 Redémarrage du serveur..."
docker start zinecraft-papermc

echo ""
echo "⏳ Génération du monde plat en cours (30 secondes)..."
sleep 30

echo ""
echo "✅ ========================================"
echo "✅   MONDE PLAT CRÉÉ !"
echo "✅ ========================================"
echo ""
echo "🌍 Vous avez maintenant un monde PLAT infini !"
echo "📦 Ancien monde sauvegardé dans : backups/world_backup_$timestamp.tar.gz"
echo ""
echo "🎮 Connectez-vous et commencez à construire !"
echo "📍 Spawn par défaut : 0, 64, 0"
echo ""
echo "💡 Commandes utiles :"
echo "   /tp 0 64 0        - Aller au spawn"
echo "   /setworldspawn    - Définir le spawn"
echo "   /gamemode creative - Mode créatif pour build"
echo ""
