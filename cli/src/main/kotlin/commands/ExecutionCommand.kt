package commands

import builders.BaseCommandBuilder
import services.FileService
import services.OutputService

/**
 * Command for executing PrintScript code
 */
class ExecutionCommand(
    private val filePath: String,
    private val version: String,
    private val fileService: FileService,
    private val outputService: OutputService,
) : Command {
    override fun execute(): CommandResult {
        return try {
            outputService.printProgress("Starting execution of $filePath (version $version)")

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

            // TODO: Setup lexer, parser, interpreter and execute
            // For now, just echo the content
            outputService.printProgress("Starting execution...")
            outputService.println("=== PROGRAM OUTPUT ===")
            outputService.println("// TODO: Real execution not implemented yet")
            outputService.println("// Source code:")
            outputService.println(sourceCode)
            outputService.println("=== END OUTPUT ===")

            outputService.printSuccess("Execution completed successfully (dummy implementation)")

            CommandResult(
                success = true,
                message = "Execution completed successfully",
            )
        } catch (e: Exception) {
            CommandResult(
                success = false,
                message = "Unexpected error during execution: ${e.message}",
                errorDetails = ErrorDetails(type = ErrorType.GENERIC),
            )
        }
    }

    override fun getName(): String = "execute"

    override fun getDescription(): String = "Execute PrintScript code"

    /**
     * Builder for ExecutionCommand
     */
    class Builder : BaseCommandBuilder<ExecutionCommand>() {
        override fun build(): ExecutionCommand {
            validateRequired()
            return ExecutionCommand(
                filePath = filePath!!,
                version = version,
                fileService = fileService!!,
                outputService = outputService!!,
            )
        }
    }
}
