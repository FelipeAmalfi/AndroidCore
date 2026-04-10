# DSTheme

Source: `design-system/src/main/java/com/url/androidcore/designsystem/theme/DSTheme.kt`

## Description

DSTheme is a Compose API extracted by the docs agent.

## Props

| Name | Type | Default |
|---|---|---|
| darkTheme | Boolean | false |
| colors | DSColors | if (darkTheme) DefaultTokens.darkColors() else DefaultTokens.lightColors() |
| typography | DSTypography | DefaultTokens.typography |
| spacing | DSSpacing | DefaultTokens.spacing |
| shapes | DSShapes | DefaultTokens.shapes |
| elevation | DSElevation | DefaultTokens.elevation |
| border | DSBorder | DefaultTokens.border |
| opacity | DSOpacity | DefaultTokens.opacity |
| motion | DSMotion | DefaultTokens.motion |
| layout | DSLayout | DefaultTokens.layout |
| content | @Composable () -> Unit | - |

## State Inputs

None

## Usage

```kotlin
DSTheme(
    // TODO: provide arguments
)
```

## Previews

No previews found.