package com.example.localmarketplace.domain

import com.example.localmarketplace.domain.model.Listing
import kotlinx.coroutines.flow.Flow

interface ListingRepository {

    suspend fun addListing(listing: Listing)
    fun getAllListings(): Flow<List<Listing>>
    fun getMyListings(userId: String): Flow<List<Listing>>
    fun getFilteredAndSortedListings(query: String, category: String?, sort: String, currentUserId: String): Flow<List<Listing>>
    suspend fun deleteListing(listing: Listing)
    suspend fun syncListings()

    suspend fun updateListing(listing: Listing)

    suspend fun markAsSold(listingId: String)
    suspend fun markAsActive(listingId: String)

    fun getListingById(
        id: String
    ): Flow<Listing?>

}