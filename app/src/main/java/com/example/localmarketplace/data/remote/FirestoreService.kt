package com.example.localmarketplace.data.remote

import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreService @Inject constructor() {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getAllListings(): List<ListingDto> {
        return try {
            val snapshot = firestore.collection("listings").get().await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(ListingDto::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addListing(listing: ListingDto) {
        firestore.collection("listings")
            .add(listing)
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
