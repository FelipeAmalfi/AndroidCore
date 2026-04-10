# Opacity Tokens

**Type:** `DSOpacity`  
**Source:** `design-system/src/main/java/com/url/androidcore/designsystem/tokens/opacity/DSOpacity.kt`  
**Accessed via:** `DesignSystem.opacity`

---

## Overview

`DSOpacity` defines reusable alpha levels for disabled, subtle, and emphasis treatments. Keep alpha choices centralized here instead of scattering raw float literals.

---

## Default scale

| Token | Value | Typical usage |
|---|---|---|
| `disabled` | `0.38f` | Disabled content or controls |
| `subtle` | `0.60f` | De-emphasized text or icon layers |
| `medium` | `0.74f` | Secondary emphasis |
| `strong` | `0.92f` | Near-opaque overlays and emphasis |

---

## Usage example

```kotlin
@Composable
fun DisabledLabel(text: String, enabled: Boolean) {
    val opacity = DesignSystem.opacity

    Text(
        text = text,
        modifier = Modifier.alpha(if (enabled) 1f else opacity.disabled),
    )
}
```

---

## Customizing opacity

```kotlin
val contentOpacity = DefaultTokens.opacity.copy(
    subtle = 0.55f,
    medium = 0.70f,
)

DSTheme(opacity = contentOpacity) { /* ... */ }
```

`merge(other)` keeps existing values when the incoming float is outside `0f..1f`.

---

## Related

- [Token System](./index.md)
- [Color Tokens](./colors.md)
- [Customization](../customization.md)

