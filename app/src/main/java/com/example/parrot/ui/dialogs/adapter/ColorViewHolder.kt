package com.example.parrot.ui.dialogs.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.parrot.R
import com.example.parrot.databinding.ItemColorBinding
import com.example.parrot.domain.model.ColorModel
import com.example.parrot.domain.model.INT_NULL

class ColorViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemColorBinding.bind(view)
    fun render(color: ColorModel, onItemSelected: (ColorModel) -> Unit) {
        val context = binding.itemColor.context
        if (color.color != INT_NULL) {
            binding.imageReset.isVisible = false
            binding.itemColor.setCardBackgroundColor(ContextCompat.getColor(context, color.color))
        } else {
            binding.imageReset.isVisible = true
            binding.itemColor.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.backcolorBlack
                )
            )
        }
        binding.frameColorLayout.setOnClickListener { onItemSelected(color) }
    }
}