plugins/ZineCraftCore/src/main/java/fr/zinecraft/core/
├── rpg/
│   ├── PlayerManager.java      (gestion joueurs RPG)
│   ├── RPGPlayer.java           (data classe joueur)
│   ├── ClassManager.java        (8 classes)
│   ├── ClassType.java           (enum classes)
│   ├── LevelManager.java        (XP + level up)
│   ├── SkillManager.java        (compétences)
│   └── Skill.java               (data compétence)
├── economy/
│   ├── EconomyManager.java      (gold)
│   └── ShopManager.java         (shops NPC)
├── quests/
│   ├── QuestManager.java        (quêtes)
│   ├── Quest.java               (data quête)
│   └── QuestObjective.java      (objectifs)
├── listeners/
│   └── RPGListener.java         (events XP, combat, etc.)
└── commands/
    ├── ClassCommand.java        (/class)
    ├── LevelCommand.java        (/level, /stats)
    ├── QuestCommand.java        (/quest)
    └── GoldCommand.java         (/gold, /pay)