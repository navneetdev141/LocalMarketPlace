package com.example.localmarketplace.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingItem(
    listing: Listing,
    modifier: Modifier = Modifier,
    isWishlisted: Boolean,
    onClick: () -> Unit,
    onWishlistClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(20.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
            pressedElevation = 4.dp
        ),
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
                Surface(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(34.dp)
                        .align(Alignment.TopEnd),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                    shadowElevation = 2.dp
                ) {
                    IconButton(
                        onClick = onWishlistClick,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = if (isWishlisted) Icons.Default.Favorite
                            else Icons.Default.FavoriteBorder,
                            contentDescription = if (isWishlisted) "Remove from wishlist"
                            else "Add to wishlist",
                            tint = if (isWishlisted) Color(0xFFF43F5E)
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(17.dp)
                        )
                    }
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
            }
        }
    }
}