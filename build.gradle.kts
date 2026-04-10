// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.library") version "8.13.2" apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

val localAiGroup = "local-ai"
val isWindows = System.getProperty("os.name").contains("Windows", ignoreCase = true)

tasks.register("setupLocalAI") {
    group = localAiGroup
    description = "Exports bundled agents and skills into .ai/ for local AI tooling."

    doLast {
        val sourceAgentsDir = file("app/src/main/assets/agents/local-ai")
        val sourceSkillsDir = file("app/src/main/assets/skills")
        val outputRoot = file(".ai")
        val outputAgentsDir = file(".ai/agents")
        val outputSkillsDir = file(".ai/skills")

        if (!sourceAgentsDir.exists()) {
            throw GradleException("Agents source directory not found: ${sourceAgentsDir.absolutePath}")
        }

        if (!sourceSkillsDir.exists()) {
            throw GradleException("Skills source directory not found: ${sourceSkillsDir.absolutePath}")
        }

        delete(outputAgentsDir, outputSkillsDir)
        outputAgentsDir.mkdirs()
        outputSkillsDir.mkdirs()

        copy {
            from(sourceAgentsDir)
            include("*.json")
            into(outputAgentsDir)
        }

        copy {
            from(sourceSkillsDir)
            include("*.md", "*.txt")
            into(outputSkillsDir)
        }

        val agentFiles = outputAgentsDir.listFiles { file -> file.extension.equals("json", ignoreCase = true) }
            ?.map { it.name }
            ?.sorted()
            ?: emptyList()
        val skillFiles = outputSkillsDir.listFiles { file -> file.isFile }
            ?.map { it.name }
            ?.sorted()
            ?: emptyList()

        val manifestContent = buildString {
            appendLine("{")
            appendLine("  \"version\": \"1.0.0\",")
            appendLine("  \"generatedAtUtc\": \"${java.time.Instant.now()}\",")
            appendLine("  \"agents\": [")
            append(agentFiles.joinToString(",\n") { "    \"$it\"" })
            appendLine()
            appendLine("  ],")
            appendLine("  \"skills\": [")
            append(skillFiles.joinToString(",\n") { "    \"$it\"" })
            appendLine()
            appendLine("  ]")
            appendLine("}")
        }

        file(".ai/manifest.json").writeText(manifestContent)

        logger.lifecycle("Exported local AI artifacts to ${outputRoot.absolutePath}")
    }
}

tasks.register<Exec>("installLocalMcp") {
    group = localAiGroup
    description = "Installs npm dependencies for the local MCP server."
    workingDir = file("mcp-server")
    commandLine(if (isWindows) listOf("cmd", "/c", "npm install") else listOf("sh", "-c", "npm install"))
}

tasks.register<Exec>("startLocalMcp") {
    group = localAiGroup
    description = "Starts the local MCP server on localhost:3333."
    dependsOn("setupLocalAI", "installLocalMcp")
    workingDir = file("mcp-server")
    commandLine(if (isWindows) listOf("cmd", "/c", "npm run start") else listOf("sh", "-c", "npm run start"))
}

tasks.register("initAI") {
    group = localAiGroup
    description = "Bootstraps local AI files and starts the local MCP server."
    dependsOn("startLocalMcp")
}
