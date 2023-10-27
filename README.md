# God Of App States
1. [About library](#about-library)
2. [Requirements](#requirements)
3. [Compatibility with DI](#compatibility-with-di)
4. [Architecture requirements](#architecture-requirements)
5. [Architecture by using library](#architecture-by-using-library)
6. [Setup](#setup)
7. [Usage](#usage)
    1. [OperationState generation](#operationstate-generation)
    2. [LoadableData](#loadabledata)
    3. [Activate code generation](#activate-code-generation)
    4. [Adding new states](#adding-new-states)
    5. [BaseRemoteDataSource](#baseremotedatasource)
    6. [StatesViewModel](#statesviewmodel)

## About library
This library created firstly for declarative UI (e.g. Jetpack Compose). The main idea of the library is easy work with states, automation of routine and all this without loss testability, flexibility and without increasing cohesion in the code

## Requirements
1. Kotlin
2. compileSdk 34+ and targetSdk 34+
3. Retrofit usage
4. Android Gradle plugin version 8+
5. 'org.jetbrains.kotlin.android' version '1.9.0'+

## Compatibility with DI
1. Koin - 100%
2. Hilt - Compatible when using a small workaround
3. Dagger 2 - not tested
4. Other DIs - there are no plans to check in the near future

## Architecture requirements
Simplifying, your architecture should be something like this.

![simple_arch](https://github.com/Std1o/GodOfAppStates/assets/37378410/94055f61-6e88-495e-be03-00dfa223df7a)

If this is unfamiliar to you, you can google MVVM, Clean Architecture, Repository Pattern

What about (MVI + MVVM) architectural pattern?
It will be hard.
1. Ok, UI state smoothly transforms into ContentState (will talk about this later).
2. And events (aka Intent) you can keep
3. But UI Actions will have to be removed

## Architecture by using library
Recommendation:
1. Take a quick look at what the architecture will turn out to be in the end
2. See what you need to do for this
3. Go back to the diagram and take a closer look at what happened as a result

As a result of using the library, you will get something like this architecture. It may be hard to understand, but it's simple to use. You practically don't have to do anything to get such an architecture.

Don't delve into it yet. Read the usage item, then return to the diagram. After that, you can start reproduce [Usage](#usage)

![god_of_app_states](https://github.com/Std1o/GodOfAppStates/assets/37378410/42c59515-4dc6-4e4f-ab5b-aafbe0d9df02)





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
    implementation 'com.github.Std1o:GodOfAppStates:0.3.4'
    // it must be before Dagger2/Hilt ksp
    ksp 'com.github.Std1o:GodOfAppStates:0.3.4'

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

### LoadableData
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
Usage Documentation in writing process
