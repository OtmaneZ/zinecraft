#!/bin/bash

# 🧹 Script de nettoyage de la map ZineCraft

echo "🧹 ========================================"
echo "🧹   NETTOYAGE DE LA MAP ZINECRAFT"
echo "🧹 ========================================"
echo ""

# Menu
echo "Que voulez-vous nettoyer ?"
echo ""
echo "1) 💀 Tuer tous les boss et mobs"
echo "2) 🗑️  Supprimer tous les items au sol"
echo "3) 🧹 Nettoyer TOUT (boss + items)"
echo "4) 🔄 Téléporter au spawn"
echo "5) ⚡ Reset météo (enlever pluie/orage)"
echo "6) 🌞 Mettre le jour"
echo "7) ❌ Annuler"
echo ""
read -p "Votre choix (1-7) : " choice

case $choice in
    1)
        echo "💀 Suppression de tous les boss et mobs..."
        docker exec zinecraft-papermc rcon-cli 'kill @e[type=!player]'
        echo "✅ Terminé !"
        ;;
    2)
        echo "🗑️  Suppression de tous les items..."
        docker exec zinecraft-papermc rcon-cli 'kill @e[type=item]'
        echo "✅ Terminé !"
        ;;
    3)
        echo "🧹 Nettoyage complet..."
        docker exec zinecraft-papermc rcon-cli 'kill @e[type=!player]'
        docker exec zinecraft-papermc rcon-cli 'kill @e[type=item]'
        echo "✅ Terminé !"
        ;;
    4)
        echo "🔄 Téléportation au spawn..."
        docker exec zinecraft-papermc rcon-cli 'tp @a 0 100 0'
        echo "✅ Terminé !"
        ;;
    5)
        echo "⚡ Reset météo..."
        docker exec zinecraft-papermc rcon-cli 'weather clear'
        echo "✅ Terminé !"
        ;;
    6)
        echo "🌞 Mise au jour..."
        docker exec zinecraft-papermc rcon-cli 'time set day'
        echo "✅ Terminé !"
        ;;
    7)
        echo "❌ Annulé"
        exit 0
        ;;
    *)
        echo "❌ Choix invalide"
        exit 1
        ;;
esac

echo ""
echo "✨ Map nettoyée !"
