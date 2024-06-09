package com.example.parrot.ui.activities.detail.view

import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navArgs
import com.example.parrot.R
import com.example.parrot.databinding.ActivityDetailBinding
import com.example.parrot.domain.model.NotesModel
import com.example.parrot.domain.state.NotesState
import com.example.parrot.ui.activities.detail.viewmodel.DetailViewModel
import com.example.parrot.ui.dialogs.DialogError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel>()
    private val args: DetailActivityArgs by navArgs()
    private var idNote: String? = null
    private var isArchived = false
    private var isDeleted = false
    private var color = -1
    private var userId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initUI()
        idNote = args.idNote
    }

    private fun initUI() {
        initListeners()
        initUIState()
    }

    override fun onResume() {
        super.onResume()
        initViewModel()
    }

    private fun initViewModel() {
        detailViewModel.noteId = idNote
        detailViewModel.getNoteId()
    }

    private fun initListeners() {
        binding.btnArchive.setOnClickListener {
            isArchived = !isArchived
            saveChangesNoteModel()
        }
        binding.btnColor.setOnClickListener {
            Toast.makeText(this, "Color", Toast.LENGTH_LONG).show()
//            color =
        }
        binding.btnDelete.setOnClickListener {
            isDeleted = !isDeleted
            saveChangesNoteModel()
        }
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.fbAdd.setOnClickListener {
            saveChangesNoteModel()
        }
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailViewModel.notesState.collect { notesState ->
                    when (notesState) {
                        NotesState.Loading -> onLoading()
                        NotesState.Complete -> onComplete()
                        is NotesState.Success -> onSuccess(notesState.notes.first())
                        is NotesState.Error -> onError(notesState.error)
                    }
                }
            }
        }
    }

    private fun onLoading() {
        if (!idNote.isNullOrEmpty()) {
            binding.progressBar.isVisible = true
        }
    }


    private fun onComplete() {
        binding.progressBar.isVisible = false
        onBackPressedDispatcher.onBackPressed()
    }

    private fun onSuccess(note: NotesModel) {
        binding.progressBar.isVisible = false
        updateUI(note)
        changeTextUI(note)
        changeColorUI(note.backcolor ?: -1)
    }

    private fun updateUI(note: NotesModel) {
        userId = note.userId ?: ""
        isArchived = note.isArchived ?: false
        isDeleted = note.isDeleted ?: false
        color = note.backcolor ?: -1
    }

    private fun changeTextUI(note: NotesModel) {
        binding.tvTitleNote.setText(note.title)
        binding.tvBodyNote.setText(note.body)
    }

    private fun changeColorUI(color: Int) {
        binding.apply {
            if (color != -1) {
                main.setBackgroundColor(color)
                toolbar.setBackgroundColor(color)
                btnBack.imageTintList = ColorStateList.valueOf(getColor(R.color.onPrimary))
                btnBack.setBackgroundColor(color)
                btnColor.imageTintList = ColorStateList.valueOf(getColor(R.color.onPrimary))
                btnColor.setBackgroundColor(color)
                btnArchive.imageTintList = ColorStateList.valueOf(getColor(R.color.onPrimary))
                btnArchive.setBackgroundColor(color)
                btnDelete.imageTintList = ColorStateList.valueOf(getColor(R.color.onPrimary))
                btnDelete.setBackgroundColor(color)
                fbAdd.imageTintList = ColorStateList.valueOf(color)
                fbAdd.backgroundTintList = ColorStateList.valueOf(getColor(R.color.onPrimary))
                tvTitleNote.setTextColor(getColor(R.color.onPrimary))
                tvTitleNote.setBackgroundColor(color)
                tvTitleNote.setHintTextColor(getColor(R.color.onPrimary))
                tvBodyNote.setTextColor(getColor(R.color.onPrimary))
                tvBodyNote.setBackgroundColor(color)
                tvBodyNote.setHintTextColor(getColor(R.color.onPrimary))
            } else {
                main.setBackgroundColor(getColor(R.color.background))
                toolbar.setBackgroundColor(getColor(R.color.background))
                btnBack.imageTintList = ColorStateList.valueOf(getColor(R.color.primary))
                btnBack.setBackgroundColor(getColor(R.color.onPrimary))
                btnColor.imageTintList = ColorStateList.valueOf(getColor(R.color.primary))
                btnColor.setBackgroundColor(getColor(R.color.onPrimary))
                btnArchive.imageTintList = ColorStateList.valueOf(getColor(R.color.primary))
                btnArchive.setBackgroundColor(getColor(R.color.onPrimary))
                btnDelete.imageTintList = ColorStateList.valueOf(getColor(R.color.primary))
                btnDelete.setBackgroundColor(getColor(R.color.onPrimary))
                fbAdd.imageTintList = ColorStateList.valueOf(getColor(R.color.onPrimary))
                fbAdd.backgroundTintList = ColorStateList.valueOf(getColor(R.color.primary))
                tvTitleNote.setTextColor(getColor(R.color.primary))
                tvTitleNote.setBackgroundColor(getColor(R.color.background))
                tvTitleNote.setHintTextColor(getColor(R.color.disable))
                tvBodyNote.setTextColor(getColor(R.color.primary))
                tvBodyNote.setBackgroundColor(getColor(R.color.background))
                tvBodyNote.setHintTextColor(getColor(R.color.disable))
            }
        }
    }

    private fun onError(errorMessage: String) {
        binding.progressBar.isVisible = false
        if (errorMessage.isNotEmpty()) {
            DialogError(this, errorMessage.toInt(), onClickButtonError = {})
        }
    }

    private fun saveChangesNoteModel() {
        val notesModel = NotesModel(
            id = idNote,
            userId = userId,
            title = binding.tvTitleNote.text.toString(),
            body = binding.tvBodyNote.text.toString(),
            isArchived = isArchived,
            isDeleted = isDeleted,
            backcolor = color
        )
        if (idNote.isNullOrEmpty()) {
            detailViewModel.saveNote(notesModel)
        } else {
            detailViewModel.updateNote(notesModel)
        }
        onBackPressedDispatcher.onBackPressed()
    }
}