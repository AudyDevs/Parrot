package com.example.parrot.domain.usecase.mail

import com.example.parrot.domain.LoginRepository
import com.example.parrot.motherobject.ParrotMotherObject.ANY_EMAIL
import com.example.parrot.motherobject.ParrotMotherObject.ANY_PASSWORD
import com.example.parrot.motherobject.ParrotMotherObject.anyLoginStateSuccess
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class LoginWithEmailUseCaseTest {

    @MockK
    private lateinit var repository: LoginRepository
    private lateinit var loginWithEmailUseCase: LoginWithEmailUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        loginWithEmailUseCase = LoginWithEmailUseCase(repository)
    }

    @Test
    fun `when LoginWithEmailUseCase is called successfully, LoginRepository should call loginWithEmail once`() =
        runBlocking {
            //Given
            val email = ANY_EMAIL
            val password = ANY_PASSWORD
            coEvery { repository.loginWithEmail(email, password) } returns anyLoginStateSuccess

            //When
            loginWithEmailUseCase.invoke(email, password)

            //Then
            coVerify(exactly = 1) { repository.loginWithEmail(email, password) }
        }

    @Test
    fun `when LoginWithEmailUseCase is called successfully, LoginRepository should return a success loginState`() =
        runBlocking {
            //Given
            val email = ANY_EMAIL
            val password = ANY_PASSWORD
            coEvery { repository.loginWithEmail(email, password) } returns anyLoginStateSuccess

            //When
            val loginState = loginWithEmailUseCase.invoke(email, password)

            //Then
            assert(loginState == anyLoginStateSuccess)
        }
}