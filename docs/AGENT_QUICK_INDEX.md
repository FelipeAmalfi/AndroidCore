# Multi-Agent System - Quick Reference Index

## 📚 Documentation Overview

This is your guide to understanding and using the multi-agent code generation system.

---

## 📖 Documentation Files

### 1. **AGENTS.md** - Core System Design
   - **What:** Complete reference for all 6 agents
   - **Use When:** You need to understand what each agent does
   - **Contains:**
     - Agent responsibilities
     - Input/output specifications
     - Rules and best practices
     - Naming conventions
     - Quality standards

### 2. **AGENT_EXECUTION_GUIDE.md** - How to Use the System
   - **What:** Step-by-step workflow for feature generation
   - **Use When:** You want to generate a new feature
   - **Contains:**
     - Required information format
     - Execution order (1-6)
     - Phase descriptions
     - Example workflow
     - Validation checklist

### 3. **AGENT_TEMPLATES.md** - Code Examples
   - **What:** Template patterns for each agent
   - **Use When:** You need to understand code structure
   - **Contains:**
     - Service interface template
     - DTO model template
     - DataSource template
     - Repository template
     - UseCase template
     - Mapper template
     - MVI components template
     - Test template
     - Enhancement template

### 4. **AGENT_INTEGRATION_POINTS.md** - How Agents Connect
   - **What:** Dependencies and integrations between agents
   - **Use When:** You need to understand data flow
   - **Contains:**
     - Agent dependencies
     - File locations
     - Reference patterns
     - Integration validation
     - Common patterns

### 5. **AGENT_SYSTEM_SETUP.md** - System Summary
   - **What:** Overview of the entire system
   - **Use When:** You're getting started or need a quick recap
   - **Contains:**
     - System overview
     - Architecture diagram
     - Quick start guide
     - Quality checklist
     - Key principles

### 6. **AGENT_QUICK_INDEX.md** - This File
   - **What:** Navigation guide for all documentation
   - **Use When:** You're lost and need directions

---

## 🎯 Quick Decision Tree

### "I want to understand the system"
→ Read: **AGENTS.md** (comprehensive overview)

### "I want to generate a feature"
→ Read: **AGENT_EXECUTION_GUIDE.md** (workflow)

### "I need code templates"
→ Read: **AGENT_TEMPLATES.md** (examples)

### "I need to know how agents connect"
→ Read: **AGENT_INTEGRATION_POINTS.md** (dependencies)

### "I need a quick recap"
→ Read: **AGENT_SYSTEM_SETUP.md** (summary)

### "I'm confused about where to find things"
→ You're reading the right file! 📍

---

## 6️⃣ The Six Agents

```
┌─────────────────────────────────────┐
│ 1. DATA LAYER AGENT                 │
│    Service, DTO, DataSource         │
└─────────────────────────────────────┘
                 ↓
┌─────────────────────────────────────┐
│ 2. DOMAIN AGENT                     │
│    Repository, UseCase, Model       │
└─────────────────────────────────────┘
                 ↓
┌─────────────────────────────────────┐
│ 3. MAPPER AGENT                     │
│    toDomain(), toUi() functions     │
└─────────────────────────────────────┘
                 ↓
┌─────────────────────────────────────┐
│ 4. PRESENTATION AGENT (MVI)         │
│    Intent, State, Effect, ViewModel │
└─────────────────────────────────────┘
                 ↓
┌─────────────────────────────────────┐
│ 5. TEST AGENT                       │
│    Unit tests, Integration tests    │
└─────────────────────────────────────┘
                 ↓
┌─────────────────────────────────────┐
│ 6. ENHANCEMENT AGENT (Optional)     │
│    Cache, Retry, Optimization       │
└─────────────────────────────────────┘
```

---

## 📋 When to Use Each Agent

### DATA LAYER AGENT
**When:** Starting a new feature or integrating a new API  
**Output:** Network/database access layer  
**Time:** Usually takes ~15 min per feature  
**Files:** 5 files (Service, DTO, DataSource x2, Repository)

### DOMAIN AGENT
**When:** After data layer is complete  
**Output:** Business logic and use cases  
**Time:** Usually takes ~15 min per feature  
**Files:** 2-5 files (Repository, Model, 1+ UseCase)

### MAPPER AGENT
**When:** After both data and domain layers  
**Output:** Transformation functions  
**Time:** Usually takes ~5 min per feature  
**Files:** 1 file (Mapper with extension functions)

### PRESENTATION AGENT
**When:** After mappers are ready  
**Output:** MVI components  
**Time:** Usually takes ~20 min per feature  
**Files:** 4 files (Intent, State, Effect, ViewModel)

### TEST AGENT
**When:** After all implementation is complete  
**Output:** Test suite  
**Time:** Usually takes ~30 min per feature  
**Files:** 4-5 files (Unit tests + integration tests)

### ENHANCEMENT AGENT
**When:** (Optional) After core is stable  
**Output:** Performance/resilience improvements  
**Time:** Varies by enhancement type  
**Files:** 1-3 files (Cache, Retry, etc.)

---

## 🏗️ Project Structure

```
AndroidCore/
├── AGENTS.md                          ← Agent documentation
├── AGENT_EXECUTION_GUIDE.md           ← How to use
├── AGENT_TEMPLATES.md                 ← Code templates
├── AGENT_INTEGRATION_POINTS.md        ← Dependencies
├── AGENT_SYSTEM_SETUP.md              ← Summary
├── AGENT_QUICK_INDEX.md               ← You are here
├── app/
│   └── src/
│       ├── main/
│       │   └── java/com/url/androidcore/
│       │       ├── core/              ← Reusable utilities
│       │       ├── data/              ← DATA LAYER output
│       │       ├── domain/            ← DOMAIN AGENT output
│       │       └── presentation/      ← PRESENTATION + MAPPER output
│       ├── test/                      ← TEST AGENT output (unit)
│       └── androidTest/               ← TEST AGENT output (integration)
```

---

## 💡 Common Tasks & Where to Find Answers

### "How do I request a new feature?"
→ **AGENT_EXECUTION_GUIDE.md** - Section: "Command Template"

### "What should my API look like for the data layer?"
→ **AGENTS.md** - Section: "1. DATA LAYER AGENT"

### "How do I structure a UseCase?"
→ **AGENT_TEMPLATES.md** - Section: "2. DOMAIN AGENT Templates"

### "What's the naming convention for ViewModels?"
→ **AGENTS.md** - Section: "Naming Conventions"

### "How do extension functions map between layers?"
→ **AGENT_TEMPLATES.md** - Section: "3. MAPPER AGENT Templates"

### "How do I write MVI tests?"
→ **AGENT_TEMPLATES.md** - Section: "5. TEST AGENT Templates"

### "How should I handle errors?"
→ **AGENTS.md** - Section: "Code Quality Standards"

### "Where do I put the new feature files?"
→ **AGENT_INTEGRATION_POINTS.md** - Section: "File Naming & Location Convention"

### "What's the data flow through the system?"
→ **AGENT_INTEGRATION_POINTS.md** - Section: "Agent-to-Agent Integration Points"

### "Can I use my own error handling?"
→ **AGENTS.md** - Section: "Core Library Integration"

---

## 🚀 Getting Started Roadmap

### Step 1: Read the Overview
1. Start with **AGENT_SYSTEM_SETUP.md** (10 min)
2. Understand the 6-agent flow
3. Review the naming conventions

### Step 2: Understand Each Agent
1. Skim **AGENTS.md** (15 min)
2. Note the agent responsibilities
3. Review the rules for your use case

### Step 3: Learn the Workflow
1. Read **AGENT_EXECUTION_GUIDE.md** (10 min)
2. Follow the step-by-step process
3. Prepare your feature spec

### Step 4: Study Code Patterns
1. Browse **AGENT_TEMPLATES.md** (15 min)
2. Understand the code structure
3. See the relationships

### Step 5: Understand Integration
1. Review **AGENT_INTEGRATION_POINTS.md** (10 min)
2. Understand data flow
3. Know file locations

### Step 6: Start Your First Feature!
1. Prepare feature specification
2. Request DATA LAYER AGENT
3. Follow the 6-agent workflow

**Total Setup Time:** ~60 minutes  
**First Feature Time:** ~2 hours (all 6 agents)

---

## 📊 Agent Comparison

| Agent | Responsibility | Difficulty | Time | Files |
|-------|---|---|---|---|
| Data Layer | API + Data Access | ⭐⭐ | 15m | 5 |
| Domain | Business Logic | ⭐⭐⭐ | 15m | 2-5 |
| Mapper | Transformation | ⭐ | 5m | 1 |
| Presentation | UI (MVI) | ⭐⭐⭐ | 20m | 4 |
| Test | Testing | ⭐⭐⭐ | 30m | 4-5 |
| Enhancement | Optimization | ⭐⭐ | Varies | 1-3 |

---

## ✅ Quality Standards (All Agents)

- ✓ Idiomatic Kotlin
- ✓ Clean Architecture
- ✓ Proper error handling
- ✓ Correct coroutines
- ✓ KDoc comments
- ✓ No core recreation
- ✓ Naming conventions
- ✓ Production-ready

---

## 🔗 Core Library Utilities (Do NOT Recreate!)

✅ **Must Use:**
- `MviViewModel` - Base class
- `UseCase` - Base interface
- `launchData()` - Coroutine helper
- `AppError` - Exception type
- `Throwable.toAppError()` - Error mapping
- `DispatcherProvider` - Dispatcher access
- `Logger` - Logging system

---

## 🎓 Learning Path

### For Beginners
1. AGENT_SYSTEM_SETUP.md
2. AGENT_EXECUTION_GUIDE.md
3. Request first feature

### For Intermediate Users
1. AGENTS.md
2. AGENT_TEMPLATES.md
3. Generate multiple features

### For Advanced Users
1. AGENT_INTEGRATION_POINTS.md
2. Generate with enhancements
3. Custom patterns

---

## 🆘 Troubleshooting

### "My feature isn't compiling"
→ Check **AGENT_INTEGRATION_POINTS.md** for import patterns

### "I'm getting import errors"
→ Verify file locations in **AGENT_INTEGRATION_POINTS.md**

### "The naming is inconsistent"
→ Reference **AGENTS.md** - Naming Conventions section

### "I don't know what the next step is"
→ Follow **AGENT_EXECUTION_GUIDE.md** step-by-step

### "My tests are failing"
→ Review **AGENT_TEMPLATES.md** - Test templates

### "I need help understanding dependencies"
→ Read **AGENT_INTEGRATION_POINTS.md**

---

## 📞 Contact & Support

When stuck, in order of priority:

1. **Check Documentation** - Most answers are here
2. **Review Examples** - AGENT_TEMPLATES.md has patterns
3. **Follow Workflow** - AGENT_EXECUTION_GUIDE.md has process
4. **Study Integration** - AGENT_INTEGRATION_POINTS.md explains flow

---

## 🎯 Next Step

**You're ready!** Choose one:

### Option A: Learn First
→ Read **AGENTS.md** in full

### Option B: Jump In
→ Follow **AGENT_EXECUTION_GUIDE.md** with your first feature

### Option C: Understand Integration
→ Study **AGENT_INTEGRATION_POINTS.md**

---

## 📈 Success Metrics

After generating your first feature, you should have:

- ✅ 5 data layer files (Service, DTO, DataSource x2, Repository)
- ✅ 2-5 domain layer files (Repository, Model, UseCase(s))
- ✅ 1 mapper file (extension functions)
- ✅ 4 presentation layer files (Intent, State, Effect, ViewModel)
- ✅ 4-5 test files (unit + integration tests)
- ✅ All code compiles without errors
- ✅ Follows Clean Architecture
- ✅ Uses core library utilities

**Total:** 20-30 files, ~2000+ lines of production-ready code

---

## 🎉 You're All Set!

The multi-agent system is ready to generate production-ready Android code.

**Start here:** [AGENT_EXECUTION_GUIDE.md](AGENT_EXECUTION_GUIDE.md)

Happy coding! 🚀


