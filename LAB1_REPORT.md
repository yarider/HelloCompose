# Лабораторна робота №1 - Звіт

## Виконані завдання

### ✅ 1. Встановлення Android Studio та SDK
- Android Studio встановлено
- SDK налаштовано (API 26+)
- Android Emulator готовий до роботи

### ✅ 2. Створення проєкту «Hello Compose»
- Проєкт створено з Empty Compose Activity
- Package name: `com.example.hellocompose`
- Language: Kotlin
- Minimum SDK: API 26

**Файли проєкту:**
- `MainActivity.kt` - основний Activity з Compose UI
- `build.gradle.kts` - конфігурація Gradle
- Українська мова в UI: "Привіт, Студент!"

### ✅ 3. Налаштування .gitignore
Створено повний `.gitignore` для Android проєкту:
- Виключено `build/`, `.gradle/`, `.idea/`
- Виключено `local.properties`
- Виключено keystore файли
- Додано інші стандартні виключення

### ✅ 4. GitFlow
Налаштовано GitFlow структуру:
- **main** - production гілка
- **develop** - основна гілка розробки (поточна)
- Структура готова для `feature/*`, `release/*`, `hotfix/*`

### ✅ 5. Issue Templates
Створено шаблони в `.github/ISSUE_TEMPLATE/`:
- **bug_report.md** - шаблон для баг-репортів
- **feature_request.md** - шаблон для запитів нових функцій
- **PULL_REQUEST_TEMPLATE.md** - шаблон для Pull Request

### ✅ 6. GitHub Actions CI
Налаштовано `.github/workflows/android-ci.yml`:
- Автоматична збірка при push/PR до main та develop
- Запуск unit-тестів
- Lint перевірка
- JDK 17, Gradle cache

### ✅ 7. README.md
Створено повний README з:
- Описом проєкту
- Інструкціями для запуску
- Документацією GitFlow
- Технічним стеком
- CI badge
- Корисними посиланнями

## Структура проєкту

```
HelloCompose/
├── .github/
│   ├── ISSUE_TEMPLATE/
│   │   ├── bug_report.md
│   │   └── feature_request.md
│   ├── workflows/
│   │   └── android-ci.yml
│   └── PULL_REQUEST_TEMPLATE.md
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/hellocompose/
│   │       │   └── MainActivity.kt
│   │       ├── res/
│   │       └── AndroidManifest.xml
│   └── build.gradle.kts
├── .gitignore
├── README.md
├── build.gradle.kts
├── settings.gradle.kts
└── gradlew
```

## Як запустити

1. Клонувати репозиторій:
```bash
git clone https://github.com/deffyxjudg/HelloCompose.git
cd HelloCompose
```

2. Відкрити в Android Studio

3. Дочекатися синхронізації Gradle

4. Запустити на емуляторі або пристрої

## CI/CD Status

![Android CI](https://github.com/deffyxjudg/HelloCompose/actions/workflows/android-ci.yml/badge.svg)

## Тестування

Проєкт можна зібрати та протестувати через командний рядок:

```bash
# Збірка
./gradlew assembleDebug

# Тести
./gradlew test

# Lint
./gradlew lint
```

## Висновки

Всі завдання лабораторної роботи виконано:
- ✅ Android Studio встановлено та налаштовано
- ✅ Проєкт Hello Compose створено
- ✅ GitFlow налаштовано з гілками main та develop
- ✅ Issue Templates додано
- ✅ GitHub Actions CI працює
- ✅ README.md повністю документовано
- ✅ .gitignore налаштовано правильно

Проєкт готовий до розробки та співпраці!

---

**Репозиторій:** https://github.com/deffyxjudg/HelloCompose  
**Гілка:** develop  
**Дата виконання:** 3 грудня 2025
