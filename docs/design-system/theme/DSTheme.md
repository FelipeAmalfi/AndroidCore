# DSTheme API

**Type:** `@Composable fun DSTheme(...)`  
**Source:** `design-system/src/main/java/com/url/androidcore/designsystem/theme/DSTheme.kt`

---

## Overview

`DSTheme` is the design-system entry point. It places every token family into `CompositionLocalProvider`, then renders `content`.

If you skip `DSTheme`, `DesignSystem` accessors still return fallback values from `CompositionLocals`, but app-level theming consistency is lost.

---

## Signature

```kotlin
@Composable
fun DSTheme(
    darkTheme: Boolean = false,
    colors: DSColors = if (darkTheme) DefaultTokens.darkColors() else DefaultTokens.lightColors(),
    typography: DSTypography = DefaultTokens.typography,
    spacing: DSSpacing = DefaultTokens.spacing,
    shapes: DSShapes = DefaultTokens.shapes,
    elevation: DSElevation = DefaultTokens.elevation,
    border: DSBorder = DefaultTokens.border,
    opacity: DSOpacity = DefaultTokens.opacity,
    motion: DSMotion = DefaultTokens.motion,
    layout: DSLayout = DefaultTokens.layout,
    content: @Composable () -> Unit,
)
```

---

## Parameters

| Parameter | Type | Default |
|---|---|---|
| `darkTheme` | `Boolean` | `false` |
| `colors` | `DSColors` | `DefaultTokens.darkColors()` or `DefaultTokens.lightColors()` |
| `typography` | `DSTypography` | `DefaultTokens.typography` |
| `spacing` | `DSSpacing` | `DefaultTokens.spacing` |
| `shapes` | `DSShapes` | `DefaultTokens.shapes` |
| `elevation` | `DSElevation` | `DefaultTokens.elevation` |
| `border` | `DSBorder` | `DefaultTokens.border` |
| `opacity` | `DSOpacity` | `DefaultTokens.opacity` |
| `motion` | `DSMotion` | `DefaultTokens.motion` |
| `layout` | `DSLayout` | `DefaultTokens.layout` |
| `content` | `@Composable () -> Unit` | required |

> API values above are aligned with `docs/.generated/components.json`.

---

## Typical usage

```kotlin
setContent {
    DSTheme(darkTheme = isSystemInDarkTheme()) {
        AppRoot()
    }
}
```

### Local override for one screen section

```kotlin
DSTheme {
    FeedScreen()

    DSTheme(colors = promoColors) {
        PromoBanner()
    }
}
```

---

## When to override which token

- Use `colors` for brand themes or contrast adjustments.
- Use `typography` and `spacing` for density/readability profiles.
- Use `layout` for large-screen adaptations.
- Keep overrides scoped (screen or section) unless you are defining a global app theme wrapper.

---

## Related

- [Composition Locals](./composition-locals.md)
- [Default Tokens](./default-tokens.md)
- [Dark Theme](./dark-theme.md)
- [Customization](../customization.md)

