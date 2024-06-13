package com.example.parrot.data

import com.example.parrot.core.datastore.DataStorePreferences
import com.example.parrot.motherobject.ParrotMotherObject.ANY_EMAIL
import com.example.parrot.motherobject.ParrotMotherObject.anyLoginModel
import com.example.parrot.motherobject.ParrotMotherObject.anyProviderType
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DataStoreRepositoryImplTest {

    @MockK
    private lateinit var dataStorePreferences: DataStorePreferences
    private lateinit var dataStoreRepositoryImpl: DataStoreRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        dataStoreRepositoryImpl = DataStoreRepositoryImpl(dataStorePreferences)
    }

    @Test
    fun `when DataStoreRepositoryImpl call getUserFromDataStore successfully, it should returns a correct loginModel`() {
        runBlocking {
            //Given
            val loginModel = anyLoginModel
            coEvery { dataStorePreferences.readDataStoreUser() } returns anyLoginModel

            //When
            val loginModelRepository = dataStoreRepositoryImpl.getUserFromDataStore()

            //Then
            assert(loginModelRepository == loginModel)
        }
    }

    @Test
    fun `when DataStoreRepositoryImpl call saveUserToDataStore successfully, it should calls saveDataStoreUser from dataStorePreferences once`() {
        runBlocking {
            //Given
            val email = ANY_EMAIL
            val provider = anyProviderType
            coEvery { dataStorePreferences.saveDataStoreUser(email, provider) } just runs

            //When
            dataStoreRepositoryImpl.saveUserToDataStore(email, provider)

            //Then
            coVerify(exactly = 1) { dataStorePreferences.saveDataStoreUser(email, provider) }
        }
    }

    @Test
    fun `when DataStoreRepositoryImpl call clearUserToDataStore successfully, it should calls clearDataStoreUser from dataStorePreferences once`() {
        runBlocking {
            //Given
            coEvery { dataStorePreferences.clearDataStoreUser() } just runs

            //When
            dataStoreRepositoryImpl.clearUserToDataStore()

            //Then
            coVerify(exactly = 1) { dataStorePreferences.clearDataStoreUser() }
        }
    }
}