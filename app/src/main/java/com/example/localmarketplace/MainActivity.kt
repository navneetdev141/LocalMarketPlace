package com.example.localmarketplace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.localmarketplace.presentation.navigation.AppNavGraph
import com.example.localmarketplace.presentation.viewmodel.ListingViewModel
import com.example.localmarketplace.ui.theme.LocalMarketPlaceTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LocalMarketPlaceTheme {
                val viewModel: ListingViewModel = hiltViewModel()
                AppNavGraph(viewModel = viewModel)
            }
        }
    }
}
