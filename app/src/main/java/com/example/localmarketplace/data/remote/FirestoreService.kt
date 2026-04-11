package com.example.localmarketplace.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreService @Inject constructor() {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getListings(): List<ListingDto> {
        return try {
            val snapshot = db.collection("listings").get().await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(ListingDto::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception){
            emptyList()
        }
    }

    suspend fun addListing(listing: ListingDto) {
        db.collection("listings")
            .add(listing)
            .await()
    }

    suspend fun deleteListing(id: String) {
        db.collection("listings")
            .document(id)
            .delete()
            .await()
    }
}
