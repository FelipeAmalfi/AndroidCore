# Using AndroidCore Agent Documentation From Another Project

## Purpose

`AndroidCore` bundles a curated set of markdown guides as Android assets.
A consuming app can read them at runtime through `AgentDocumentation`.

This is useful when you want to:

- show architecture guidance inside a sample or debug screen
- inspect the library guidance from a host app
- export the documentation into a format you can paste into Copilot chat
- keep a local copy of the library guidance while integrating the core toolkit

## What is exposed

The bundled documentation is the mirrored copy under `app/src/main/assets/agents/**`.
The runtime entry point is `core/agent/AgentDocumentation.kt`.

Use `AgentDocumentation.availableDocuments` to discover what is bundled.
Current categories include:

- `overview/*`
- `architecture/*`
- `guides/*`
- `examples/*`

## Important limitation for Copilot

`AgentDocumentation` is a **runtime access API**.
It does **not** make Copilot or other IDE coding agents automatically read the documentation from the dependency.

That means:

- importing the library is enough for app code to read the docs at runtime
- importing the library is **not** enough for Copilot to use the docs as repository instructions by itself

If you want Copilot in the consuming project to follow this guidance consistently, you should do one of these:

1. copy the relevant guidance into the consuming repository (`AGENTS.md`, `README.md`, or local docs)
2. paste or attach the generated text into the Copilot chat prompt
3. export the generated text from the app and keep it in a promptable file inside the consuming repo

This repository now includes two downstream-friendly options:

- `templates/consumer-copilot/` for a copyable starter pack
- `scripts/export-androidcore-guidance.ps1` for repeatable sync into another repository

## Basic usage in a consuming Android project

```kotlin
import com.url.androidcore.core.agent.AgentDocumentation

suspend fun readDocs(context: android.content.Context) {
    val intro = AgentDocumentation.getIntroduction(context)
    val featureGuide = AgentDocumentation.getExecutionGuide(context)
    val allDocs = AgentDocumentation.readAllDocumentation(context)

    println(intro)
    println(featureGuide)
    println(allDocs.keys)
}
```

## Generate one bundle for Copilot chat

`AgentDocumentation.buildCopilotContext(context)` combines every bundled document into one markdown string.
That is useful when you want a single block of context to paste into a chat.

```kotlin
import com.url.androidcore.core.agent.AgentDocumentation

fun buildPromptContext(context: android.content.Context): String {
    return AgentDocumentation.buildCopilotContext(context)
}
```

## Export the bundle to a file in the consuming app

A consuming app can export the combined markdown and then copy it into its own repository docs.

```kotlin
import android.content.Context
import com.url.androidcore.core.agent.AgentDocumentation
import java.io.File

fun exportAndroidCoreGuidance(context: Context): File {
    val output = File(context.cacheDir, "androidcore-agent-context.md")
    output.writeText(AgentDocumentation.buildCopilotContext(context))
    return output
}
```

## Which API to use

Use the convenience methods when you already know the document you need:

- `getIntroduction(...)`
- `getSystemVision(...)`
- `getCleanArchitectureOverview(...)`
- `getMviOverview(...)`
- `getAgentsSpecification(...)`
- `getExecutionGuide(...)`
- `getConsumerUsageGuide(...)`
- `getEndToEndExample(...)`

Use the generic helpers when the consuming app wants to browse or export everything:

- `availableDocuments`
- `readDocumentation(...)`
- `readAllDocumentation(...)`
- `buildCopilotContext(...)`

## Recommended workflow for consumer repositories

If the goal is to help Copilot generate code in the consuming project, the most reliable flow is:

1. import `AndroidCore`
2. copy `templates/consumer-copilot/` into the consumer repository or run `scripts/export-androidcore-guidance.ps1`
3. optionally read/export the bundled docs with `AgentDocumentation`
4. keep the generated guidance in files that live inside the consumer repository
5. reference that local guidance from prompts and repository instruction files

This keeps the guidance visible to both humans and tooling.

## Repeatable export from this repository

From the AndroidCore repository root, you can export consumer-ready guidance into another project with:

```powershell
.\scripts\export-androidcore-guidance.ps1 -TargetRepoPath "C:\Path\To\ConsumerRepo" -Force
```

That command writes:

- `AGENTS.md`
- `.github/copilot-instructions.md`
- `docs/androidcore/androidcore-guidance.md`

into the target repository.

## Sync contract for maintainers

When bundled documentation changes in this library, keep these locations aligned:

- `docs/**`
- `app/src/main/assets/agents/**`
- `core/agent/AgentDocumentation.kt`

That includes `availableDocuments` and any convenience accessors.

