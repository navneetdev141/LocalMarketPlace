package com.example.localmarketplace.presentation.auth

import android.util.Log.e
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localmarketplace.data.remote.UserProfileDto
import com.example.localmarketplace.domain.model.UserProfile
import com.example.localmarketplace.domain.UserRepository
import com.example.localmarketplace.utils.AppError
import com.example.localmarketplace.utils.AppErrorMapper
import com.example.localmarketplace.utils.AppErrorMapper.map
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state: StateFlow<AuthState> = _state

    fun login(email: String, password: String) {
        _state.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { _state.value = AuthState.Success }
            .addOnFailureListener {
                _state.value = AuthState.Error(AppErrorMapper.map(it))
            }
    }

    fun signup(name: String, email: String, password: String, phoneNumber: String) {
        _state.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val user = result.user
                val profile = UserProfile(
                    userId = user?.uid ?: "",
                    name = name,
                    email = email,
                    phoneNumber = phoneNumber
                )
                viewModelScope.launch {
                    try {
                        userRepository.createProfile(profile)
                        _state.value = AuthState.Success
                    } catch (e: Exception) {
                        _state.value = AuthState.Error(AppErrorMapper.map(e))
                    }
                }
            }
            .addOnFailureListener {
                _state.value = AuthState.Error(AppErrorMapper.map(it))
            }


    }

    fun logout() {
        auth.signOut()
        _state.value = AuthState.Idle
    }

    fun clearError() {
        _state.value = AuthState.Idle
    }
}
