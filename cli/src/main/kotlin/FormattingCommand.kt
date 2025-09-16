package main.kotlin.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file

class FormattingCommand :
    CliktCommand(
        name = "formatting",
        help = "Format a PrintScript file according to configuration rules",
    ) {
    private val sourceFile by argument(
        name = "SOURCE_FILE",
        help = "Path to the PrintScript source file to format",
    ).file(mustExist = true, canBeFile = true, canBeDir = false)

    private val configFile by option(
        "--config",
        "-c",
        help = "Path to the formatting configuration file (JSON format)",
    ).file(mustExist = true, canBeFile = true, canBeDir = false)

    private val outputFile by option(
        "--output",
        "-o",
        help = "Path to write the formatted output (default: stdout)",
    ).file(canBeFile = true, canBeDir = false)

    private val version by option(
        "--version",
        "-v",
        help = "Version of PrintScript to use (default: 1.0)",
    ).default("1.0")

    override fun run() {
        echo("Formatting PrintScript file: ${sourceFile.name}")
        echo("Version: $version")
        echo("Config: ${configFile?.name ?: "default"}")
        echo()

        val cli = PrintScriptCLI()
        val result =
            cli.format(sourceFile, configFile, outputFile, version) { message ->
                echo(message)
            }

        when (result) {
            is FormattingResult.Success -> {
                // Output result
                if (result.outputFile != null) {
                    echo(" Formatted code written to: ${result.outputFile!!.absolutePath}")
                } else {
                    echo(" Formatted code:")
                    echo("".repeat(50))
                    echo(result.formattedCode)
                    echo("".repeat(50))
                }

                echo()
                echo(" Summary:")
                echo("File: ${result.file.absolutePath}")
                echo("Version: ${result.version}")
                echo("Config: ${result.configFile.absolutePath}")
                echo("Tokens: ${result.tokenCount}")
                echo("Statements: ${result.statementCount}")
                echo("Status:FORMATTED")
            }
            is FormattingResult.Error -> {
                echo("Error: ${result.error.message}")

                // Try to provide more detailed error information
                when (result.error) {
                    is IllegalArgumentException -> {
                        echo("This appears to be a syntax error.")
                        echo("Check your PrintScript syntax and try again.")
                    }
                    else -> {
                        echo("This appears to be a formatting error.")
                        echo("Please check your configuration file and try again.")
                    }
                }

                echo()
                echo("For help, run: printscript formatting --help")
            }
        }
    }
}
