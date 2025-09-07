package commands

import builders.BaseCommandBuilder
import services.FileService
import services.OutputService

/**
 * Command for analyzing PrintScript code (DUMMY implementation)
 */
class AnalyzingCommand(
    private val filePath: String,
    private val version: String,
    private val fileService: FileService,
    private val outputService: OutputService,
) : Command {
    override fun execute(): CommandResult {
        return try {
            outputService.printProgress("Starting analysis of $filePath (version $version)")

            // Check if file exists
            if (!fileService.exists(filePath)) {
                return CommandResult(
                    success = false,
                    message = "File not found: $filePath",
                    errorDetails = ErrorDetails(type = ErrorType.FILE_NOT_FOUND),
                )
            }

            // Read source code
            val sourceCode = fileService.readFile(filePath)
            outputService.printProgress("File read successfully")

            // DUMMY IMPLEMENTATION - Basic "analysis"
            outputService.printProgress("Running code analysis...")

            val lines = sourceCode.lines()
            val nonEmptyLines = lines.filter { it.trim().isNotEmpty() }
            val commentLines = lines.filter { it.trim().startsWith("//") }

            outputService.println("=== ANALYSIS REPORT ===")
            outputService.println("Total lines: ${lines.size}")
            outputService.println("Non-empty lines: ${nonEmptyLines.size}")
            outputService.println("Comment lines: ${commentLines.size}")
            outputService.println("Code lines: ${nonEmptyLines.size - commentLines.size}")
            // TODO: Add real static analysis (unused variables, code complexity, etc.)
            outputService.println("=== END ANALYSIS ===")

            outputService.printSuccess("Analysis completed (using dummy implementation)")
            outputService.println("⚠️  Note: Real analyzer not implemented yet")

            CommandResult(
                success = true,
                message = "Analysis completed successfully (dummy implementation)",
            )
        } catch (e: Exception) {
            CommandResult(
                success = false,
                message = "Error during analysis: ${e.message}",
                errorDetails = ErrorDetails(type = ErrorType.GENERIC),
            )
        }
    }

    override fun getName(): String = "analyze"

    override fun getDescription(): String = "Analyze PrintScript code (TODO: not implemented)"

    /**
     * Builder for AnalyzingCommand
     */
    class Builder : BaseCommandBuilder<AnalyzingCommand>() {
        override fun build(): AnalyzingCommand {
            validateRequired()
            return AnalyzingCommand(
                filePath = filePath!!,
                version = version,
                fileService = fileService!!,
                outputService = outputService!!,
            )
        }
    }
}
