package com.example.localmarketplace.domain.model

data class UserProfile(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val profileImageUrl: String = ""
)