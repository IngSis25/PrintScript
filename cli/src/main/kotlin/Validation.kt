package main.kotlin.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import main.kotlin.lexer.LexerFactory
import org.ParserFactory
import java.io.File
import java.io.StringReader

class Validation : CliktCommand() {
    private val filePath by argument(help = "Path to the script file to execute")
    private val version by option(
        "--version",
        "-v",
        help = "PrintScript version to use (1.0 or 1.1)",
    ).choice("1.0", "1.1", ignoreCase = true).default("1.1")

    override fun run() {
        try {
            val file = File(filePath)
            if (!file.exists()) {
                echo("Error: File not found: $filePath", err = true)
                return
            }

            val code = file.readText()
            val reader = StringReader(code)

            echo("Analyzing...\n", trailingNewline = true)
            val lexer =
                when (version) {
                    "1.0" -> LexerFactory.createLexerV10(reader)
                    "1.1" -> LexerFactory.createLexerV11(reader)
                    else -> throw IllegalArgumentException("Unsupported version: $version")
                }

            echo("Parsing...\n", trailingNewline = true)
            val parser =
                when (version) {
                    "1.0" -> ParserFactory.createParserV10(lexer)
                    "1.1" -> ParserFactory.createParserV11(lexer)
                    else -> throw IllegalArgumentException("Unsupported version: $version")
                }
            parser.collectAllASTNodes()

            echo("Validation successful")
        } catch (e: Exception) {
            echo("Error: ${e.message}", err = true)
            throw e
        }
    }
}
