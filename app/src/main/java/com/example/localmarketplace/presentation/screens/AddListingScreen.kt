package com.example.localmarketplace.presentation.screens

import android.net.Uri
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.localmarketplace.domain.model.Listing
import com.example.localmarketplace.presentation.components.CategoryDropDown
import com.example.localmarketplace.presentation.components.ListingUiState
import com.example.localmarketplace.presentation.viewmodel.ListingViewModel
import com.example.localmarketplace.utils.AppErrorMapper
import com.example.localmarketplace.utils.toUserMessage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddListingScreen(
    viewModel: ListingViewModel,
    listingId: String,
    onListingAdded: () -> Unit,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    val categories = listOf("Stationery", "Sports Equipment", "Home Appliances", "Others")
    var selectedCategory by remember { mutableStateOf("") }

    var titleError by remember { mutableStateOf<String?>(null) }
    var priceError by remember { mutableStateOf<String?>(null) }
    var descriptionError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var categoryError by remember { mutableStateOf<String?>(null) }
    var imageError by remember { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }  // add at top with other states

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
        if (uris.size > 5) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Maximum 5 images allowed"
                )
            }
        } else {
            selectedImages = uris
            imageError = null
        }
    }

    val uiState by viewModel.uiState.collectAsState()
    val existingListing by viewModel
        .getListingById(listingId)
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
                onListingAdded()
                viewModel.resetState()
            }

            is ListingUiState.Error -> {
                isSubmitting = false
                snackbarHostState.showSnackbar(
                    message = (uiState as ListingUiState.Error)
                        .error.toUserMessage(),
                    duration = SnackbarDuration.Short
                )
                viewModel.resetState()
            }

            else -> Unit
        }
    }

    fun validateForm() {
        titleError = when {
            title.isBlank() -> "Title is required"
            title.length < 3 -> "Minimum 3 characters"
            else -> null
        }
        priceError = when {
            price.isBlank() -> "Price is required"
            price.toDoubleOrNull() == null ->
                "Enter a valid number"

            price.toDouble() <= 0 ->
                "Price must be greater than 0"

            else -> null
        }
        descriptionError = when {
            description.isBlank() -> "Description is required"
            description.length < 15 -> "Minimum 15 characters"
            description.length > 500 -> "Maximum 500 characters"
            else -> null
        }
        phoneError = when {

            phoneNumber.isBlank() ->
                "Phone number is required"

            !phoneNumber.all(Char::isDigit) ->
                "Only digits are allowed"

            phoneNumber.length != 10 ->
                "Phone number must contain 10 digits"

            else ->
                null
        }
        categoryError =
            if (selectedCategory.isBlank())
                "Please select a category"
            else
                null
        imageError =
            if (selectedImages.isEmpty() && existingListing == null)
                "Please select at least one image"
            else
                null
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },   // snackbar goes here
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (existingListing == null) "New Listing" else "Edit Listing",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(top = 16.dp)
            ) {
                if (existingImageUrls.isNotEmpty() || selectedImages.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp)
                            .background(
                                Color.LightGray.copy(alpha = 0.1f),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp)
                    ) {
                        if (selectedImages.isNotEmpty()
                        ) {
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
                        } else {
                            items(existingImageUrls) { url ->
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
                imageError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        titleError = when {
                            it.isBlank() -> "Title is required"
                            it.length < 3 -> "Minimum 3 characters"
                            else -> null
                        }
                    },
                    label = { Text("Title") },
                    isError = titleError != null,
                    supportingText = {
                        titleError?.let {
                            Text(it)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = {
                        price = it
                        priceError = when {
                            it.isBlank() -> "Price is required"
                            it.toDoubleOrNull() == null ->
                                "Enter a valid number"

                            it.toDouble() <= 0 ->
                                "Price must be greater than 0"

                            else -> null
                        }
                    },
                    label = { Text("Price") },
                    isError = priceError != null,
                    supportingText = {
                        priceError?.let {
                            Text(it)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                CategoryDropDown(
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = {
                        selectedCategory = it
                        categoryError = null
                    }
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
                        descriptionError = when {
                            it.isBlank() -> "Description is required"
                            it.length < 15 -> "Minimum 15 characters"
                            it.length > 500 -> "Maximum 500 characters"
                            else -> null
                        }
                    },
                    label = { Text("Description") },
                    isError = descriptionError != null,
                    supportingText = {
                        descriptionError?.let {
                            Text(it)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = {
                        phoneNumber = it
                        phoneError = when {

                            it.isBlank() ->
                                "Phone number is required"

                            !it.all(Char::isDigit) ->
                                "Only digits are allowed"

                            it.length != 10 ->
                                "Phone number must contain 10 digits"

                            else ->
                                null
                        }
                    },
                    label = { Text("Phone Number") },
                    isError = phoneError != null,
                    supportingText = {
                        phoneError?.let {
                            Text(it)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (isSubmitting) return@Button
                        validateForm()
                        val hasErrors = titleError != null ||
                                priceError != null ||
                                descriptionError != null ||
                                phoneError != null ||
                                categoryError != null ||
                                imageError != null

                        if (hasErrors) return@Button
                        isSubmitting = true
                        scope.launch {
                            try {
                                val imageUrls = if (selectedImages.isNotEmpty()) {
                                    viewModel.uploadImages(selectedImages, context)
                                } else {
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

                                snackbarHostState.showSnackbar(
                                    message = AppErrorMapper.map(e).toUserMessage()
                                )


                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isSubmitting
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

}