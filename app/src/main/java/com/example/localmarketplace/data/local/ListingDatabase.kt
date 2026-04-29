package com.example.localmarketplace.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.localmarketplace.data.ListingDao
import com.example.localmarketplace.data.local.ListingEntity

@Database(entities = [ListingEntity::class], version = 3)
@TypeConverters(Converters::class)
abstract class ListingDatabase: RoomDatabase() {
    abstract fun listingDao(): ListingDao
}