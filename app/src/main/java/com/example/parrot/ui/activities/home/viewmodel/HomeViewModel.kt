package com.example.parrot.ui.activities.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parrot.core.DispatcherProvider
import com.example.parrot.domain.state.LoginState
import com.example.parrot.domain.state.NotesState
import com.example.parrot.domain.usecase.datastore.ClearUserDataStoreUseCase
import com.example.parrot.domain.usecase.mail.LogOutWithEmailUseCase
import com.example.parrot.domain.usecase.notes.MultiDeleteNoteUseCase
import com.example.parrot.domain.usecase.notes.MultiUpdateNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val logOutWithEmailUseCase: LogOutWithEmailUseCase,
    private val clearUserDataStoreUseCase: ClearUserDataStoreUseCase,
    private val multiUpdateNoteUseCase: MultiUpdateNoteUseCase,
    private val multiDeleteNoteUseCase: MultiDeleteNoteUseCase
) : ViewModel() {

    private var _loginState = MutableStateFlow<LoginState?>(null)
    var loginState: StateFlow<LoginState?> = _loginState

    private var _notesState = MutableStateFlow<NotesState>(NotesState.Loading)
    var notesState: StateFlow<NotesState> = _notesState

    var listNotesId: MutableList<String>? = mutableListOf()

    fun resetLoginState() {
        _loginState.value = null
    }

    fun logOutUser() {
        viewModelScope.launch {
            withContext(dispatcherProvider.io) {
                _loginState.value = LoginState.Loading
                _loginState.value = logOutWithEmailUseCase.invoke()
                if (_loginState.value == LoginState.Success) {
                    clearUserToDataStore()
                }
            }
        }
    }

    private fun clearUserToDataStore() {
        viewModelScope.launch {
            withContext(dispatcherProvider.io) {
                clearUserDataStoreUseCase.invoke()
            }
        }
    }

    fun multiUpdateNote(mapUpdate: Map<String, Boolean>) {
        viewModelScope.launch {
            withContext(dispatcherProvider.io) {
                if (!listNotesId.isNullOrEmpty()) {
                    _notesState.value = NotesState.Loading
                    multiUpdateNoteUseCase.invoke(listNotesId!!, mapUpdate)
                        .addOnSuccessListener { notesState ->
                            _notesState.value = notesState
                        }.addOnFailureListener { exception ->
                            _notesState.value = NotesState.Error(exception.message ?: "")
                        }
                }
            }
        }
    }

    fun multiDeleteNote() {
        viewModelScope.launch {
            withContext(dispatcherProvider.io) {
                if (!listNotesId.isNullOrEmpty()) {
                    _notesState.value = NotesState.Loading
                    multiDeleteNoteUseCase.invoke(listNotesId!!)
                        .addOnSuccessListener { notesState ->
                            _notesState.value = notesState
                        }.addOnFailureListener { exception ->
                        _notesState.value = NotesState.Error(exception.message ?: "")
                    }
                }
            }
        }
    }
}