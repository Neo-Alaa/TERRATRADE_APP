package com.example.landrenting.data.mappers

import com.example.landrenting.data.entities.UserSessionEntity
import com.example.landrenting.models.UserSession

fun UserSessionEntity.toModel(): UserSession {
    return UserSession(
        id = this.id,
        currentUserId = this.currentUserId,
        isLoggedIn = this.isLoggedIn
    )
}

fun UserSession.toEntity(): UserSessionEntity {
    return UserSessionEntity(
        id = this.id,
        currentUserId = this.currentUserId,
        isLoggedIn = this.isLoggedIn
    )
}