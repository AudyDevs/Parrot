package com.example.parrot.domain.usecase.notes

import com.example.parrot.domain.FireStoreRepository
import com.example.parrot.motherobject.ParrotMotherObject.anyTaskNotesSuccessState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetNotesUseCaseTest {

    @MockK
    private lateinit var fireStoreRepository: FireStoreRepository
    private lateinit var getNotesUseCase: GetNotesUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        getNotesUseCase = GetNotesUseCase(fireStoreRepository)
    }

    @Test
    fun `when GetNotesUseCase is called successfully, FireStoreRepository should return a correct success NotesState`() =
        runBlocking {
            //Given
            val anyTaskNotesSuccessState = anyTaskNotesSuccessState()
            coEvery { fireStoreRepository.getNotes() } returns anyTaskNotesSuccessState

            //When
            val taskNotesState = getNotesUseCase.invoke()

            //Then
            assert(taskNotesState == anyTaskNotesSuccessState)
        }

    @Test
    fun `when GetNotesUseCase is called successfully, FireStoreRepository should call getNotes once`() =
        runBlocking {
            //Given
            val anyTaskNotesSuccessState = anyTaskNotesSuccessState()
            coEvery { fireStoreRepository.getNotes() } returns anyTaskNotesSuccessState

            //When
            getNotesUseCase.invoke()

            //Then
            coVerify(exactly = 1) { fireStoreRepository.getNotes() }
        }
}