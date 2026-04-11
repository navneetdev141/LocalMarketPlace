package com.example.localmarketplace.presentation.listing

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localmarketplace.data.local.ListingEntity
import com.example.localmarketplace.data.repository.ListingRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListingViewModel @Inject constructor(private val repository: ListingRepositoryImpl) : ViewModel() {

    private val _uiState = MutableStateFlow<ListingUiState>(ListingUiState.Idle)
    val uiState: StateFlow<ListingUiState> = _uiState

    fun resetState() {
        _uiState.value = ListingUiState.Idle
    }

    val listings = repository.getAllListings().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

//    fun insertListing(title: String,description: String,category: String, price: Double, phoneNumber: String,imageUri: String,) {
//        viewModelScope.launch {
//            val listing = ListingEntity(
//                title = title,
//                description = description,
//                category = category,
//                price = price,
//                phoneNumber = phoneNumber,
//                imageUri = imageUri,
//            )
//            listingRepository.insertListing(listing)
//        }
//    }



}