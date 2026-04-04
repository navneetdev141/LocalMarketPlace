package com.example.localmarketplace.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListingRepository @Inject constructor(private val listingDao: ListingDao) {

    fun getAllListings(): Flow<List<ListingEntity>> = listingDao.getAllListings()


    suspend fun insertListing(listing: ListingEntity) {
        listingDao.insertListing(listing)
    }
}