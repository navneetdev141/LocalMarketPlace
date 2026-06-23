package com.example.localmarketplace.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localmarketplace.domain.UserProfile
import com.example.localmarketplace.domain.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel() {

    private val _profile = MutableStateFlow(UserProfile())
    val profile : StateFlow<UserProfile> = _profile
    //can also be written as
    //val profile = _profile.asStateFlow()
    //this is rather the modern way, but we wrote that way to maintain consistency

    fun getProfile(userId:String){
        viewModelScope.launch {
            _profile.value = userRepository.getProfile(userId)
        }
    }
}