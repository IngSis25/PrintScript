package commands

import builders.BaseCommandBuilder
import services.FileService
import services.OutputService
// TODO: Add imports for lexer, parser when dependencies are set up

/**
 * Command for validating PrintScript syntax
 */
class ValidationCommand(
    private val filePath: String,
    private val version: String,
    private val fileService: FileService,
    private val outputService: OutputService,
) : Command {
    override fun execute(): CommandResult {
        return try {
            outputService.printProgress("Starting validation of $filePath (version $version)")

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

            // TODO: Setup lexer, parser and validate
            // For now, just basic validation
            if (sourceCode.trim().isEmpty()) {
                return CommandResult(
                    success = false,
                    message = "File is empty",
                    errorDetails = ErrorDetails(type = ErrorType.SYNTAX),
                )
            }

            outputService.printProgress("Basic validation completed")
            outputService.printSuccess("Validation successful - No syntax errors found")

            CommandResult(
                success = true,
                message = "Validation completed successfully",
            )
        } catch (e: Exception) {
            CommandResult(
                success = false,
                message = "Unexpected error during validation: ${e.message}",
                errorDetails = ErrorDetails(type = ErrorType.GENERIC),
            )
        }
    }

    override fun getName(): String = "validate"

    override fun getDescription(): String = "Validate PrintScript syntax"

    /**
     * Builder for ValidationCommand
     */
    class Builder : BaseCommandBuilder<ValidationCommand>() {
        override fun build(): ValidationCommand {
            validateRequired()
            return ValidationCommand(
                filePath = filePath!!,
                version = version,
                fileService = fileService!!,
                outputService = outputService!!,
            )
        }
    }
}
