package com.example.localmarketplace.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localmarketplace.domain.model.UserProfile
import com.example.localmarketplace.domain.UserRepository
import com.example.localmarketplace.presentation.components.ListingUiState
import com.example.localmarketplace.utils.AppError
import com.example.localmarketplace.utils.AppErrorMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    private val _profile = MutableStateFlow(UserProfile())
    val profile: StateFlow<UserProfile> = _profile
    //can also be written as
    //val profile = _profile.asStateFlow()
    //this is rather the modern way, but we wrote that way to maintain consistency

    private val _error = MutableStateFlow<AppError?>(null)
    val error: StateFlow<AppError?> = _error

    fun getProfile(userId: String) {
        viewModelScope.launch {
            try {
                _profile.value = userRepository.getProfile(userId)
            } catch (e: Exception) {
                _error.value = AppErrorMapper.map(e)
            }
        }
    }
}