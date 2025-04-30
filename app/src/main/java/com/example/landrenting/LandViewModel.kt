package com.example.landrenting


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.landrenting.models.LandModel

class LandViewModel : ViewModel() {
    private val _favoriteLands = MutableLiveData<MutableList<LandModel>>(mutableListOf())
    val favoriteLands: LiveData<MutableList<LandModel>> get() = _favoriteLands

    fun toggleFavorite(land: LandModel) {
        val currentFavorites = _favoriteLands.value ?: mutableListOf()
        land.isFavorite = !land.isFavorite
        if (land.isFavorite) {
            if (!currentFavorites.any { it.id == land.id }) {
                currentFavorites.add(land)
            }
        } else {
            currentFavorites.removeIf { it.id == land.id }
        }
        _favoriteLands.value = currentFavorites
    }
}