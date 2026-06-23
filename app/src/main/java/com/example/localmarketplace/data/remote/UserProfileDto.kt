package com.example.localmarketplace.data.remote

data class UserProfileDto(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val profileImageUrl: String = ""
)