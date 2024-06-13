package com.example.parrot.domain.usecase.notes

import com.example.parrot.domain.FireStoreRepository
import com.example.parrot.motherobject.ParrotMotherObject.anyTaskNotesCompleteState
import com.example.parrot.motherobject.ParrotMotherObject.listNoteId
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class MultiDeleteNoteUseCaseTest {

    @MockK
    private lateinit var fireStoreRepository: FireStoreRepository
    private lateinit var multiDeleteNoteUseCase: MultiDeleteNoteUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        multiDeleteNoteUseCase = MultiDeleteNoteUseCase(fireStoreRepository)
    }

    @Test
    fun `when MultiDeleteNoteUseCase is called successfully, FireStoreRepository should return a correct complete NotesState`() =
        runBlocking {
            //Given
            val listNoteId = listNoteId
            val anyTaskNotesCompleteState = anyTaskNotesCompleteState()
            coEvery { fireStoreRepository.multiDeleteNote(listNoteId) } returns anyTaskNotesCompleteState

            //When
            val taskNotesState = multiDeleteNoteUseCase.invoke(listNoteId)

            //Then
            assert(taskNotesState == anyTaskNotesCompleteState)
        }

    @Test
    fun `when MultiDeleteNoteUseCase is called successfully, FireStoreRepository should call multiDeleteNote once`() =
        runBlocking {
            //Given
            val listNoteId = listNoteId
            val anyTaskNotesCompleteState = anyTaskNotesCompleteState()
            coEvery { fireStoreRepository.multiDeleteNote(listNoteId) } returns anyTaskNotesCompleteState

            //When
            multiDeleteNoteUseCase.invoke(listNoteId)

            //Then
            coVerify(exactly = 1) { fireStoreRepository.multiDeleteNote(listNoteId) }
        }
}