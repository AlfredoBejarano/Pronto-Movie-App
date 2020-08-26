# MovieList App
<img width="160" height="160" alt="app_icon" src="./app/src/main/ic_launcher-playstore.png">

Demo app that uses [TheMovideDB V3 API](https://developers.themoviedb.org/3/getting-started/introduction) to fetch a list of movies and allow the user to mark some of those movies as their favorites and see them in a second list.

- This app follows the recommended modern Android development guidelines and good practices described by Google in Android Jetpack. More info about this architecture can be found within the [Android Jetpack documentation](https://developer.android.com/jetpack/docs/guide).

- This app harness the power of the [Android Architecture components](https://developer.android.com/topic/libraries/architecture) to create a lean, fast and reliable app.

- This app offers a clean, layered architecture while keeping it simple, the `model` classes are transparent across the architecture, the `repository` classes provide a clean API and a single source of truth from all the app `data sources` for the rest of the layers in the app. The `ViewModel` classes serves as intermediaries between the `view` (UI Controllers) and the `repository` layers, provides clean and immutable observation via `LiveData` and reacts accordingly to the app's lifecycle.

- It also uses the good practices accepted from Google, such as _Dependency Injection_ using **Dagger** and using the _Observation_ pattern. The app architecture is **M**odel**V**iew**V**iew**M**odel (_MVVM from now on_).

- This project is written 100% natively using **Kotlin**, Kotlin is a concise and safe language interoperable with Java. This makes Kotlin a powerful tool for android development. It is also the oficial language for Android.

# How the app is built?

As said before, **MovieList app** is based upon the Sunflower MVVM architecture with an added layer for business logic, each layer being separated into a module, it looks as follows:

![app-arch](https://i.ibb.co/Y0pLk65/Arquitectura-MVVM.jpg)

All the layers dependencies are satisfied using *Dependency Injection* (_DI from now on_) using **Dagger**.

The idea of separate layers and module is to maintain the **single responsibility principle**.

# Modules
- App
  - Navigation Component
  - Safe Args
  - Activity
  - Fragments
  - ViewModel
- Domain
  - Use Cases
- Repository
  - Repositories
- Remote
  - Retrofit
  - GSON
- Local
  - Room
- Core
  - model

# Layers

This section will explain the idea behind each layer, represented by a package:

## Datasource
The datasource package would fall within the **model layer** following the diagram above this file. This package contains the classes that return the model classes,
this classes are (_as their name says_) the source of the data, but alone this classes don't do anything more. Files such as Room DAO interfaces or the Retrofit
Api Service interface are here.

## Domain
The domain package contains the UseCase classes. Use case classes are mean to store the business logic of the app preventing ViewModels to become
too bloated and become untestable. Use case classes are bit sized pieces of code that can be re-used within a los of ViewModel classes, preventing
boilerplate code, An Use case class can use other use case classes to do the business logic.

## Injection
The injection package isn't meant to be a layer at all, it contains all the classes that help Dagger build the app dependency graph.

## Model
The model package contains all the model DTO classes that defines data for the app. Models are divided in two:
  - remote models: DTO definitions of a JSON schemes from a remote data source (like Retrofit or Firebase)
  - local models: DTO classes that can be stored in Room or other local source such as SharedPreferences, this model classes follow what the UI needs.

This layer also contains mapper classes, following the [Guide to app architecture guidelines](https://developer.android.com/jetpack/guide?gclid=Cj0KCQjwp4j6BRCRARIsAGq4yMHFSUkhs8FHq_HKPk1lb96Sx4qM_jmx5R-41izSaEvFXUHnAG7-ejgaArT5EALw_wcB&gclsrc=aw.ds), model classes
should follow the UI needs, remote models usually don't follow the UI, they follow what makes sense for the source they belong to instead. The mapper classes helps
transforming a remote model into a local model.

## Repository
The repository package defines the needed repository classes for the app. Repository classes are the single source of truth for the app, this classes uses one or more datasource classes
and depending on certain conditions and scenarios (such as caching or internet availability), this classes decide which source is the better suited for the app needs.

## Utils
Like the injection package, this package isn't meant to be a layer within the architecture. This package contains some extension functions.

## View
This package contains the UI classes for the app, this layer responsibility is the to receive user interactions. This layer should handle
data operations, it has just to know how to draw said data.

## ViewModel
ViewModel classes receive the UI interactions intercepted by the view layer and retrieve computed data from use case classes.
ViewModel classes don't compute data, they just receive it and send it to the view layer.

# The single responsibility principle

Layers, modules, separation... to what end?. To achieve the single responsibility principle.

The single responsibility principle describes that each class should do and just do one thing. It means, the UI classes should just display stuff and interact with the user, the repository classes should manage all the various data sources available to provide a clean API to upper layers, the domain layer should manage said data and just compute stuff with it, the data sources should just retrieve data and thats it. Is basically prevent classes from having too much stuff and let them do just one thing. This helps the codebase being understandable, scalable and maintainable.

# So, what clean code should be?

In my opinion, a clean codebase or "clean code" should be code that is easily understandable, that goes "to the point" and is easily maintainable, it also should help the development of future features to be more productive. This means variables should have a meaningful name and functions should be small and compact, usually doing just a single action or two (how many lines your function or class have is a good indicative of this).

So no. multiple model classes or single implementation interfaces are not a good indicative of clean code, at least in my opinion. This is because if you have to write 16 or more classes or 4 different model classes or an activity with a single fragment to implement a feature that is not productive or fast, is just a chore. More code, means more things to maintain.

This is, for me at least, what a clean code should look like.

# Running the project

In order to run the project, you are going to need a **gradle.properties** file. This file is not within this repository for security reasons (as much security a demo project needs).

    # Project-wide Gradle settings.
    # IDE (e.g. Android Studio) users:
    # Gradle settings configured through the IDE *will override*
    # any settings specified in this file.
    # For more details on how to configure your build environment visit
    # http://www.gradle.org/docs/current/userguide/build_environment.html
    # Specifies the JVM arguments used for the daemon process.
    # The setting is particularly useful for tweaking memory settings.
    org.gradle.jvmargs=-Xmx4096m
    # When configured, Gradle will run in incubating parallel mode.
    # This option should only be used with decoupled projects. More details, visit
    # http://www.gradle.org/docs/current/userguide/multi_project_builds.html#sec:decoupled_projects
    # org.gradle.parallel=true
    # AndroidX package structure to make it clearer which packages are bundled with the
    # Android operating system, and which are packaged with your app's APK
    # https://developer.android.com/topic/libraries/support-library/androidx-rn
    android.useAndroidX=true
    # Automatically convert third-party libraries to use AndroidX
    android.enableJetifier=true
    # Kotlin code style for this project: "official" or "obsolete":
    kotlin.code.style=official
    # TheMovieDB API key
    theMovieDBApiKey="841fe808855945f7b06ccb3d5a20e7ea"
    # TheMovieDB Base URL
    theMovieDBBaseURL="https://api.themoviedb.org/3/"
    # TheMovieDB Images base URLs
    theMovieDBImageBaseURl="https://image.tmdb.org/t/p/w200"
    # TheMovieDB page size
    theMovieDbPageSize=20