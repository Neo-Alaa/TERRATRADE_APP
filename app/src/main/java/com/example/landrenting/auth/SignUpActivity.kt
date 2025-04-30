package com.example.landrenting.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.landrenting.R
import com.example.landrenting.models.User
import com.example.landrenting.utils.UserManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.UUID

class SignUpActivity : AppCompatActivity() {
    private lateinit var fullNameInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText
    private lateinit var phoneInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var confirmPasswordInput: TextInputEditText
    
    private lateinit var fullNameLayout: TextInputLayout
    private lateinit var emailLayout: TextInputLayout
    private lateinit var phoneLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var confirmPasswordLayout: TextInputLayout
    
    private lateinit var signUpButton: MaterialButton
    private lateinit var loginButton: MaterialButton

    private lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_sign_up)
        
        userManager = UserManager.getInstance(this)
        initializeViews()
        setupClickListeners()
    }

    private fun initializeViews() {
        fullNameInput = findViewById(R.id.fullNameInput)
        emailInput = findViewById(R.id.emailInput)
        phoneInput = findViewById(R.id.phoneInput)
        passwordInput = findViewById(R.id.passwordInput)
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput)
        
        fullNameLayout = findViewById(R.id.fullNameLayout)
        emailLayout = findViewById(R.id.emailLayout)
        phoneLayout = findViewById(R.id.phoneLayout)
        passwordLayout = findViewById(R.id.passwordLayout)
        confirmPasswordLayout = findViewById(R.id.confirmPasswordLayout)
        
        signUpButton = findViewById(R.id.signUpButton)
        loginButton = findViewById(R.id.loginButton)
    }

    private fun setupClickListeners() {
        signUpButton.setOnClickListener {
            if (validateInputs()) {
                val newUser = User(
                    id = UUID.randomUUID().toString(),
                    fullName = fullNameInput.text.toString(),
                    email = emailInput.text.toString(),
                    phone = phoneInput.text.toString(),
                    password = passwordInput.text.toString()
                )
                
                userManager.saveUser(newUser)
                userManager.setCurrentUser(newUser)
                
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()
                }, 125)
            }
        }

        loginButton.setOnClickListener {
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        // Validate full name
        if (fullNameInput.text.toString().isEmpty()) {
            fullNameLayout.error = "Full name is required"
            isValid = false
        } else {
            fullNameLayout.error = null
        }

        // Validate email
        val email = emailInput.text.toString()
        if (email.isEmpty()) {
            emailLayout.error = "Email is required"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.error = "Invalid email format"
            isValid = false
        } else {
            emailLayout.error = null
        }

        // Validate phone
        val phone = phoneInput.text.toString()
        if (phone.isEmpty()) {
            phoneLayout.error = "Phone number is required"
            isValid = false
        } else if (phone.length < 10) {
            phoneLayout.error = "Invalid phone number"
            isValid = false
        } else {
            phoneLayout.error = null
        }

        // Validate password
        val password = passwordInput.text.toString()
        if (password.isEmpty()) {
            passwordLayout.error = "Password is required"
            isValid = false
        } else if (password.length < 6) {
            passwordLayout.error = "Password must be at least 6 characters"
            isValid = false
        } else {
            passwordLayout.error = null
        }

        // Validate confirm password
        val confirmPassword = confirmPasswordInput.text.toString()
        if (confirmPassword.isEmpty()) {
            confirmPasswordLayout.error = "Please confirm password"
            isValid = false
        } else if (confirmPassword != password) {
            confirmPasswordLayout.error = "Passwords do not match"
            isValid = false
        } else {
            confirmPasswordLayout.error = null
        }

        return isValid
    }

    private fun saveUserData() {
        // We'll implement this when we add local storage
    }
}