# Autonomous Docs Agent

This bundled guide mirrors `docs/guides/autonomous-docs-agent.md`.

## Push automation

- Pre-push hook runs docs generation and publish.
- CI workflow retries publish failures.
- Generated docs are written into `docs/`.

## Core files

- `agents/docs-agent/config.json`
- `agents/docs-agent/runner.js`
- `scripts/hooks/pre-push`
- `.github/workflows/docs.yml`

