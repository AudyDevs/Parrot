package com.example.parrot.ui.activities.register.view

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
import com.example.parrot.R
import com.example.parrot.core.type.ErrorType
import com.example.parrot.core.validates.ValidateEmail.validEmail
import com.example.parrot.core.validates.ValidatePassword.validPassword
import com.example.parrot.core.validates.ValidatePhone.validPhone
import com.example.parrot.core.validates.ValidateRepeatPassword.validRepeatPassword
import com.example.parrot.databinding.ActivityRegisterBinding
import com.example.parrot.domain.state.LoginState
import com.example.parrot.ui.activities.register.viewmodel.RegisterViewModel
import com.example.parrot.ui.dialogs.DialogError
import com.example.parrot.ui.dialogs.DialogSuccess
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
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

    private fun initUI() {
        initListeners()
        initUIState()
    }

    private fun initListeners() {
        binding.textPhone.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.layoutPhone.helperText =
                    validPhone(this, binding.textPhone.text.toString())
            }
        }
        binding.textEmail.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.layoutEmail.helperText =
                    validEmail(this, binding.textEmail.text.toString())
            }
        }
        binding.layoutEmail.setEndIconOnClickListener {
            clearEmail()
        }
        binding.textPassword.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.layoutPassword.helperText =
                    validPassword(this, binding.textPassword.text.toString())
            }
        }
        binding.textRepeatPassword.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.layoutRepeatPassword.helperText =
                    validRepeatPassword(
                        this,
                        binding.textPassword.text.toString(),
                        binding.textRepeatPassword.text.toString()
                    )
            }
        }
        binding.btnRegister.setOnClickListener {
            if (
                binding.layoutNickName.helperText.isNullOrEmpty() &&
                binding.layoutPhone.helperText.isNullOrEmpty() &&
                binding.layoutEmail.helperText.isNullOrEmpty() &&
                binding.layoutPassword.helperText.isNullOrEmpty() &&
                binding.layoutRepeatPassword.helperText.isNullOrEmpty()
            ) {
                registerViewModel.email = binding.textEmail.text.toString()
                registerViewModel.password = binding.textPassword.text.toString()
                registerViewModel.signUpUser()
            }
        }
    }

    private fun clearEmail() {
        binding.textEmail.text?.clear()
        binding.layoutEmail.helperText = ContextCompat.getString(this, R.string.required)
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                registerViewModel.loginState.collect { loginState ->
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
        DialogSuccess(this, onClickButtonSuccess = {
            navigateToLoginActivity()
        })
    }

    private fun onError() {
        binding.progressBar.isVisible = false
        DialogError(this, ErrorType.SignUp, onClickButtonError = {
            registerViewModel.resetLoginState()
        })
    }

    private fun onReset() {
        binding.progressBar.isVisible = false
    }

    private fun navigateToLoginActivity() {
        onBackPressedDispatcher.onBackPressed()
    }
}