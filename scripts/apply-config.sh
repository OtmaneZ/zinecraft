#!/bin/bash
# ==========================================
# ZineCraft - Apply Server Configuration
# ==========================================
# Ce script applique automatiquement toutes les configurations au serveur

CONTAINER_NAME="zinecraft-papermc"
CONFIG_DIR="$(dirname "$0")/../config"

echo "🎮 Application des configurations ZineCraft..."

# Attendre que le serveur soit prêt
sleep 5

# Appliquer les gamerules
echo "⚙️  Application des gamerules..."
while IFS= read -r line || [ -n "$line" ]; do
    # Ignorer les commentaires et lignes vides
    if [[ "$line" =~ ^#.*$ ]] || [[ -z "$line" ]]; then
        continue
    fi
    
    # Exécuter la commande
    docker exec $CONTAINER_NAME rcon-cli "$line"
done < "$CONFIG_DIR/gamerules.txt"

# Appliquer le spawn
echo "📍 Configuration du spawn..."
while IFS= read -r line || [ -n "$line" ]; do
    if [[ "$line" =~ ^#.*$ ]] || [[ -z "$line" ]]; then
        continue
    fi
    docker exec $CONTAINER_NAME rcon-cli "$line"
done < "$CONFIG_DIR/spawn.txt"

echo "✅ Configuration appliquée avec succès !"
