# End-to-End Example

## Scenario

Imagine a simple `User` feature that loads a profile and shows it on screen.

This example is intentionally high level. It shows how the repository is meant to be used without turning `docs/` into a template dump.

## 1. Start with a feature brief

Example inputs:

- endpoint: `GET /users/{id}`
- domain concept: `User`
- user interaction: open profile screen, retry on failure
- business rule: blank IDs are invalid

## 2. Data layer outcome

At the data layer, you would typically define:

- a remote service contract
- a `UserDto`
- a remote data source using `safeApiCall(...)`
- a repository implementation that fulfills the domain contract

Main concern at this stage:

- getting data safely
- handling transport or persistence failures early
- preparing for mapping into domain

## 3. Domain layer outcome

At the domain layer, you would define:

- a `User` domain model
- a `UserRepository` contract
- a `GetUserUseCase`

Main concern at this stage:

- validation
- business meaning
- returning `Result<User>` instead of leaking implementation details upward

## 4. Mapping outcome

Mapping stays explicit and boring on purpose.

Typical transformations:

- `UserDto.toDomain()`
- `User.toUi()`

Main concern at this stage:

- converting shapes between layers without adding business logic

## 5. Presentation outcome

At the presentation layer, the feature would usually have:

- `UserIntent`
- `UserUiState`
- `UserUiEffect`
- `UserViewModel`

A typical interaction looks like this:

```text
Screen opens
→ dispatch LoadUser intent
→ ViewModel calls launchData(...)
→ GetUserUseCase runs
→ repository returns Result<User>
→ state updates on success or failure
→ UI renders data or error
```

## 6. Testing outcome

Useful tests would usually cover:

- blank ID validation
- success from repository
- failure mapped to user-safe messaging
- loading and success state transitions
- one-time retry or navigation effects if used

## Example feature shape

```text
feature/user/
├── data/
│   ├── dto/
│   ├── datasource/
│   └── repository/
├── domain/
│   ├── model/
│   ├── repository/
│   └── usecase/
└── presentation/
    ├── contract/
    ├── mapper/
    └── viewmodel/
```

The exact package and filename rules remain in `.github/skills/` and `.github/agents/`.

## Why this flow matters

This order helps avoid a common failure mode: building the UI first and discovering too late that the domain contract or data shape is unstable.

By working from data to domain to mapping to presentation to tests, each layer has the context it needs.

## What to reuse in this example

For a real implementation, the feature should rely on the shared core instead of inventing its own infrastructure.

That usually includes:

- `safeApiCall(...)`
- `AppError`
- `UseCase<P, R>`
- `Result<R>`
- `MviViewModel`
- `launchData(...)`
- testing helpers from `core/testing/`

## Where to go from here

- For the creation workflow: `docs/guides/how-to-create-feature.md`
- For architecture context: `docs/architecture/clean-architecture.md`
- For execution details: `.github/agents/*.agent.md`
- For precise rules: `.github/skills/*.skill.md`

