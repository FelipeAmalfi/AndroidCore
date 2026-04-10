# Border Tokens

**Type:** `DSBorder`  
**Source:** `design-system/src/main/java/com/url/androidcore/designsystem/tokens/border/DSBorder.kt`  
**Accessed via:** `DesignSystem.border`

---

## Overview

`DSBorder` provides stroke widths in `Dp`. Reusing these values avoids random one-off border thicknesses across inputs, dividers, and outlines.

---

## Default scale

| Token | Value | Typical usage |
|---|---|---|
| `hairline` | 0.5 dp | Subtle separators |
| `thin` | 1 dp | Standard input borders, dividers |
| `medium` | 2 dp | Focused controls, emphasized borders |
| `thick` | 3 dp | Strong container outlines |

---

## Usage example

```kotlin
@Composable
fun OutlinedTile(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val border = DesignSystem.border
    val colors = DesignSystem.colors
    val shapes = DesignSystem.shapes

    Box(
        modifier = modifier.border(
            width = border.thin,
            color = colors.outline,
            shape = RoundedCornerShape(shapes.sm),
        )
    ) {
        content()
    }
}
```

---

## Customizing borders

```kotlin
val highContrastBorder = DefaultTokens.border.copy(
    thin = 1.5.dp,
    medium = 2.5.dp,
)

DSTheme(border = highContrastBorder) { /* ... */ }
```

`merge(other)` uses `Dp.Unspecified` as the sentinel for keeping existing values.

---

## Related

- [Token System](./index.md)
- [Color Tokens](./colors.md)
- [Customization](../customization.md)

