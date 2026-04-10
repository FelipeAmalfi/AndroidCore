package com.url.androidcore.designsystem.tokens.opacity

import androidx.compose.runtime.Immutable

@Immutable
data class DSOpacity(
    val disabled: Float,
    val subtle: Float,
    val medium: Float,
    val strong: Float,
) {
    fun merge(other: DSOpacity): DSOpacity = copy(
        disabled = other.disabled.orElse(disabled),
        subtle = other.subtle.orElse(subtle),
        medium = other.medium.orElse(medium),
        strong = other.strong.orElse(strong),
    )

    companion object {
        @Suppress("unused") // Public library API
        fun default(): DSOpacity = DSOpacity(
            disabled = 0.38f,
            subtle = 0.60f,
            medium = 0.74f,
            strong = 0.92f,
        )
    }
}

private fun Float.orElse(fallback: Float): Float = if (this in 0f..1f) this else fallback

