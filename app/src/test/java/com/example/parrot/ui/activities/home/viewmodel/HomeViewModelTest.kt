package com.example.parrot.ui.activities.home.viewmodel

import com.example.parrot.dispatcher.DispatcherRule
import com.example.parrot.dispatcher.TestDispatchers
import com.example.parrot.domain.usecase.datastore.ClearUserDataStoreUseCase
import com.example.parrot.domain.usecase.mail.LogOutWithEmailUseCase
import com.example.parrot.domain.usecase.notes.MultiDeleteNoteUseCase
import com.example.parrot.domain.usecase.notes.MultiUpdateNoteUseCase
import com.example.parrot.motherobject.ParrotMotherObject.anyLoginStateSuccess
import com.example.parrot.motherobject.ParrotMotherObject.anyTaskNotesCompleteState
import com.example.parrot.motherobject.ParrotMotherObject.listNoteId
import com.example.parrot.motherobject.ParrotMotherObject.mapUpdate
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @get:Rule
    val dispatcherRule = DispatcherRule()

    @MockK
    private lateinit var logOutWithEmailUseCase: LogOutWithEmailUseCase

    @MockK
    private lateinit var clearUserDataStoreUseCase: ClearUserDataStoreUseCase

    @MockK
    private lateinit var multiUpdateNoteUseCase: MultiUpdateNoteUseCase

    @MockK
    private lateinit var multiDeleteNoteUseCase: MultiDeleteNoteUseCase

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var testDispatchers: TestDispatchers

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        testDispatchers = TestDispatchers()
        homeViewModel = HomeViewModel(
            testDispatchers,
            logOutWithEmailUseCase,
            clearUserDataStoreUseCase,
            multiUpdateNoteUseCase,
            multiDeleteNoteUseCase
        )
    }

    @Test
    fun `when NotesViewModel calls getNotes successfully, it should return a success LoginState`() =
        runBlocking {
            //Given
            coEvery { logOutWithEmailUseCase.invoke() } returns anyLoginStateSuccess

            //When
            homeViewModel.logOutUser()
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()
            val loginState = homeViewModel.loginState.value

            //Then
            assert(loginState == anyLoginStateSuccess)
        }

    @Test
    fun `when NotesViewModel calls getNotes successfully, it should call getNotesUseCase once`() =
        runBlocking {
            //Given
            coEvery { logOutWithEmailUseCase.invoke() } returns anyLoginStateSuccess

            //When
            homeViewModel.logOutUser()
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()

            //Then
            coVerify(exactly = 1) { logOutWithEmailUseCase.invoke() }
        }

    @Test
    fun `when NotesViewModel calls multiUpdateNote successfully, it should call multiUpdateNoteUseCase once`() =
        runBlocking {
            //Given
            val listNoteId = listNoteId
            val mapUpdate = mapUpdate
            val anyTaskNotesCompleteState = anyTaskNotesCompleteState()
            coEvery {
                multiUpdateNoteUseCase.invoke(
                    listNoteId,
                    mapUpdate
                )
            } returns anyTaskNotesCompleteState

            //When
            homeViewModel.multiUpdateNote(listNoteId, mapUpdate)
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()

            //Then
            coVerify(exactly = 1) { multiUpdateNoteUseCase.invoke(listNoteId, mapUpdate) }
        }

    @Test
    fun `when NotesViewModel calls multiDeleteNote successfully, it should call multiDeleteNoteUseCase once`() =
        runBlocking {
            //Given
            val listNoteId = listNoteId
            val anyTaskNotesCompleteState = anyTaskNotesCompleteState()
            coEvery { multiDeleteNoteUseCase.invoke(listNoteId) } returns anyTaskNotesCompleteState

            //When
            homeViewModel.multiDeleteNote(listNoteId)
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()

            //Then
            coVerify(exactly = 1) { multiDeleteNoteUseCase.invoke(listNoteId) }
        }
}