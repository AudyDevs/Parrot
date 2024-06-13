package com.example.parrot.domain.usecase.notes

import com.example.parrot.domain.FireStoreRepository
import com.example.parrot.motherobject.ParrotMotherObject.anyTaskNotesCompleteState
import com.example.parrot.motherobject.ParrotMotherObject.listNoteId
import com.example.parrot.motherobject.ParrotMotherObject.mapUpdate
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class MultiUpdateNoteUseCaseTest {

    @MockK
    private lateinit var fireStoreRepository: FireStoreRepository
    private lateinit var multiUpdateNoteUseCase: MultiUpdateNoteUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        multiUpdateNoteUseCase = MultiUpdateNoteUseCase(fireStoreRepository)
    }

    @Test
    fun `when MultiUpdateNoteUseCase is called successfully, FireStoreRepository should return a correct complete NotesState`() =
        runBlocking {
            //Given
            val listNoteId = listNoteId
            val mapUpdate = mapUpdate
            val anyTaskNotesCompleteState = anyTaskNotesCompleteState()
            coEvery {
                fireStoreRepository.multiUpdateNote(
                    listNoteId,
                    mapUpdate
                )
            } returns anyTaskNotesCompleteState

            //When
            val taskNotesState = multiUpdateNoteUseCase.invoke(listNoteId, mapUpdate)

            //Then
            assert(taskNotesState == anyTaskNotesCompleteState)
        }

    @Test
    fun `when MultiUpdateNoteUseCase is called successfully, FireStoreRepository should call multiUpdateNote once`() =
        runBlocking {
            //Given
            val listNoteId = listNoteId
            val mapUpdate = mapUpdate
            val anyTaskNotesCompleteState = anyTaskNotesCompleteState()
            coEvery {
                fireStoreRepository.multiUpdateNote(
                    listNoteId,
                    mapUpdate
                )
            } returns anyTaskNotesCompleteState

            //When
            multiUpdateNoteUseCase.invoke(listNoteId, mapUpdate)

            //Then
            coVerify(exactly = 1) { fireStoreRepository.multiUpdateNote(listNoteId, mapUpdate) }
        }
}