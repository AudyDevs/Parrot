package com.example.parrot.ui.dialogs

import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.parrot.R

class DialogDelete(
    view: View,
    multiDelete: Boolean,
    private val onSelectedButton: (Boolean) -> Unit
) {

    init {
        val titleMessage: String
        val subTitleMessage: String
        if (multiDelete) {
            titleMessage = ContextCompat.getString(view.context, R.string.dialogMultiDeleteTitle)
            subTitleMessage =
                ContextCompat.getString(view.context, R.string.dialogMultiDeleteSubTitle)
        } else {
            titleMessage = ContextCompat.getString(view.context, R.string.dialogDeleteTitle)
            subTitleMessage = ContextCompat.getString(view.context, R.string.dialogDeleteSubTitle)
        }

        val builder = AlertDialog.Builder(view.context)
            .setTitle(titleMessage)
            .setMessage(subTitleMessage)
            .setCancelable(false)
            .setPositiveButton(
                R.string.dialogDeleteButtonYes
            ) { _, _ -> onSelectedButton(true) }
            .setNegativeButton(
                R.string.dialogDeleteButtonCancel
            ) { _, _ -> onSelectedButton(false) }
            .create()
        builder.show()
        builder.getButton(AlertDialog.BUTTON_POSITIVE)
    }
}