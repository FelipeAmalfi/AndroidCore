# How To Use The Figma Compose Agent

## Purpose

`figma-compose-agent` transforms Figma MCP design metadata into reusable Jetpack Compose visuals.

The agent focuses on visual generation only:

- read design data from MCP
- preserve layout/spacing/typography/color hierarchy
- produce Compose components with strong reuse
- report safe fallbacks for unsupported or ambiguous design input

## Quick Start

1. Select a Figma frame or component set and gather node metadata through MCP.
2. Decide output package and feature/component naming.
3. Invoke `figma-compose-agent` with the selected node ids and generation preferences.
4. Review generated files, assumptions, and TODO fallback notes.
5. Wire generated composables into presentation state/events only when needed.

## Example Invocation Prompt

```text
Run agent: figma-compose-agent

Use Figma MCP to read:
- file key: <figma-file-key>
- node ids: ["12:48", "12:130"]

Generate:
- package: com.url.androidcore.feature.profile.presentation.ui
- top-level composable: ProfileScreenContent
- reusable subcomponents: AvatarHeader, ProfileStatsRow, ProfileActionButton

Rules:
- Compose only, no XML
- prioritize visual fidelity, then reusability
- keep composables stateless where possible
- add TODO only for unresolved design details
```

## Input Expectations

Minimum data needed for high-fidelity output:

- node tree with parent/child ordering
- auto layout direction, spacing, padding, and alignment
- constraints and sizing behavior (fixed, hug, fill)
- text style data (font family, size, weight, line height)
- fills/strokes/radius/effects
- component and variant metadata when present

If some metadata is unavailable, the agent still generates compile-safe Compose with fallback notes.

## Figma To Compose Mapping Conventions

| Figma structure | Compose mapping |
|---|---|
| Frame with vertical auto layout | `Column` + `verticalArrangement` + `horizontalAlignment` |
| Frame with horizontal auto layout | `Row` + `horizontalArrangement` + `verticalAlignment` |
| Stacked/overlay children | `Box` + aligned children (`Modifier.align`) |
| Wrapping chip/tag groups | `FlowRow` when wrap behavior is explicit |
| Padding | `Modifier.padding(...)` |
| Item spacing | `Arrangement.spacedBy(...)` or `Spacer` |
| Fixed size | `Modifier.width/height/size(...)` |
| Fill container intent | `fillMaxWidth`, `fillMaxHeight`, or `weight` |
| Typography styles | `MaterialTheme.typography.*` or explicit `TextStyle` |
| Fill/stroke colors | semantic token/color constants, then literals |
| Corner radius | `RoundedCornerShape(...)` |
| Shadows/effects | `Modifier.shadow(...)` or closest safe approximation |

## Reusability Rules

- Extract repeated child structures into reusable composables when repeated 2+ times.
- Keep APIs stateless and parameterized.
- Prefer `modifier: Modifier = Modifier` as first optional UI parameter.
- Map component variants to explicit types/parameters instead of hardcoded branching.

## Fallback And Safety Rules

- Missing tokens: create named local constants and note token migration path.
- Unsupported effects: apply closest Compose equivalent and emit one TODO.
- Ambiguous constraints: default to deterministic, compile-safe sizing.
- Unknown image/icon sources: generate placeholders or slots, never fake assets.
- Deeply nested wrappers: flatten only if visual behavior is preserved.
- Never invent interactions, navigation, or animations absent from design data.

## Expected Output Shape

```text
Generated files:
- app/src/main/java/.../ProfileScreenContent.kt
- app/src/main/java/.../ProfileComponents.kt
- app/src/main/java/.../ProfilePreviews.kt

Notes:
- Mapping decisions (layout + style)
- Reused subcomponents extracted
- Fallback report for unresolved node details
```

## Example Compose Output Shape

```kotlin
@Composable
fun ProfileScreenContent(
    state: ProfileUi,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(ProfileTokens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(ProfileTokens.SectionSpacing),
    ) {
        AvatarHeader(
            name = state.name,
            role = state.role,
            avatarPainter = state.avatar,
        )
        ProfileStatsRow(stats = state.stats)
        ProfileActionButton(text = "Edit profile", onClick = onEditClick)
    }
}
```

## Troubleshooting

- Agent cannot map layout correctly: verify node ids and auto layout metadata from MCP.
- Typography mismatches: check whether Figma text styles are linked or detached.
- Token mismatches: ensure token naming map is included in prompt or project context.
- Output too monolithic: request stricter extraction threshold for repeated patterns.
- Missing assets: provide image/icon references or keep slot-based placeholders.

## Extending The Agent

1. Add new mapping behavior to a dedicated skill file under `.github/skills/`.
2. Keep `.github/agents/figma-compose.agent.md` orchestration-focused.
3. Mirror guide updates into `app/src/main/assets/agents/guides/`.
4. If local AI exports are affected, update `app/src/main/assets/agents/local-ai/` and `app/src/main/assets/skills/` together.


