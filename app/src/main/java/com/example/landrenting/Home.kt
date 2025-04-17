package com.example.landrenting

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.landrenting.Adapter.LandAdapter
import com.example.landrenting.Model.LandModel

class Home : AppCompatActivity() {
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
        setContentView(R.layout.activity_home)

        // Initialize RecyclerView
        val propertiesRecycler: RecyclerView = findViewById(R.id.propertiesRecycler)
        val propertiesRecycler2: RecyclerView = findViewById(R.id.propertiesRecycler2)
        propertiesRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        propertiesRecycler2.layoutManager = LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL , false)

        // Create sample data
        val landList = listOf(
            LandModel(R.drawable.recimagetest, "New York City", "1800DH", "Alaa"),
            LandModel(R.drawable.recimagetest, "Los Angeles", "2200DH", "Mohamed"),
            LandModel(R.drawable.recimagetest, "Chicago", "1500DH", "Sarah"),
            LandModel(R.drawable.recimagetest, "Miami", "2500DH", "David"),
            LandModel(R.drawable.recimagetest, "Seattle", "1900DH", "Emma")
        )

        val landList2 = listOf(
            LandModel(R.drawable.testimage, "New York City", "1800DH", "Alaa"),
            LandModel(R.drawable.telech, "Los Angeles", "2200DH", "Mohamed"),
            LandModel(R.drawable.telech, "Chicago", "1500DH", "Sarah"),
            LandModel(R.drawable.recimagetest, "Miami", "2500DH", "David"),
            LandModel(R.drawable.testimage, "Seattle", "1900DH", "Emma")
        )

        // Set up adapter
        val adapter = LandAdapter(landList) { land ->
            // Handle item click here
            // You can open a detail activity or show a dialog
        }
        val adapter2 = LandAdapter(landList2) { land ->
            // Handle item click here
            // You can open a detail activity or show a dialog
        }

        propertiesRecycler.adapter = adapter
        propertiesRecycler2.adapter = adapter2
    }
}