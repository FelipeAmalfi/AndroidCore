# Getting Started with the Design System

This guide explains how to add the `design-system` module to your project and wrap your Compose UI with `DSTheme`.

---

## 1. Add the dependency

In your app or feature module's `build.gradle.kts`:

```kotlin
dependencies {
    implementation(project(":design-system"))
}
```

Make sure `settings.gradle.kts` includes the module:

```kotlin
include(":design-system")
```

---

## 2. Wrap your root composable

Call `DSTheme` at the top of your composable hierarchy - typically in `Activity.setContent` or the root screen composable:

```kotlin
import com.url.androidcore.designsystem.theme.DSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DSTheme {
                // Your app content here
                MyRootScreen()
            }
        }
    }
}
```

`DSTheme` with no arguments uses the **light** colour palette and all default token values.

---

## 3. Enable dark theme support

Pass `darkTheme = true` (or derive from system settings) to switch to the dark token set:

```kotlin
import androidx.compose.foundation.isSystemInDarkTheme
import com.url.androidcore.designsystem.theme.DSTheme

setContent {
    DSTheme(darkTheme = isSystemInDarkTheme()) {
        MyRootScreen()
    }
}
```

See [Dark Theme Guide](./theme/dark-theme.md) for more details.

---

## 4. Access tokens inside composables

Use the `DesignSystem` object from any composable that runs inside `DSTheme`:

```kotlin
import com.url.androidcore.designsystem.core.DesignSystem

@Composable
fun MyCard() {
    val colors   = DesignSystem.colors
    val spacing  = DesignSystem.spacing
    val shapes   = DesignSystem.shapes

    Box(
        modifier = Modifier
            .background(colors.surface)
            .padding(spacing.lg)
            .clip(RoundedCornerShape(shapes.md))
    ) {
        Text(
            text = "Hello Design System",
            color = colors.content.primary,
        )
    }
}
```

---

## 5. Write Compose Previews

Wrap `@Preview` functions with `DSTheme` so they render with the correct tokens:

```kotlin
@Preview(showBackground = true)
@Composable
private fun MyCardPreview() {
    DSTheme {
        MyCard()
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun MyCardDarkPreview() {
    DSTheme(darkTheme = true) {
        MyCard()
    }
}
```

---

## What comes next

- [Token Reference](./tokens/index.md) - explore all token families
- [DSTheme API](./theme/DSTheme.md) - full parameter reference
- [Customization](./customization.md) - override tokens per-screen or per-brand
- [Dark Theme](./theme/dark-theme.md) - dark palette and dynamic switching


