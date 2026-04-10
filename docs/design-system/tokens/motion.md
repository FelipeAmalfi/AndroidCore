# Motion Tokens

**Type:** `DSMotion`  
**Source:** `design-system/src/main/java/com/url/androidcore/designsystem/tokens/motion/DSMotion.kt`  
**Accessed via:** `DesignSystem.motion`

---

## Overview

`DSMotion` centralizes animation timing and easing. It includes shared durations (`DSMotionDurations`) plus four easing curves.

---

## Default values

### Durations (milliseconds)

| Token | Value | Typical usage |
|---|---|---|
| `durations.short` | `120` | Micro-interactions, press states |
| `durations.medium` | `240` | Standard UI transitions |
| `durations.long` | `360` | Larger transitions and staged motion |

### Easing tokens

| Token | Default |
|---|---|
| `standardEasing` | `FastOutSlowInEasing` |
| `emphasizedEasing` | `CubicBezierEasing(0.2f, 0f, 0f, 1f)` |
| `decelerateEasing` | `LinearEasing` |
| `accelerateEasing` | `FastOutLinearInEasing` |

---

## Usage example

```kotlin
@Composable
fun AnimatedVisibilityPanel(visible: Boolean, content: @Composable () -> Unit) {
    val motion = DesignSystem.motion

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = motion.durations.medium,
                easing = motion.standardEasing,
            )
        ),
        exit = fadeOut(
            animationSpec = tween(
                durationMillis = motion.durations.short,
                easing = motion.accelerateEasing,
            )
        ),
    ) {
        content()
    }
}
```

---

## Customizing motion

```kotlin
val snappierMotion = DefaultTokens.motion.copy(
    durations = DefaultTokens.motion.durations.copy(short = 90, medium = 180),
)

DSTheme(motion = snappierMotion) { /* ... */ }
```

For durations, `merge(other)` keeps the fallback when an override value is negative.

---

## Related

- [Token System](./index.md)
- [Layout Tokens](./layout.md)
- [Customization](../customization.md)

