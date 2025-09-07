package registry

import builders.CommandBuilder
import commands.*
import services.FileService
import services.OutputService

/**
 * Registry for managing all available commands
 * This makes it super easy to add new commands!
 */
class CommandRegistry {
    private val commandBuilders = mutableMapOf<String, () -> CommandBuilder<out Command>>()

    init {
        // Register all available commands
        registerCommand("validate") { ValidationCommand.Builder() }
        registerCommand("execute") { ExecutionCommand.Builder() }
        registerCommand("format") { FormattingCommand.Builder() }
        registerCommand("analyze") { AnalyzingCommand.Builder() }

        // Aliases for convenience
        registerCommand("validation") { ValidationCommand.Builder() }
        registerCommand("execution") { ExecutionCommand.Builder() }
        registerCommand("run") { ExecutionCommand.Builder() }
        registerCommand("formatting") { FormattingCommand.Builder() }
        registerCommand("analyzing") { AnalyzingCommand.Builder() }
        registerCommand("analysis") { AnalyzingCommand.Builder() }
    }

    /**
     * Register a new command - SUPER EXPANDIBLE!
     */
    fun registerCommand(
        name: String,
        builderFactory: () -> CommandBuilder<out Command>,
    ) {
        commandBuilders[name.lowercase()] = builderFactory
    }

    /**
     * Create a command using the builder pattern
     */
    fun createCommand(
        commandName: String,
        filePath: String,
        version: String = "1.0",
        fileService: FileService,
        outputService: OutputService,
    ): Command {
        val builderFactory =
            commandBuilders[commandName.lowercase()]
                ?: throw IllegalArgumentException("Unknown command: $commandName. Available: ${getAvailableCommands()}")

        return builderFactory()
            .withFile(filePath)
            .withVersion(version)
            .withFileService(fileService)
            .withOutputService(outputService)
            .build()
    }

    /**
     * Get all available command names
     */
    fun getAvailableCommands(): List<String> = commandBuilders.keys.toList().sorted()

    /**
     * Check if a command exists
     */
    fun hasCommand(commandName: String): Boolean = commandBuilders.containsKey(commandName.lowercase())

    /**
     * Get main commands (without aliases)
     */
    fun getMainCommands(): List<String> = listOf("validate", "execute", "format", "analyze")
}
