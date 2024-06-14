<h1 align="center">Parrot</h1>

<p align="center"> 
  Aplicación que permite guardar notas a través de FireBase. Registro a través de FireBase Auth con email o Google. Personaliza tus notas con el color que prefieras. Archiva, envía a la papelera, restaura notas o elimínalas permanentemente.
</p>
<p align="center">   
  Aplicicación Android basada en la arquitectura MVVM desarrollada con DaggerHilt, StateFlows, ViewModels, Corrutinas, FireBase Cloud, FireBase Auth (Email y Google) y Testing
</p>

## 🛠 Herramientas y librerias
- Basado en lenguaje [Kotlin](https://kotlinlang.org/) con una interfaz en XML / Jetpack Compose
- Arquitectura MVVM (Model-View-ViewModel)
- ViewModel y StateFlow: Nos permite almacenar el estado y realizar cambios de forma reactiva en la interfaz de usuario.
- Lifecycle: Observador de los ciclos de vida de Androrid. Los usamos para recolectar los cambios de estado en el StateFlow para modificar la interfaz del usuario.
- [FireBase](https://github.com/firebase/firebase-android-sdk): Suite de muchas herramientas tales como, notificaciones push, base de datos cloud, informe de errores, analíticas y controles de login.
- [Coroutines](https://github.com/Kotlin/kotlinx.coroutines): Manejo de tareas asíncronas, usado para no bloquear el hilo principal de la aplicación mientras se espera la respuesta de la consulta.
- [Dagger Hilt](https://dagger.dev/hilt/) para inyección de dependencias.
- Navigation component: Es una parte de la suite de Jetpack que simplifica la implementación de la navegación en las Activities y los Fragments.
- Datastore preferences: Nos permite almacenar datos en local de forma asíncrona. Ideal para guardar preferencias de usuario y configuraciones de la aplicación.
- Testing
- Código con Clean Code y Clean Architecture

## 📱 Capturas
| Splash | Login | Register |
|--|--|--|
| <img src="/previews/SplashActivity.webp" width="245" height="500"> | <img src="/previews/LoginActivity.webp" width="245" height="500"> | <img src="/previews/RegisterActivity.webp" width="245" height="500">

| Notes | Detail note | Color dialog |
|--|--|--|
| <img src="/previews/NotesActivity.webp" width="245" height="500"> | <img src="/previews/DetailNoteActivity.webp" width="245" height="500"> | <img src="/previews/ColorDialog.webp" width="245" height="500">

## 👇 Descargar 👇
Ir a [Releases](https://github.com/AudyDevs/Parrot/releases) para descargar el último APK.
