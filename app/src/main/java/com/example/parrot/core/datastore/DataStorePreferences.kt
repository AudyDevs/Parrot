package com.example.parrot.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.parrot.core.type.ProviderType
import com.example.parrot.domain.model.LoginModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class DataStorePreferences @Inject constructor(private val dataStore: DataStore<Preferences>) {

    companion object {
        const val PREFERENCE_KEY_USER = "PK_EMAIL_PASS"
        const val EMAIL = "EMAIL"
        const val PROVIDER = "PROVIDER"
    }

    private object PreferenceKeys {
        val email = stringPreferencesKey(EMAIL)
        val provider = stringPreferencesKey(PROVIDER)
    }

    suspend fun readDataStoreUser(): LoginModel = runBlocking {
        val preferences = dataStore.data.first()
        val email = preferences[PreferenceKeys.email].orEmpty()
        val provider = when (preferences[PreferenceKeys.provider].orEmpty()) {
            ProviderType.Email.type -> ProviderType.Email
            ProviderType.Google.type -> ProviderType.Google
            else -> ProviderType.Email
        }
        return@runBlocking LoginModel(email = email, provider = provider)
    }

    suspend fun saveDataStoreUser(email: String, provider: ProviderType) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.email] = email
            preferences[PreferenceKeys.provider] = provider.type
        }
    }

    suspend fun clearDataStoreUser() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}