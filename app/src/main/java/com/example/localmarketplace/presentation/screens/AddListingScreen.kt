package com.example.localmarketplace.presentation.screens

import android.net.Uri
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.localmarketplace.domain.Listing
import com.example.localmarketplace.presentation.components.CategoryDropDown
import com.example.localmarketplace.presentation.listing.ListingUiState
import com.example.localmarketplace.presentation.viewmodel.ListingViewModel

@Composable
fun AddListingScreen(viewModel: ListingViewModel, onListingAdded: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val categories = listOf(
        "Stationery",
        "Sports Equipment",
        "Home Appliances",
        "Others"
    )
    var selectedCategory by remember { mutableStateOf("") }

    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent())
        { uri -> imageUri = uri }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        when (uiState) {
            is ListingUiState.Success -> {
                Toast.makeText(context, "Listing added", Toast.LENGTH_SHORT).show()
                onListingAdded()
            }

            is ListingUiState.Error -> {
                Toast.makeText(
                    context,
                    (uiState as ListingUiState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> Unit
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)) {

            imageUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }

            Button(
                onClick = { launcher.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Choose Image")
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

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val listing = Listing(
                        title = title,
                        price = price.toDoubleOrNull() ?: 0.0,
                        description = description,
                        category = selectedCategory
                    )
                    viewModel.addListing(listing, imageUri, context)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is ListingUiState.Loading
            ) {
                Text("Add Listing")
            }
        }
        if (uiState is ListingUiState.Loading){
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