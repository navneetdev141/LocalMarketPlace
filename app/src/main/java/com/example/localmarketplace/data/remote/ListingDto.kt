package com.example.localmarketplace.data.remote

data class ListingDto(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val phoneNumber: String = "",
    val category: String = "",
    val imageUrls: List<String> = emptyList(),
    val userId: String = "",
    val createdAt: Long = 0L,
    val isActive: Boolean = true,
    val isSold: Boolean = false
)
