package com.url.androidcore.designsystem.tokens.layout

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class DSLayout(
    val contentMaxWidth: Dp,
    val screenHorizontalPadding: Dp,
    val sectionVerticalGap: Dp,
    val itemMinTouchSize: Dp,
    val gridColumns: Int,
) {
    fun merge(other: DSLayout): DSLayout = copy(
        contentMaxWidth = other.contentMaxWidth.orElse(contentMaxWidth),
        screenHorizontalPadding = other.screenHorizontalPadding.orElse(screenHorizontalPadding),
        sectionVerticalGap = other.sectionVerticalGap.orElse(sectionVerticalGap),
        itemMinTouchSize = other.itemMinTouchSize.orElse(itemMinTouchSize),
        gridColumns = other.gridColumns.orElse(gridColumns),
    )

    companion object {
        @Suppress("unused") // Public library API
        fun default(): DSLayout = DSLayout(
            contentMaxWidth = 720.dp,
            screenHorizontalPadding = 16.dp,
            sectionVerticalGap = 24.dp,
            itemMinTouchSize = 48.dp,
            gridColumns = 12,
        )
    }
}

private fun Dp.orElse(fallback: Dp): Dp = if (this == Dp.Unspecified) fallback else this
private fun Int.orElse(fallback: Int): Int = if (this > 0) this else fallback

