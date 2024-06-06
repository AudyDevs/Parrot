package com.example.parrot.core.validates

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.parrot.R

object ValidateRepeatPassword {
    fun validRepeatPassword(context: Context, password: String, repeatPassword: String): String? {
        if (password.isBlank() && repeatPassword.isBlank()) {
            return ContextCompat.getString(context, R.string.required)
        }
        if (password != repeatPassword) {
            return ContextCompat.getString(context, R.string.exactPassword)
        }
        return null
    }
}