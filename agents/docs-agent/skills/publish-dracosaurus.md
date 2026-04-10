# publish-dracosaurus

## Goal
Publish generated docs to Dracosaurus.

## Rules
- Default command: `dracosaurus push ./docs`.
- Command is configurable in `agents/docs-agent/config.json`.
- Local execution must not block git push on publish failure.
- CI retries publish with backoff.
- Always log command output and failure reason.

