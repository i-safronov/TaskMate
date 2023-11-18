<h1 align="center">TaskMate</h1>

<p align="center">  
✏ TaskMate demonstrates modern Android development with Dagger, Coroutines, Flow, Jetpack (Room, ViewModel), and xml based on MVVM architecture.
</p>

<p align="center">
<img src="preview/preview_img.png"/>
</p>

## Download
The application is under development, will soon be available on [Play Market](https://play.google.com/store/apps?hl=en&gl=US), please wait

## Tech stack & Libraries
- Minimum SDK level 23
- [Kotlin](https://kotlinlang.org/) based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
- ViewModel: Manages UI-related data holder and lifecycle aware. Allows data to survive configuration changes such as screen rotations.
- ViewBinding: Binds UI components in your layouts to data sources in your app.
- Room: Constructs Database by providing an abstraction layer over SQLite to allow fluent database access.
- [Dagger](https://github.com/google/dagger): for dependency injection.
- GJson converter to convert domain object.
- Architecture
  - MVVM Architecture (View - DataBinding - ViewModel - Model)
  - Clean Architecture
  - TDD (Test-driven development) Pattern
