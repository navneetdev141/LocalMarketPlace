package com.example.localmarketplace.presentation.listing

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localmarketplace.data.ListingEntity
import com.example.localmarketplace.data.repository.ListingRepository
import com.example.localmarketplace.domain.Listing
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListingViewModel @Inject constructor(private val listingRepository: ListingRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<ListingUiState>(ListingUiState.Idle)
    val uiState: StateFlow<ListingUiState> = _uiState

    fun resetState() {
        _uiState.value = ListingUiState.Idle
    }


    val listings = listingRepository.getAllListings()

    fun insertListing(title: String,description: String,category: String, price: String, phoneNumber: String,imageUri: Uri?) {
        viewModelScope.launch {
            val listing = ListingEntity(
                title = title,
                description = description,
                category = category,
                price = price,
                phoneNumber = phoneNumber,
                imageUrl = imageUri.toString()
            )
            listingRepository.insertListing(listing)
        }
    }

}