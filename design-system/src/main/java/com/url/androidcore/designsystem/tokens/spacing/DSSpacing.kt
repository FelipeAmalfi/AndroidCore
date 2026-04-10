package com.url.androidcore.designsystem.tokens.spacing

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class DSSpacing(
    val xxs: Dp,
    val xs: Dp,
    val sm: Dp,
    val md: Dp,
    val lg: Dp,
    val xl: Dp,
    val xxl: Dp,
) {
    fun merge(other: DSSpacing): DSSpacing = copy(
        xxs = other.xxs.orElse(xxs),
        xs = other.xs.orElse(xs),
        sm = other.sm.orElse(sm),
        md = other.md.orElse(md),
        lg = other.lg.orElse(lg),
        xl = other.xl.orElse(xl),
        xxl = other.xxl.orElse(xxl),
    )

    companion object {
        @Suppress("unused") // Public library API
        fun default(): DSSpacing = DSSpacing(
            xxs = 2.dp,
            xs = 4.dp,
            sm = 8.dp,
            md = 12.dp,
            lg = 16.dp,
            xl = 24.dp,
            xxl = 32.dp,
        )
    }
}

private fun Dp.orElse(fallback: Dp): Dp = if (this == Dp.Unspecified) fallback else this

