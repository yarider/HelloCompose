# GitFlow Workflow - Інструкція

## Огляд GitFlow

GitFlow - це модель гілкування для Git, яка визначає структуру гілок і правила їх використання.

## Структура гілок

### Основні гілки (постійні)
- **main** - production код, завжди стабільний
- **develop** - основна гілка розробки, інтеграція feature

### Тимчасові гілки
- **feature/** - розробка нових функцій
- **release/** - підготовка до релізу
- **hotfix/** - термінові виправлення в production

## Робота з Feature гілками

### Створення нової feature

```bash
# Переключитись на develop
git checkout develop

# Отримати останні зміни
git pull origin develop

# Створити нову feature гілку
git checkout -b feature/login-screen
```

### Робота над feature

```bash
# Робіть зміни...
git add .
git commit -m "Add login screen UI"

# Періодично синхронізуйтесь з develop
git checkout develop
git pull origin develop
git checkout feature/login-screen
git merge develop
```

### Завершення feature

```bash
# Переключитись на develop
git checkout develop

# Змерджити feature (без fast-forward для збереження історії)
git merge --no-ff feature/login-screen

# Видалити локальну гілку
git branch -d feature/login-screen

# Push змін
git push origin develop
```

### Використання PR (рекомендовано)

```bash
# Push feature гілки на GitHub
git push origin feature/login-screen

# Створити PR через GitHub UI
# develop ← feature/login-screen

# Після review та merge видалити гілку
git checkout develop
git pull origin develop
git branch -d feature/login-screen
```

## Робота з Release гілками

### Створення release

```bash
# Від develop створити release гілку
git checkout develop
git pull origin develop
git checkout -b release/1.0.0

# Оновити версію в build.gradle.kts
# versionCode = 1
# versionName = "1.0.0"

git add .
git commit -m "Bump version to 1.0.0"
```

### Завершення release

```bash
# Змерджити в main
git checkout main
git merge --no-ff release/1.0.0
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin main --tags

# Змерджити в develop (для синхронізації)
git checkout develop
git merge --no-ff release/1.0.0
git push origin develop

# Видалити release гілку
git branch -d release/1.0.0
```

## Робота з Hotfix гілками

### Створення hotfix

```bash
# Від main створити hotfix гілку
git checkout main
git pull origin main
git checkout -b hotfix/fix-crash-on-startup

# Виправити баг
git add .
git commit -m "Fix crash on startup"
```

### Завершення hotfix

```bash
# Змерджити в main
git checkout main
git merge --no-ff hotfix/fix-crash-on-startup
git tag -a v1.0.1 -m "Hotfix 1.0.1: Fix crash"
git push origin main --tags

# Змерджити в develop
git checkout develop
git merge --no-ff hotfix/fix-crash-on-startup
git push origin develop

# Видалити hotfix гілку
git branch -d hotfix/fix-crash-on-startup
```

## Альтернатива: git-flow утиліта

### Встановлення

**Windows (через Git Bash):**
```bash
# Скачати з https://github.com/nvie/gitflow/wiki/Windows
# Або через Chocolatey
choco install gitflow-avh
```

**macOS:**
```bash
brew install git-flow-avh
```

**Linux:**
```bash
sudo apt install git-flow
```

### Ініціалізація

```bash
git flow init -d
```

### Feature workflow

```bash
# Створити feature
git flow feature start login-screen

# Завершити feature
git flow feature finish login-screen
```

### Release workflow

```bash
# Створити release
git flow release start 1.0.0

# Завершити release
git flow release finish 1.0.0
```

### Hotfix workflow

```bash
# Створити hotfix
git flow hotfix start fix-crash

# Завершити hotfix
git flow hotfix finish fix-crash
```

## Правила роботи

### ✅ DO (Робіть)
- Завжди створюйте feature гілки від develop
- Використовуйте описові назви гілок: `feature/add-user-profile`
- Робіть регулярні коміти з чіткими повідомленнями
- Використовуйте Pull Request для code review
- Тестуйте перед merge
- Видаляйте гілки після завершення роботи

### ❌ DON'T (Не робіть)
- Не комітьте прямо в main або develop
- Не використовуйте fast-forward merge для feature/release/hotfix
- Не залишайте незавершені feature гілки надовго
- Не ігноруйте конфлікти при merge

## Назви гілок

### Конвенції
- `feature/feature-name` - нові функції
- `bugfix/bug-description` - виправлення багів у develop
- `hotfix/critical-fix` - критичні виправлення в main
- `release/version` - підготовка релізу

### Приклади
- `feature/user-authentication`
- `feature/dark-theme`
- `bugfix/login-validation`
- `hotfix/crash-on-startup`
- `release/2.1.0`

## Корисні команди

```bash
# Подивитись всі гілки
git branch -a

# Подивитись поточну гілку
git branch

# Видалити локальну гілку
git branch -d branch-name

# Видалити remote гілку
git push origin --delete branch-name

# Подивитись історію комітів
git log --oneline --graph --all

# Скасувати незбережені зміни
git checkout -- .

# Подивитись статус
git status
```

## Візуалізація GitFlow

```
main       ─────o─────────o─────────o───→ (production)
                 │         │         │
                tag       tag       tag
                v1.0      v1.1      v2.0
                 │         │         │
develop    ─o───┴─o─o─o───┴─o─o─o───┴─o─→ (integration)
             │       │         │
feature/*    └─o─o─o┘         └─o─o─o─┘  (temporary)
```

## Додаткові ресурси

- [A successful Git branching model](https://nvie.com/posts/a-successful-git-branching-model/)
- [git-flow cheatsheet](https://danielkummer.github.io/git-flow-cheatsheet/)
- [Atlassian Git Flow Tutorial](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow)

---

**Проєкт:** HelloCompose  
**Репозиторій:** https://github.com/deffyxjudg/HelloCompose
