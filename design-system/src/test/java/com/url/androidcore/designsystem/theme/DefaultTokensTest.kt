package com.url.androidcore.designsystem.theme

import androidx.compose.ui.graphics.Color
import com.url.androidcore.designsystem.tokens.color.DSColors
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class DefaultTokensTest {

    @Test
    fun lightAndDarkColorProvidersExposeDifferentBackgrounds() {
        val light = DefaultTokens.lightColors()
        val dark = DefaultTokens.darkColors()

        assertNotEquals(light.background, dark.background)
    }

    @Test
    fun colorMergeUsesFallbackForUnspecifiedValues() {
        val base = DSColors.light()
        val overrides = base.copy(
            primary = Color(0xFF123456),
            onPrimary = Color.Unspecified,
        )

        val merged = base.merge(overrides)

        assertEquals(Color(0xFF123456), merged.primary)
        assertEquals(base.onPrimary, merged.onPrimary)
    }
}

