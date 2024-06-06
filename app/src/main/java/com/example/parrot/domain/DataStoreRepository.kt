package com.example.parrot.domain

import com.example.parrot.core.type.ProviderType
import com.example.parrot.domain.model.LoginModel

interface DataStoreRepository {

    suspend fun getUserFromDataStore(): LoginModel

    suspend fun saveUserToDataStore(email: String, provider: ProviderType)

    suspend fun clearUserToDataStore()
}