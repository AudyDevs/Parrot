package com.example.parrot.ui.activities.home.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.parrot.R
import com.example.parrot.core.type.ErrorType
import com.example.parrot.core.type.FragmentType
import com.example.parrot.databinding.ActivityHomeBinding
import com.example.parrot.domain.state.LoginState
import com.example.parrot.domain.state.NotesState
import com.example.parrot.ui.activities.home.manager.MenuManager
import com.example.parrot.ui.activities.home.viewmodel.HomeViewModel
import com.example.parrot.ui.activities.login.view.LoginActivity
import com.example.parrot.ui.dialogs.DialogDelete
import com.example.parrot.ui.dialogs.DialogError
import com.example.parrot.ui.dialogs.DialogLogout
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    @Inject
    lateinit var menuManager: MenuManager

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private val homeViewModel by viewModels<HomeViewModel>()
    private var isArchived = false
    private var isDeleted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        initUI()
    }

    override fun onResume() {
        super.onResume()
        onReset()
    }

    private fun initUI() {
        initNavigation()
        initListeners()
        initUIState()
    }

    private fun initNavigation() {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHost.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    private fun initListeners() {
        binding.btnArchive.setOnClickListener {
            val listNotesId = menuManager.listNotesSelected.value
            val isArchived = isArchived
            saveChangesNoteArchived(listNotesId, isArchived)
            val snackBarText = if (isArchived) {
                getString(R.string.snackBarTextArchived)
            } else {
                getString(R.string.snackBarTextUnArchived)
            }
            Snackbar.make(binding.fragmentContainerView, snackBarText, Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.snackBarTextUndo)) {
                    menuManager.setListMenu(listNotesId)
                    saveChangesNoteArchived(listNotesId, !isArchived)
                }
                .show()
        }
        binding.btnDelete.setOnClickListener {
            val listNotesId = menuManager.listNotesSelected.value
            val isDeleted = isDeleted
            saveChangesNoteDeleted(listNotesId, isDeleted)
            val snackBarText = if (isDeleted) {
                getString(R.string.snackBarTextDelete)
            } else {
                getString(R.string.snackBarTextRestore)
            }
            Snackbar.make(binding.fragmentContainerView, snackBarText, Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.snackBarTextUndo)) {
                    menuManager.setListMenu(listNotesId)
                    saveChangesNoteDeleted(listNotesId, !isDeleted)
                }
                .show()
        }
        binding.btnDeleteForever.setOnClickListener {
            deleteNote()
        }
        binding.btnLogout.setOnClickListener {
            showDialogLogout()
        }
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                menuManager.menuOnClickItem.collect { fragmentType ->
                    when (fragmentType) {
                        FragmentType.NotesFragment -> {
                            isArchived = true
                            isDeleted = true
                            binding.btnArchive.setImageResource(R.drawable.ic_archived)
                            binding.btnDelete.setImageResource(R.drawable.ic_deleted)
                            binding.btnArchive.isVisible = true
                            binding.btnDelete.isVisible = true
                            binding.btnDeleteForever.isVisible = false
                        }

                        FragmentType.ArchivedFragment -> {
                            isArchived = false
                            isDeleted = true
                            binding.btnArchive.setImageResource(R.drawable.ic_unarchived)
                            binding.btnDelete.setImageResource(R.drawable.ic_deleted)
                            binding.btnArchive.isVisible = true
                            binding.btnDelete.isVisible = true
                            binding.btnDeleteForever.isVisible = false
                        }

                        FragmentType.DeletedFragment -> {
                            isArchived = false
                            isDeleted = false
                            binding.btnDelete.setImageResource(R.drawable.ic_restore)
                            binding.btnArchive.isVisible = false
                            binding.btnDelete.isVisible = true
                            binding.btnDeleteForever.isVisible = true
                        }

                        null -> {
                            isArchived = false
                            isDeleted = false
                            binding.btnArchive.tag = null
                            binding.btnDelete.tag = null
                            binding.btnArchive.isVisible = false
                            binding.btnDelete.isVisible = false
                            binding.btnDeleteForever.isVisible = false
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.loginState.collect { loginState ->
                    when (loginState) {
                        LoginState.Loading -> onLoading()
                        LoginState.Success -> onSuccess()
                        is LoginState.Error -> onError()
                        null -> {}
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.notesState.collect { notesState ->
                    when (notesState) {
                        NotesState.Loading -> onLoading()
                        is NotesState.Success -> onSuccess()
                        NotesState.Complete -> onComplete()
                        is NotesState.Error -> onError()
                    }
                }
            }
        }
    }

    private fun onLoading() {
        binding.progressBar.isVisible = true
    }

    private fun onSuccess() {
        binding.progressBar.isVisible = false
        navigateToLoginActivity()
    }

    private fun onComplete() {
        binding.progressBar.isVisible = false
        menuManager.availableSelectorMenu(true)
        menuManager.resetMenu()
    }

    private fun onError() {
        binding.progressBar.isVisible = false
        DialogError(this, ErrorType.LogOut.errorMessage, onClickButtonError = {
            homeViewModel.resetLoginState()
        })
    }

    private fun onReset() {
        binding.progressBar.isVisible = false
    }

    private fun showDialogLogout() {
        DialogLogout(
            binding.main,
            onSelectedButton = { isSelected ->
                if (isSelected) {
                    homeViewModel.logOutUser()
                }
            })
    }

    private fun saveChangesNoteArchived(listNotesId: MutableList<String>?, isArchived: Boolean) {
        if (!listNotesId.isNullOrEmpty()) {
            val mapUpdate = mapOf("isArchived" to isArchived)
            homeViewModel.multiUpdateNote(listNotesId, mapUpdate)
        }
    }

    private fun saveChangesNoteDeleted(listNotesId: MutableList<String>?, isDeleted: Boolean) {
        if (!listNotesId.isNullOrEmpty()) {
            val mapUpdate = mapOf("isDeleted" to isDeleted)
            homeViewModel.multiUpdateNote(listNotesId, mapUpdate)
        }
    }

    private fun deleteNote() {
        DialogDelete(
            binding.main,
            multiDelete = true,
            onSelectedButton = { isSelected ->
                if (isSelected) {
                    if (!menuManager.listNotesSelected.value.isNullOrEmpty()) {
                        homeViewModel.multiDeleteNote(menuManager.listNotesSelected.value)
                    }
                    Snackbar.make(
                        binding.fragmentContainerView,
                        getString(R.string.snackBarTextDeleteForever),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            })
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}