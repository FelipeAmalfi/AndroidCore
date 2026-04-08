# Agent and Skill Descriptor Versioning

## Goals
- Keep agent and skill descriptors human-readable.
- Keep prompt routing stable across file renames.
- Make behavior changes traceable with explicit versions.

## File Conventions
- Agents: `agents/*.agent.md`
- Skills: `skills/*.skill.md`
- Legacy `.agent` and `.skill` files can remain as temporary compatibility aliases.

## Metadata Contract
Every descriptor should include frontmatter fields:

```yaml
id: <stable-id>
type: agent | skill
version: <semver>
schema_version: 1
updated_at: YYYY-MM-DD
legacy_file: <old-file-name>
```

## Prompt Invocation Contract
- Always invoke by `id`/`name` (for example `data-layer-agent`).
- Do not invoke by filename or extension.

## Versioning Rules
- `MAJOR`: breaking behavior changes for existing prompt expectations.
- `MINOR`: backward-compatible behavior additions.
- `PATCH`: wording, clarity, typo, or metadata fixes with no behavior change.

## Recommended Release Practice
1. Update descriptor `version` and `updated_at`.
2. Commit descriptor and related docs updates together.
3. Tag repository release when multiple descriptors change.

