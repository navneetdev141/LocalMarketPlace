package com.example.localmarketplace.presentation.screens

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.localmarketplace.presentation.components.CategoryDropDown
import com.example.localmarketplace.presentation.components.ListingItem
import com.example.localmarketplace.presentation.viewmodel.ListingViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(viewModel: ListingViewModel, onAddClick: () -> Unit) {

    val listings by viewModel.listings.collectAsState()
    val query by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if(isGranted){
                viewModel.observeNewListings(context)
            }
        }
    )
    LaunchedEffect(Unit) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            launcher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
        viewModel.observeNewListings(context)
    }


    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Column(modifier = Modifier
            .padding(16.dp)
            .padding(top = 35.dp)) {
            Button(onClick = onAddClick, modifier = Modifier.fillMaxWidth()) {
                Text("Go to Add Screen")
            }
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = query,
                onValueChange = {viewModel
                    .updateSearchQuery(it)},
                label = { Text("Search") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))

            CategoryDropDown(
                categories = listOf("All","Stationery", "Sports Equipment", "Home Appliances", "Others"),
                selectedCategory = selectedCategory ?: "All",
                onCategorySelected = {
                    viewModel.updateSelectedCategory(
                        if(it == "All") null else it
                    )
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn {
                items(listings) { listing ->
                    ListingItem(
                        listing = listing,
                        onDelete = { viewModel.deleteListing(listing) }
                    )
                }
            }
        }
    }
}
