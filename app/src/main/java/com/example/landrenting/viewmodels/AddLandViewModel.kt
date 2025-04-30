package com.example.landrenting.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.landrenting.data.entities.LandEntity
import com.example.landrenting.data.repository.LandRepository
import kotlinx.coroutines.launch

class AddLandViewModel(private val repository: LandRepository) : ViewModel() {
    
    private val _submissionStatus = MutableLiveData<SubmissionResult>()
    val submissionStatus: LiveData<SubmissionResult> = _submissionStatus

    fun submitLand(
        imageUris: List<String>,
        location: String,
        price: Double,
        landName: String,
        ownerId: String,
        ownerPhone: String,
        description: String,
        landSize: Double,
        category: String
    ) {
        viewModelScope.launch {
            _submissionStatus.value = SubmissionResult.Loading
            
            try {
                val result = repository.submitLand(
                    imageUris = imageUris,
                    location = location,
                    price = price,
                    landName = landName,
                    ownerId = ownerId,
                    ownerPhone = ownerPhone,
                    description = description,
                    landSize = landSize,
                    category = category
                )
                
                result.fold(
                    onSuccess = { 
                        _submissionStatus.value = SubmissionResult.Success(it)
                    },
                    onFailure = {
                        _submissionStatus.value = SubmissionResult.Error(it.message ?: "Unknown error occurred")
                    }
                )
            } catch (e: Exception) {
                _submissionStatus.value = SubmissionResult.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    sealed class SubmissionResult {
        object Loading : SubmissionResult()
        data class Success(val land: LandEntity) : SubmissionResult()
        data class Error(val message: String) : SubmissionResult()
    }
}