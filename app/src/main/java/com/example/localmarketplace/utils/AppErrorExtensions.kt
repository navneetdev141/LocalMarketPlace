package com.example.localmarketplace.utils

//it translates the error into plain language for the user
fun AppError.toUserMessage(): String {
    return when (this) {

        // Network
        AppError.NoInternet         -> "No internet connection. Please check your network."
        AppError.TimeoutError       -> "Request timed out. Please try again."
        AppError.ServerError        -> "Server is unavailable. Please try later."

        // Auth
        AppError.InvalidCredentials -> "Incorrect email or password."
        AppError.UserNotFound       -> "No account found with this email."
        AppError.EmailAlreadyExists -> "An account with this email already exists."
        AppError.WeakPassword       -> "Password must be at least 6 characters."
        AppError.SessionExpired     -> "Your session has expired. Please log in again."

        // Firestore
        AppError.PermissionDenied   -> "You don't have permission to do this."
        AppError.DocumentNotFound   -> "The requested item was not found."
        AppError.QuotaExceeded      -> "Too many requests. Please wait a moment."

        // Upload
        AppError.FileTooLarge       -> "Image is too large. Please choose a smaller one."
        AppError.InvalidFileType    -> "Invalid file type. Please choose an image."
        AppError.UploadFailed       -> "Image upload failed. Please try again."

        // Validation
        AppError.EmptyTitle         -> "Please enter a title for your listing."
        AppError.EmptyDescription   -> "Please enter a description."
        AppError.EmptyPrice         -> "Please enter a price."
        AppError.InvalidPrice       -> "Please enter a valid price."
        AppError.NoImageSelected    -> "Please select at least one image."
        AppError.EmptyPhoneNumber   -> "Please enter your phone number."
        AppError.InvalidPhoneNumber -> "Please enter a valid 10-digit phone number."

        // Fallback
        is AppError.Unknown         -> message ?: "Something went wrong. Please try again."
    }
}

fun AppError.toTitle(): String {
    return when (this) {
        AppError.NoInternet,
        AppError.TimeoutError,
        AppError.ServerError        -> "Connection Problem"

        AppError.InvalidCredentials,
        AppError.UserNotFound,
        AppError.EmailAlreadyExists,
        AppError.WeakPassword,
        AppError.SessionExpired     -> "Authentication Error"

        AppError.PermissionDenied,
        AppError.DocumentNotFound,
        AppError.QuotaExceeded      -> "Access Error"

        AppError.FileTooLarge,
        AppError.InvalidFileType,
        AppError.UploadFailed       -> "Upload Error"

        AppError.EmptyTitle,
        AppError.EmptyDescription,
        AppError.EmptyPrice,
        AppError.InvalidPrice,
        AppError.NoImageSelected,
        AppError.EmptyPhoneNumber,
        AppError.InvalidPhoneNumber -> "Validation Error"

        is AppError.Unknown         -> "Error"
    }
}