# Autonomous Docs Agent

This bundled guide mirrors `docs/guides/autonomous-docs-agent.md`.

## Push automation

- Pre-push hook runs docs generation and Docusaurus build.
- CI workflow triggers on `pull_request`, `push` to `main`, and `workflow_dispatch`.
- CI always builds Docusaurus and deploys via GitHub Pages only on pushes to `main`.
- Generated docs are written into `docs/`.

## Core files

- `agents/docs-agent/config.json`
- `agents/docs-agent/runner.js`
- `scripts/hooks/pre-push`
- `.github/workflows/docs.yml`

