#!/bin/bash

# 🗑️ Script de reset complet du monde Minecraft

echo "⚠️  ========================================"
echo "⚠️   RESET COMPLET DU MONDE MINECRAFT"
echo "⚠️  ========================================"
echo ""
echo "❌ ATTENTION : Cette action va :"
echo "   - Supprimer TOUS les mondes (overworld, nether, end)"
echo "   - Supprimer TOUTES les constructions"
echo "   - Supprimer TOUS les coffres et items"
echo "   - Réinitialiser le spawn"
echo "   - Générer un NOUVEAU monde"
echo ""
echo "✅ Sera conservé :"
echo "   - Les plugins"
echo "   - Les configurations"
echo "   - Les permissions"
echo ""
read -p "Êtes-vous SÛR de vouloir continuer ? (tapez 'OUI' en majuscules) : " confirm

if [ "$confirm" != "OUI" ]; then
    echo "❌ Annulé. Aucun changement effectué."
    exit 0
fi

echo ""
echo "🛑 Arrêt du serveur..."
docker stop zinecraft-papermc

echo ""
echo "🗑️  Suppression des mondes..."
rm -rf /root/projects/zinecraft/server/world
rm -rf /root/projects/zinecraft/server/world_nether
rm -rf /root/projects/zinecraft/server/world_the_end

echo ""
echo "✅ Mondes supprimés !"
echo ""
echo "🔄 Redémarrage du serveur (va générer un nouveau monde)..."
docker start zinecraft-papermc

echo ""
echo "⏳ Attente de la génération du monde (30 secondes)..."
sleep 30

echo ""
echo "✅ ========================================"
echo "✅   RESET TERMINÉ !"
echo "✅ ========================================"
echo ""
echo "🌍 Un nouveau monde a été généré !"
echo "🎮 Vous pouvez vous reconnecter au serveur."
echo ""
echo "📜 Voir les logs : docker logs -f zinecraft-papermc"
echo ""
