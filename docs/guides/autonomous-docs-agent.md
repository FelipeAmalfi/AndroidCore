# Autonomous Docs Agent

This guide explains the zero-manual docs automation added to AndroidCore.

## What runs on push

- Local pre-push hook runs docs agent.
- Docs generation runs only when relevant source changes are detected.
- Docusaurus site build is attempted.
- Site build failures in local hook are logged and do not block push.

## Flow

1. Scan changed files.
2. Extract Kotlin Compose components.
3. Extract design tokens.
4. Generate docs markdown.
5. Organize docs directories.
6. Build Docusaurus site.

## Key files

- `agents/docs-agent/config.json`
- `agents/docs-agent/runner.js`
- `scripts/hooks/pre-push`
- `.github/workflows/docs.yml`
- `build.gradle.kts` (`generateDocs`, `publishDocs`, `buildDocsSite`, `installDocsPrePushHook`)

## Local commands

```powershell
.\gradlew.bat installDocsPrePushHook
.\gradlew.bat generateDocs
.\gradlew.bat publishDocs
.\gradlew.bat buildDocsSite
```

## CI

Workflow: `.github/workflows/docs.yml`

- Trigger: `pull_request`, `push` (branch `main`), and manual `workflow_dispatch`
- Builds Docusaurus on each trigger
- Deploys to GitHub Pages only on pushes to `main`
- Uploads docs artifacts when failures happen

