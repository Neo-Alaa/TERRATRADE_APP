package com.example.landrenting

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.landrenting.auth.MainActivity
import com.example.landrenting.models.User
import com.example.landrenting.utils.UserManager
import java.util.UUID

class SplachScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splach_screen)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        )

        // Initialize admin user if not exists
        initializeAdminUser()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, 3000)
    }

    private fun initializeAdminUser() {
        val userManager = UserManager.getInstance(this)
        val adminEmail = "admin@terratrade.com"
        
        if (userManager.getUser(adminEmail) == null) {
            val adminUser = User(
                id = UUID.randomUUID().toString(),
                fullName = "Admin",
                email = adminEmail,
                phone = "123456789",
                password = "admin123",
                isAdmin = true
            )
            userManager.saveUser(adminUser)
        }
    }
}