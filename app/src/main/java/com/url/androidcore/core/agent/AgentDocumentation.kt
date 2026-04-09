package com.url.androidcore.core.agent

import android.content.Context
import java.io.IOException

/**
 * Helper class to access agent documentation and specifications.
 *
 * Agent documentation is bundled with AndroidCore library as Android assets and can be read
 * by any project that imports this library.
 *
 * This is a runtime access mechanism for host apps and tooling built on top of the app.
 * It does not cause IDE assistants such as Copilot to automatically ingest the bundled
 * documentation from the dependency on their own.
 *
 * Usage:
 * ```
 * val intro = AgentDocumentation.getIntroduction(context)
 * val featureGuide = AgentDocumentation.getExecutionGuide(context)
 * val copilotContext = AgentDocumentation.buildCopilotContext(context)
 * ```
 */
object AgentDocumentation {

    private const val AGENTS_FOLDER = "agents"

    const val INTRODUCTION = "overview/introduction.md"
    const val SYSTEM_VISION = "overview/system-vision.md"
    const val CLEAN_ARCHITECTURE = "architecture/clean-architecture.md"
    const val MVI_OVERVIEW = "architecture/mvi-overview.md"
    const val HOW_TO_CREATE_FEATURE = "guides/how-to-create-feature.md"
    const val HOW_AGENTS_WORK = "guides/how-agents-work.md"
    const val USING_AGENT_DOCUMENTATION = "guides/using-agent-documentation.md"
    const val END_TO_END_EXAMPLE = "examples/end-to-end-example.md"

    /**
     * List of available agent documentation files.
     */
    val availableDocuments = listOf(
        INTRODUCTION,
        SYSTEM_VISION,
        CLEAN_ARCHITECTURE,
        MVI_OVERVIEW,
        HOW_TO_CREATE_FEATURE,
        HOW_AGENTS_WORK,
        USING_AGENT_DOCUMENTATION,
        END_TO_END_EXAMPLE
    )

    /**
     * Read a specific agent documentation file.
     *
     * @param context Android context for accessing assets
     * @param fileName Relative path under assets/agents (e.g., "overview/introduction.md")
     * @return Content of the documentation file as String
     * @throws IOException if file is not found or cannot be read
     */
    @Throws(IOException::class)
    fun readDocumentation(context: Context, fileName: String): String {
        val filePath = "$AGENTS_FOLDER/$fileName"
        return context.assets.open(filePath).bufferedReader().use { it.readText() }
    }

    /**
     * Read every bundled documentation file and return them keyed by relative path.
     */
    @Throws(IOException::class)
    fun readAllDocumentation(context: Context): Map<String, String> =
        availableDocuments.associateWith { readDocumentation(context, it) }

    /**
     * Build a single markdown bundle that can be pasted into Copilot or another AI assistant.
     *
     * Consumers can persist or copy this text into their own repository guidance files if they
     * want the assistant to use it consistently.
     */
    @Throws(IOException::class)
    fun buildCopilotContext(context: Context): String = buildString {
        appendLine("# AndroidCore bundled agent documentation")
        appendLine()
        appendLine("This content was generated from AndroidCore bundled assets.")
        appendLine("Use it as explicit prompt context or copy the relevant parts into the consuming repository.")

        availableDocuments.forEach { document ->
            appendLine()
            appendLine("---")
            appendLine()
            appendLine("## Document: $document")
            appendLine()
            appendLine(readDocumentation(context, document))
        }
    }

    /**
     * Get the top-level introduction guide.
     */
    @Throws(IOException::class)
    fun getIntroduction(context: Context): String =
        readDocumentation(context, INTRODUCTION)

    /**
     * Get the system vision overview.
     */
    @Throws(IOException::class)
    fun getSystemVision(context: Context): String =
        readDocumentation(context, SYSTEM_VISION)

    /**
     * Get the clean architecture overview.
     */
    @Throws(IOException::class)
    fun getCleanArchitectureOverview(context: Context): String =
        readDocumentation(context, CLEAN_ARCHITECTURE)

    /**
     * Get the MVI overview.
     */
    @Throws(IOException::class)
    fun getMviOverview(context: Context): String =
        readDocumentation(context, MVI_OVERVIEW)

    /**
     * Get an overview of how agents fit into the system.
     */
    @Throws(IOException::class)
    fun getAgentsSpecification(context: Context): String =
        readDocumentation(context, HOW_AGENTS_WORK)

    /**
     * Get the guide that explains how consumers can read and reuse the bundled docs.
     */
    @Throws(IOException::class)
    fun getConsumerUsageGuide(context: Context): String =
        readDocumentation(context, USING_AGENT_DOCUMENTATION)

    /**
     * Get the feature creation guide.
     */
    @Throws(IOException::class)
    fun getExecutionGuide(context: Context): String =
        readDocumentation(context, HOW_TO_CREATE_FEATURE)

    /**
     * Get the top-level introduction guide.
     */
    @Throws(IOException::class)
    fun getQuickIndex(context: Context): String =
        readDocumentation(context, INTRODUCTION)

    /**
     * Get the end-to-end example.
     */
    @Throws(IOException::class)
    fun getEndToEndExample(context: Context): String =
        readDocumentation(context, END_TO_END_EXAMPLE)

    /**
     * Legacy alias kept for compatibility; returns the end-to-end feature example.
     */
    @Deprecated("Use getEndToEndExample(context)")
    @Throws(IOException::class)
    fun getTemplates(context: Context): String =
        getEndToEndExample(context)

    /**
     * Legacy alias kept for compatibility; returns the clean architecture overview.
     */
    @Deprecated("Use getCleanArchitectureOverview(context)")
    @Throws(IOException::class)
    fun getIntegrationPoints(context: Context): String =
        getCleanArchitectureOverview(context)
}

