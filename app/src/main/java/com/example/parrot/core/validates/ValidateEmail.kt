package com.example.parrot.core.validates

import android.content.Context
import android.util.Patterns
import androidx.core.content.ContextCompat
import com.example.parrot.R

object ValidateEmail {
    fun validEmail(context: Context, email: String): String? {
        if (email.isBlank()) {
            return ContextCompat.getString(context, R.string.required)
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ContextCompat.getString(context, R.string.invalidEmail)
        }
        return null
    }
}