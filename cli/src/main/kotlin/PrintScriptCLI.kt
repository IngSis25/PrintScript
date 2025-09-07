import registry.CommandRegistry
import services.DefaultFileService
import services.DefaultOutputService

/**
 * Main CLI class - SUPER SIMPLE to demonstrate Builder pattern
 */
class PrintScriptCLI {
    private val commandRegistry = CommandRegistry()
    private val fileService = DefaultFileService()
    private val outputService = DefaultOutputService()

    companion object {
        const val VERSION = "1.0.0"
    }

    fun run(args: Array<String>) {
        try {
            when {
                args.isEmpty() -> showHelp()
                args[0] == "--help" || args[0] == "-h" -> showHelp()
                args[0] == "--version" || args[0] == "-v" -> showVersion()
                args.size < 2 -> {
                    outputService.printError("Missing required arguments")
                    showUsage()
                }
                else -> executeCommand(args)
            }
        } catch (e: Exception) {
            outputService.printError("Unexpected error: ${e.message}")
        }
    }

    private fun executeCommand(args: Array<String>) {
        val commandName = args[0]
        val filePath = args[1]
        val version = if (args.size > 2) args[2] else "1.0"

        try {
            outputService.println("PrintScript CLI v$VERSION")
            outputService.println("Command: $commandName | File: $filePath | Version: $version")
            outputService.println("=".repeat(50))

            val command =
                commandRegistry.createCommand(
                    commandName = commandName,
                    filePath = filePath,
                    version = version,
                    fileService = fileService,
                    outputService = outputService,
                )

            val result = command.execute()

            outputService.println("=".repeat(50))
            if (result.success) {
                outputService.printSuccess("Operation completed successfully")
            } else {
                outputService.printError("Operation failed: ${result.message}")
                System.exit(1)
            }
        } catch (e: IllegalArgumentException) {
            outputService.printError(e.message ?: "Invalid command")
            showAvailableCommands()
            System.exit(1)
        }
    }

    private fun showHelp() {
        outputService.println(
            """
            PrintScript CLI v$VERSION - Builder Pattern Demo
            
            USAGE:
                printscript <command> <file> [version]
            
            EXAMPLES:
                printscript validate example.ps
                printscript execute example.ps 1.0
                printscript format example.ps
                printscript analyze example.ps
                
            OPTIONS:
                -h, --help     Show this help
                -v, --version  Show version
            """.trimIndent(),
        )
        showAvailableCommands()
    }

    private fun showAvailableCommands() {
        outputService.println("\nAVAILABLE COMMANDS:")
        commandRegistry.getMainCommands().forEach { command ->
            outputService.println("  • $command")
        }
        outputService.println("\n✨ Adding new commands is SUPER EASY with builders!")
    }

    private fun showVersion() {
        outputService.println("PrintScript CLI v$VERSION")
        outputService.println("Using Builder Pattern for extensibility! 🚀")
    }

    private fun showUsage() {
        outputService.println("Usage: printscript <command> <file> [version]")
        outputService.println("Use --help for more information")
    }
}
