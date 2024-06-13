package com.example.parrot.ui.activities.login.viewmodel

import com.example.parrot.dispatcher.DispatcherRule
import com.example.parrot.dispatcher.TestDispatchers
import com.example.parrot.domain.usecase.datastore.GetUserDataStoreUseCase
import com.example.parrot.domain.usecase.datastore.SaveUserDataStoreUseCase
import com.example.parrot.domain.usecase.google.LoginWithGoogleUseCase
import com.example.parrot.domain.usecase.google.SignUpWithGoogleUseCase
import com.example.parrot.domain.usecase.mail.LoginWithEmailUseCase
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

class LoginViewModelTest {

    @get:Rule
    val dispatcherRule = DispatcherRule()

    @MockK
    private lateinit var loginWithEmailUseCase: LoginWithEmailUseCase

    @MockK
    private lateinit var loginWithGoogleUseCase: LoginWithGoogleUseCase

    @MockK
    private lateinit var signUpWithGoogleUseCase: SignUpWithGoogleUseCase

    @MockK
    private lateinit var getUserDataStoreUseCase: GetUserDataStoreUseCase

    @MockK
    private lateinit var saveUserDataStoreUseCase: SaveUserDataStoreUseCase

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var testDispatchers: TestDispatchers

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        testDispatchers = TestDispatchers()
        loginViewModel = LoginViewModel(
            testDispatchers,
            loginWithEmailUseCase,
            loginWithGoogleUseCase,
            signUpWithGoogleUseCase,
            getUserDataStoreUseCase,
            saveUserDataStoreUseCase
        )
    }

    @Test
    fun `when LoginViewModel calls loginUserWithEmail successfully, it should return a success LoginState`() =
        runBlocking {
            //Given
            val email = ANY_EMAIL
            val password = ANY_PASSWORD
            coEvery { loginWithEmailUseCase.invoke(email, password) } returns anyLoginStateSuccess

            //When
            loginViewModel.email = email
            loginViewModel.password = password
            loginViewModel.loginUserWithEmail()
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()
            val loginState = loginViewModel.loginState.value

            //Then
            assert(loginState == anyLoginStateSuccess)
        }

    @Test
    fun `when LoginViewModel calls loginUserWithEmail successfully, it should call loginWithEmailUseCase once`() =
        runBlocking {
            //Given
            val email = ANY_EMAIL
            val password = ANY_PASSWORD
            coEvery { loginWithEmailUseCase.invoke(email, password) } returns anyLoginStateSuccess

            //When
            loginViewModel.email = email
            loginViewModel.password = password
            loginViewModel.loginUserWithEmail()
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()

            //Then
            coVerify(exactly = 1) { loginWithEmailUseCase.invoke(email, password) }
        }
}