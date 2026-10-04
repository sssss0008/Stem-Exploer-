package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = ElectricBlueLight,
    onPrimary = SpaceDarkBg,
    primaryContainer = ElectricBlueDark,
    onPrimaryContainer = Color.White,
    secondary = CosmicPurpleLight,
    onSecondary = SpaceDarkBg,
    secondaryContainer = CosmicPurpleDark,
    onSecondaryContainer = Color.White,
    tertiary = BrightCyan,
    onTertiary = SpaceDarkBg,
    background = SpaceDarkBg,
    onBackground = LightText,
    surface = SpaceCardBg,
    onSurface = LightText,
    surfaceVariant = SpaceSurfaceSubtle,
    onSurfaceVariant = LightText,
    outline = LightOutline
)

private val LightColorScheme = lightColorScheme(
    primary = ElectricBlue,
    onPrimary = Color.White,
    primaryContainer = ElectricBlueContainer,
    onPrimaryContainer = OnElectricBlueContainer,
    secondary = CosmicPurple,
    onSecondary = Color.White,
    secondaryContainer = CosmicPurpleContainer,
    onSecondaryContainer = OnCosmicPurpleContainer,
    tertiary = BrightCyan,
    onTertiary = Color.White,
    background = LightBackground,
    onBackground = DarkText,
    surface = LightSurface,
    onSurface = DarkText,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = MediumText,
    outline = LightOutlineVariant
)

@Composable
fun STEMExplorerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our intentional custom STEM palette
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
