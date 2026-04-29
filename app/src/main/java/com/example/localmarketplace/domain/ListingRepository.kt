package com.example.localmarketplace.domain

import kotlinx.coroutines.flow.Flow

interface ListingRepository {

    suspend fun addListing(listing: Listing)

    fun getAllListings(): Flow<List<Listing>>
    fun searchListings(query: String, category: String?): Flow<List<Listing>>
    suspend fun deleteListing(listing: Listing)
    suspend fun syncListings()

}