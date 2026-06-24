package com.example.localmarketplace.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.localmarketplace.presentation.auth.AuthViewModel
import com.example.localmarketplace.presentation.auth.LoginScreen
import com.example.localmarketplace.presentation.auth.SignupScreen
import com.example.localmarketplace.presentation.screens.AddListingScreen
import com.example.localmarketplace.presentation.screens.HomeScreen
import com.example.localmarketplace.presentation.screens.ListingDetailScreen
import com.example.localmarketplace.presentation.screens.MyListingsScreen
import com.example.localmarketplace.presentation.screens.ProfileScreen
import com.example.localmarketplace.presentation.viewmodel.ListingViewModel
import com.example.localmarketplace.presentation.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph(viewModel: ListingViewModel,profileViewModel: ProfileViewModel) {

    val navController = rememberNavController()
    val currentUser = FirebaseAuth.getInstance().currentUser

    val startDestination =
        if (currentUser != null) "home"
        else "login"

    NavHost(navController = navController, startDestination = startDestination) {


        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onAddClick = {
                    navController.navigate("add")
                },
                onListingClick = { id -> navController.navigate("detail/$id") },
                onMyListingsClick = { navController.navigate("myListings") },
                onLogoutClick = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                },
                onProfileClick = {
                    navController.navigate("profile")
                },
                profileViewModel = profileViewModel
            )
        }

        composable("myListings") {
            MyListingsScreen(
                viewModel = viewModel,
                onBack = {
                    navController.popBackStack()
                },
                onListingClick = { id -> navController.navigate("detail/$id") },
                onEditListing = {listingId -> navController.navigate("add?listingId=$listingId")}

            )
        }

        composable("add?listingId={listingId}") {backStackEntry ->

            val listingId = backStackEntry.arguments?.getString("listingId") ?: ""
            AddListingScreen(
                viewModel = viewModel,
                onListingAdded = {
                    navController.popBackStack()
                },
                listingId = listingId
            )
        }
        composable("detail/{listingId}") { backStackEntry ->

            val listingId = backStackEntry.arguments?.getString("listingId") ?: ""

            ListingDetailScreen(
                listingId = listingId,
                viewModel = viewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable("login") {
            val authViewModel: AuthViewModel = hiltViewModel()
            LoginScreen(
                authViewModel = authViewModel,
                navController = navController
            )
        }
        composable("signup") {
            val authViewModel: AuthViewModel = hiltViewModel()
            SignupScreen(
                authViewModel = authViewModel,
                navController = navController
            )
        }

        composable("profile"){
            val profileViewModel: ProfileViewModel =
                hiltViewModel()

            ProfileScreen(
                profileViewModel = profileViewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}