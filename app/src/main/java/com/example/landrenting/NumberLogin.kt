package com.example.landrenting

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class NumberLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        setContentView(R.layout.activity_number_login)

        val drawable = ContextCompat.getDrawable(this, R.drawable.testiiing)

        var btnZero = findViewById<Button>(R.id.zero)
        var btnOne = findViewById<Button>(R.id.one)
        var btnTwo = findViewById<Button>(R.id.two)
        var btnThree = findViewById<Button>(R.id.three)
        var btnFour = findViewById<Button>(R.id.four)
        var btnFive = findViewById<Button>(R.id.five)
        var btnSix = findViewById<Button>(R.id.six)
        var btnSeven = findViewById<Button>(R.id.seven)
        var btnEight = findViewById<Button>(R.id.eight)
        var btnNine = findViewById<Button>(R.id.nine)
        var b = findViewById<Button>(R.id.b)
        var a = findViewById<Button>(R.id.a)

        val btnList = listOf<Button>(
            btnZero,
            btnOne,
            btnTwo,
            btnThree,
            btnFour,
            btnFive,
            btnSix,
            btnSeven,
            btnEight,
            btnNine,
            a,
            b
        )


        var colorApp = getColor(R.color.appColor)


        findViewById<EditText>(R.id.phoneEditText).apply {
            isFocusableInTouchMode = true  // Allows touch focus
            showSoftInputOnFocus = false   // Blocks system keyboard
        }

       btnList.forEach {
           it.apply {
               setOnClickListener {
                       setBackgroundDrawable(drawable)
                       setTextColor(Color.WHITE)
//                       overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                   Handler(Looper.getMainLooper()).postDelayed({
                       setBackgroundColor(Color.WHITE)
                       setTextColor(colorApp)
                   }, 70)

               }
           }
       }

    }
}