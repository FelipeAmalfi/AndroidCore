package com.url.androidcore.designsystem.tokens.motion

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.runtime.Immutable

@Immutable
data class DSMotionDurations(
    val short: Int,
    val medium: Int,
    val long: Int,
)

@Immutable
data class DSMotion(
    val durations: DSMotionDurations,
    val standardEasing: Easing,
    val emphasizedEasing: Easing,
    val decelerateEasing: Easing,
    val accelerateEasing: Easing,
) {
    fun merge(other: DSMotion): DSMotion = copy(
        durations = durations.merge(other.durations),
        standardEasing = other.standardEasing,
        emphasizedEasing = other.emphasizedEasing,
        decelerateEasing = other.decelerateEasing,
        accelerateEasing = other.accelerateEasing,
    )

    companion object {
        @Suppress("unused") // Public library API
        fun default(): DSMotion = DSMotion(
            durations = DSMotionDurations(short = 120, medium = 240, long = 360),
            standardEasing = FastOutSlowInEasing,
            emphasizedEasing = CubicBezierEasing(0.2f, 0f, 0f, 1f),
            decelerateEasing = LinearEasing,
            accelerateEasing = FastOutLinearInEasing,
        )
    }
}

private fun DSMotionDurations.merge(other: DSMotionDurations): DSMotionDurations = copy(
    short = other.short.orElse(short),
    medium = other.medium.orElse(medium),
    long = other.long.orElse(long),
)

private fun Int.orElse(fallback: Int): Int = if (this >= 0) this else fallback

