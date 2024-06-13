package com.example.parrot.domain.usecase.notes

import com.example.parrot.domain.FireStoreRepository
import com.example.parrot.motherobject.ParrotMotherObject.ANY_NOTE_ID1
import com.example.parrot.motherobject.ParrotMotherObject.anyTaskNotesCompleteState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DeleteNoteUseCaseTest {

    @MockK
    private lateinit var fireStoreRepository: FireStoreRepository
    private lateinit var deleteNoteUseCase: DeleteNoteUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        deleteNoteUseCase = DeleteNoteUseCase(fireStoreRepository)
    }

    @Test
    fun `when DeleteNoteUseCase is called successfully, FireStoreRepository should return a correct complete NotesState`() =
        runBlocking {
            //Given
            val noteId = ANY_NOTE_ID1
            val anyTaskNotesCompleteState = anyTaskNotesCompleteState()
            coEvery { fireStoreRepository.deleteNote(noteId) } returns anyTaskNotesCompleteState

            //When
            val taskNotesState = deleteNoteUseCase.invoke(noteId)

            //Then
            assert(taskNotesState == anyTaskNotesCompleteState)
        }

    @Test
    fun `when DeleteNoteUseCase is called successfully, FireStoreRepository should call deleteNote once`() =
        runBlocking {
            //Given
            val noteId = ANY_NOTE_ID1
            val anyTaskNotesCompleteState = anyTaskNotesCompleteState()
            coEvery { fireStoreRepository.deleteNote(noteId) } returns anyTaskNotesCompleteState

            //When
            deleteNoteUseCase.invoke(noteId)

            //Then
            coVerify(exactly = 1) { fireStoreRepository.deleteNote(noteId) }
        }
}