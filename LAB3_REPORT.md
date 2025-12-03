# Лабораторна робота №3
## Тема: Мультиекранний застосунок з навігацією та збереженням стану

**Виконав:** Гуранець Артем
**Група:** К-41
**Дата:** 3 грудня 2025
**Репозиторій:** https://github.com/yarider/HelloCompose

---

## Мета роботи

Навчитися створювати застосунок з кількома екранами, організовувати переходи між ними, передавати параметри, працювати з ViewModel і StateFlow для збереження стану.

---

## Завдання

Розробити Android-застосунок із трьома екранами:

1. **HomeScreen** - головний екран із кнопками переходу на Profile та Settings
2. **ProfileScreen** - приймає параметр `userId` і відображає дані користувача
3. **SettingsScreen** - дозволяє змінювати налаштування (ім'я користувача), які зберігаються у ViewModel

### Додаткові вимоги:
- Реалізувати state hoisting для інпуту (поле вводу імені)
- Використати ViewModel + StateFlow для збереження стану
- Додати deep link для переходу на Profile (`myapp://profile/{userId}`)

---

## Хід виконання роботи

### 1. Налаштування проекту

#### 1.1. Залежності
У файлі `app/build.gradle.kts` підключено бібліотеку навігації:

```kotlin
implementation("androidx.navigation:navigation-compose:2.7.7")
```

#### 1.2. Структура проекту
```
app/src/main/java/com/example/hellocompose/
├── MainActivity.kt
├── screens/
│   ├── HomeScreen.kt
│   ├── ProfileScreen.kt
│   └── SettingsScreen.kt
├── viewmodel/
│   └── SettingsViewModel.kt
└── navigation/
    └── AppNavHost.kt
```

---

### 2. Реалізація компонентів

#### 2.1. SettingsViewModel

Створено ViewModel для збереження стану імені користувача з використанням StateFlow:

```kotlin
class SettingsViewModel : ViewModel() {
    private val _username = MutableStateFlow("Guest")
    val username: StateFlow<String> = _username

    fun updateUsername(newName: String) {
        _username.value = newName
    }
}
```

**Особливості:**
- `MutableStateFlow` - для внутрішньої зміни стану
- `StateFlow` - для зовнішнього читання (immutable для UI)
- Зберігає стан при ротації екрану завдяки ViewModel

---

#### 2.2. HomeScreen

Головний екран з навігацією до інших екранів:

```kotlin
@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🏠 Home Screen",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        Button(
            onClick = { navController.navigate("profile/42") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Go to Profile (userId=42)")
        }
        
        Button(
            onClick = { navController.navigate("settings") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Go to Settings")
        }
    }
}
```

**Функціонал:**
- Дві кнопки для навігації
- Передача параметра `userId=42` при переході на профіль
- Використання `NavController.navigate()` для переходів

---

#### 2.3. ProfileScreen

Екран профілю, який приймає та відображає `userId`:

```kotlin
@Composable
fun ProfileScreen(userId: String?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "👤 Profile Screen",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Text(
            text = "User ID: $userId",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}
```

**Особливості:**
- Приймає `userId` як nullable параметр
- Відображає отриманий ID користувача
- Підтримує deep link навігацію

---

#### 2.4. SettingsScreen з State Hoisting

Екран налаштувань з винесеним станом (state hoisting):

```kotlin
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = viewModel()
) {
    val username by viewModel.username.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "⚙ Settings Screen",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        UsernameInput(
            username = username,
            onUsernameChange = { viewModel.updateUsername(it) }
        )
        
        Text(
            text = "Current username: $username",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
fun UsernameInput(username: String, onUsernameChange: (String) -> Unit) {
    TextField(
        value = username,
        onValueChange = onUsernameChange,
        label = { Text("Enter your name") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}
```

**State Hoisting реалізовано через:**
- Винесення компонента `UsernameInput` окремо
- Передача стану `username` як параметра
- Передача callback `onUsernameChange` для зміни стану
- Використання `collectAsState()` для підписки на StateFlow

---

#### 2.5. AppNavHost - Налаштування навігації

Центральний компонент навігації з deep link:

```kotlin
@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        
        composable("settings") {
            SettingsScreen(navController)
        }
        
        composable(
            route = "profile/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType }),
            deepLinks = listOf(navDeepLink { uriPattern = "myapp://profile/{userId}" })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            ProfileScreen(userId)
        }
    }
}
```

**Особливості навігації:**
- `startDestination = "home"` - початковий екран
- Параметризований маршрут `profile/{userId}`
- Визначення типу аргументу `NavType.StringType`
- Deep link: `myapp://profile/{userId}`
- Отримання аргументів через `backStackEntry.arguments`

---

#### 2.6. MainActivity

Інтеграція навігації в головну активність:

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LabScreensTheme {
                val navController = rememberNavController()
                AppNavHost(navController)
            }
        }
    }
}
```

**Ключові моменти:**
- `rememberNavController()` - створення контролера навігації
- Контролер зберігається при рекомпозиції
- Передача контролера в `AppNavHost`

---

#### 2.7. AndroidManifest.xml - Deep Link

Налаштування intent-filter для deep link:

```xml
<activity
    android:name=".MainActivity"
    android:exported="true"
    android:label="@string/app_name"
    android:theme="@style/Theme.HelloCompose">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
    
    <!-- Deep link для профілю -->
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data android:scheme="myapp" android:host="profile" />
    </intent-filter>
</activity>
```

---

## 3. Перевірка роботи

### 3.1. Запуск застосунку

```bash
.\gradlew.bat assembleDebug
.\gradlew.bat installDebug
```

### 3.2. Тестування функціоналу

#### Тест 1: Навігація між екранами
1. ✅ Запущено застосунок → відкрився HomeScreen
2. ✅ Натиснуто "Go to Profile" → перехід на ProfileScreen з userId=42
3. ✅ Натиснуто кнопку "Назад" → повернення на HomeScreen
4. ✅ Натиснуто "Go to Settings" → перехід на SettingsScreen

#### Тест 2: Збереження стану
1. ✅ Відкрито SettingsScreen
2. ✅ Введено ім'я "Іван Петренко"
3. ✅ Виконано ротацію екрану → ім'я збереглося
4. ✅ Перехід на HomeScreen і повернення → ім'я збереглося

#### Тест 3: State Hoisting
1. ✅ При введенні тексту в TextField
2. ✅ Значення негайно відображається в Text нижче
3. ✅ Зміна стану відбувається через ViewModel
4. ✅ Компонент UsernameInput не зберігає власний стан

#### Тест 4: Deep Link
Тестування через ADB:
```bash
adb shell am start -W -a android.intent.action.VIEW -d "myapp://profile/123" com.example.hellocompose
```
✅ Застосунок відкрився на ProfileScreen з userId=123

---

## 4. Ключові концепції

### 4.1. State Hoisting (Винесення стану)

**Принцип:** Стан зберігається у батьківському компоненті, а дочірній компонент отримує:
- Значення стану як параметр
- Callback функцію для зміни стану

**Переваги:**
- Легше тестувати компоненти
- Можливість повторного використання
- Єдине джерело правди (Single Source of Truth)
- Спрощена логіка компонентів

### 4.2. ViewModel + StateFlow

**StateFlow vs LiveData:**
- StateFlow - частина Kotlin Coroutines
- Завжди має початкове значення
- Краща інтеграція з Compose через `collectAsState()`
- Type-safe

**Переваги ViewModel:**
- Зберігає дані при зміні конфігурації
- Переживає ротацію екрану
- Керує життєвим циклом
- Ізолює бізнес-логіку від UI

### 4.3. Navigation Compose

**Основні компоненти:**
- `NavController` - керує навігацією
- `NavHost` - контейнер для екранів
- `composable()` - визначення маршруту
- `navArgument()` - типізація параметрів

**Передача параметрів:**
- Через шлях: `profile/{userId}`
- Отримання: `backStackEntry.arguments?.getString("userId")`

### 4.4. Deep Links

**Структура URI:** `scheme://host/path`
- Приклад: `myapp://profile/42`
- `myapp` - схема
- `profile` - хост
- `42` - параметр

**Використання:**
- Відкриття застосунку з браузера
- Push-повідомлення
- Інтеграція з іншими додатками

---

## 5. Результати роботи

### Реалізовані вимоги:

✅ **Три екрани:**
- HomeScreen з навігацією
- ProfileScreen з параметром userId
- SettingsScreen з можливістю редагування

✅ **Навігація:**
- Переходи між екранами
- Передача параметрів
- Повернення назад

✅ **State Management:**
- ViewModel для збереження стану
- StateFlow для реактивності
- Збереження при ротації

✅ **State Hoisting:**
- Винесений компонент UsernameInput
- Передача стану та callbacks
- Separation of Concerns

✅ **Deep Link:**
- Схема myapp://profile/{userId}
- Intent-filter в AndroidManifest
- Коректна обробка параметрів

---

## 6. Висновки

В ході виконання лабораторної роботи було:

1. **Освоєно Navigation Compose** - створення багатоекранного застосунку з навігацією між екранами та передачею параметрів

2. **Реалізовано State Management** - використання ViewModel + StateFlow для збереження стану, що переживає зміни конфігурації

3. **Застосовано State Hoisting** - винесення стану з компонентів для покращення їх повторного використання та тестування

4. **Налаштовано Deep Links** - можливість відкриття конкретного екрану застосунку через URI

5. **Вивчено best practices** - правильна архітектура додатку з розділенням відповідальності між UI, ViewModel та навігацією

Всі вимоги лабораторної роботи виконано повністю. Застосунок коректно працює, зберігає стан при ротації екрану та підтримує deep link навігацію.

---

## 7. Скріншоти

### HomeScreen
![HomeScreen](screenshots/home_screen.png)
*Головний екран з кнопками навігації*

### ProfileScreen
![ProfileScreen](screenshots/profile_screen.png)
*Екран профілю з відображенням userId*

### SettingsScreen
![SettingsScreen](screenshots/settings_screen.png)
*Екран налаштувань з полем вводу імені*

---

## 8. Використані ресурси

- [Jetpack Compose Navigation](https://developer.android.com/jetpack/compose/navigation)
- [StateFlow and SharedFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow)
- [ViewModel Overview](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [State hoisting in Compose](https://developer.android.com/jetpack/compose/state-hoisting)
- [Deep Links in Android](https://developer.android.com/training/app-links/deep-linking)
