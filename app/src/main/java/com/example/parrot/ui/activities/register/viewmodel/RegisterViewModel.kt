package com.example.parrot.ui.activities.register.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parrot.core.DispatcherProvider
import com.example.parrot.domain.state.LoginState
import com.example.parrot.domain.usecase.mail.SignUpWithEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val signUpWithEmailUseCase: SignUpWithEmailUseCase
) : ViewModel() {

    private var _loginState = MutableStateFlow<LoginState?>(null)
    var loginState: StateFlow<LoginState?> = _loginState

    var email: String = ""
    var password: String = ""

    fun resetLoginState() {
        _loginState.value = null
    }

    fun signUpUser() {
        viewModelScope.launch {
            withContext(dispatcherProvider.io) {
                _loginState.value = LoginState.Loading
                _loginState.value = signUpWithEmailUseCase.invoke(email, password)
            }
        }
    }
}