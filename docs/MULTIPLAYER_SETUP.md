# 🎮 Guide de connexion Multi-joueurs - ZineCraft

> Comment jouer ensemble sur le serveur (Mac + PC)

---

## 🏠 **MÉTHODE 1 : Réseau Local (Même WiFi) - RECOMMANDÉ**

### **✅ Avantages**
- Gratuit
- Rapide (0ms latency)
- Facile à configurer
- Fonctionne toujours

---

### **📋 Étapes :**

#### **1️⃣ Sur le Mac (Otmane - Serveur)**

**A. Démarrer le serveur**
```bash
cd ~/Documents/Zinecraft/docker
docker-compose up -d
```

**B. Trouver ton IP locale**
```bash
# Si Ethernet
ipconfig getifaddr en0

# Si WiFi
ipconfig getifaddr en1

# Ou plus simple
ifconfig | grep "inet " | grep -v 127.0.0.1
```

Tu vas avoir quelque chose comme : **`192.168.1.10`** ou **`192.168.0.15`**

**C. Vérifier que le serveur est lancé**
```bash
docker-compose ps
# papermc doit être "Up"

docker-compose logs -f papermc
# Tu dois voir: "Done! For help, type "help""
```

---

#### **2️⃣ Sur le PC (Adam - Client)**

**A. Ouvrir Minecraft Java Edition**

**B. Multijoueur → Ajouter un serveur**

**C. Remplir :**
- **Nom du serveur** : `ZineCraft - Papa`
- **Adresse** : `192.168.1.XX:25565` (remplace XX par l'IP de Papa)
  - Exemple : `192.168.1.10:25565`
  - ⚠️ Le `:25565` est important !

**D. Rejoindre le serveur !** 🎮

---

### **🆘 Problèmes fréquents**

#### ❌ **"Can't reach server"**

**Solution 1 : Vérifier le firewall Mac**
```bash
# Ouvrir les Préférences Système → Sécurité → Pare-feu
# Autoriser les connexions entrantes pour Docker
```

**Solution 2 : Ping test depuis le PC d'Adam**
```cmd
# Sur Windows CMD
ping 192.168.1.XX

# Doit répondre, sinon problème réseau
```

**Solution 3 : Vérifier que Docker écoute**
```bash
# Sur Mac
lsof -i :25565
# Doit montrer docker-proxy
```

---

#### ❌ **"Outdated client/server"**
Vérifiez que vous avez **tous les deux la version 1.20.4** de Minecraft

---

#### ❌ **"Failed to verify username"**
Le serveur est en `ONLINE_MODE: false` pour les tests, donc pas besoin de compte Mojang premium

---

## 🌍 **MÉTHODE 2 : Internet (depuis l'extérieur)**

Si Adam veut se connecter depuis un autre endroit (pas la maison) :

### **Option A : Serveur Cloud (Recommandé pour production)**

**Services :**
- **OVH Game** : ~5€/mois
- **Scaleway** : ~10€/mois
- **DigitalOcean** : ~12$/mois

**Avantages :**
- ✅ Accessible 24/7
- ✅ IP publique fixe
- ✅ Bonne bande passante
- ✅ Pas de config réseau

---

### **Option B : Port Forwarding (Gratuit mais compliqué)**

⚠️ **Attention** : Expose ton réseau domestique !

**Étapes (si vraiment nécessaire) :**

1. **Configurer la box Internet**
   - Se connecter à l'interface (192.168.1.1 ou 192.168.0.1)
   - Aller dans "NAT / PAT" ou "Port Forwarding"
   - Ajouter une règle :
     - Port externe : `25565`
     - Port interne : `25565`
     - IP locale : `192.168.1.XX` (ton Mac)
     - Protocol : `TCP/UDP`

2. **Trouver ton IP publique**
   ```bash
   curl ifconfig.me
   ```

3. **Adam se connecte avec cette IP**
   - Adresse : `XX.XX.XX.XX:25565`

⚠️ **Risques de sécurité** :
- Ton IP est exposée
- Possible attaque DDoS
- Configuration firewall nécessaire

**👉 Ne pas utiliser pour production !**

---

### **Option C : Playit.gg (Gratuit et sécurisé)**

**Service gratuit de tunneling** : https://playit.gg

**Étapes :**
1. Créer un compte sur playit.gg
2. Télécharger le client Mac
3. Configurer le tunnel port 25565
4. Récupérer l'URL : `xxxxx.playit.gg`
5. Adam se connecte avec cette URL

**Avantages :**
- ✅ Gratuit
- ✅ Pas de config réseau
- ✅ Pas d'exposition de ton IP
- ✅ Facile à configurer

---

## 🛠️ **Commandes utiles**

### **Vérifier l'état du serveur**
```bash
cd ~/Documents/Zinecraft/docker
docker-compose ps
```

### **Voir les logs en temps réel**
```bash
docker-compose logs -f papermc
```

### **Arrêter le serveur**
```bash
docker-compose down
```

### **Redémarrer le serveur**
```bash
docker-compose restart papermc
```

### **Nettoyer et rebuild**
```bash
docker-compose down
docker-compose up -d --force-recreate
```

---

## 🎯 **Configuration recommandée**

### **Pour le développement (maintenant) :**
- ✅ Réseau local (`192.168.1.XX`)
- ✅ `ONLINE_MODE: false`
- ✅ Vous deux sur le même WiFi

### **Pour la production (lancement) :**
- ✅ Serveur Cloud (OVH/Scaleway)
- ✅ `ONLINE_MODE: true`
- ✅ Domaine : `play.zinecraft.fr`

---

## 📊 **Test de connexion**

### **Checklist avant de jouer :**

**Sur le Mac (Otmane) :**
- [ ] Docker est lancé
- [ ] `docker-compose ps` montre papermc "Up"
- [ ] Logs montrent "Done! For help..."
- [ ] IP locale trouvée : `192.168.1.__`

**Sur le PC (Adam) :**
- [ ] Minecraft version 1.20.4
- [ ] Adresse serveur : `192.168.1.XX:25565`
- [ ] Même réseau WiFi que Papa

**Test :**
- [ ] Connexion réussie ✅
- [ ] Peut se déplacer
- [ ] Voit l'autre joueur
- [ ] Commandes fonctionnent

---

## 🎮 **Commandes in-game utiles**

```
/op VotrePseudo          # Devenir admin
/gamemode creative       # Mode créatif
/gamemode survival       # Mode survie
/tp joueur1 joueur2      # Téléporter
/give @p diamond 64      # Donner items
/time set day            # Jour
/weather clear           # Beau temps
```

---

## 🆘 **Support**

**Si ça marche pas :**
1. Vérifier que vous êtes sur le même WiFi
2. Vérifier que le serveur tourne
3. Ping l'IP du Mac depuis le PC
4. Checker les logs Docker
5. Redémarrer le serveur

**Logs Docker :**
```bash
docker-compose logs papermc | tail -100
```

---

**🎮 Bon jeu ! 🚀**
