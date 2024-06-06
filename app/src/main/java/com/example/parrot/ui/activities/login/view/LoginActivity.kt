package com.example.parrot.ui.activities.login.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.parrot.R
import com.example.parrot.core.type.ErrorType
import com.example.parrot.core.type.ProviderType
import com.example.parrot.databinding.ActivityLoginBinding
import com.example.parrot.domain.state.LoginState
import com.example.parrot.domain.state.SplashState
import com.example.parrot.ui.activities.home.view.HomeActivity
import com.example.parrot.ui.activities.login.viewmodel.LoginViewModel
import com.example.parrot.ui.activities.register.view.RegisterActivity
import com.example.parrot.ui.dialogs.DialogError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                when (loginViewModel.splashState.value) {
                    SplashState.Loading -> true
                    SplashState.NewUser -> false
                    SplashState.RegisteredUser -> false
                }
            }
        }
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initUI()
    }

    override fun onResume() {
        super.onResume()
        onReset()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loginViewModel.signUpWithGoogle(requestCode, resultCode, data)
    }

    private fun initUI() {
        initListeners()
        initUIState()
    }

    private fun initListeners() {
        binding.btnSignIn.setOnClickListener {
            if (binding.textEmail.text.toString()
                    .isNotEmpty() && binding.textPassword.text.toString().isNotEmpty()
            ) {
                loginViewModel.email = binding.textEmail.text.toString()
                loginViewModel.password = binding.textPassword.text.toString()
                loginViewModel.provider = ProviderType.Email
                loginViewModel.loginUserWithEmail()
            }
        }
        binding.btnSignInGoogle.setOnClickListener {
            loginViewModel.provider = ProviderType.Google
            loginViewModel.loginUserWithGoogle(this)
        }
        binding.btnSignInFacebook.setOnClickListener {
            loginViewModel.provider = ProviderType.Facebook
            loginViewModel.loginUserWithFacebook()
        }
        binding.tvSignUp.setOnClickListener {
            navigateToRegisterActivity()
        }
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.splashState.collect { splashState ->
                    when (splashState) {
                        SplashState.Loading -> onLoading()
                        SplashState.NewUser -> onReset()
                        SplashState.RegisteredUser -> navigateToHomeActivity()
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.loginState.collect { loginState ->
                    when (loginState) {
                        LoginState.Loading -> onLoading()
                        is LoginState.Success -> onSuccess()
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
        navigateToHomeActivity()
    }

    private fun onError() {
        binding.progressBar.isVisible = false
        DialogError(this, ErrorType.SignIn, onClickButtonError = {
            loginViewModel.resetLoginState()
        })
    }

    private fun onReset() {
        binding.progressBar.isVisible = false
        binding.textEmail.text?.clear()
        binding.textPassword.text?.clear()
    }

    private fun navigateToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}