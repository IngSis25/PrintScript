package main.kotlin.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file

class ExecutionCommand :
    CliktCommand(
        name = "execution",
        help = "Execute a PrintScript file",
    ) {
    private val sourceFile by argument(
        name = "SOURCE_FILE",
        help = "Path to the PrintScript source file to execute",
    ).file(mustExist = true, canBeFile = true, canBeDir = false)

    private val version by option(
        "--version",
        "-v",
        help = "Version of PrintScript to use (default: 1.0)",
    ).default("1.0")

    override fun run() {
        echo("Executing PrintScript file: ${sourceFile.name}")
        echo("Version: $version")
        echo()

        val cli = PrintScriptCLI()
        val result =
            cli.execute(sourceFile, version) { message ->
                if (message.startsWith("📤") || message.startsWith("─")) {
                    // Don't echo progress messages during execution output
                    return@execute
                }
                echo(message)
            }

        when (result) {
            is ExecutionResult.Success -> {
                echo("Summary:")
                echo("File: ${result.file.absolutePath}")
                echo("Version: ${result.version}")
                echo("Tokens: ${result.tokenCount}")
                echo("Statements: ${result.statementCount}")
                echo("Status:EXECUTED")
            }
            is ExecutionResult.Error -> {
                echo("Error: ${result.error.message}")

                // Try to provide more detailed error information
                when (result.error) {
                    is IllegalArgumentException -> {
                        echo("This appears to be a syntax error.")
                        echo("Check your PrintScript syntax and try again.")
                    }
                    else -> {
                        echo("This appears to be a runtime error.")
                        echo("Please check your code logic and try again.")
                    }
                }

                echo()
                echo("For help, run: printscript execution --help")
            }
        }
    }
}
