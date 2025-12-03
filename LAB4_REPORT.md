# Лабораторна робота №4
## Тема: Архітектура застосунку: MVVM, Clean Architecture, Hilt, StateFlow

**Виконав:** Гуранець Артем
**Група:** К-41
**Дата:** 3 грудня 2025
**Репозиторій:** https://github.com/yarider/HelloCompose

---

## Мета роботи

Створити застосунок з використанням сучасної архітектури Android, а саме:
- Впровадити патерн **MVVM** (Model-View-ViewModel)
- Застосувати принципи **Clean Architecture**
- Використати **Hilt** для Dependency Injection
- Реалізувати реактивний стан через **StateFlow**

---

## Завдання

Створити застосунок **UserApp**, який:
- Відображає список користувачів
- При натисканні на користувача відкриває екран деталей
- Дані беруться з фейкового джерела (FakeRepository)
- Структура проєкту відповідає Clean Architecture
- Залежності впроваджуються через Hilt

---

## Хід виконання роботи

### 1. Налаштування проєкту

#### 1.1. Додавання залежностей

**У файлі `gradle/libs.versions.toml`** додано версії та бібліотеки:

```toml
[versions]
hilt = "2.51"
hiltNavigationCompose = "1.2.0"
lifecycleViewmodelCompose = "2.8.0"

[libraries]
androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycleViewmodelCompose" }
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-compiler = { group = "com.google.dagger", name = "hilt-compiler", version.ref = "hilt" }
hilt-navigation-compose = { group = "androidx.hilt", name = "hilt-navigation-compose", version.ref = "hiltNavigationCompose" }

[plugins]
hilt-android = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
kotlin-kapt = { id = "org.jetbrains.kotlin.kapt", version.ref = "kotlin" }
```

**У файлі `app/build.gradle.kts`** підключено плагіни та залежності:

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.kapt)
}

dependencies {
    // ViewModel Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    
    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
}
```

**У файлі `build.gradle.kts` (project level):**

```kotlin
plugins {
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
}
```

---

### 2. Структура проєкту (Clean Architecture)

```
com.example.hellocompose/
├── data/                           # Data Layer
│   └── repository/
│       └── UserRepositoryImpl.kt   # Реалізація репозиторію
│
├── domain/                         # Domain Layer (Business Logic)
│   ├── model/
│   │   └── User.kt                 # Domain модель
│   ├── repository/
│   │   └── UserRepository.kt       # Інтерфейс репозиторію
│   └── usecase/
│       ├── GetUsersUseCase.kt      # Use case для отримання списку
│       └── GetUserByIdUseCase.kt   # Use case для отримання по ID
│
├── presentation/                   # Presentation Layer (UI)
│   ├── ui/
│   │   ├── UserListScreen.kt       # Екран списку
│   │   └── UserDetailScreen.kt     # Екран деталей
│   ├── viewmodel/
│   │   └── UserViewModel.kt        # ViewModel
│   └── navigation/
│       └── NavGraph.kt             # Навігація
│
├── di/                             # Dependency Injection
│   └── AppModule.kt                # Hilt модуль
│
├── UserApp.kt                      # Application клас
└── MainActivity.kt                 # Головна Activity
```

---

### 3. Domain Layer (Бізнес-логіка)

#### 3.1. Domain модель

**`domain/model/User.kt`**

```kotlin
package com.example.hellocompose.domain.model

data class User(
    val id: Int,
    val name: String,
    val email: String
)
```

**Особливості:**
- Незалежна від фреймворку модель
- Чиста Kotlin data class
- Не залежить від Android або зовнішніх бібліотек

---

#### 3.2. Repository Interface

**`domain/repository/UserRepository.kt`**

```kotlin
package com.example.hellocompose.domain.repository

import com.example.hellocompose.domain.model.User

interface UserRepository {
    suspend fun getUsers(): List<User>
    suspend fun getUserById(id: Int): User?
}
```

**Принципи:**
- Інтерфейс в Domain layer
- Реалізація в Data layer (Dependency Inversion Principle)
- Використання suspend функцій для асинхронності

---

#### 3.3. Use Cases

**`domain/usecase/GetUsersUseCase.kt`**

```kotlin
package com.example.hellocompose.domain.usecase

import com.example.hellocompose.domain.model.User
import com.example.hellocompose.domain.repository.UserRepository

class GetUsersUseCase(private val repo: UserRepository) {
    suspend operator fun invoke(): List<User> = repo.getUsers()
}
```

**`domain/usecase/GetUserByIdUseCase.kt`**

```kotlin
package com.example.hellocompose.domain.usecase

import com.example.hellocompose.domain.model.User
import com.example.hellocompose.domain.repository.UserRepository

class GetUserByIdUseCase(private val repo: UserRepository) {
    suspend operator fun invoke(id: Int): User? = repo.getUserById(id)
}
```

**Переваги Use Cases:**
- Інкапсуляція бізнес-логіки
- Повторне використання коду
- Легке тестування
- Оператор `invoke()` дозволяє викликати як функцію

---

### 4. Data Layer

#### 4.1. Repository Implementation

**`data/repository/UserRepositoryImpl.kt`**

```kotlin
package com.example.hellocompose.data.repository

import com.example.hellocompose.domain.model.User
import com.example.hellocompose.domain.repository.UserRepository
import kotlinx.coroutines.delay

class UserRepositoryImpl : UserRepository {
    private val users = listOf(
        User(1, "Олена", "olena@example.com"),
        User(2, "Ігор", "ihor@example.com"),
        User(3, "Марія", "maria@example.com"),
        User(4, "Андрій", "andriy@example.com"),
        User(5, "Наталія", "natalia@example.com")
    )

    override suspend fun getUsers(): List<User> {
        delay(1000) // імітація завантаження з мережі
        return users
    }

    override suspend fun getUserById(id: Int): User? {
        delay(500) // імітація завантаження
        return users.find { it.id == id }
    }
}
```

**Особливості:**
- Реалізує інтерфейс з Domain layer
- Імітація мережевих запитів через `delay()`
- Фейкові дані для демонстрації
- В реальному додатку тут було б API, база даних тощо

---

### 5. Dependency Injection (Hilt)

#### 5.1. Application клас

**`UserApp.kt`**

```kotlin
package com.example.hellocompose

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class UserApp : Application()
```

**Налаштування в `AndroidManifest.xml`:**

```xml
<application
    android:name=".UserApp"
    ...>
```

---

#### 5.2. Hilt Module

**`di/AppModule.kt`**

```kotlin
package com.example.hellocompose.di

import com.example.hellocompose.data.repository.UserRepositoryImpl
import com.example.hellocompose.domain.repository.UserRepository
import com.example.hellocompose.domain.usecase.GetUserByIdUseCase
import com.example.hellocompose.domain.usecase.GetUsersUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository = UserRepositoryImpl()

    @Provides
    fun provideGetUsersUseCase(repo: UserRepository) = GetUsersUseCase(repo)

    @Provides
    fun provideGetUserByIdUseCase(repo: UserRepository) = GetUserByIdUseCase(repo)
}
```

**Пояснення анотацій:**

- `@Module` - позначає клас як модуль Hilt
- `@InstallIn(SingletonComponent::class)` - визначає час життя (застосунок)
- `@Provides` - метод надає залежність
- `@Singleton` - один екземпляр на весь час життя застосунку

**Граф залежностей:**
```
UserRepository (Singleton)
    ↓
GetUsersUseCase & GetUserByIdUseCase
    ↓
UserViewModel
    ↓
UI Screens
```

---

### 6. Presentation Layer (MVVM)

#### 6.1. ViewModel з StateFlow

**`presentation/viewmodel/UserViewModel.kt`**

```kotlin
package com.example.hellocompose.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hellocompose.domain.model.User
import com.example.hellocompose.domain.usecase.GetUserByIdUseCase
import com.example.hellocompose.domain.usecase.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase
) : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    private val _selectedUser = MutableStateFlow<User?>(null)
    val selectedUser: StateFlow<User?> = _selectedUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _users.value = getUsersUseCase()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectUser(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _selectedUser.value = getUserByIdUseCase(id)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
```

**Ключові концепції:**

1. **@HiltViewModel** - автоматичне створення через Hilt
2. **@Inject constructor** - впровадження залежностей
3. **MutableStateFlow** - для внутрішніх змін
4. **StateFlow (asStateFlow())** - immutable для UI
5. **viewModelScope** - автоматичне скасування при знищенні ViewModel
6. **try-finally** - гарантує виконання коду (зняття loading)

---

#### 6.2. UI Screens

**`presentation/ui/UserListScreen.kt`**

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    navController: NavHostController,
    viewModel: UserViewModel = hiltViewModel()
) {
    val users by viewModel.users.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUsers()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Список користувачів") })
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when {
                isLoading && users.isEmpty() -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                users.isEmpty() -> {
                    Text(
                        text = "Немає користувачів",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(users) { user ->
                            ListItem(
                                headlineContent = { Text(user.name) },
                                supportingContent = { Text(user.email) },
                                modifier = Modifier.clickable {
                                    navController.navigate("detail/${user.id}")
                                }
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}
```

**Особливості реалізації:**

- `hiltViewModel()` - отримання ViewModel через Hilt
- `collectAsState()` - підписка на StateFlow з автоматичною рекомпозицією
- `LaunchedEffect(Unit)` - виконується один раз при створенні
- Різні стани UI: Loading, Empty, Data

---

**`presentation/ui/UserDetailScreen.kt`**

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    userId: Int,
    navController: NavHostController,
    viewModel: UserViewModel = hiltViewModel()
) {
    val user by viewModel.selectedUser.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(userId) {
        viewModel.selectUser(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Деталі користувача") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                user != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "ID користувача",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = user!!.id.toString(),
                                    style = MaterialTheme.typography.headlineSmall
                                )
                            }
                        }

                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Ім'я",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = user!!.name,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                            }
                        }

                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Email",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = user!!.email,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                            }
                        }
                    }
                }
                else -> {
                    Text(
                        text = "Користувача не знайдено",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}
```

---

#### 6.3. Навігація

**`presentation/navigation/NavGraph.kt`**

```kotlin
package com.example.hellocompose.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.hellocompose.presentation.ui.UserDetailScreen
import com.example.hellocompose.presentation.ui.UserListScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "list") {
        composable("list") {
            UserListScreen(navController)
        }

        composable("detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
            UserDetailScreen(userId = id, navController = navController)
        }
    }
}
```

---

#### 6.4. MainActivity

**`MainActivity.kt`**

```kotlin
package com.example.hellocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.hellocompose.presentation.navigation.NavGraph
import com.example.hellocompose.ui.theme.LabScreensTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LabScreensTheme {
                val navController = rememberNavController()
                NavGraph(navController)
            }
        }
    }
}
```

**@AndroidEntryPoint** - дозволяє Hilt впроваджувати залежності в Activity

---

### 7. Результати роботи

#### 7.1. Успішна збірка проєкту

```bash
BUILD SUCCESSFUL in 1m 45s
42 actionable tasks: 42 executed
```

#### 7.2. Встановлення на пристрій

```bash
Installing APK 'app-debug.apk' on 'Pixel_8a(AVD) - 16'
Installed on 1 device.
BUILD SUCCESSFUL
```

#### 7.3. Функціонал застосунку

✅ **Список користувачів**
- Відображення 5 користувачів
- Loading індикатор при завантаженні
- Клік на користувача відкриває деталі

✅ **Екран деталей**
- Відображення ID, імені та email
- Кнопка повернення назад
- Loading стан при завантаженні

✅ **Архітектура**
- Чіткий поділ на шари (Data, Domain, Presentation)
- Dependency Injection через Hilt
- StateFlow для реактивності
- MVVM патерн

---

## 8. Архітектурні патерни та принципи

### 8.1. Clean Architecture

**Шари архітектури:**

```
┌─────────────────────────────────────┐
│      Presentation Layer (UI)        │
│   - Screens (Composables)           │
│   - ViewModel                        │
│   - Navigation                       │
└──────────────┬──────────────────────┘
               │ залежить від
┌──────────────▼──────────────────────┐
│        Domain Layer (Core)          │
│   - Models                           │
│   - Repository Interface             │
│   - Use Cases                        │
└──────────────┬──────────────────────┘
               │ реалізує
┌──────────────▼──────────────────────┐
│         Data Layer (External)       │
│   - Repository Implementation        │
│   - Data Sources (API, DB)          │
└─────────────────────────────────────┘
```

**Переваги:**

1. **Незалежність від фреймворків** - Domain layer не залежить від Android
2. **Тестованість** - легко тестувати кожен шар окремо
3. **Гнучкість** - легко змінити джерело даних
4. **Maintainability** - код легко підтримувати та розширювати

---

### 8.2. MVVM (Model-View-ViewModel)

```
┌──────────┐         ┌──────────────┐         ┌───────┐
│   View   │ observe │  ViewModel   │  calls  │ Model │
│ (Screen) │◄────────│  (StateFlow) │────────►│(UseCase)
└──────────┘         └──────────────┘         └───────┘
     │                      │
     │      user actions    │
     └─────────────────────►│
```

**Компоненти:**

- **View (Screen)** - відображає UI, спостерігає за StateFlow
- **ViewModel** - містить стан UI, обробляє бізнес-логіку
- **Model** - UseCase + Repository, джерело даних

**Переваги MVVM:**
- Відокремлення UI від логіки
- Легке тестування ViewModel
- Автоматичне оновлення UI при зміні стану
- Збереження стану при configuration changes

---

### 8.3. Dependency Injection (Hilt)

**Принципи DI:**

1. **Інверсія залежностей (DIP)** - залежимо від абстракцій
2. **Впровадження залежностей** - не створюємо об'єкти самі
3. **Single Responsibility** - об'єкти не відповідають за створення залежностей

**Граф залежностей Hilt:**

```
UserApp (@HiltAndroidApp)
    │
    ├─► AppModule (Singleton)
    │       ├─► UserRepository (Singleton)
    │       ├─► GetUsersUseCase
    │       └─► GetUserByIdUseCase
    │
    ├─► MainActivity (@AndroidEntryPoint)
    │
    └─► UserViewModel (@HiltViewModel)
            ├─ GetUsersUseCase (injected)
            └─ GetUserByIdUseCase (injected)
```

**Переваги Hilt:**
- Автоматичне управління життєвим циклом
- Compile-time перевірка
- Інтеграція з Jetpack (ViewModel, Navigation)
- Менше boilerplate коду

---

### 8.4. StateFlow vs LiveData

| Характеристика | StateFlow | LiveData |
|----------------|-----------|----------|
| Частина | Kotlin Coroutines | Android Jetpack |
| Lifecycle aware | Ні (потрібно collectAsState) | Так |
| Початкове значення | Обов'язкове | Опційне |
| Thread-safe | Так | Так |
| Compose інтеграція | Ідеальна (collectAsState) | Потрібен адаптер |
| Зовні Android | Так (Kotlin Multiplatform) | Ні |

**Чому StateFlow краще для Compose:**
```kotlin
// StateFlow - nature інтеграція
val users by viewModel.users.collectAsState()

// LiveData - потрібен observeAsState()
val users by viewModel.users.observeAsState(emptyList())
```

---

## 9. Контрольні питання та відповіді

### 1. У чому полягає різниця між MVVM і MVC?

**MVC (Model-View-Controller):**
- View пасивна, Controller оновлює View
- Тісний зв'язок між компонентами
- Важко тестувати
- Controller містить багато логіки

**MVVM (Model-View-ViewModel):**
- View активно спостерігає за ViewModel
- Слабкий зв'язок (через observer pattern)
- Легко тестувати ViewModel
- ViewModel не знає про View
- Автоматичне оновлення UI

**Приклад:**
```kotlin
// MVVM - View спостерігає
val users by viewModel.users.collectAsState()

// MVC - Controller оновлює View
controller.updateUserList(users)
```

---

### 2. Які переваги Clean Architecture?

1. **Незалежність від UI** - можна змінити з XML на Compose без зміни бізнес-логіки

2. **Незалежність від баз даних** - легко змінити Room на Realm

3. **Незалежність від зовнішніх фреймворків** - Domain layer чистий Kotlin

4. **Тестованість:**
   ```kotlin
   // Легко тестувати UseCase
   val useCase = GetUsersUseCase(fakeRepository)
   val result = useCase()
   assertEquals(5, result.size)
   ```

5. **Підтримуваність** - зміни в одному шарі не впливають на інші

6. **Масштабованість** - легко додавати нові функції

---

### 3. Для чого потрібен Hilt?

**Основні завдання:**

1. **Управління залежностями:**
   ```kotlin
   // Без Hilt
   val repository = UserRepositoryImpl()
   val useCase = GetUsersUseCase(repository)
   val viewModel = UserViewModel(useCase, ...)
   
   // З Hilt
   @HiltViewModel
   class UserViewModel @Inject constructor(...) // автоматично
   ```

2. **Контроль життєвого циклу:**
   - `@Singleton` - на весь застосунок
   - `@ActivityScoped` - на Activity
   - `@ViewModelScoped` - на ViewModel

3. **Тестування:**
   ```kotlin
   @TestInstallIn(replaces = [AppModule::class])
   @Module
   object FakeAppModule {
       @Provides
       fun provideRepository() = FakeRepository()
   }
   ```

4. **Зменшення boilerplate коду** - не потрібно вручну створювати фабрики

---

### 4. Чому StateFlow кращий за LiveData у Compose?

**Переваги StateFlow:**

1. **Native Kotlin** - частина Kotlin Coroutines, не залежить від Android

2. **Кращий для Compose:**
   ```kotlin
   // StateFlow
   val state by viewModel.state.collectAsState()
   
   // LiveData
   val state by viewModel.state.observeAsState()
   ```

3. **Завжди має значення** - немає nullable початкового стану

4. **Kotlin Multiplatform** - можна використовувати в KMM

5. **Більше операторів:**
   ```kotlin
   val filteredUsers = users
       .map { it.filter { user -> user.name.contains(query) } }
       .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
   ```

6. **Type-safe** - компілятор перевіряє типи

**Коли використовувати LiveData:**
- Legacy код
- Потрібна lifecycle-awareness без Compose
- Інтеграція зі старими бібліотеками

---

### 5. Як реалізується зв'язок між ViewModel і UI?

**Механізм роботи:**

1. **ViewModel створює StateFlow:**
   ```kotlin
   private val _users = MutableStateFlow<List<User>>(emptyList())
   val users: StateFlow<List<User>> = _users.asStateFlow()
   ```

2. **UI підписується через collectAsState():**
   ```kotlin
   val users by viewModel.users.collectAsState()
   ```

3. **При зміні стану UI автоматично рекомпозується:**
   ```kotlin
   viewModel.loadUsers() // змінює _users.value
   // UI автоматично оновлюється
   ```

**Потік даних:**
```
User Action → ViewModel.loadUsers()
    ↓
UseCase викликається
    ↓
Repository повертає дані
    ↓
ViewModel оновлює MutableStateFlow
    ↓
StateFlow емітує нове значення
    ↓
collectAsState() отримує оновлення
    ↓
Compose рекомпозиція UI
```

**Lifecycle:**
- `collectAsState()` автоматично підписується при створенні
- Автоматично відписується при знищенні Composable
- Не потрібно ручне управління підпискою

---

## 10. Висновки

В ході виконання лабораторної роботи було:

1. **Реалізовано Clean Architecture** - проєкт чітко розділений на три шари (Data, Domain, Presentation), що забезпечує високу підтримуваність та тестованість коду

2. **Впроваджено MVVM патерн** - відокремлення бізнес-логіки від UI через ViewModel, що спрощує тестування та підтримку

3. **Налаштовано Hilt DI** - автоматичне управління залежностями, що зменшує boilerplate код та забезпечує правильне управління життєвим циклом об'єктів

4. **Використано StateFlow** - реактивний підхід до управління станом, що ідеально інтегрується з Jetpack Compose

5. **Створено Use Cases** - інкапсуляція бізнес-логіки в окремі класи, що полегшує повторне використання та тестування

6. **Реалізовано Repository Pattern** - абстракція джерела даних, що дозволяє легко змінювати реалізацію без впливу на інші шари

**Ключові досягнення:**
- ✅ Модульна архітектура з чітким розділенням відповідальності
- ✅ Dependency Injection через Hilt
- ✅ Реактивне управління станом через StateFlow
- ✅ Легко тестується код
- ✅ Готовність до масштабування

Всі вимоги лабораторної роботи виконано повністю. Застосунок демонструє сучасні best practices Android розробки.

---

## 11. Скріншоти

### Список користувачів
![User List](screenshots/user_list.png)
*Екран зі списком користувачів з індикатором завантаження*

### Деталі користувача
![User Details](screenshots/user_details.png)
*Екран деталей з інформацією про користувача*

### Архітектура проєкту
![Project Structure](screenshots/project_structure.png)
*Структура файлів згідно Clean Architecture*

---

## 12. Використані ресурси

- [Hilt Documentation](https://developer.android.com/training/dependency-injection/hilt-android)
- [Clean Architecture by Uncle Bob](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [StateFlow and SharedFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow)
- [MVVM Architecture](https://developer.android.com/topic/architecture)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [Jetpack Compose Navigation](https://developer.android.com/jetpack/compose/navigation)
