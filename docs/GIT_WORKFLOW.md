# 🌿 Git Workflow - ZineCraft

> Guide de collaboration Git pour Otmane & Adam

---

## 🎯 Structure des branches

```
main                         (🔒 Production - NE PAS TOUCHER)
  │
  └── dev                    (🔄 Intégration - Merge ici)
       │
       ├── feature/otmane-database      (Otmane)
       ├── feature/otmane-docker        (Otmane)
       ├── feature/otmane-api           (Otmane)
       │
       ├── feature/adam-skills          (Adam)
       ├── feature/adam-pets            (Adam)
       └── feature/adam-quests          (Adam)
```

---

## 📋 Règles d'or

1. 🚫 **JAMAIS** de commit direct sur `main`
2. 🚫 **JAMAIS** de commit direct sur `dev` (sauf exception)
3. ✅ **TOUJOURS** travailler sur une branche `feature/`
4. ✅ **TOUJOURS** tester avant de merge
5. ✅ **TOUJOURS** des messages de commit clairs

---

## 🔄 Workflow complet

### **1️⃣ Démarrer une nouvelle feature**

```bash
# Partir de dev à jour
git checkout dev
git pull origin dev

# Créer ta branche
git checkout -b feature/ton-nom-feature-description

# Exemple Otmane
git checkout -b feature/otmane-database

# Exemple Adam
git checkout -b feature/adam-skills
```

---

### **2️⃣ Travailler sur ta branche**

```bash
# Vérifier sur quelle branche tu es
git branch
# Tu dois voir un * devant ta branche

# Coder, coder, coder...

# Voir ce que tu as modifié
git status
git diff

# Ajouter tes modifications
git add .
# OU
git add fichier1.java fichier2.java

# Commit avec un bon message
git commit -m "feat(database): Add MySQL connection pool"

# Pousser sur GitHub
git push origin feature/otmane-database
```

---

### **3️⃣ Messages de commit**

**Format :** `type(scope): description`

**Types :**
- `feat` : Nouvelle fonctionnalité
- `fix` : Correction de bug
- `docs` : Documentation
- `style` : Formatage, style (pas de logique)
- `refactor` : Refactoring de code
- `test` : Ajout de tests
- `chore` : Maintenance

**Exemples :**
```bash
# ✅ BON
git commit -m "feat(skills): Add mining skill with XP system"
git commit -m "fix(pets): Fix cat pet spawn location"
git commit -m "docs(readme): Update installation guide"
git commit -m "refactor(database): Simplify query builder"

# ❌ MAUVAIS
git commit -m "update"
git commit -m "fix bug"
git commit -m "test"
git commit -m "modif"
```

---

### **4️⃣ Récupérer les modifs des autres**

```bash
# Avant de commencer à coder, récupère les dernières modifs
git checkout dev
git pull origin dev

# Merge dev dans ta branche
git checkout feature/ton-nom-feature
git merge dev

# Si conflit, demande de l'aide !
```

---

### **5️⃣ Merge dans dev (Papa fait ça)**

```bash
# Otmane vérifie que tout est OK
git checkout dev
git pull origin dev

# Teste la feature d'Adam d'abord
git checkout feature/adam-skills
cd plugins/ZineCraftCore
gradle build
# Si ça compile et marche → OK !

# Merge
git checkout dev
git merge feature/adam-skills

# Push
git push origin dev

# Dire à Adam : "C'est mergé ! 🎉"
```

---

### **6️⃣ Release vers main (Fin de semaine)**

```bash
# Otmane SEULEMENT
# Quand dev est stable et testé

git checkout main
git pull origin main
git merge dev
git tag -a v1.0.0 -m "Release Week 1"
git push origin main --tags
```

---

## 🆘 Situations d'urgence

### **❌ J'ai fait un commit sur la mauvaise branche**

```bash
# Annuler le dernier commit (garde les modifs)
git reset --soft HEAD~1

# Changer de branche
git checkout la-bonne-branche

# Re-commit
git add .
git commit -m "ton message"
```

---

### **❌ J'ai des conflits !**

```bash
# Voir les fichiers en conflit
git status

# Ouvrir le fichier, tu verras :
<<<<<<< HEAD
ton code
=======
le code de l'autre
>>>>>>> feature/autre-branche

# Résoudre manuellement
# Garder ce que tu veux, supprimer les <<<< ==== >>>>

# Puis
git add fichier-resolu.java
git commit -m "fix: Resolve merge conflict"
```

**Si tu comprends rien → APPELLE PAPA ! 🆘**

---

### **❌ Je veux annuler mes modifications**

```bash
# Annuler UN fichier
git checkout -- fichier.java

# Annuler TOUT (⚠️ perte définitive)
git checkout .

# Annuler le dernier commit (garde les modifs)
git reset --soft HEAD~1

# Annuler le dernier commit (PERD les modifs ⚠️)
git reset --hard HEAD~1
```

---

### **❌ J'ai poussé n'importe quoi sur GitHub**

```bash
# Annuler le dernier push (⚠️ DANGEREUX)
git reset --hard HEAD~1
git push origin feature/ta-branche --force

# ⚠️ À faire SEULEMENT si personne d'autre n'a pull !
```

---

## 📊 Commandes utiles

### **Voir l'historique**

```bash
# Historique complet
git log

# Historique compact
git log --oneline

# Historique graphique
git log --oneline --graph --all

# Les 10 derniers commits
git log --oneline -10

# Commits d'Adam
git log --author="Adam" --oneline
```

---

### **Voir les différences**

```bash
# Différences non commitées
git diff

# Différences d'un fichier
git diff fichier.java

# Différences entre branches
git diff dev feature/adam-skills

# Différences entre commits
git diff abc123 def456
```

---

### **Branches**

```bash
# Lister les branches locales
git branch

# Lister toutes les branches (local + remote)
git branch -a

# Créer une branche
git branch feature/nouvelle-feature

# Créer et basculer
git checkout -b feature/nouvelle-feature

# Supprimer une branche (locale)
git branch -d feature/ancienne-feature

# Supprimer une branche (remote)
git push origin --delete feature/ancienne-feature
```

---

## 📅 Workflow hebdomadaire type

### **Lundi matin**
```bash
# Otmane & Adam
git checkout dev
git pull origin dev

# Créer/reprendre les branches de la semaine
git checkout feature/adam-skills
git merge dev
```

### **Tous les jours**
```bash
# Avant de coder
git pull origin dev
git checkout ta-branche
git merge dev

# Après avoir codé
git add .
git commit -m "feat: description"
git push origin ta-branche
```

### **Mercredi (Review)**
```bash
# Otmane regarde le code d'Adam
git diff dev feature/adam-skills

# Si OK → merge
git checkout dev
git merge feature/adam-skills
git push origin dev
```

### **Vendredi (Release semaine)**
```bash
# Otmane merge tout dans main
git checkout main
git merge dev
git tag -a v1.X.0 -m "Week X release"
git push origin main --tags
```

---

## 🎯 Checklist avant chaque merge

- [ ] ✅ Le code compile (`gradle build`)
- [ ] ✅ Pas d'erreur dans la console
- [ ] ✅ Testé in-game si possible
- [ ] ✅ Pas de `System.out.println()`
- [ ] ✅ Commentaires ajoutés
- [ ] ✅ Message de commit clair
- [ ] ✅ Pas de fichiers temporaires (.class, .log, etc.)

---

## 💡 Tips

1. **Commit souvent** - Petits commits > Gros commits
2. **Pull avant de push** - Évite les conflits
3. **Messages clairs** - Ton futur toi te remerciera
4. **Une feature = Une branche** - Facile à review
5. **Teste avant de push** - Évite de casser dev

---

**🌿 Happy Git-ing! 🚀**
