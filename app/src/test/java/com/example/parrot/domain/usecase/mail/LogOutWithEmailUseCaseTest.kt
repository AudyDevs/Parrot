package com.example.parrot.domain.usecase.mail

import com.example.parrot.domain.LoginRepository
import com.example.parrot.motherobject.ParrotMotherObject.anyLoginStateSuccess
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class LogOutWithEmailUseCaseTest {

    @MockK
    private lateinit var repository: LoginRepository
    private lateinit var logOutWithEmailUseCase: LogOutWithEmailUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        logOutWithEmailUseCase = LogOutWithEmailUseCase(repository)
    }

    @Test
    fun `when LogOutWithEmailUseCase is called successfully, LoginRepository should call logoutWithEmail once`() =
        runBlocking {
            //Given
            coEvery { repository.logoutWithEmail() } returns anyLoginStateSuccess

            //When
            logOutWithEmailUseCase.invoke()

            //Then
            coVerify(exactly = 1) { repository.logoutWithEmail() }
        }

    @Test
    fun `when LogOutWithEmailUseCase is called successfully, LoginRepository should return a success loginState`() =
        runBlocking {
            //Given
            coEvery { repository.logoutWithEmail() } returns anyLoginStateSuccess

            //When
            val loginState = logOutWithEmailUseCase.invoke()

            //Then
            assert(loginState == anyLoginStateSuccess)
        }
}