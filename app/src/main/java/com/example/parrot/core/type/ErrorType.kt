package com.example.parrot.core.type

import com.example.parrot.R

sealed class ErrorType(val errorMessage: Int) {
    data object SignIn: ErrorType(R.string.subTitleErrorSignIn)
    data object LogOut: ErrorType(R.string.subTitleErrorLogOut)
    data object SignUp: ErrorType(R.string.subTitleErrorSignUp)
}