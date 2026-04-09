# androidcore-clean-architecture

Use these rules when generating feature code:

- Keep dependency direction: presentation -> domain, data -> domain contracts.
- Keep domain layer framework-light and free of Android UI imports.
- Reuse AndroidCore primitives (`UseCase`, `Result`, `AppError`, `MviViewModel`, `launchData`).
- Keep mapper layer extension-based (`Dto.toDomain()`, `Domain.toUi()`).
- Follow feature naming conventions from `AGENTS.md` and `.github/skills/naming-conventions.skill.md`.

