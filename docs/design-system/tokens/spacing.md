# Spacing Tokens

**Type:** `DSSpacing`  
**Source:** `designsystem/tokens/spacing/DSSpacing.kt`  
**Accessed via:** `DesignSystem.spacing`

---

## Overview

`DSSpacing` provides a seven-step spacing scale expressed in `Dp`. Use it for padding, margins, and gaps throughout the UI. The scale follows a geometric progression that keeps density consistent across screen sizes.

---

## Default scale

| Token | Value | Usage |
|---|---|---|
| `xxs` | 2 dp | Tight micro-gaps between inline elements |
| `xs` | 4 dp | Icon padding, badge inner margin |
| `sm` | 8 dp | Between closely related elements (label + field) |
| `md` | 12 dp | Intra-card spacing, compact list gaps |
| `lg` | 16 dp | Standard section padding, screen horizontal padding |
| `xl` | 24 dp | Between visually distinct groups |
| `xxl` | 32 dp | Large section separators, hero-area breathing room |

---

## Usage examples

### Padding

```kotlin
@Composable
fun ContentCard(content: @Composable () -> Unit) {
    val spacing = DesignSystem.spacing

    Box(
        modifier = Modifier.padding(
            horizontal = spacing.lg,
            vertical   = spacing.md,
        )
    ) {
        content()
    }
}
```

### Gap between items

```kotlin
@Composable
fun FormField(label: String, value: String) {
    val spacing = DesignSystem.spacing

    Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
        Text(label)
        TextField(value = value, onValueChange = {})
    }
}
```

---

## Customizing spacing

```kotlin
val compactSpacing = DefaultTokens.spacing.copy(
    md = 8.dp,
    lg = 12.dp,
)

DSTheme(spacing = compactSpacing) { /* ... */ }
```

---

## Related

- [Token System](./index.md)
- [Layout Tokens](./layout.md)
- [Customization](../customization.md)

