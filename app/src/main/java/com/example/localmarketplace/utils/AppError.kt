package com.example.localmarketplace.utils

//this is the list of all the possible errors which can happen
//and what domain do they belong to
sealed class AppError: Exception() {

    //Network errors
    object NoInternet : AppError()
    object TimeoutError : AppError()
    object ServerError : AppError()

    // Auth errors
    object InvalidCredentials : AppError()
    object UserNotFound : AppError()
    object EmailAlreadyExists : AppError()
    object WeakPassword : AppError()
    object SessionExpired : AppError()

    //  Firestore errors
    object PermissionDenied : AppError()
    object DocumentNotFound : AppError()
    object QuotaExceeded : AppError()

    // Storage/Upload errors
    object FileTooLarge : AppError()
    object InvalidFileType : AppError()
    object UploadFailed : AppError()

    //  Validation errors
    object EmptyTitle : AppError()
    object EmptyDescription : AppError()
    object EmptyPrice : AppError()
    object InvalidPrice : AppError()
    object NoImageSelected : AppError()
    object EmptyPhoneNumber : AppError()
    object InvalidPhoneNumber : AppError()

    // Fallback Errors
    data class Unknown(override val message: String?) : AppError()
}