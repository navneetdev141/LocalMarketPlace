package com.example.localmarketplace.data.repository

import android.net.Uri
import com.example.localmarketplace.data.ListingDao
import com.example.localmarketplace.data.ListingEntity
import com.example.localmarketplace.domain.Listing
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class ListingRepository @Inject constructor(private val listingDao: ListingDao) {


    suspend fun insertListing(listing: ListingEntity) {
        listingDao.insertListing(listing)
    }

    fun getAllListings(): Flow<List<ListingEntity>> {
        return listingDao.getAllListings()
    }
}