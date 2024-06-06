package com.example.parrot.domain.state

sealed class SplashState {
    data object Loading : SplashState()
    data object NewUser : SplashState()
    data object RegisteredUser : SplashState()
}