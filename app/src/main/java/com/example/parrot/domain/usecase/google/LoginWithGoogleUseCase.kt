package com.example.parrot.domain.usecase.google

import android.app.Activity
import com.example.parrot.domain.LoginRepository
import javax.inject.Inject

class LoginWithGoogleUseCase @Inject constructor(private val repository: LoginRepository) {
    suspend operator fun invoke(activity: Activity) = repository.loginWithGoogle(activity)
}