package com.example.localmarketplace.data.local

import androidx.room.Entity

@Entity(
    tableName = "wishlist",
    primaryKeys = ["userId","listingId"]
)
data class WishlistEntity(
    val userId: String,
    val listingId: String
)