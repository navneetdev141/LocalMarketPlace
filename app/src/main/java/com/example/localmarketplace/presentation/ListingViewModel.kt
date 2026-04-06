package com.example.localmarketplace.presentation

import android.R.attr.phoneNumber
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localmarketplace.data.ListingEntity
import com.example.localmarketplace.data.ListingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListingViewModel @Inject constructor(private val listingRepository: ListingRepository) : ViewModel() {

    val listings = listingRepository.getAllListings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun insertListing(title: String, description: String,category: String, price: String, phoneNumber: String, imageUri: String?){
        viewModelScope.launch{
            listingRepository.insertListing(
                ListingEntity(
                    title = title,
                    description = description,
                    category = category,
                    price = price,
                    phoneNumber = phoneNumber,
                    imageUri = imageUri
                )
            )
        }
    }

    fun deleteListing(listing: ListingEntity){
        viewModelScope.launch {
            listingRepository.deleteListing(listing)
        }
    }
}