# Docs Agent

Autonomous documentation pipeline for AndroidCore.

## Pipeline

1. Scan project files.
2. Extract Kotlin Compose components.
3. Extract design tokens from `design-system`.
4. Generate markdown docs.
5. Organize docs structure.
6. Build Docusaurus site.

## Run

```powershell
npm --prefix .\website install
node .\agents\docs-agent\runner.js --mode generate
node .\agents\docs-agent\runner.js --mode publish
```

## Smoke test

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\agents\docs-agent\smoke-test.ps1
```

## Config

- Main config: `agents/docs-agent/config.json`
- Optional config notes: `agents/docs-agent/config/README.md`

## Outputs

- `docs/components/*`
- `docs/tokens/*`
- `docs/design-system/overview.md`
- `docs/architecture/documentation-pipeline.md`
- `docs/.generated/components.json`
- `docs/.generated/tokens.json`


