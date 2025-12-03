# Звіт з виконання лабораторної роботи №2

**Студент:** [Ваше ім'я]  
**Група:** [Ваша група]  
**Дата:** 3 грудня 2025

## Тема
Побудова екранів: списки, деталі, AppBar, темізація (Material 3)

## Мета роботи
Навчитися створювати екрани у Jetpack Compose з використанням списків (LazyColumn), екрану деталей, верхньої панелі (AppBar) та застосуванням темізації (Material 3).

## Виконані завдання

### 1. Базова реалізація

#### 1.1. Налаштування проєкту
- ✅ Додано залежність `androidx.navigation:navigation-compose:2.7.7` для навігації
- ✅ Налаштовано Material 3 теми (світла та темна)

#### 1.2. Темізація (Material 3)
Створено файл `Theme.kt` з двома схемами кольорів:

**Світла тема:**
- Primary: `#1976D2` (синій)
- Secondary: `#03DAC6` (бірюзовий)
- Background: `#F6F6F6` (світло-сірий)
- Surface: `White`

**Темна тема:**
- Primary: `#90CAF9` (світло-синій)
- Secondary: `#80CBC4` (світло-бірюзовий)
- Background: `#121212` (темний)
- Surface: `#1E1E1E` (темно-сірий)

#### 1.3. Модель даних
Створено `data class Item` з полями:
- `id: Int` - унікальний ідентифікатор
- `title: String` - заголовок елемента
- `description: String` - детальний опис

Згенеровано 20 тестових елементів у `sampleItems`.

#### 1.4. Екран списку (ItemListScreen)
- Використано `LazyColumn` для оптимізованого відображення списку
- Додано `ListItem` з підтримкою:
  - `headlineContent` - заголовок
  - `supportingContent` - опис
  - `leadingContent` - іконка
- Реалізовано `clickable` для переходу до деталей
- Додано `HorizontalDivider` між елементами

#### 1.5. TopAppBar
- Створено `CenterAlignedTopAppBar` з назвою "LabScreensApp"
- Додано іконку меню (`Icons.Default.Menu`)
- Додано іконку пошуку (`Icons.Default.Search`)
- Реалізовано перемикання між звичайним та пошуковим режимом

#### 1.6. Екран деталей (ItemDetailsScreen)
- Використано `TopAppBar` з:
  - Кнопкою "Назад" (`Icons.Default.ArrowBack`)
  - Кнопкою "Поділитися" (`Icons.Default.Share`)
- Відображення інформації:
  - Заголовок (великий шрифт)
  - Опис (звичайний текст)
  - Додаткова інформація в `Card` (ID, категорія)
- Кнопка "Поділитися" внизу екрану

#### 1.7. Навігація (AppNavigation)
- Використано `NavHost` з `rememberNavController()`
- Маршрути:
  - `"list"` - екран списку (стартовий)
  - `"details/{itemId}"` - екран деталей з параметром
- Реалізовано передачу параметрів через URL

### 2. Додаткові завдання

#### 2.1. ✅ Іконки для елементів списку
Додано 5 різних іконок залежно від `id % 5`:
- `Icons.Default.Star` - зірка (важливе)
- `Icons.Default.Favorite` - серце (улюблене)
- `Icons.Default.Build` - інструмент
- `Icons.Default.Home` - будинок
- `Icons.Default.Info` - інформація

#### 2.2. ✅ Темна тема
Реалізовано `darkColorScheme` з відповідними кольорами:
- Світлі відтінки для primary/secondary
- Темний фон (#121212)
- Автоматичне перемикання через `isSystemInDarkTheme()`

#### 2.3. ✅ Кнопка меню в AppBar
Додано `navigationIcon` з іконкою `Icons.Default.Menu` в `CenterAlignedTopAppBar`.

#### 2.4. ✅ Кнопка "Поділитися"
Реалізовано на екрані деталей:
- В `actions` TopAppBar
- Як окрема кнопка внизу екрану
- Використовує `Intent.ACTION_SEND` для шерінгу тексту

#### 2.5. ✅ Пошук по списку
Реалізовано повнофункціональний пошук:
- Кнопка пошуку в TopAppBar
- TextField для введення запиту
- Фільтрація по `title` та `description` (case-insensitive)
- Можливість закрити пошук
- Реактивне оновлення списку

## Структура проєкту

```
app/src/main/java/com/example/hellocompose/
├── MainActivity.kt                    # Головна активність
├── model/
│   └── Item.kt                       # Модель даних
├── screens/
│   ├── ItemListScreen.kt            # Екран списку
│   └── ItemDetailsScreen.kt         # Екран деталей
├── navigation/
│   └── AppNavigation.kt             # Навігація
└── ui/theme/
    ├── Color.kt                      # Кольори
    ├── Theme.kt                      # Теми Material 3
    └── Type.kt                       # Типографія
```

## Відповіді на контрольні питання

### 1. У чому різниця між LazyColumn та Column?

**Column:**
- Завантажує всі елементи одразу в пам'ять
- Підходить для невеликої кількості елементів
- Не має вбудованої прокрутки
- Всі елементи рендеряться незалежно від видимості

**LazyColumn:**
- Використовує "ледиве" завантаження (lazy loading)
- Рендерить тільки видимі елементи на екрані
- Автоматична прокрутка
- Оптимізовано для великих списків
- Перевикористовує composables для кращої продуктивності

### 2. Як організувати перехід від одного екрану до іншого в Compose?

Використовується **Navigation Compose** бібліотека:

1. Додати залежність: `androidx.navigation:navigation-compose`
2. Створити `NavController` через `rememberNavController()`
3. Визначити `NavHost` з маршрутами
4. Використовувати `composable("route") { }` для кожного екрану
5. Навігація через `navController.navigate("route")`
6. Повернення назад через `navController.popBackStack()`
7. Передача параметрів через URL: `"details/{id}"`

### 3. Які основні компоненти теми Material 3?

**MaterialTheme** складається з:

1. **ColorScheme** - палітра кольорів:
   - primary, secondary, tertiary
   - background, surface
   - error, outline
   - on-варіанти (onPrimary, onBackground, тощо)

2. **Typography** - типографічні стилі:
   - displayLarge/Medium/Small
   - headlineLarge/Medium/Small
   - bodyLarge/Medium/Small
   - labelLarge/Medium/Small

3. **Shapes** - форми компонентів:
   - small, medium, large
   - rounded corners (RoundedCornerShape)

### 4. Як реалізується AppBar у Jetpack Compose?

У Material 3 є кілька варіантів AppBar:

1. **TopAppBar** - базовий варіант
2. **CenterAlignedTopAppBar** - з центрованим заголовком
3. **SmallTopAppBar** - компактний варіант
4. **MediumTopAppBar** - середній розмір
5. **LargeTopAppBar** - великий з collapsing

**Основні параметри:**
- `title` - заголовок
- `navigationIcon` - ліва іконка (зазвичай меню/назад)
- `actions` - праві іконки (дії)
- `colors` - кольорова схема
- `scrollBehavior` - поведінка при прокрутці

**Приклад:**
```kotlin
@OptIn(ExperimentalMaterial3Api::class)
CenterAlignedTopAppBar(
    title = { Text("Заголовок") },
    navigationIcon = {
        IconButton(onClick = { }) {
            Icon(Icons.Default.Menu, "Меню")
        }
    },
    actions = {
        IconButton(onClick = { }) {
            Icon(Icons.Default.Search, "Пошук")
        }
    }
)
```

## Висновки

В ході виконання лабораторної роботи було:

1. ✅ Створено повнофункціональний додаток з навігацією між екранами
2. ✅ Реалізовано список з 20 елементами та іконками
3. ✅ Створено детальний екран з можливістю шерінгу
4. ✅ Налаштовано світлу та темну теми Material 3
5. ✅ Додано TopAppBar з меню та пошуком
6. ✅ Реалізовано функціонал пошуку по списку

Всі основні та додаткові завдання виконано успішно. Додаток працює коректно, має зручний інтерфейс та відповідає принципам Material Design 3.

## Скріншоти

[Тут можна додати скріншоти роботи додатку після запуску]

- Екран списку (світла тема)
- Екран списку (темна тема)
- Екран пошуку
- Екран деталей
- Функція поділитися

## Використані технології

- Kotlin
- Jetpack Compose
- Material 3
- Navigation Compose
- LazyColumn
- State Management (remember, mutableStateOf)
- Icons (Material Icons)
