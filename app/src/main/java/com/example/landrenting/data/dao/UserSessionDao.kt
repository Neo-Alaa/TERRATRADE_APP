package com.example.landrenting.data.dao

import androidx.room.*
import com.example.landrenting.data.entities.UserSessionEntity

@Dao
interface UserSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSession(session: UserSessionEntity)

    @Query("SELECT * FROM user_sessions WHERE id = 1")
    suspend fun getCurrentSession(): UserSessionEntity?

    @Query("DELETE FROM user_sessions")
    suspend fun clearSession()
}