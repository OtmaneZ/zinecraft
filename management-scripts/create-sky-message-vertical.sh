#!/bin/bash

# 🔥 Script pour créer "ES-TU PRET ?" en FEU VERTICAL dans le ciel

echo "🔥 ========================================"
echo "🔥   MESSAGE VERTICAL DE FEU"
echo "🔥 ========================================"
echo ""

# Position : Nord de l'arène, visible depuis le centre
BASE_X=123
BASE_Y=110
BASE_Z=-65  # Au Nord de l'arène

echo "🗑️  Nettoyage de l'ancien message..."
docker exec zinecraft-papermc rcon-cli "fill 100 110 -70 140 135 -60 minecraft:air" > /dev/null 2>&1

echo "🔥 Création du message vertical..."

# Fonction pour créer une lettre verticale
create_letter() {
    local x=$1
    local y_start=$2
    local z=$3

    docker exec zinecraft-papermc rcon-cli "fill $x $y_start $z $x $y_start $z minecraft:netherrack" > /dev/null 2>&1
    docker exec zinecraft-papermc rcon-cli "setblock $x $((y_start+1)) $z minecraft:fire" > /dev/null 2>&1
}

# E (5x5)
echo "  Lettre E..."
for y in $(seq 0 4); do
    docker exec zinecraft-papermc rcon-cli "setblock 105 $((BASE_Y + y)) $BASE_Z minecraft:netherrack" > /dev/null 2>&1
done
for x in $(seq 106 109); do
    docker exec zinecraft-papermc rcon-cli "setblock $x $BASE_Y $BASE_Z minecraft:netherrack" > /dev/null 2>&1
    docker exec zinecraft-papermc rcon-cli "setblock $x $((BASE_Y + 2)) $BASE_Z minecraft:netherrack" > /dev/null 2>&1
    docker exec zinecraft-papermc rcon-cli "setblock $x $((BASE_Y + 4)) $BASE_Z minecraft:netherrack" > /dev/null 2>&1
done

# S (5x5)
echo "  Lettre S..."
for x in $(seq 111 114); do
    docker exec zinecraft-papermc rcon-cli "setblock $x $((BASE_Y + 4)) $BASE_Z minecraft:netherrack" > /dev/null 2>&1
    docker exec zinecraft-papermc rcon-cli "setblock $x $((BASE_Y + 2)) $BASE_Z minecraft:netherrack" > /dev/null 2>&1
    docker exec zinecraft-papermc rcon-cli "setblock $x $BASE_Y $BASE_Z minecraft:netherrack" > /dev/null 2>&1
done
docker exec zinecraft-papermc rcon-cli "setblock 111 $((BASE_Y + 3)) $BASE_Z minecraft:netherrack" > /dev/null 2>&1
docker exec zinecraft-papermc rcon-cli "setblock 114 $((BASE_Y + 1)) $BASE_Z minecraft:netherrack" > /dev/null 2>&1

# - (tiret)
echo "  Tiret..."
for x in $(seq 116 118); do
    docker exec zinecraft-papermc rcon-cli "setblock $x $((BASE_Y + 2)) $BASE_Z minecraft:netherrack" > /dev/null 2>&1
done

# T (5x5)
echo "  Lettre T..."
for x in $(seq 120 124); do
    docker exec zinecraft-papermc rcon-cli "setblock $x $((BASE_Y + 4)) $BASE_Z minecraft:netherrack" > /dev/null 2>&1
done
for y in $(seq 0 3); do
    docker exec zinecraft-papermc rcon-cli "setblock 122 $((BASE_Y + y)) $BASE_Z minecraft:netherrack" > /dev/null 2>&1
done

# U (5x5)
echo "  Lettre U..."
for y in $(seq 1 4); do
    docker exec zinecraft-papermc rcon-cli "setblock 126 $((BASE_Y + y)) $BASE_Z minecraft:netherrack" > /dev/null 2>&1
    docker exec zinecraft-papermc rcon-cli "setblock 130 $((BASE_Y + y)) $BASE_Z minecraft:netherrack" > /dev/null 2>&1
done
for x in $(seq 127 129); do
    docker exec zinecraft-papermc rcon-cli "setblock $x $BASE_Y $BASE_Z minecraft:netherrack" > /dev/null 2>&1
done

# Nouvelle ligne - descendre de 7 blocs
BASE_Y=$((BASE_Y - 7))

# P (5x5)
echo "  Lettre P..."
for y in $(seq 0 4); do
    docker exec zinecraft-papermc rcon-cli "setblock 105 $((BASE_Y + y)) $BASE_Z minecraft:netherrack" > /dev/null 2>&1
done
for x in $(seq 106 108); do
    docker exec zinecraft-papermc rcon-cli "setblock $x $((BASE_Y + 4)) $BASE_Z minecraft:netherrack" > /dev/null 2>&1
    docker exec zinecraft-papermc rcon-cli "setblock $x $((BASE_Y + 2)) $BASE_Z minecraft:netherrack" > /dev/null 2>&1
done
docker exec zinecraft-papermc rcon-cli "setblock 109 $((BASE_Y + 3)) $BASE_Z minecraft:netherrack" > /dev/null 2>&1

# R (5x5)
echo "  Lettre R..."
for y in $(seq 0 4); do
    docker exec zinecraft-papermc rcon-cli "setblock 111 $((BASE_Y + y)) $BASE_Z minecraft:netherrack" > /dev/null 2>&1
done
for x in $(seq 112 114); do
    docker exec zinecraft-papermc rcon-cli "setblock $x $((BASE_Y + 4)) $BASE_Z minecraft:netherrack" > /dev/null 2>&1
    docker exec zinecraft-papermc rcon-cli "setblock $x $((BASE_Y + 2)) $BASE_Z minecraft:netherrack" > /dev/null 2>&1
done
docker exec zinecraft-papermc rcon-cli "setblock 115 $((BASE_Y + 3)) $BASE_Z minecraft:netherrack" > /dev/null 2>&1
docker exec zinecraft-papermc rcon-cli "setblock 114 $BASE_Y $BASE_Z minecraft:netherrack" > /dev/null 2>&1
docker exec zinecraft-papermc rcon-cli "setblock 115 $((BASE_Y + 1)) $BASE_Z minecraft:netherrack" > /dev/null 2>&1

# E (5x5)
echo "  Lettre E..."
for y in $(seq 0 4); do
    docker exec zinecraft-papermc rcon-cli "setblock 117 $((BASE_Y + y)) $BASE_Z minecraft:netherrack" > /dev/null 2>&1
done
for x in $(seq 118 121); do
    docker exec zinecraft-papermc rcon-cli "setblock $x $BASE_Y $BASE_Z minecraft:netherrack" > /dev/null 2>&1
    docker exec zinecraft-papermc rcon-cli "setblock $x $((BASE_Y + 2)) $BASE_Z minecraft:netherrack" > /dev/null 2>&1
    docker exec zinecraft-papermc rcon-cli "setblock $x $((BASE_Y + 4)) $BASE_Z minecraft:netherrack" > /dev/null 2>&1
done

# T (5x5)
echo "  Lettre T..."
for x in $(seq 123 127); do
    docker exec zinecraft-papermc rcon-cli "setblock $x $((BASE_Y + 4)) $BASE_Z minecraft:netherrack" > /dev/null 2>&1
done
for y in $(seq 0 3); do
    docker exec zinecraft-papermc rcon-cli "setblock 125 $((BASE_Y + y)) $BASE_Z minecraft:netherrack" > /dev/null 2>&1
done

# ? (5x5)
echo "  Point d'interrogation..."
for x in $(seq 129 132); do
    docker exec zinecraft-papermc rcon-cli "setblock $x $((BASE_Y + 4)) $BASE_Z minecraft:netherrack" > /dev/null 2>&1
done
docker exec zinecraft-papermc rcon-cli "setblock 133 $((BASE_Y + 3)) $BASE_Z minecraft:netherrack" > /dev/null 2>&1
docker exec zinecraft-papermc rcon-cli "setblock 132 $((BASE_Y + 2)) $BASE_Z minecraft:netherrack" > /dev/null 2>&1
docker exec zinecraft-papermc rcon-cli "setblock 131 $((BASE_Y + 1)) $BASE_Z minecraft:netherrack" > /dev/null 2>&1
docker exec zinecraft-papermc rcon-cli "setblock 131 $BASE_Y $BASE_Z minecraft:netherrack" > /dev/null 2>&1

echo ""
echo "🔥 Allumage des feux..."
sleep 1

# Allumer le feu sur tous les netherrack
docker exec zinecraft-papermc rcon-cli "fill 100 109 $((BASE_Z-1)) 140 120 $((BASE_Z+1)) minecraft:fire replace minecraft:air"

echo ""
echo "✅ ========================================"
echo "✅   MESSAGE VERTICAL CRÉÉ !"
echo "✅ ========================================"
echo ""
echo "🔥 'ES-TU PRET ?' est maintenant visible depuis l'arène !"
echo "📍 Position : Nord de l'arène (Z=$BASE_Z)"
echo ""
echo "💡 Pour voir : /tp 123 100 -44 (regardez vers le Nord)"
echo "🗑️  Pour supprimer : /fill 100 100 -70 140 120 -60 air"
echo ""
