package com.example.localmarketplace.data.mapper

import com.example.localmarketplace.data.local.ListingEntity
import com.example.localmarketplace.data.remote.ListingDto
import com.example.localmarketplace.domain.Listing


fun ListingEntity.toDomain(): Listing {
    return Listing(
        id = id,
        title = title,
        description = description,
        price = price,
        category = category,
        imageUrl = imageUrl,
        userId = userId,
        phoneNumber = phoneNumber,
        createdAt = createdAt
    )
}

fun Listing.toEntity(): ListingEntity {
    return ListingEntity(
        id = id,
        title = title,
        description = description,
        price = price,
        category = category,
        imageUrl = imageUrl,
        userId = userId,
        phoneNumber = phoneNumber,
        createdAt = createdAt
    )
}
fun ListingDto.toEntity(): ListingEntity {
    return ListingEntity(
        id = id,
        title = title,
        description = description,
        price = price,
        category = category,
        imageUrl = imageUrl,
        userId = userId,
        phoneNumber = phoneNumber,
        createdAt = createdAt
    )
}