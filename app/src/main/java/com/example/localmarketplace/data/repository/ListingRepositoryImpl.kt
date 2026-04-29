package com.example.localmarketplace.data.repository

import android.content.Context
import android.net.Uri
import com.example.localmarketplace.data.ListingDao
import com.example.localmarketplace.data.mapper.toDomain
import com.example.localmarketplace.data.mapper.toDto
import com.example.localmarketplace.data.mapper.toEntity
import com.example.localmarketplace.data.remote.FirestoreService
import com.example.localmarketplace.domain.Listing
import com.example.localmarketplace.domain.ListingRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject


class ListingRepositoryImpl @Inject constructor(
    private val listingDao: ListingDao,
    private val firestore: FirestoreService,
): ListingRepository {

    override fun searchListings(query: String, category: String?): Flow<List<Listing>> {
        return listingDao.searchAndFilter(query, category)
            .map { list -> list.map { it.toDomain() } }
    }

    override fun getAllListings(): Flow<List<Listing>> {
        return listingDao.getAllListings()
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun syncListings() {
        try {
            val remoteListings = firestore.getAllListings()

            //clear the old data
            listingDao.clearAll()

            //add the new data
            listingDao.insertListings(remoteListings.map { it.toEntity() })

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override suspend fun addListing(listing: Listing) {

        val userId = FirebaseAuth.getInstance().currentUser?.uid
            ?: throw IllegalStateException("User not logged in")

        //while adding data to the firestore ,it generates a random id for the listing
        //which won't match with the room database
        //so we assign an id with each listing before adding it to the firestore
        val id = UUID.randomUUID().toString()
        val newListing = listing.copy(
            id = id,
            userId = userId,
            createdAt = System.currentTimeMillis()
        )
        //adding the listing locally
        listingDao.insertListing(newListing.toEntity())

        //syncing to firestore
        try {
            firestore.addListing(newListing.toDto())
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    override suspend fun deleteListing(listing: Listing) {
        firestore.deleteListing(listing.id)
        listingDao.deleteListing(listing.toEntity())
    }


}
 