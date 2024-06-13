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

class SaveNoteUseCaseTest {

    @MockK
    private lateinit var fireStoreRepository: FireStoreRepository
    private lateinit var saveNoteUseCase: SaveNoteUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        saveNoteUseCase = SaveNoteUseCase(fireStoreRepository)
    }

    @Test
    fun `when SaveNoteUseCase is called successfully, FireStoreRepository should return a correct complete NotesState`() =
        runBlocking {
            //Given
            val notesModel = anyNotesModel
            val anyTaskNotesCompleteState = anyTaskNotesCompleteState()
            coEvery { fireStoreRepository.saveNote(notesModel) } returns anyTaskNotesCompleteState

            //When
            val taskNotesState = saveNoteUseCase.invoke(notesModel)

            //Then
            assert(taskNotesState == anyTaskNotesCompleteState)
        }

    @Test
    fun `when SaveNoteUseCase is called successfully, FireStoreRepository should call saveNote once`() =
        runBlocking {
            //Given
            val notesModel = anyNotesModel
            val anyTaskNotesCompleteState = anyTaskNotesCompleteState()
            coEvery { fireStoreRepository.saveNote(notesModel) } returns anyTaskNotesCompleteState

            //When
            saveNoteUseCase.invoke(notesModel)

            //Then
            coVerify(exactly = 1) { fireStoreRepository.saveNote(notesModel) }
        }
}