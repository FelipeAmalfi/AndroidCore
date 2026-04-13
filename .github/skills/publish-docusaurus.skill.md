---
id: publish-docusaurus
type: skill
version: 1.0.0
schema_version: 1
updated_at: 2026-04-10
---

# publish-docusaurus

**Description:** Build and deploy generated docs with Docusaurus in local and CI contexts.

## Rules
- Default site build command is `npm run build` from `website/`.
- Keep publish command configurable through docs-agent config.
- Local pre-push flow must not block push when docs site build fails.
- CI installs website dependencies, builds the site, and deploys via GitHub Pages.



