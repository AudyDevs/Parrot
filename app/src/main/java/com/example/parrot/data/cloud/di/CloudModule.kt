package com.example.parrot.data.cloud.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.parrot.core.datastore.DataStorePreferences
import com.example.parrot.data.DataStoreRepositoryImpl
import com.example.parrot.data.LoginRepositoryImpl
import com.example.parrot.domain.DataStoreRepository
import com.example.parrot.domain.LoginRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class CloudModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    private val Context.dataStoreUser: DataStore<Preferences> by preferencesDataStore(name = DataStorePreferences.PREFERENCE_KEY_USER)

    @Provides
    @Singleton
    fun provideDataStoreUser(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStoreUser
    }

    @Provides
    fun provideLoginRepository(
        firebaseAuth: FirebaseAuth
    ): LoginRepository =
        LoginRepositoryImpl(firebaseAuth)

    @Provides
    fun provideDataStoreRepository(dataStorePreferences: DataStorePreferences): DataStoreRepository =
        DataStoreRepositoryImpl(dataStorePreferences)
}