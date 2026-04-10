# DesignSystem Accessor

**Type:** `object DesignSystem`  
**Source:** `design-system/src/main/java/com/url/androidcore/designsystem/core/DSThemeAccessor.kt`

---

## Overview

`DesignSystem` is the public runtime accessor for reading active tokens in composables. Each property maps to one Composition Local and is marked `@ReadOnlyComposable`.

---

## Available accessors

| Property | Type | Reads from |
|---|---|---|
| `colors` | `DSColors` | `LocalDSColors.current` |
| `typography` | `DSTypography` | `LocalDSTypography.current` |
| `spacing` | `DSSpacing` | `LocalDSSpacing.current` |
| `shapes` | `DSShapes` | `LocalDSShapes.current` |
| `elevation` | `DSElevation` | `LocalDSElevation.current` |
| `border` | `DSBorder` | `LocalDSBorder.current` |
| `opacity` | `DSOpacity` | `LocalDSOpacity.current` |
| `motion` | `DSMotion` | `LocalDSMotion.current` |
| `layout` | `DSLayout` | `LocalDSLayout.current` |

---

## Usage example

```kotlin
@Composable
fun DSButton(label: String, onClick: () -> Unit) {
    val colors = DesignSystem.colors
    val spacing = DesignSystem.spacing
    val shapes = DesignSystem.shapes

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(shapes.pill),
        colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
        modifier = Modifier.padding(horizontal = spacing.lg),
    ) {
        Text(text = label, color = colors.onPrimary)
    }
}
```

---

## Best practices

- Read `DesignSystem` values inside composables, not in `ViewModel` or domain code.
- Keep token reads near the UI usage site for clarity.
- Wrap previews and screens in `DSTheme` to avoid accidental fallback styling.

---

## Related

- [DSTheme API](../theme/DSTheme.md)
- [Composition Locals](../theme/composition-locals.md)
- [Token System](../tokens/index.md)

