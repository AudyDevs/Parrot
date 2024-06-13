package com.example.parrot.domain.usecase.datastore

import com.example.parrot.domain.DataStoreRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class ClearUserDataStoreUseCaseTest {

    @MockK
    private lateinit var repository: DataStoreRepository
    private lateinit var clearUserDataStoreUseCase: ClearUserDataStoreUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        clearUserDataStoreUseCase = ClearUserDataStoreUseCase(repository)
    }

    @Test
    fun `when SaveUserDataStoreUseCase is called successfully, DataStoreRepository should call saveUserToDataStore once`() =
        runBlocking {
            //Given
            coEvery { repository.clearUserToDataStore() } just runs

            //When
            clearUserDataStoreUseCase.invoke()

            //Then
            coVerify(exactly = 1) { repository.clearUserToDataStore() }
        }
}