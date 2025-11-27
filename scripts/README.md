# 🎮 Scripts de Déploiement ZineCraft

## 📦 Après avoir créé une nouvelle arme

### Option 1 : Script complet (avec vérification)

```bash
./scripts/update-plugin.sh
```

- ✅ Compile le plugin
- ✅ Copie vers le serveur
- ✅ Redémarre le serveur
- ✅ Attend 30 secondes
- ✅ Vérifie que le serveur est prêt

### Option 2 : Script rapide (recommandé)

```bash
./scripts/quick-update.sh
```

- ⚡ Compile le plugin
- ⚡ Copie vers le serveur
- ⚡ Redémarre le serveur
- ⏳ Attendez ~30 secondes avant de vous reconnecter

---

## 🔧 Workflow complet pour Adam

### 1. Créer une nouvelle arme

Modifier les fichiers :

- `WeaponType.java` - Ajouter l'enum de l'arme
- `WeaponManager.java` - Ajouter la méthode de pouvoir
- `WeaponCommand.java` - Ajouter les alias de commande

### 2. Tester localement (optionnel)

```bash
cd plugins/ZineCraftCore
gradle build
```

### 3. Commit & Push

```bash
git add .
git commit -m "Ajout arme NomDeLArme - Description courte"
git push origin main
```

### 4. Déployer sur le serveur

```bash
./scripts/quick-update.sh
```

### 5. Tester in-game

- Attendez 30 secondes
- Reconnectez-vous au serveur
- Testez avec `/weapon give nomarme`

---

## 🚨 En cas d'erreur de compilation

Si le script affiche "❌ Erreur compilation !" :

1. **Vérifier les erreurs** :

```bash
cd plugins/ZineCraftCore
gradle build
```

2. **Corriger les erreurs** dans le code

3. **Réessayer** :

```bash
./scripts/quick-update.sh
```

---

## 📝 Commandes manuelles (si besoin)

### Compiler seulement

```bash
cd plugins/ZineCraftCore
gradle build
```

### Copier le plugin

```bash
cp plugins/ZineCraftCore/build/libs/ZineCraftCore-1.0.0-SNAPSHOT.jar server/plugins/
```

### Redémarrer le serveur

```bash
docker restart zinecraft-papermc
```

### Voir les logs

```bash
docker logs zinecraft-papermc --tail 50
```

---

## 💡 Astuces

- **Ne pas oublier** de `git pull` avant de commencer à coder
- **Toujours tester** la compilation avant de push
- **Redémarrer le serveur** après chaque modification
- **Attendre 30 secondes** après le redémarrage avant de se reconnecter
