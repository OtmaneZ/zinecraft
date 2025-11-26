#!/bin/bash

# 🔥 Message "ES-TU PRET ?" À PLAT dans le ciel (lisible d'en bas)

echo "🔥 ========================================"
echo "🔥   MESSAGE DANS LE CIEL"
echo "🔥 ========================================"
echo ""

# Hauteur du message (TRÈS HAUT dans le ciel)
Y=200

# Centre de l'arène
CENTER_X=123
CENTER_Z=-44

echo "🔥 Création du message à Y=$Y..."

# E
echo "  E..."
docker exec zinecraft-papermc rcon-cli "fill 105 $Y -50 105 $Y -46 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 106 $Y -50 109 $Y -50 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 106 $Y -48 108 $Y -48 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 106 $Y -46 109 $Y -46 minecraft:netherrack"

# S
echo "  S..."
docker exec zinecraft-papermc rcon-cli "fill 111 $Y -50 114 $Y -50 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 111 $Y -49 111 $Y -49 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 111 $Y -48 114 $Y -48 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 114 $Y -47 114 $Y -47 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 111 $Y -46 114 $Y -46 minecraft:netherrack"

# - (tiret)
echo "  -..."
docker exec zinecraft-papermc rcon-cli "fill 116 $Y -48 118 $Y -48 minecraft:netherrack"

# T
echo "  T..."
docker exec zinecraft-papermc rcon-cli "fill 120 $Y -50 124 $Y -50 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 122 $Y -49 122 $Y -46 minecraft:netherrack"

# U
echo "  U..."
docker exec zinecraft-papermc rcon-cli "fill 126 $Y -50 126 $Y -47 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 130 $Y -50 130 $Y -47 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 127 $Y -46 129 $Y -46 minecraft:netherrack"

# Deuxième ligne : PRET ?
# P
echo "  P..."
docker exec zinecraft-papermc rcon-cli "fill 105 $Y -42 105 $Y -38 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 106 $Y -42 108 $Y -42 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 109 $Y -41 109 $Y -42 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 106 $Y -40 108 $Y -40 minecraft:netherrack"

# R
echo "  R..."
docker exec zinecraft-papermc rcon-cli "fill 111 $Y -42 111 $Y -38 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 112 $Y -42 114 $Y -42 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 115 $Y -41 115 $Y -42 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 112 $Y -40 114 $Y -40 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 114 $Y -39 115 $Y -38 minecraft:netherrack"

# E
echo "  E..."
docker exec zinecraft-papermc rcon-cli "fill 117 $Y -42 117 $Y -38 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 118 $Y -42 121 $Y -42 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 118 $Y -40 120 $Y -40 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 118 $Y -38 121 $Y -38 minecraft:netherrack"

# T
echo "  T..."
docker exec zinecraft-papermc rcon-cli "fill 123 $Y -42 127 $Y -42 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 125 $Y -41 125 $Y -38 minecraft:netherrack"

# ?
echo "  ?..."
docker exec zinecraft-papermc rcon-cli "fill 129 $Y -42 132 $Y -42 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 133 $Y -41 133 $Y -41 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 132 $Y -40 132 $Y -40 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 131 $Y -39 131 $Y -39 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 131 $Y -37 131 $Y -37 minecraft:netherrack"

echo ""
echo "🔥 Allumage des feux..."
sleep 1

# Allumer TOUT le feu au-dessus
docker exec zinecraft-papermc rcon-cli "fill 104 $((Y+1)) -51 135 $((Y+1)) -36 minecraft:fire replace minecraft:air"

echo ""
echo "✅ ========================================"
echo "✅   MESSAGE CRÉÉ DANS LE CIEL !"
echo "✅ ========================================"
echo ""
echo "🔥 'ES-TU PRET ?' est écrit dans le ciel à Y=$Y"
echo "📍 Visible depuis l'arène en regardant EN HAUT ! 👆"
echo ""
echo "💡 Commandes utiles :"
echo "   /tp 123 100 -44  (centre arène, regardez en haut)"
echo "   /tp 123 190 -44  (monter pour voir de près)"
echo ""
echo "🗑️  Pour supprimer : /fill 104 $Y 51 135 $((Y+2)) -36 air"
echo ""
