package com.example.parrot.domain.usecase.datastore

import com.example.parrot.domain.DataStoreRepository
import com.example.parrot.motherobject.ParrotMotherObject.anyLoginModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetUserDataStoreUseCaseTest {

    @MockK
    private lateinit var repository: DataStoreRepository
    private lateinit var getUserDataStoreUseCase: GetUserDataStoreUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        getUserDataStoreUseCase = GetUserDataStoreUseCase(repository)
    }

    @Test
    fun `when GetUserDataStoreUseCase is called successfully, DataStoreRepository should return a correct loginModel`() =
        runBlocking {
            //Given
            coEvery { repository.getUserFromDataStore() } returns anyLoginModel

            //When
            val loginModel = getUserDataStoreUseCase.invoke()

            //Then
            assert(loginModel == anyLoginModel)
        }

    @Test
    fun `when GetUserDataStoreUseCase is called successfully, DataStoreRepository should call getUserFromDataStore once`() =
        runBlocking {
            //Given
            coEvery { repository.getUserFromDataStore() } returns anyLoginModel

            //When
            getUserDataStoreUseCase.invoke()

            //Then
            coVerify(exactly = 1) { repository.getUserFromDataStore() }
        }
}