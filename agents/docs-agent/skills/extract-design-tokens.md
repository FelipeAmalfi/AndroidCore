# extract-design-tokens

## Goal
Extract design token data from the design-system module or MCP export.

## Rules
- Parse colors from `DSColors.kt` token constants.
- Parse spacing from `DSSpacing.kt` default values.
- Parse typography from `DSTypography.kt` default styles.
- If MCP is enabled, merge MCP token data on top of local tokens.
- Record source and extraction timestamp.

