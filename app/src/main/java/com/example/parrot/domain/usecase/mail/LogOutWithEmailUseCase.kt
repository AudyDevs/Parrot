package com.example.parrot.domain.usecase.mail

import com.example.parrot.domain.LoginRepository
import javax.inject.Inject

class LogOutWithEmailUseCase @Inject constructor(private val repository: LoginRepository) {
    suspend operator fun invoke() = repository.logoutWithEmail()
}