package com.example.parrot.domain

import android.app.Activity
import android.content.Intent
import com.example.parrot.domain.model.LoginModel
import com.example.parrot.domain.state.LoginState

interface LoginRepository {

    //Email
    suspend fun loginWithEmail(email: String, password: String): LoginState

    suspend fun signupWithEmail(email: String, password: String): LoginState

    suspend fun logoutWithEmail(): LoginState

    //Google
    suspend fun loginWithGoogle(activity: Activity)

    suspend fun signupWithGoogle(requestCode: Int, resultCode: Int, data: Intent?): LoginModel

    //Facebook
    suspend fun loginWithFacebook()
}