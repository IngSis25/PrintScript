package commands

/**
 * Base interface for all CLI commands
 */
interface Command {
    fun execute(): CommandResult

    fun getName(): String

    fun getDescription(): String
}

/**
 * Result of a command execution
 */
data class CommandResult(
    val success: Boolean,
    val message: String = "",
    val errorDetails: ErrorDetails? = null,
)

/**
 * Error details with location information
 */
data class ErrorDetails(
    val line: Int? = null,
    val column: Int? = null,
    val type: ErrorType = ErrorType.GENERIC,
)

enum class ErrorType {
    LEXICAL,
    SYNTAX,
    RUNTIME,
    FILE_NOT_FOUND,
    GENERIC,
}
