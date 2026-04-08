package com.example.localmarketplace.domain

data class Listing (
    val id: String = "",
    val title: String = "",
    val price: String = "",
    val phoneNumber: String = "",
    val description: String = "",
    val category: String = "",
    val imageUrl: String? = null
)