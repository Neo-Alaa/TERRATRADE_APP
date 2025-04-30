package com.example.landrenting.data.repository

import com.example.landrenting.data.dao.LandDao
import com.example.landrenting.data.entities.LandEntity
import com.example.landrenting.models.ApprovalStatus
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class LandRepository(private val landDao: LandDao) {
    
    fun getAllLands(): Flow<List<LandEntity>> = landDao.getAllLands()
    
    fun getLandsByUser(userId: String): Flow<List<LandEntity>> = landDao.getLandsByUser(userId)
    
    fun getLandsByStatus(status: ApprovalStatus): Flow<List<LandEntity>> = landDao.getLandsByStatus(status)
    
    suspend fun submitLand(
        imageUris: List<String>,
        location: String,
        price: Double,
        landName: String,
        ownerId: String,
        ownerPhone: String,
        description: String,
        landSize: Double,
        category: String
    ): Result<LandEntity> {
        return try {
            val landEntity = LandEntity(
                id = UUID.randomUUID().toString(),
                imageUris = imageUris.joinToString(","),
                location = location,
                price = price,
                landName = landName,
                ownerId = ownerId,
                ownerPhone = ownerPhone,
                description = description,
                landSize = landSize,
                timestamp = System.currentTimeMillis(),
                category = category,
                status = ApprovalStatus.PENDING,
                rejectionReason = null
            )
            landDao.insertLand(landEntity)
            Result.success(landEntity)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateLandStatus(landId: String, status: ApprovalStatus, rejectionReason: String? = null) {
        val land = landDao.getLandById(landId) ?: return
        land.copy(
            status = status,
            rejectionReason = rejectionReason
        ).also {
            landDao.updateLand(it)
        }
    }
}