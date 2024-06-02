package com.example.parrot.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.parrot.R
import com.example.parrot.core.validates.ValidateEmail
import com.example.parrot.core.validates.ValidatePassword
import com.example.parrot.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

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

    private fun initUI() {
        initListeners()
    }

    private fun initListeners() {
        binding.textEmail.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.layoutEmail.helperText =
                    ValidateEmail.validEmail(this, binding.textEmail.text.toString())
            }
        }
        binding.layoutEmail.setEndIconOnClickListener {
            clearEmail()
        }
        binding.textPassword.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.layoutPassword.helperText =
                    ValidatePassword.validPassword(this, binding.textPassword.text.toString())
            }
        }

//        binding.btnOK.setOnClickListener {
//            navigateToMainActivity()
//        }
//        binding.btnCancel.setOnClickListener {
//            navigateToLoginActivity()
//        }
    }

    private fun clearEmail() {
        binding.textEmail.setText("")
        binding.layoutEmail.helperText = ContextCompat.getString(this, R.string.required)
    }

    private fun navigateToMainActivity() {
        onBackPressedDispatcher.onBackPressed()
    }

    private fun navigateToLoginActivity() {
        onBackPressedDispatcher.onBackPressed()
    }
}