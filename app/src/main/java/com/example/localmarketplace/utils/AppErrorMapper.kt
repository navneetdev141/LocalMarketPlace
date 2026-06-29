package com.example.localmarketplace.utils

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestoreException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

//it converts the given error into a specific error type
object AppErrorMapper {

    fun map(throwable: Throwable): AppError {
        return when (throwable) {

            //Network
            is SocketTimeoutException -> AppError.TimeoutError
            is UnknownHostException,
            is IOException -> AppError.NoInternet


            //Firebase Auth
            is FirebaseAuthWeakPasswordException
                -> AppError.WeakPassword

            is FirebaseAuthInvalidCredentialsException
                -> AppError.InvalidCredentials

            is FirebaseAuthInvalidUserException
                -> AppError.UserNotFound

            is FirebaseAuthUserCollisionException
                -> AppError.EmailAlreadyExists

            is FirebaseNetworkException
                -> AppError.NoInternet

            is FirebaseTooManyRequestsException
                -> AppError.QuotaExceeded

            //Firestore
            is FirebaseFirestoreException -> {
                when (throwable.code) {
                    FirebaseFirestoreException.Code.PERMISSION_DENIED
                        -> AppError.PermissionDenied
                    FirebaseFirestoreException.Code.NOT_FOUND
                        -> AppError.DocumentNotFound
                    FirebaseFirestoreException.Code.RESOURCE_EXHAUSTED
                        -> AppError.QuotaExceeded
                    FirebaseFirestoreException.Code.UNAVAILABLE
                        -> AppError.ServerError
                    else
                         -> AppError.Unknown(throwable.message)
                }
            }

            //Fallback
            else -> AppError.Unknown(throwable.message)
        }
    }
}