package com.example.parrot.core.type

sealed class ProviderType(val type: String) {
    data object Email : ProviderType("EMAIL")
    data object Google : ProviderType("GOOGLE")
}