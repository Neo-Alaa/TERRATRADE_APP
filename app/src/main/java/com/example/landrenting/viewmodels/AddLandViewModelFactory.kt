package com.example.landrenting.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.landrenting.data.repository.LandRepository

class AddLandViewModelFactory(
    private val repository: LandRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddLandViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddLandViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}