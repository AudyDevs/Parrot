package com.example.parrot.ui.activities.register.viewmodel

import com.example.parrot.dispatcher.DispatcherRule
import com.example.parrot.dispatcher.TestDispatchers
import com.example.parrot.domain.usecase.mail.SignUpWithEmailUseCase
import com.example.parrot.motherobject.ParrotMotherObject.ANY_EMAIL
import com.example.parrot.motherobject.ParrotMotherObject.ANY_PASSWORD
import com.example.parrot.motherobject.ParrotMotherObject.anyLoginStateSuccess
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RegisterViewModelTest {

    @get:Rule
    val dispatcherRule = DispatcherRule()

    @MockK
    private lateinit var signUpWithEmailUseCase: SignUpWithEmailUseCase

    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var testDispatchers: TestDispatchers

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        testDispatchers = TestDispatchers()
        registerViewModel = RegisterViewModel(
            testDispatchers,
            signUpWithEmailUseCase
        )
    }

    @Test
    fun `when RegisterViewModel calls signUpUser successfully, it should return a success LoginState`() =
        runBlocking {
            //Given
            val email = ANY_EMAIL
            val password = ANY_PASSWORD
            coEvery { signUpWithEmailUseCase.invoke(email, password) } returns anyLoginStateSuccess

            //When
            registerViewModel.email = email
            registerViewModel.password = password
            registerViewModel.signUpUser()
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()
            val loginState = registerViewModel.loginState.value

            //Then
            assert(loginState == anyLoginStateSuccess)
        }

    @Test
    fun `when RegisterViewModel calls signUpUser successfully, it should call signUpWithEmailUseCase once`() =
        runBlocking {
            //Given
            val email = ANY_EMAIL
            val password = ANY_PASSWORD
            coEvery { signUpWithEmailUseCase.invoke(email, password) } returns anyLoginStateSuccess

            //When
            registerViewModel.email = email
            registerViewModel.password = password
            registerViewModel.signUpUser()
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()

            //Then
            coVerify(exactly = 1) { signUpWithEmailUseCase.invoke(email, password) }
        }
}