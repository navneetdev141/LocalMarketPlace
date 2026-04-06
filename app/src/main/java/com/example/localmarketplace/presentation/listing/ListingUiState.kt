package com.example.localmarketplace.presentation.listing

sealed class ListingUiState {
    object Idle : ListingUiState()
    object Loading : ListingUiState()
    object Success : ListingUiState()
    data class Error(val message: String) : ListingUiState()
}