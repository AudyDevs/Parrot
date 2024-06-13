package com.example.parrot.domain.usecase.notes

import com.example.parrot.domain.FireStoreRepository
import com.example.parrot.motherobject.ParrotMotherObject.anyNotesModel
import com.example.parrot.motherobject.ParrotMotherObject.anyTaskNotesCompleteState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class UpdateNoteUseCaseTest {

    @MockK
    private lateinit var fireStoreRepository: FireStoreRepository
    private lateinit var updateNoteUseCase: UpdateNoteUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        updateNoteUseCase = UpdateNoteUseCase(fireStoreRepository)
    }

    @Test
    fun `when UpdateNoteUseCase is called successfully, FireStoreRepository should return a correct complete NotesState`() =
        runBlocking {
            //Given
            val notesModel = anyNotesModel
            val anyTaskNotesCompleteState = anyTaskNotesCompleteState()
            coEvery { fireStoreRepository.updateNote(notesModel) } returns anyTaskNotesCompleteState

            //When
            val taskNotesState = updateNoteUseCase.invoke(notesModel)

            //Then
            assert(taskNotesState == anyTaskNotesCompleteState)
        }

    @Test
    fun `when UpdateNoteUseCase is called successfully, FireStoreRepository should call updateNote once`() =
        runBlocking {
            //Given
            val notesModel = anyNotesModel
            val anyTaskNotesCompleteState = anyTaskNotesCompleteState()
            coEvery { fireStoreRepository.updateNote(notesModel) } returns anyTaskNotesCompleteState

            //When
            updateNoteUseCase.invoke(notesModel)

            //Then
            coVerify(exactly = 1) { fireStoreRepository.updateNote(notesModel) }
        }
}