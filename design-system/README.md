# design-system

Reusable Compose design-system module for AndroidCore.

## What it provides
- Typed tokens for color, typography, spacing, shape, elevation, border, opacity, motion, and layout.
- `DSTheme(...)` composable that injects token families through composition locals.
- `DesignSystem` accessors for token lookup in composables.

## Usage
```kotlin
DSTheme {
    val colors = DesignSystem.colors
    val spacing = DesignSystem.spacing
}
```

