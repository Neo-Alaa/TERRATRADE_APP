package com.example.landrenting.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val password: String,
    val isAdmin: Boolean = false,
    val imageRes: Int = 0
)