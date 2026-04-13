package com.example.localmarketplace.presentation.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.localmarketplace.presentation.components.ListingItem
import com.example.localmarketplace.presentation.viewmodel.ListingViewModel


@Composable
fun HomeScreen(viewModel: ListingViewModel){

    val listings by viewModel.listings.collectAsState()

    LazyColumn {
        items(listings){listing ->
            ListingItem(listing = listing,
                onDelete = {viewModel.deleteListing(listing)})
        }
    }
}