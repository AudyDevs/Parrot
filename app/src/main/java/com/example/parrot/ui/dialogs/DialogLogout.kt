package com.example.parrot.ui.dialogs

import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.parrot.R

class DialogLogout(view: View, private val onSelectedButton: (Boolean) -> Unit) {

    init {
        val builder = AlertDialog.Builder(view.context)
            .setTitle(R.string.dialogLogoutTitle)
            .setMessage(R.string.dialogLogoutSubTitle)
            .setCancelable(false)
            .setPositiveButton(
                R.string.dialogLogoutButtonYes
            ) { _, _ -> onSelectedButton(true) }
            .setNegativeButton(
                R.string.dialogLogoutButtonCancel
            ) { _, _ -> onSelectedButton(false) }
            .create()
        builder.show()
        builder.getButton(AlertDialog.BUTTON_POSITIVE)
    }
}