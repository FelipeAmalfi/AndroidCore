# Design System - Overview

The `design-system` module is a Compose-only Android library that ships typed design tokens and the `DSTheme` composable entry point.

Any feature that renders Compose UI should be wrapped inside `DSTheme` so it receives the full token set through Composition Locals.

---

## Module coordinates

| Item | Value |
|---|---|
| Gradle module | `:design-system` |
| Namespace | `com.url.androidcore.designsystem` |
| Compose enabled | yes |
| Min SDK | 24 |

---

## What the module provides

| Concern | Class / Object |
|---|---|
| Theme entry point | `DSTheme` composable |
| Token access inside composables | `DesignSystem` object |
| Token defaults | `DefaultTokens` object |
| Composition Locals | `LocalDSColors`, `LocalDSTypography`, ... |

---

## Token families

| Family | Type | Default factory |
|---|---|---|
| Color | `DSColors` | `DefaultTokens.lightColors()` / `DefaultTokens.darkColors()` |
| Typography | `DSTypography` | `DefaultTokens.typography` |
| Spacing | `DSSpacing` | `DefaultTokens.spacing` |
| Shapes | `DSShapes` | `DefaultTokens.shapes` |
| Elevation | `DSElevation` | `DefaultTokens.elevation` |
| Border | `DSBorder` | `DefaultTokens.border` |
| Opacity | `DSOpacity` | `DefaultTokens.opacity` |
| Motion | `DSMotion` | `DefaultTokens.motion` |
| Layout | `DSLayout` | `DefaultTokens.layout` |

---

## Documentation map

```
docs/design-system/
|- overview.md              -> you are here
|- getting-started.md       -> integration and first-use guide
|- customization.md         -> overriding tokens and building custom themes
|- tokens/
|  |- index.md              -> token system explained
|  |- colors.md
|  |- typography.md
|  |- spacing.md
|  |- shapes.md
|  |- elevation.md
|  |- border.md
|  |- opacity.md
|  |- motion.md
|  `- layout.md
|- theme/
|  |- DSTheme.md            -> composable API reference
|  |- composition-locals.md
|  |- dark-theme.md
|  `- default-tokens.md
`- accessor/
   `- DesignSystem.md       -> runtime token accessor
```

---

## Related

- [Getting Started](./getting-started.md)
- [Token System](./tokens/index.md)
- [Color Tokens](./tokens/colors.md)
- [Typography Tokens](./tokens/typography.md)
- [Spacing Tokens](./tokens/spacing.md)
- [Shape Tokens](./tokens/shapes.md)
- [Elevation Tokens](./tokens/elevation.md)
- [Border Tokens](./tokens/border.md)
- [Opacity Tokens](./tokens/opacity.md)
- [Motion Tokens](./tokens/motion.md)
- [Layout Tokens](./tokens/layout.md)
- [DSTheme API](./theme/DSTheme.md)
- [Composition Locals](./theme/composition-locals.md)
- [Dark Theme](./theme/dark-theme.md)
- [Default Tokens](./theme/default-tokens.md)
- [DesignSystem Accessor](./accessor/DesignSystem.md)
- [Customization](./customization.md)
