package com.example.localmarketplace.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.unit.dp

@Composable
fun SortRow(
    selectedSort: String,
    onSortSelected: (String) -> Unit
){
    val sortOptions = listOf("latest", "price_low","price_high")

    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(sortOptions){sort ->
            FilterChip(
                selected = selectedSort == sort,

                onClick = {onSortSelected(sort)},

                label = { Text(
                    when(sort){
                        "latest" -> "Latest"
                        "price_low" -> "Price ↑"
                        else  -> "Price ↓"
                    }
                ) },

            )
        }
    }
}