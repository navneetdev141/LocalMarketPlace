package com.example.localmarketplace.presentation.navigation

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
import com.example.localmarketplace.presentation.viewmodel.ListingViewModel

@Composable
fun AppNavGraph(viewModel: ListingViewModel) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {


        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onAddClick = {
                    navController.navigate("add")
                }
            )
        }
        composable("add") {
            AddListingScreen(
                viewModel = viewModel,
                onListingAdded = {
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
    }
}