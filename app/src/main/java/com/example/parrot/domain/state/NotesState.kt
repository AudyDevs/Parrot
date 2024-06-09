package com.example.parrot.domain.state

import com.example.parrot.domain.model.NotesModel

sealed class NotesState {
    data object Loading : NotesState()
    data class Success(val notes: List<NotesModel>) : NotesState()
    data class Error(val error: String) : NotesState()
    data object Complete : NotesState()
}