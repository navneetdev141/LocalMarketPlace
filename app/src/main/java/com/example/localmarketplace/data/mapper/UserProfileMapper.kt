package com.example.localmarketplace.data.mapper

import com.example.localmarketplace.data.remote.UserProfileDto
import com.example.localmarketplace.domain.model.UserProfile

fun UserProfileDto.toDomain() = UserProfile(
    userId = userId,
    name = name,
    email = email,
    phoneNumber = phoneNumber,
    profileImageUrl = profileImageUrl
)

fun UserProfile.toDto() = UserProfileDto(
    userId = userId,
    name = name,
    email = email,
    phoneNumber = phoneNumber,
    profileImageUrl = profileImageUrl
)