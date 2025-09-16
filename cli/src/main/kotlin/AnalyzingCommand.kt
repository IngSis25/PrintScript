package main.kotlin.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file

class AnalyzingCommand :
    CliktCommand(
        name = "analyzing",
        help = "Analyze a PrintScript file for code quality and style issues",
    ) {
    private val sourceFile by argument(
        name = "SOURCE_FILE",
        help = "Path to the PrintScript source file to analyze",
    ).file(mustExist = true, canBeFile = true, canBeDir = false)

    private val configFile by option(
        "--config",
        "-c",
        help = "Path to the analyzer configuration file (JSON format)",
    ).file(mustExist = true, canBeFile = true, canBeDir = false)

    private val version by option(
        "--version",
        "-v",
        help = "Version of PrintScript to use (default: 1.0)",
    ).default("1.0")

    override fun run() {
        echo("Analyzing PrintScript file: ${sourceFile.name}")
        echo("Version: $version")
        echo("Config: ${configFile?.name ?: "default"}")
        echo()

        val cli = PrintScriptCLI()
        val result =
            cli.analyze(sourceFile, configFile, version) { message ->
                echo(message)
            }

        when (result) {
            is AnalysisResult.Success -> {
                // Display results
                echo("Analysis Results:")
                echo("─".repeat(50))

                if (result.analysisResult.diagnostics.isEmpty()) {
                    echo("No issues found! Your code looks great!")
                } else {
                    result.analysisResult.diagnostics.forEach { diagnostic ->
                        val icon =
                            when (diagnostic.severity) {
                                main.kotlin.analyzer.DiagnosticSeverity.ERROR -> "❌"
                                main.kotlin.analyzer.DiagnosticSeverity.WARNING -> "⚠️"
                                main.kotlin.analyzer.DiagnosticSeverity.INFO -> "ℹ️"
                            }

                        echo("$icon [${diagnostic.severity}] ${diagnostic.code}")
                        echo("${diagnostic.position}")
                        echo("${diagnostic.message}")

                        if (diagnostic.suggestions.isNotEmpty()) {
                            echo("Suggestions:")
                            diagnostic.suggestions.forEach { suggestion ->
                                echo("   $suggestion")
                            }
                        }
                        echo()
                    }
                }

                echo("─".repeat(50))
                echo()
                echo("Summary:")
                echo("File: ${result.file.absolutePath}")
                echo("Version: ${result.version}")
                echo("Config: ${result.configFile.absolutePath}")
                echo("Tokens: ${result.tokenCount}")
                echo("Statements: ${result.statementCount}")
                echo("Errors: ${result.analysisResult.errorCount}")
                echo("Warnings: ${result.analysisResult.warningCount}")
                echo("Status: ${if (result.analysisResult.success) "PASSED" else "FAILED"}")
            }
            is AnalysisResult.Error -> {
                echo("Error: ${result.error.message}")

                // Try to provide more detailed error information
                when (result.error) {
                    is IllegalArgumentException -> {
                        echo("This appears to be a syntax error.")
                        echo("Check your PrintScript syntax and try again.")
                    }
                    else -> {
                        echo("This appears to be an analysis error.")
                        echo("Please check your configuration file and try again.")
                    }
                }

                echo()
                echo("For help, run: printscript analyzing --help")
            }
        }
    }
}
