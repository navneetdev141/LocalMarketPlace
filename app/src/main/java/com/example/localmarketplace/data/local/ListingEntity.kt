package com.example.localmarketplace.data.local

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "listings")
data class ListingEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val price: Double,
    val phoneNumber: String,
    val imageUrl: String,
    val userId: String,
    val createdAt: Long
)