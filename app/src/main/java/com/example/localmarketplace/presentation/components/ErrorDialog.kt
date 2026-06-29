package com.example.localmarketplace.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.localmarketplace.utils.AppError
import com.example.localmarketplace.utils.toTitle
import com.example.localmarketplace.utils.toUserMessage

@Composable
fun ErrorDialog(
    error: AppError,
    onDismiss: () -> Unit,
    onRetry: (() -> Unit)? = null
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Text(
                error.toTitle(),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
        },
        text = {
            Text(
                error.toUserMessage(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        confirmButton = {
            if (onRetry != null) {
                Button(
                    onClick = { onRetry(); onDismiss() },
                    shape = RoundedCornerShape(10.dp)
                ) { Text("Retry") }
            } else {
                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(10.dp)
                ) { Text("OK") }
            }
        },
        dismissButton = {
            if (onRetry != null) {
                TextButton(onClick = onDismiss) { Text("Cancel") }
            }
        }
    )
}
