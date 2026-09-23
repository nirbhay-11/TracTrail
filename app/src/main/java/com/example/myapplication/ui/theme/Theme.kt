package com.example.myapplication.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = CustomSoftSlate,             // #748cab - Primary Accent
    onPrimary = CustomDarkNavy,            // #0d1321 - Text on Primary
    primaryContainer = CustomMidnightBlue, // #1d2d44 - Card/Container
    onPrimaryContainer = CustomWarmCream,  // #f0ebd8 - Text/Icons on Container

    secondary = CustomSteelBlue,           // #3e5c76
    onSecondary = CustomWarmCream,         // #f0ebd8
    secondaryContainer = CustomSteelBlue,  // #3e5c76
    onSecondaryContainer = CustomWarmCream,

    tertiary = CustomSoftSlate,            // #748cab
    onTertiary = CustomDarkNavy,

    background = CustomDarkNavy,           // #0d1321 - App Background
    onBackground = CustomWarmCream,        // #f0ebd8 - App Text

    surface = CustomMidnightBlue,          // #1d2d44 - Cards & Navigation
    onSurface = CustomWarmCream,           // #f0ebd8 - Text & Icons on Surface

    surfaceVariant = CustomSteelBlue,      // #3e5c76 - Secondary Chips/Containers
    onSurfaceVariant = CustomWarmCream,    // #f0ebd8 - Subtitle text & Secondary Icons

    outline = CustomSoftSlate,             // #748cab - Outlines
    outlineVariant = CustomSteelBlue
)

private val LightColorScheme = lightColorScheme(
    primary = LightSkyBlue,                // #5aa9e6 - Primary Accent
    onPrimary = Color.White,
    primaryContainer = LightSoftBlue.copy(alpha = 0.3f), // #7fc8f8
    onPrimaryContainer = Color(0xFF1E293B),

    secondary = LightCoralPink,            // #ff6392 - Accent
    onSecondary = Color.White,
    secondaryContainer = LightCoralPink.copy(alpha = 0.15f),
    onSecondaryContainer = LightCoralPink,

    tertiary = LightWarmYellow,            // #ffe45e - Streak / Special
    onTertiary = Color(0xFF1E293B),

    background = LightOffWhite,            // #f9f9f9 - Background
    onBackground = Color(0xFF1E293B),      // Text

    surface = Color.White,                 // #f9f9f9
    onSurface = Color(0xFF1E293B),

    surfaceVariant = LightSoftBlue.copy(alpha = 0.2f),
    onSurfaceVariant = Color(0xFF334155),

    outline = LightSkyBlue,
    outlineVariant = LightSoftBlue
)

@Composable
fun MyApplicationTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
