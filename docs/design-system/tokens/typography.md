# Typography Tokens

**Type:** `DSTypography`  
**Source:** `designsystem/tokens/typography/DSTypography.kt`  
**Accessed via:** `DesignSystem.typography`

---

## Overview

`DSTypography` defines eight text scales. Each scale is represented by a `DSTextStyle` value object that bundles four properties together:

| Property | Type | Description |
|---|---|---|
| `fontSize` | `TextUnit` (sp) | Size of the text |
| `lineHeight` | `TextUnit` (sp) | Vertical space for each line |
| `fontWeight` | `FontWeight` | Thickness of the glyphs |
| `letterSpacing` | `TextUnit` (sp) | Spacing between characters |

---

## Default scale

| Token | Font Size | Line Height | Font Weight | Letter Spacing | Usage |
|---|---|---|---|---|---|
| `displayLarge` | 40 sp | 48 sp | **Bold** | 0 sp | Hero headings |
| `headlineMedium` | 28 sp | 34 sp | **SemiBold** | 0 sp | Section titles |
| `titleLarge` | 22 sp | 28 sp | **SemiBold** | 0 sp | Card titles, screen titles |
| `titleMedium` | 18 sp | 24 sp | **Medium** | 0.15 sp | Sub-section labels |
| `bodyLarge` | 16 sp | 24 sp | Normal | 0.3 sp | Primary body text |
| `bodyMedium` | 14 sp | 20 sp | Normal | 0.2 sp | Supporting body text |
| `labelLarge` | 14 sp | 20 sp | **Medium** | 0.1 sp | Buttons, input labels |
| `labelSmall` | 12 sp | 16 sp | **Medium** | 0.5 sp | Captions, chips, badges |

---

## Usage examples

### Apply a scale to a `Text` composable

```kotlin
@Composable
fun ArticleCard(title: String, body: String) {
    val typography = DesignSystem.typography
    val colors     = DesignSystem.colors

    Column {
        Text(
            text  = title,
            style = typography.titleLarge.toTextStyle(),
            color = colors.content.primary,
        )
        Spacer(Modifier.height(DesignSystem.spacing.sm))
        Text(
            text  = body,
            style = typography.bodyMedium.toTextStyle(),
            color = colors.content.secondary,
        )
    }
}
```

> **Note:** `DSTextStyle` is a lightweight value class, not `androidx.compose.ui.text.TextStyle`. Convert it with an extension like:
> ```kotlin
> fun DSTextStyle.toTextStyle() = TextStyle(
>     fontSize      = fontSize,
>     lineHeight    = lineHeight,
>     fontWeight    = fontWeight,
>     letterSpacing = letterSpacing,
> )
> ```

---

## Customizing typography

Override only the scales you need using `copy()`:

```kotlin
val myTypography = DefaultTokens.typography.copy(
    displayLarge = DSTextStyle(
        fontSize      = 48.sp,
        lineHeight    = 56.sp,
        fontWeight    = FontWeight.ExtraBold,
        letterSpacing = 0.sp,
    )
)

DSTheme(typography = myTypography) { /* ... */ }
```

Use `merge(other)` to apply partial style overrides:

```kotlin
val merged = DefaultTokens.typography.bodyLarge.merge(
    DSTextStyle(
        fontSize      = TextUnit.Unspecified,  // keep default
        lineHeight    = TextUnit.Unspecified,  // keep default
        fontWeight    = FontWeight.Bold,       // override
        letterSpacing = TextUnit.Unspecified,  // keep default
    )
)
```

---

## Related

- [Token System](./index.md)
- [Customization](../customization.md)

