package com.url.androidcore.designsystem.theme

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import com.url.androidcore.designsystem.tokens.border.DSBorder
import com.url.androidcore.designsystem.tokens.color.DSColors
import com.url.androidcore.designsystem.tokens.elevation.DSElevation
import com.url.androidcore.designsystem.tokens.layout.DSLayout
import com.url.androidcore.designsystem.tokens.motion.DSMotion
import com.url.androidcore.designsystem.tokens.opacity.DSOpacity
import com.url.androidcore.designsystem.tokens.shape.DSShapes
import com.url.androidcore.designsystem.tokens.spacing.DSSpacing
import com.url.androidcore.designsystem.tokens.typography.DSTypography

val LocalDSColors: ProvidableCompositionLocal<DSColors> =
    staticCompositionLocalOf { DefaultTokens.lightColors() }

val LocalDSTypography: ProvidableCompositionLocal<DSTypography> =
    staticCompositionLocalOf { DefaultTokens.typography }

val LocalDSSpacing: ProvidableCompositionLocal<DSSpacing> =
    staticCompositionLocalOf { DefaultTokens.spacing }

val LocalDSShapes: ProvidableCompositionLocal<DSShapes> =
    staticCompositionLocalOf { DefaultTokens.shapes }

val LocalDSElevation: ProvidableCompositionLocal<DSElevation> =
    staticCompositionLocalOf { DefaultTokens.elevation }

val LocalDSBorder: ProvidableCompositionLocal<DSBorder> =
    staticCompositionLocalOf { DefaultTokens.border }

val LocalDSOpacity: ProvidableCompositionLocal<DSOpacity> =
    staticCompositionLocalOf { DefaultTokens.opacity }

val LocalDSMotion: ProvidableCompositionLocal<DSMotion> =
    staticCompositionLocalOf { DefaultTokens.motion }

val LocalDSLayout: ProvidableCompositionLocal<DSLayout> =
    staticCompositionLocalOf { DefaultTokens.layout }
