package com.example.localmarketplace.presentation.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.localmarketplace.R
import com.example.localmarketplace.presentation.viewmodel.ListingViewModel
import org.jetbrains.annotations.Async

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingDetailScreen(
    listingId: String,
    viewModel: ListingViewModel,
    onBack: () -> Unit
) {
    val listing by viewModel
        .getListingById(listingId)
        .collectAsState(initial = null)
    val pagerState = rememberPagerState(pageCount = { listing?.imageUrls?.size ?: 0 })

    if (listing == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val currentListing = listing!!

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Listing Details") },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
                .fillMaxSize()
        ) {
            if (currentListing.imageUrls.isNotEmpty()) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                ) { page ->
                    AsyncImage(
                        model = currentListing.imageUrls[page],
                        contentDescription = null,
                        placeholder = painterResource(R.drawable.placeholder),
                        error = painterResource(R.drawable.placeholder),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            } else {
                Text("No Images available")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            )
            {
                repeat(currentListing.imageUrls.size) { index ->

                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .padding(2.dp)
                            .background(
                                if (pagerState.currentPage == index) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    Color.Gray
                                },
                                CircleShape
                            )
                    )
                }
            }
            Row(modifier = Modifier.fillMaxWidth()
                , horizontalArrangement = Arrangement.SpaceBetween)
            {
                Text(
                    text = currentListing.title,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "₹${currentListing.price}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = currentListing.description)

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Category: ${currentListing.category}")

            Spacer(modifier = Modifier.height(16.dp))

            val context = LocalContext.current

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    val url = "https://wa.me/${currentListing.phoneNumber}"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Contact Seller")
            }

        }
    }
}