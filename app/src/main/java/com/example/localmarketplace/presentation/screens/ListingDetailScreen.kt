package com.example.localmarketplace.presentation.screens
//
//import android.net.Uri
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import coil.compose.AsyncImage
//import com.example.localmarketplace.data.local.ListingEntity
//import com.example.localmarketplace.presentation.viewmodel.ListingViewModel
//
//@Composable
//fun ListingDetailScreen(listingEntity: ListingEntity,navController: NavController, viewModel: ListingViewModel = hiltViewModel()) {
//
//
//    var title by remember { mutableStateOf("title") }
//    var description by remember { mutableStateOf("") }
//    var price by remember { mutableStateOf("") }
//    var imageUri by remember { mutableStateOf<Uri?>(null) }
//
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        imageUri =  uri
//    }
//    val imagePath = imageUri.toString()
//
//    Box(modifier = Modifier.fillMaxWidth()
//        .padding(10.dp)
//    ) {
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        )
//        {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(Color.Transparent)
//                    .padding(50.dp)
//                , horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.Top
//            ) {
//                Text("Edit Product", fontSize = 50.sp, color = Color.Black)
//            }
//            Spacer(modifier = Modifier.height(12.dp))
//
//            // Preview image
//            imageUri?.let{ uri ->
//                AsyncImage(
//                    model = uri,
//                    contentDescription = null,
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier.size(200.dp)
//                )
//            }
//            Spacer(modifier = Modifier.height(5.dp))
//
//            Button(
//                onClick = { launcher.launch("image/*") },
//            ) {
//                Text(text = "Choose Image")
//            }
//            OutlinedTextField(
//                value = title,
//                onValueChange = { title = it },
//                label = { Text("Enter the Product's Name ") },
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(12.dp)
//            )
//            Spacer(modifier = Modifier.height(12.dp))
//
//
//            OutlinedTextField(
//                value = description,
//                onValueChange = { description = it },
//                label = { Text("Describe the Product ") },
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(12.dp),
//                maxLines = 4
//            )
//            Spacer(modifier = Modifier.height(12.dp))
//
//
//            OutlinedTextField(
//                value = price,
//                onValueChange = { price = it },
//                label = { Text("Enter the price you want for this product ") },
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(12.dp)
//            )
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            Button(
//                onClick = {
//                    viewModel.deleteListing(listingEntity).also { navController.navigate("listing") }
//                }
//            ) {
//                Text("Delete Listing")
//            }
//        }
//    }
//}