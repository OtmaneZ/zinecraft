#!/bin/bash

# Script pour compiler et déployer le plugin ZineCraftCore
# Usage: ./scripts/update-plugin.sh

set -e  # Arrêter en cas d'erreur

echo "🔨 Compilation du plugin ZineCraftCore..."
cd /root/projects/zinecraft/plugins/ZineCraftCore
gradle build --quiet

if [ $? -ne 0 ]; then
    echo "❌ Erreur de compilation !"
    exit 1
fi

echo "✅ Compilation réussie !"

echo "📦 Copie du plugin vers le serveur..."
cp -f build/libs/ZineCraftCore-1.0.0-SNAPSHOT.jar /root/projects/zinecraft/server/plugins/

echo "🔄 Redémarrage du serveur..."
docker restart zinecraft-papermc

echo "⏳ Attente du démarrage (30 secondes)..."
sleep 30

echo "📋 Vérification du serveur..."
docker logs zinecraft-papermc --tail 5 | grep -E "Done|ZineCraft"

echo ""
echo "✅ Serveur redémarré avec succès !"
echo "🎮 Vous pouvez maintenant vous reconnecter et tester vos modifications !"
