package com.example.parrot.domain.usecase.notes

import com.example.parrot.domain.FireStoreRepository
import javax.inject.Inject

class MultiDeleteNoteUseCase @Inject constructor(private val fireStoreRepository: FireStoreRepository) {
    suspend operator fun invoke(listNoteId: MutableList<String>) =
        fireStoreRepository.multiDeleteNote(listNoteId)
}
