package com.example.parrot.domain.model

data class NotesModel(
    var id: String? = null,
    var userId: String? = null,
    val title: String? = null,
    val body: String? = null,
    @field:JvmField val isArchived: Boolean? = null,
    @field:JvmField val isDeleted: Boolean? = null,
    val backcolor: Int? = null
)