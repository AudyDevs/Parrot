package com.example.parrot.domain.usecase.notes

import com.example.parrot.domain.FireStoreRepository
import com.example.parrot.domain.model.NotesModel
import javax.inject.Inject

class UpdateNoteUseCase @Inject constructor(private val fireStoreRepository: FireStoreRepository) {
    suspend operator fun invoke(notesResponse: NotesModel) =
        fireStoreRepository.updateNote(notesResponse)
}