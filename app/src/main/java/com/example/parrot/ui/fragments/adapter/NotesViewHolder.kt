package com.example.parrot.ui.fragments.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.parrot.R
import com.example.parrot.databinding.ItemNoteBinding
import com.example.parrot.domain.model.INT_NULL
import com.example.parrot.domain.model.NotesModel

class NotesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemNoteBinding.bind(view)
    private val context = view.context

    fun render(
        note: NotesModel,
        itemSelectedList: MutableList<String>,
        showMenuOnClick: (Boolean, MutableList<String>) -> Unit,
        onItemSelected: (NotesModel) -> Unit
    ) {
        binding.apply {
            tvIdNote.text = note.id
            tvTitleNote.isVisible = !note.title.isNullOrEmpty()
            tvTitleNote.text = limitedText(note.title ?: "", 50)
            tvBodyNote.isVisible = !note.body.isNullOrEmpty()
            tvBodyNote.text = limitedText(note.body ?: "", 100)
            changeColorUI(note.backcolor!!, false)

            layoutCard.setOnLongClickListener {
                if (itemSelectedList.isEmpty()) {
                    showMenuOnClick(true, itemSelectedList)
                    itemSelectedList.add(note.id ?: "")
                    changeColorUI(note.backcolor, true)
                }
                true
            }

            layoutCard.setOnClickListener {
                if (itemSelectedList.isEmpty()) {
                    onItemSelected(note)
                } else {
                    if (itemSelectedList.contains(note.id ?: "")) {
                        itemSelectedList.remove(note.id ?: "")
                        changeColorUI(note.backcolor, false)
                        if (itemSelectedList.isEmpty()) {
                            showMenuOnClick(false, itemSelectedList)
                        } else {
                            showMenuOnClick(true, itemSelectedList)
                        }
                    } else {
                        showMenuOnClick(true, itemSelectedList)
                        itemSelectedList.add(note.id ?: "")
                        changeColorUI(note.backcolor, true)
                    }
                }
            }
        }
    }

    private fun limitedText(text: String, maxLength: Int): String {
        return if (text.length > maxLength) {
            text.take(maxLength) + "..."
        } else {
            text
        }
    }

    private fun changeColorUI(color: Int, selected: Boolean) {
        binding.apply {
            if (selected) {
                frameLayout.background =
                    ContextCompat.getDrawable(context, R.drawable.item_card_border_white)
                layoutCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.selected))
                tvTitleNote.setTextColor(ContextCompat.getColor(context, R.color.textWhite))
                tvBodyNote.setTextColor(ContextCompat.getColor(context, R.color.textWhite))
            } else {
                if (color == INT_NULL) {
                    frameLayout.background =
                        ContextCompat.getDrawable(context, R.drawable.item_card_border_primary)
                    layoutCard.setCardBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.background
                        )
                    )
                    tvTitleNote.setTextColor(ContextCompat.getColor(context, R.color.primary))
                    tvBodyNote.setTextColor(ContextCompat.getColor(context, R.color.primary))
                } else {
                    frameLayout.background =
                        ContextCompat.getDrawable(context, R.drawable.item_card_border_white)
                    layoutCard.setCardBackgroundColor(ContextCompat.getColor(context, color))
                    tvTitleNote.setTextColor(ContextCompat.getColor(context, R.color.textWhite))
                    tvBodyNote.setTextColor(ContextCompat.getColor(context, R.color.textWhite))
                }
            }
        }
    }
}