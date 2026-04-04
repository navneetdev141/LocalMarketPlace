package com.example.localmarketplace.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.localmarketplace.presentation.ListingViewModel

@Composable
fun ListingScreen(navController: NavController, viewModel: ListingViewModel = hiltViewModel()) {

    val listings by viewModel.listings.collectAsState(initial = emptyList())

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(listings) { item ->
            Text(text = item.title, modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}