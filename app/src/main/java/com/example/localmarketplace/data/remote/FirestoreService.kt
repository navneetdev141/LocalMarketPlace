package com.example.localmarketplace.data.remote

import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreService @Inject constructor() {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getAllListings(): List<ListingDto> {
        val snapshot = firestore.collection("listings").get().await()

        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(ListingDto::class.java)
        }
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

    fun listenToNewListings(onNewListing: (ListingDto) -> Unit) {

        firestore.collection("listings").addSnapshotListener { snapshot, _ ->
            snapshot?.documentChanges?.forEach { change ->

                if (change.type == DocumentChange.Type.ADDED) {
                    val listing = change.document.toObject(ListingDto::class.java)
                    onNewListing(listing)
                }
            }
        }
    }
}
