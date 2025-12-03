#!/bin/bash

# Script pour compiler et tester le Désert Mortel
# Usage: ./test-desert.sh

echo "================================================"
echo "🏜️  ZINECRAFT - TEST DÉSERT MORTEL"
echo "================================================"
echo ""

# 1. Compiler le plugin
echo "📦 Compilation du plugin..."
cd plugins/ZineCraftCore
./gradlew clean shadowJar

if [ $? -ne 0 ]; then
    echo "❌ Erreur de compilation!"
    exit 1
fi

echo "✅ Compilation réussie!"
echo ""

# 2. Afficher les nouvelles classes
echo "📋 Nouvelles classes ajoutées:"
echo "  • DeadlyDesertZoneBuilder.java"
echo "  • DeadlyDesertCommand.java"
echo "  • SandstormManager.java"
echo "  • DesertTeleportCommand.java"
echo "  • DesertZoneListener.java"
echo ""

# 3. Afficher les commandes disponibles
echo "🎮 Commandes disponibles:"
echo "  /desert              - Générer le désert"
echo "  /tpdesert            - Se téléporter au désert"
echo "  /gotodesert          - Alias de /tpdesert"
echo ""

# 4. Afficher les coordonnées
echo "📍 Coordonnées du désert:"
echo "  Centre: -500, 65, 500"
echo "  Rayon: 150 blocs"
echo "  Niveau: 30-40"
echo ""

# 5. Caractéristiques
echo "🎯 Caractéristiques:"
echo "  ✓ Tempête de sable permanente"
echo "  ✓ Pyramide massive (50x50x40)"
echo "  ✓ 6 cratères de météorites"
echo "  ✓ Village abandonné (10 maisons)"
echo "  ✓ Oasis avec PNJ marchand"
echo "  ✓ Boss DEMON_BLAZE (sommet pyramide)"
echo "  ✓ 200+ cactus et décoration"
echo ""

# 6. Instructions de test
echo "🧪 Pour tester:"
echo "  1. Démarrer le serveur: cd ../../ && ./scripts/start.sh"
echo "  2. Se connecter en jeu"
echo "  3. Taper: /desert"
echo "  4. Attendre 2-5 minutes"
echo "  5. Taper: /tpdesert"
echo "  6. Explorer le désert!"
echo ""

echo "================================================"
echo "✅ Prêt pour les tests!"
echo "================================================"
