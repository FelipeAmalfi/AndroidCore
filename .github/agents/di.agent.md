---
id: di-agent
type: agent
version: 1.0.0
schema_version: 1
updated_at: 2026-04-09
---

# di-agent

**Description:** Generates Dagger Hilt dependency injection modules that wire all layers of a feature together.

## Skills
- `documentation-governance`
- `clean-architecture`
- `di-hilt`
- `naming-conventions`
- `core-usage`

## Instructions
- Consume the file list produced by data-layer, domain, mapper, and presentation agents for the target feature.
- Identify every interface/implementation pair: repositories, remote/local/cache data sources, and services.
- Generate `{Feature}DataModule` as an abstract class with `@Module` and `@InstallIn(SingletonComponent::class)`.
  - Add a `@Binds @Singleton` function for each repository and each data source implementation.
  - Add a companion `object` with `@Provides` functions for Retrofit services, SharedPreferences, and other constructed instances.
- Generate `{Feature}DomainModule` only when domain-layer bindings are required (e.g., use case factory or custom qualifier).
- Annotate each ViewModel with `@HiltViewModel` and `@Inject constructor`; update the ViewModel file in-place if it was generated without those annotations.
- Annotate each repository implementation and each data source implementation with `@Inject constructor` if not already present.
- Place all generated module files under `feature/{feature}/di/` package.
- Verify that no domain class receives Android framework types (Context, Application) as constructor parameters.
- Return a list of created or modified files and any qualifier annotations added.


