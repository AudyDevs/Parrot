package com.example.parrot.ui.activities.home.manager

import com.example.parrot.core.type.FragmentType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MenuManager @Inject constructor() {

    private val _menuOnClickItem = MutableStateFlow<FragmentType?>(null)
    val menuOnClickItem: StateFlow<FragmentType?> = _menuOnClickItem

    private val _listNotesSelected = MutableStateFlow<MutableList<String>?>(null)
    val listNotesSelected: StateFlow<MutableList<String>?> = _listNotesSelected

    private val _isSelectedMenu = MutableStateFlow(false)
    val isSelectedMenu: StateFlow<Boolean> = _isSelectedMenu

    fun availableSelectorMenu(reset: Boolean) {
        _isSelectedMenu.value = reset
    }

    fun resetMenu() {
        _menuOnClickItem.value = null
        _listNotesSelected.value = null
    }

    fun setListMenu(listNotesId: MutableList<String>?) {
        _listNotesSelected.value = listNotesId
    }

    fun showMenuOnClickItem(fragmentType: FragmentType, listNotesSelected: MutableList<String>) {
        _listNotesSelected.value = listNotesSelected
        _menuOnClickItem.value = fragmentType
    }
}