package com.example.parrot.ui.activities.login.viewmodel

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parrot.core.DispatcherProvider
import com.example.parrot.core.type.ProviderType
import com.example.parrot.domain.state.LoginState
import com.example.parrot.domain.state.SplashState
import com.example.parrot.domain.usecase.datastore.GetUserDataStoreUseCase
import com.example.parrot.domain.usecase.datastore.SaveUserDataStoreUseCase
import com.example.parrot.domain.usecase.google.LoginWithGoogleUseCase
import com.example.parrot.domain.usecase.google.SignUpWithGoogleUseCase
import com.example.parrot.domain.usecase.mail.LoginWithEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val loginWithEmailUseCase: LoginWithEmailUseCase,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    private val signUpWithGoogleUseCase: SignUpWithGoogleUseCase,
    private val getUserDataStoreUseCase: GetUserDataStoreUseCase,
    private val saveUserDataStoreUseCase: SaveUserDataStoreUseCase
) : ViewModel() {

    private var _splashState = MutableStateFlow<SplashState>(SplashState.Loading)
    var splashState: StateFlow<SplashState> = _splashState

    private var _loginState = MutableStateFlow<LoginState?>(null)
    var loginState: StateFlow<LoginState?> = _loginState

    var showSplash: Boolean = true

    var email: String = ""
    var password: String = ""
    var provider: ProviderType = ProviderType.Email

    init {
        viewModelScope.launch {
            withContext(dispatcherProvider.io) {
                val loginModel = getUserDataStoreUseCase.invoke()
                if (loginModel.email.isNotEmpty() && loginModel.provider.type.isNotEmpty()) {
                    email = loginModel.email
                    provider = loginModel.provider
                    _splashState.value = SplashState.RegisteredUser
                } else {
                    _splashState.value = SplashState.NewUser
                }

            }
        }
    }

    private fun saveUserToDataStore() {
        viewModelScope.launch {
            withContext(dispatcherProvider.io) {
                saveUserDataStoreUseCase.invoke(email, provider)
            }
        }
    }

    fun resetLoginState() {
        _loginState.value = null
    }

    fun loginUserWithEmail() {
        viewModelScope.launch {
            withContext(dispatcherProvider.io) {
                _loginState.value = LoginState.Loading
                _loginState.value = loginWithEmailUseCase.invoke(email, password)
                if (_loginState.value == LoginState.Success) {
                    saveUserToDataStore()
                }
            }
        }
    }

    fun loginUserWithGoogle(activity: Activity) {
        viewModelScope.launch {
            withContext(dispatcherProvider.io) {
                loginWithGoogleUseCase.invoke(activity)
            }
        }
    }

    fun signUpWithGoogle(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModelScope.launch {
            withContext(dispatcherProvider.io) {
                _loginState.value = LoginState.Loading
                val loginModel = signUpWithGoogleUseCase.invoke(requestCode, resultCode, data)
                if (loginModel.email.isNotEmpty()) {
                    email = loginModel.email
                    provider = loginModel.provider
                    _loginState.value = LoginState.Success
                    saveUserToDataStore()
                } else {
                    _loginState.value = LoginState.Error
                }
            }
        }
    }
}