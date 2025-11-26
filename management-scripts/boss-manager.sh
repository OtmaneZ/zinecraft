#!/bin/bash

# 🎮 Script de gestion des boss

echo "🎮 ========================================"
echo "🎮     GESTION DES BOSS"
echo "🎮 ========================================"
echo ""
echo "Que voulez-vous faire ?"
echo ""
echo "1) 🔥 Spawn un boss"
echo "2) ☠️  Supprimer tous les boss"
echo "3) 🎯 Supprimer le boss le plus proche"
echo "4) 🧹 Nettoyer tout (boss + items)"
echo "5) ❌ Annuler"
echo ""
read -p "Votre choix (1-5) : " choice

case $choice in
    1)
        echo ""
        echo "🔥 Quel boss voulez-vous spawn ?"
        echo ""
        echo "  1) ⚔️  Titan Zombie (200 HP) - Facile"
        echo "  2) ☠️  Dragon Skeleton (350 HP) - Moyen"
        echo "  3) 🔥 Demon Blaze (500 HP) - Difficile"
        echo "  4) 🔥 Dragon de Feu (800 HP) - ÉPIQUE"
        echo "  5) ❄️  Golem de Glace (1000 HP) - ÉPIQUE"
        echo "  6) 💀 Titan des Ombres (1500 HP) - LÉGENDAIRE"
        echo ""
        read -p "Boss (1-6) : " boss_choice

        case $boss_choice in
            1)
                echo "⚔️  Spawn du Titan Zombie..."
                docker exec zinecraft-papermc rcon-cli "execute as @a run boss titan"
                ;;
            2)
                echo "☠️  Spawn du Dragon Skeleton..."
                docker exec zinecraft-papermc rcon-cli "execute as @a run boss dragon"
                ;;
            3)
                echo "🔥 Spawn du Demon Blaze..."
                docker exec zinecraft-papermc rcon-cli "execute as @a run boss demon"
                ;;
            4)
                echo "🔥🔥🔥 Spawn du Dragon de Feu..."
                docker exec zinecraft-papermc rcon-cli "execute as @a run boss firedragon"
                ;;
            5)
                echo "❄️❄️❄️ Spawn du Golem de Glace..."
                docker exec zinecraft-papermc rcon-cli "execute as @a run boss icegolem"
                ;;
            6)
                echo "💀💀💀 Spawn du TITAN DES OMBRES..."
                docker exec zinecraft-papermc rcon-cli "execute as @a run boss shadow"
                ;;
            *)
                echo "❌ Choix invalide"
                exit 1
                ;;
        esac
        echo "✅ Boss spawné !"
        ;;

    2)
        echo "☠️  Suppression de tous les boss et mobs..."
        result=$(docker exec zinecraft-papermc rcon-cli "kill @e[type=!player]")
        echo "$result"
        echo "✅ Tous les boss ont été supprimés !"
        ;;

    3)
        echo "🎯 Suppression du boss le plus proche..."
        result=$(docker exec zinecraft-papermc rcon-cli "kill @e[type=!player,limit=1,sort=nearest]")
        echo "$result"
        echo "✅ Boss le plus proche supprimé !"
        ;;

    4)
        echo "🧹 Nettoyage complet..."
        echo "  ☠️  Suppression des boss et mobs..."
        result1=$(docker exec zinecraft-papermc rcon-cli "kill @e[type=!player]")
        echo "  $result1"
        echo "  🗑️  Suppression des items au sol..."
        result2=$(docker exec zinecraft-papermc rcon-cli "kill @e[type=item]")
        echo "  $result2"
        echo "✅ Nettoyage complet terminé !"
        ;;

    5)
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
echo ""
echo "💡 Commandes utiles :"
echo "   /kill @e[type=!player]  - Tuer tous les boss"
echo "   /kill @e[type=item]     - Supprimer les items"
echo "   /boss <type>            - Spawn un boss"
echo ""
