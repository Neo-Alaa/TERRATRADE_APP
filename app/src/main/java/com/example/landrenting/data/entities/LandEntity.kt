package com.example.landrenting.data.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.landrenting.models.ApprovalStatus
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "lands")
data class LandEntity(
    @PrimaryKey
    val id: String,
    val imageUris: String,
    val location: String,
    val price: Double,
    val landName: String,
    val ownerId: String,
    val ownerPhone: String,
    val description: String,
    val landSize: Double,
    val timestamp: Long,
    val category: String,
    val status: ApprovalStatus,
    val rejectionReason: String? = null
) : Parcelable
