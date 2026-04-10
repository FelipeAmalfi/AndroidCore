# Token System

Design tokens are the single source of truth for all visual decisions in the design system. They are typed Kotlin data classes, not raw constants, so the compiler enforces correct usage.

---

## Families

| Family | Covers | Reference |
|---|---|---|
| [Colors](./colors.md) | Palette, semantic, content, interaction colors | `DSColors` |
| [Typography](./typography.md) | Text scales - size, weight, line-height, letter spacing | `DSTypography` |
| [Spacing](./spacing.md) | Padding and gap scale | `DSSpacing` |
| [Shapes](./shapes.md) | Corner radius scale | `DSShapes` |
| [Elevation](./elevation.md) | Shadow levels | `DSElevation` |
| [Border](./border.md) | Stroke widths | `DSBorder` |
| [Opacity](./opacity.md) | Alpha levels | `DSOpacity` |
| [Motion](./motion.md) | Animation durations and easing curves | `DSMotion` |
| [Layout](./layout.md) | Grid, max width, and touch targets | `DSLayout` |

---

## How tokens reach composables

```
DSTheme(colors = ..., spacing = ..., ...)
    |
    |- CompositionLocalProvider(LocalDSColors provides colors, ...)
    |
    `- content composable
           |
           `- DesignSystem.colors   <- reads LocalDSColors.current
```

`DSTheme` injects every token family through a dedicated `CompositionLocal`. The `DesignSystem` singleton provides named read-only accessors. You never read `LocalDSColors.current` directly; use `DesignSystem.colors` instead.

---

## Token data class contract

Every token data class:

- is annotated `@Immutable` (safe for the Compose snapshot system)
- has a `companion object` factory with a `default()` method (or `light()` / `dark()` for colors)
- exposes a `merge(other)` method for selective overrides
- uses Kotlin `data class` semantics, so `copy(...)` is available for full replacement

---

## Accessing tokens

```kotlin
@Composable
fun ExampleComponent() {
    val colors    = DesignSystem.colors
    val spacing   = DesignSystem.spacing
    val typography = DesignSystem.typography
    val shapes    = DesignSystem.shapes
    val elevation = DesignSystem.elevation
    val border    = DesignSystem.border
    val opacity   = DesignSystem.opacity
    val motion    = DesignSystem.motion
    val layout    = DesignSystem.layout
}
```

All accessors are `@ReadOnlyComposable`, meaning they do not trigger recomposition on their own - they read a static Composition Local value.

---

## Adding a new token

1. Create a new `data class DS{Name}` annotated `@Immutable` in the appropriate `tokens/{name}/` package.
2. Add a `merge(other)` method and a `companion object` with `default()`.
3. Add a `CompositionLocal` in `CompositionLocals.kt`.
4. Add the parameter to `DSTheme`.
5. Add a read-only accessor property to `DesignSystem`.
6. Document the new token family in a new `docs/design-system/tokens/{name}.md`.

---

## Related

- [DSTheme API](../theme/DSTheme.md)
- [DesignSystem Accessor](../accessor/DesignSystem.md)
- [Customization](../customization.md)


