package com.alqiran.quraanapp.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    background = Color(0xFF131313),
    surface = Color(0xFF181F2B),
    primary = Color(0xFFF8FAFC),
    onPrimary = Color(0xFF131313),
    secondary = Color(0xFF52E1ED),
    onSecondary = Color(0xFF00363a),
    tertiary = Color(0xFF00BFA6),
    onTertiary = Color(0xFF002921),
    onBackground = Color(0xFFF8FAFC),
    onSurface = Color(0xFFE2E8F0),
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF334155),
    inversePrimary = Color(0xFF3B82F6),
    inverseSurface = Color(0xFFEFF3F8),
    inverseOnSurface = Color(0xFF1E293B),
    error = Color(0xFFCF6679),
    onError = Color(0xFF000000),
    primaryContainer = Color(0xFF00BCD4),
    secondaryContainer = Color(0xFF00E777),
    surfaceTint = Color(0xFF2A3444)
)

private val LightColorScheme = lightColorScheme(
    background = Color(0xFFF8FAFC),
    surface = Color(0xFFEFF3F8),
    primary = Color(0xFF131313),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF3B82F6),
    onSecondary = Color(0xFFFFFFFF),
    tertiary = Color(0XFF00BFA6),
    onTertiary = Color(0xFFFFFFFF),
    onBackground = Color(0xFF131313),
    onSurface = Color(0xFF1E293B),
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFF94A3B8),
    inversePrimary = Color(0xFF52E1ED),
    inverseSurface = Color(0xFF1E293B),
    inverseOnSurface = Color(0xFFF8FAFC),
    error = Color(0xFFB00020),
    onError = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF00BCD4),
    secondaryContainer = Color(0xFF00E676),
    surfaceTint = Color(0xFFE2E8F0)
)


@Composable
fun QuraanAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}