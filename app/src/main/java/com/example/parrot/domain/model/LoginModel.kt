package com.example.parrot.domain.model

import com.example.parrot.core.type.ProviderType

data class LoginModel(
    val email: String,
    val provider: ProviderType
)