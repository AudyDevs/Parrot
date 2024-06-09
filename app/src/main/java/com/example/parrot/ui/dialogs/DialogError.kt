package com.example.parrot.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import androidx.core.content.ContextCompat
import com.example.parrot.databinding.DialogErrorBinding

class DialogError(
    context: Context, private val errorMessage: Int, private var onClickButtonError: () -> Unit
) : Dialog(context) {

    private val binding = DialogErrorBinding.inflate(layoutInflater)

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        initMessageError()
        initListeners()
        show()
    }

    private fun initMessageError() {
        binding.tvSubTitleError.text = ContextCompat.getString(context, errorMessage)
    }

    private fun initListeners() {
        binding.btnError.setOnClickListener {
            onClickButtonError()
            dismiss()
        }
    }
}