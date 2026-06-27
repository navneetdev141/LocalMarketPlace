package com.example.localmarketplace.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.localmarketplace.data.ListingDao
import com.example.localmarketplace.data.local.ListingEntity
import com.example.localmarketplace.data.mapper.toDomain
import com.example.localmarketplace.data.mapper.toDto
import com.example.localmarketplace.data.mapper.toEntity
import com.example.localmarketplace.data.remote.FirestoreService
import com.example.localmarketplace.domain.model.Listing
import com.example.localmarketplace.domain.ListingRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject


class ListingRepositoryImpl @Inject constructor(
    private val listingDao: ListingDao,
    private val firestore: FirestoreService,
    private val auth: FirebaseAuth
) : ListingRepository {

    override fun getFilteredAndSortedListings(
        query: String,
        category: String?,
        sort: String,
        currentUserId: String
    ): Flow<List<Listing>> {
        Log.d("HOME_FILTER", "Current User ID = $currentUserId")
        Log.d("HOME_DEBUG", "query = '$query'")
        Log.d("HOME_DEBUG", "category = '$category'")
        
        return listingDao.searchAndFilter(query, category, currentUserId)
            .map { list ->
                Log.d("HOME_DEBUG", "DAO returned ${list.size} listings")
                val domainList = list.map { it.toDomain() }

                when (sort) {
                    "price_low" -> domainList.sortedBy { it.price }
                    "price_high" -> domainList.sortedByDescending { it.price }
                    else -> domainList.sortedByDescending { it.createdAt }
                }
            }
    }

    override fun getAllListings(): Flow<List<Listing>> {
        return listingDao.getAllListings()
            .map { list -> list.map { it.toDomain() } }
    }

    override fun getMyListings(userId: String): Flow<List<Listing>> {
        return listingDao.getMyListings(userId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun syncListings() {
        try {
            Log.d("SYNC", "Starting sync")
            val remoteListings = firestore.getAllListings()
            Log.d("SYNC", "Firestore returned ${remoteListings.size}")
            listingDao.insertListings(remoteListings.map { it.toEntity() })
            Log.d("SYNC", "Inserted into Room")
        } catch (e: Exception) {
            Log.e("SYNC", "Error syncing listings: ${e.message}")
        }
    }

    override suspend fun updateListing(listing: Listing) {

        Log.d("UPDATE", "Room insert started")

        listingDao.insertListing(listing.toEntity())

        Log.d("UPDATE", "Room insert finished")

        firestore.updateListing(listing.toDto())

        Log.d("UPDATE", "Firestore update finished")

    }

    override suspend fun addListing(listing: Listing) {
        val userId = auth.currentUser?.uid
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
        Log.d("DB_INSERT", "Inserted locally")

        //syncing to firestore
        firestore.addListing(newListing.toDto())
        Log.d("DB_INSERT", "Synced to Firestore")
    }

    override suspend fun deleteListing(listing: Listing) {
        firestore.deleteListing(listing.id)
        listingDao.deleteListing(listing.toEntity())
    }

    override fun getListingById(
        id: String
    ): Flow<Listing?> {

        return listingDao.getListingById(id)
            .map { it?.toDomain() }
    }

    override suspend fun markAsSold(listingId: String) {
        firestore.markListingAsSold(listingId)

        val entity = listingDao.getListingById(listingId).firstOrNull()
        entity?.let {
            listingDao.updateListing(it.copy(isSold = true, isActive = false))
        }
    }

    override suspend fun markAsActive(listingId: String) {
        firestore.markListingAsActive(listingId)

        val entity = listingDao.getListingById(listingId).firstOrNull()
        entity?.let {
            listingDao.updateListing(
                it.copy(isSold = false, isActive = true)
            )
        }
    }
}
