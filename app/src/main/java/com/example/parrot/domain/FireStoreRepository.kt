package com.example.parrot.domain

import com.example.parrot.domain.model.NotesModel
import com.example.parrot.domain.state.NotesState
import com.google.android.gms.tasks.Task

interface FireStoreRepository {

    suspend fun getNotes(): Task<NotesState>

    suspend fun getNoteId(noteId: String): Task<NotesState>

    suspend fun saveNote(notesResponse: NotesModel): Task<NotesState>

    suspend fun updateNote(notesResponse: NotesModel): Task<NotesState>

    suspend fun deleteNote(noteId: String): Task<NotesState>

    suspend fun multiUpdateNote(
        listNoteId: MutableList<String>,
        mapUpdate: Map<String, Boolean>
    ): Task<NotesState>

    suspend fun multiDeleteNote(listNoteId: MutableList<String>): Task<NotesState>
}