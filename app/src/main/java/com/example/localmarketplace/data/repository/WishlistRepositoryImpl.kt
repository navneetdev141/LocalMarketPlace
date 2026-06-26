package com.example.localmarketplace.data.repository

import androidx.compose.foundation.isSystemInDarkTheme
import com.example.localmarketplace.data.ListingDao
import com.example.localmarketplace.data.local.WishlistDao
import com.example.localmarketplace.data.local.WishlistEntity
import com.example.localmarketplace.data.mapper.toDomain
import com.example.localmarketplace.domain.WishlistRepository
import com.example.localmarketplace.domain.model.Listing
import com.example.localmarketplace.domain.model.Wishlist
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class WishlistRepositoryImpl @Inject constructor(
    private val wishlistDao: WishlistDao,
    private val listingDao: ListingDao,
    private val auth: FirebaseAuth
) : WishlistRepository {

    private fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
    override suspend fun addToWishlist(listingId: String) {
        val userId = getCurrentUserId() ?: return

        val entity = WishlistEntity(
            userId = userId,
            listingId = listingId
        )
        wishlistDao.addToWishlist(entity)
    }

    override suspend fun removeFromWishlist(listingId: String) {
        val userId = getCurrentUserId() ?: return

        val entity = WishlistEntity(
            userId = userId,
            listingId = listingId
        )
        wishlistDao.removeFromWishlist(entity)
    }

    override fun getWishlistIds(): Flow<Set<String>> {
        val userId = getCurrentUserId() ?: return flowOf(emptySet())

        return wishlistDao.getWishlistIds(userId).map {
            it.toSet()
        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getWishlistListings(): Flow<List<Listing>> {
        val userId = getCurrentUserId() ?: return flowOf(emptyList())

        return wishlistDao.getWishlistIds(userId)
            .flatMapLatest { ids ->
                if (ids.isEmpty()) {
                    flowOf(emptyList())
                } else {
                    listingDao.getListingsByIds(ids)
                }
            }.map { entities ->
            entities.map { it.toDomain() }}
    }
}