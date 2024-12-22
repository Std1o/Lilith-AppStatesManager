# God Of App States
1. [About library](#about-library)
2. [Requirements](#requirements)
3. [Compatibility with DI](#compatibility-with-di)
4. [Architecture requirements](#architecture-requirements)
5. [Architecture by using library](#architecture-by-using-library)
6. [Setup](#setup)
7. [Usage](#usage)
    1. [OperationState generation](#operationstate-generation)
    2. [LoadableData generation](#loadabledata-generation)
    3. [Making single events](#making-single-events)
        1. [Events markers](#events-markers)
        2. [SingleEventFlow](#singleeventflow)
    4. [Activate code generation](#activate-code-generation)
    5. [Adding new states](#adding-new-states)
    6. [BaseRemoteDataSource](#baseremotedatasource)
    7. [StatesViewModel](#statesviewmodel)
        1. [loadData](#loaddata)
        2. [loadDataFlow](#loaddataflow)
        3. [executeOperation](#executeoperation)
        4. [executeEmptyOperation](#executeemptyoperation)
        5. [executeOperationAndIgnoreData](#executeoperationandignoredata)
        6. [Important information](#important-information)
    8. [UI reaction to state](#ui-reaction-to-state)
    9. [StillLoading annotation](#stillloading-annotation)

## О библиотеке
What does this library do?
1. It helps to create your own states without losing or duplicating the basic ones (Loading, Error, Success, Empty204).
2. Automatically forms base states.
3. Removes boilerplate code from ViewModel. You will not have to worry about delivery of Loading and Error states. For your functionality states you can define a general reaction to these states and, if necessary, assign a specific reaction for screen, or different reactions by OperationType.
4. Automatically solves problem with Loading state collisions. In addition, there is the mechanism that allows you to handle same state on the screen in different ways, depending on the request.
5. Ideologically, it helps to create independent states for the components of the screen. For example, it is very easy to make skeletons with this approach.

The library is flexibly combined with existing code. You can just not use library in some places.

When using the library, it is important to adhere to the concept. The library helps to adhere it as much as possible with minimal effort.

It is successfully being used for one [medium pet project](https://github.com/Std1o/StudentTestingSystem),  but for large projects it is not time-tested yet

Also you can se [wiki Simple OperationState explanation](https://github.com/Std1o/GodOfAppStates/wiki/Simple-OperationState-explanation)

## Requirements
1. Kotlin
2. compileSdk 34+ and targetSdk 34+
3. Retrofit usage
4. Android Gradle plugin version 8+
5. 'org.jetbrains.kotlin.android' version '1.9.0'+

## Compatibility with DI
For DI you must use ksp intead of kapt.

Just replace "kapt" with "ksp" in build.gradle of your module. Also you can see https://dagger.dev/dev-guide/ksp.html

1. Koin - 100%
2. Hilt - Compatible when using a small workaround
3. Dagger 2 - not tested
4. Other DIs - there are no plans to check in the near future

## Architecture requirements
Simplifying, your architecture should be something like this.

![simple_arch](https://github.com/Std1o/GodOfAppStates/assets/37378410/94055f61-6e88-495e-be03-00dfa223df7a)

If this is unfamiliar to you, you can google MVVM, Clean Architecture, Repository Pattern

## Architecture by using library
Recommendation:
1. Take a quick look at what the architecture will turn out to be in the end.
2. See what you need to do for this.
3. Go back to the diagram and take a closer look at what will be happened as a result.

As a result of using the library, you will get something like this architecture. It may be hard to understand, but it's simple to use. You practically don't have to do anything to get such an architecture.

Don't delve into it yet. Read the usage item, then return to the diagram. After that, you can start reproduce [Usage](#usage)

![god_of_app_states](https://github.com/Std1o/GodOfAppStates/assets/37378410/56143f47-1710-43de-8325-7525e7471d28)

## Setup

In your settings.gradle
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
In your project build.gradle
```Gradle
buildscript {
    dependencies {
        classpath("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:1.9.10-1.0.13")
    }
}
```
In your app module build.gradle add ksp plugin
```Gradle
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'com.google.devtools.ksp'
}
```
Then add the dependencies
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
## Usage
### OperationState generation
> [!NOTE]  
> To generate the OperationState, you need to create at least one functionality state marked with an annotation.
>
> Functionality state is a state that you create in your [Use Cases](https://developer.android.com/topic/architecture/domain-layer) if the OperationState states are not enough for you or if the state must be long-term.
>
> If you don't need your functionality state yet, you can write it and delete it as soon as a useful one appears.
>
> ```Kotlin
> @OperationState
> sealed interface OperationStateTrigger<out R>
> ```

To create your own functionality state with access to the base operations states (Success, Error, Loading,  Empty204, NoState) just create something like this:
```Kotlin
@OperationState
sealed interface YourFunctionalityState<out R> {
    data class State1(val someData: SomeData) : YourFunctionalityState<Nothing>
    data object State2 : YourFunctionalityState<Nothing>
    data object State3 : YourFunctionalityState<Nothing>
}
```
All operation states must be in the same package.

> [!IMPORTANT]  
> You can create a hierarchy of your states, but keep in mind that it should be reverse. [Why?](https://github.com/Std1o/GodOfAppStates/wiki/Reverse-hierarchy)
> 
> You need to mark  with an annotation only semantically base class in your hierarchy.
> 
> Example:
> 
> <img width="542" alt="image" src="https://github.com/Std1o/GodOfAppStates/assets/37378410/e047952a-592c-47bb-91b8-f731ddb12f88">

You can see real examples [here](https://github.com/Std1o/StudentTestingSystem/tree/main/app/src/main/java/student/testing/system/domain/states/operationStates)

> [!IMPORTANT]
> OperationState contains the result of operation and is not intended for long-term storage state.
>
> For long-term states, create your own functionality states marked with an annotation.

### LoadableData generation
LoadableData is state of some view or composable fun. Thanks to it, the state of the component is isolated from the rest of the screen, which allows you to increase UX and contributes to the absence of collisions.

To create a custom LoadableData, just mark your sealed interface with @LoadableData annotation:
```Kotlin
@LoadableData
sealed interface CustomLoadableData<out R> {
    data class State1(val someData: SomeData) : CustomLoadableData<Nothing>
    data object State2 : CustomLoadableData<Nothing>
    data object State3 : CustomLoadableData<Nothing>
}
```

If you don't need a custom LoadableData yet, you can write it and delete it as soon as a useful one appears.

```Kotlin
@LoadableData
sealed interface LoadableDataTrigger<out R>
```
> [!IMPORTANT]
> A hierarchy here is also reversed

LoadableData is used in ContentState. It looks something like this:
```Kotlin
@ContentState
data class SomeContentState(
    val someList: LoadableData<List<T>> = LoadableData.NoState,
    val someImage: LoadableData<String> = LoadableData.NoState,
    val someText: LoadableData<String> = LoadableData.NoState,
    val someList2: LoadableData<List<K>> = LoadableData.NoState,
)
```

### Making single events
#### Events markers
You can use this at the sealed class level, and then all childs will be single.
```Kotlin
@SingleEvents
sealed interface MainScreenEventsChild<out R> {
    data class ShowSuccessToast(val text: String) : MainScreenEventsChild<String>
}
```

Also you can use this in child of parent NOsinfgle sealed class
```Kotlin
@SingleEvent
    data class ErrorSingle(
        val exception: String,
        val code: Int = -1,
        val operationType: OperationType = OperationType.DefaultOperation
    ) : OperationState<Nothing>
```

If sealed class is single but you need one non single state you can use @NoSingleEvent
```Kotlin
@NoSingleEvent
    data class Loading(val operationType: OperationType = OperationType.DefaultOperation) :
        OperationState<Nothing>
```

#### SingleEventFlow
Class for single events flow.
In other words, the action is performed only once.
And it will not be executed again even if the device configuration was changed in.

Usage example №1
```Kotlin
private val _screenEvents = SingleEventFlow<MainScreenEventsChild<String>>()
val screenEvents = _screenEvents.asSharedFlow()
```

Usage example №2
```Kotlin
private val _resultReviewEvent = SingleEventFlow<TestResult>()
val resultReviewFlow = _resultReviewEvent.asSharedFlow()
```

### Activate code generation
When there is at least one state marked with @OperationState annotation and at least one state marked with @LoadableData annotation, you can start generating some library classes.

To do this, mark your Application class with @AllStatesReadyToUse annotation.
```Kotlin
@AllStatesReadyToUse
class App : Application()
```

If you have created an Application class just now, don't forget to specify it in manifest.

<img width="265" alt="image" src="https://github.com/Std1o/GodOfAppStates/assets/37378410/6e3d9c3b-7f6f-4bcd-85e0-3dfacdcc6725">

After that, rebuild your project. How to do this is written in the next section.

### Adding new states
During app development most likely you will need to create your own functionality states or custom LoadableData.

To make basic states available for recently added annotated states, you need to "make module".

<img width="554" alt="Снимок экрана 2023-10-27 в 20 44 41" src="https://github.com/Std1o/GodOfAppStates/assets/37378410/36e4df4d-3cba-4115-9bed-f6ba7a7bf96c">

If it doesn't help, then rebuild project

<img width="498" alt="image" src="https://github.com/Std1o/GodOfAppStates/assets/37378410/85935610-54cf-477a-ba08-9bcfd168cb30">

### BaseRemoteDataSource
This class has 2 methods. To use them, inherit your RemoteDataSource from BaseRemoteDataSource.

```Kotlin
class RemoteDataSource(private val someRetrofitService: SomeRetrofitService) : BaseRemoteDataSource() {
    // Your methods
}
```

Method executeOperation() generates OperationState that contains a limited set of states for any request.

Usage exmaple:
```Kotlin
override suspend fun signUp(request: SignUpReq) = executeOperation { mainService.signUp(request) }
```
To solve collisions when using multiple operations on screen, the result of which UI should react differently, you can specify OperationType.

You don't have to worry about the Loading status collision, it's solved in StatesViewModel (we will talk about it later).

Usage example:
```Kotlin
override suspend fun createCourse(request: CourseCreationReq) =
    executeOperation(CourseAddingOperations.CREATE_COURSE) { mainService.createCourse(request) }
```
Then you can get OperationType in UI if state is Success, Error or Empty204.

There is example of your OperationType:
```Kotlin
enum class CourseAddingOperations : OperationType {
    CREATE_COURSE, JOIN_COURSE
}
```

Method loadData() generates LoadableData that contains a limited set of loading data states.

Usage example:
```Kotlin
override suspend fun getCourses() = loadData { mainService.getCourses() }
```

### StatesViewModel
StatesViewModel contains a StateFlow that broadcasts last operation state, and a method that launching operations and updating last operation state based on the response.

Also StatesViewModel has methods for calling LoadableData requests with automatically setting Loading status.

> [!IMPORTANT]
> If you use Hilt, create it in your ViewModels package and be sure not to forget to import generated StatesViewModel
> ```Kotlin
> @Suppress("unused")
> class StatesViewModel : ViewModel()
> ```
> @HiltViewModel requires subclass of ViewModel. Hilt doesn't see our generated StatesViewModel so Hilt can't check it

#### loadData
Method for calling LoadableData requests, that automatically sets Loading status.

Params: call - lambda that returns LoadableData or parent of LoadableData.

At first create a flow of ContentState and a variable to easily update the flow.

Example:
```Kotlin
private val _contentState = MutableStateFlow(CoursesContentState())
val contentState = _contentState.asStateFlow()
private var contentStateVar by stateFlowVar(_contentState)
```
Then you can call loadData().

Usage example:
```Kotlin
viewModelScope.launch {
    loadData { repository.getCourses() }.collect {
        // Where "courses" is LoadableData
        contentStateVar = contentStateVar.copy(courses = it)
    }
}
```
#### loadDataFlow
Method for calling LoadableData requests, that automatically sets Loading status

Params: call - lambda that returns flow of LoadableData or parent of LoadableData

Usage example:
```Kotlin
viewModelScope.launch {
    // Here repository.getCourses() returns Flow of LoadableData
    loadDataFlow { repository.getCourses() }.collect {
        contentStateVar = contentStateVar.copy(courses = it)
    }
}
```

#### executeOperation
Launches operations and updating last operation state based on the response.

<img width="585" alt="image" src="https://github.com/Std1o/GodOfAppStates/assets/37378410/a65f0800-3ffd-413a-aeca-966825e0e664">

All "execute" methods can work with your functionality state. If current state is some state of OperationState it automatically send to lastOperationState which is contained in StatesViewModel. Then method returns current state.

Example:
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
In the previous example, lambda returned functionality state. You can also call this method passing lambda that returns flow of functionality state or OperationState.

Example:
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
> All "execute" methods can take in functionality state, OperationState or flow of ever of both

#### executeEmptyOperation
If you are sure that method return 204 response code instead of 200.

onEmpty204 - An optional callback function that may be called for some ViewModel businesses.

Example:
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
We will talk about the protect() extension later.

#### executeOperationAndIgnoreData
If it doesn't matter to you what response data will be returned on success.

Example:
```Kotlin
viewModelScope.launch {
    val requestResult = executeOperationAndIgnoreData({ loginUseCase(email, password) }) {
        navigateToCourses()
    }
    _loginState.value = requestResult
}
```

#### Important information
If you don't use either val/var or collect for the methods listed above, you need to call protect().

Example:
```Kotlin
executeOperation({ createTestUseCase(testCreationReq) }, Test::class).protect()
```
This is necessary because otherwise kotlin compiler generates incorrect lambda return type in the invokeSuspend() method.

### UI reaction to state
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

### StillLoading annotation
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
## License
This project is licensed under the [Apache License, Version 2.0](https://github.com/Std1o/GodOfAppStates/blob/main/LICENSE)
