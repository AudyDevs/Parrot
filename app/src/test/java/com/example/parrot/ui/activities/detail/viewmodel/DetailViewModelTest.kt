package com.example.parrot.ui.activities.detail.viewmodel

import com.example.parrot.dispatcher.DispatcherRule
import com.example.parrot.dispatcher.TestDispatchers
import com.example.parrot.domain.usecase.notes.DeleteNoteUseCase
import com.example.parrot.domain.usecase.notes.GetNoteIdUseCase
import com.example.parrot.domain.usecase.notes.SaveNoteUseCase
import com.example.parrot.domain.usecase.notes.UpdateNoteUseCase
import com.example.parrot.motherobject.ParrotMotherObject.ANY_NOTE_ID1
import com.example.parrot.motherobject.ParrotMotherObject.anyNotesModel
import com.example.parrot.motherobject.ParrotMotherObject.anyTaskNotesSuccessState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailViewModelTest {

    @get:Rule
    val dispatcherRule = DispatcherRule()

    @MockK
    private lateinit var getNoteIdUseCase: GetNoteIdUseCase

    @MockK
    private lateinit var saveNoteUseCase: SaveNoteUseCase

    @MockK
    private lateinit var updateNoteUseCase: UpdateNoteUseCase

    @MockK
    private lateinit var deleteNoteUseCase: DeleteNoteUseCase

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var testDispatchers: TestDispatchers

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        testDispatchers = TestDispatchers()
        detailViewModel = DetailViewModel(
            testDispatchers,
            getNoteIdUseCase,
            saveNoteUseCase,
            updateNoteUseCase,
            deleteNoteUseCase
        )
    }

    @Test
    fun `when DetailViewModel calls getNoteId successfully, it should call getNoteIdUseCase once`() =
        runBlocking {
            //Given
            val noteId = ANY_NOTE_ID1
            val anyTaskNotesSuccessState = anyTaskNotesSuccessState()
            coEvery { getNoteIdUseCase.invoke(noteId) } returns anyTaskNotesSuccessState

            //When
            detailViewModel.noteId = noteId
            detailViewModel.getNoteId()
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()

            //Then
            coVerify(exactly = 1) { getNoteIdUseCase.invoke(noteId) }
        }

    @Test
    fun `when DetailViewModel calls saveNote successfully, it should call saveNoteUseCase once`() =
        runBlocking {
            //Given
            val notesModel = anyNotesModel
            val anyTaskNotesSuccessState = anyTaskNotesSuccessState()
            coEvery { saveNoteUseCase.invoke(notesModel) } returns anyTaskNotesSuccessState

            //When
            detailViewModel.saveNote(notesModel)
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()

            //Then
            coVerify(exactly = 1) { saveNoteUseCase.invoke(notesModel) }
        }

    @Test
    fun `when DetailViewModel calls updateNote successfully, it should call updateNoteUseCase once`() =
        runBlocking {
            //Given
            val notesModel = anyNotesModel
            val anyTaskNotesSuccessState = anyTaskNotesSuccessState()
            coEvery { updateNoteUseCase.invoke(notesModel) } returns anyTaskNotesSuccessState

            //When
            detailViewModel.updateNote(notesModel)
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()

            //Then
            coVerify(exactly = 1) { updateNoteUseCase.invoke(notesModel) }
        }

    @Test
    fun `when DetailViewModel calls deleteNote successfully, it should call deleteNoteUseCase once`() =
        runBlocking {
            //Given
            val noteId = ANY_NOTE_ID1
            val anyTaskNotesSuccessState = anyTaskNotesSuccessState()
            coEvery { deleteNoteUseCase.invoke(noteId) } returns anyTaskNotesSuccessState

            //When
            detailViewModel.noteId = noteId
            detailViewModel.deleteNote()
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()

            //Then
            coVerify(exactly = 1) { deleteNoteUseCase.invoke(noteId) }
        }
}