package com.url.androidcore.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.url.androidcore.designsystem.tokens.border.DSBorder
import com.url.androidcore.designsystem.tokens.color.DSColors
import com.url.androidcore.designsystem.tokens.elevation.DSElevation
import com.url.androidcore.designsystem.tokens.layout.DSLayout
import com.url.androidcore.designsystem.tokens.motion.DSMotion
import com.url.androidcore.designsystem.tokens.opacity.DSOpacity
import com.url.androidcore.designsystem.tokens.shape.DSShapes
import com.url.androidcore.designsystem.tokens.spacing.DSSpacing
import com.url.androidcore.designsystem.tokens.typography.DSTypography

@Composable
fun DSTheme(
    darkTheme: Boolean = false,
    colors: DSColors = if (darkTheme) DefaultTokens.darkColors() else DefaultTokens.lightColors(),
    typography: DSTypography = DefaultTokens.typography,
    spacing: DSSpacing = DefaultTokens.spacing,
    shapes: DSShapes = DefaultTokens.shapes,
    elevation: DSElevation = DefaultTokens.elevation,
    border: DSBorder = DefaultTokens.border,
    opacity: DSOpacity = DefaultTokens.opacity,
    motion: DSMotion = DefaultTokens.motion,
    layout: DSLayout = DefaultTokens.layout,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalDSColors provides colors,
        LocalDSTypography provides typography,
        LocalDSSpacing provides spacing,
        LocalDSShapes provides shapes,
        LocalDSElevation provides elevation,
        LocalDSBorder provides border,
        LocalDSOpacity provides opacity,
        LocalDSMotion provides motion,
        LocalDSLayout provides layout,
        content = content,
    )
}


