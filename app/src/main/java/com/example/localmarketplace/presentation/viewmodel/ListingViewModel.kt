package com.example.localmarketplace.presentation.viewmodel

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localmarketplace.data.remote.FirestoreService
import com.example.localmarketplace.data.repository.ListingRepositoryImpl
import com.example.localmarketplace.domain.Listing
import com.example.localmarketplace.presentation.listing.ListingUiState
import com.example.localmarketplace.utils.NotificationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListingViewModel @Inject constructor(private val repository: ListingRepositoryImpl,private val firestoreService: FirestoreService) : ViewModel() {

    private val _uiState = MutableStateFlow<ListingUiState>(ListingUiState.Idle)
    val uiState: StateFlow<ListingUiState> = _uiState

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory : StateFlow<String?> = _selectedCategory

    private val _searchQuery = MutableStateFlow("")
    var searchQuery : StateFlow<String> = _searchQuery

    private val seenIds = mutableSetOf<String>()
    private val sessionStartTime = System.currentTimeMillis()

    @OptIn(ExperimentalCoroutinesApi::class)
    val listings = combine(searchQuery, selectedCategory) { query, category ->
        query to category
    }.flatMapLatest { (query, category) ->
        repository.getListings(query, category)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            repository.syncListings()
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateSelectedCategory(category: String?) {
        _selectedCategory.value = category
    }

    fun addListing(listing: Listing, imageUri: Uri?, context: Context) {
        viewModelScope.launch {
            _uiState.value = ListingUiState.Loading
            try {
                repository.addListing(listing, imageUri, context)
                _uiState.value = ListingUiState.Success(listings.value)
            } catch (e: Exception) {
                _uiState.value = ListingUiState.Error(e.message ?: "Upload Failed")
            }

        }
    }

    fun deleteListing(listing: Listing) {
        viewModelScope.launch {
            repository.deleteListing(listing)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun observeNewListings(context: Context){
        firestoreService.listenToNewListings { listingDto ->
            if (!seenIds.contains(listingDto.id) && listingDto.createdAt > sessionStartTime) {
                seenIds.add(listingDto.id)
                NotificationHelper.showNotification(
                    context,
                    "New Listing",
                    listingDto.title
                )
            }
            else{
                seenIds.add(listingDto.id)
            }
        }
    }
}

