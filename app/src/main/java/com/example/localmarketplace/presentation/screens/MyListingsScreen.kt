package com.example.localmarketplace.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.localmarketplace.presentation.components.MyListingItem
import com.example.localmarketplace.presentation.viewmodel.ListingViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyListingsScreen(
    viewModel: ListingViewModel,
    onListingClick: (String) -> Unit,
    onBack: () -> Unit,
    onEditListing: (String) -> Unit
) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val myListings by viewModel
        .getMyListings(userId ?: "")
        .collectAsState(initial = emptyList())

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "My Listings",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        if (myListings.isNotEmpty()) {
                            Text(
                                "${myListings.size} active listing${if (myListings.size > 1) "s" else ""}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
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
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        if (myListings.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(72.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Inventory,
                                contentDescription = null,
                                modifier = Modifier.size(32.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Text(
                        "No listings yet",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "Tap \"New Listing\" on the home screen to get started",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(padding)
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                items(myListings, key = { it.id }) { listing ->
                    MyListingItem(
                        listing = listing,
                        onDelete = { viewModel.deleteListing(listing) },
                        onClick = { onListingClick(listing.id) },
                        onEdit = { onEditListing(listing.id) },
                        modifier = Modifier.animateItem()
                    )
                }
                item { Spacer(Modifier.height(24.dp)) }
            }
        }
    }
}