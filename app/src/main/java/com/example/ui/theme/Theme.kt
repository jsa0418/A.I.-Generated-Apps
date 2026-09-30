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

private val DarkColorScheme =
  darkColorScheme(
    primary = NaturalSproutAccent,
    onPrimary = NaturalOnSprout,
    primaryContainer = NaturalPrimary,
    onPrimaryContainer = NaturalBg,
    secondary = NaturalSageLight,
    onSecondary = NaturalTextPrimary,
    secondaryContainer = DarkSurfaceVariant,
    onSecondaryContainer = NaturalSproutAccent,
    tertiary = NaturalSproutAccent,
    onTertiary = NaturalOnSprout,
    background = DarkBg,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkOutline
  )

private val LightColorScheme =
  lightColorScheme(
    primary = NaturalPrimary,
    onPrimary = Color.White,
    primaryContainer = NaturalSageLight,
    onPrimaryContainer = NaturalPrimaryDark,
    secondary = NaturalSproutAccent,
    onSecondary = NaturalOnSprout,
    secondaryContainer = NaturalSageBorder,
    onSecondaryContainer = NaturalTextPrimary,
    tertiary = NaturalPrimary,
    onTertiary = Color.White,
    background = NaturalBg,
    onBackground = NaturalTextPrimary,
    surface = NaturalSurface,
    onSurface = NaturalTextPrimary,
    surfaceVariant = NaturalSageLight,
    onSurfaceVariant = NaturalTextSecondary,
    outline = NaturalSageBorder
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Use our handcrafted eco colors for strong theme identity
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
