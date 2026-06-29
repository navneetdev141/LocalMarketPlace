package com.example.localmarketplace.presentation.viewmodel

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localmarketplace.data.remote.CloudinaryService
import com.example.localmarketplace.data.remote.FirestoreService
import com.example.localmarketplace.domain.model.Listing
import com.example.localmarketplace.domain.ListingRepository
import com.example.localmarketplace.domain.WishlistRepository
import com.example.localmarketplace.presentation.components.ListingUiState
import com.example.localmarketplace.utils.AppErrorMapper
import com.example.localmarketplace.utils.NotificationHelper
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListingViewModel @Inject constructor(
    private val repository: ListingRepository,
    private val firestoreService: FirestoreService,
    private val wishlistRepository: WishlistRepository,
    private val cloudinaryService: CloudinaryService,
    private val auth: FirebaseAuth
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

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val authIdFlow = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.uid ?: "")
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), auth.currentUser?.uid ?: "")

    @OptIn(ExperimentalCoroutinesApi::class)
    val wishlistIds = authIdFlow.flatMapLatest { userId ->
        if (userId.isEmpty()) flowOf(emptySet())
        else wishlistRepository.getWishlistIds(userId)
    }
        .stateIn(
            scope = viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptySet()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val wishlistListings = authIdFlow.flatMapLatest { userId ->
        if (userId.isEmpty()) flowOf(emptyList())
        else wishlistRepository.getWishlistListings(userId)
    }
        .stateIn(
            scope = viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun toggleWishlist(listingId: String) {
        viewModelScope.launch {
            if (listingId in wishlistIds.value) {
                wishlistRepository.removeFromWishlist(listingId)
            }
            else {
                wishlistRepository.addToWishlist(listingId)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val listings = combine(searchQuery, selectedCategory, sortType, authIdFlow)
    { query, category, sort, userId ->
        val data = Triple(query, category, sort)
        data to userId
    }.flatMapLatest { (triple, userId) ->
        val (query, category, sort) = triple
        repository.getFilteredAndSortedListings(query, category, sort, userId)
    }.onEach { list ->
        Log.d("DEBUG_VM", "Listings emitted: ${list.size} items")
        _isLoading.value = false
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    suspend fun uploadImages(
        uris: List<Uri>,
        context: Context
    ): List<String> = coroutineScope {
        uris.map { uri ->
            async {
                cloudinaryService.uploadImage(uri, context)
            }
        }.awaitAll()
    }

    init {
        viewModelScope.launch {
            authIdFlow.collect { userId ->
                if (userId.isNotEmpty()) {
                    repository.syncListings()
                }
            }
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
                _uiState.value = ListingUiState.Error(AppErrorMapper.map(e))
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
    fun getListingById(
        id: String
    ): Flow<Listing?> {

        return repository.getListingById(id)
    }
    fun updateListing(listing: Listing) {
        viewModelScope.launch {
            _uiState.value = ListingUiState.Loading
            try {
                repository.updateListing(listing)

                _uiState.value = ListingUiState.Success(listings.value)
            } catch (e: Exception) {
                _uiState.value =
                    ListingUiState.Error(
                        AppErrorMapper.map(e)
                    )
            }
        }
    }
    fun markAsSold(listingId: String) {
        viewModelScope.launch {
            try {
                repository.markAsSold(listingId)
            } catch (e: Exception) {
                _uiState.value = ListingUiState.Error(
                    AppErrorMapper.map(e)
                )
            }
        }
    }
    fun markAsActive(listingId: String) {
        viewModelScope.launch {
            try {
                repository.markAsActive(listingId)
            }catch (e: Exception) {
                _uiState.value = ListingUiState.Error(
                    AppErrorMapper.map(e)
                )
            }
        }
    }
    fun getUserListingStats(userId: String): Flow<Triple<Int, Int, Int>> {
        return repository.getMyListings(userId).    map { listings ->
            val posted = listings.size
            val active = listings.count { it.isActive && !it.isSold }
            val sold   = listings.count { it.isSold }
            Triple(posted, active, sold)
        }
    }
}

