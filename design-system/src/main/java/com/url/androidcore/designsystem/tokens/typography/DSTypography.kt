package com.url.androidcore.designsystem.tokens.typography

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Immutable
data class DSTextStyle(
    val fontSize: TextUnit,
    val lineHeight: TextUnit,
    val fontWeight: FontWeight,
    val letterSpacing: TextUnit,
) {
    fun merge(other: DSTextStyle): DSTextStyle = copy(
        fontSize = other.fontSize.orElse(fontSize),
        lineHeight = other.lineHeight.orElse(lineHeight),
        fontWeight = other.fontWeight,
        letterSpacing = other.letterSpacing.orElse(letterSpacing),
    )
}

@Immutable
data class DSTypography(
    val displayLarge: DSTextStyle,
    val headlineMedium: DSTextStyle,
    val titleLarge: DSTextStyle,
    val titleMedium: DSTextStyle,
    val bodyLarge: DSTextStyle,
    val bodyMedium: DSTextStyle,
    val labelLarge: DSTextStyle,
    val labelSmall: DSTextStyle,
) {
    fun merge(other: DSTypography): DSTypography = copy(
        displayLarge = displayLarge.merge(other.displayLarge),
        headlineMedium = headlineMedium.merge(other.headlineMedium),
        titleLarge = titleLarge.merge(other.titleLarge),
        titleMedium = titleMedium.merge(other.titleMedium),
        bodyLarge = bodyLarge.merge(other.bodyLarge),
        bodyMedium = bodyMedium.merge(other.bodyMedium),
        labelLarge = labelLarge.merge(other.labelLarge),
        labelSmall = labelSmall.merge(other.labelSmall),
    )

    companion object {
        @Suppress("unused") // Public library API
        fun default(): DSTypography = DSTypography(
            displayLarge = DSTextStyle(40.sp, 48.sp, FontWeight.Bold, 0.sp),
            headlineMedium = DSTextStyle(28.sp, 34.sp, FontWeight.SemiBold, 0.sp),
            titleLarge = DSTextStyle(22.sp, 28.sp, FontWeight.SemiBold, 0.sp),
            titleMedium = DSTextStyle(18.sp, 24.sp, FontWeight.Medium, 0.15.sp),
            bodyLarge = DSTextStyle(16.sp, 24.sp, FontWeight.Normal, 0.3.sp),
            bodyMedium = DSTextStyle(14.sp, 20.sp, FontWeight.Normal, 0.2.sp),
            labelLarge = DSTextStyle(14.sp, 20.sp, FontWeight.Medium, 0.1.sp),
            labelSmall = DSTextStyle(12.sp, 16.sp, FontWeight.Medium, 0.5.sp),
        )
    }
}

private fun TextUnit.orElse(fallback: TextUnit): TextUnit =
    if (this == TextUnit.Unspecified) fallback else this


