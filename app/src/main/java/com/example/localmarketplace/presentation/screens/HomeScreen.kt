package com.example.localmarketplace.presentation.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.localmarketplace.presentation.components.CategoryDropDown
import com.example.localmarketplace.presentation.components.ListingItem
import com.example.localmarketplace.presentation.viewmodel.ListingViewModel


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(viewModel: ListingViewModel,
               onAddClick: () -> Unit,
               onListingClick: (String) -> Unit
) {

    val listings by viewModel.listings.collectAsState()
    val query by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                viewModel.observeNewListings(context)
            }
        }
    )
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        viewModel.observeNewListings(context)
    }


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onAddClick() }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Listing"
                )
            }
        },
        topBar = {
            TopAppBar(
                title = { Text("College MarketPlace") }
            )
        }

    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(8.dp)
        ) {
            if (listings.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No listings found",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                LazyColumn {
                    item {
                        OutlinedTextField(
                            value = query,
                            onValueChange = { viewModel.updateSearchQuery(it) },
                            label = { Text("Search Listings") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null
                                )
                            },
                            trailingIcon = {
                                if (query.isNotEmpty()) {
                                    IconButton(onClick = {
                                        viewModel.updateSearchQuery("")
                                    }) {
                                        Icon(Icons.Default.Close, contentDescription = null)
                                    }
                                }
                            },
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                        )
                    }
                    item { Spacer(modifier= Modifier.height(10.dp)) }

                    item {
                        CategoryDropDown(
                            categories = listOf(
                                "All",
                                "Stationery",
                                "Sports Equipment",
                                "Home Appliances",
                                "Others"
                            ),
                            selectedCategory = selectedCategory ?: "All",
                            onCategorySelected = {
                                viewModel.updateSelectedCategory(
                                    if (it == "All") null else it
                                )
                            }
                        )
                    }
                    items(
                        items = listings,
                        key = { it.id })
                    { listing ->
                        ListingItem(
                            listing = listing,
                            onDelete = { viewModel.deleteListing(listing) },
                            onClick = {onListingClick(listing.id)},
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }
        }
    }
}
