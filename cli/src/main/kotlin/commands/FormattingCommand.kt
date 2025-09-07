package commands

import builders.BaseCommandBuilder
import services.FileService
import services.OutputService

/**
 * Command for formatting PrintScript code (DUMMY implementation)
 */
class FormattingCommand(
    private val filePath: String,
    private val version: String,
    private val fileService: FileService,
    private val outputService: OutputService,
) : Command {
    override fun execute(): CommandResult {
        return try {
            outputService.printProgress("Starting formatting of $filePath (version $version)")

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

            // DUMMY IMPLEMENTATION - Just echo the content for now
            outputService.printProgress("Applying formatting rules...")
            outputService.println("=== FORMATTED CODE ===")
            outputService.println(sourceCode) // TODO: Apply real formatting
            outputService.println("=== END FORMATTED CODE ===")

            outputService.printSuccess("Formatting completed (using dummy implementation)")
            outputService.println("⚠️  Note: Real formatter not implemented yet")

            CommandResult(
                success = true,
                message = "Formatting completed successfully (dummy implementation)",
            )
        } catch (e: Exception) {
            CommandResult(
                success = false,
                message = "Error during formatting: ${e.message}",
                errorDetails = ErrorDetails(type = ErrorType.GENERIC),
            )
        }
    }

    override fun getName(): String = "format"

    override fun getDescription(): String = "Format PrintScript code (TODO: not implemented)"

    /**
     * Builder for FormattingCommand
     */
    class Builder : BaseCommandBuilder<FormattingCommand>() {
        override fun build(): FormattingCommand {
            validateRequired()
            return FormattingCommand(
                filePath = filePath!!,
                version = version,
                fileService = fileService!!,
                outputService = outputService!!,
            )
        }
    }
}
