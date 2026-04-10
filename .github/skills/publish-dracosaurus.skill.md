---
id: publish-dracosaurus
type: skill
version: 1.0.0
schema_version: 1
updated_at: 2026-04-10
---

# publish-dracosaurus

**Description:** Publish generated docs to Dracosaurus in local and CI contexts.

## Rules
- Default publish command is `dracosaurus push ./docs`.
- Keep publish command configurable through docs-agent config.
- Local pre-push flow must not block push when publish fails.
- CI retries publish failures with backoff.

