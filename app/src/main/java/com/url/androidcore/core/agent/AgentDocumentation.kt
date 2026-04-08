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
 * val docs = AgentDocumentation(context)
 * val agentSpec = docs.readAgent("AGENTS.md")
 * ```
 */
object AgentDocumentation {

    private const val AGENTS_FOLDER = "agents"

    /**
     * List of available agent documentation files.
     */
    val availableDocuments = listOf(
        "AGENTS.md",
        "AGENT_DESCRIPTOR_VERSIONING.md",
        "AGENT_EXECUTION_GUIDE.md",
        "AGENT_INTEGRATION_POINTS.md",
        "AGENT_QUICK_INDEX.md",
        "AGENT_SYSTEM_READY.md",
        "AGENT_SYSTEM_SETUP.md",
        "AGENT_TEMPLATES.md",
        "AGENT_VISUAL_WORKFLOW.md",
        "CORE_UTILITIES.md",
        "EXTENDED_IMPLEMENTATION_SUMMARY.md",
        "EXTENDED_UTILITIES.md",
        "IMPLEMENTATION_CHECKLIST.md",
        "IMPLEMENTATION_SUMMARY.md",
        "INDEX.md",
        "INTEGRATION_EXAMPLES.md",
        "QUICK_REFERENCE.md",
        "README_AGENTS.md",
        "README_EXTENDED_CORE.md"
    )

    /**
     * Read a specific agent documentation file.
     *
     * @param context Android context for accessing assets
     * @param fileName Name of the documentation file (e.g., "AGENTS.md")
     * @return Content of the documentation file as String
     * @throws IOException if file is not found or cannot be read
     */
    @Throws(IOException::class)
    fun readDocumentation(context: Context, fileName: String): String {
        val filePath = "$AGENTS_FOLDER/$fileName"
        return context.assets.open(filePath).bufferedReader().use { it.readText() }
    }

    /**
     * Get the main agents specification.
     */
    @Throws(IOException::class)
    fun getAgentsSpecification(context: Context): String =
        readDocumentation(context, "AGENTS.md")

    /**
     * Get the execution guide for using agents.
     */
    @Throws(IOException::class)
    fun getExecutionGuide(context: Context): String =
        readDocumentation(context, "AGENT_EXECUTION_GUIDE.md")

    /**
     * Get the quick index/navigation guide.
     */
    @Throws(IOException::class)
    fun getQuickIndex(context: Context): String =
        readDocumentation(context, "AGENT_QUICK_INDEX.md")

    /**
     * Get the code templates for agents.
     */
    @Throws(IOException::class)
    fun getTemplates(context: Context): String =
        readDocumentation(context, "AGENT_TEMPLATES.md")

    /**
     * Get the integration points documentation.
     */
    @Throws(IOException::class)
    fun getIntegrationPoints(context: Context): String =
        readDocumentation(context, "AGENT_INTEGRATION_POINTS.md")
}

