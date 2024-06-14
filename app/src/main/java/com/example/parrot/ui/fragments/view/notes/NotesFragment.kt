package com.example.parrot.ui.fragments.view.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.parrot.R
import com.example.parrot.core.type.FragmentType
import com.example.parrot.databinding.FragmentNotesBinding
import com.example.parrot.domain.model.NotesModel
import com.example.parrot.domain.state.NotesState
import com.example.parrot.ui.activities.home.manager.MenuManager
import com.example.parrot.ui.dialogs.DialogError
import com.example.parrot.ui.fragments.adapter.NotesAdapter
import com.example.parrot.ui.fragments.viewmodel.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotesFragment : Fragment() {

    @Inject
    lateinit var menuManager: MenuManager

    private val notesViewModel by viewModels<NotesViewModel>()
    private lateinit var notesAdapter: NotesAdapter
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(layoutInflater, container, false)
        initUI()
        return binding.root
    }

    private fun initUI() {
        configSwipe()
        initAdapter()
        initListeners()
        initUIState()
    }

    override fun onResume() {
        super.onResume()
        initViewModel()
    }

    private fun initViewModel() {
        menuManager.resetMenu()
        notesViewModel.getNotes()
    }

    private fun configSwipe() {
        binding.swipe.setColorSchemeResources(R.color.secondary)
        binding.swipe.setOnRefreshListener {
            notesViewModel.getNotes()
        }
    }

    private fun initAdapter() {
        notesAdapter = NotesAdapter(
            onItemSelected = { noteModel ->
                navigateToDetailActivity(noteModel.id)
            },
            showMenuOnClick = { showMenu, listNotesSelected ->
                if (showMenu) {
                    menuManager.showMenuOnClickItem(FragmentType.NotesFragment, listNotesSelected)
                } else {
                    menuManager.resetMenu()
                }
            })

        binding.rvNotes.setHasFixedSize(true)
        binding.rvNotes.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = notesAdapter
        }
    }

    private fun initListeners() {
        binding.fbAdd.setOnClickListener {
            navigateToDetailActivity(null)
        }
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                notesViewModel.notesState.collect { notesState ->
                    when (notesState) {
                        NotesState.Loading -> onLoading()
                        NotesState.Complete -> onComplete()
                        is NotesState.Success -> onSuccess(notesState.notes)
                        is NotesState.Error -> onError(notesState.error)
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                menuManager.isSelectedMenu.collect { isSelectedMenu ->
                    if (isSelectedMenu) {
                        notesAdapter.resetItemSelectList()
                        notesViewModel.getNotes()
                        menuManager.availableSelectorMenu(false)
                    }
                }
            }
        }
    }

    private fun onLoading() {
        binding.swipe.isRefreshing = true
    }

    private fun onComplete() {
        binding.swipe.isRefreshing = false
    }

    private fun onSuccess(notes: List<NotesModel>?) {
        binding.swipe.isRefreshing = false
        if (!notes.isNullOrEmpty()) {
            val filterNotes = notes.filter { notesModel ->
                notesModel.isArchived == false && notesModel.isDeleted == false
            }
            binding.layoutStateNotes.isVisible = filterNotes.isEmpty()
            notesAdapter.updateList(filterNotes)
        } else {
            binding.layoutStateNotes.isVisible = true
        }
    }

    private fun onError(errorMessage: String) {
        binding.swipe.isRefreshing = false
        DialogError(requireContext(), errorMessage.toInt(), onClickButtonError = {})
    }

    private fun navigateToDetailActivity(idNote: String?) {
        findNavController().navigate(
            NotesFragmentDirections.actionNotesFragmentToDetailActivity(
                idNote, FragmentType.NotesFragment
            )
        )
    }
}