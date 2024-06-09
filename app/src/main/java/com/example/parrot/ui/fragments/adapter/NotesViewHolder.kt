package com.example.parrot.ui.fragments.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.parrot.databinding.ItemNoteBinding
import com.example.parrot.domain.model.NotesModel

class NotesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemNoteBinding.bind(view)
    fun render(note: NotesModel, onItemSelected: (NotesModel) -> Unit) {
        binding.apply {
            tvIdNote.text = note.id
            tvTitleNote.text = note.title
            tvBodyNote.text = note.body

            layoutCard.setOnClickListener { onItemSelected(note) }
        }
    }
}