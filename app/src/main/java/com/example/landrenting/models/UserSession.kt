package com.example.landrenting.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_sessions")
data class UserSession(
    @PrimaryKey
    val id: Int = 1, // We'll only ever have one session
    val currentUserId: String?,
    val isLoggedIn: Boolean = false
)