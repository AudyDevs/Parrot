<h1 align="center">Parrot</h1>

<p align="center"> 
  Aplicación que permite chatear con diferentes usuarios registrados a través de FireBase. Chats grupales, notificaciones para mensajes nuevos, subida de documentos al servidor para que otros usuarios pueda descargarla, modificación del perfil de usuario. Chat privado para poder probar la aplicación sin ningun usuario.
</p>
<p align="center">   
  Aplicicación Android basada en la arquitectura MVVM desarrollada con DaggerHilt, StateFlows, ViewModels, Corrutinas, FireBase, Notificaciones cloud y Testing
</p>

## 🛠 Herramientas y librerias
- Basado en lenguaje [Kotlin](https://kotlinlang.org/) con una interfaz en XML / Jetpack Compose
- Arquitectura MVVM (Model-View-ViewModel)
- ViewModel y StateFlow: Nos permite almacenar el estado y realizar cambios de forma reactiva en la interfaz de usuario.
- Lifecycle: Observador de los ciclos de vida de Androrid. Los usamos para recolectar los cambios de estado en el StateFlow para modificar la interfaz del usuario.
- [FireBase](https://github.com/firebase/firebase-android-sdk): Suite de muchas herramientas tales como, notificaciones push, base de datos cloud, informe de errores, analíticas y controles de login.
- [Coroutines](https://github.com/Kotlin/kotlinx.coroutines): Manejo de tareas asíncronas, usado para no bloquear el hilo principal de la aplicación mientras se espera la respuesta de la consulta.
- [Dagger Hilt](https://dagger.dev/hilt/) para inyección de dependencias.
- Testing
- Código con Clean Code y Clean Architecture

## 📱 Capturas
| Splash | Login | Chats |
|--|--|--|
| <img src="" width="245" height="500"> | <img src="" width="245" height="500"> | <img src="" width="245" height="500">

| Groups | Profile | Messages |
|--|--|--|
| <img src="" width="245" height="500"> | <img src="" width="245" height="500"> | <img src="" width="245" height="500">

## 👇 Descargar 👇
Ir a [Releases](https://github.com/AudyDevs/Parrot/releases) para descargar el último APK.
