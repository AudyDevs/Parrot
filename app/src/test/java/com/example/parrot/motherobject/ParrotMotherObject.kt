package com.example.parrot.motherobject

import com.example.parrot.core.type.ProviderType
import com.example.parrot.domain.model.LoginModel
import com.example.parrot.domain.model.NotesModel
import com.example.parrot.domain.state.LoginState
import com.example.parrot.domain.state.NotesState
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource

object ParrotMotherObject {

    val anyLoginModel = LoginModel(email = "test@test.es", provider = ProviderType.Email)
    const val ANY_EMAIL = "test@test.es"
    const val ANY_PASSWORD = "Test123"

    private const val ANY_USER_ID = "2dpLxC7fEkQj3wDntu2kgTeubTH2"
    const val ANY_NOTE_ID1 = "5GPqXHGYVrz7S4LzrIhJ"
    private const val ANY_NOTE_ID2 = "3YIjOUvxsvD7zzjH0aAy"

    val listNoteId: MutableList<String> = mutableListOf(ANY_NOTE_ID1, ANY_NOTE_ID2)
    val mapUpdate: Map<String, Boolean> = mapOf("isArchived" to false)
    val anyProviderType = ProviderType.Email
    val anyLoginStateSuccess = LoginState.Success

    private val anyListNotesModel: List<NotesModel> = listOf(
        NotesModel(
            id = ANY_NOTE_ID1,
            userId = ANY_USER_ID,
            title = "noteTitleTest1",
            body = "noteBodyTest1",
            isArchived = false,
            isDeleted = false,
            backcolor = -1
        ), NotesModel(
            id = ANY_NOTE_ID2,
            userId = ANY_USER_ID,
            title = "noteTitleTest2",
            body = "noteBodyTest2",
            isArchived = false,
            isDeleted = false,
            backcolor = -1
        )
    )
    val anyNotesModel: NotesModel =
        NotesModel(
            id = ANY_NOTE_ID1,
            userId = ANY_USER_ID,
            title = "noteTitleTest1",
            body = "noteBodyTest1",
            isArchived = false,
            isDeleted = false,
            backcolor = -1
        )

    private fun anyNoteStateComplete() = NotesState.Complete
    private fun anyNoteStateSuccess() = NotesState.Success(anyListNotesModel)

    fun anyTaskNotesSuccessState(): Task<NotesState> {
        val taskCompletionSource = TaskCompletionSource<NotesState>()
        val notesState = anyNoteStateSuccess()
        taskCompletionSource.setResult(notesState)
        return taskCompletionSource.task
    }

    fun anyTaskNotesCompleteState(): Task<NotesState> {
        val taskCompletionSource = TaskCompletionSource<NotesState>()
        val notesState = anyNoteStateComplete()
        taskCompletionSource.setResult(notesState)
        return taskCompletionSource.task
    }
}