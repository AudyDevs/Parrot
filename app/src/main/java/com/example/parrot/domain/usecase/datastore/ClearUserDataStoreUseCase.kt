package com.example.parrot.domain.usecase.datastore

import com.example.parrot.domain.DataStoreRepository
import javax.inject.Inject

class ClearUserDataStoreUseCase @Inject constructor(private val repository: DataStoreRepository) {
    suspend operator fun invoke() = repository.clearUserToDataStore()
}