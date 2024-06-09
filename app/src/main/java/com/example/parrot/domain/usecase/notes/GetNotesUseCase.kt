package com.example.parrot.domain.usecase.notes

import com.example.parrot.domain.FireStoreRepository
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(private val fireStoreRepository: FireStoreRepository) {
    suspend operator fun invoke() = fireStoreRepository.getNotes()
}