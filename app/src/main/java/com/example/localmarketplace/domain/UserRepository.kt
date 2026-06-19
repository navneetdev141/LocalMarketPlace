package com.example.localmarketplace.domain

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