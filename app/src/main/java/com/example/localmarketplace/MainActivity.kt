package com.example.localmarketplace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.*
import com.example.localmarketplace.presentation.screens.CreateListingScreen
import com.example.localmarketplace.presentation.screens.ListingScreen
import com.example.localmarketplace.presentation.screens.LoginScreen
import com.example.localmarketplace.presentation.screens.SignupScreen
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            val navController = rememberNavController()
            val currentUser = FirebaseAuth.getInstance().currentUser

            val startDestination = if (currentUser != null) { "listing" } else { "login" }

            NavHost(navController = navController, startDestination = startDestination) {

                composable("signup") { SignupScreen(navController) }
                composable("login") { LoginScreen(navController) }
                composable("listing") { ListingScreen(navController) }
                composable("create_listing") { CreateListingScreen(navController) }
                //composable("listing_detail/{listingId}") { ListingDetailScreen(listingEntity = ) }
            }
        }
    }
}