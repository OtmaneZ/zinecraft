#!/bin/bash

# 🏟️ Script de gestion de l'arène de boss

echo "🏟️ ========================================"
echo "🏟️     GESTION DE L'ARÈNE DE BOSS"
echo "🏟️ ========================================"
echo ""
echo "Options disponibles :"
echo ""
echo "1) 🚪 Ouvrir les portes de l'arène"
echo "2) 🔒 Fermer les portes de l'arène"
echo "3) 📍 Se téléporter au centre de l'arène"
echo "4) 📍 Se téléporter à l'entrée de l'arène"
echo "5) 🗑️  Supprimer l'arène"
echo "6) ❌ Annuler"
echo ""
read -p "Votre choix (1-6) : " choice

case $choice in
    1)
        echo "🚪 Ouverture des portes..."
        docker exec zinecraft-papermc rcon-cli 'setblock 121 100 -29 minecraft:air'
        docker exec zinecraft-papermc rcon-cli 'setblock 121 101 -29 minecraft:air'
        docker exec zinecraft-papermc rcon-cli 'setblock 125 100 -29 minecraft:air'
        docker exec zinecraft-papermc rcon-cli 'setblock 125 101 -29 minecraft:air'
        echo "✅ Portes ouvertes !"
        ;;
    2)
        echo "🔒 Fermeture des portes..."
        docker exec zinecraft-papermc rcon-cli 'setblock 121 100 -29 minecraft:iron_door[half=lower,facing=north]'
        docker exec zinecraft-papermc rcon-cli 'setblock 121 101 -29 minecraft:iron_door[half=upper,facing=north]'
        docker exec zinecraft-papermc rcon-cli 'setblock 125 100 -29 minecraft:iron_door[half=lower,facing=north]'
        docker exec zinecraft-papermc rcon-cli 'setblock 125 101 -29 minecraft:iron_door[half=upper,facing=north]'
        echo "✅ Portes fermées !"
        ;;
    3)
        echo "📍 Téléportation au centre de l'arène..."
        docker exec zinecraft-papermc rcon-cli 'tp @a 123 100 -44'
        echo "✅ Téléporté !"
        ;;
    4)
        echo "📍 Téléportation à l'entrée de l'arène..."
        docker exec zinecraft-papermc rcon-cli 'tp @a 123 100 -27'
        echo "✅ Téléporté !"
        ;;
    5)
        echo "🗑️  Suppression de l'arène..."
        echo "⚠️  Êtes-vous sûr ? (tapez OUI)"
        read -p "> " confirm
        if [ "$confirm" = "OUI" ]; then
            # Supprimer le sol
            docker exec zinecraft-papermc rcon-cli 'fill 108 99 -59 138 99 -29 minecraft:smooth_stone'
            # Supprimer les murs et tours
            docker exec zinecraft-papermc rcon-cli 'fill 108 100 -59 138 110 -29 minecraft:air'
            echo "✅ Arène supprimée !"
        else
            echo "❌ Annulé"
        fi
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

echo ""
echo "✨ Terminé !"
