package com.url.androidcore.designsystem.tokens.shape

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class DSShapes(
    val none: Dp,
    val xs: Dp,
    val sm: Dp,
    val md: Dp,
    val lg: Dp,
    val pill: Dp,
) {
    fun merge(other: DSShapes): DSShapes = copy(
        none = other.none.orElse(none),
        xs = other.xs.orElse(xs),
        sm = other.sm.orElse(sm),
        md = other.md.orElse(md),
        lg = other.lg.orElse(lg),
        pill = other.pill.orElse(pill),
    )

    companion object {
        @Suppress("unused") // Public library API
        fun default(): DSShapes = DSShapes(
            none = 0.dp,
            xs = 4.dp,
            sm = 8.dp,
            md = 12.dp,
            lg = 16.dp,
            pill = 999.dp,
        )
    }
}

private fun Dp.orElse(fallback: Dp): Dp = if (this == Dp.Unspecified) fallback else this

