package com.example.localmarketplace.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ── Blue/Indigo palette ──────────────────────────────────────────
val Indigo50  = Color(0xFFEEF2FF)
val Indigo100 = Color(0xFFE0E7FF)
val Indigo200 = Color(0xFFC7D2FE)
val Indigo400 = Color(0xFF818CF8)
val Indigo500 = Color(0xFF6366F1)
val Indigo600 = Color(0xFF4F46E5)
val Indigo700 = Color(0xFF4338CA)
val Indigo900 = Color(0xFF312E81)

val Slate100 = Color(0xFFF1F5F9)
val Slate200 = Color(0xFFE2E8F0)
val Slate400 = Color(0xFF94A3B8)
val Slate500 = Color(0xFF64748B)
val Slate700 = Color(0xFF334155)
val Slate800 = Color(0xFF1E293B)
val Slate850 = Color(0xFF172033)
val Slate900 = Color(0xFF0F172A)
val Slate950 = Color(0xFF080D18)

private val LightColors = lightColorScheme(
    primary            = Indigo600,
    onPrimary          = Color.White,
    primaryContainer   = Indigo100,
    onPrimaryContainer = Indigo700,
    secondary          = Indigo500,
    onSecondary        = Color.White,
    tertiary           = Indigo400,
    onTertiary         = Color.White,
    background         = Slate100,
    onBackground       = Slate900,
    surface            = Color.White,
    onSurface          = Slate800,
    surfaceVariant     = Slate100,
    onSurfaceVariant   = Slate500,
    outline            = Slate200,
    outlineVariant     = Slate200,
    error              = Color(0xFFDC2626),
    onError            = Color.White,
    errorContainer     = Color(0xFFFEE2E2),
    onErrorContainer   = Color(0xFF7F1D1D),
)

private val DarkColors = darkColorScheme(
    primary            = Indigo400,
    onPrimary          = Slate900,
    primaryContainer   = Indigo900,
    onPrimaryContainer = Indigo200,
    secondary          = Indigo500,
    onSecondary        = Color.White,
    tertiary           = Indigo400,
    onTertiary         = Slate900,
    background         = Slate950,
    onBackground       = Slate100,
    surface            = Slate900,       // card background
    onSurface          = Slate100,
    surfaceVariant     = Slate800,       // subtle containers
    onSurfaceVariant   = Slate400,
    outline            = Slate700,
    outlineVariant     = Slate700,
    error              = Color(0xFFF87171),
    onError            = Slate900,
    errorContainer     = Color(0xFF7F1D1D),
    onErrorContainer   = Color(0xFFFEE2E2),
)

@Composable
fun LocalMarketPlaceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}