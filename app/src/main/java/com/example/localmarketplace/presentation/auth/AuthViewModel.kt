package com.example.localmarketplace.presentation.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state: StateFlow<AuthState> = _state

    fun login(email: String, password: String) {
        _state.value = AuthState.Loading

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { _state.value = AuthState.Success }
            .addOnFailureListener { _state.value = AuthState.Error(it.message ?: "Login Failed") }
    }

    fun signup(email: String,password: String){
        _state.value = AuthState.Loading

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _state.value = AuthState.Success }
            .addOnFailureListener { _state.value = AuthState.Error(it.message ?: "SignUp Failed") }
    }

    fun logout(){
        auth.signOut()
        _state.value = AuthState.Idle
    }
}
