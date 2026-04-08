package com.example.localmarketplace.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ListingEntity::class], version = 2)
abstract class ListingDatabase: RoomDatabase() {
    abstract fun listingDao(): ListingDao
}
