#!/bin/bash
# ==========================================
# ZineCraft - Script de Déploiement
# ==========================================
# Utilisé sur le serveur distant pour déployer une nouvelle version

set -e  # Arrêter en cas d'erreur

echo "🚀 Déploiement ZineCraft..."

# 1. Pull du code
echo "📥 Récupération du code..."
git pull origin main

# 2. Build du plugin
echo "🔨 Compilation du plugin..."
cd plugins/ZineCraftCore
./gradlew clean build
cd ../..

# 3. Copie du JAR
echo "📦 Installation du plugin..."
cp plugins/ZineCraftCore/build/libs/ZineCraftCore-*.jar server/plugins/

# 4. Redémarrage du serveur
echo "🔄 Redémarrage du serveur..."
cd docker
docker-compose restart papermc

# 5. Attendre que le serveur redémarre
echo "⏳ Attente du démarrage..."
sleep 10

# 6. Application des configurations
echo "⚙️  Application des configurations..."
cd ..
./scripts/apply-config.sh

echo "✅ Déploiement terminé avec succès !"
