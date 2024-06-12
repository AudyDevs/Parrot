package com.example.parrot.ui.activities.detail.view

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.parrot.R
import com.example.parrot.core.type.FragmentType
import com.example.parrot.databinding.ActivityDetailBinding
import com.example.parrot.domain.model.ColorModel
import com.example.parrot.domain.model.INT_NULL
import com.example.parrot.domain.model.NotesModel
import com.example.parrot.domain.state.NotesState
import com.example.parrot.ui.activities.detail.viewmodel.DetailViewModel
import com.example.parrot.ui.dialogs.DialogDelete
import com.example.parrot.ui.dialogs.DialogError
import com.example.parrot.ui.dialogs.adapter.ColorAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var colorAdapter: ColorAdapter
    private lateinit var dialog: BottomSheetDialog
    private lateinit var recyclerview: RecyclerView
    private val detailViewModel by viewModels<DetailViewModel>()
    private val args: DetailActivityArgs by navArgs()
    private var idNote: String? = null
    private lateinit var fragmentType: FragmentType
    private var userId: String? = null
    private var color: Int = INT_NULL
    private var isArchived: Boolean = false
    private var isDeleted: Boolean = false

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
        idNote = args.idNote
        fragmentType = args.fragmentType
        initUI()
    }

    private fun initUI() {
        initButtonsUI()
        initListeners()
        initUIState()
        initColorUI()
    }

    override fun onResume() {
        super.onResume()
        initViewModel()
    }

    private fun initButtonsUI() {
        if (idNote != null) {
            when (fragmentType) {
                FragmentType.NotesFragment -> {
                    binding.btnArchive.setImageResource(R.drawable.ic_archived)
                    binding.btnDelete.setImageResource(R.drawable.ic_deleted)
                    binding.btnArchive.isVisible = true
                    binding.btnDelete.isVisible = true
                    binding.btnDeleteForever.isVisible = false
                }

                FragmentType.ArchivedFragment -> {
                    binding.btnArchive.setImageResource(R.drawable.ic_unarchived)
                    binding.btnDelete.setImageResource(R.drawable.ic_deleted)
                    binding.btnArchive.isVisible = true
                    binding.btnDelete.isVisible = true
                    binding.btnDeleteForever.isVisible = false
                }

                FragmentType.DeletedFragment -> {
                    binding.btnDelete.setImageResource(R.drawable.ic_restore)
                    binding.btnArchive.isVisible = false
                    binding.btnDelete.isVisible = true
                    binding.btnDeleteForever.isVisible = true
                }
            }
        } else {
            binding.btnArchive.isVisible = false
            binding.btnDelete.isVisible = false
            binding.btnDeleteForever.isVisible = false
        }
    }

    private fun initViewModel() {
        detailViewModel.noteId = idNote
        detailViewModel.getNoteId()
    }

    private fun initListeners() {
        binding.btnArchive.setOnClickListener {
            isArchived = !isArchived
            saveChangesNote()
        }
        binding.btnColor.setOnClickListener {
            showBottomSheet()
        }
        binding.btnDelete.setOnClickListener {
            isDeleted = !isDeleted
            saveChangesNote()
        }
        binding.btnBack.setOnClickListener {
            if (binding.tvTitleNote.text.toString().isNotEmpty()) {
                saveChangesNote()
            } else {
                onBackPressedDispatcher.onBackPressed()
            }
        }
        binding.fbAdd.setOnClickListener {
            saveChangesNote()
        }
        binding.btnDeleteForever.setOnClickListener {
            deleteNote()
        }
    }

    private fun initColorUI() {
        binding.fbAdd.imageTintList = ColorStateList.valueOf(getColor(R.color.background))
        binding.fbAdd.backgroundTintList = ColorStateList.valueOf(getColor(R.color.primary))
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
        changeColorUI()
    }

    private fun updateUI(note: NotesModel) {
        userId = note.userId!!
        isArchived = note.isArchived!!
        isDeleted = note.isDeleted!!
        color = note.backcolor!!
        binding.tvTitleNote.setText(note.title)
        binding.tvBodyNote.setText(note.body)
    }

    private fun changeColorUI() {
        var textColor: Int
        var backColor: Int
        var hintColor: Int
        binding.apply {
            if (color != INT_NULL) {
                textColor = getColor(R.color.textWhite)
                backColor = ContextCompat.getColor(this@DetailActivity, color)
                hintColor = getColor(R.color.disable)
            } else {
                textColor = getColor(R.color.primary)
                backColor = getColor(R.color.background)
                hintColor = getColor(R.color.disable)
            }
            main.setBackgroundColor(backColor)
            toolbar.setBackgroundColor(backColor)
            btnBack.imageTintList = ColorStateList.valueOf(textColor)
            btnBack.setBackgroundColor(backColor)
            btnColor.imageTintList = ColorStateList.valueOf(textColor)
            btnColor.setBackgroundColor(backColor)
            btnArchive.imageTintList = ColorStateList.valueOf(textColor)
            btnArchive.setBackgroundColor(backColor)
            btnDelete.imageTintList = ColorStateList.valueOf(textColor)
            btnDelete.setBackgroundColor(backColor)
            btnDeleteForever.imageTintList = ColorStateList.valueOf(textColor)
            btnDeleteForever.setBackgroundColor(backColor)
            fbAdd.imageTintList = ColorStateList.valueOf(backColor)
            fbAdd.backgroundTintList = ColorStateList.valueOf(textColor)
            tvTitleNote.setTextColor(textColor)
            tvTitleNote.backgroundTintList = ColorStateList.valueOf(backColor)
            tvTitleNote.setHintTextColor(hintColor)
            tvBodyNote.setTextColor(textColor)
            tvBodyNote.backgroundTintList = ColorStateList.valueOf(backColor)
            tvBodyNote.setHintTextColor(hintColor)
        }
    }

    private fun onError(errorMessage: String) {
        binding.progressBar.isVisible = false
        if (errorMessage.isNotEmpty()) {
            DialogError(this, errorMessage.toInt(), onClickButtonError = {})
        }
    }

    private fun showBottomSheet() {
        val dialogView = layoutInflater.inflate(R.layout.bottom_sheet, null)
        if (color != INT_NULL) {
            dialogView.setBackgroundColor(ContextCompat.getColor(this@DetailActivity, color))
        } else {
            dialogView.setBackgroundColor(
                ContextCompat.getColor(
                    this@DetailActivity,
                    R.color.primary
                )
            )
        }
        dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        dialog.setContentView(dialogView)
        recyclerview = dialogView.findViewById(R.id.rvColors)
        colorAdapter = ColorAdapter(listOf(
            ColorModel.Blank,
            ColorModel.Red,
            ColorModel.Orange,
            ColorModel.Yellow,
            ColorModel.Green,
            ColorModel.Cyan,
            ColorModel.LightBlue,
            ColorModel.Blue,
            ColorModel.Purple,
            ColorModel.Rose,
            ColorModel.Brown,
            ColorModel.Grey,
        ),
            onItemSelected = { colorModel ->
                color = colorModel.color
                changeColorUI()
                dialog.dismiss()
            })
        recyclerview.adapter = colorAdapter
        dialog.show()
    }

    private fun saveChangesNote() {
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

    private fun deleteNote() {
        DialogDelete(
            binding.main,
            multiDelete = false,
            onSelectedButton = { isSelected ->
                if (isSelected) {
                    detailViewModel.deleteNote()
                }
            })
    }
}