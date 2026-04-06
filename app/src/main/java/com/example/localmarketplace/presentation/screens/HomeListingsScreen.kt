package com.example.localmarketplace.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.localmarketplace.presentation.components.ListingItem
import com.example.localmarketplace.presentation.listing.ListingViewModel
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun HomeListingsScreen(
//    navController: NavController,
//    viewModel: ListingViewModel = hiltViewModel(),
//    onAddClick: () -> Unit = {
//        navController.navigate("create_listing")
//    }
//) {
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Listings") }
//            )
//        },
//        floatingActionButton = {
//            FloatingActionButton(onClick = { onAddClick() }) {
//                Text("+")
//            }
//        }
//    ) { padding ->
//
//        LazyColumn(
//            contentPadding = padding,
//            verticalArrangement = Arrangement.spacedBy(12.dp),
//            modifier = Modifier.padding(12.dp)
//        ) {
//
//            items(listings) { listing ->
//                ListingItem(
//                    listing = listing,
//                    onClick = { navController.navigate("listing_detail/${listing.id}") }
//                )
//            }
//
//        }
//    }
//}