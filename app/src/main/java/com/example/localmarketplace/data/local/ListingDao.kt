package com.example.localmarketplace.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.localmarketplace.data.local.ListingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ListingDao {

    @Query(
        "SELECT * FROM listings " +
                "WHERE title LIKE '%' || :query || '%' " +
                "AND (:category IS NULL OR category = :category)"+
                "ORDER BY createdAt DESC"
    )
    fun searchAndFilter(query: String, category: String?): Flow<List<ListingEntity>>

    @Query("SELECT * FROM listings")
    fun getAllListings(): Flow<List<ListingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListing(listing: ListingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListings(listings: List<ListingEntity>)

    @Delete
    suspend fun deleteListing(listing: ListingEntity)

    @Query("DELETE FROM listings")
    suspend fun clearAll()

}
