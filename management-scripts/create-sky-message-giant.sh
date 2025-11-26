#!/bin/bash

# 🔥 Message GÉANT "ES-TU PRET ?" dans le ciel (plus bas et plus gros)

echo "🔥 ========================================"
echo "🔥   MESSAGE GÉANT DANS LE CIEL"
echo "🔥 ========================================"
echo ""

# Hauteur : Plus bas pour être plus visible
Y=140

# Centre de l'arène
CENTER_X=123
CENTER_Z=-44

echo "🔥 Création du message GÉANT à Y=$Y..."
echo "   (Lettres 2x plus grandes)"

# LIGNE 1 : ES-TU

# E (grand)
echo "  E..."
docker exec zinecraft-papermc rcon-cli "fill 100 $Y -55 100 $Y -46 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 101 $Y -55 106 $Y -55 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 101 $Y -51 105 $Y -51 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 101 $Y -46 106 $Y -46 minecraft:netherrack"

# S (grand)
echo "  S..."
docker exec zinecraft-papermc rcon-cli "fill 108 $Y -55 114 $Y -55 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 108 $Y -54 108 $Y -52 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 109 $Y -51 114 $Y -51 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 114 $Y -50 114 $Y -47 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 108 $Y -46 114 $Y -46 minecraft:netherrack"

# - (tiret)
echo "  -..."
docker exec zinecraft-papermc rcon-cli "fill 117 $Y -51 120 $Y -51 minecraft:netherrack"

# T (grand)
echo "  T..."
docker exec zinecraft-papermc rcon-cli "fill 122 $Y -55 130 $Y -55 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 126 $Y -54 126 $Y -46 minecraft:netherrack"

# U (grand)
echo "  U..."
docker exec zinecraft-papermc rcon-cli "fill 132 $Y -55 132 $Y -47 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 140 $Y -55 140 $Y -47 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 133 $Y -46 139 $Y -46 minecraft:netherrack"

# LIGNE 2 : PRET ?

# P (grand)
echo "  P..."
docker exec zinecraft-papermc rcon-cli "fill 100 $Y -42 100 $Y -33 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 101 $Y -42 106 $Y -42 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 107 $Y -41 107 $Y -38 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 101 $Y -37 106 $Y -37 minecraft:netherrack"

# R (grand)
echo "  R..."
docker exec zinecraft-papermc rcon-cli "fill 109 $Y -42 109 $Y -33 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 110 $Y -42 115 $Y -42 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 116 $Y -41 116 $Y -38 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 110 $Y -37 115 $Y -37 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 114 $Y -36 116 $Y -34 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 117 $Y -33 117 $Y -33 minecraft:netherrack"

# E (grand)
echo "  E..."
docker exec zinecraft-papermc rcon-cli "fill 119 $Y -42 119 $Y -33 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 120 $Y -42 125 $Y -42 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 120 $Y -38 124 $Y -38 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 120 $Y -33 125 $Y -33 minecraft:netherrack"

# T (grand)
echo "  T..."
docker exec zinecraft-papermc rcon-cli "fill 127 $Y -42 135 $Y -42 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 131 $Y -41 131 $Y -33 minecraft:netherrack"

# ? (grand)
echo "  ?..."
docker exec zinecraft-papermc rcon-cli "fill 138 $Y -42 144 $Y -42 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 145 $Y -41 145 $Y -39 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 143 $Y -38 144 $Y -38 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 142 $Y -37 142 $Y -36 minecraft:netherrack"
docker exec zinecraft-papermc rcon-cli "fill 142 $Y -34 142 $Y -33 minecraft:netherrack"

echo ""
echo "🔥 Allumage des feux MASSIFS..."
sleep 1

# Allumer TOUT le feu au-dessus (2 couches pour plus de flammes)
docker exec zinecraft-papermc rcon-cli "fill 99 $((Y+1)) -56 146 $((Y+1)) -32 minecraft:fire replace minecraft:air"
docker exec zinecraft-papermc rcon-cli "fill 99 $((Y+2)) -56 146 $((Y+2)) -32 minecraft:fire replace minecraft:air"

echo ""
echo "✅ ========================================"
echo "✅   MESSAGE GÉANT CRÉÉ !"
echo "✅ ========================================"
echo ""
echo "🔥 'ES-TU PRET ?' GÉANT dans le ciel à Y=$Y"
echo "📍 2x plus GRAND et plus BAS (plus visible !)"
echo ""
echo "💡 Depuis l'arène : /tp 123 100 -44 (regardez en haut 👆)"
echo "🗑️  Pour supprimer : /fill 99 $Y -56 146 $((Y+3)) -32 air"
echo ""
