package com.example.parrot.ui.dialogs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.parrot.R
import com.example.parrot.domain.model.ColorModel

class ColorAdapter(
    private var colors: List<ColorModel> = emptyList(),
    private var onItemSelected: (ColorModel) -> Unit
) :
    RecyclerView.Adapter<ColorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val layoutInflater =
            LayoutInflater.from(parent.context).inflate(R.layout.item_color, parent, false)
        return ColorViewHolder(layoutInflater)
    }

    override fun getItemCount(): Int = colors.size

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.render(colors[position], onItemSelected)
    }

}