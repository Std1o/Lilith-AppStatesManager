# God Of App States
1. [About library](#about-library)
2. [Requirements](#requirements)
3. [Architecture requirements](#architecture-requirements)
4. [Setup](#setup)
5. [Usage](#usage)

## About library
The main idea of the library is easy work with states, automation of routine and all this without loss testability, flexibility and without increasing cohesion in the code

## Requirements
1. Kotlin
2. compileSdk 34+ and targetSdk 34+
3. Retrofit usage
4. Android Gradle plugin version 8+
5. 'org.jetbrains.kotlin.android' version '1.9.0'+

## Architecture requirements
Simplifying, your architecture should be something like this.
![simple_arch](https://github.com/Std1o/GodOfAppStates/assets/37378410/b592bb4c-6646-48a4-b445-b70858590dbf)
If this is unfamiliar to you, you can google MVVM, Clean Architecture, Repository Pattern

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
