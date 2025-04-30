package com.example.landrenting.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class LandModel(
    val id: String,
    val imageUris: List<String> = listOf(), // Changed from imageRes to support multiple images
    val location: String,
    val price: Double, // Changed from String to Double for proper formatting
    val landName: String,
    val ownerId: String,
    val ownerPhone: String, // Added contact information
    val description: String = "", // Detailed description
    val landSize: Double = 0.0, // Size in square meters
    val timestamp: Long = System.currentTimeMillis(), // For sorting and history
    var isFavorite: Boolean = false,
    var category: String,
    var status: ApprovalStatus = ApprovalStatus.PENDING,
    var rejectionReason: String? = null
) : Parcelable {
    // Helper function to format price
    fun getFormattedPrice(): String {
        return String.format("%,.2f DZD", price)
    }

    // Helper function to format land size
    fun getFormattedSize(): String {
        return String.format("%.2f m²", landSize)
    }
}

enum class ApprovalStatus {
    PENDING,
    APPROVED,
    REJECTED
}

