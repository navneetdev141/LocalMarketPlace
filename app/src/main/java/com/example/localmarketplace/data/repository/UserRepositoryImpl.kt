package com.example.localmarketplace.data.repository

import com.example.localmarketplace.data.mapper.toDomain
import com.example.localmarketplace.data.mapper.toDto
import com.example.localmarketplace.data.remote.FirestoreService
import com.example.localmarketplace.domain.UserProfile
import com.example.localmarketplace.domain.UserRepository
import jakarta.inject.Inject


class UserRepositoryImpl @Inject constructor(
    private val firestoreService: FirestoreService
) : UserRepository {
    override suspend fun createProfile(profile: UserProfile) {
        firestoreService.createUserProfile(profile.toDto())
    }

    override suspend fun getProfile(userId: String): UserProfile {
         return firestoreService.getUserProfile(userId).toDomain()
    }

    override suspend fun updateProfile(profile: UserProfile) {
        firestoreService.updateUserProfile(profile.toDto())
    }
}