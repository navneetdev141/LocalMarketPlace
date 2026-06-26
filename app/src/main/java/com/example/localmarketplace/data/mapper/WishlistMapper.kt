package com.example.localmarketplace.data.mapper

import com.example.localmarketplace.data.local.WishlistEntity
import com.example.localmarketplace.domain.model.Wishlist

fun WishlistEntity.toDomain(): Wishlist {
    return Wishlist(
        listingId = listingId,
        userId = userId
    )
}

fun Wishlist.toEntity(): WishlistEntity {
    return WishlistEntity(
        listingId = listingId,
        userId = userId
    )
}