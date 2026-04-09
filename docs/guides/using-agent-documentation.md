# Using AndroidCore Bundled Documentation

## Purpose

`AndroidCore` includes bundled markdown documentation under `app/src/main/assets/agents/**`.
These assets are useful as static reference material and can be copied into consuming projects.

This is useful when you want to:

- keep a local copy of the library guidance while integrating the core toolkit
- reference architecture patterns and naming conventions
- copy documentation into your own project for team reference

## What is exposed

The bundled documentation is stored under `app/src/main/assets/agents/**`.

Categories include:

- `overview/*`
- `architecture/*`
- `guides/*`
- `examples/*`

## Recommended workflow for consumer repositories

If the goal is to help Copilot generate code in the consuming project, the most reliable flow is:

1. import `AndroidCore`
2. copy the relevant docs from `app/src/main/assets/agents/**` into your consuming repository
3. or run `scripts/export-androidcore-guidance.ps1` to bundle guidance files
4. keep the guidance in files that live inside the consumer repository
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


