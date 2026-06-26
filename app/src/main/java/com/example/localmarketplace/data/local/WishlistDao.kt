package com.example.localmarketplace.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
@Dao
interface WishlistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWishlist(
        wishlist: WishlistEntity
    )

    @Delete
    suspend fun removeFromWishlist(
        wishlist: WishlistEntity
    )

    @Query(
        """
        SELECT * FROM wishlist
        WHERE userId = :userId
        """
    )
    fun getWishlist(
        userId: String
    ): Flow<List<WishlistEntity>>

    @Query(
        """
        SELECT EXISTS(
            SELECT 1
            FROM wishlist
            WHERE userId = :userId
            AND listingId = :listingId
        )
        """
    )
    fun isWishlisted(
        userId: String,
        listingId: String
    ): Flow<Boolean>

    @Query(
        """
    SELECT listingId
    FROM wishlist
    WHERE userId = :userId
    """
    )
    fun getWishlistIds(
        userId: String
    ): Flow<List<String>>

}