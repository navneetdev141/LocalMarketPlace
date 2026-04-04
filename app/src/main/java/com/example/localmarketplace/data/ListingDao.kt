package com.example.localmarketplace.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ListingDao {

    @Query("SELECT * FROM listings")
    fun getAllListings(): Flow<List<ListingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListing(listing: ListingEntity)
}
