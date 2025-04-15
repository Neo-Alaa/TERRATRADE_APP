package com.example.landrenting

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
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
        var phoneNumber = findViewById<EditText>(R.id.phoneEditText)

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


        // Hada Code dyal dik Background li katdar 3la lbuttons tal mn be3da wn3awd n9adha hssn

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

        // Spinner Hna

        val spinner: Spinner = findViewById(R.id.mySpinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.planets_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, Welcome::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, 5000)
    }
}