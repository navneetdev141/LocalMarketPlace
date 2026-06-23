package com.example.localmarketplace.data.remote

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class CloudinaryService @Inject constructor(private val cloudinaryApi: CloudinaryApi) {

    suspend fun uploadImage(uri: Uri, context: Context): String {

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

        try {
            val response = cloudinaryApi.uploadImage(
                file = body,
                uploadPreset = presetPart
            )
            android.util.Log.d("CLOUDINARY", "Upload success: ${response.secure_url}")
            return response.secure_url
        } catch (e: Exception) {
            android.util.Log.e("CLOUDINARY", "Upload failed: ${e.message}", e)
            throw e
        }
    }
}
