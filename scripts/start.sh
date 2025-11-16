#!/bin/bash

# ==========================================
# ZineCraft - Script de démarrage rapide
# ==========================================

echo "🎮 ZineCraft - Quick Start"
echo "=============================="
echo ""

# Vérifier Docker
if ! command -v docker &> /dev/null; then
    echo "❌ Docker n'est pas installé!"
    echo "Installer Docker: https://docs.docker.com/get-docker/"
    exit 1
fi

echo "✅ Docker détecté"

# Vérifier Docker Compose
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose n'est pas installé!"
    exit 1
fi

echo "✅ Docker Compose détecté"
echo ""

# Aller dans le dossier docker
cd "$(dirname "$0")/../docker" || exit 1

# Créer .env si n'existe pas
if [ ! -f .env ]; then
    echo "📝 Création du fichier .env..."
    cp .env.example .env
    echo "✅ Fichier .env créé"
fi

echo ""
echo "🚀 Démarrage des containers..."
echo ""

# Lancer Docker Compose
docker-compose up -d

echo ""
echo "✅ Serveur démarré!"
echo ""
echo "📊 Accès aux services:"
echo "  - Minecraft Server: localhost:25565"
echo "  - Portainer:        http://localhost:9000"
echo "  - phpMyAdmin:       http://localhost:8080"
echo ""
echo "📝 Commandes utiles:"
echo "  - Voir les logs:    docker-compose logs -f papermc"
echo "  - Arrêter:          docker-compose down"
echo "  - Redémarrer:       docker-compose restart"
echo ""
echo "🎯 Bon développement! 🚀"
