package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CaptionFlowColorScheme = darkColorScheme(
    primary = BrandAqua,
    onPrimary = BrandDarkCharcoal,
    primaryContainer = BrandAquaContainer,
    onPrimaryContainer = BrandAqua,
    secondary = BrandPink,
    onSecondary = Color.White,
    secondaryContainer = BrandPinkContainer,
    onSecondaryContainer = BrandPink,
    tertiary = BrandAqua,
    onTertiary = BrandDarkCharcoal,
    background = BrandDarkCharcoal,
    onBackground = BrandLightGrey,
    surface = BrandDarkCard,
    onSurface = BrandLightGrey,
    surfaceVariant = BrandDarkSurface,
    onSurfaceVariant = BrandSubtleText,
    outline = BrandBorder,
    outlineVariant = BrandBorder.copy(alpha = 0.5f),
    error = BrandPink,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CaptionFlowColorScheme,
        typography = Typography,
        content = content
    )
}
