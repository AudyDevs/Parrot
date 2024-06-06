package com.example.parrot.domain.state

sealed class LoginState {
    data object Loading : LoginState()
    data object Success : LoginState()
    data object Error : LoginState()
}