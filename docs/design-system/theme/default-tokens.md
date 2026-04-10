# Default Tokens

**Source:** `design-system/src/main/java/com/url/androidcore/designsystem/theme/DefaultTokens.kt`

---

## Overview

`DefaultTokens` is a singleton that provides the canonical baseline for every token family used by `DSTheme` and Composition Local fallbacks.

---

## API

| Member | Return type | Backed by |
|---|---|---|
| `typography` | `DSTypography` | `DSTypography.default()` |
| `spacing` | `DSSpacing` | `DSSpacing.default()` |
| `shapes` | `DSShapes` | `DSShapes.default()` |
| `elevation` | `DSElevation` | `DSElevation.default()` |
| `border` | `DSBorder` | `DSBorder.default()` |
| `opacity` | `DSOpacity` | `DSOpacity.default()` |
| `motion` | `DSMotion` | `DSMotion.default()` |
| `layout` | `DSLayout` | `DSLayout.default()` |
| `lightColors()` | `DSColors` | `DSColors.light()` |
| `darkColors()` | `DSColors` | `DSColors.dark()` |

---

## Usage patterns

### Build a small override from defaults

```kotlin
val compactTokens = DefaultTokens.spacing.copy(
    md = 8.dp,
    lg = 12.dp,
)

DSTheme(spacing = compactTokens) { /* ... */ }
```

### Use defaults as the source for custom wrappers

```kotlin
@Composable
fun AppTheme(darkTheme: Boolean, content: @Composable () -> Unit) {
    DSTheme(
        darkTheme = darkTheme,
        colors = if (darkTheme) DefaultTokens.darkColors() else DefaultTokens.lightColors(),
        typography = DefaultTokens.typography,
        content = content,
    )
}
```

---

## Related

- [DSTheme API](./DSTheme.md)
- [Token System](../tokens/index.md)
- [Customization](../customization.md)

