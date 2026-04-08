# Using Agents in Projects That Import AndroidCore

When your project imports **AndroidCore** as a library, you can access the complete multi-agent system for code generation.

## 📚 Accessing Agent Documentation

The agent documentation is bundled with the AndroidCore library in the `assets/agents/` folder.

### Method 1: Using AgentDocumentation Helper

```kotlin
import com.url.androidcore.core.agent.AgentDocumentation

class YourActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            // Read specific agent documentation
            val agentSpec = AgentDocumentation.readDocumentation(
                context = this,
                fileName = "AGENTS.md"
            )
            
            // Or use convenience functions
            val executionGuide = AgentDocumentation.getExecutionGuide(this)
            val templates = AgentDocumentation.getTemplates(this)
            
            println(agentSpec)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
```

### Method 2: Direct Asset Access

```kotlin
val fileName = "AGENTS.md"
val filePath = "agents/$fileName"
val content = context.assets.open(filePath).bufferedReader().use { it.readText() }
```

## 🎯 What Documentation is Available

All agent documentation files are included:

- **`AGENTS.md`** - Complete agent specifications
- **`AGENT_EXECUTION_GUIDE.md`** - Step-by-step workflow
- **`AGENT_QUICK_INDEX.md`** - Navigation guide
- **`AGENT_SYSTEM_SETUP.md`** - System overview
- **`AGENT_TEMPLATES.md`** - Code templates
- **`AGENT_INTEGRATION_POINTS.md`** - Dependencies
- **`AGENT_VISUAL_WORKFLOW.md`** - Visual diagrams
- **`README_AGENTS.md`** - Complete documentation index

## 🚀 How to Use the Agents

### Step 1: Prepare Your Feature Specification

Document what you want to build:
- API endpoints
- Data models
- Business logic
- UI interactions

### Step 2: Read the Agents Documentation

```kotlin
// Get the execution guide
val guide = AgentDocumentation.getExecutionGuide(context)

// Share with your team or use for reference
```

### Step 3: Follow the Agent Workflow

The agents work in phases:

1. **DATA LAYER AGENT** - Creates data access layer
2. **DOMAIN AGENT** - Creates business logic layer
3. **MAPPER AGENT** - Creates transformation functions
4. **PRESENTATION AGENT** - Creates MVI components
5. **TEST AGENT** - Creates comprehensive tests
6. **ENHANCEMENT AGENT** (optional) - Adds optimizations

### Step 4: Integrate Generated Code

Use the templates from `AGENT_TEMPLATES.md` to generate code that:
- Extends the core utilities provided by AndroidCore
- Follows the documented architecture
- Integrates seamlessly with your app

## 💡 Example: Build a User Feature

```
FEATURE SPEC:
- GET /api/users/{id}
- POST /api/users
- Domain model: User
- UI: List users and show details

AGENT WORKFLOW:
1. DATA LAYER AGENT
   → UserService, UserDto, UserDataSource, UserRepositoryImpl

2. DOMAIN AGENT
   → UserRepository interface, UserModel, GetUserUseCase

3. MAPPER AGENT
   → UserDto.toDomain(), UserModel.toUi()

4. PRESENTATION AGENT
   → UserIntent, UserUiState, UserUiEffect, UserViewModel

5. TEST AGENT
   → UserUseCaseTest, UserRepositoryImplTest, UserViewModelTest

RESULT:
✅ 15+ production-ready files
✅ 1000+ lines of code
✅ 100% Clean Architecture
✅ Full test coverage
```

## 🔧 Integration with Core Utilities

AndroidCore provides reusable utilities you should use:

```kotlin
// From core/mvi/
import com.url.androidcore.core.mvi.MviViewModel

// From core/usecase/
import com.url.androidcore.core.usecase.UseCase

// From core/error/
import com.url.androidcore.core.error.AppError

// From core/coroutines/
import com.url.androidcore.core.coroutines.DispatcherProvider

// Example: Your ViewModel extends MviViewModel
class YourViewModel(
    private val useCase: YourUseCase,
    private val dispatchers: DispatcherProvider
) : MviViewModel<YourIntent, YourUiState, YourUiEffect>()
```

## 📋 Available Agent Documentation

```kotlin
// List all available documents
val docs = AgentDocumentation.availableDocuments
docs.forEach { fileName ->
    println("Available: $fileName")
}
```

## ⚠️ Important Notes

1. **Documentation is Reference** - The agent system is designed to guide code generation, not to be code itself
2. **Follow Best Practices** - Always follow the patterns documented in the agents
3. **Use Core Utilities** - Reuse core utilities instead of recreating them
4. **Maintain Architecture** - Keep Clean Architecture layers separate
5. **Test Everything** - Follow the testing patterns from TEST AGENT

## 🎓 Learning Resources

For importing projects, use this learning path:

1. **Quick Start** (15 min)
   ```
   Read: AGENT_QUICK_INDEX.md
   ```

2. **Understand System** (30 min)
   ```
   Read: AGENT_SYSTEM_SETUP.md + AGENTS.md
   ```

3. **Learn Workflow** (20 min)
   ```
   Read: AGENT_EXECUTION_GUIDE.md
   ```

4. **Study Examples** (30 min)
   ```
   Read: AGENT_TEMPLATES.md
   ```

5. **Build Your Feature** (60 min)
   ```
   Follow AGENT_EXECUTION_GUIDE.md workflow
   ```

## 🔗 Example: Display Agents in Your App

```kotlin
class AgentDocumentationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        lifecycleScope.launch {
            try {
                val agentDocs = AgentDocumentation.readDocumentation(
                    this@AgentDocumentationActivity,
                    "AGENTS.md"
                )
                
                // Display in WebView or TextView
                binding.webView.loadData(
                    "<pre>$agentDocs</pre>",
                    "text/html",
                    "utf-8"
                )
            } catch (e: IOException) {
                binding.textView.text = "Failed to load: ${e.message}"
            }
        }
    }
}
```

## ✅ Checklist: Using Agents in Your Project

- [ ] Import AndroidCore library
- [ ] Access agent documentation via `AgentDocumentation`
- [ ] Read feature specification guide
- [ ] Follow DATA LAYER AGENT workflow
- [ ] Follow DOMAIN AGENT workflow
- [ ] Generate mapping functions
- [ ] Create PRESENTATION layer with MVI
- [ ] Write comprehensive tests
- [ ] Validate code against quality standards
- [ ] Deploy with confidence

## 📞 Need Help?

All documentation is available in the library:

1. Check `AGENT_QUICK_INDEX.md` for navigation
2. Review `AGENT_TEMPLATES.md` for code examples
3. Follow `AGENT_EXECUTION_GUIDE.md` for workflow
4. Read `AGENT_INTEGRATION_POINTS.md` for dependencies

---

**Your AndroidCore library includes everything needed to build production-ready Android features using the multi-agent system!**

