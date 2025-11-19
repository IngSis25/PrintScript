package main.kotlin.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import main.kotlin.analyzer.AnalyzerConfig
import main.kotlin.analyzer.AnalyzerFactory
import main.kotlin.analyzer.ConfigLoader
import main.kotlin.analyzer.DiagnosticSeverity
import main.kotlin.lexer.LexerFactory
import org.ParserFactory
import java.io.File
import java.io.StringReader

class Validate : CliktCommand() {
    private val filePath by argument(help = "Path to the script file to execute")
    private val version by option(
        "--version",
        "-v",
        help = "PrintScript version to use (1.0 or 1.1)",
    ).choice("1.0", "1.1", ignoreCase = true).default("1.1")
    private val configPath by option(
        "--config",
        help = "Path to analyzer configuration JSON (defaults to analyzer-config.json if present)",
    )

    override fun run() {
        try {
            val file = File(filePath)
            if (!file.exists()) {
                echo("Error: File not found: $filePath", err = true)
                return
            }

            val code = file.readText()
            val reader = StringReader(code)

            echo("Lexing...\n", trailingNewline = true)
            echo("Parsing...\n", trailingNewline = true)
            val parser = createParser(version, reader)
            val nodes = parser.collectAllASTNodes()

            echo("Analyzing...\n", trailingNewline = true)
            val analyzerParser = createParser(version, StringReader(code))
            val analyzer = createAnalyzer(version, analyzerParser)
            val analyzerConfig = loadAnalyzerConfig(configPath)
            val result = analyzer.analyze(nodes, analyzerConfig, version)

            if (result.diagnostics.isNotEmpty()) {
                result.diagnostics.forEach { diagnostic ->
                    val output =
                        "${diagnostic.severity}: ${diagnostic.message} " +
                            "(code=${diagnostic.code}, position=${diagnostic.position})"
                    if (diagnostic.severity == DiagnosticSeverity.ERROR) {
                        echo(output, err = true)
                    } else {
                        echo(output)
                    }
                }
            }

            if (result.success) {
                echo("Validation completed")
            } else {
                echo("Validation failed", err = true)
            }
        } catch (e: Exception) {
            echo("Error: ${e.message}", err = true)
            throw e
        }
    }

    private fun createParser(
        version: String,
        reader: StringReader,
    ) = when (version) {
        "1.0" -> ParserFactory.createParserV10(LexerFactory.createLexerV10(reader))
        "1.1" -> ParserFactory.createParserV11(LexerFactory.createLexerV11(reader))
        else -> throw IllegalArgumentException("Unsupported version: $version")
    }

    private fun createAnalyzer(
        version: String,
        parser: org.example.iterator.PrintScriptIterator<org.example.astnode.ASTNode>,
    ) = when (version) {
        "1.0" -> AnalyzerFactory().createAnalyzerV10(parser)
        "1.1" -> AnalyzerFactory().createAnalyzerV11(parser)
        else -> throw IllegalArgumentException("Unsupported version: $version")
    }

    private fun loadAnalyzerConfig(path: String?): AnalyzerConfig {
        val file =
            path?.let { File(it) }
                ?: File("analyzer-config.json").takeIf { it.exists() }
                ?: return AnalyzerConfig()

        val rawJson = file.readText()
        val jsonObject: JsonObject =
            try {
                JsonParser.parseString(rawJson).asJsonObject
            } catch (e: Exception) {
                throw IllegalArgumentException("Invalid analyzer config JSON: ${e.message}")
            }

        return try {
            ConfigLoader.loadFromJsonString(rawJson).copy(jsonConfig = jsonObject)
        } catch (_: Exception) {
            AnalyzerConfig(jsonConfig = jsonObject)
        }
    }
}
