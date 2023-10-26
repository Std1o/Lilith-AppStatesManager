# God Of App States
1. [About library](#about-library)
2. [Requirements](#requirements)
3. [Compatibility with DI](#compatibility-with-di)
4. [Architecture requirements](#architecture-requirements)
5. [Architecture by using library](#architecture-by-using-library)
6. [Setup](#setup)
7. [Usage](#usage)

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
Usage Documentation in writing process
