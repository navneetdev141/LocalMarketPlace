package com.example.localmarketplace.presentation.viewmodel

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localmarketplace.data.remote.CloudinaryService
import com.example.localmarketplace.data.remote.FirestoreService
import com.example.localmarketplace.data.repository.ListingRepositoryImpl
import com.example.localmarketplace.domain.Listing
import com.example.localmarketplace.domain.ListingRepository
import com.example.localmarketplace.presentation.listing.ListingUiState
import com.example.localmarketplace.utils.NotificationHelper
import dagger.Provides
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListingViewModel @Inject constructor(
    private val repository: ListingRepository,
    private val firestoreService: FirestoreService,
    private val cloudinaryService: CloudinaryService
) : ViewModel() {

    private val _uiState = MutableStateFlow<ListingUiState>(ListingUiState.Idle)
    val uiState: StateFlow<ListingUiState> = _uiState

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory

    private val _searchQuery = MutableStateFlow("")
    var searchQuery: StateFlow<String> = _searchQuery

    private val _sortType = MutableStateFlow("latest")
    val sortType: StateFlow<String> = _sortType

    private val seenIds = mutableSetOf<String>()
    private val sessionStartTime = System.currentTimeMillis()

    @OptIn(ExperimentalCoroutinesApi::class)
    val listings = combine(searchQuery, selectedCategory, sortType)
    { query, category, sort -> Triple(query, category, sort)
    }.flatMapLatest { (query, category,sort) ->
        repository.getFilteredAndSortedListings(query, category,sort)
    }.onEach { list ->
        android.util.Log.d("DEBUG_VM", "Listings emitted: ${list.size} items")
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    suspend fun uploadImages(uris: List<Uri>, context: Context): List<String> {
        val urls = mutableListOf<String>()

        for (uri in uris) {
            val url = cloudinaryService.uploadImage(uri, context)
            urls.add(url)
        }
        return urls
    }

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

    fun updateSortType(sort: String) {
        _sortType.value = sort
    }

    fun addListing(listing: Listing) {
        viewModelScope.launch {
            _uiState.value = ListingUiState.Loading
            try {
                repository.addListing(listing)
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

    fun resetState() {
        _uiState.value = ListingUiState.Idle
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun observeNewListings(context: Context) {
        firestoreService.listenToNewListings { listingDto ->
            if (!seenIds.contains(listingDto.id) && listingDto.createdAt > sessionStartTime) {
                seenIds.add(listingDto.id)
                NotificationHelper.showNotification(
                    context,
                    "New Listing",
                    listingDto.title
                )
            } else {
                seenIds.add(listingDto.id)
            }
        }
    }

    fun getMyListings(userId: String): Flow<List<Listing>> {
        return repository.getMyListings(userId)

    }
}

