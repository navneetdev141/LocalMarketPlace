package com.example.localmarketplace.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.localmarketplace.utils.toUserMessage

@Composable
fun SignupScreen(
    authViewModel: AuthViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val state by authViewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state) {
        when (state) {
            is AuthState.Success -> {
                navController.navigate("login") {
                    popUpTo("signup") { inclusive = true }
                }
            }
            is AuthState.Error -> {
                snackbarHostState.showSnackbar(
                    message = (state as AuthState.Error).error.toUserMessage(),
                    duration = SnackbarDuration.Short
                )
                authViewModel.clearError()
            }
            else -> Unit
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Sign Up",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = if (it.isBlank()) "Name is required" else null
                },
                label = { Text("Name") },
                singleLine = true,
                isError = nameError != null,
                supportingText = { nameError?.let { Text(it) } },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedLabelColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = {
                    phoneNumber = it
                    phoneError = when {
                        it.isBlank()           -> "Phone number is required"
                        !it.all(Char::isDigit) -> "Only digits allowed"
                        it.length != 10        -> "Must be 10 digits"
                        else                   -> null
                    }
                },
                label = { Text("Phone No.") },
                singleLine = true,
                isError = phoneError != null,
                supportingText = { phoneError?.let { Text(it) } },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedLabelColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = if (it.isBlank()) "Email is required" else null
                },
                label = { Text("Email") },
                singleLine = true,
                isError = emailError != null,
                supportingText = { emailError?.let { Text(it) } },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedLabelColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = when {
                        it.isBlank()   -> "Password is required"
                        it.length < 6  -> "Minimum 6 characters"
                        else           -> null
                    }
                },
                label = { Text("Password") },
                singleLine = true,
                isError = passwordError != null,
                supportingText = { passwordError?.let { Text(it) } },
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedLabelColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    // Validation
                    nameError = if (name.isBlank()) "Name is required" else null
                    phoneError = when {
                        phoneNumber.isBlank()           -> "Phone number is required"
                        !phoneNumber.all(Char::isDigit) -> "Only digits allowed"
                        phoneNumber.length != 10        -> "Must be 10 digits"
                        else                            -> null
                    }
                    emailError = if (email.isBlank()) "Email is required" else null
                    passwordError = when {
                        password.isBlank()  -> "Password is required"
                        password.length < 6 -> "Minimum 6 characters"
                        else                -> null
                    }

                    if (nameError == null && phoneError == null &&
                        emailError == null && passwordError == null) {
                        authViewModel.signup(name, email, password, phoneNumber)
                    }
                },
                enabled = state !is AuthState.Loading,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    if (state is AuthState.Loading) "Signing Up..." else "Sign Up",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            Spacer(Modifier.height(10.dp))

            TextButton(
                onClick = {
                    navController.navigate("login") {
                        popUpTo("signup") { inclusive = true }
                    }
                }
            ) {
                Text("Already have an account? Log In")
            }
        }

        if (state is AuthState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}