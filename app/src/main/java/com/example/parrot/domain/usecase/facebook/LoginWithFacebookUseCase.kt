package com.example.parrot.domain.usecase.facebook

import com.example.parrot.domain.LoginRepository
import javax.inject.Inject

class LoginWithFacebookUseCase @Inject constructor(private val repository: LoginRepository) {
    suspend operator fun invoke() = repository.loginWithFacebook()
}