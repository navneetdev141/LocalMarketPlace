package com.example.localmarketplace.domain

data class UserProfile(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val profileImageUrl: String = ""
)