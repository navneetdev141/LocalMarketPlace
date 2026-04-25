package com.example.localmarketplace.data.repository

import android.R.attr.query
import android.content.Context
import android.net.Uri
import com.example.localmarketplace.data.ListingDao
import com.example.localmarketplace.data.mapper.toDomain
import com.example.localmarketplace.data.mapper.toDto
import com.example.localmarketplace.data.mapper.toEntity
import com.example.localmarketplace.data.remote.CloudinaryApi
import com.example.localmarketplace.data.remote.FirestoreService
import com.example.localmarketplace.domain.Listing
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.UUID
import javax.inject.Inject


class ListingRepositoryImpl @Inject constructor(
    private val listingDao: ListingDao,
    private val firestore: FirestoreService,
    private val cloudinaryApi: CloudinaryApi
) {

    fun getListings(query: String, category: String?): Flow<List<Listing>> {
        return listingDao.searchAndFilter(query, category)
            .map { list -> list.map { it.toDomain() } }
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

    suspend fun addListing(listing: Listing, imageUri: Uri?, context: Context) {

        var imageUrl = ""
        if (imageUri != null) {
            imageUrl = uploadToCloudinary(imageUri, context)
        }

        val userId = FirebaseAuth.getInstance().currentUser?.uid
            ?: throw IllegalStateException("User not logged in")

        //while adding data to the firestore ,it generates a random id for the listing
        //which won't match with the room database
        //so we assign an id with each listing before adding it to the firestore
        val id = UUID.randomUUID().toString()
        val newListing = listing.copy(
            id = id,
            userId = userId,
            imageUrl = imageUrl,
            createdAt = System.currentTimeMillis()
        )

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

    suspend fun uploadToCloudinary(uri: Uri, context: Context): String {

        val presetPart = "eicurqzl".toRequestBody("text/plain".toMediaTypeOrNull())

        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalStateException("Failed to open image")

        val requestBody = inputStream.readBytes()
            .toRequestBody("image/*".toMediaTypeOrNull())

        val body = MultipartBody.Part.createFormData(
            "file",
            "image.jpg",
            requestBody
        )

        val response = cloudinaryApi.uploadImage(
            file = body,
            uploadPreset = presetPart
        )
        return response.secure_url
    }
}
 