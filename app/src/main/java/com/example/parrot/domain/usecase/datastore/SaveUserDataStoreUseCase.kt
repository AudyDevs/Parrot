package com.example.parrot.domain.usecase.datastore

import com.example.parrot.core.type.ProviderType
import com.example.parrot.domain.DataStoreRepository
import javax.inject.Inject

class SaveUserDataStoreUseCase @Inject constructor(private val repository: DataStoreRepository) {
    suspend operator fun invoke(email: String, provider: ProviderType) =
        repository.saveUserToDataStore(email, provider)
}