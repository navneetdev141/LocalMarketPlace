package com.example.localmarketplace.data.remote

import android.util.Log
import com.example.localmarketplace.domain.model.Listing
import com.example.localmarketplace.domain.model.UserProfile
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreService @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun getAllListings(): List<ListingDto> {
        try {
            val snapshot = firestore.collection("listings").get().await()

            return snapshot.documents.mapNotNull { doc ->
                doc.toObject(ListingDto::class.java)
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Failed to fetch listings", e)
            throw e
        }
    }

    suspend fun updateListing(listingDto: ListingDto) {

        firestore.collection("listings")
            .document(listingDto.id)
            .set(listingDto)
            .await()
    }

    suspend fun addListing(listing: ListingDto) {
        firestore.collection("listings")
            .document(listing.id)
            .set(listing)
            .await()

    }

    suspend fun deleteListing(id: String) {
        firestore.collection("listings")
            .document(id)
            .delete()
            .await()
    }

    suspend fun createUserProfile(profile: UserProfileDto) {
        firestore.collection("users")
            .document(profile.userId)
            .set(profile)
            .await()
    }

    suspend fun getUserProfile(userId: String): UserProfileDto {
        try {
            val snapshot =
                firestore.collection("users")
                    .document(userId)
                    .get()
                    .await()

            return snapshot.toObject(UserProfileDto::class.java)
                ?: UserProfileDto()
        } catch (e: Exception) {
            Log.e("Profile", "Failed to get profile", e)
            throw e
        }
    }

    suspend fun updateUserProfile(profile: UserProfileDto) {
        firestore.collection("users")
            .document(profile.userId)
            .set(profile)
            .await()
    }

    fun listenToNewListings(onNewListing: (ListingDto) -> Unit) {

        firestore.collection("listings").addSnapshotListener { snapshot, error ->
            if (error != null) {
                android.util.Log.e("FIRESTORE", "Listen failed: $error")
                return@addSnapshotListener
            }
            snapshot?.documentChanges?.forEach { change ->

                if (change.type == DocumentChange.Type.ADDED) {
                    val listing = change.document.toObject(ListingDto::class.java)
                    onNewListing(listing)
                }
            }
        }
    }

    suspend fun markListingAsSold(listingId: String) {
        firestore.collection("listings")
            .document(listingId)
            .update(
                mapOf(
                    "isSold" to true,
                    "isActive" to false
                )
            ).await()
    }

    suspend fun markListingAsActive(listingId: String) {
        firestore.collection("listings")
            .document(listingId)
            .update(
                mapOf(
                    "isSold" to false,
                    "isActive" to true
                )
            ).await()
    }
}
