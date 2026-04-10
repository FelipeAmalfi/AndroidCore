# Layout Tokens

**Type:** `DSLayout`  
**Source:** `design-system/src/main/java/com/url/androidcore/designsystem/tokens/layout/DSLayout.kt`  
**Accessed via:** `DesignSystem.layout`

---

## Overview

`DSLayout` defines structural layout constraints so screens align to the same grid and accessibility baseline.

---

## Default values

| Token | Value | Purpose |
|---|---|---|
| `contentMaxWidth` | 720 dp | Max readable content width on large screens |
| `screenHorizontalPadding` | 16 dp | Default page horizontal inset |
| `sectionVerticalGap` | 24 dp | Space between major sections |
| `itemMinTouchSize` | 48 dp | Minimum interactive target size |
| `gridColumns` | 12 | Baseline grid column count |

---

## Usage examples

```kotlin
@Composable
fun ResponsiveContent(content: @Composable ColumnScope.() -> Unit) {
    val layout = DesignSystem.layout

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = layout.screenHorizontalPadding),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier.widthIn(max = layout.contentMaxWidth),
            verticalArrangement = Arrangement.spacedBy(layout.sectionVerticalGap),
            content = content,
        )
    }
}
```

```kotlin
Modifier.defaultMinSize(
    minWidth = DesignSystem.layout.itemMinTouchSize,
    minHeight = DesignSystem.layout.itemMinTouchSize,
)
```

---

## Customizing layout

```kotlin
val tabletLayout = DefaultTokens.layout.copy(
    contentMaxWidth = 840.dp,
    gridColumns = 16,
)

DSTheme(layout = tabletLayout) { /* ... */ }
```

`merge(other)` keeps existing values when `Dp` is `Dp.Unspecified` or `gridColumns <= 0`.

---

## Related

- [Token System](./index.md)
- [Spacing Tokens](./spacing.md)
- [Customization](../customization.md)

