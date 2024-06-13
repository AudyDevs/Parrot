package com.example.parrot.domain.usecase.datastore

import com.example.parrot.domain.DataStoreRepository
import com.example.parrot.motherobject.ParrotMotherObject.ANY_EMAIL
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

class SaveUserDataStoreUseCaseTest {

    @MockK
    private lateinit var repository: DataStoreRepository
    private lateinit var saveUserDataStoreUseCase: SaveUserDataStoreUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        saveUserDataStoreUseCase = SaveUserDataStoreUseCase(repository)
    }

    @Test
    fun `when SaveUserDataStoreUseCase is called successfully, DataStoreRepository should call saveUserToDataStore once`() =
        runBlocking {
            //Given
            val email = ANY_EMAIL
            val provider = anyProviderType
            coEvery { repository.saveUserToDataStore(email, provider) } just runs

            //When
            saveUserDataStoreUseCase.invoke(email, provider)

            //Then
            coVerify(exactly = 1) { repository.saveUserToDataStore(email, provider) }
        }
}