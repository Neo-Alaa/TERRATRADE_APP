package com.example.landrenting

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.landrenting.models.LandModel
import com.example.landrenting.models.User

class LandDetailsActivity : AppCompatActivity() {

    private lateinit var land: LandModel
    private lateinit var owner: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_land_details)

        // Retrieve data from Intent
        land = intent.getParcelableExtra("LAND_DATA") ?: return
        owner = intent.getParcelableExtra("OWNER_DATA") ?: return

        // Find views
        val landImage: ImageView = findViewById(R.id.land_detail_image)
        val landTitle: TextView = findViewById(R.id.land_detail_title)
        val landLocation: TextView = findViewById(R.id.land_detail_location)
        val landPrice: TextView = findViewById(R.id.land_detail_price)
        val landCategory: TextView = findViewById(R.id.land_detail_category)
        val ownerImage: ImageView = findViewById(R.id.owner_image)
        val ownerName: TextView = findViewById(R.id.owner_name)
        val ownerPhone: TextView = findViewById(R.id.owner_phone)
        val ownerEmail: TextView = findViewById(R.id.owner_email)
        val callButton: Button = findViewById(R.id.button_call)
        val whatsappButton: Button = findViewById(R.id.button_whatsapp)
        val appointmentButton: Button = findViewById(R.id.button_appointment)

        // Populate views
        try {
            if (land.imageUris.isNotEmpty()) {
                try {
                    // Try to parse as resource ID first
                    val resourceId = land.imageUris[0].toInt()
                    landImage.setImageResource(resourceId)
                } catch (e: NumberFormatException) {
                    // If not a resource ID, try to load as URI
                    val uri = Uri.parse(land.imageUris[0])
                    landImage.setImageURI(uri)
                }
            } else {
                landImage.setImageResource(R.drawable.recimagetest)
            }
        } catch (e: Exception) {
            landImage.setImageResource(R.drawable.recimagetest)
            e.printStackTrace()
        }
        
        landTitle.text = land.landName
        landLocation.text = land.location
        landPrice.text = String.format("%,.2f DH", land.price)
        landCategory.text = land.category.uppercase()
        ownerImage.setImageResource(owner.imageRes)
        ownerName.text = owner.fullName
        ownerPhone.text = owner.phone
        ownerEmail.text = owner.email

        // --- Button Click Listeners ---

        // Call Button
        callButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${owner.phone}")
            }
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "No phone app found", Toast.LENGTH_SHORT).show()
            }
        }

        // WhatsApp Button
        whatsappButton.setOnClickListener {
            val phoneNumber = owner.phone.replace("[^0-9]".toRegex(), "") // Clean phone number
            val whatsappIntent = Intent(Intent.ACTION_VIEW)
            val url = "https://api.whatsapp.com/send?phone=$phoneNumber"

            whatsappIntent.data = Uri.parse(url)
            // Check if WhatsApp is installed before launching
            if (isAppInstalled("com.whatsapp")) {
                 try {
                     startActivity(whatsappIntent)
                 } catch (e: ActivityNotFoundException) {
                     // Should not happen if check passes, but good practice
                     Toast.makeText(this, "Error opening WhatsApp", Toast.LENGTH_SHORT).show()
                 }
            } else {
                 Toast.makeText(this, "WhatsApp is not installed", Toast.LENGTH_SHORT).show()
                 // Optionally, redirect to Play Store
                 // val playStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.whatsapp"))
                 // startActivity(playStoreIntent)
            }
        }


        // Appointment Button (Placeholder)
        appointmentButton.setOnClickListener {
            // No logic yet, just show a toast
            Toast.makeText(this, "Appointment feature coming soon!", Toast.LENGTH_SHORT).show()
            // You could start another activity or show a dialog here later
        }
    }

    // Helper function to check if an app is installed
    private fun isAppInstalled(packageName: String): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}
