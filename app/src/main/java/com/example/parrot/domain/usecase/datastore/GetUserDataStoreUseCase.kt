package com.example.parrot.domain.usecase.datastore

import com.example.parrot.domain.DataStoreRepository
import javax.inject.Inject

class GetUserDataStoreUseCase @Inject constructor(private val repository: DataStoreRepository) {
    suspend operator fun invoke() = repository.getUserFromDataStore()
}