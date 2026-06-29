package com.example.localmarketplace.presentation.auth

import com.example.localmarketplace.utils.AppError

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val error: AppError) : AuthState()
}