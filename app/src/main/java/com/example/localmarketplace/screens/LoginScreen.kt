package com.example.localmarketplace.screens


import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth


@Composable
fun LoginScreen(navController: NavController, modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Login", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            val context = LocalContext.current

            Button(onClick = {
                if (email.isBlank() || password.isBlank()){
                    Toast.makeText(context, "Please enter an email and password", Toast.LENGTH_SHORT).show()
                }else{
                    Firebase.auth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                Toast.makeText(context, "Login Successful ", Toast.LENGTH_SHORT).show()
                                navController.navigate("create_listing"){
                                    popUpTo("login"){inclusive = true}
                                }
                            }
                            else{
                                Toast.makeText(context, task.exception?.message?: "Login Failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                }

            },modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Login")
            }

            Spacer(modifier = Modifier.height(10.dp))

            TextButton(onClick = {
                navController.navigate("signup") {
                    popUpTo("login") { inclusive = true }
                }
            }) {
                Text(text = "Don't have an account? Sign Up")
            }

        }
    }
}