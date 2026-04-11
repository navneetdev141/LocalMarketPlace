package com.example.localmarketplace.data.repository

import com.example.localmarketplace.data.local.ListingDao
import com.example.localmarketplace.data.mapper.toDomain
import com.example.localmarketplace.data.mapper.toEntity
import com.example.localmarketplace.data.remote.FirestoreService
import com.example.localmarketplace.domain.Listing
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class ListingRepositoryImpl @Inject constructor(
    private val listingDao: ListingDao,
    private val firestore : FirestoreService) {

    suspend fun insertListing(listing: Listing) {
        listingDao.insertListing(listing.toEntity())
    }

    suspend fun deleteListing(listing: Listing) {
        listingDao.deleteListing(listing.toEntity())
    }

    fun getAllListings(): Flow<List<Listing>> {
        return listingDao.getAllListings().map{list ->
            list.map{it.toDomain()}

        }
    }
}