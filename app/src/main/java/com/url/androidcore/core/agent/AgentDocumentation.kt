package com.url.androidcore.core.agent

import android.content.Context
import java.io.IOException

/**
 * Helper class to access agent documentation and specifications.
 *
 * Agent documentation is bundled with AndroidCore library and can be read
 * by any project that imports this library.
 *
 * Usage:
 * ```
 * val intro = AgentDocumentation.readDocumentation(context, "overview/introduction.md")
 * val featureGuide = AgentDocumentation.getExecutionGuide(context)
 * ```
 */
object AgentDocumentation {

    private const val AGENTS_FOLDER = "agents"

    /**
     * List of available agent documentation files.
     */
    val availableDocuments = listOf(
        "overview/introduction.md",
        "overview/system-vision.md",
        "architecture/clean-architecture.md",
        "architecture/mvi-overview.md",
        "guides/how-to-create-feature.md",
        "guides/how-agents-work.md",
        "examples/end-to-end-example.md"
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
     * Get an overview of how agents fit into the system.
     */
    @Throws(IOException::class)
    fun getAgentsSpecification(context: Context): String =
        readDocumentation(context, "guides/how-agents-work.md")

    /**
     * Get the feature creation guide.
     */
    @Throws(IOException::class)
    fun getExecutionGuide(context: Context): String =
        readDocumentation(context, "guides/how-to-create-feature.md")

    /**
     * Get the top-level introduction guide.
     */
    @Throws(IOException::class)
    fun getQuickIndex(context: Context): String =
        readDocumentation(context, "overview/introduction.md")

    /**
     * Legacy alias kept for compatibility; returns the end-to-end feature example.
     */
    @Throws(IOException::class)
    fun getTemplates(context: Context): String =
        readDocumentation(context, "examples/end-to-end-example.md")

    /**
     * Legacy alias kept for compatibility; returns the clean architecture overview.
     */
    @Throws(IOException::class)
    fun getIntegrationPoints(context: Context): String =
        readDocumentation(context, "architecture/clean-architecture.md")
}

