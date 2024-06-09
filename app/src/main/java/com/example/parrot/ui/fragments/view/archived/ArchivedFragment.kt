package com.example.parrot.ui.fragments.view.archived

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
import androidx.recyclerview.widget.GridLayoutManager
import com.example.parrot.databinding.FragmentArchivedBinding
import com.example.parrot.domain.model.NotesModel
import com.example.parrot.domain.state.NotesState
import com.example.parrot.ui.dialogs.DialogError
import com.example.parrot.ui.fragments.viewmodel.NotesViewModel
import com.example.parrot.ui.fragments.adapter.NotesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArchivedFragment : Fragment() {

    private val notesViewModel by viewModels<NotesViewModel>()
    private lateinit var notesAdapter: NotesAdapter
    private var _binding: FragmentArchivedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArchivedBinding.inflate(layoutInflater, container, false)
        initUI()
        return binding.root
    }

    private fun initUI() {
        initAdapter()
        initUIState()
    }

    override fun onResume() {
        super.onResume()
        initViewModel()
    }

    private fun initViewModel() {
        notesViewModel.getNotes()
    }

    private fun initAdapter() {
        notesAdapter = NotesAdapter(onItemSelected = { noteModel ->
            navigateToDetailActivity(noteModel.id)
        })

        binding.rvNotes.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = notesAdapter
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
    }

    private fun onLoading() {
        binding.progressBar.isVisible = true
    }

    private fun onComplete() {
        binding.progressBar.isVisible = false
    }

    private fun onSuccess(notes: List<NotesModel>?) {
        binding.progressBar.isVisible = false
        if (!notes.isNullOrEmpty()) {
            val filterNotes = notes.filter { notesModel ->
                notesModel.isArchived == true && notesModel.isDeleted == false
            }
            notesAdapter.updateList(filterNotes)
        }
    }

    private fun onError(errorMessage: String) {
        binding.progressBar.isVisible = false
        DialogError(requireContext(), errorMessage.toInt(), onClickButtonError = {})
    }

    private fun navigateToDetailActivity(idNote: String?) {
        findNavController().navigate(
            ArchivedFragmentDirections.actionArchivedFragmentToDetailActivity(idNote)
        )
    }
}