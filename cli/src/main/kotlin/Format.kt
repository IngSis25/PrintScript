package org

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import main.kotlin.lexer.LexerFactory
import org.example.formatter.Formatter
import org.example.formatter.RulesFactory
import java.io.File
import java.io.StringReader

class Format : CliktCommand() {
    private val filePath by argument(help = "Path to the script file to execute")
    private val rulePath by argument(help = "Path to the rule json to use")
    private val version by option(
        "--version",
        "-v",
        help = "PrintScript version to use (1.0 or 1.1)",
    ).choice("1.0", "1.1", ignoreCase = true).default("1.1")

    override fun run() {
        val code = File(filePath).readText()
        val reader = StringReader(code)
        val rulesContent = File(rulePath).readText()
        val rules = Json.parseToJsonElement(rulesContent).jsonObject

        echo("Lexing...\n", trailingNewline = true)
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

        echo("Formatting...\n", trailingNewline = true)
        val formatRules = RulesFactory().getRules(rules.toString(), version)
        val formatResult = Formatter(parser).format(formatRules)
        File(filePath).writeText(formatResult.code)

        echo("Format successful")
    }
}
