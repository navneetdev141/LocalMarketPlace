package com.example.localmarketplace.presentation.components

import com.example.localmarketplace.domain.model.Listing
import com.example.localmarketplace.utils.AppError

sealed class ListingUiState {
    object Idle : ListingUiState()
    object Loading : ListingUiState()
    data class Success(val data: List<Listing>) : ListingUiState()
    data class Error(val error: AppError) : ListingUiState()
}