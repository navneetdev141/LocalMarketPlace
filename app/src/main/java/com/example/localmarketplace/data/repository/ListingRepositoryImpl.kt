package com.example.localmarketplace.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
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
) : ListingRepository {

    val currentUserId =
        FirebaseAuth.getInstance().currentUser?.uid ?: ""

    override fun getFilteredAndSortedListings(
        query: String,
        category: String?,
        sort: String
    ): Flow<List<Listing>> {
        Log.d("DEBUG_REPO", "Querying: query='$query', category='$category', sort='$sort'")
        return listingDao.searchAndFilter(query, category,currentUserId)
            .map { list ->
                Log.d("DEBUG_REPO", "Found ${list.size} items in Room")
                val domainList = list.map { it.toDomain() }

                when(sort){

                    "price_low" ->
                        domainList.sortedBy { it.price }

                    "price_high" ->
                        domainList.sortedByDescending { it.price }

                    else ->
                        domainList.sortedByDescending { it.createdAt }
                }
            }
    }

    override fun getAllListings(): Flow<List<Listing>> {
        return listingDao.getAllListings()
            .map { list -> list.map { it.toDomain() } }
    }

    override fun getMyListings(userId: String): Flow<List<Listing>> {
        return listingDao.getMyListings(userId).map{list->
            list.map{it.toDomain()}
        }
    }

    override suspend fun syncListings() {
        try {
            Log.d("DEBUG_REPO", "Starting sync...")
            val remoteListings = firestore.getAllListings()
            Log.d("DEBUG_REPO", "Fetched ${remoteListings.size} listings from Firestore")
            
            if (remoteListings.isNotEmpty()) {
                listingDao.insertListings(remoteListings.map { it.toEntity() })
                Log.d("DEBUG_REPO", "Successfully inserted ${remoteListings.size} items into Room")
            } else {
                Log.d("DEBUG_REPO", "Firestore returned 0 listings")
            }

        } catch (e: Exception) {
            Log.e("DEBUG_REPO", "Sync failed: ${e.message}", e)
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
        Log.d("DB_INSERT", "Inserted locally")

        //syncing to firestore
        firestore.addListing(newListing.toDto())
        Log.d("DB_INSERT", "Synced to Firestore")
    }

    override suspend fun deleteListing(listing: Listing) {
        firestore.deleteListing(listing.id)
        listingDao.deleteListing(listing.toEntity())
    }
}
