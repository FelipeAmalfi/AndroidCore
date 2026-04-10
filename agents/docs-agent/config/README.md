# Docs Agent Config

Optional local overrides can be added in this folder.

- `mcp-tokens.json`: optional design tokens exported from Figma MCP.
- Environment variables:
  - `DRACOSAURUS_TOKEN`
  - `DRACOSAURUS_BASE_URL`

If `mcp-tokens.json` exists and MCP is enabled in `../config.json`, token extraction merges those values.

