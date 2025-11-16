#!/bin/bash

# ==========================================
# ZineCraft - Script de build du plugin
# ==========================================

echo "🔨 Building ZineCraft Core Plugin..."
echo "======================================"
echo ""

cd "$(dirname "$0")/../plugins/ZineCraftCore" || exit 1

# Build avec Gradle
./gradlew clean build

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Build réussi!"
    echo "📦 JAR créé dans: build/libs/"
    echo "📋 Copié automatiquement dans: ../../server/plugins/"
    echo ""
    echo "🔄 Pour appliquer les changements, redémarrez le serveur:"
    echo "   cd ../../docker"
    echo "   docker-compose restart papermc"
else
    echo ""
    echo "❌ Build échoué!"
    exit 1
fi
