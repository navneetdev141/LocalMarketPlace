package com.example.localmarketplace.domain

import com.example.localmarketplace.domain.model.Listing
import com.example.localmarketplace.domain.model.Wishlist
import kotlinx.coroutines.flow.Flow

interface WishlistRepository {

    suspend fun addToWishlist(listingId: String)

    suspend fun removeFromWishlist(listingId: String)

    fun getWishlistListings(): Flow<List<Listing>>

    fun getWishlistIds(): Flow<Set<String>>
}