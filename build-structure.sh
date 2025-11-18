#!/bin/bash

# ==========================================
# ZineCraft - Script de construction de structures
# ==========================================

if [ $# -lt 2 ]; then
    echo "Usage: ./build-structure.sh <structure> <player>"
    echo ""
    echo "Structures disponibles:"
    ls -1 server/structures/ | sed 's/.txt//' | sed 's/^/  - /'
    echo ""
    echo "Exemple: ./build-structure.sh castle OtmaneZ"
    exit 1
fi

STRUCTURE=$1
PLAYER=$2
STRUCTURE_FILE="server/structures/${STRUCTURE}.txt"

if [ ! -f "$STRUCTURE_FILE" ]; then
    echo "❌ Structure '$STRUCTURE' non trouvée!"
    echo ""
    echo "Structures disponibles:"
    ls -1 server/structures/ | sed 's/.txt//' | sed 's/^/  - /'
    exit 1
fi

echo "🏗️  Construction de '$STRUCTURE' pour $PLAYER..."
echo ""

# Lire le fichier et exécuter chaque commande
while IFS= read -r line; do
    # Ignorer les commentaires et lignes vides
    if [[ $line =~ ^#.* ]] || [[ -z "$line" ]]; then
        continue
    fi
    
    # Remplacer @s par le nom du joueur
    cmd=$(echo "$line" | sed "s/@s/$PLAYER/g")
    
    echo "  Exécution: $cmd"
    docker exec zinecraft-papermc rcon-cli "execute at $PLAYER run $cmd" > /dev/null 2>&1
    
    # Petit délai pour éviter la surcharge
    sleep 0.1
done < "$STRUCTURE_FILE"

echo ""
echo "✅ Structure '$STRUCTURE' construite avec succès!"
echo "📍 Position: Devant $PLAYER"
