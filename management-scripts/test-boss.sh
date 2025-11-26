#!/bin/bash

# 🔥 Script de test rapide des boss ZineCraft 🔥

echo "🎮 ========================================"
echo "🎮     TEST DES BOSS ZINECRAFT"
echo "🎮 ========================================"
echo ""

# Vérifier si le serveur tourne
echo "📡 Vérification du serveur..."
if docker ps | grep -q zinecraft-papermc; then
    echo "✅ Serveur actif !"
else
    echo "❌ Serveur inactif. Démarrage..."
    cd /root/projects/zinecraft/docker
    docker compose up -d
    echo "⏳ Attente du démarrage (30 secondes)..."
    sleep 30
fi

echo ""
echo "📊 Status des conteneurs :"
docker ps | grep zinecraft

echo ""
echo "🎮 ========================================"
echo "🎮  SERVEUR PRÊT !"
echo "🎮 ========================================"
echo ""
echo "📌 IP du serveur : 91.99.237.55:25565"
echo "🎯 Version : Minecraft Java 1.21"
echo ""
echo "🔥 COMMANDES BOSS DISPONIBLES :"
echo "   /boss titan      - Titan Zombie (200 HP)"
echo "   /boss dragon     - Dragon Skeleton (350 HP)"
echo "   /boss demon      - Demon Blaze (500 HP)"
echo "   /boss firedragon - Dragon de Feu (800 HP) 🔥"
echo "   /boss icegolem   - Golem de Glace (1000 HP) ❄️"
echo "   /boss shadow     - Titan des Ombres (1500 HP) 💀"
echo ""
echo "📖 Guide complet : /root/projects/zinecraft/TEST_BOSS.md"
echo ""
echo "💡 TIPS :"
echo "   - Utilisez /gamemode creative pour ne pas mourir"
echo "   - Utilisez /time set night pour mieux voir les effets"
echo "   - Le Titan des Ombres est GÉANT (12 blocs) !"
echo ""
echo "📜 Logs en temps réel :"
echo "   docker logs -f zinecraft-papermc"
echo ""
