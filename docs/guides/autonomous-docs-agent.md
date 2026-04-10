# Autonomous Docs Agent

This guide explains the zero-manual docs automation added to AndroidCore.

## What runs on push

- Local pre-push hook runs docs agent.
- Docs generation runs only when relevant source changes are detected.
- Dracosaurus publish is attempted.
- Publish failures in local hook are logged and do not block push.

## Flow

1. Scan changed files.
2. Extract Kotlin Compose components.
3. Extract design tokens.
4. Generate docs markdown.
5. Organize docs directories.
6. Publish docs.

## Key files

- `agents/docs-agent/config.json`
- `agents/docs-agent/runner.js`
- `scripts/hooks/pre-push`
- `.github/workflows/docs.yml`
- `build.gradle.kts` (`generateDocs`, `publishDocs`, `installDocsPrePushHook`)

## Local commands

```powershell
.\gradlew.bat installDocsPrePushHook
.\gradlew.bat generateDocs
.\gradlew.bat publishDocs
```

## CI

Workflow: `.github/workflows/docs.yml`

- Trigger: `push`
- Retries Dracosaurus publish up to 3 times
- Uploads docs artifacts when failures happen

