#!/bin/bash
# Script utilitaire pour Minecraft Server
# Usage: ./rcon.sh [action] [params...]

case "$1" in
  cmd|command)
    # Exécuter une commande RCON
    docker exec zinecraft-papermc rcon-cli "$2"
    ;;
  backup)
    # Backup du monde
    echo "Création du backup..."
    docker exec zinecraft-papermc rcon-cli "save-all"
    sleep 2
    tar -czf "/root/projects/zinecraft/backups/world-$(date +%Y%m%d-%H%M%S).tar.gz" -C /root/projects/zinecraft/server world
    echo "Backup créé dans backups/"
    ;;
  restart)
    # Redémarrer le serveur
    echo "Redémarrage du serveur..."
    cd /root/projects/zinecraft/docker
    docker compose restart papermc
    ;;
  logs)
    # Afficher les logs
    docker logs --tail 50 zinecraft-papermc
    ;;
  status)
    # Status du serveur
    docker ps | grep zinecraft
    ;;
  *)
    echo "Usage: ./rcon.sh [action] [params]"
    echo "Actions disponibles:"
    echo "  cmd \"commande\"    - Exécuter une commande RCON"
    echo "  backup            - Sauvegarder le monde"
    echo "  restart           - Redémarrer le serveur"
    echo "  logs              - Voir les logs"
    echo "  status            - Voir le status des containers"
    ;;
esac
