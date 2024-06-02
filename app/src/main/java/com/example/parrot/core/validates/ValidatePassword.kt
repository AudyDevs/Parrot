package com.example.parrot.core.validates

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.parrot.R

object ValidatePassword {
    fun validPassword(context: Context, password: String): String? {
        if (password.isBlank()) {
            return ContextCompat.getString(context, R.string.required)
        }
        if (password.length < 8) {
            return ContextCompat.getString(context, R.string.minLengthPassword)
        }
        if (password.length > 16) {
            return ContextCompat.getString(context, R.string.maxLengthPassword)
        }
        if (password.matches(".*[A-Z].*".toRegex())) {
            return ContextCompat.getString(context, R.string.minUpCasePassword)
        }
        if (password.matches(".*[a-z].*".toRegex())) {
            return ContextCompat.getString(context, R.string.minLowCasePassword)
        }
        return null
    }
}