#!/bin/bash

# 🔥 Script pour créer le message "ES-TU PRET ?" en feu dans le ciel

echo "🔥 ========================================"
echo "🔥   MESSAGE DE FEU DANS LE CIEL"
echo "🔥 ========================================"
echo ""
echo "Création du message 'ES-TU PRET ?' au-dessus de l'arène..."
echo ""

# Hauteur du message
Y=125

# Position de départ (au-dessus du centre de l'arène)
START_X=105
START_Z=-52

echo "🔥 Création des lettres en netherrack..."

# Fonction pour allumer le feu
light_fire() {
    local x1=$1
    local y1=$2
    local z1=$3
    local x2=$4
    local y2=$5
    local z2=$6

    # Placer netherrack
    docker exec zinecraft-papermc rcon-cli "fill $x1 $y1 $z1 $x2 $y2 $z2 minecraft:netherrack" > /dev/null 2>&1

    # Allumer le feu sur le dessus
    for x in $(seq $x1 $x2); do
        for z in $(seq $z1 $z2); do
            docker exec zinecraft-papermc rcon-cli "setblock $x $((y2+1)) $z minecraft:fire" > /dev/null 2>&1
        done
    done
}

# E
echo "  Lettre E..."
light_fire 105 $Y -52 105 $((Y+3)) -52  # Barre verticale
light_fire 106 $Y -52 108 $Y -52         # Barre bas
light_fire 106 $((Y+2)) -52 107 $((Y+2)) -52  # Barre milieu
light_fire 106 $((Y+3)) -52 108 $((Y+3)) -52  # Barre haut

# S
echo "  Lettre S..."
light_fire 110 $((Y+3)) -52 112 $((Y+3)) -52  # Haut
light_fire 110 $((Y+2)) -52 110 $((Y+2)) -52  # Gauche milieu
light_fire 111 $((Y+1)) -52 111 $((Y+1)) -52  # Centre
light_fire 112 $Y -52 112 $Y -52                # Droite bas
light_fire 110 $Y -52 112 $Y -52                # Bas

# - (tiret)
echo "  Tiret..."
light_fire 114 $((Y+1)) -52 115 $((Y+1)) -52

# T
echo "  Lettre T..."
light_fire 117 $((Y+3)) -52 120 $((Y+3)) -52  # Barre haut
light_fire 118 $Y -52 118 $((Y+2)) -52         # Barre verticale

# U
echo "  Lettre U..."
light_fire 122 $Y -52 124 $Y -52                # Bas
light_fire 122 $((Y+1)) -52 122 $((Y+3)) -52   # Gauche
light_fire 124 $((Y+1)) -52 124 $((Y+3)) -52   # Droite

# P
echo "  Lettre P..."
light_fire 105 $Y -46 105 $((Y+3)) -46          # Barre verticale
light_fire 106 $((Y+3)) -46 107 $((Y+3)) -46   # Haut
light_fire 108 $((Y+2)) -46 108 $((Y+3)) -46   # Droite haut
light_fire 106 $((Y+2)) -46 107 $((Y+2)) -46   # Milieu

# R
echo "  Lettre R..."
light_fire 110 $Y -46 110 $((Y+3)) -46          # Barre verticale
light_fire 111 $((Y+3)) -46 112 $((Y+3)) -46   # Haut
light_fire 113 $((Y+2)) -46 113 $((Y+3)) -46   # Droite haut
light_fire 111 $((Y+2)) -46 112 $((Y+2)) -46   # Milieu
light_fire 112 $Y -46 113 $Y -46                # Jambe

# E
echo "  Lettre E..."
light_fire 115 $Y -46 115 $((Y+3)) -46         # Barre verticale
light_fire 116 $Y -46 118 $Y -46                # Barre bas
light_fire 116 $((Y+2)) -46 117 $((Y+2)) -46   # Barre milieu
light_fire 116 $((Y+3)) -46 118 $((Y+3)) -46   # Barre haut

# T
echo "  Lettre T..."
light_fire 120 $((Y+3)) -46 123 $((Y+3)) -46   # Barre haut
light_fire 121 $Y -46 121 $((Y+2)) -46          # Barre verticale

# ? (point d'interrogation)
echo "  Point d'interrogation..."
light_fire 126 $((Y+3)) -46 127 $((Y+3)) -46   # Haut
light_fire 128 $((Y+2)) -46 128 $((Y+3)) -46   # Droite haut
light_fire 127 $((Y+1)) -46 127 $((Y+1)) -46   # Centre
light_fire 127 $Y -47 127 $Y -47                # Point

echo ""
echo "🔥 Allumage des feux..."
sleep 2

# Allumer tous les feux (grand fill pour être sûr)
docker exec zinecraft-papermc rcon-cli "fill 104 $((Y+1)) -53 130 $((Y+4)) -45 minecraft:fire replace minecraft:air"

echo ""
echo "✅ ========================================"
echo "✅   MESSAGE CRÉÉ !"
echo "✅ ========================================"
echo ""
echo "🔥 Message : 'ES-TU PRET ?'"
echo "📍 Position : Au-dessus de l'arène (Y=$Y)"
echo ""
echo "💡 Pour voir : /tp 123 $((Y-5)) -44"
echo "🗑️  Pour supprimer : /fill 104 $Y -53 130 $((Y+5)) -45 air"
echo ""
