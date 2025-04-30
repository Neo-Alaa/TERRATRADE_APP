package com.example.landrenting.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.landrenting.R
import com.example.landrenting.Welcome
import com.example.landrenting.admin.AdminDashboardActivity
import com.example.landrenting.home.Home
import com.example.landrenting.utils.UserManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var signInButton: MaterialButton
    private lateinit var createAccountButton: MaterialButton
    private lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main)
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        )

        userManager = UserManager.getInstance(this)
        
        // Initialize views
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        emailInputLayout = findViewById(R.id.emailInputLayout)
        passwordInputLayout = findViewById(R.id.passwordInputLayout)
        signInButton = findViewById(R.id.signInButton)
        createAccountButton = findViewById(R.id.createAccountButton)

        signInButton.setOnClickListener {
            if (validateInputs()) {
                val email = emailInput.text.toString()
                val password = passwordInput.text.toString()

                if (userManager.validateCredentials(email, password)) {
                    val user = userManager.getUser(email)
                    if (user != null) {
                        userManager.setCurrentUser(user)
                        
                        // Check if user is admin and redirect accordingly
                        if (user.isAdmin) {
                            startActivity(Intent(this, AdminDashboardActivity::class.java))
                        } else {
                            startActivity(Intent(this, Home::class.java))
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        finish()
                    }
                } else {
                    Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }

        createAccountButton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        // Login with number
        findViewById<TextView>(R.id.textView2).setOnClickListener {
            startActivity(Intent(this, NumberLogin::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        // Email validation
        val email = emailInput.text.toString()
        if (email.isEmpty()) {
            emailInputLayout.error = "Email is required"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInputLayout.error = "Invalid email format"
            isValid = false
        } else {
            emailInputLayout.error = null
        }

        // Password validation
        val password = passwordInput.text.toString()
        if (password.isEmpty()) {
            passwordInputLayout.error = "Password is required"
            isValid = false
        } else if (password.length < 6) {
            passwordInputLayout.error = "Password must be at least 6 characters"
            isValid = false
        } else {
            passwordInputLayout.error = null
        }

        return isValid
    }
}
