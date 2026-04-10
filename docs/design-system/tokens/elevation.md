# Elevation Tokens

**Type:** `DSElevation`  
**Source:** `design-system/src/main/java/com/url/androidcore/designsystem/tokens/elevation/DSElevation.kt`  
**Accessed via:** `DesignSystem.elevation`

---

## Overview

`DSElevation` defines six depth levels for surfaces. Use the same levels across cards, dialogs, and overlays to keep visual hierarchy predictable.

---

## Default scale

| Token | Value | Typical usage |
|---|---|---|
| `level0` | 0 dp | Flat surfaces, page background |
| `level1` | 1 dp | Resting cards |
| `level2` | 3 dp | Raised cards, pressed state |
| `level3` | 6 dp | Floating panels |
| `level4` | 8 dp | Menus, popups |
| `level5` | 12 dp | Modal surfaces with strongest emphasis |

---

## Usage example

```kotlin
@Composable
fun ElevatedCard(content: @Composable () -> Unit) {
    val elevation = DesignSystem.elevation
    val shapes = DesignSystem.shapes

    Surface(
        shadowElevation = elevation.level2,
        shape = RoundedCornerShape(shapes.md),
    ) {
        content()
    }
}
```

---

## Customizing elevation

```kotlin
val flatterElevation = DefaultTokens.elevation.copy(
    level2 = 2.dp,
    level3 = 4.dp,
)

DSTheme(elevation = flatterElevation) { /* ... */ }
```

Use `merge(other)` when building partial overrides with `Dp.Unspecified` fallback behavior.

---

## Related

- [Token System](./index.md)
- [Border Tokens](./border.md)
- [Customization](../customization.md)

