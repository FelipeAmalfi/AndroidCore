# Composition Locals

**Source:** `design-system/src/main/java/com/url/androidcore/designsystem/theme/CompositionLocals.kt`

---

## Overview

The design system exposes one Composition Local per token family. `DSTheme` writes these values, and `DesignSystem` reads them.

Prefer `DesignSystem` accessors in feature code. Access `Local*` objects directly only inside the design-system module.

---

## Available locals

| Local | Type | Fallback default |
|---|---|---|
| `LocalDSColors` | `DSColors` | `DefaultTokens.lightColors()` |
| `LocalDSTypography` | `DSTypography` | `DefaultTokens.typography` |
| `LocalDSSpacing` | `DSSpacing` | `DefaultTokens.spacing` |
| `LocalDSShapes` | `DSShapes` | `DefaultTokens.shapes` |
| `LocalDSElevation` | `DSElevation` | `DefaultTokens.elevation` |
| `LocalDSBorder` | `DSBorder` | `DefaultTokens.border` |
| `LocalDSOpacity` | `DSOpacity` | `DefaultTokens.opacity` |
| `LocalDSMotion` | `DSMotion` | `DefaultTokens.motion` |
| `LocalDSLayout` | `DSLayout` | `DefaultTokens.layout` |

---

## Data flow

```text
DSTheme(...)
  -> CompositionLocalProvider(LocalDSColors provides colors, ...)
  -> child composables
  -> DesignSystem.colors / typography / ...
```

---

## Why this matters

- Scoped overrides are easy: nest `DSTheme` and replace only one token family.
- Composables stay decoupled from concrete theme instances.
- Fallback defaults prevent crashes when previews forget to wrap with a theme (but still wrap previews for correctness).

---

## Related

- [DSTheme API](./DSTheme.md)
- [DesignSystem Accessor](../accessor/DesignSystem.md)
- [Token System](../tokens/index.md)

