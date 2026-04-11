package com.example.localmarketplace.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localmarketplace.data.repository.ListingRepositoryImpl
import com.example.localmarketplace.domain.Listing
import com.example.localmarketplace.presentation.listing.ListingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListingViewModel @Inject constructor(private val repository: ListingRepositoryImpl) : ViewModel() {

    val listings = repository.getAllListings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init{
        viewModelScope.launch {
            repository.syncListings()
        }
    }

    fun addListing(listing: Listing){
        viewModelScope.launch {
            repository.addListing(listing)
        }
    }

    fun deleteListing(listing: Listing){
        viewModelScope.launch {
            repository.deleteListing(listing)
        }
    }
}

