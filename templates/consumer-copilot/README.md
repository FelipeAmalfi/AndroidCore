# AndroidCore Consumer Copilot Template

Copy this template into a consuming repository when you want Copilot to follow AndroidCore guidance consistently.

## What this template gives you
- a consumer-facing `AGENTS.md`
- a consumer-facing `.github/copilot-instructions.md`
- a local `docs/androidcore/androidcore-guidance.md` reference file

These files are designed to live **inside the consumer repository**, because that is where Copilot reads project guidance most reliably.

## Recommended destination layout in the consumer repo

```text
<consumer-repo>/
├── AGENTS.md
├── .github/
│   └── copilot-instructions.md
└── docs/
    └── androidcore/
        └── androidcore-guidance.md
```

## How to use
1. Copy these files into the consuming repository.
2. Adjust package names, module names, and any app-specific paths.
3. Keep the AndroidCore rules that matter for generated features.
4. Re-sync when AndroidCore guidance changes.

## When to prefer the export script instead
Use `scripts/export-androidcore-guidance.ps1` from the AndroidCore repository when you want a repeatable sync into many downstream repositories.

## What not to assume
Importing the AndroidCore library dependency alone does not make Copilot use the library guidance automatically.
The guidance needs to exist as files in the consumer repository, or be attached/pasted into chat.

