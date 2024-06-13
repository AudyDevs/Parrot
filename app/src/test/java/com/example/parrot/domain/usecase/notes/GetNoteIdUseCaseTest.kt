package com.example.parrot.domain.usecase.notes

import com.example.parrot.domain.FireStoreRepository
import com.example.parrot.motherobject.ParrotMotherObject.ANY_NOTE_ID1
import com.example.parrot.motherobject.ParrotMotherObject.anyTaskNotesSuccessState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetNoteIdUseCaseTest {

    @MockK
    private lateinit var fireStoreRepository: FireStoreRepository
    private lateinit var getNoteIdUseCase: GetNoteIdUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        getNoteIdUseCase = GetNoteIdUseCase(fireStoreRepository)
    }

    @Test
    fun `when GetNoteIdUseCase is called successfully, FireStoreRepository should return a correct success NotesState`() =
        runBlocking {
            //Given
            val noteId = ANY_NOTE_ID1
            val anyTaskNotesSuccessState = anyTaskNotesSuccessState()
            coEvery { fireStoreRepository.getNoteId(noteId) } returns anyTaskNotesSuccessState

            //When
            val taskNotesState = getNoteIdUseCase.invoke(noteId)

            //Then
            assert(taskNotesState == anyTaskNotesSuccessState)
        }

    @Test
    fun `when GetNoteIdUseCase is called successfully, FireStoreRepository should call getNoteId once`() =
        runBlocking {
            //Given
            val noteId = ANY_NOTE_ID1
            val anyTaskNotesSuccessState = anyTaskNotesSuccessState()
            coEvery { fireStoreRepository.getNoteId(noteId) } returns anyTaskNotesSuccessState

            //When
            getNoteIdUseCase.invoke(noteId)

            //Then
            coVerify(exactly = 1) { fireStoreRepository.getNoteId(noteId) }
        }
}