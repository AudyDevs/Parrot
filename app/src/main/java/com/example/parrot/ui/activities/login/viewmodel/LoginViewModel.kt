package com.example.parrot.ui.activities.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private var _isReady = MutableStateFlow(false)
    var isReady: StateFlow<Boolean> = _isReady

    init {
        viewModelScope.launch {
            delay(1000L)
            _isReady.value = true
        }
    }
}