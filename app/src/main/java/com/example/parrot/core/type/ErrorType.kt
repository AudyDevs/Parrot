package com.example.parrot.core.type

import com.example.parrot.R

sealed class ErrorType(val errorMessage: Int) {
    data object SignIn : ErrorType(R.string.subTitleErrorSignIn)
    data object LogOut : ErrorType(R.string.subTitleErrorLogOut)
    data object SignUp : ErrorType(R.string.subTitleErrorSignUp)
    data object GetNotes : ErrorType(R.string.subTitleErrorGetNotes)
    data object SaveNotes : ErrorType(R.string.subTitleErrorSaveNotes)
    data object UpdateNotes : ErrorType(R.string.subTitleErrorUpdateNotes)
    data object DeleteNotes : ErrorType(R.string.subTitleErrorDeleteNotes)
}