package com.example.localmarketplace.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "listings")
data class ListingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val category: String,
    val price: String,
    val phoneNumber: String,
    val imageUri: String?
)
