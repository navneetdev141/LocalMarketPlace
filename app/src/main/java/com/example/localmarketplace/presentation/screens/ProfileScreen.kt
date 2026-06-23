package com.example.localmarketplace.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.localmarketplace.presentation.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel) {
    val profile by profileViewModel.profile.collectAsState()

    val currentUser = FirebaseAuth.getInstance().currentUser

    LaunchedEffect(Unit) {
        currentUser?.uid?.let {
            profileViewModel.getProfile(it)
        }
    }
}