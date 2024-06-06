package com.example.parrot.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.example.parrot.databinding.DialogSuccesBinding

class DialogSuccess(
    context: Context,
    private var onClickButtonSuccess: () -> Unit
) : Dialog(context) {

    private val binding = DialogSuccesBinding.inflate(layoutInflater)

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        initListeners()
        show()
    }

    private fun initListeners() {
        binding.btnSuccess.setOnClickListener {
            onClickButtonSuccess()
            dismiss()
        }
    }
}