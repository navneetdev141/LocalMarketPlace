package com.example.localmarketplace.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.localmarketplace.R
import com.example.localmarketplace.domain.model.Listing
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyListingItem(
    listing: Listing,
    onDelete: () -> Unit,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            shape = RoundedCornerShape(20.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text(
                    "Delete Listing",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            },
            text = {
                Text(
                    "Are you sure you want to delete \"${listing.title}\"? This cannot be undone.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                Button(
                    onClick = { onDelete(); showDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancel") }
            }
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(20.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp, pressedElevation = 4.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
            ) {
                AsyncImage(
                    model = listing.imageUrls.firstOrNull(),
                    contentDescription = listing.title,
                    placeholder = painterResource(R.drawable.placeholder),
                    error = painterResource(R.drawable.placeholder),
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.55f))
                            )
                        )
                )
                Surface(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.TopStart),
                    shape = RoundedCornerShape(50.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.95f),
                    shadowElevation = 2.dp
                ) {
                    Text(
                        text = listing.category,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = listing.title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = "₹${listing.price}",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                if (listing.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = listing.description,
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 17.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (listing.userId == currentUserId) {
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant,
                        thickness = 1.dp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showDialog = true },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
                            ),
                            contentPadding = PaddingValues(vertical = 10.dp)
                        ) {
                            Icon(
                                Icons.Default.Delete, null,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(Modifier.width(5.dp))
                            Text(
                                "Delete",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }

                        Button(
                            onClick = onEdit,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(vertical = 10.dp)
                        ) {
                            Icon(
                                Icons.Default.Edit, null,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(Modifier.width(5.dp))
                            Text(
                                "Edit",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}