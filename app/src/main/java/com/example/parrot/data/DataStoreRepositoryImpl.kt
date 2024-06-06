package com.example.parrot.data

import com.example.parrot.core.datastore.DataStorePreferences
import com.example.parrot.core.type.ProviderType
import com.example.parrot.domain.DataStoreRepository
import com.example.parrot.domain.model.LoginModel
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    private val dataStorePreferences: DataStorePreferences
) : DataStoreRepository {

    override suspend fun getUserFromDataStore(): LoginModel {
        return dataStorePreferences.readDataStoreUser()
    }

    override suspend fun saveUserToDataStore(email: String, provider: ProviderType) {
        dataStorePreferences.saveDataStoreUser(email, provider)
    }

    override suspend fun clearUserToDataStore() {
        dataStorePreferences.clearDataStoreUser()
    }
}