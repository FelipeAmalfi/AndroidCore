# Color Tokens

**Type:** `DSColors`  
**Source:** `designsystem/tokens/color/DSColors.kt`  
**Accessed via:** `DesignSystem.colors`

---

## Overview

`DSColors` groups colors into four sub-categories:

| Sub-type | Purpose |
|---|---|
| `DSBaseColors` (`base`) | Raw palette primitives (blue, gray, green, amber, red, cyan) |
| `DSContentColors` (`content`) | Text and icon tints (primary, secondary, tertiary, inverse, disabled) |
| `DSSemanticColors` (`semantic`) | Status meanings (success, error, warning, info) |
| `DSInteractionColors` (`interactions`) | Touch-state overlays (pressed, focused, hovered, disabled overlay) |

Top-level fields cover backgrounds, surfaces, primary/secondary brand colors, and decorative elements.

---

## Light Palette

### Base colors

| Token | Hex |
|---|---|
| `base.blue500` | `#0057B8` |
| `base.blue700` | `#003E85` |
| `base.gray50` | `#FFFBFE` |
| `base.gray200` | `#CAC4D0` |
| `base.gray900` | `#1C1B1F` |
| `base.green500` | `#1B873F` |
| `base.amber500` | `#9E6200` |
| `base.red500` | `#BA1A1A` |
| `base.cyan500` | `#00658A` |

### Content colors

| Token | Hex | Usage |
|---|---|---|
| `content.primary` | `#1C1B1F` | Body text, icons |
| `content.secondary` | `#4A4450` | Supporting text |
| `content.tertiary` | `#6A6470` | Placeholder, captions |
| `content.inverse` | `#FFFFFF` | Text on dark surfaces |
| `content.disabled` | `#1C1B1F` @ 38 % | Disabled elements |

### Semantic colors

| Token | Hex | Usage |
|---|---|---|
| `semantic.success` | `#1B873F` | Positive feedback |
| `semantic.error` | `#BA1A1A` | Errors |
| `semantic.warning` | `#9E6200` | Warnings |
| `semantic.info` | `#00658A` | Informational |

### Surfaces and brand

| Token | Hex | Usage |
|---|---|---|
| `background` | `#FFFBFE` | Screen background |
| `surface` | `#FFFFFF` | Cards, sheets |
| `surfaceVariant` | `#F4EFF4` | Alternate surface |
| `onBackground` | `#1C1B1F` | Text on background |
| `onSurface` | `#1C1B1F` | Text on surface |
| `primary` | `#0057B8` | Primary brand color |
| `onPrimary` | `#FFFFFF` | Content on primary |
| `secondary` | `#4A5968` | Secondary brand color |
| `onSecondary` | `#FFFFFF` | Content on secondary |
| `error` | `#BA1A1A` | Error color |
| `onError` | `#FFFFFF` | Content on error |
| `success` | `#1B873F` | Success color |
| `warning` | `#9E6200` | Warning color |
| `info` | `#00658A` | Info color |
| `outline` | `#7A757F` | Borders, dividers |
| `border` | `#CAC4D0` | Input borders |
| `scrim` | `#000000` @ 40 % | Modal overlay |

### Interaction overlays (light)

| Token | Hex | Usage |
|---|---|---|
| `interactions.pressed` | `#1C1B1F` @ 12 % | Pressed ripple |
| `interactions.focused` | `#1C1B1F` @ 16 % | Focus ring |
| `interactions.hovered` | `#1C1B1F` @ 8 % | Hover highlight |
| `interactions.disabledOverlay` | `#1C1B1F` @ 38 % | Disabled overlay |

---

## Dark Palette

### Base colors (dark)

| Token | Hex |
|---|---|
| `base.blue500` | `#8CB4FF` |
| `base.blue700` | `#2F5FAE` |
| `base.gray50` | `#E2E2E9` |
| `base.gray200` | `#8E99A8` |
| `base.gray900` | `#10131A` |
| `base.green500` | `#8EDAA5` |
| `base.amber500` | `#FFC870` |
| `base.red500` | `#FFB4AB` |
| `base.cyan500` | `#84D2FF` |

### Content colors (dark)

| Token | Hex |
|---|---|
| `content.primary` | `#E2E2E9` |
| `content.secondary` | `#C5C8D0` |
| `content.tertiary` | `#9EA6B2` |
| `content.inverse` | `#10131A` |
| `content.disabled` | `#E2E2E9` @ 32 % |

### Surfaces and brand (dark)

| Token | Hex |
|---|---|
| `background` | `#10131A` |
| `surface` | `#161B22` |
| `surfaceVariant` | `#222A35` |
| `onBackground` | `#E2E2E9` |
| `onSurface` | `#E2E2E9` |
| `primary` | `#8CB4FF` |
| `onPrimary` | `#002B61` |
| `secondary` | `#B7C9DD` |
| `onSecondary` | `#1D3144` |
| `error` | `#FFB4AB` |
| `onError` | `#690005` |
| `success` | `#8EDAA5` |
| `warning` | `#FFC870` |
| `info` | `#84D2FF` |
| `outline` | `#8E99A8` |
| `border` | `#414C5A` |
| `scrim` | `#000000` @ 60 % |

### Interaction overlays (dark)

| Token | Hex |
|---|---|
| `interactions.pressed` | `#E2E2E9` @ 16 % |
| `interactions.focused` | `#E2E2E9` @ 20 % |
| `interactions.hovered` | `#E2E2E9` @ 12 % |
| `interactions.disabledOverlay` | `#E2E2E9` @ 32 % |

---

## Usage examples

```kotlin
@Composable
fun StatusBanner(isError: Boolean) {
    val colors = DesignSystem.colors

    Box(
        modifier = Modifier.background(
            if (isError) colors.semantic.error else colors.semantic.success
        )
    ) {
        Text(
            text = if (isError) "Error" else "Success",
            color = colors.content.inverse,
        )
    }
}
```

---

## Customizing colors

```kotlin
val customColors = DefaultTokens.lightColors().copy(
    primary   = Color(0xFF6200EE),
    onPrimary = Color(0xFFFFFFFF),
)

DSTheme(colors = customColors) { /* ... */ }
```

See [Customization](../customization.md) for full override patterns.

---

## Related

- [Token System](./index.md)
- [Dark Theme](../theme/dark-theme.md)
- [Customization](../customization.md)


