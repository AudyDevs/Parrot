package com.example.parrot.domain.usecase.google

import android.content.Intent
import com.example.parrot.domain.LoginRepository
import com.example.parrot.domain.state.LoginState
import javax.inject.Inject

class SignUpWithGoogleUseCase @Inject constructor(private val repository: LoginRepository) {
    suspend operator fun invoke(requestCode: Int, resultCode: Int, data: Intent?) =
        repository.signupWithGoogle(requestCode, resultCode, data)
}