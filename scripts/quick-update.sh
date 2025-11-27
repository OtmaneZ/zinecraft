#!/bin/bash

# Script RAPIDE pour déployer le plugin (sans attendre le redémarrage)
# Usage: ./scripts/quick-update.sh

echo "🔨 Compilation..."
cd /root/projects/zinecraft/plugins/ZineCraftCore
gradle build --quiet || { echo "❌ Erreur compilation !"; exit 1; }

echo "📦 Copie..."
cp -f build/libs/ZineCraftCore-1.0.0-SNAPSHOT.jar /root/projects/zinecraft/server/plugins/

echo "🔄 Redémarrage..."
docker restart zinecraft-papermc > /dev/null

echo "✅ Done! Serveur redémarre (attendez 30 sec avant de vous connecter)"
