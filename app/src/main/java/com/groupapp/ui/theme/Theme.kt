package com.groupapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Violet = Color(0xFF5B4BE0)
val VioletDeep = Color(0xFF332A8C)
val VioletSoft = Color(0xFFE6E2FF)
val Mint = Color(0xFF00B894)
val MintSoft = Color(0xFFD3F5EC)
val Amber = Color(0xFFFFB020)
val AmberSoft = Color(0xFFFFF0D6)
val Coral = Color(0xFFFF6B6B)

private val LightScheme = lightColorScheme(
    primary = Violet,
    onPrimary = Color.White,
    primaryContainer = VioletSoft,
    onPrimaryContainer = VioletDeep,
    secondary = Mint,
    onSecondary = Color.White,
    secondaryContainer = MintSoft,
    onSecondaryContainer = Color(0xFF00513F),
    tertiary = Amber,
    onTertiary = Color(0xFF3D2A00),
    tertiaryContainer = AmberSoft,
    onTertiaryContainer = Color(0xFF3D2A00),
    background = Color(0xFFF6F6FB),
    onBackground = Color(0xFF15141C),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF15141C),
    surfaceVariant = Color(0xFFECEBF4),
    onSurfaceVariant = Color(0xFF5B5972),
    outline = Color(0xFFD4D2E2),
    error = Coral,
    onError = Color.White
)

private val DarkScheme = darkColorScheme(
    primary = Color(0xFFB9B2FF),
    onPrimary = Color(0xFF2A2170),
    primaryContainer = Color(0xFF4136A8),
    onPrimaryContainer = Color(0xFFE6E2FF),
    secondary = Color(0xFF5CDCB8),
    onSecondary = Color(0xFF00382B),
    secondaryContainer = Color(0xFF00513F),
    onSecondaryContainer = Color(0xFFD3F5EC),
    tertiary = Color(0xFFFFC962),
    onTertiary = Color(0xFF3D2A00),
    tertiaryContainer = Color(0xFF5C4200),
    onTertiaryContainer = Color(0xFFFFF0D6),
    background = Color(0xFF121218),
    onBackground = Color(0xFFE7E5EF),
    surface = Color(0xFF1B1B23),
    onSurface = Color(0xFFE7E5EF),
    surfaceVariant = Color(0xFF2A2A35),
    onSurfaceVariant = Color(0xFFB6B3C6),
    outline = Color(0xFF44434F),
    error = Color(0xFFFF8A8A),
    onError = Color(0xFF3A0000)
)

@Composable
fun GroupAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkScheme else LightScheme,
        content = content
    )
}
