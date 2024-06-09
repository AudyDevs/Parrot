package com.example.parrot.core.validates

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.parrot.R

object ValidateNickName {
    fun validNickName(context: Context, nickname: String): String? {
        if (nickname.isBlank()) {
            return ContextCompat.getString(context, R.string.required)
        }
        return null
    }
}