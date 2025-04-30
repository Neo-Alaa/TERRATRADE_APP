package com.example.landrenting.data.dao

import androidx.room.*
import com.example.landrenting.data.entities.LandEntity
import com.example.landrenting.models.ApprovalStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface LandDao {
    @Query("SELECT * FROM lands")
    fun getAllLands(): Flow<List<LandEntity>>

    @Query("SELECT * FROM lands WHERE ownerId = :userId")
    fun getLandsByUser(userId: String): Flow<List<LandEntity>>

    @Query("SELECT * FROM lands WHERE status = :status")
    fun getLandsByStatus(status: ApprovalStatus): Flow<List<LandEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLand(land: LandEntity)

    @Update
    suspend fun updateLand(land: LandEntity)

    @Delete
    suspend fun deleteLand(land: LandEntity)

    @Query("SELECT * FROM lands WHERE id = :landId")
    suspend fun getLandById(landId: String): LandEntity?
}