#!/bin/bash

# 🏗️ Script de création de plateforme pour les démos

echo "🏗️ ========================================"
echo "🏗️   CRÉATION DE PLATEFORME"
echo "🏗️ ========================================"
echo ""
echo "Choisissez la taille de la plateforme :"
echo ""
echo "1) 🟦 Petite (50x50 blocs)"
echo "2) 🟦 Moyenne (100x100 blocs)"
echo "3) 🟦 Grande (150x150 blocs)"
echo "4) 🟦 Énorme (200x200 blocs)"
echo "5) 🗑️  Supprimer la plateforme actuelle"
echo "6) ❌ Annuler"
echo ""
read -p "Votre choix (1-6) : " choice

# Coordonnées de base (votre position)
BASE_X=123
BASE_Y=99
BASE_Z=-44

case $choice in
    1)
        SIZE=25
        echo "🏗️  Création d'une plateforme 50x50..."
        ;;
    2)
        SIZE=50
        echo "🏗️  Création d'une plateforme 100x100..."
        ;;
    3)
        SIZE=75
        echo "🏗️  Création d'une plateforme 150x150..."
        ;;
    4)
        SIZE=100
        echo "🏗️  Création d'une plateforme 200x200..."
        ;;
    5)
        echo "🗑️  Suppression de la plateforme..."
        # Remplacer par de l'air
        X1=$((BASE_X - 100))
        Z1=$((BASE_Z - 100))
        X2=$((BASE_X + 100))
        Z2=$((BASE_Z + 100))
        docker exec zinecraft-papermc rcon-cli "fill $X1 $BASE_Y $Z1 $X2 $BASE_Y $Z2 minecraft:air"
        echo "✅ Plateforme supprimée !"
        exit 0
        ;;
    6)
        echo "❌ Annulé"
        exit 0
        ;;
    *)
        echo "❌ Choix invalide"
        exit 1
        ;;
esac

# Calculer les coordonnées
X1=$((BASE_X - SIZE))
Z1=$((BASE_Z - SIZE))
X2=$((BASE_X + SIZE))
Z2=$((BASE_Z + SIZE))

# Créer la plateforme
docker exec zinecraft-papermc rcon-cli "fill $X1 $BASE_Y $Z1 $X2 $BASE_Y $Z2 minecraft:smooth_stone"

# Nettoyer au-dessus (enlever les blocs qui gênent)
docker exec zinecraft-papermc rcon-cli "fill $X1 $((BASE_Y + 1)) $Z1 $X2 $((BASE_Y + 50)) $Z2 minecraft:air"

echo "✅ Plateforme créée !"
echo ""
echo "📍 Coordonnées :"
echo "   De ($X1, $BASE_Y, $Z1)"
echo "   À  ($X2, $BASE_Y, $Z2)"
echo ""
echo "💡 Pour supprimer : relancez ce script et choisissez option 5"
