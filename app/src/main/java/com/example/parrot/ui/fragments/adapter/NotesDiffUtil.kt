package com.example.parrot.ui.fragments.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.parrot.domain.model.NotesModel

class NotesDiffUtil(
    private val oldList: List<NotesModel>,
    private val newList: List<NotesModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}