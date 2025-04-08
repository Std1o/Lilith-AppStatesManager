# Лилит – менеджер состояний приложения
[![en](https://img.shields.io/badge/lang-en-red.svg)](https://github.com/Std1o/Lilith-App.States.Manager/blob/main/README-EN.md)
[![ru](https://img.shields.io/badge/lang-ru-blue.svg)](https://github.com/Std1o/Lilith-App.States.Manager)

Перед ознакомлением с документацией рекомендуется посмотреть [видео](https://disk.yandex.ru/i/VDFpfnZl_z4t7Q) или хотя бы [презентацию](https://github.com/user-attachments/files/18225885/lilith.pptx)

1. [О библиотеке](#о-библиотеке)
2. [Требования](#требования)
3. [Совместимость с DI](#совместимость-с-di)
4. [Требования к архитектуре](#требования-к-архитектуре)
5. [Архитектура при использовании библиотеки](#архитектура-при-использовании-библиотеки)
6. [Установка](#установка)
7. [Использование](#использование)
    1. [Генерация OperationState](#генерация-operationstate)
    2. [Генерация LoadableData](#генерация-loadabledata)
    3. [Создание single ивентов](#создание-single-ивентов)
        1. [Event markers](#event-markers)
        2. [SingleEventFlow](#singleeventflow)
    4. [Включение кодогенерации](#включение-кодогенерации)
    5. [Добавление новых состояний](#добавление-новых-состояний)
    6. [BaseRemoteDataSource](#baseremotedatasource)
    7. [StatesViewModel](#statesviewmodel)
        1. [loadData](#loaddata)
        2. [loadDataFlow](#loaddataflow)
        3. [executeOperation](#executeoperation)
        4. [executeEmptyOperation](#executeemptyoperation)
        5. [executeOperationAndIgnoreData](#executeoperationandignoredata)
        6. [Важная информация](#важная-информация)
    8. [Реакция UI на состояние](#реакция-UI-на-состояние)
    9. [StillLoading аннотация](#stillloading-аннотация)

## О библиотеке
Что делает библиотека:

1. Помогает создавать новые наборы состояний без потери или дублирования базовых.
2. Автоматически формирует базовые состояния.
3. Убирает boilerplate code из ViewModel. Во View можно назначить как общие, так и уникальные реакции на Error и Loading.
4. Убирает коллизии лоадера незавсимо от причины возникновения.
5. Помогает создавать независимые состояния компонентов экрана (сама сформирует состояния, сама разберётся с лоадингом). Пример такого подхода: скелетоны, показ ошибки на конкретном компоненте экрана, а не на всём экране и т.д.

Библиотека не будет конфликтовать с вашей архитектурой, если она +- сделана по clean. Можно написать новый экран с библиотекой, а старые не трогать.

Библиотека поддерживает single ивенты из коробки

Библиотека успешно используется на одно из [средних pet проектах](https://github.com/Std1o/StudentTestingSystem)

## Требования
1. Kotlin
2. compileSdk 34+ and targetSdk 34+
3. Retrofit usage
4. Android Gradle plugin version 8+
5. 'org.jetbrains.kotlin.android' version '1.9.0'+

## Совместимость с DI
Для DI необходимо использовать ksp вместо kapt.

Просто замените "kapt" на "ksp" в build.gradle вашего модуля. Также можете посмотреть https://dagger.dev/dev-guide/ksp.html

1. Koin - 100%
2. Hilt - Совместимо с небольшим костылём
3. Dagger 2 - Не проверено
4. Остальные DI - Проверка не планируется в ближайшем будущем

## Требования к архитектуре
Упрощая, ваша архитектура должна выглядеть примерно так.

![simple_arch](https://github.com/Std1o/GodOfAppStates/assets/37378410/94055f61-6e88-495e-be03-00dfa223df7a)

Если это вам незнакомо, вы можете погуглить: MVVM, Clean Architecture, Repository Pattern

## Архитектура при использовании библиотеки
Рекомендация:
1. Взгляните на то, какой в итоге получится архитектура.
2. Посмотрите, что нужно для этого сделать.
3. Вернитесь к диаграмме и посмотрите внимательнее, что должно получится в итоге.

В результате использования библиотеки, мы получим примерно такую архитектуру. Она может быть сложна для понимания, но проста в использовании. Вам практически ничего не нужно делать, чтобы получить такую архитектуру.

Пока не углубляйтесь в это. Прочтите пункт [Использование](#использование), затем вернитесь к диаграмме. После этого вы можете приступить к воспроизведению.

![god_of_app_states](https://github.com/Std1o/GodOfAppStates/assets/37378410/56143f47-1710-43de-8325-7525e7471d28)

## Установка

В вашем settings.gradle
```Gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url "https://jitpack.io" }
    }
}
```
В вашем build.gradle на уровне проекта
```Gradle
buildscript {
    dependencies {
        classpath("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:1.9.10-1.0.13")
    }
}
```
В вашем build.gradle на уровне модуля app добавьте ksp plugin
```Gradle
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'com.google.devtools.ksp'
}
```
Затем добавьте зависимости
```Gradle
dependencies {

    // God Of App States
    implementation 'com.github.Std1o:GodOfAppStates:0.3.5'
    // it must be before Dagger2/Hilt ksp
    ksp 'com.github.Std1o:GodOfAppStates:0.3.5'

    // Requirements
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation "org.jetbrains.kotlin:kotlin-reflect:1.8.22"
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2'
}
```
## Использование
### Генерация OperationState
> [!NOTE]  
> Чтобы сгенерировать OperationState нужно создать как минимум одно функциональное состояние, помеченное аннотацией.
>
> Функциональное состояние – это состояние, которое вы создаёте в вашем [Use Cases](https://developer.android.com/topic/architecture/domain-layer) если OperationState состояний недостаточно или требуется долгосрочное состояние.
>
> Если вам пока что не нужно функциональное состояние, вы можете написать это. Как только появится полезное функциональное состояние, можете удалить это.
>
> ```Kotlin
> @OperationState
> sealed interface OperationStateTrigger<out R>
> ```

Чтобы создать ваше собственное функциональное состояние с доступом к базовым состояниям операций (Success, Error, Loading,  Empty204, NoState) просто создайте что-то в роде этого:
```Kotlin
@OperationState
sealed interface YourFunctionalityState<out R> {
    data class State1(val someData: SomeData) : YourFunctionalityState<Nothing>
    data object State2 : YourFunctionalityState<Nothing>
    data object State3 : YourFunctionalityState<Nothing>
}
```
Все состояния операций должны находиться в одном пакете.

> [!IMPORTANT]  
> Вы можете создать иерархию ваших состояний, но имейте в виду, что она должна быть обратной. [Почему?](https://github.com/Std1o/GodOfAppStates/wiki/Reverse-hierarchy)
> 
> Вам нужно пометить аннотацией только семантически базовый класс в вашей иерархии.
> 
> Пример:
> 
> <img width="542" alt="image" src="https://github.com/Std1o/GodOfAppStates/assets/37378410/e047952a-592c-47bb-91b8-f731ddb12f88">

Вы можете посмотреть реальный пример использования [здесь](https://github.com/Std1o/StudentTestingSystem/tree/main/app/src/main/java/student/testing/system/domain/states/operationStates)

> [!IMPORTANT]
> OperationState содержит результат операции и не предназначен для долгосрочных состояний.
>
> Для долгосрочных состоянй создайте собственные функциональные состояния, помеченные аннотацией.

### Генерация LoadableData
LoadableData – это состояние некоторого View или composable-функции. Благодаря ему, состояние компонента изолировано от остальной части экрана, что позволяет улучшить UX и способствует отсутствию коллизий.

Чтобы создать кастомную LoadableData, просто пометьте ваш sealed interface аннотацией @LoadableData:
```Kotlin
@LoadableData
sealed interface CustomLoadableData<out R> {
    data class State1(val someData: SomeData) : CustomLoadableData<Nothing>
    data object State2 : CustomLoadableData<Nothing>
    data object State3 : CustomLoadableData<Nothing>
}
```

Если вам пока что не нужен кастомный LoadableData, вы можете написать это, и удалить, как только появится полезный LoadableData.

```Kotlin
@LoadableData
sealed interface LoadableDataTrigger<out R>
```
> [!IMPORTANT]
> Иерархия здесь тоже обратная

LoadableData используется ContentState. Это выглядит примерно так:
```Kotlin
@ContentState
data class SomeContentState(
    val someList: LoadableData<List<T>> = LoadableData.NoState,
    val someImage: LoadableData<String> = LoadableData.NoState,
    val someText: LoadableData<String> = LoadableData.NoState,
    val someList2: LoadableData<List<K>> = LoadableData.NoState,
)
```

### Создание single ивентов
#### Event markers
Вы можете использовать это на уровне sealed интерфейса, и тогда все дочерние элементы будут single.
```Kotlin
@SingleEvents
sealed interface MainScreenEventsChild<out R> {
    data class ShowSuccessToast(val text: String) : MainScreenEventsChild<String>
}
```

Также вы можете использовать single на уровне дочернего элемента в не single sealed интерфейсе.
```Kotlin
@SingleEvent
    data class ErrorSingle(
        val exception: String,
        val code: Int = -1,
        val operationType: OperationType = OperationType.DefaultOperation
    ) : OperationState<Nothing>
```

Если sealed interface является single, но вам нужно не single состояние, вы можете использовать аннотацию @NoSingleEvent.
```Kotlin
@NoSingleEvent
    data class Loading(val operationType: OperationType = OperationType.DefaultOperation) :
        OperationState<Nothing>
```

#### SingleEventFlow
Класс для flow single ивентов. 
Другими словами, действие выполнится только один раз, и не будет запущено снова при смене конфигурации устройства.

Пример использования №1
```Kotlin
private val _screenEvents = SingleEventFlow<MainScreenEventsChild<String>>()
val screenEvents = _screenEvents.asSharedFlow()
```

Пример использования №2
```Kotlin
private val _resultReviewEvent = SingleEventFlow<TestResult>()
val resultReviewFlow = _resultReviewEvent.asSharedFlow()
```

### Включение кодогенерации
Если есть хотя бы одно состояние, помеченное аннотацией @OperationState, и хотя бы одно состояние, помеченное аннотацией @LoadableData, можно приступить к генерации классов библиотеки.

Чтобы сделать это, пометьте ваш Application класс аннотацией @AllStatesReadyToUse.
```Kotlin
@AllStatesReadyToUse
class App : Application()
```

Если вы создали Application класс только что, не забудьте указать его в манифесте.

<img width="265" alt="image" src="https://github.com/Std1o/GodOfAppStates/assets/37378410/6e3d9c3b-7f6f-4bcd-85e0-3dfacdcc6725">

После этого сделайте rebuild проекта. Как это сделать написано в следующей секции.

### Добавление новых состояний
Во время разработки приложения, скорее всего, вам потребуется создать свои собственные функциональные состояния или кастомную LoadableData.

Чтобы сделать базовые состояния доступными для недавно добавленных аннотированных состояний, вам нужно нажать "make module".

<img width="554" alt="Снимок экрана 2023-10-27 в 20 44 41" src="https://github.com/Std1o/GodOfAppStates/assets/37378410/36e4df4d-3cba-4115-9bed-f6ba7a7bf96c">

Если это не помогло, нажмите "rebuild project"

<img width="498" alt="image" src="https://github.com/Std1o/GodOfAppStates/assets/37378410/85935610-54cf-477a-ba08-9bcfd168cb30">

### BaseRemoteDataSource
Этот класс имеет 2 метода. Чтобы использовать их, наследуйте свой RemoteDataSource от BaseRemoteDataSource.

```Kotlin
class RemoteDataSource(private val someRetrofitService: SomeRetrofitService) : BaseRemoteDataSource() {
    // Your methods
}
```

Метод executeOperation() генерирует OperationState, который содержит ограниченный набор состояний для любого запроса.

Пример использования:
```Kotlin
override suspend fun signUp(request: SignUpReq) = executeOperation { mainService.signUp(request) }
```
Чтобы избежать коллизий при использовании нескольких операций на экране, на результат которых UI должен реагировать по-разному, можно задать OperationType.

Вам не нужно беспокоиться о коллизиях лоадера, это решается в StatesViewModel (об этом позже).

Пример использования:
```Kotlin
override suspend fun createCourse(request: CourseCreationReq) =
    executeOperation(CourseAddingOperations.CREATE_COURSE) { mainService.createCourse(request) }
```
Затем вы можете получить OperationType в UI если состояние Success, Error или Empty204.

Пример вашего OperationType:
```Kotlin
enum class CourseAddingOperations : OperationType {
    CREATE_COURSE, JOIN_COURSE
}
```

Метод loadData() генерирует LoadableData, который содержит ограниченный набор состояний загружаемых данных.

Пример использования:
```Kotlin
override suspend fun getCourses() = loadData { mainService.getCourses() }
```

### StatesViewModel
StatesViewModel содержит StateFlow для транслирования последнего состояния операции и методы для выполнения операций с автоматическим обновлением состояний.

Также StatesViewModel имеет методы для вызова LoadableData запросов с автоматической установкой Loading статуса.

> [!IMPORTANT]
> Если вы используете Dagger 2/Hilt, создайте это в вашем пакете с ViewModel и убедитесь, что не забыли ипортировать сгенерированную StatesViewModel.
> ```Kotlin
> @Suppress("unused")
> class StatesViewModel : ViewModel()
> ```
> @HiltViewModel требует подкласса ViewModel. Hilt не видит сгенерированную StatesViewModel, соответственно Hilt не может её проверить.

#### loadData
Метод для вызова LoadableData запросов, который автоматически ставит Loading состояние.

Параметры: call - лямбда, возвращающая LoadableData или родителя LoadableData.

Сначала создайте flow ContentState и переменную для лёгкого обновления flow.

Пример:
```Kotlin
private val _contentState = MutableStateFlow(CoursesContentState())
val contentState = _contentState.asStateFlow()
private var contentStateVar by stateFlowVar(_contentState)
```
Затем вы может вызвать loadData().

Пример использования:
```Kotlin
viewModelScope.launch {
    loadData { repository.getCourses() }.collect {
        // Where "courses" is LoadableData
        contentStateVar = contentStateVar.copy(courses = it)
    }
}
```
#### loadDataFlow
Метод для вызова LoadableData запросов, который автоматически ставит Loading состояние.

Параметры: call - лямбда, возвращающая LoadableData или родителя LoadableData.

Пример использования:
```Kotlin
viewModelScope.launch {
    // Here repository.getCourses() returns Flow of LoadableData
    loadDataFlow { repository.getCourses() }.collect {
        contentStateVar = contentStateVar.copy(courses = it)
    }
}
```

#### executeOperation
Запускает операции и обновляет последнее состояние операции на основе ответа.

<img width="585" alt="image" src="https://github.com/Std1o/GodOfAppStates/assets/37378410/a65f0800-3ffd-413a-aeca-966825e0e664">

Все "execute" методы могут работать с вашим функциональным состоянием. Если текущее состояние является некоторым состоянием OperationState, оно автоматически отправляется в lastOperationState, которое содержится в StatesViewModel. Затем метод возвращает текущее состояние.

Пример:
```Kotlin
// Some functionality state if you need it
private val _testState = MutableStateFlow<TestCreationState<Test>>(OperationState.NoState)
val testState = _testState.asStateFlow()

// ...
// ...

viewModelScope.launch {
    val requestResult =
        executeOperation({ createTestUseCase(testCreationReq) }, Test::class)
    _testState.value = requestResult
    // Here we don't use onSuccess callback because otherwise our assignment would be erased by the line above
    // Actually you can assign this state in your UseCase
    if (requestResult is OperationState.Success) {
        _testState.value = TestCreationState.Created(requestResult.data)
    }
}
```
В предыдущем примере лямбда-выражение возвращало функциональное состояние. Вы также можете вызвать этот метод, передавая лямбда-выражение, возвращающее flow функционального состояния или OperationState.

Пример:
```Kotlin
viewModelScope.launch {
    executeOperation(
        call = { createCourseUseCase(name) },
        operationType = CourseAddingOperations.CREATE_COURSE, // Optionally. You can define it to handle Loading states differently
        type = defaultType // private val defaultType = CourseResponse::class
    ) { courseResponse -> // onSuccess callback
        addCourseToContent(courseResponse)
    }.collect {
        _lastValidationState.value = it
    }
}
```
> [!NOTE]
> Все методы "execute" могут принимать функциональное состояние, OperationState или поток любого из них

#### executeEmptyOperation
Если вы уверены, что метод возвращает код 204 вместо 200

onEmpty204 - Необязательный коллбек, который может быть вызван для некоторых действий во ViewModel.

Пример:
```Kotlin
viewModelScope.launch {
    executeEmptyOperation({ repository.deleteCourse(courseId) }) { // it's onEmpty204 callback
        val newCourses = (contentStateVar.courses as LoadableData.Success)
            .data.filter { it.id != courseId }
        contentStateVar =
            contentStateVar.copy(courses = LoadableData.Success(newCourses))
    }.protect()
}
```
Об extension'е protect() будет далее.

#### executeOperationAndIgnoreData
Если для вас неважно, какие данные ответа будут возвращены в случае успеха.

Пример:
```Kotlin
viewModelScope.launch {
    val requestResult = executeOperationAndIgnoreData({ loginUseCase(email, password) }) {
        navigateToCourses()
    }
    _loginState.value = requestResult
}
```

#### Важная информация
If you don't use either val/var or collect for the methods listed above, you need to call protect().

Example:
```Kotlin
executeOperation({ createTestUseCase(testCreationReq) }, Test::class).protect()
```
This is necessary because otherwise kotlin compiler generates incorrect lambda return type in the invokeSuspend() method.

### Реакция UI на состояние
Collecting of your states will look something like this:
```Kotlin
val contentState by viewModel.contentState.collectAsState() // Then you can use contentState.someLoadableData
val lastOperationState by viewModel.lastOperationState.collectAsState() // This is in the StatesViewModel
// Please don't check the states from OperationState by this val, there are lastOperationState for this
val loginState by viewModel.loginState.collectAsState() // example of functionality state
```
If you are using Jetpack Compose, just create UIReactionOnLastOperationState.kt and paste this code there:
```Kotlin
/**
 * Used for temporary and short-lived states caused by the last operation
 * @param onLoading if you want override default loading
 * @param onError if you want override on error default reaction
 */
@Composable
fun <T> UIReactionOnLastOperationState(
    operationState: OperationState<T>,
    onErrorReceived: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onLoading: ((OperationType) -> Unit)? = null,
    onError: ((String, Int, OperationType) -> Unit)? = null
) {
    with(operationState) {
        when (this) {
            is OperationState.Loading -> onLoading?.invoke(operationType) ?: LoadingIndicator()
            is OperationState.Error -> {
                LaunchedEffect(Unit) {
                    onError?.invoke(exception, code, operationType)
                        ?: snackbarHostState.showSnackbar(exception)
                    onErrorReceived()
                }
            }

            is OperationState.Empty204,
            is OperationState.Success,
            is OperationState.NoState -> {
                // do nothing
            }
        }
    }
}
```
You can modify this function as you want.

Then just add this to your screen:
```Kotlin
UIReactionOnLastOperationState(
    lastOperationState,
    { testsVM.onErrorReceived() },
    snackbarHostState
)
```
> [!NOTE]
> This is a composable fun that handles OperationState.Loading and OperationState.Error.
>
> For Success and Empty204 states, it is recommended to perform actions in your ViewModel or set some state to your functionality state and work in the UI already with it.

This part of the code is not included in the library, so not to make a dependency on Jetpack Compose.

### StillLoading аннотация
For states from UseCase. Mark with it if loader should be still shown. Note that in this case, UseCase must return flow.

Example:
```Kotlin
@OperationState
sealed interface ValidatableOperationState<out R> {
    // ...
    // ...
    @StillLoading
    data class SuccessfulValidation(val operationType: OperationType = OperationType.DefaultOperation) :
        ValidatableOperationState<Nothing>
}
```
## Лицензия
This project is licensed under the [Apache License, Version 2.0](https://github.com/Std1o/GodOfAppStates/blob/main/LICENSE)
