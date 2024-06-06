package com.example.parrot.domain.usecase.mail

import com.example.parrot.domain.LoginRepository
import javax.inject.Inject

class SignUpWithEmailUseCase @Inject constructor(private val repository: LoginRepository) {
    suspend operator fun invoke(email: String, password: String) =
        repository.signupWithEmail(email, password)
}