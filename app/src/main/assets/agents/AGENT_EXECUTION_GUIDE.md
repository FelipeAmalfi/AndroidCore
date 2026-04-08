# Agent Execution Guide

This guide helps you use the multi-agent system to generate production-ready Android code.

## How to Request Code Generation

When you want to generate a new feature, provide:

### Required Information
```
Feature Name: {Name}
Description: {What does this feature do?}
API Endpoint(s): {URLs or REST specifications}
Data Models: {JSON structure or field descriptions}
Business Rules: {Core requirements and validations}
User Interactions: {What actions trigger state changes?}
```

### Example
```
Feature Name: UserProfile
Description: Fetch and display user profile information
API Endpoint: GET /api/v1/users/{id}
Data Models: 
  - User (id, name, email, avatar, bio)
  - UserResponse (user, timestamp)
Business Rules:
  - Validate email format
  - Cache user data for 1 hour
User Interactions:
  - Load profile on screen open
  - Retry on network error
  - Refresh profile manually
```

---

## Agent Execution Order

### Phase 1: Data Foundation
**Invoke:** DATA LAYER AGENT  
**Task:** Generate data access layer

```
Input: API specification, data models
Output:
  - UserService.kt (Retrofit interface)
  - UserDto.kt (Data models)
  - UserDataSource.kt + UserDataSourceImpl.kt
  - UserRepositoryImpl.kt (data layer)
```

---

### Phase 2: Business Logic
**Invoke:** DOMAIN AGENT  
**Task:** Generate domain layer and use cases

```
Input: Repository interface from Phase 1, business requirements
Output:
  - UserRepository.kt (domain interface)
  - UserModel.kt (domain model)
  - GetUserUseCase.kt
  - UpdateUserUseCase.kt (if applicable)
```

---

### Phase 3: Data Transformation
**Invoke:** MAPPER AGENT  
**Task:** Generate layer transformation functions

```
Input: DTOs from Phase 1, Domain Models from Phase 2, UI requirements
Output:
  - UserMapper.kt with:
    - UserDto.toDomain(): UserModel
    - UserModel.toUi(): UserUiModel
```

---

### Phase 4: UI Layer (MVI)
**Invoke:** PRESENTATION AGENT  
**Task:** Generate MVI components

```
Input: Repository interface from Phase 2, UI requirements, mapper from Phase 3
Output:
  - UserIntent.kt (User actions)
  - UserUiState.kt (UI state)
  - UserUiEffect.kt (Side effects)
  - UserViewModel.kt (MVI orchestrator)
```

---

### Phase 5: Testing
**Invoke:** TEST AGENT  
**Task:** Generate comprehensive tests

```
Input: All implementations from Phases 1-4
Output:
  - GetUserUseCaseTest.kt
  - UserRepositoryImplTest.kt
  - UserDataSourceImplTest.kt
  - UserViewModelTest.kt
```

---

### Phase 6: Enhancements (Optional)
**Invoke:** ENHANCEMENT AGENT  
**Task:** Add caching, retry logic, or transformations

```
Input: Existing architecture + enhancement requirement
Output:
  - Cache layer (if caching)
  - Retry wrapper (if retry needed)
  - Updated repository/datasource
```

---

## Example: Building a "Get User Profile" Feature

### Step 1: Request to DATA LAYER AGENT

```
Feature: User Profile (GET)
API Endpoint: GET /api/v1/users/{id}
Response Model:
{
  "id": "123",
  "name": "John Doe",
  "email": "john@example.com",
  "avatar": "https://...",
  "bio": "Software Engineer"
}
```

**Expected Output:**
- `UserService.kt` with `getUser(id: String): UserDto`
- `UserDto.kt` data class
- `UserDataSource.kt` + `UserDataSourceImpl.kt`
- `UserRepositoryImpl.kt` implementing `UserRepository`

---

### Step 2: Request to DOMAIN AGENT

```
Business Logic:
- Use case: Fetch user by ID
- Validate ID is not empty
- Map DTO to domain model
- Handle network errors
- Cache for 1 hour (enhancement later)
```

**Expected Output:**
- `UserRepository.kt` interface (domain)
- `UserModel.kt` domain model
- `GetUserUseCase.kt` implementation

---

### Step 3: Request to MAPPER AGENT

```
Mappings needed:
- UserDto → UserModel (for domain)
- UserModel → UserUiState (for presentation)
```

**Expected Output:**
- `UserMapper.kt` with extension functions

---

### Step 4: Request to PRESENTATION AGENT

```
UI Interactions:
- Screen loads → emit Loading state
- User refreshes → retry fetch
- Success → show user data
- Error → show error message
Navigation:
- On error, show retry button
- On success, show profile details
```

**Expected Output:**
- `UserIntent.kt` (LoadUser, RefreshUser)
- `UserUiState.kt` (Loading, Success, Error)
- `UserUiEffect.kt` (NavigateToDetail, ShowError)
- `UserViewModel.kt` with MVI logic

---

### Step 5: Request to TEST AGENT

```
Test coverage:
- UseCase: success and error cases
- Repository: verify datasource calls
- ViewModel: state transitions and effects
```

**Expected Output:**
- Unit tests for all components
- Mock implementations
- Success and error scenarios

---

### Step 6: Request to ENHANCEMENT AGENT (Optional)

```
Enhancement: Add caching
Requirements:
- Cache user data for 1 hour
- Invalidate cache on refresh
- Show stale data while fetching
```

**Expected Output:**
- Cache layer integration
- Updated repository
- Cache invalidation logic

---

## Command Template

When ready to generate code, use this format:

**Agent reference rule:**
- Invoke by stable agent `name` (for example `data-layer-agent`), not by filename.
- Descriptor files are Markdown and versioned under `agents/*.agent.md` and `skills/*.skill.md`.

```
[FEATURE SPECIFICATION]

Feature Name: {name}
Layer Agent: {DATA | DOMAIN | MAPPER | PRESENTATION | TEST | ENHANCEMENT}

API/Requirements:
{detailed specification}

Existing Contracts: {reference to previous agent outputs}

Expected Output:
{list of files to generate}
```

---

## Validation Checklist

After each agent completes, verify:

✅ All files created  
✅ Code compiles  
✅ Imports are correct  
✅ Follows naming conventions  
✅ Follows Clean Architecture  
✅ Uses core library utilities (no recreation)  
✅ Error handling implemented  
✅ Tests cover success and error paths  

---

## Ready?

Once you provide a complete feature specification, I'll start with the DATA LAYER AGENT and proceed through all phases in order.


