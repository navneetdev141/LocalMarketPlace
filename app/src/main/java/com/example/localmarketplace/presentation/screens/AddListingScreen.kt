package com.example.localmarketplace.presentation.screens

import android.net.Uri
import android.text.Layout
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.localmarketplace.domain.model.Listing
import com.example.localmarketplace.presentation.components.CategoryDropDown
import com.example.localmarketplace.presentation.listing.ListingUiState
import com.example.localmarketplace.presentation.viewmodel.ListingViewModel
import kotlinx.coroutines.launch

@Composable
fun AddListingScreen(
    viewModel: ListingViewModel,
    listingId: String,
    onListingAdded: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    val categories = listOf("Stationery", "Sports Equipment", "Home Appliances", "Others")
    var selectedCategory by remember { mutableStateOf("") }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var selectedImages by remember {
        mutableStateOf<List<Uri>>(emptyList())
    }
    var existingImageUrls by remember {
        mutableStateOf<List<String>>(emptyList())
    }

    var isSubmitting by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.size <= 5) {
            selectedImages = uris
        } else {
            Toast.makeText(context, "Max 5 images allowed", Toast.LENGTH_SHORT).show()
        }
    }

    val uiState by viewModel.uiState.collectAsState()
    val existingListing by viewModel
        .getListingById(listingId ?: "")
        .collectAsState(initial = null)

    LaunchedEffect(existingListing) {
        existingListing?.let {
            title = it.title
            price = it.price.toString()
            description = it.description
            phoneNumber = it.phoneNumber
            selectedCategory = it.category

            existingImageUrls = it.imageUrls
        }
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is ListingUiState.Success -> {
                isSubmitting = false
                Toast.makeText(context, "Listing added", Toast.LENGTH_SHORT).show()
                onListingAdded()
                viewModel.resetState()
            }

            is ListingUiState.Error -> {
                isSubmitting = false
                Toast.makeText(
                    context,
                    (uiState as ListingUiState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> Unit
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .padding(top = 16.dp)
        ) {
            Log.d("VM_ADD", viewModel.toString())
            if (existingImageUrls.isNotEmpty() || selectedImages.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp)
                        .background(Color.LightGray.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    if(selectedImages.isNotEmpty()
                        ){
                        items(selectedImages) { uri ->
                            Image(
                                painter = rememberAsyncImagePainter(uri),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(200.dp)
                                    .padding(end = 8.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    else{
                        items(existingImageUrls){url ->
                            AsyncImage(
                                model = url,
                                contentDescription = null
                            )
                        }
                    }

                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(
                onClick = { launcher.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = if (selectedImages.isEmpty()) "Choose Images" else "Change Images")
            }

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price") },
                modifier = Modifier.fillMaxWidth()
            )

            CategoryDropDown(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it }
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (isSubmitting) return@Button
                    isSubmitting = true
                    scope.launch {
                        try {
                            val imageUrls = if(selectedImages.isNotEmpty()){
                                viewModel.uploadImages(selectedImages, context)
                            }else{
                                existingListing?.imageUrls ?: emptyList()
                            }
                            val listing = Listing(
                                title = title,
                                price = price.toDoubleOrNull() ?: 0.0,
                                description = description,
                                phoneNumber = phoneNumber,
                                category = selectedCategory,
                                imageUrls = imageUrls
                            )
                            if (existingListing == null) {
                                viewModel.addListing(listing)
                            } else {
                                viewModel.updateListing(
                                    listing.copy(
                                        id = existingListing!!.id,
                                        userId = existingListing!!.userId,
                                        createdAt = existingListing!!.createdAt
                                    )
                                )
                            }
                        } catch (e: Exception) {
                            isSubmitting = false
                            android.util.Log.e("ADD_SCREEN", "Error adding listing", e)
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSubmitting &&
                        title.isNotBlank() &&
                        (selectedImages.isNotEmpty() || existingListing!= null)
            ) {
                Text(
                    when {
                        uiState is ListingUiState.Loading ->
                            "Uploading..."

                        existingListing == null ->
                            "Add Listing"

                        else ->
                            "Save Changes"
                    }
                )
            }
        }
        if (isSubmitting) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
