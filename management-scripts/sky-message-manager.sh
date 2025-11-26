#!/bin/bash

# 🔥 Gestion du message de feu dans le ciel

echo "🔥 ========================================"
echo "🔥   GESTION DU MESSAGE DE FEU"
echo "🔥 ========================================"
echo ""
echo "Options :"
echo ""
echo "1) 🔥 Créer/Recréer le message 'ES-TU PRET ?'"
echo "2) 👁️  Se téléporter pour voir le message"
echo "3) ⚡ Rallumer les feux (si éteints)"
echo "4) 🗑️  Supprimer le message"
echo "5) ❌ Annuler"
echo ""
read -p "Votre choix (1-5) : " choice

Y=125

case $choice in
    1)
        echo "🔥 Création du message..."
        /root/projects/zinecraft/create-sky-message.sh
        ;;
    2)
        echo "👁️  Téléportation pour voir le message..."
        docker exec zinecraft-papermc rcon-cli "tp @a 123 118 -44"
        docker exec zinecraft-papermc rcon-cli "tp @a ~ ~ ~ facing 123 $Y -49"
        echo "✅ Regardez en haut ! 👆"
        ;;
    3)
        echo "⚡ Rallumage des feux..."
        docker exec zinecraft-papermc rcon-cli "fill 104 $((Y+1)) -53 130 $((Y+2)) -45 minecraft:fire replace minecraft:air"
        echo "✅ Feux rallumés !"
        ;;
    4)
        echo "🗑️  Suppression du message..."
        docker exec zinecraft-papermc rcon-cli "fill 104 $Y -53 130 $((Y+5)) -45 minecraft:air"
        echo "✅ Message supprimé !"
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
