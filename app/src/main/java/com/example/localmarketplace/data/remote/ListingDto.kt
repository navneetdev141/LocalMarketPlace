package com.example.localmarketplace.data.remote


//dto stands for data transfer object
data class ListingDto(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val phoneNumber: String = "",
    val category: String = "",
    val imageUrl: String = "",
    val userId: String = "",
    val createdAt: Long = 0L
)
