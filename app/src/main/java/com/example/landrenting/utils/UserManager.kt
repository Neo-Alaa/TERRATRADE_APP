package com.example.landrenting.utils

import android.content.Context
import com.example.landrenting.data.AppDatabase
import com.example.landrenting.data.mappers.toEntity
import com.example.landrenting.data.mappers.toUser
import com.example.landrenting.data.entities.UserSessionEntity
import com.example.landrenting.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UserManager private constructor(context: Context) {
    private val userDao = AppDatabase.getDatabase(context).userDao()
    private val userSessionDao = AppDatabase.getDatabase(context).userSessionDao()
    private val scope = CoroutineScope(Dispatchers.IO)

    companion object {
        @Volatile
        private var instance: UserManager? = null

        fun getInstance(context: Context): UserManager {
            return instance ?: synchronized(this) {
                instance ?: UserManager(context.applicationContext).also { instance = it }
            }
        }
    }

    fun saveUser(user: User) {
        scope.launch {
            userDao.saveUser(user.toEntity())
        }
    }

    fun getUser(email: String): User? = runBlocking {
        userDao.getUserByEmail(email)?.toUser()
    }

    fun validateCredentials(email: String, password: String): Boolean = runBlocking {
        val user = userDao.getUserByEmail(email)
        user?.password == password
    }

    fun setCurrentUser(user: User) = runBlocking {
        val session = UserSessionEntity(currentUserId = user.id, isLoggedIn = true)
        userSessionDao.saveSession(session)
    }

    fun getCurrentUser(): User? = runBlocking {
        val session = userSessionDao.getCurrentSession()
        session?.currentUserId?.let { userId ->
            userDao.getUserById(userId)?.toUser()
        }
    }

    fun isLoggedIn(): Boolean = runBlocking {
        val session = userSessionDao.getCurrentSession()
        session?.isLoggedIn ?: false
    }

    fun logout() = runBlocking {
        userSessionDao.clearSession()
    }

    // Admin-specific methods
    fun getLandsByStatus(status: String): List<String> {
        // TODO: Implement fetching lands by status (pending, approved, rejected)
        return emptyList()
    }

    fun updateLandStatus(landId: String, status: String, rejectionReason: String? = null) {
        // TODO: Implement updating land status
    }
}