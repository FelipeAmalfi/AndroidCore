package com.url.androidcore.designsystem.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.url.androidcore.designsystem.theme.LocalDSBorder
import com.url.androidcore.designsystem.theme.LocalDSColors
import com.url.androidcore.designsystem.theme.LocalDSElevation
import com.url.androidcore.designsystem.theme.LocalDSLayout
import com.url.androidcore.designsystem.theme.LocalDSMotion
import com.url.androidcore.designsystem.theme.LocalDSOpacity
import com.url.androidcore.designsystem.theme.LocalDSShapes
import com.url.androidcore.designsystem.theme.LocalDSSpacing
import com.url.androidcore.designsystem.theme.LocalDSTypography
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
object DesignSystem {
    val colors: DSColors
        @Composable
        @ReadOnlyComposable
        get() = LocalDSColors.current

    val typography: DSTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalDSTypography.current

    val spacing: DSSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalDSSpacing.current

    val shapes: DSShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalDSShapes.current

    val elevation: DSElevation
        @Composable
        @ReadOnlyComposable
        get() = LocalDSElevation.current

    val border: DSBorder
        @Composable
        @ReadOnlyComposable
        get() = LocalDSBorder.current

    val opacity: DSOpacity
        @Composable
        @ReadOnlyComposable
        get() = LocalDSOpacity.current

    val motion: DSMotion
        @Composable
        @ReadOnlyComposable
        get() = LocalDSMotion.current

    val layout: DSLayout
        @Composable
        @ReadOnlyComposable
        get() = LocalDSLayout.current
}


