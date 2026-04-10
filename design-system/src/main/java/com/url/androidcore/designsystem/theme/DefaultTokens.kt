package com.url.androidcore.designsystem.theme

import com.url.androidcore.designsystem.tokens.border.DSBorder
import com.url.androidcore.designsystem.tokens.color.DSColors
import com.url.androidcore.designsystem.tokens.elevation.DSElevation
import com.url.androidcore.designsystem.tokens.layout.DSLayout
import com.url.androidcore.designsystem.tokens.motion.DSMotion
import com.url.androidcore.designsystem.tokens.opacity.DSOpacity
import com.url.androidcore.designsystem.tokens.shape.DSShapes
import com.url.androidcore.designsystem.tokens.spacing.DSSpacing
import com.url.androidcore.designsystem.tokens.typography.DSTypography

@Suppress("unused") // Public library API — consumed by host apps, not by this module
object DefaultTokens {
    val typography: DSTypography = DSTypography.default()
    val spacing: DSSpacing = DSSpacing.default()
    val shapes: DSShapes = DSShapes.default()
    val elevation: DSElevation = DSElevation.default()
    val border: DSBorder = DSBorder.default()
    val opacity: DSOpacity = DSOpacity.default()
    val motion: DSMotion = DSMotion.default()
    val layout: DSLayout = DSLayout.default()

    fun lightColors(): DSColors = DSColors.light()

    fun darkColors(): DSColors = DSColors.dark()
}


