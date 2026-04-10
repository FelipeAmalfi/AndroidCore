package com.url.androidcore.designsystem.tokens.elevation

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class DSElevation(
    val level0: Dp,
    val level1: Dp,
    val level2: Dp,
    val level3: Dp,
    val level4: Dp,
    val level5: Dp,
) {
    fun merge(other: DSElevation): DSElevation = copy(
        level0 = other.level0.orElse(level0),
        level1 = other.level1.orElse(level1),
        level2 = other.level2.orElse(level2),
        level3 = other.level3.orElse(level3),
        level4 = other.level4.orElse(level4),
        level5 = other.level5.orElse(level5),
    )

    companion object {
        @Suppress("unused") // Public library API
        fun default(): DSElevation = DSElevation(
            level0 = 0.dp,
            level1 = 1.dp,
            level2 = 3.dp,
            level3 = 6.dp,
            level4 = 8.dp,
            level5 = 12.dp,
        )
    }
}

private fun Dp.orElse(fallback: Dp): Dp = if (this == Dp.Unspecified) fallback else this

