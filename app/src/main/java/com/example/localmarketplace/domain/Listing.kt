package com.example.localmarketplace.domain

data class Listing(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val category: String = "",
    val imageUrls: List<String> ,
    val userId: String = "",
    val phoneNumber: String = "",
    val createdAt: Long = System.currentTimeMillis()
)