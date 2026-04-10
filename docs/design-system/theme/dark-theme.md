# Dark Theme

**Sources:**  
- `design-system/src/main/java/com/url/androidcore/designsystem/theme/DSTheme.kt`  
- `design-system/src/main/java/com/url/androidcore/designsystem/tokens/color/DSColors.kt`

---

## Overview

Dark mode in this module is color-driven. When `darkTheme = true`, `DSTheme` defaults `colors` to `DefaultTokens.darkColors()`; all other token families stay unchanged unless explicitly overridden.

---

## Enabling dark theme

```kotlin
setContent {
    DSTheme(darkTheme = isSystemInDarkTheme()) {
        AppRoot()
    }
}
```

You can also force dark mode in previews:

```kotlin
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun CardDarkPreview() {
    DSTheme(darkTheme = true) {
        DemoCard()
    }
}
```

---

## What changes in dark mode

`darkTheme` switches only `DSColors` defaults:

- `background` -> `#10131A`
- `surface` -> `#161B22`
- `content.primary` -> `#E2E2E9`
- `primary` -> `#8CB4FF`
- `scrim` -> `#000000` at 60%

See [Color Tokens](../tokens/colors.md) for the full palette table.

---

## Overriding dark colors

If your brand needs custom dark colors, pass `colors` directly:

```kotlin
val brandDark = DefaultTokens.darkColors().copy(
    primary = Color(0xFF91A6FF),
    onPrimary = Color(0xFF0A1A3A),
)

DSTheme(darkTheme = true, colors = brandDark) {
    AppRoot()
}
```

`colors` has priority over `darkTheme`-derived defaults when both are provided.

---

## Related

- [DSTheme API](./DSTheme.md)
- [Default Tokens](./default-tokens.md)
- [Color Tokens](../tokens/colors.md)

