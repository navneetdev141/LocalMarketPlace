package com.example.localmarketplace.presentation.listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localmarketplace.data.ListingEntity
import com.example.localmarketplace.data.ListingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListingViewModel @Inject constructor(private val listingRepository: ListingRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<ListingUiState>(ListingUiState.Idle)
    val uiState: StateFlow<ListingUiState> = _uiState

    fun insertListing(
        title: String,
        price: String,
        phoneNumber: String,
        description: String,
        category: String,
        imageUri: String?
    ) {
        viewModelScope.launch {
            _uiState.value = ListingUiState.Loading

            try {
                listingRepository.insertListing(
                    ListingEntity(
                        title = title,
                        price = price,
                        phoneNumber = phoneNumber,
                        description = description,
                        category = category,
                        imageUri = imageUri
                    )
                )

                _uiState.value = ListingUiState.Success

            } catch (e: Exception) {
                _uiState.value = ListingUiState.Error(
                    e.message ?: "Something went wrong"
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = ListingUiState.Idle
    }
}