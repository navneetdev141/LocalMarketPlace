package com.example.localmarketplace.data.repository

import com.example.localmarketplace.data.ListingDao
import com.example.localmarketplace.data.mapper.toDomain
import com.example.localmarketplace.data.mapper.toDto
import com.example.localmarketplace.data.mapper.toEntity
import com.example.localmarketplace.data.remote.FirestoreService
import com.example.localmarketplace.domain.Listing
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject


class ListingRepositoryImpl @Inject constructor(
    private val listingDao: ListingDao,
    private val firestore: FirestoreService
) {

    fun getAllListings(): Flow<List<Listing>> {
        return listingDao.getAllListings().map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun syncListings() {
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

    suspend fun addListing(listing: Listing) {

        //while adding data to the firestore ,it generates a random id for the listing
        //which won't match with the room database
        //so we assign an id with each listing before adding it to the firestore
        val id = UUID.randomUUID().toString()
        val newListing = listing.copy(id = id)

        //adding the new listing to the firebase
        listingDao.insertListing(newListing.toEntity())

        try {
            firestore.addListing(newListing.toDto())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteListing(listing: Listing) {
        firestore.deleteListing(listing.id)
        listingDao.deleteListing(listing.toEntity())
    }
}
