package com.url.androidcore.designsystem.tokens.color

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class DSBaseColors(
    val blue500: Color,
    val blue700: Color,
    val gray50: Color,
    val gray200: Color,
    val gray900: Color,
    val green500: Color,
    val amber500: Color,
    val red500: Color,
    val cyan500: Color,
) {
    fun merge(other: DSBaseColors): DSBaseColors = copy(
        blue500 = other.blue500.orElse(blue500),
        blue700 = other.blue700.orElse(blue700),
        gray50 = other.gray50.orElse(gray50),
        gray200 = other.gray200.orElse(gray200),
        gray900 = other.gray900.orElse(gray900),
        green500 = other.green500.orElse(green500),
        amber500 = other.amber500.orElse(amber500),
        red500 = other.red500.orElse(red500),
        cyan500 = other.cyan500.orElse(cyan500),
    )
}

@Immutable
data class DSContentColors(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val inverse: Color,
    val disabled: Color,
) {
    fun merge(other: DSContentColors): DSContentColors = copy(
        primary = other.primary.orElse(primary),
        secondary = other.secondary.orElse(secondary),
        tertiary = other.tertiary.orElse(tertiary),
        inverse = other.inverse.orElse(inverse),
        disabled = other.disabled.orElse(disabled),
    )
}

@Immutable
data class DSSemanticColors(
    val success: Color,
    val error: Color,
    val warning: Color,
    val info: Color,
) {
    fun merge(other: DSSemanticColors): DSSemanticColors = copy(
        success = other.success.orElse(success),
        error = other.error.orElse(error),
        warning = other.warning.orElse(warning),
        info = other.info.orElse(info),
    )
}

@Immutable
data class DSInteractionColors(
    val pressed: Color,
    val focused: Color,
    val hovered: Color,
    val disabledOverlay: Color,
) {
    fun merge(other: DSInteractionColors): DSInteractionColors = copy(
        pressed = other.pressed.orElse(pressed),
        focused = other.focused.orElse(focused),
        hovered = other.hovered.orElse(hovered),
        disabledOverlay = other.disabledOverlay.orElse(disabledOverlay),
    )
}

@Immutable
data class DSColors(
    val base: DSBaseColors,
    val content: DSContentColors,
    val semantic: DSSemanticColors,
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val onBackground: Color,
    val onSurface: Color,
    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val onSecondary: Color,
    val error: Color,
    val onError: Color,
    val success: Color,
    val warning: Color,
    val info: Color,
    val outline: Color,
    val border: Color,
    val scrim: Color,
    val interactions: DSInteractionColors,
) {
    fun merge(other: DSColors): DSColors = copy(
        base = base.merge(other.base),
        content = content.merge(other.content),
        semantic = semantic.merge(other.semantic),
        background = other.background.orElse(background),
        surface = other.surface.orElse(surface),
        surfaceVariant = other.surfaceVariant.orElse(surfaceVariant),
        onBackground = other.onBackground.orElse(onBackground),
        onSurface = other.onSurface.orElse(onSurface),
        primary = other.primary.orElse(primary),
        onPrimary = other.onPrimary.orElse(onPrimary),
        secondary = other.secondary.orElse(secondary),
        onSecondary = other.onSecondary.orElse(onSecondary),
        error = other.error.orElse(error),
        onError = other.onError.orElse(onError),
        success = other.success.orElse(success),
        warning = other.warning.orElse(warning),
        info = other.info.orElse(info),
        outline = other.outline.orElse(outline),
        border = other.border.orElse(border),
        scrim = other.scrim.orElse(scrim),
        interactions = interactions.merge(other.interactions),
    )

    companion object {
        @Suppress("unused") // Public library API
        fun light(): DSColors = DSColors(
            base = DSBaseColors(
                blue500 = DSColorTokens.Light.Blue500,
                blue700 = DSColorTokens.Light.Blue700,
                gray50 = DSColorTokens.Light.Gray50,
                gray200 = DSColorTokens.Light.Gray200,
                gray900 = DSColorTokens.Light.Gray900,
                green500 = DSColorTokens.Light.Green500,
                amber500 = DSColorTokens.Light.Amber500,
                red500 = DSColorTokens.Light.Red500,
                cyan500 = DSColorTokens.Light.Cyan500,
            ),
            content = DSContentColors(
                primary = DSColorTokens.Light.ContentPrimary,
                secondary = DSColorTokens.Light.ContentSecondary,
                tertiary = DSColorTokens.Light.ContentTertiary,
                inverse = DSColorTokens.Light.ContentInverse,
                disabled = DSColorTokens.Light.ContentDisabled,
            ),
            semantic = DSSemanticColors(
                success = DSColorTokens.Light.SemanticSuccess,
                error = DSColorTokens.Light.SemanticError,
                warning = DSColorTokens.Light.SemanticWarning,
                info = DSColorTokens.Light.SemanticInfo,
            ),
            background = DSColorTokens.Light.Background,
            surface = DSColorTokens.Light.Surface,
            surfaceVariant = DSColorTokens.Light.SurfaceVariant,
            onBackground = DSColorTokens.Light.OnBackground,
            onSurface = DSColorTokens.Light.OnSurface,
            primary = DSColorTokens.Light.Primary,
            onPrimary = DSColorTokens.Light.OnPrimary,
            secondary = DSColorTokens.Light.Secondary,
            onSecondary = DSColorTokens.Light.OnSecondary,
            error = DSColorTokens.Light.Error,
            onError = DSColorTokens.Light.OnError,
            success = DSColorTokens.Light.Success,
            warning = DSColorTokens.Light.Warning,
            info = DSColorTokens.Light.Info,
            outline = DSColorTokens.Light.Outline,
            border = DSColorTokens.Light.Border,
            scrim = DSColorTokens.Light.Scrim,
            interactions = DSInteractionColors(
                pressed = DSColorTokens.Light.InteractionPressed,
                focused = DSColorTokens.Light.InteractionFocused,
                hovered = DSColorTokens.Light.InteractionHovered,
                disabledOverlay = DSColorTokens.Light.InteractionDisabledOverlay,
            ),
        )

        fun dark(): DSColors = DSColors(
            base = DSBaseColors(
                blue500 = DSColorTokens.Dark.Blue500,
                blue700 = DSColorTokens.Dark.Blue700,
                gray50 = DSColorTokens.Dark.Gray50,
                gray200 = DSColorTokens.Dark.Gray200,
                gray900 = DSColorTokens.Dark.Gray900,
                green500 = DSColorTokens.Dark.Green500,
                amber500 = DSColorTokens.Dark.Amber500,
                red500 = DSColorTokens.Dark.Red500,
                cyan500 = DSColorTokens.Dark.Cyan500,
            ),
            content = DSContentColors(
                primary = DSColorTokens.Dark.ContentPrimary,
                secondary = DSColorTokens.Dark.ContentSecondary,
                tertiary = DSColorTokens.Dark.ContentTertiary,
                inverse = DSColorTokens.Dark.ContentInverse,
                disabled = DSColorTokens.Dark.ContentDisabled,
            ),
            semantic = DSSemanticColors(
                success = DSColorTokens.Dark.SemanticSuccess,
                error = DSColorTokens.Dark.SemanticError,
                warning = DSColorTokens.Dark.SemanticWarning,
                info = DSColorTokens.Dark.SemanticInfo,
            ),
            background = DSColorTokens.Dark.Background,
            surface = DSColorTokens.Dark.Surface,
            surfaceVariant = DSColorTokens.Dark.SurfaceVariant,
            onBackground = DSColorTokens.Dark.OnBackground,
            onSurface = DSColorTokens.Dark.OnSurface,
            primary = DSColorTokens.Dark.Primary,
            onPrimary = DSColorTokens.Dark.OnPrimary,
            secondary = DSColorTokens.Dark.Secondary,
            onSecondary = DSColorTokens.Dark.OnSecondary,
            error = DSColorTokens.Dark.Error,
            onError = DSColorTokens.Dark.OnError,
            success = DSColorTokens.Dark.Success,
            warning = DSColorTokens.Dark.Warning,
            info = DSColorTokens.Dark.Info,
            outline = DSColorTokens.Dark.Outline,
            border = DSColorTokens.Dark.Border,
            scrim = DSColorTokens.Dark.Scrim,
            interactions = DSInteractionColors(
                pressed = DSColorTokens.Dark.InteractionPressed,
                focused = DSColorTokens.Dark.InteractionFocused,
                hovered = DSColorTokens.Dark.InteractionHovered,
                disabledOverlay = DSColorTokens.Dark.InteractionDisabledOverlay,
            ),
        )
    }
}

private object DSColorTokens {
    object Light {
        val Blue500: Color = colorFromHex(0xFF0057B8L)
        val Blue700: Color = colorFromHex(0xFF003E85L)
        val Gray50: Color = colorFromHex(0xFFFFFBFEL)
        val Gray200: Color = colorFromHex(0xFFCAC4D0L)
        val Gray900: Color = colorFromHex(0xFF1C1B1FL)
        val Green500: Color = colorFromHex(0xFF1B873FL)
        val Amber500: Color = colorFromHex(0xFF9E6200L)
        val Red500: Color = colorFromHex(0xFFBA1A1AL)
        val Cyan500: Color = colorFromHex(0xFF00658AL)

        val ContentPrimary: Color = colorFromHex(0xFF1C1B1FL)
        val ContentSecondary: Color = colorFromHex(0xFF4A4450L)
        val ContentTertiary: Color = colorFromHex(0xFF6A6470L)
        val ContentInverse: Color = colorFromHex(0xFFFFFFFFL)
        val ContentDisabled: Color = colorFromHex(0x611C1B1FL)

        val SemanticSuccess: Color = colorFromHex(0xFF1B873FL)
        val SemanticError: Color = colorFromHex(0xFFBA1A1AL)
        val SemanticWarning: Color = colorFromHex(0xFF9E6200L)
        val SemanticInfo: Color = colorFromHex(0xFF00658AL)

        val Background: Color = colorFromHex(0xFFFFFBFEL)
        val Surface: Color = colorFromHex(0xFFFFFFFFL)
        val SurfaceVariant: Color = colorFromHex(0xFFF4EFF4L)
        val OnBackground: Color = colorFromHex(0xFF1C1B1FL)
        val OnSurface: Color = colorFromHex(0xFF1C1B1FL)
        val Primary: Color = colorFromHex(0xFF0057B8L)
        val OnPrimary: Color = colorFromHex(0xFFFFFFFFL)
        val Secondary: Color = colorFromHex(0xFF4A5968L)
        val OnSecondary: Color = colorFromHex(0xFFFFFFFFL)
        val Error: Color = colorFromHex(0xFFBA1A1AL)
        val OnError: Color = colorFromHex(0xFFFFFFFFL)
        val Success: Color = colorFromHex(0xFF1B873FL)
        val Warning: Color = colorFromHex(0xFF9E6200L)
        val Info: Color = colorFromHex(0xFF00658AL)
        val Outline: Color = colorFromHex(0xFF7A757FL)
        val Border: Color = colorFromHex(0xFFCAC4D0L)
        val Scrim: Color = colorFromHex(0x66000000L)

        val InteractionPressed: Color = colorFromHex(0x1F1C1B1FL)
        val InteractionFocused: Color = colorFromHex(0x291C1B1FL)
        val InteractionHovered: Color = colorFromHex(0x141C1B1FL)
        val InteractionDisabledOverlay: Color = colorFromHex(0x611C1B1FL)
    }

    object Dark {
        val Blue500: Color = colorFromHex(0xFF8CB4FFL)
        val Blue700: Color = colorFromHex(0xFF2F5FAEL)
        val Gray50: Color = colorFromHex(0xFFE2E2E9L)
        val Gray200: Color = colorFromHex(0xFF8E99A8L)
        val Gray900: Color = colorFromHex(0xFF10131AL)
        val Green500: Color = colorFromHex(0xFF8EDAA5L)
        val Amber500: Color = colorFromHex(0xFFFFC870L)
        val Red500: Color = colorFromHex(0xFFFFB4ABL)
        val Cyan500: Color = colorFromHex(0xFF84D2FFL)

        val ContentPrimary: Color = colorFromHex(0xFFE2E2E9L)
        val ContentSecondary: Color = colorFromHex(0xFFC5C8D0L)
        val ContentTertiary: Color = colorFromHex(0xFF9EA6B2L)
        val ContentInverse: Color = colorFromHex(0xFF10131AL)
        val ContentDisabled: Color = colorFromHex(0x52E2E2E9L)

        val SemanticSuccess: Color = colorFromHex(0xFF8EDAA5L)
        val SemanticError: Color = colorFromHex(0xFFFFB4ABL)
        val SemanticWarning: Color = colorFromHex(0xFFFFC870L)
        val SemanticInfo: Color = colorFromHex(0xFF84D2FFL)

        val Background: Color = colorFromHex(0xFF10131AL)
        val Surface: Color = colorFromHex(0xFF161B22L)
        val SurfaceVariant: Color = colorFromHex(0xFF222A35L)
        val OnBackground: Color = colorFromHex(0xFFE2E2E9L)
        val OnSurface: Color = colorFromHex(0xFFE2E2E9L)
        val Primary: Color = colorFromHex(0xFF8CB4FFL)
        val OnPrimary: Color = colorFromHex(0xFF002B61L)
        val Secondary: Color = colorFromHex(0xFFB7C9DDL)
        val OnSecondary: Color = colorFromHex(0xFF1D3144L)
        val Error: Color = colorFromHex(0xFFFFB4ABL)
        val OnError: Color = colorFromHex(0xFF690005L)
        val Success: Color = colorFromHex(0xFF8EDAA5L)
        val Warning: Color = colorFromHex(0xFFFFC870L)
        val Info: Color = colorFromHex(0xFF84D2FFL)
        val Outline: Color = colorFromHex(0xFF8E99A8L)
        val Border: Color = colorFromHex(0xFF414C5AL)
        val Scrim: Color = colorFromHex(0x99000000L)

        val InteractionPressed: Color = colorFromHex(0x29E2E2E9L)
        val InteractionFocused: Color = colorFromHex(0x33E2E2E9L)
        val InteractionHovered: Color = colorFromHex(0x1FE2E2E9L)
        val InteractionDisabledOverlay: Color = colorFromHex(0x52E2E2E9L)
    }
}

private fun colorFromHex(hex: Long): Color = Color(hex.toULong())

private fun Color.orElse(fallback: Color): Color = if (this == Color.Unspecified) fallback else this


