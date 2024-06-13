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

class SignUpWithEmailUseCaseTest {

    @MockK
    private lateinit var repository: LoginRepository
    private lateinit var signUpWithEmailUseCase: SignUpWithEmailUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        signUpWithEmailUseCase = SignUpWithEmailUseCase(repository)
    }

    @Test
    fun `when SignUpWithEmailUseCase is called successfully, LoginRepository should call signupWithEmail once`() =
        runBlocking {
            //Given
            val email = ANY_EMAIL
            val password = ANY_PASSWORD
            coEvery { repository.signupWithEmail(email, password) } returns anyLoginStateSuccess

            //When
            signUpWithEmailUseCase.invoke(email, password)

            //Then
            coVerify(exactly = 1) { repository.signupWithEmail(email, password) }
        }

    @Test
    fun `when SignUpWithEmailUseCase is called successfully, LoginRepository should return a success loginState`() =
        runBlocking {
            //Given
            val email = ANY_EMAIL
            val password = ANY_PASSWORD
            coEvery { repository.signupWithEmail(email, password) } returns anyLoginStateSuccess

            //When
            val loginState = signUpWithEmailUseCase.invoke(email, password)

            //Then
            assert(loginState == anyLoginStateSuccess)
        }
}