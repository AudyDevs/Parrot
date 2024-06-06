package com.example.parrot.core.validates

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.parrot.R

object ValidatePhone {
    fun validPhone(context: Context, phone: String): String? {
        if (phone.isBlank()) {
            return null
        }
        if (!phone.matches(".*[0-9].*".toRegex())) {
            return ContextCompat.getString(context, R.string.invalidPhone)
        }
        if (phone.length != 9) {
            return ContextCompat.getString(context, R.string.minLengthPhone)
        }
        return null
    }
}