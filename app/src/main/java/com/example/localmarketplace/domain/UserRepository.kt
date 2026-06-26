package com.example.localmarketplace.domain

import com.example.localmarketplace.domain.model.UserProfile

interface UserRepository {

    suspend fun createProfile(
        profile: UserProfile
    )
    suspend fun getProfile(
        userId: String
    ): UserProfile

    suspend fun updateProfile(
        profile: UserProfile
    )
}