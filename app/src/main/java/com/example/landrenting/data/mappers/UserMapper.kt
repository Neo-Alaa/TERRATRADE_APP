package com.example.landrenting.data.mappers

import com.example.landrenting.data.entities.UserEntity
import com.example.landrenting.models.User

fun UserEntity.toUser(): User {
    return User(
        id = this.id,
        fullName = this.fullName,
        email = this.email,
        phone = this.phone,
        password = this.password,
        isAdmin = this.isAdmin,
        imageRes = this.imageRes
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = this.id,
        fullName = this.fullName,
        email = this.email,
        phone = this.phone,
        password = this.password,
        isAdmin = this.isAdmin,
        imageRes = this.imageRes
    )
}