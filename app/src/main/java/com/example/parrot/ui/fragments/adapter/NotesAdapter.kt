package com.example.parrot.ui.fragments.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.parrot.R
import com.example.parrot.domain.model.NotesModel

class NotesAdapter(
    private var notesList: List<NotesModel> = emptyList(),
    private val showMenuOnClick: (Boolean, MutableList<String>) -> Unit,
    private val onItemSelected: (NotesModel) -> Unit
) :
    RecyclerView.Adapter<NotesViewHolder>() {

    private val itemSelectedList = mutableListOf<String>()

    fun updateList(list: List<NotesModel>) {
        val diff = NotesDiffUtil(notesList, list)
        val result = DiffUtil.calculateDiff((diff))
        notesList = list
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val layoutInflater =
            LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NotesViewHolder(layoutInflater)
    }

    override fun getItemCount(): Int = notesList.size

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.render(
            notesList[position],
            itemSelectedList,
            showMenuOnClick,
            onItemSelected
        )
    }
}