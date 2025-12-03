# Hello Compose

Навчальний приклад використання Jetpack Compose для створення простого Android застосунку.

![Android CI](https://github.com/deffyxjudg/HelloCompose/actions/workflows/android-ci.yml/badge.svg)

##  Опис проєкту

Це навчальний проєкт, створений для демонстрації:
- Jetpack Compose UI
- GitFlow workflow
- GitHub Actions CI/CD
- Issue Templates
- Kotlin для Android

##  Як запустити

### Вимоги
- Android Studio (остання стабільна версія)
- Android SDK (API 26+)
- JDK 17
- Git

### Кроки для запуску

1. **Клонуйте репозиторій:**
   ```bash
   git clone https://github.com/deffyxjudg/HelloCompose.git
   cd HelloCompose
   ```

2. **Відкрийте проєкт в Android Studio:**
   - File  Open  Оберіть папку проєкту
   - Зачекайте поки Gradle завершить синхронізацію

3. **Запустіть застосунок:**
   - Створіть AVD (Android Virtual Device) через AVD Manager
   - Або підключіть фізичний пристрій з увімкненим USB debugging
   - Натисніть  (Run) або `Shift+F10`

### Альтернативний запуск через командний рядок

```bash
# Зібрати debug APK
./gradlew assembleDebug

# Запустити тести
./gradlew test

# Запустити lint
./gradlew lint
```

##  GitFlow

Проєкт використовує GitFlow workflow:

- `main` - production гілка
- `develop` - основна гілка для розробки
- `feature/*` - гілки для нових функцій
- `release/*` - гілки для релізів
- `hotfix/*` - гілки для термінових виправлень

### Приклад роботи з feature

```bash
# Створити нову feature гілку
git checkout -b feature/my-new-feature develop

# Після завершення роботи
git checkout develop
git merge --no-ff feature/my-new-feature
git branch -d feature/my-new-feature
git push origin develop
```

##  Технічний стек

- **Мова:** Kotlin
- **UI Framework:** Jetpack Compose
- **Build System:** Gradle (Kotlin DSL)
- **Min SDK:** 26 (Android 8.0)
- **Target SDK:** 36

### Основні залежності

- `androidx.compose.ui` - Compose UI
- `androidx.compose.material3` - Material Design 3
- `androidx.activity-compose` - Activity integration
- `androidx.core-ktx` - Kotlin extensions

##  Issue Templates

Проєкт містить шаблони для:
-  Bug Report - повідомлення про помилки
-  Feature Request - запити на нові функції
-  Pull Request Template - шаблон для PR

##  CI/CD

GitHub Actions автоматично виконує:
- Build debug APK
- Unit tests
- Lint перевірку

Workflow запускається при push та PR до гілок `main` та `develop`.

##  Функціональність

Застосунок демонструє базову Compose функціональність:
- Відображення тексту з українською мовою
- Інтерактивна кнопка
- Зміна стану UI при натисканні
- Material Design 3 компоненти

##  Автор

Лабораторна робота №1 - Android Development

##  Ліцензія

MIT License

##  Участь у розробці

1. Fork проєкт
2. Створіть feature гілку (`git checkout -b feature/AmazingFeature`)
3. Commit зміни (`git commit -m 'Add some AmazingFeature'`)
4. Push в гілку (`git push origin feature/AmazingFeature`)
5. Відкрийте Pull Request

##  Корисні посилання

- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Android Studio](https://developer.android.com/studio)
- [GitFlow Workflow](https://nvie.com/posts/a-successful-git-branching-model/)
- [GitHub Actions](https://docs.github.com/en/actions)
