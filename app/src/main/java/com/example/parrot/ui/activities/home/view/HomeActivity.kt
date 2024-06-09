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
import com.example.parrot.databinding.ActivityHomeBinding
import com.example.parrot.domain.state.LoginState
import com.example.parrot.ui.activities.home.viewmodel.HomeViewModel
import com.example.parrot.ui.activities.login.view.LoginActivity
import com.example.parrot.ui.dialogs.DialogError
import com.example.parrot.ui.dialogs.DialogLogout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private val homeViewModel by viewModels<HomeViewModel>()

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
        binding.btnLogout.setOnClickListener {
            showDialogLogout()
        }
    }

    private fun initUIState() {
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
    }

    private fun onLoading() {
        binding.progressBar.isVisible = true
    }

    private fun onSuccess() {
        binding.progressBar.isVisible = false
        navigateToLoginActivity()
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

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}