package com.example.parrot.ui.fragments.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parrot.core.DispatcherProvider
import com.example.parrot.domain.state.NotesState
import com.example.parrot.domain.usecase.notes.GetNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val getNotesUseCase: GetNotesUseCase
) : ViewModel() {

    private var _notesState = MutableStateFlow<NotesState>(NotesState.Loading)
    var notesState: StateFlow<NotesState> = _notesState

    fun getNotes() {
        viewModelScope.launch {
            withContext(dispatcherProvider.io) {
                _notesState.value = NotesState.Loading
                getNotesUseCase.invoke().addOnSuccessListener { notesState ->
                    _notesState.value = notesState
                }.addOnFailureListener { exception ->
                    _notesState.value = NotesState.Error(exception.message ?: "")
                }
            }
        }
    }
}