package com.url.androidcore.designsystem.tokens.border

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class DSBorder(
    val hairline: Dp,
    val thin: Dp,
    val medium: Dp,
    val thick: Dp,
) {
    fun merge(other: DSBorder): DSBorder = copy(
        hairline = other.hairline.orElse(hairline),
        thin = other.thin.orElse(thin),
        medium = other.medium.orElse(medium),
        thick = other.thick.orElse(thick),
    )

    companion object {
        @Suppress("unused") // Public library API
        fun default(): DSBorder = DSBorder(
            hairline = 0.5.dp,
            thin = 1.dp,
            medium = 2.dp,
            thick = 3.dp,
        )
    }
}

private fun Dp.orElse(fallback: Dp): Dp = if (this == Dp.Unspecified) fallback else this


