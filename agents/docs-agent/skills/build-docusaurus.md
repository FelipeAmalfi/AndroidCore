# build-docusaurus

## Goal
Build generated docs into a Docusaurus static site.

## Rules
- Default command: run `npm run build` from `website/`.
- Command is configurable in `agents/docs-agent/config.json`.
- Local execution must not block git push on site build failure.
- CI must install dependencies and build Docusaurus before deployment.
- Always log command output and failure reason.



