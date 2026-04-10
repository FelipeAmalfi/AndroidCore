# Shape Tokens

**Type:** `DSShapes`  
**Source:** `designsystem/tokens/shape/DSShapes.kt`  
**Accessed via:** `DesignSystem.shapes`

---

## Overview

`DSShapes` provides a corner-radius scale in `Dp`. Apply the values to `RoundedCornerShape` to keep rounding consistent across the component library.

---

## Default scale

| Token | Value | Typical usage |
|---|---|---|
| `none` | 0 dp | Sharp corners - images, full-bleed dividers |
| `xs` | 4 dp | Chips, tags, small badges |
| `sm` | 8 dp | Input fields, secondary buttons |
| `md` | 12 dp | Cards, dialogs |
| `lg` | 16 dp | Bottom sheets, large panels |
| `pill` | 999 dp | Fully rounded - FABs, toggle buttons, search bars |

---

## Usage examples

### Card surface

```kotlin
@Composable
fun DSCard(content: @Composable () -> Unit) {
    val shapes  = DesignSystem.shapes
    val colors  = DesignSystem.colors
    val spacing = DesignSystem.spacing

    Surface(
        shape = RoundedCornerShape(shapes.md),
        color = colors.surface,
        modifier = Modifier.padding(spacing.lg),
    ) {
        content()
    }
}
```

### Pill button

```kotlin
@Composable
fun PillButton(label: String, onClick: () -> Unit) {
    val shapes = DesignSystem.shapes

    Button(
        onClick = onClick,
        shape   = RoundedCornerShape(shapes.pill),
    ) {
        Text(label)
    }
}
```

---

## Customizing shapes

```kotlin
val squarierShapes = DefaultTokens.shapes.copy(
    md = 4.dp,
    lg = 8.dp,
)

DSTheme(shapes = squarierShapes) { /* ... */ }
```

---

## Related

- [Token System](./index.md)
- [Customization](../customization.md)


