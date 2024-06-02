package com.example.parrot.ui.activities.login.view

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.parrot.R
import com.example.parrot.core.validates.ValidateEmail
import com.example.parrot.core.validates.ValidateEmail.validEmail
import com.example.parrot.core.validates.ValidatePassword.validPassword
import com.example.parrot.databinding.ActivityLoginBinding
import com.example.parrot.ui.activities.MainActivity
import com.example.parrot.ui.activities.RegisterActivity
import com.example.parrot.ui.activities.login.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition { !loginViewModel.isReady.value }
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

    private fun initUI() {
        initListeners()
    }

    private fun initListeners() {
//        binding.btnAccess.setOnClickListener {
        // si no funciona poner un helptext en los textos para decir que no hay registro para entrar. o un dialog
//            navigateToMainActivity()
//        }
//        binding.btnRegister.setOnClickListener {
//            navigateToRegisterActivity()
//        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}