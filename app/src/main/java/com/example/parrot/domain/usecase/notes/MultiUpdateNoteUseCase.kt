package com.example.parrot.domain.usecase.notes

import com.example.parrot.domain.FireStoreRepository
import javax.inject.Inject

class MultiUpdateNoteUseCase @Inject constructor(private val fireStoreRepository: FireStoreRepository) {
    suspend operator fun invoke(listNoteId: MutableList<String>, mapUpdate: Map<String, Boolean>) =
        fireStoreRepository.multiUpdateNote(listNoteId, mapUpdate)
}