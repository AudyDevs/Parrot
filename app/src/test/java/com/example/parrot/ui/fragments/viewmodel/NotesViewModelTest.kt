package com.example.parrot.ui.fragments.viewmodel

import com.example.parrot.dispatcher.DispatcherRule
import com.example.parrot.dispatcher.TestDispatchers
import com.example.parrot.domain.usecase.notes.GetNotesUseCase
import com.example.parrot.motherobject.ParrotMotherObject.anyTaskNotesSuccessState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NotesViewModelTest {

    @get:Rule
    val dispatcherRule = DispatcherRule()

    @MockK
    private lateinit var getNotesUseCase: GetNotesUseCase

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var testDispatchers: TestDispatchers

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        testDispatchers = TestDispatchers()
        notesViewModel = NotesViewModel(
            testDispatchers,
            getNotesUseCase
        )
    }

    @Test
    fun `when NotesViewModel calls getNotes successfully, it should call getNotesUseCase once`() =
        runBlocking {
            //Given
            val anyTaskNotesSuccessState = anyTaskNotesSuccessState()
            coEvery { getNotesUseCase.invoke() } returns anyTaskNotesSuccessState

            //When
            notesViewModel.getNotes()
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()

            //Then
            coVerify(exactly = 1) { getNotesUseCase.invoke() }
        }
}