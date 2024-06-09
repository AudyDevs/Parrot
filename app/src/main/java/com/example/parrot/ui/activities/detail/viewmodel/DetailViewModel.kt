package com.example.parrot.ui.activities.detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parrot.core.DispatcherProvider
import com.example.parrot.domain.model.NotesModel
import com.example.parrot.domain.state.NotesState
import com.example.parrot.domain.usecase.notes.DeleteNoteUseCase
import com.example.parrot.domain.usecase.notes.GetNoteIdUseCase
import com.example.parrot.domain.usecase.notes.SaveNoteUseCase
import com.example.parrot.domain.usecase.notes.UpdateNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val getNoteIdUseCase: GetNoteIdUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {

    private var _notesState = MutableStateFlow<NotesState>(NotesState.Loading)
    var notesState: StateFlow<NotesState> = _notesState

    var noteId: String? = null

    fun getNoteId() {
        viewModelScope.launch {
            withContext(dispatcherProvider.io) {
                _notesState.value = NotesState.Loading
                if (!noteId.isNullOrEmpty()) {
                    getNoteIdUseCase.invoke(noteId!!).addOnSuccessListener { notesState ->
                        _notesState.value = notesState
                    }.addOnFailureListener { exception ->
                        _notesState.value = NotesState.Error(exception.message ?: "")
                    }
                }
            }
        }
    }

    fun saveNote(notesModel: NotesModel) {
        viewModelScope.launch {
            withContext(dispatcherProvider.io) {
                saveNoteUseCase.invoke(notesModel).addOnSuccessListener { notesState ->
                    _notesState.value = notesState
                }.addOnFailureListener { exception ->
                    _notesState.value = NotesState.Error(exception.message ?: "")
                }
            }
        }
    }

    fun updateNote(notesModel: NotesModel) {
        viewModelScope.launch {
            withContext(dispatcherProvider.io) {
                updateNoteUseCase.invoke(notesModel).addOnSuccessListener { notesState ->
                    _notesState.value = notesState
                }.addOnFailureListener { exception ->
                    _notesState.value = NotesState.Error(exception.message ?: "")
                }
            }
        }
    }

    fun deleteNote() {
        viewModelScope.launch {
            withContext(dispatcherProvider.io) {
                if (!noteId.isNullOrEmpty()) {
                    deleteNoteUseCase.invoke(noteId!!).addOnSuccessListener { notesState ->
                        _notesState.value = notesState
                    }.addOnFailureListener { exception ->
                        _notesState.value = NotesState.Error(exception.message ?: "")
                    }
                }
            }
        }
    }
}