package com.example.landrenting.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val password: String,
    val isAdmin: Boolean = false,
    val imageRes: Int = 0
) : Parcelable