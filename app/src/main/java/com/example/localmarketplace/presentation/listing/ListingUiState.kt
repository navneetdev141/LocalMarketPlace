package com.example.localmarketplace.presentation.listing

import com.example.localmarketplace.domain.model.Listing

sealed class ListingUiState {
    object Idle : ListingUiState()
    object Loading : ListingUiState()
    data class Success(val data: List<Listing>) : ListingUiState()
    data class Error(val message: String) : ListingUiState()
}